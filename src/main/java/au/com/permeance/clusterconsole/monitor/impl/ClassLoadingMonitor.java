package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.ClassLoadingMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoadingMonitor implements Monitor {

    public static final String NAME = "Class Loading";

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoadingMonitor.class);

    private final ClassLoadingMXBean classLoadingMXBean;

    private final JSONFactory jsonFactory;

    public ClassLoadingMonitor(final ClassLoadingMXBean classLoadingMXBean,
                               final JSONFactory jsonFactory) {
        LOGGER.debug("ClassLoadingMonitor({}, {}) - start", classLoadingMXBean, jsonFactory);
        this.classLoadingMXBean = classLoadingMXBean;
        this.jsonFactory = jsonFactory;
        LOGGER.debug("ClassLoadingMonitor({}, {}) - end", classLoadingMXBean, jsonFactory);
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
                                          .put("loadedClassCount", classLoadingMXBean.getLoadedClassCount())
                                          .put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount())
                                          .put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

}
