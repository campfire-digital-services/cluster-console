package au.com.permeance.clusterconsole.reducer.impl;

import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

public class ClassLoadingJSONReducer implements JSONReducer {

    private final JSONFactory jsonFactory;

    public ClassLoadingJSONReducer(final JSONFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject reduce(final JSONObject jsonObject) {
        return jsonFactory.createJSONObject()
                          .put("Timestamp", jsonObject.getLong("timestamp"))
                          .put("Unloaded Class Count", jsonObject.getJSONObject("monitor")
                                                                 .getJSONObject("result")
                                                                 .getLong("unloadedClassCount"))
                          .put("Loaded Class Count", jsonObject.getJSONObject("monitor")
                                                               .getJSONObject("result")
                                                               .getLong("loadedClassCount"))
                          .put("Total Loaded Class Count", jsonObject.getJSONObject("monitor")
                                                                     .getJSONObject("result")
                                                                     .getLong("totalLoadedClassCount"));
    }

}
