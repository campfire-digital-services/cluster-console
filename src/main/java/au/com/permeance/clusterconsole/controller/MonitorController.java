package au.com.permeance.clusterconsole.controller;

import au.com.permeance.clusterconsole.monitor.MonitorHistory;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import javax.portlet.ResourceResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import static com.liferay.portal.kernel.util.ContentTypes.APPLICATION_JSON;
import static java.lang.String.format;

@Controller
public class MonitorController {

    private final JSONFactory jsonFactory;

    private final MonitorHistory monitorHistory;

    private final String viewPrefix;


    public MonitorController(final JSONFactory jsonFactory,
                             final MonitorHistory monitorHistory,
                             final String viewPrefix) {
        this.jsonFactory = jsonFactory;
        this.monitorHistory = monitorHistory;
        this.viewPrefix = viewPrefix;
    }

    @ModelAttribute("monitorHistory")
    public SortedMap<String, SortedMap<Date, JSONObject>> getHistory() {
        return monitorHistory.getCategorisedHistory();
    }

    @ResourceMapping
    @RequestMapping("VIEW")
    public void resourceHistory(final ResourceResponse response) throws IOException {
        final List<JSONObject> history = monitorHistory.getHistory();
        final String json = jsonFactory.createJSONSerializer()
                                       .serializeDeep(history);

        response.setContentType(APPLICATION_JSON);
        response.setContentLength(json.length());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    @RenderMapping
    @RequestMapping("VIEW")
    public String render() {
        return format("%s/view", viewPrefix);
    }

}
