import {isAdmin, isAuthenticated, isUser} from "../auth/auth.js";
import {baseUrl, userApiUrl} from "../shared.js";

async function postLogin() {

    const password = document.getElementById("reset-password").value;
    const apiUrl = `${userApiUrl}/api/auth/reset/finish`;

    function requestBody(password) {
        let payload = {
            "authresetstart": password,
            "authresetfinish": password
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
        throw Error('Fel lösenord')

        
    }
    const json = await response.json();
    window.localStorage.setItem("jwt", json.accessToken);
    window.localStorage.setItem("refreshToken", json.refreshToken);
}

window.addEventListener('submit', async (event) => {
    event.preventDefault();
    console.log("återställ");
    try{
    await postLogin();
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
    } catch(e){
        document.querySelector(".password-error").innerHTML = "Password not correct";
      document.querySelector(".password-error").style.display = "block";
      
        console.log(e);
    }

    
});





