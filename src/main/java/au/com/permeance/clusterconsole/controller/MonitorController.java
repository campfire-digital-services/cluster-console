package au.com.permeance.clusterconsole.controller;

import au.com.permeance.clusterconsole.monitor.MonitorHistory;
import au.com.permeance.clusterconsole.reducer.JSONReducer;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.portlet.ResourceResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import static com.liferay.portal.kernel.util.ContentTypes.APPLICATION_JSON;

@Controller
public class MonitorController {

    private final JSONFactory jsonFactory;

    private final MonitorHistory monitorHistory;

    private final Map<String, JSONReducer> reducers;

    private final String view;

    public MonitorController(final JSONFactory jsonFactory,
                             final MonitorHistory monitorHistory,
                             final String view) {
        this(jsonFactory,
             monitorHistory,
             view,
             Collections.<String, JSONReducer>emptyMap());
    }

    public MonitorController(final JSONFactory jsonFactory,
                             final MonitorHistory monitorHistory,
                             final String view,
                             final Map<String, JSONReducer> reducers) {
        this.jsonFactory = jsonFactory;
        this.monitorHistory = monitorHistory;
        this.reducers = reducers;
        this.view = view;
    }

    @ModelAttribute("history")
    public SortedMap<String, List<JSONObject>> getHistory() {
        return monitorHistory.getHistory();
    }

    @ResourceMapping
    @RequestMapping("VIEW")
    public void resourceHistory(final ResourceResponse response,
                                @RequestParam(required = false, value = "reducer", defaultValue = "*") final String reducer) throws IOException {
        final SortedMap<String, List<JSONObject>> history = monitorHistory.getHistory();

        reduceHistory(reducer, history);

        final String json = jsonFactory.createJSONSerializer().serializeDeep(history);

        response.setContentType(APPLICATION_JSON);
        response.setContentLength(json.length());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    protected void reduceHistory(final String reducer,
                               final SortedMap<String, List<JSONObject>> history) {
        final JSONReducer jsonReducer = reducers.get(reducer);
        if (reducer != null) {
            for (final Map.Entry<String, List<JSONObject>> historyItem : history.entrySet()) {
                final List<JSONObject> jsonObjects = historyItem.getValue();
                final List<JSONObject> reducedValues = new ArrayList<JSONObject>(jsonObjects.size());
                for (final JSONObject jsonObject : jsonObjects) {
                    final JSONObject reduced = jsonReducer.reduce(jsonObject);
                    reducedValues.add(reduced);
                }
                historyItem.setValue(reducedValues);
            }
        }
    }

    @RenderMapping
    @RequestMapping("VIEW")
    public String render() {
        return view;
    }

}
