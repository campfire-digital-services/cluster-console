package au.com.permeance.clusterconsole.monitor;

import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageStatusMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageStatus;
import com.liferay.portal.kernel.util.InetAddressUtil;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.currentTimeMillis;

public class MonitorMessageListener extends BaseMessageStatusMessageListener {

    public static final String MONITOR_NAME = "MONITOR_NAME";

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorMessageListener.class);

    private final ClusterNode clusterNode;

    private final JSONFactory jsonFactory;

    private final Monitor monitor;

    public MonitorMessageListener(final ClusterNode clusterNode,
                                  final JSONFactory jsonFactory,
                                  final Monitor monitor) {
        LOGGER.debug("MonitorMessageListener({}, {}, {}) - start", new Object[]{clusterNode, jsonFactory, monitor});
        this.clusterNode = clusterNode;
        this.jsonFactory = jsonFactory;
        this.monitor = monitor;
        LOGGER.debug("MonitorMessageListener({}, {}, {}) - end", new Object[]{clusterNode, jsonFactory, monitor});
    }

    @Override
    public void receive(final Message message) {
        final String monitorName = message.getString(MONITOR_NAME);
        if (monitorName.equals(monitor.name())) {
            super.receive(message);
        }
    }

    @Override
    protected void doReceive(final Message message,
                             final MessageStatus messageStatus) {
        LOGGER.debug("doReceive({}, {}) - start", message, messageStatus);
        final JSONObject status = jsonFactory.createJSONObject()
                                             .put("clusterNodeId", clusterNode == null
                                                                   ? getHostName()
                                                                   : clusterNode.getClusterNodeId())
                                             .put("timestamp", currentTimeMillis())
                                             .put("monitor", jsonFactory.createJSONObject()
                                                                        .put("name", monitor.name())
                                                                        .put("result", monitor.poll()));
        messageStatus.setPayload(status);
        LOGGER.debug("doReceive({}, {}) - end", message, messageStatus);
    }

    protected String getHostName() {
        LOGGER.debug("getHostName() - start");

        InetAddress localInetAddress;
        try {
            localInetAddress = InetAddressUtil.getLocalInetAddress();
        }
        catch (final Exception e) {
            localInetAddress = null;
            LOGGER.warn("Error getting local address", e);
        }

        final String hostName = localInetAddress == null
                                ? "unknown host"
                                : localInetAddress.getCanonicalHostName();

        LOGGER.debug("getHostName() - end - returning: {}", hostName);
        return hostName;
    }

}
