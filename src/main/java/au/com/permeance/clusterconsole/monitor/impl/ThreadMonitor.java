package au.com.permeance.clusterconsole.monitor.impl;

import au.com.permeance.clusterconsole.monitor.Monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadMonitor implements Monitor {

    public static final String NAME = "Threads";

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadMonitor.class);

    private final JSONFactory jsonFactory;

    private final ThreadMXBean threadMXBean;

    public ThreadMonitor(final JSONFactory jsonFactory,
                         final ThreadMXBean threadMXBean) {
        LOGGER.debug("ThreadMonitor({}, {}) - start", jsonFactory, threadMXBean);
        this.jsonFactory = jsonFactory;
        this.threadMXBean = threadMXBean;
        LOGGER.debug("ThreadMonitor({}, {}) - end", jsonFactory, threadMXBean);
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
        final JSONObject threads = jsonFactory.createJSONObject();
        final long[] threadIds = threadMXBean.getAllThreadIds();
        for (final long threadId : threadIds) {
            threads.put(Long.toString(threadId), pollThread(threadId));
        }
        final JSONObject jsonObject = jsonFactory.createJSONObject()
                                          .put("daemonThreadCount", threadMXBean.getDaemonThreadCount())
                                          .put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount())
                                          .put("threadCount", threadMXBean.getThreadCount())
                                          .put("threads", threads);
        LOGGER.debug("poll() - end - returning: {}", jsonObject);
        return jsonObject;
    }

    protected JSONObject pollThread(final long threadId) {
        LOGGER.debug("pollThread({}) - start", threadId);
        final ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
        final JSONObject jsonObject = jsonFactory.createJSONObject()
                                          .put("cpuTime", threadMXBean.getThreadCpuTime(threadId))
                                          .put("userTime", threadMXBean.getThreadUserTime(threadId))
                                          .put("threadName", threadInfo.getThreadName())
                                          .put("threadState", threadInfo.getThreadState().toString());
        LOGGER.debug("pollThread({}) - end - returning: {}", threadId, jsonObject);
        return jsonObject;
    }

}
