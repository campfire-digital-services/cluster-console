package au.com.permeance.clusterconsole.scheduler;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngineUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.Trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static au.com.permeance.clusterconsole.monitor.MonitorMessageListener.MONITOR_NAME;
import static com.liferay.portal.kernel.scheduler.StorageType.MEMORY;
import static java.lang.String.format;

public class ClusterConsoleScheduleConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterConsoleScheduleConfigurator.class);

    private final String destinationName;

    private final List<Trigger> triggers;

    public ClusterConsoleScheduleConfigurator(final String destinationName,
                                              final List<Trigger> triggers) {
        LOGGER.debug("ClusterConsoleScheduleConfigurator({}, {}) - start", destinationName, triggers);
        this.destinationName = destinationName;
        this.triggers = triggers;
        LOGGER.debug("ClusterConsoleScheduleConfigurator({}, {}) - end", destinationName, triggers);
    }

    public void afterPropertiesSet() {
        LOGGER.debug("afterPropertiesSet() - start");
        for (final Trigger trigger : triggers) {
            scheduleEntry(trigger);
        }
        LOGGER.debug("afterPropertiesSet() - end");
    }

    protected void scheduleEntry(final Trigger trigger) {
        LOGGER.debug("scheduleEntry({}) - start", trigger);
        final Message message = new Message();
        message.put(MONITOR_NAME, trigger.getJobName());

        try {
            SchedulerEngineUtil.schedule(trigger,
                                         MEMORY,
                                         format("Schedule job for monitor: %s", trigger.getJobName()),
                                         destinationName,
                                         message,
                                         0);
        }
        catch (final SchedulerException e) {
            LOGGER.warn("Error scheduling trigger: {} for monitor name: {}", trigger, e);
        }
        LOGGER.debug("scheduleEntry({}) - end", trigger);
    }

    public void destroy() {
        LOGGER.debug("destroy() - start");
        for (final Trigger trigger : triggers) {
            unScheduleEntry(trigger);
        }
        LOGGER.debug("destroy() - end");
    }

    protected void unScheduleEntry(final Trigger trigger) {
        LOGGER.debug("unScheduleEntry({}) - start", trigger);
        try {
            SchedulerEngineUtil.unschedule(trigger.getJobName(),
                                           trigger.getGroupName(),
                                           MEMORY);
        }
        catch (final SchedulerException e) {
            LOGGER.warn("Error un-scheduling trigger: {}", trigger, e);
        }
        LOGGER.debug("unScheduleEntry({}) - end", trigger);
    }

}
