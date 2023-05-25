import {userApiUrl} from "../shared.js";
import {isAuthenticatedWithRedirect} from "../auth/auth.js";

const refresh = document.getElementById('refresh');

window.addEventListener('load', async () => {
    await isAuthenticatedWithRedirect();

    try {
        const businesses = await fetchBusinesses();
        displayReportData(businesses);
    } catch (error) {
        console.error(error);
    }
});

async function fetchBusinesses() {
    try {
        const response = await fetch(`${userApiUrl}/api/admin/business`, {
            headers: {
                Authorization: `Bearer ${window.localStorage.getItem("jwt")}`
            }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch businesses");
        }

        const businesses = await response.json();
        const reportData = [];

        for (const business of businesses) {
            const reportResponse = await fetch(`${userApiUrl}/api/user/seat/business/${business.name}`, {
                headers: {
                    Authorization: `Bearer ${window.localStorage.getItem("jwt")}`
                }
            });

            const reports = await reportResponse.json();

            let totalReports = 0;
            let totalUsageAll = 0;
            let totalUsageCompleted = 0;
            let pendingReports = 0;
            let approvedReports = 0;
            let emptyReports = 0;

            for (const report of reports) {
                totalReports++;
                if (report.status === "FILL") {
                    emptyReports++;
                    continue;
                }


                if (report.status === "REVIEW") {
                    pendingReports++;
                    totalUsageAll += report.seatUsed;

                } else if (report.status === "COMPLETE") {
                    approvedReports++;
                    totalUsageCompleted += report.seatUsed;

                }
            }

            const averageUsageCompleted = approvedReports > 0 ? totalUsageCompleted / approvedReports : 0;

            reportData.push({
                businessName: business.name,
                totalReports: totalReports,
                totalUsage: totalUsageAll,
                averageUsage: averageUsageCompleted,
                pendingReports: pendingReports,
                approvedReports: approvedReports,
                emptyReports: emptyReports,
            });
        }

        return reportData;
    } catch (error) {
        throw new Error(`Failed to fetch businesses: ${error.message}`);
    }
}

function displayReportData(reportData) {
    const reportTable = document.getElementById('report-table');
    reportTable.innerHTML = '';

    for (const report of reportData) {
        const row = reportTable.insertRow();
        row.insertCell().textContent = report.businessName;
        row.insertCell().textContent = report.totalReports;
        row.insertCell().textContent = report.totalUsage;
        row.insertCell().textContent = report.emptyReports;
        row.insertCell().textContent = report.pendingReports;
        row.insertCell().textContent = report.approvedReports;
        row.insertCell().textContent = report.averageUsage.toFixed(2);
    }
    const updateTables = async () => {
        document.getElementById('report-table').innerHTML = '';
        refresh.style.cursor = 'wait';
        refresh.disabled = true;
        const spinning = 'fa-solid fa-arrow-rotate-right fa-spin';
        const still = 'fa-solid fa-arrow-rotate-right';
        const refreshIcon = document.getElementById('refreshIcon');
        refreshIcon.setAttribute('class', spinning);
        refreshIcon.style.pointerEvents = 'none';

        const business = fetchBusinesses();
        const promises = await Promise.all([business, new Promise(r => setTimeout(r, 400))])

        const timer = new Promise(r => setTimeout(r, 1600));
        displayReportData(promises[0]);

        await Promise.all([timer]);
        refreshIcon.setAttribute('class', still);
        refreshIcon.style.pointerEvents = 'auto';
        refresh.disabled = false;
        refresh.style.cursor = 'pointer';
    }
    refresh.addEventListener('click', async ev => {
        ev.target.disabled = true;
        ev.preventDefault();
        await updateTables();
        ev.target.disabled = false;
    })
}
