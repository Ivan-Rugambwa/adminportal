import {userApiUrl} from "../../shared.js";
import {isAuthenticated} from "../../auth/auth.js";

window.addEventListener('load', async ev => {
    await isAuthenticated();
    const user = await getUser();
    let businesses = await getBusinesses();
    businesses = businesses.filter(business => business['name'] !== user['businessName'])
    console.log(user)
    console.log(businesses)
    fillForm(user, businesses);

})

window.addEventListener('submit', async ev => {
    ev.preventDefault();
    const form = document.getElementById('userForm');
    console.log(form.elements['business-select'].value);
    await putUpdate()
})
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

const fillForm = (user, businesses) => {
    document.getElementById('uuid').setAttribute('value', user['uuid']);
    document.getElementById('email').setAttribute('value', user['email']);
    document.getElementById('firstName').setAttribute('value', user['firstName']);
    document.getElementById('lastName').setAttribute('value', user['lastName']);
    const select = document.getElementById('business-select');
    const currentBusiness = document.createElement('option');
    currentBusiness.setAttribute('value', user['businessUuid']);
    currentBusiness.innerText = user['businessName'];
    select.appendChild(currentBusiness);
    businesses.forEach(business => {
        const option = document.createElement('option');
        option.setAttribute('value', business['uuid']);
        option.innerText = business['name'];
        select.appendChild(option);
    });
}

const putUpdate = async () => {
    const form = document.getElementById('userForm');
    const body = {
        email: form.elements['email'].value,
        firstName: form.elements['firstName'].value,
        lastName: form.elements['lastName'].value,
        business: form.elements['business-select'].value
    }
    console.log(body)
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