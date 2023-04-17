import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticated} from "../../auth/auth.js";

window.addEventListener('load', async ev => {
    await isAuthenticated();
    let businesses = await getBusinesses();
    fillForm(businesses);

})

const cancel = document.querySelector('.cancelButton');
const form = document.getElementById('form');
const load = document.getElementById('load');
const loadIcon = document.getElementById('loadIcon');
form.addEventListener('submit', async ev => {
    ev.preventDefault();
    if (form.elements['business-select'].value === '') {
        console.log('No business chosen');
    }
    load.innerText = '';
    loadIcon.style.display = 'flex';
    try {
        const post = postRegister();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        load.innerText = 'Kunden har registrerats.'
        load.style.color = '#99CC00'
        form.elements['email'].value = '';
        form.elements['firstName'].value = '';
        form.elements['lastName'].value = '';
        form.elements['business-select'].value = '';
    } catch (e) {
        load.innerText = 'Kunde inte registrera, försök igen senare eller kontakta support.'
        load.style.color = 'red';
        console.log(e.message);
    }
    loadIcon.style.display = 'none';

})
cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/admin/user`)
})

const getBusinesses = async () => {
    const url = `${userApiUrl}/api/admin/business`
    let response = await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${window.localStorage.getItem("jwt")}`
        }
    });
    if (response.status !== 200) {
        throw Error('Failed getting businesses');
    }
    return await response.json();
}

const fillForm = (businesses) => {
    const select = document.getElementById('business-select');
    select.setAttribute('required', '')
    const currentBusiness = document.createElement('option');
    currentBusiness.setAttribute('disabled', '');
    currentBusiness.setAttribute('hidden', '');
    currentBusiness.setAttribute('selected', '');
    currentBusiness.setAttribute('value', '');
    currentBusiness.innerText = 'Välj företag';
    select.appendChild(currentBusiness);
    businesses.forEach(business => {
        const option = document.createElement('option');
        option.setAttribute('value', business['name']);
        option.innerText = business['name'];
        select.appendChild(option);
    });
}

const postRegister = async () => {
    const form = document.getElementById('form');
    const body = {
        email: form.elements['email'].value,
        firstName: form.elements['firstName'].value,
        lastName: form.elements['lastName'].value,
        businessName: form.elements['business-select'].value
    }
    const response = await fetch(`${userApiUrl}/api/admin/preregister`, {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        },
        body: JSON.stringify(body)
    });
    const json = await response.json();
    if (response.status >= 300) {
        throw Error('Something went wrong registering account: ' + json.message)
    }
    return json;
}