"use strict";
AUI.add("cluster-console",
        function (A) {
            A.log("Initialising ClusterConsole namespace", "debug");
            A.ClusterConsole = {
                chart : function (selector, series) {
                    A.log("Creating chart for: " + selector + " with series: " + series, "info");
                    A.all(selector).each(function (node) {
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
                            var styles = {
                                series : {}
                            };
                            A.Array(series).forEach(function (val) {
                                A.log("Configuring marker style for series: " + val, "debug");
                                styles.series[val] = marker;
                            });
                            A.log("Graphing data from: " + e, "debug");
                            new A.Chart({
                                            axes                : axes,
                                            categoryKey         : "Timestamp",
                                            categoryType        : "time",
                                            dataProvider        : e.response.results,
                                            horizontalGridlines : true,
                                            render              : node,
                                            styles              : styles,
                                            verticalGridlines   : true
                                        });
                        };
                        var failure = function (e) {
                            A.log("Error fetching chart data: " + e, "error");
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
                        var schema = {
                            schema : {
                                resultFields      : [
                                    {
                                        key    : "Timestamp",
                                        parser : "date"
                                    }
                                ].concat(series),
                                resultListLocator : node.attr("data-cluster-node-id")
                            }
                        };
                        A.log("Creating JSON request for data", "info");
                        new A.DataSource.IO(config).plug(A.Plugin.DataSourceJSONSchema, schema).sendRequest(request);
                    });
                }
            };
        },
        "0.1",
        {
            requires : [
                "charts",
                "datasource-io",
                "datasource-jsonschema"
            ]
        }
);

