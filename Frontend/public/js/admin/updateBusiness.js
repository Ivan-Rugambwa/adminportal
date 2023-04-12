function updateBusniess() {
    const seat = document.getElementById("seatAmount").value;
    const name = document.getElementById("name").value;
    
    
    const url = `${baseUrl}/api/auth/register/api/admin/busniess/${uuid}`
  
    
  
    let payload = {
      "name":name,
      "seatAmount": seatAmount
      
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
 
    