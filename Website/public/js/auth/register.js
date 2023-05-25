import {baseUrl, userApiUrl} from "../shared.js";

const form = document.getElementById('form');
let uuid;
const load = document.getElementById('load');
const loginButton = document.getElementById('loginButton');

window.addEventListener('load', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    uuid = urlParams.get('uuid');
    try {
        const preRegister = await getPreRegister();
        await loadForm(preRegister);
        document.getElementById('getPreRegisterLoad').classList.add('none');
        document.getElementById('formData').classList.remove('none');
    } catch (e) {
        document.getElementById('getPreRegisterLoad').classList.add('none');
        document.getElementById('failDiv').classList.remove('none');
    }
});

form.addEventListener('submit', async ev => {
    ev.preventDefault();
    load.innerText = '';
    if (isValid()) {
        try {
            await postRegister();
            load.innerText = 'Registreringen lyckades! Du kan nu logga in.';
            load.style.color = '#99CC00';
            document.getElementById('registerButton').style.display = 'none';
            document.getElementById('loginButton').style.display = 'block';
        } catch (e) {
            load.innerText = 'Det gick inte att registrera, försök igen senare eller kontakta support.';
            load.style.color = 'red';
        }
    }
});
loginButton.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/`);
})
const isValid = () => {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    let isValid = true;
    if (password.length < 8) {
        load.innerText = 'Lösenord måste vara minst 8 tecken.';
        load.style.color = 'red';
        isValid = false;
    }
    if (password.length > 64) {
        load.innerText += '\nLösenord kan vara max 64 tecken.';
        load.style.color = 'red';
        isValid = false;
    }
    if (confirmPassword !== password) {
        load.innerText += '\nLösenord måste matcha.';
        load.style.color = 'red';
        isValid = false;
    }
    return isValid;
}
const loadForm = async (preRegister) => {
    const email = document.getElementById('email');
    const business = document.getElementById('business');
    const userBusiness = preRegister['businessName'];
    email.value = preRegister['email'];

    if (userBusiness) {
        business.value = userBusiness;
        document.getElementById('business-div').style.visibility = 'visible';
    }

}

const getPreRegister = async () => {

    const response = await fetch(`${userApiUrl}/api/auth/preregister/${uuid}`, {
        method: 'GET',
    });
    if (response.status !== 200) {
        throw Error('Failed getting preregister')
    }
    return await response.json();
}
const postRegister = async () => {
    const data = {
        preRegisterUuid: uuid,
        password: document.getElementById('password').value,
        confirmPassword: document.getElementById('confirmPassword').value
    }
    const response = await fetch(`${userApiUrl}/api/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    if (response.status !== 200) {
        throw Error('Failed registering: ' + (await response.json()).message)
    }
    return await response.json();
}