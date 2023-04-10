function updateBusniess() {
    const seat = document.getElementById("seatAmount").value;
    const name = document.getElementById("name").value;
    
    const apiUrl = `83.233.216.66:35462/api/admin/busniess/${uuid}`;
  
    let payload = {
      "name":name,
      "seatAmount": seatAmount
      
    };
  
    fetch(apiUrl, {
      method: "POST",
      headers: {
        
        'Authorization': `Bearer ${"eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbeyJuYW1lIjoiQURNSU4iLCJkZXNjcmlwdGlvbiI6IlNpdGUgYWRtaW5pc3RyYXRvciJ9XSwic3ViIjoia2ltQHRlc3QuY29tIiwiaWF0IjoxNjgwNzgwNDI1LCJleHAiOjE2ODA3ODEzMjV9.KZFhGq1Vcj3azT8HeEzNvGtGVAv-PA2m3dAmthxyVqpkeIZxfCcF8f9FkHT-naqIo3u2-dpFHybVhQlWSlcaiQ"}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(data => {
      console.log(data);
      // handle the response data as needed
    })
    .catch(error => {
      console.error('Error:', error);
      // handle errors as needed
    });
  }
 
    