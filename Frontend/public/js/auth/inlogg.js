import {isAdmin, isAuthenticatedWithRedirect, isUser} from "./auth.js";
import {baseUrl, userApiUrl} from "../shared.js";

async function getInfo() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const apiUrl = `${userApiUrl}/api/auth/authenticate`;


    function requestBody(email, password) {
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
            window.localStorage.setItem("jwt", data.accessToken);
            window.localStorage.setItem("refreshToken", data.refreshToken);
        }).catch((error) => {
            console.error("Login error:", error);
            // Display an error message
            alert("An error occurred while logging in");
        });

}

window.addEventListener('submit', async (event) => {
    event.preventDefault();
    console.log("logging in");
    await getInfo();
    if (await isAuthenticatedWithRedirect() === false) return window.location.assign(`${baseUrl}/error`);

    const urlParams = new URLSearchParams(window.location.search);
    const redirectUrl = urlParams.get('redirect');
    console.log(redirectUrl)

    if (redirectUrl !== null) {
        window.location.assign(`${baseUrl}${redirectUrl}`);
    } else if ((await isAdmin())) {
        window.location.assign(`${baseUrl}/admin`);
    } else if ((await isUser())) {
        window.location.assign(`${baseUrl}/seat/report`);
    } else {
        window.location.assign(`${baseUrl}/error`)
    }
    console.log("after verify");
});

