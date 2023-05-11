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
        const successMessage = document.querySelector(".success-message");
        successMessage.innerHTML = "Lösenordet har ändrats.<br> <a href='/index'>Tryck för att logga in</a>";
        successMessage.style.display = "block";
    } catch (e) {
        document.querySelector(".password-error").innerHTML = "Fel lösenord";
        document.querySelector(".password-error").style.display = "block";
        console.log(e);
    }
});
