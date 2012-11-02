package au.com.permeance.clusterconsole.reducer.impl;

import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

public class OperatingSystemJSONReducer implements JSONReducer {

    private final JSONFactory jsonFactory;

    public OperatingSystemJSONReducer(final JSONFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject reduce(final JSONObject jsonObject) {
        return jsonFactory.createJSONObject()
                          .put("Timestamp", jsonObject.getLong("timestamp"))
                          .put("System Load Average", jsonObject.getJSONObject("monitor")
                                                                .getJSONObject("result")
                                                                .getLong("systemLoadAverage"));
    }

}
