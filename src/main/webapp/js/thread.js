AUI().use("charts", "datasource-io", "datasource-jsonschema", function (A) {

    "use strict";

    A.all(".thread-chart").each(function (node) {

        var success = function (e) {

            var axes = {
                "Timestamp" : {
                    labelFormat : "%T"
                }
            };

            var marker = {
                marker : {
                    height : 4,
                    width  : 4
                },
                line   : {
                    weight : 3
                }
            };

            new A.Chart({
                            axes                : axes,
                            categoryKey         : "Timestamp",
                            categoryType        : "time",
                            dataProvider        : e.response.results,
                            horizontalGridlines : true,
                            render              : node,
                            styles              : {
                                series : {
                                    "Daemon Thread Count"        : marker,
                                    "Thread Count"               : marker,
                                    "Total Started Thread Count" : marker
                                }
                            },
                            verticalGridlines   : true
                        });
        };

        var failure = function (e) {
            alert("Oops... something went wrong while rendering the chart: " + e);
        };

        var config = {
            source : node.attr("data-json-url")
        };

        var request = {
            request : "",
            on      : {
                success : success,
                failure : failure
            }
        };

        var dataSource = new A.DataSource.IO(config);

        dataSource.plug(A.Plugin.DataSourceJSONSchema, {
            schema : {
                resultFields      : [
                    {
                        key    : "Timestamp",
                        parser : "date"
                    },
                    "Daemon Thread Count",
                    "Thread Count",
                    "Total Started Thread Count"
                ],
                resultListLocator : node.attr("data-cluster-node-id")
            }
        });

        dataSource.sendRequest(request);

    });

});
