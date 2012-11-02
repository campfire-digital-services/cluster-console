AUI().use("cluster-console", function (A) {
    A.ClusterConsole.chart(".classLoading-chart", ["Unloaded Class Count", "Loaded Class Count", "Total Loaded Class Count"]);
});
