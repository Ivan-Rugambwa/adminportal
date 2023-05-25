import {baseUrl, camundaApiUrl, userApiUrl} from "../../shared.js";
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
const loadIcon = document.getElementById('loadIcon');
form.addEventListener('submit', async ev => {
    ev.preventDefault();
    const loadText = document.getElementById('loadText');
    const registerButton = document.getElementById('registerButton');
    loadText.innerText = '';
    loadText.classList.add('none');
    loadIcon.classList.remove('none');
    registerButton.style.pointerEvents = 'none';

    try {
        const post = postRegister();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        loadText.innerText = 'Seatrapporten har skapats.';
        loadText.style.color = 'green';
    } catch (e) {
        loadText.innerText = 'Kunde inte skapa seatrapporten. Försök igen senare eller kontakta admin.';
        loadText.style.color = 'red';
    }
    loadText.classList.remove('none');
    loadIcon.classList.add('none');
    registerButton.style.pointerEvents = 'auto';

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
        option.setAttribute('value', business['uuid']);
        option.innerText = business['name'];
        select.appendChild(option);
    });
}

const postRegister = async () => {
    const form = document.getElementById('form');
    const body = {

        forDate: form.elements['date-select'].value,
        businessUuid: form.elements['business-select'].value

    }
    const response = await fetch(`${camundaApiUrl}/api/camunda/start/single`, {
        method: 'POST',
        headers: {
            'Content-Type': "application/json",
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        },
        body: JSON.stringify(body)
    });
    if (response.status >= 300) {
        const json = await response.json();
        throw Error('Something went wrong registering account: ' + json.message)
    }
}