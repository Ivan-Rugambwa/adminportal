
//const accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbeyJuYW1lIjoiQURNSU4iLCJkZXNjcmlwdGlvbiI6IlNpdGUgYWRtaW5pc3RyYXRvciJ9XSwic3ViIjoia2ltQHRlc3QuY29tIiwiaWF0IjoxNjgwODczODk5LCJleHAiOjE2ODA4NzQ3OTl9.dR8cu7DfCAdReJ3Shh16YZgMUkevJKumK5YcrmywATJZ4d0_cXNeQPem5DWHKsx_EcL9lUtXzwX2CfZZpOEazQ"

function getInfo() {

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const apiUrl = "http://83.233.216.66:35462/api/auth/authenticate";

  function requestBody(email, password) {
    console.log(email, password);
    let payload = {
      "email": email,
      "password": password
    }
    return JSON.stringify(payload);
  };

  fetch(apiUrl, {
    method: "POST",
    body: requestBody(email, password),
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => response.json())

    .then((data) => {

      console.log(data.accessToken)
    })
    .catch((error) => {
      console.error("Login error:", error);
      // Display an error message
      alert("An error occurred while logging in");
    });
}

// Check if the user is logged in when loading the admin portal page
// window.addEventListener('load', function() {
//   if (!localStorage.getItem('isLoggedIn')) {
//       // Redirect the user to the login page
//       window.location.replace = "inlogg.html";
//   }
// });
