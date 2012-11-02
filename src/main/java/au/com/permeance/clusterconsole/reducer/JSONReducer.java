package au.com.permeance.clusterconsole.reducer;

import com.liferay.portal.kernel.json.JSONObject;

public interface JSONReducer {

    JSONObject reduce(JSONObject jsonObject);

}
