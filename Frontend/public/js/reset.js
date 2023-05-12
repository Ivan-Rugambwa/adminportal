//import { json } from "body-parser";
import {userApiUrl} from "./shared.js";

const form = document.getElementById("login-form");

form.addEventListener('submit', async (event) => {
    // Prevent default form submission
    event.preventDefault();
    document.getElementById('errorMessage').classList.add('none');
    document.getElementById('successMessage').classList.add('none');
    const loginText = document.getElementById('loginText');
    const loadIcon = "fa-solid fa-arrow-rotate-right fa-spin".split(" ");
    loginText.innerText = '';
    loginText.classList.add(...loadIcon);
    // Get the email value
    const email = document.getElementById("email").value;
    const body = {
        email: form.elements['email'].value
    }
    console.log(body)
    // Send a password reset link to the provided email address
    const apiUrl = `${userApiUrl}/api/auth/reset/start`;
    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            body: JSON.stringify(email),
            headers: {
                'Content-Type': "application/json"
            }
        });

        if (response.status === 204) {
            document.getElementById('successMessage').classList.remove('none');
            document.getElementById('successMessage').innerText = 'Om denna e-postadress finns registrerad kommer ett e-postmeddelande med instruktioner om hur du återställer ditt lösenord skickas till din inkorg.';
        } else {
            throw Error("Failed post of reset");
        }
    } catch (e) {
        console.log(e)
        document.getElementById('errorMessage').classList.remove('none');
        document.getElementById('errorMessage').innerText = 'Det uppstod ett fel när din förfrågan behandlades. Vänligen försök igen senare eller kontakta support.';
    }
    loginText.innerText = 'Skicka';
    loginText.classList.remove(...loadIcon);

});


