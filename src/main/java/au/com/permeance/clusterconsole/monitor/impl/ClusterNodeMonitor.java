package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterNodeMonitor implements Monitor {

    public static final String NAME = "Cluster Node";

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterNodeMonitor.class);
    
    private static final String HOSTNAME_UNKNOWN = "UNKNOWN";

    private final ClusterNode clusterNode;

    private final JSONFactory jsonFactory;

    public ClusterNodeMonitor(final ClusterNode clusterNode,
                              final JSONFactory jsonFactory) {
        LOGGER.debug("ClusterNodeMonitor({}, {}) - start", clusterNode, jsonFactory);
        this.clusterNode = clusterNode;
        this.jsonFactory = jsonFactory;
        LOGGER.debug("ClusterNodeMonitor({}, {}) - end", clusterNode, jsonFactory);
    }

    @Override
    public String name() {
        LOGGER.debug("name() - start");
        LOGGER.debug("name() - end - returning: {}", NAME);
        return NAME;
    }

    @Override
    public JSONObject poll() {
        LOGGER.debug("poll() - start");
        final JSONObject jsonObject = jsonFactory.createJSONObject();
        if (clusterNode != null) {
        	String hostname = HOSTNAME_UNKNOWN;
        	InetAddress inetAddress = clusterNode.getInetAddress();
        	if (inetAddress != null) {
        		hostname = inetAddress.getHostName();
        	}
            jsonObject.put("hostName", hostname)
                      .put("clusterNodeId", clusterNode.getClusterNodeId())
                      .put("port", clusterNode.getPort())
                      .put("address", clusterNode.getInetAddress().toString());
        }
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

}
