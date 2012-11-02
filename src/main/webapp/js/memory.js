AUI().use("cluster-console", function (A) {
    A.ClusterConsole.chart(".memory-chart", ["Initial Memory", "Used Memory", "Committed Memory", "Total Memory"]);
});
