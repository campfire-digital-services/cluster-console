package au.com.permeance.clusterconsole.util;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONObjectWithTimestampComparator implements Comparator<JSONObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObjectWithTimestampComparator.class);

    public final static JSONObjectWithTimestampComparator INSTANCE = new JSONObjectWithTimestampComparator();

    private JSONObjectWithTimestampComparator() {
        LOGGER.debug("JSONObjectWithTimestampComparator() - start");
        // Hide me
        LOGGER.debug("JSONObjectWithTimestampComparator() - end");
    }

    @Override
    public int compare(final JSONObject o1, final JSONObject o2) {
        LOGGER.debug("compare({}, {}) - start", o1, o2);
        final int result;
        if (o1.has("timestamp") && o2.has("timestamp")) {
            final long v1 = o1.getLong("timestamp");
            final long v2 = o2.getLong("timestamp");
            result = (v1 == v2)
                     ? 0
                     : (v1 > v2)
                       ? 1
                       : -1;
        }
        else if (o1.has("timestamp")) {
            LOGGER.warn("Object 2: {} has no timestamp", o2);
            result = 1;
        }
        else if (o2.has("timestamp")) {
            LOGGER.warn("Object 1: {} has no timestamp", o1);
            result = -1;
        }
        else {
            LOGGER.warn("Neither object 1: {} or object 2: {} have timestamps", o1, o2);
            result = 0;
        }
        LOGGER.debug("compare({}, {}) - end - returning: {}", new Object[]{o1, o2, result});
        return result;
    }
}
