package au.com.permeance.clusterconsole.monitor;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.unmodifiableList;

public class MonitorHistoryMessageListener extends BaseMessageListener implements MonitorHistory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorHistoryMessageListener.class);

    private final Queue<JSONObject> history = new ConcurrentLinkedQueue<JSONObject>();

    private final String monitorName;

    private final int size;

    public MonitorHistoryMessageListener(final String monitorName,
                                         final int size) {
        LOGGER.debug("MonitorHistoryMessageListener({}, {}) - start", monitorName, size);
        this.monitorName = monitorName;
        this.size = size;
        LOGGER.debug("MonitorHistoryMessageListener({}, {}) - end", monitorName, size);
    }

    @Override
    protected void doReceive(final Message message) {
        LOGGER.debug("doReceive({}) - start", message);
        if (message.getPayload() instanceof MessageStatus) {
            final MessageStatus messageStatus = (MessageStatus) message.getPayload();
            if (messageStatus.getPayload() instanceof JSONObject) {
                final JSONObject jsonObject = (JSONObject) messageStatus.getPayload();
                if (monitorName.equals(jsonObject.getJSONObject("monitor")
                                                 .getString("name", ""))) {
                    history.add(jsonObject);
                    if (history.size() > size) {
                        history.poll();
                    }
                }
            }
        }
        LOGGER.debug("doReceive({}) - end", message);
    }

    @Override
    public List<JSONObject> getHistory() {
        return unmodifiableList(new ArrayList<JSONObject>(history));
    }

    @Override
    public SortedMap<String, SortedMap<Date, JSONObject>> getCategorisedHistory() {
        LOGGER.debug("getCategorisedHistory() - start");
        final SortedMap<String, SortedMap<Date, JSONObject>> historyMap = new TreeMap<String, SortedMap<Date, JSONObject>>();
        for (final JSONObject historyJSONObject : history) {

            final String clusterNodeId = historyJSONObject.getString("clusterNodeId", "");
            if (clusterNodeId.isEmpty()) {
                LOGGER.warn("Skipping history item without a cluster node id: {}", historyJSONObject);
                continue;
            }

            final long timestamp = historyJSONObject.getLong("timestamp", -1L);
            if (timestamp < 0L) {
                LOGGER.warn("Skipping history item without a timestamp: {}", historyJSONObject);
                continue;
            }

            final JSONObject monitorJSONObject = historyJSONObject.getJSONObject("monitor");
            if (monitorJSONObject == null) {
                LOGGER.warn("Skipping history item without a monitor: {}", historyJSONObject);
                continue;
            }

            final JSONObject resultJSONObject = monitorJSONObject.getJSONObject("result");
            if (resultJSONObject == null) {
                LOGGER.warn("Skipping history item without a result: {}", historyJSONObject);
                continue;
            }

            if (!historyMap.containsKey(clusterNodeId)) {
                historyMap.put(clusterNodeId, new TreeMap<Date, JSONObject>());
            }
            final SortedMap<Date, JSONObject> dateMap = historyMap.get(clusterNodeId);

            dateMap.put(new Date(timestamp), resultJSONObject);

        }
        LOGGER.debug("getCategorisedHistory() - end - returning: {}", historyMap);
        return historyMap;
    }

}
