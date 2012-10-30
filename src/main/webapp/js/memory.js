"use strict";
AUI().use("charts",
          "console",
          "datasource-io",
          "json-parse",
          function (A) {
              A.log("creating charts...");
              A.all(".memory-chart").each(function (node) {

                  var clusterNodeId = node.attr("data-cluster-node-id");
                  A.log("creating filter function for: " + clusterNodeId);
                  var filterFn = function (item) {
                      A.log("filtering: " + item);
                      return item.clusterNodeId === clusterNodeId;
                  };

                  A.log("creating map function");
                  var mapFn = function (item) {
                      A.log("mapping: " + item);
                      return {
                          date            : new Date(item.timestamp),
                          'used heap'     : item.monitor.result.heap.used / (1024 * 1024),
                          'used non-heap' : item.monitor.result.nonHeap.used / (1024 * 1024)
                      };
                  };

                  var jsonUrl = node.attr("data-json-url");
                  A.log("creating datasource for: " + jsonUrl);
                  var dataSource = new A.DataSource.IO({
                                                           source : jsonUrl
                                                       });

                  A.log("sending request for data");
                  dataSource.sendRequest({
                                             request : "",
                                             on      : {
                                                 success : function (e) {
                                                     A.log("handling success callback for: " + e);

                                                     var json = A.JSON.parse(e.response.results[0].responseText);
                                                     A.log("parsed json: " + json);

                                                     var data = A.Array(json).filter(filterFn).map(mapFn);
                                                     A.log("chart data: " + data);

                                                     A.log("creating chart for: " + node);
                                                     var chart = new A.Chart({
                                                                                 dataProvider : data,
                                                                                 render       : node,
                                                                                 categoryKey  : "date",
                                                                                 categoryType : "time",
                                                                                 axes         : {
                                                                                     date : {
                                                                                         labelFormat : "%T"
                                                                                     }
                                                                                 }});

                                                 },
                                                 failure : function (e) {
                                                     A.log("handling error callback for: " + e);
                                                     alert("Oops something went wrong here.. " + e.error.message);
                                                 }
                                             }
                                         });
              });
          });
