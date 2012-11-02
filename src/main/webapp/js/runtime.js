AUI().use("cluster-console", function (A) {
    A.ClusterConsole.chart(".runtime-chart", ["Free Memory", "Maximum Memory", "Total Memory"]);
});
