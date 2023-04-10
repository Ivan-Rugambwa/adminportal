function updateUser() {
    const uuid = document.getElementById("uuid").value;
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const business = document.getElementById("business").value;
    const password = document.getElementById("password").value;
    const apiUrl = `83.233.216.66:35462/api/admin/user/${uuid}`;
  
    let payload = {
      "firstName": firstName,
      "lastName": lastName,
      "email": email,
      "business": business,
      "password": password
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
 
    