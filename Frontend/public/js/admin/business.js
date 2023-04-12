function getBusiness() {
  const uuid = document.getElementById("uuid").value;
  const business = document.getElementById("name").value;
  const seats = document.getElementById("seatAmount").value;

  const apiUrl = "http://wsprakt2.apendo.se:35462/api/admin/business";





  fetch(apiUrl, {
    method: "GET",
    headers: {
      'Authorization': `Bearer ${window.localStorage.getItem("jwt")}`,
      "Content-Type": "application/json",
    },
  })
    .then(response => response.json())
    .then(data => {
      console.log(data);
      const tableBody = document.getElementById("myTable");
      // remove any existing rows from the table
      //  tableBody.innerHTML = '';
      // loop through the data and create a new row for each object
      data.forEach(obj => {
        const row = document.createElement('tr');
        const uuidCell = document.createElement('td');
        const nameCell = document.createElement('td');
        const seatAmountCell = document.createElement('td');
        uuidCell.innerText = obj.uuid;
        nameCell.innerText = obj.name;
        seatAmountCell.innerText = obj.seatBaseline;
        row.appendChild(uuidCell);
        row.appendChild(nameCell);
        row.appendChild(seatAmountCell);
        tableBody.appendChild(row);
      });
    })


}
