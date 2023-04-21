import {userApiUrl, baseUrl} from "../../shared.js";
import {adminPage} from "../../auth/adminPage.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

const cancel = document.querySelector('.cancelButton');
const form = document.getElementById('form');
const load = document.getElementById('load');
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
    const form = document.getElementById('editTable');

    await putUpdate()
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
    console.log(url);
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

const fillForm = (business) => {
    document.getElementById('uuid').setAttribute('value', business['uuid']);
    document.getElementById('seatBasline').setAttribute('value', business['seatBasline']);
    document.getElementById('business-name').setAttribute('value', business['name']);


}

const putUpdate = async () => {
    const form = document.getElementById('editTable');
    const body = {
        uuid: form.elements['uuid'].value,
        seatBaseline: form.elements['seatBaseline'].value,
        name: form.elements['business-name'].value

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