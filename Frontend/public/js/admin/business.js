import { baseUrl } from "../shared";

function getBusiness() {
    const business = document.getElementById("name").value;
    const seats = document.getElementById("seatAmount").value;
    const url = `${baseUrl}/api/admin/business/`

    fetch(url, {
      method: "GET",
      headers: {
        'Authorization': `Bearer ${window.localStorage.getItem("jwt")}`,
        "Content-Type": "application/json",
      },
    })
}
