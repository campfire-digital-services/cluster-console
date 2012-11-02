YUI.add("cluster-console", function (Y) {

    "use strict";

    Y.ClusterConsole = {

        chart : function (selector, series) {

            AUI().use("charts", "datasource-io", "datasource-jsonschema", function (A) {

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

                        new A.Chart({
                                        axes                : axes,
                                        categoryKey         : "Timestamp",
                                        categoryType        : "time",
                                        dataProvider        : e.response.results,
                                        horizontalGridlines : true,
                                        render              : node,
                                        styles              : {
                                            series : {
                                                "Unloaded Class Count"     : marker,
                                                "Loaded Class Count"       : marker,
                                                "Total Loaded Class Count" : marker
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

                    new A.DataSource.IO(config).plug(A.Plugin.DataSourceJSONSchema, schema).sendRequest(request);

                });

            });

        }
    };
}, "0.1", {requires : ["charts", "datasource-io", "datasource-jsonschema"]});

