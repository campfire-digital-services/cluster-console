"use strict";

AUI().use("charts", "datasource-io", "json-parse", function (A) {

    // Configuration for rendering the axes of the chart - see XXX line XXX for details
    var axes = {
        time : {
            labelFormat : "%T"
        }
    };

    // Configuration of the marker display's of the chart - see charts-debug.js lines 7133 and 8548 for details
    var marker = {
        marker : {
            height : 3,
            width  : 3
        },
        line   : {
            weight : 2
        }
    };

    // Use the same marker styles for each series
    var styles = {
        series : {
            max   : marker,
            total : marker,
            used  : marker
        }
    };

    A.all(".memory-chart").each(function (node) {

        // The filter predicate to apply to incoming history items to determine whether it should be included in the chart
        var filterFn = function (item) {
            return item.clusterNodeId === node.attr("data-cluster-node-id");
        };

        // The map transformation to apply to an incoming history item to determine what should information be included in the chart
        var mapFn = function (item) {
            return {
                time  : new Date(item.timestamp),
                max   : item.monitor.result.heap.max,
                total : item.monitor.result.heap.committed,
                used  : item.monitor.result.heap.used
            };
        };

        // The function to call on data fetch success
        var success = function (e) {

            // Parse the response text as JSON
            var historyItems = A.JSON.parse(e.response.results[0].responseText);

            // Then filter and map the resulting json as necessary for display
            var dataSet = A.Array(historyItems).filter(filterFn).map(mapFn);

            // And finally throw it into a chart using the earlier objects for configuration
            new A.Chart({
                            axes                : axes,
                            categoryKey         : "time",
                            categoryType        : "time",
                            dataProvider        : dataSet,
                            horizontalGridlines : true,
                            render              : node,
                            styles              : styles,
                            verticalGridlines   : true
                        });
        };

        // The function to call on data fetch failure
        var failure = function (e) {
            // If it failed - just show an alert and hope for the best
            alert("Oops something went wrong here.. " + e.error.message);
        };

        // Create a data source referring back to the relevant node JSON url
        var dataSource = new A.DataSource.IO({ source : node.attr("data-json-url") });

        // Make chart go now
        dataSource.sendRequest({
                                   on : {
                                       success : success,
                                       failure : failure
                                   }
                               });
    });

});
