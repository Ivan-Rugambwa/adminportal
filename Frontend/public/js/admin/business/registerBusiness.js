import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticated, isAuthenticatedWithRedirect} from "../../auth/auth.js";

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
    let businesses = await getBusinesses();
    console.log(businesses)
    //fillForm(businesses);

})

window.addEventListener('submit', async ev => {
    ev.preventDefault();
    const form = document.getElementById('form');
   
     await postRegister();
})

const getBusinesses = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('business');
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



const postRegister = async () => {
    const form = document.getElementById('form');
    const body = {
        seatBaseline: form.elements['seatBaseline'].value,
        name: form.elements['business-name'].value
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
            throw Error('Something went wrong register business: ' + error)
        });
}