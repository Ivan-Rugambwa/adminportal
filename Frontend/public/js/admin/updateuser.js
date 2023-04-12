function updateUser() {
    const uuid = document.getElementById("uuid").value;
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const business = document.getElementById("business").value;
    const password = document.getElementById("password").value;
    
    const url = `${baseUrl}/api/admin/user/${uuid}`
  
    let payload = {
      "firstName": firstName,
      "lastName": lastName,
      "email": email,
      "business": business,
      "password": password
    };
  
    fetch(url, {
      method: "POST",
      headers: {
        
        'Authorization': `Bearer ${window.localStorage.getItem("jwt")}`,
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
 
    