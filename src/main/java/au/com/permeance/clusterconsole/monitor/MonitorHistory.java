package au.com.permeance.clusterconsole.monitor;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.List;
import java.util.SortedMap;

public interface MonitorHistory {

    SortedMap<String, List<JSONObject>> getHistory();

    void expireEntries();

}
