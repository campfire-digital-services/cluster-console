"use strict";
YUI.add("cluster-console",
        function (Y) {
            Y.log("Initialising ClusterConsole namespace", "debug");
            Y.ClusterConsole = {
                chart : function (selector, series) {
                    Y.log("Creating chart for: " + selector + " with series: " + series, "info");
                    Y.all(selector).each(function (node) {
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
                            Y.Array(series).forEach(function (val) {
                                Y.log("Configuring marker style for series: " + val, "debug");
                                styles.series[val] = marker;
                            });
                            Y.log("Graphing data from: " + e, "debug");
                            new Y.Chart({
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
                            Y.log("Error fetching chart data: " + e, "error");
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
                        Y.log("Creating JSON request for data", "info");
                        new Y.DataSource.IO(config).plug(Y.Plugin.DataSourceJSONSchema, schema).sendRequest(request);
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

