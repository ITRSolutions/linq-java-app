
    document.addEventListener("DOMContentLoaded", function () {
        // Check if the job_search table exists
        const tbody = document.getElementById("job_search");

        if (tbody) {
            const rows = tbody.querySelectorAll("tr");

            // If no rows found, append the "No data found" message
            if (rows.length === 0) {
                const noDataRow = document.createElement("tr");
                noDataRow.innerHTML = `
                    <td colspan="3" class="text-center py-4 text-muted">No data found</td>
                `;
                tbody.appendChild(noDataRow);
            }

            // Get total number of rows (excluding the "No data found" row if it's added)
            const totalCount = rows.length;

            // Find the element with id="totalResult" and set its text
            const resultElement = document.getElementById("totalResult");
            if (resultElement) {
                resultElement.textContent = totalCount;
            }
        }
    });
