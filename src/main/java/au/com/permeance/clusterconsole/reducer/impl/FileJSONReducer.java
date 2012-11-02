package au.com.permeance.clusterconsole.reducer.impl;

import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

public class FileJSONReducer implements JSONReducer {

    private final JSONFactory jsonFactory;

    private final String type;

    public FileJSONReducer(final JSONFactory jsonFactory,
                           final String type) {
        this.jsonFactory = jsonFactory;
        this.type = type;
    }

    @Override
    public JSONObject reduce(final JSONObject jsonObject) {
        return jsonFactory.createJSONObject()
                          .put("Timestamp", jsonObject.getLong("timestamp"))
                          .put("Free Space", jsonObject.getJSONObject("monitor")
                                                           .getJSONObject("result")
                                                           .getJSONObject(type)
                                                           .getLong("freeSpace"))
                          .put("Usable Space", jsonObject.getJSONObject("monitor")
                                                        .getJSONObject("result")
                                                        .getJSONObject(type)
                                                        .getLong("usableSpace"))
                          .put("Total Space", jsonObject.getJSONObject("monitor")
                                                         .getJSONObject("result")
                                                         .getJSONObject(type)
                                                         .getLong("totalSpace"));
    }

}
