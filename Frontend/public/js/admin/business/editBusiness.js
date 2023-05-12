import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

const cancel = document.querySelector('.cancelButton');
const form = document.getElementById('form');
const load = document.getElementById('load');
const loadText = document.getElementById('loadText');
const loadIcon = document.getElementById('loadIcon');
const registerButton = document.getElementById('registerButton');

cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/admin/business`);
});

window.addEventListener('load', async ev => {
    await isAuthenticatedWithRedirect();

    const business = await getBusiness();

    fillForm(business);

})


window.addEventListener('submit', async ev => {
    ev.preventDefault();
    loadText.innerText = '';
    loadText.classList.add('none');
    loadIcon.classList.remove('none');
    registerButton.style.pointerEvents = 'none';

    try {
        const post = putUpdate();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        loadText.innerText = 'Företaget har uppdaterats.';
        loadText.style.color = 'green';
    } catch (e) {
        loadText.innerText = e.message;
        loadText.style.color = 'red';
    }
    loadText.classList.remove('none');
    loadIcon.classList.add('none');
    registerButton.style.pointerEvents = 'auto';
})
const getBusinesses = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('business');
    const url = `${userApiUrl}/api/admin/business/${uuid}`
    let response;
    await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${window.localStorage.getItem("jwt")}`
        }
    })
        .then(res => {
            response = res.json()
        })
        .catch(error => {
            console.error(error);
        })
    return response;
}

const getBusiness = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('business');
    const url = `${userApiUrl}/api/admin/business/${uuid}`
    let response;

    await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${window.localStorage.getItem("jwt")}`
        }
    })
        .then(async res => {
            response = await res.json()
        })
        .catch(error => {
            console.error(error);
        })
    console.log(response);
    return response;
}

const fillForm = (business) => {
    document.getElementById('uuid').setAttribute('value', business['uuid']);
    document.getElementById('seatBaseline').setAttribute('value', business['seatBaseline']);
    document.getElementById('business-name').setAttribute('value', business['name']);
    document.getElementById('email-frequency').value = business['emailFrequency'];


}

const putUpdate = async () => {
    const form = document.getElementById('form');
    const body = {
        seatBaseline: form.elements['seatBaseline'].value,
        name: form.elements['business-name'].value,
        emailFrequency: form.elements['email-frequency'].value

    }
    console.log(body)
    return await fetch(`${userApiUrl}/api/admin/business/${form.elements['uuid'].value}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': "application/json",
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        },
        body: JSON.stringify(body)
    })
        .then(response => response.json())
        .catch(error => {
            console.error(error);
            throw Error('Something went wrong updating account: ' + error)
        });
}