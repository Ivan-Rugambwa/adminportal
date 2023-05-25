import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

window.addEventListener('load', async () => {
    await isAuthenticatedWithRedirect();
    const user = await getUser();
    let businesses = await getBusinesses();
    businesses = businesses.filter(business => business['name'] !== user['businessName'])
    fillForm(user, businesses);

});

const cancel = document.querySelector('.cancelButton');
const form = document.getElementById('form');
const load = document.getElementById('load');
const loadIcon = document.getElementById('loadIcon');
const registerButton = document.getElementById('registerButton');
window.addEventListener('submit', async ev => {
    ev.preventDefault();
    load.innerText = '';
    loadIcon.style.display = 'flex';
    registerButton.style.pointerEvents = 'none';
    try {
        const post = putUpdate();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        load.innerText = 'Kunden har uppdaterats.';
        load.style.color = '#99CC00';
    } catch (e) {
        load.innerText = 'Kunde inte uppdatera, försök igen senare eller kontakta support.';
        load.style.color = 'red';
    }
    loadIcon.style.display = 'none';
    registerButton.style.pointerEvents = 'auto';
});

cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/admin/user`);
});

const getUser = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('user');
    const url = `${userApiUrl}/api/admin/user/${uuid}`
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

const getBusinesses = async () => {
    const url = `${userApiUrl}/api/admin/business`
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

const fillForm = (user, businesses) => {
    document.getElementById('uuid').setAttribute('value', user['uuid']);
    document.getElementById('email').setAttribute('value', user['email']);
    document.getElementById('firstName').setAttribute('value', user['firstName']);
    document.getElementById('lastName').setAttribute('value', user['lastName']);
    document.getElementById('role').setAttribute('value', user['roleNames']);
    const select = document.getElementById('business-select');
    if (user['roleNames'].some(role => role === 'ADMIN')) {
        document.getElementById('label-business-select').style.display = 'none';
        select.style.display = 'none';
    } else {
        document.getElementById('label-business-select').style.display = 'block';
        select.style.display = 'block';
        const currentBusiness = document.createElement('option');
        currentBusiness.setAttribute('value', user['businessName']);
        currentBusiness.innerText = user['businessName'];
        select.appendChild(currentBusiness);
        businesses.forEach(business => {
            const option = document.createElement('option');
            option.setAttribute('value', business['name']);
            option.innerText = business['name'];
            select.appendChild(option);
        });
    }
}

const putUpdate = async () => {
    const body = {
        email: form.elements['email'].value,
        firstName: form.elements['firstName'].value,
        lastName: form.elements['lastName'].value,
        business: form.elements['business-select'].value
    }
    return await fetch(`${userApiUrl}/api/admin/user/${form.elements['uuid'].value}`, {
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