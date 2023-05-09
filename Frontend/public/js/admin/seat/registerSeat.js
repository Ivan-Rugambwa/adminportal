import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

window.addEventListener('load', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const role = urlParams.get('role');
    await isAuthenticatedWithRedirect();
    if (role !== 'admin') {
        document.getElementById('business-div').style.visibility = 'visible';
        let businesses = await getBusinesses();
        fillForm(businesses);
    } else {
        document.getElementById('titleText').innerText += ' administratör';
    }
})

const cancel = document.querySelector('.cancelButton');
const form = document.getElementById('form');
const load = document.getElementById('load');
const loadIcon = document.getElementById('loadIcon');
form.addEventListener('submit', async ev => {
    ev.preventDefault();
    load.innerText = '';
    loadIcon.style.display = 'flex';
    try {
        const post = postRegister();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        load.innerText = 'Seat har registrerats.'
        load.style.color = '#99CC00'
        form.elements['date-select'].value = '';
        form.elements['business-select'].value = '';
    } catch (e) {
        load.innerText = 'kunde inte skapa seat, försök igen senare eller kontakta support.'
        load.style.color = 'red';
        console.log(e.message);
    }
    loadIcon.style.display = 'none';

})
cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/admin/seat`);
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
        
        date: form.elements['date-select'].value,
        businessName: form.elements['business-select'].value
       
    }
    const response = await fetch(`https://2598-31-208-229-4.eu.ngrok.io/api/camunda/start/single`, {
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