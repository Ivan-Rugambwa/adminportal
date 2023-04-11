import {isAdmin, isUser, verifyJwt} from "./auth.js";

//const accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbeyJuYW1lIjoiQURNSU4iLCJkZXNjcmlwdGlvbiI6IlNpdGUgYWRtaW5pc3RyYXRvciJ9XSwic3ViIjoia2ltQHRlc3QuY29tIiwiaWF0IjoxNjgwODczODk5LCJleHAiOjE2ODA4NzQ3OTl9.dR8cu7DfCAdReJ3Shh16YZgMUkevJKumK5YcrmywATJZ4d0_cXNeQPem5DWHKsx_EcL9lUtXzwX2CfZZpOEazQ"

async function getInfo() {

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const apiUrl = "http://wsprakt3.apendo.se:35462/api/auth/authenticate";


    function requestBody(email, password) {
        console.log(email, password);
        let payload = {
            "email": email,
            "password": password
        }
        return JSON.stringify(payload);
    }

    await fetch(apiUrl, {
        method: "POST",
        body: requestBody(email, password),
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => response.json())

    .then(data => {
      console.log(data.accessToken);
      window.localStorage.setItem("jwt", data.accessToken);
      window.localStorage.setItem("refreshToken", data.refreshToken);
    }).catch((error) => {
      console.error("Login error:", error);
      // Display an error message
      alert("An error occurred while logging in");
    });

}

//Check if the user is logged in when loading the admin portal page
window.addEventListener('load', async function () {
    this.window.localStorage.setItem("jwt", null);
    console.log("checking jwt");
    if (localStorage.getItem('jwt')) {
        console.log("found jwt");
        const result = await verifyJwt();
        if (result === 200) {
            console.log("jwt ok!!!");
            window.location.assign('http://localhost:3000/admin');
        } else {
            console.log("jwt bad");
        }
    } else {
        console.log("no jwt");
    }
});

window.addEventListener('submit', async (event) => {
    event.preventDefault();
    console.log("logging in");
    await getInfo();
    const statusCode = await verifyJwt();
    if (statusCode !== 200) return window.location.assign('http://localhost:3000' + '/error');
    if ((await isAdmin())) {
        window.location.assign('http://localhost:3000/admin');
    }
    if ((await isUser())) {
        window.location.assign('http://localhost:3000/seat/report');
    } else window.location.assign('http://localhost:3000/error')
    console.log("after verify");

});

