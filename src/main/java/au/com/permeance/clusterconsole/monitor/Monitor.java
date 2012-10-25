package au.com.permeance.clusterconsole.monitor;

import com.liferay.portal.kernel.json.JSONObject;

public interface Monitor {

    String name();

    JSONObject poll();

}
