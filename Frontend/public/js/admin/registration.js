function formValidation() {

    // get the form data
    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const business = document.getElementById("business").value.trim();
    const url = `${baseUrl}/api/auth/register`
  
    // perform basic form validation
    if (firstName === "" || lastName === "" || email === "" || password === "" || business === "") {
      alert("Please fill out all fields.");
      return;
    }
  
    if (!isValidEmail(email)) {
      alert("Please enter a valid email address.");
      return;
    }
  
    // create an object with the form data
    let payload = {
      "firstName": firstName,
      "lastName": lastName,
      "email": email,
      "password": password,
      "business": business
    };
  
    function requestBody(firstName, lastName, email, password, business) {
      console.log(firstName, lastName, email, password, business);
      return payload;
    }
  
    // make a POST request to the API with the form data
    fetch(url, {
        method: 'POST',
        headers: {
          
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })
      .then(response => {
       
        return response.json();
      })
      .then(data => {
        console.log(data);
        // do something with the response data
      })
      .catch(error => {
        console.error('There was an error!', error);
      });
  }
  
  function isValidEmail(email) {
    // basic email validation using a regular expression
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
  