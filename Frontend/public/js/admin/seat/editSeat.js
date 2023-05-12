import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

const cancel = document.querySelector('.cancelButton');
const loadIcon = document.getElementById('loadIcon');
const registerButton = document.getElementById('registerButton');

cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/admin/seat`);
});

window.addEventListener('load', async () => {
    await isAuthenticatedWithRedirect();
    const seat = await getSeat();

    fillForm(seat);

})


window.addEventListener('submit', async ev => {
    ev.preventDefault();
    const loadText = document.getElementById('loadText');
    loadText.innerText = '';
    loadText.classList.add('none');
    loadIcon.classList.remove('none');
    registerButton.style.pointerEvents = 'none';

    try {
        const post = putUpdate();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        loadText.innerText = 'Seatrapporten har uppdaterats.';
        loadText.style.color = 'green';
    } catch (e) {
        loadText.innerText = 'Seatrapportuppdatering misslyckades. Försök igen senare eller kontakta admin.';
        loadText.style.color = 'red';
    }
    loadText.classList.remove('none');
    loadIcon.classList.add('none');
    registerButton.style.pointerEvents = 'auto';
})


const getSeat = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('seat');
    const url = `${userApiUrl}/api/admin/seat/${uuid}`
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

const fillForm = (seat) => {
    document.getElementById('uuid').setAttribute('value', seat['uuid']);
    document.getElementById('businessBaseline').setAttribute('value', seat['businessBaseline']);
    document.getElementById('businessName').setAttribute('value', seat['businessName']);
    document.getElementById('seatUsed').setAttribute('value', seat['seatUsed'] ?? '');
    document.getElementById('completedByEmail').setAttribute('value', seat['completedByEmail'] ?? 'Ingen');
    document.getElementById('forYearMonth').setAttribute('value', seat['forYearMonth']);
    document.getElementById('lastChangeDate').setAttribute('value', seat['lastChangeDate'] ?? 'Ingen');
    document.getElementById('status').setAttribute('value', seat['status']);


}

const putUpdate = async () => {
    const form = document.getElementById('form');
    const body = {
        usedSeat: form.elements['seatUsed'].value,
        status: form.elements['status'].value

    }
    console.log(body)
    await fetch(`${userApiUrl}/api/admin/seat/${form.elements['uuid'].value}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': "application/json",
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        },
        body: JSON.stringify(body)
    })
    // .then(response => )
    // .catch(error => {
    //     console.error(error);
    //     throw Error('Something went wrong updating account: ' + error)
    // });
}