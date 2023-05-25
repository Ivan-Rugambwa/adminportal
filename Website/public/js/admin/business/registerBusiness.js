import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

const cancel = document.querySelector('.cancelButton');
const loadText = document.getElementById('loadText');
const loadIcon = document.getElementById('loadIcon');
const registerButton = document.getElementById('registerButton');


cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/admin/business`);
});

window.addEventListener('load', async () => {
    await isAuthenticatedWithRedirect();
})

window.addEventListener('submit', async ev => {
    ev.preventDefault();
    loadText.innerText = '';
    loadText.classList.add('none');
    loadIcon.classList.remove('none');
    registerButton.style.pointerEvents = 'none';

    try {
        const post = postRegister();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        loadText.innerText = 'Företaget har skapats.';
        loadText.style.color = 'green';
    } catch (e) {
        loadText.innerText = e.message;
        loadText.style.color = 'red';
    }
    loadText.classList.remove('none');
    loadIcon.classList.add('none');
    registerButton.style.pointerEvents = 'auto';
})

const postRegister = async () => {
    const form = document.getElementById('form');
    const body = {
        seatBaseline: form.elements['seatBaseline'].value,
        name: form.elements['business-name'].value,
        emailFrequency: form.elements['email-frequency'].value
    }
    const response = await fetch(`${userApiUrl}/api/admin/business`, {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        },
        body: JSON.stringify(body)
    });
    const json = await response.json();
    if (response.status > 299) {
        throw Error(json.message);
    }

}