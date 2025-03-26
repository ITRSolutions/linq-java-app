$(document).ready(function () {
    // Function to fetch and display both server stats and user stats from actuator endpoint
    function fetchServerStats() {
        $.ajax({
            url: '/api/v1/actuator', // Endpoint for actuator metrics
            type: 'GET',
            success: function (response) {
                var statsHtml = '';

                // Assuming the response includes the required data, adjust as per actual structure
                // Sample metrics: health, cpu, disk, application.ready.time, etc.
                var healthStatus = response.health || 'UNKNOWN';
                var cpuUsage = response.cpuUsage || 0;
                var diskFree = response.diskFree || 0;
                var diskTotal = response.diskTotal || 0;
                var activeUser = response.activeUser || 0;
                var applicationStartedTime = response.applicationStartedTime || 0;
                var activeExecutor = response.activeExecutor || 0;
                var completedExecutor = response.completedExecutor || 0;

                let healthAnchor = '<a href="/actuator/health" target="_blank" style="color: unset;">'+healthStatus+'</a >';
                // Health Status
                statsHtml += '<li>Health Status: <span id="healthStatus" class="pull-right ' + (healthStatus === 'UP' ? 'text-success' : 'text-danger') + '">' + healthAnchor + '</span></li>';

                statsHtml += '<li>Current Website Ping: <span id="cpuUsage" class="text-success pull-right">' + activeUser + ' Users </span></li>';

                statsHtml += '<li>Application Started Time: <span id="applicationStartedTime" class="pull-right">' + applicationStartedTime.toFixed(2) + ' ms</span></li>';

                // CPU Usage
                statsHtml += '<li>CPU Usage: <span id="cpuUsage" class="text-success pull-right">' + (cpuUsage * 100).toFixed(2) + '%</span></li>';

                // Disk stats
                statsHtml += '<li>Disk Free: <span id="diskFree" class="text-success pull-right">' + formatBytes(diskFree) + '</span></li>';
                statsHtml += '<li>Disk Total: <span id="diskTotal" class="text-success pull-right">' + formatBytes(diskTotal) + '</span></li>';

                // Insert the stats into the HTML
                $('#serverStatsList').html(statsHtml);
            },
            error: function (error) {
                console.error('Error fetching stats:', error);
            }
        });
    }

    // Function to format bytes to human-readable format
    function formatBytes(bytes) {
        var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
        if (bytes === 0) return '0 Bytes';
        var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
        return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
    }

    // Call the function to load stats on page load
    fetchServerStats();

    // Optionally, refresh stats every 30 seconds
    setInterval(fetchServerStats, 30000);
});