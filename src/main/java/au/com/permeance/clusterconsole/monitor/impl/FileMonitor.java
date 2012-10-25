package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.util.Portal;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMonitor implements Monitor {

    public static final String NAME = "Files";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMonitor.class);

    private final JSONFactory jsonFactory;

    private final Portal portal;

    public FileMonitor(final JSONFactory jsonFactory,
                       final Portal portal) {
        LOGGER.debug("FileMonitor({}, {}) - start", jsonFactory, portal);
        this.jsonFactory = jsonFactory;
        this.portal = portal;
        LOGGER.debug("FileMonitor({}, {}) - end", jsonFactory, portal);
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
        final File globalLibDir = new File(portal.getGlobalLibDir());
        final File portalLibDir = new File(portal.getPortalLibDir());
        final File portalWebDir = new File(portal.getPortalWebDir());
        final JSONObject jsonObject = jsonFactory.createJSONObject()
                                          .put("globalLibDir", jsonFactory.createJSONObject()
                                                                          .put("freeSpace", globalLibDir.getFreeSpace())
                                                                          .put("path", globalLibDir.getPath())
                                                                          .put("totalSpace", globalLibDir.getTotalSpace())
                                                                          .put("usableSpace", globalLibDir.getUsableSpace()))
                                          .put("portalLibDir", jsonFactory.createJSONObject()
                                                                          .put("freeSpace", portalLibDir.getFreeSpace())
                                                                          .put("path", portalLibDir.getPath())
                                                                          .put("totalSpace", portalLibDir.getTotalSpace())
                                                                          .put("usableSpace", portalLibDir.getUsableSpace()))
                                          .put("portalWebDir", jsonFactory.createJSONObject()
                                                                          .put("freeSpace", portalWebDir.getFreeSpace())
                                                                          .put("path", portalWebDir.getPath())
                                                                          .put("totalSpace", portalWebDir.getTotalSpace())
                                                                          .put("usableSpace", portalWebDir.getUsableSpace()));
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

}
