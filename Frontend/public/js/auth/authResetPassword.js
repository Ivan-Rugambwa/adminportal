import {isAdmin, isAuthenticated, isUser} from "../auth/auth.js";
import {baseUrl, userApiUrl} from "../shared.js";

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
    const loginText = document.getElementById('loginText');
    const loadIcon = "fa-solid fa-arrow-rotate-right fa-spin".split(" ");
    loginText.innerText = '';
    loginText.classList.add(...loadIcon);
    try {

        await postLogin();
        const successMessage = document.querySelector(".success-message");
        successMessage.innerHTML = "Lösenordet har ändrats.<br> <a href='/index'>Tryck för att logga in</a>";
        successMessage.style.display = "block";
    } catch (e) {
        document.querySelector(".password-error").innerHTML = "Något gick fel";
        document.querySelector(".password-error").style.display = "block";
        console.log(e);
    }
    loginText.innerText = 'Skicka';
    loginText.classList.remove(...loadIcon);
});

const checkIfValidUuid = async (uuid) => {
    const response = await fetch(`${userApiUrl}/api/password/reset/exists/${uuid}`, {
        method: "HEAD",
    });

    if (response.status === 400) {
        throw new BadRequestError("Invalid password reset uuid");
    }
};
window.addEventListener('load', async ev => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('uuid');
    if (!uuid) {
        window.location.assign(`/`);
    }
    try {
        await checkIfValidUuid(uuid);
        document.getElementById('loadFrame').classList.add('none');
        document.getElementById('loginFrame').classList.remove('none');
    } catch (e) {
        console.log(e.name)
        if (e instanceof TypeError) {
            document.getElementById('errorText').innerText = 'Vi kan inte hantera din förfrågan just nu, vänligen försök igen senare eller kontakta support.';
            document.getElementById('loadFrame').classList.add('none');
            document.getElementById('errorFrame').classList.remove('none');
        } else if (e instanceof BadRequestError) {
            document.getElementById('errorText').innerText = 'Denna länk är inte giltig. Vänligen återställ ditt lösenord igen eller gå till startsidan.';
            document.getElementById('loadFrame').classList.add('none');
            document.getElementById('errorFrame').classList.remove('none');
            document.getElementById('links').classList.remove('none');
        }

    }

});

class BadRequestError extends Error {
    constructor(message) {
        super(message); // (1)
        this.name = "BadRequestError"; // (2)
    }
}
