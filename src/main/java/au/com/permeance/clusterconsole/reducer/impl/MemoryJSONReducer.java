package au.com.permeance.clusterconsole.reducer.impl;

import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

public class MemoryJSONReducer implements JSONReducer {

    private final JSONFactory jsonFactory;

    private final String type;

    public MemoryJSONReducer(final JSONFactory jsonFactory,
                             final String type) {
        this.jsonFactory = jsonFactory;
        this.type = type;
    }

    @Override
    public JSONObject reduce(final JSONObject jsonObject) {
        return jsonFactory.createJSONObject()
                          .put("Timestamp", jsonObject.getLong("timestamp"))
                          .put("Initial Memory", jsonObject.getJSONObject("monitor")
                                                           .getJSONObject("result")
                                                           .getJSONObject(type)
                                                           .getLong("init"))
                          .put("Used Memory", jsonObject.getJSONObject("monitor")
                                                        .getJSONObject("result")
                                                        .getJSONObject(type)
                                                        .getLong("used"))
                          .put("Committed Memory", jsonObject.getJSONObject("monitor")
                                                             .getJSONObject("result")
                                                             .getJSONObject(type)
                                                             .getLong("committed"))
                          .put("Total Memory", jsonObject.getJSONObject("monitor")
                                                         .getJSONObject("result")
                                                         .getJSONObject(type)
                                                         .getLong("total"));
    }

}
