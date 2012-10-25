package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryMonitor implements Monitor {

    public static final String NAME = "Memory";

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryMonitor.class);

    private final JSONFactory jsonFactory;

    private final MemoryMXBean memoryMXBean;

    public MemoryMonitor(final JSONFactory jsonFactory,
                         final MemoryMXBean memoryMXBean) {
        LOGGER.debug("MemoryMonitor({}, {}) - start", jsonFactory, memoryMXBean);
        this.jsonFactory = jsonFactory;
        this.memoryMXBean = memoryMXBean;
        LOGGER.debug("MemoryMonitor({}, {}) - end", jsonFactory, memoryMXBean);
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
        final MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        final MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        final JSONObject jsonObject = jsonFactory.createJSONObject()
                                          .put("heap", jsonFactory.createJSONObject()
                                                                  .put("committed", heapMemoryUsage.getCommitted())
                                                                  .put("init", heapMemoryUsage.getInit())
                                                                  .put("max", heapMemoryUsage.getMax())
                                                                  .put("used", heapMemoryUsage.getUsed()))
                                          .put("nonHeap", jsonFactory.createJSONObject()
                                                                     .put("committed", nonHeapMemoryUsage.getCommitted())
                                                                     .put("init", nonHeapMemoryUsage.getInit())
                                                                     .put("max", nonHeapMemoryUsage.getMax())
                                                                     .put("used", nonHeapMemoryUsage.getUsed()));
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

}
