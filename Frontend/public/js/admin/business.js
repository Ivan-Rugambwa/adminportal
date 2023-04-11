function getBusiness() {
    const business = document.getElementById("name").value;
    const seats = document.getElementById("seatAmount").value;
    
    const apiUrl = "83.233.216.66:35462/api/admin/business";
  
    
  
    
  
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
          const nameCell = document.createElement('td');
          const seatAmountCell = document.createElement('td');
          nameCell.innerText = obj.name;
          seatAmountCell.innerText = obj.seatAmount;
          row.appendChild(nameCell);
          row.appendChild(seatAmountCell);
          tableBody.appendChild(row);
        });
      })
  
  
  }
  