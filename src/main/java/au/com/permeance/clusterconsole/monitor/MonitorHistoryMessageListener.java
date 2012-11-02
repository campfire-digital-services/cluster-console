package au.com.permeance.clusterconsole.monitor;

import au.com.permeance.clusterconsole.util.JSONObjectWithTimestampComparator;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.currentTimeMillis;

public class MonitorHistoryMessageListener extends BaseMessageListener implements MonitorHistory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorHistoryMessageListener.class);

    private final ConcurrentMap<String, ConcurrentMap<Long, JSONObject>> clusterHistory = new ConcurrentHashMap<String, ConcurrentMap<Long, JSONObject>>();

    private final String monitorName;

    private final int size;

    private final long ttl;

    public MonitorHistoryMessageListener(final String monitorName,
                                         final int size,
                                         final long ttl) {
        LOGGER.debug("MonitorHistoryMessageListener({}, {}, {}) - start", new Object[]{monitorName, size, ttl});
        this.monitorName = monitorName;
        this.size = size;
        this.ttl = ttl;
        LOGGER.debug("MonitorHistoryMessageListener({}, {}, {}) - end", new Object[]{monitorName, size, ttl});
    }

    @Override
    protected void doReceive(final Message message) {
        LOGGER.debug("doReceive({}) - start", message);
        final JSONObject jsonObject = getPayloadFromMessageStatus(message);
        if (jsonObject != null) {
            if (monitorName.equals(jsonObject.getJSONObject("monitor")
                                             .getString("name", ""))) {

                final String clusterNodeId = jsonObject.getString("clusterNodeId");
                clusterHistory.putIfAbsent(clusterNodeId, new ConcurrentHashMap<Long, JSONObject>(size));
                final ConcurrentMap<Long, JSONObject> nodeHistory = clusterHistory.get(clusterNodeId);

                final long timestamp = jsonObject.getLong("timestamp");
                nodeHistory.put(timestamp, jsonObject);

            }
        }

        expireEntries();

        LOGGER.debug("doReceive({}) - end", message);
    }

    protected JSONObject getPayloadFromMessageStatus(final Message message) {
        LOGGER.debug("getPayloadFromMessageStatus({}) - start", message);
        if (message.getPayload() instanceof MessageStatus) {
            final MessageStatus messageStatus = (MessageStatus) message.getPayload();
            if (messageStatus.getPayload() instanceof JSONObject) {
                final JSONObject jsonObject = (JSONObject) messageStatus.getPayload();
                LOGGER.debug("getPayloadFromMessageStatus({}) - end - returning: {}", message, jsonObject);
                return jsonObject;
            }
        }
        LOGGER.debug("getPayloadFromMessageStatus({}) - end - returning: {}", message, null);
        return null;
    }

    @Override
    public SortedMap<String, List<JSONObject>> getHistory() {
        LOGGER.debug("getHistory() - start");
        final SortedMap<String, List<JSONObject>> sortedHistory = new TreeMap<String, List<JSONObject>>();
        for (final Map.Entry<String, ? extends Map<Long, JSONObject>> clusterHistoryEntry : clusterHistory.entrySet()) {

            final String key = clusterHistoryEntry.getKey();
            final List<JSONObject> value = new ArrayList<JSONObject>(clusterHistoryEntry.getValue().values());
            Collections.sort(value, JSONObjectWithTimestampComparator.INSTANCE);

            sortedHistory.put(key, value);
        }
        LOGGER.debug("getHistory() - end - returning: {}", sortedHistory);
        return sortedHistory;
    }

    @Override
    public void expireEntries() {
        LOGGER.debug("expireEntries() - start");
        final long now = currentTimeMillis();
        for (final String clusterNodeId : clusterHistory.keySet()) {
            final Map<Long, JSONObject> nodeHistory = clusterHistory.get(clusterNodeId);
            for (final long timestamp : nodeHistory.keySet()) {
                if (now - timestamp > ttl) {
                    nodeHistory.remove(timestamp);
                }
                if (nodeHistory.isEmpty()) {
                    clusterHistory.remove(clusterNodeId);
                }
            }
        }
        LOGGER.debug("expireEntries() - end");
    }

}
