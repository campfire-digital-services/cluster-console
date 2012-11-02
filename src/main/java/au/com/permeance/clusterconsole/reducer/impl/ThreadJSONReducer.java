package au.com.permeance.clusterconsole.reducer.impl;

import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

public class ThreadJSONReducer implements JSONReducer {

    private final JSONFactory jsonFactory;

    public ThreadJSONReducer(final JSONFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject reduce(final JSONObject jsonObject) {
        return jsonFactory.createJSONObject()
                          .put("Timestamp", jsonObject.getLong("timestamp"))
                          .put("Daemon Thread Count", jsonObject.getJSONObject("monitor")
                                                                .getJSONObject("result")
                                                                .getLong("daemonThreadCount"))
                          .put("Thread Count", jsonObject.getJSONObject("monitor")
                                                         .getJSONObject("result")
                                                         .getLong("threadCount"))
                          .put("Total Started Thread Count", jsonObject.getJSONObject("monitor")
                                                                       .getJSONObject("result")
                                                                       .getLong("totalStartedThreadCount"));
    }

}
