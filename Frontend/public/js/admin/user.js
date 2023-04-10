function getUser() {
  const userId = document.getElementById("uuid").value;
  const firstName = document.getElementById("firstName").value;
  const lastName = document.getElementById("lastName").value;
  const email = document.getElementById("email").value;
  const apiUrl = "83.233.216.66:35462/api/admin/user";

  

  

  fetch(apiUrl, {
    method: "GET",
    headers: {
      'Authorization': `Bearer ${"eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbeyJuYW1lIjoiQURNSU4iLCJkZXNjcmlwdGlvbiI6IlNpdGUgYWRtaW5pc3RyYXRvciJ9XSwic3ViIjoia2ltQHRlc3QuY29tIiwiaWF0IjoxNjgwNzgwNDI1LCJleHAiOjE2ODA3ODEzMjV9.KZFhGq1Vcj3azT8HeEzNvGtGVAv-PA2m3dAmthxyVqpkeIZxfCcF8f9FkHT-naqIo3u2-dpFHybVhQlWSlcaiQ"}`,
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
        const firstNameCell = document.createElement('td');
        const lastNameCell = document.createElement('td');
        const emailCell = document.createElement('td');
        uuidCell.innerText = obj.uuid;
        firstNameCell.innerText = obj.firstName;
        lastNameCell.innerText = obj.lastName;
        emailCell.innerText = obj.email;
        row.appendChild(uuidCell);
        row.appendChild(firstNameCell);
        row.appendChild(lastNameCell);
        row.appendChild(emailCell);
        tableBody.appendChild(row);
      });
    })


}
