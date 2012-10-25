package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.RuntimeMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeMonitor implements Monitor {

    public static final String NAME = "Runtime";

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeMonitor.class);

    private final JSONFactory jsonFactory;

    private final Runtime runtime;

    private final RuntimeMXBean runtimeMXBean;

    public RuntimeMonitor(final JSONFactory jsonFactory,
                          final Runtime runtime,
                          final RuntimeMXBean runtimeMXBean) {
        LOGGER.debug("RuntimeMonitor({}, {}, {}) - start", new Object[]{jsonFactory, runtime, runtimeMXBean});
        this.jsonFactory = jsonFactory;
        this.runtime = runtime;
        this.runtimeMXBean = runtimeMXBean;
        LOGGER.debug("RuntimeMonitor({}, {}, {}) - end", new Object[]{jsonFactory, runtime, runtimeMXBean});
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
                                          .put("availableProcessors", runtime.availableProcessors())
                                          .put("freeMemory", runtime.freeMemory())
                                          .put("maxMemory", runtime.maxMemory())
                                          .put("totalMemory", runtime.totalMemory())
                                          .put("startTime", runtimeMXBean.getStartTime())
                                          .put("uptime", runtimeMXBean.getUptime());
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

}
