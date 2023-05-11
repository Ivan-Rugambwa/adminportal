import { isAdmin, isAuthenticated, isUser } from "../auth/auth.js";
import { baseUrl, userApiUrl } from "../shared.js";

async function postLogin() {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('uuid');
    const newPassword = document.getElementById("newPassword").value;
    const confirmNewPassword = document.getElementById("confirmNewPassword").value;
    const apiUrl = `${userApiUrl}/api/auth/reset/finish`;

    let payload = {
        "resetPasswordUuid": uuid,
        "newPassword": newPassword,
        "confirmNewPassword": confirmNewPassword
    }

    const response = await fetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        throw Error('Fel lösenord')
    }
}

window.addEventListener('submit', async (event) => {
    event.preventDefault();
    console.log("återställ");
    try {
        await postLogin();
        if (!await isAuthenticated()) {
            return window.location.assign(`${baseUrl}/error`);
        }

        const urlParams = new URLSearchParams(window.location.search);
        const redirectUrl = urlParams.get('redirect');
        console.log(redirectUrl);

        if (redirectUrl !== null) {
            window.location.assign(`${baseUrl}${redirectUrl}`);
        } else if (await isAdmin()) {
            window.location.assign(`${baseUrl}/admin`);
        } else if (await isUser()) {
            window.location.assign(`${baseUrl}/seat/report`);
        } else {
            window.location.assign(`${baseUrl}/error`)
        }
        console.log("after verify");
    } catch (e) {
        document.querySelector(".password-error").innerHTML = "Fel lösenord";
        document.querySelector(".password-error").style.display = "block";
        console.log(e);
    }
});
