import {userApiUrl} from "../shared.js";

window.addEventListener('load', async ev => {
    const user = await getUser();
    console.log(user)
    fillForm(user);

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

const fillForm = (user) => {
    document.getElementById('email').setAttribute('value', user['email']);
    document.getElementById('firstName').setAttribute('value', user['firstName']);
    document.getElementById('lastName').setAttribute('value', user['lastName']);
    document.getElementById('businessName').setAttribute('value', user['businessName']);
}