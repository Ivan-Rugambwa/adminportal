import { baseUrl, userApiUrl } from "../shared.js";
import { isAuthenticatedWithRedirect } from "../auth/auth.js";
const refresh = document.getElementById('refresh');

window.addEventListener('load', async ev => {
  await isAuthenticatedWithRedirect();

  try {
    const businesses = await fetchBusinesses();
    console.log(businesses);
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
    console.log(businesses);
    const reportData = [];

    for (const business of businesses) {
      const reportResponse  = await fetch(`${userApiUrl}/api/user/seat/business/${business.name}`,{

        headers: {
          Authorization: `Bearer ${window.localStorage.getItem("jwt")}`
        }

      });
      
      
      
      const reports = await reportResponse.json();
      console.log(reports);

      let totalReports = 0;
      let totalUsage = 0;
      let pendingReports = 0;
      let approvedReports = 0;
      let emptyReports = 0;
  
      for (const report of reports) {
        totalReports++;
        if(report.seatUsed === null){
          emptyReports++;
          continue;
        }
          
        totalUsage += report.seatUsed;

        if (report.status === "REVIEW") {
          // Seat report being reviewed
          pendingReports++;
        } else if (report.status === "COMPLETE") {
          // Approved seat report
          approvedReports++;
        
        } else if(report.status === "FILL"){
          //Empty seat report
          emptyReports++
        }
        console.log(report);

      }

      const averageUsage = approvedReports > 0 ? totalUsage / approvedReports : 0;

      reportData.push({
        businessName: business.name,
        totalReports: totalReports,
        totalUsage: totalUsage,
        averageUsage: averageUsage,
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
    row.insertCell().textContent = report.pendingReports;
    row.insertCell().textContent = report.approvedReports;
    row.insertCell().textContent = report.emptyReports;
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

    const business =  fetchBusinesses();
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
