import {isAdmin, isAuthenticated, isUser} from "./auth/auth.js";
import {baseUrl, userApiUrl} from "./shared.js";

async function postLogin() {

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

    const response = await fetch(apiUrl, {
        method: "POST",
        body: requestBody(email, password),
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (response.status !== 200) {
        throw Error('Fel email eller lösenord')


    }
    const json = await response.json();
    window.localStorage.setItem("jwt", json.accessToken);
    window.localStorage.setItem("refreshToken", json.refreshToken);
}

window.addEventListener('submit', async (event) => {
    event.preventDefault();
    console.log("logging in");
    const loginText = document.getElementById('loginText');
    const loadIcon = "fa-solid fa-arrow-rotate-right fa-spin".split(" ");
    try {
        loginText.innerText = '';
        loginText.classList.add(...loadIcon);
        const post = postLogin();
        await Promise.all([post, new Promise(r => setTimeout(r, 200))])

        if (await isAuthenticated() === false) return window.location.assign(`${baseUrl}/error`);

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
    } catch (e) {
        document.querySelector(".password-error").innerText = "Fel användarnamn eller lösenord";
        document.querySelector(".password-error").style.display = "block";

        console.log(e);
    }
    loginText.innerText = 'Logga in';
    loginText.classList.remove(...loadIcon);

});





