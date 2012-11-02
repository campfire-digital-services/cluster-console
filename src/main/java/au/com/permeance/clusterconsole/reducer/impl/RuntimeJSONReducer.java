package au.com.permeance.clusterconsole.reducer.impl;

import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

public class RuntimeJSONReducer implements JSONReducer {

    private final JSONFactory jsonFactory;

    public RuntimeJSONReducer(final JSONFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject reduce(final JSONObject jsonObject) {
        return jsonFactory.createJSONObject()
                          .put("Timestamp", jsonObject.getLong("timestamp"))
                          .put("Free Memory", jsonObject.getJSONObject("monitor")
                                                        .getJSONObject("result")
                                                        .getLong("freeMemory"))
                          .put("Maximum Memory", jsonObject.getJSONObject("monitor")
                                                             .getJSONObject("result")
                                                             .getLong("maxMemory"))
                          .put("Total Memory", jsonObject.getJSONObject("monitor")
                                                         .getJSONObject("result")
                                                         .getLong("totalMemory"));
    }

}
