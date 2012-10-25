package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.OperatingSystemMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperatingSystemMonitor implements Monitor {

    public static final String NAME = "Operating System";

    private static final Logger LOGGER = LoggerFactory.getLogger(OperatingSystemMonitor.class);

    private final JSONFactory jsonFactory;

    private final OperatingSystemMXBean operatingSystemMXBean;

    public OperatingSystemMonitor(final JSONFactory jsonFactory,
                                  final OperatingSystemMXBean operatingSystemMXBean) {
        LOGGER.debug("OperatingSystemMonitor({}, {}) - start", jsonFactory, operatingSystemMXBean);
        this.jsonFactory = jsonFactory;
        this.operatingSystemMXBean = operatingSystemMXBean;
        LOGGER.debug("OperatingSystemMonitor({}, {}) - end", jsonFactory, operatingSystemMXBean);
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
        final JSONObject jsonObject = jsonFactory.createJSONObject()
                                          .put("arch", operatingSystemMXBean.getArch())
                                          .put("availableProcessors", operatingSystemMXBean.getAvailableProcessors())
                                          .put("name", operatingSystemMXBean.getName())
                                          .put("systemLoadAverage", operatingSystemMXBean.getSystemLoadAverage())
                                          .put("version", operatingSystemMXBean.getVersion());
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

}
