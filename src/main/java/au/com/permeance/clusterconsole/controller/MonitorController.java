package au.com.permeance.clusterconsole.controller;

import au.com.permeance.clusterconsole.monitor.MonitorHistory;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Date;
import java.util.SortedMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import static java.lang.String.*;

@Controller
public class MonitorController {

    private final MonitorHistory monitorHistory;

    private final String viewPrefix;

    public MonitorController(final MonitorHistory monitorHistory,
                             final String viewPrefix) {
        this.monitorHistory = monitorHistory;
        this.viewPrefix = viewPrefix;
    }

    @ModelAttribute("monitorHistory")
    public SortedMap<String, SortedMap<Date, JSONObject>> getHistory() {
        return monitorHistory.getHistory();
    }

    @RenderMapping
    @RequestMapping("VIEW")
    public String render() {
        return format("%s/view", viewPrefix);
    }

}
