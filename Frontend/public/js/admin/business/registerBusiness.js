import {userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

window.addEventListener('load', async ev => {
    await isAuthenticatedWithRedirect();
    let businesses = await getBusinesses();
    console.log(businesses)
    fillForm(businesses);

})

window.addEventListener('submit', async ev => {
    ev.preventDefault();
    const form = document.getElementById('userForm');
    console.log(form.elements['business-select'].value)
    if (form.elements['business-select'].value === '') {
        console.log('No business chosen')
    }
    // await postRegister();
})

const getBusinesses = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('user');
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
    const form = document.getElementById('userForm');
    const body = {
        uuid: form.elements['uuid'].value,
        firstName: form.elements['firstName'].value,
        lastName: form.elements['lastName'].value,
        business: form.elements['business-select'].value
    }
    console.log(body)
    return await fetch(`${userApiUrl}/api/admin/business`, {
        method: 'POST',
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