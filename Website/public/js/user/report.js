import {baseUrl, camundaApiUrl, userApiUrl} from "../shared.js";
import {getJwtPayload, isAuthenticatedWithRedirect, isUser} from "../auth/auth.js";

const cancel = document.querySelector('.cancelButton');
const loadText = document.getElementById('loadText');
const loadIcon = document.getElementById('loadIcon');
const registerButton = document.getElementById('registerButton');

window.addEventListener('submit', async ev => {
    ev.preventDefault();
    loadText.innerText = '';
    loadText.classList.add('none');
    loadIcon.classList.remove('none');
    registerButton.style.pointerEvents = 'none';
    try {
        const post = postReport();
        await Promise.all([post, new Promise(r => setTimeout(r, 1000))])
        loadText.innerText = 'Rapporten har skickats.';
        loadText.style.color = 'green';
        const seat = await getSeat();
        fillForm(seat);
    } catch (e) {
        loadText.innerText = 'Kunde inte skicka rapporten, försök igen senare eller kontakta support.';
        loadText.style.color = 'red';
    }
    loadText.classList.remove('none');
    loadIcon.classList.add('none');
    registerButton.style.pointerEvents = 'auto';
})

cancel.addEventListener('click', ev => {
    ev.preventDefault();
    window.location.assign(`${baseUrl}/seat`);
});

window.addEventListener('load', async () => {
    if (await isAuthenticatedWithRedirect() === false) {
    }
    if (!(await isUser())) {
        window.location.assign(`${baseUrl}/auth/unauthorized`)
    }
    const seat = await getSeat();
    fillForm(seat);
});
const postReport = async () => {
    const jwt = await getJwtPayload();
    const uuid = document.getElementById('uuid').innerText;
    const changedBy = document.getElementById('changedBy').innerText;
    const forYearMonth = document.getElementById('forYearMonth').innerText;
    const business = document.getElementById('business').innerText;
    const seatAmount = document.getElementById('seatAmount').innerText;
    const body = {
        "message": "report-response",
        "email": changedBy,
        "forYearMonth": forYearMonth,
        "business": business,
        "seatUuid": uuid,
        "amountOfSeatsUsed": seatAmount,
        "firstName": jwt['firstName'],
        "lastName": jwt['lastName']
    }
    let response;

    response = await fetch(`${camundaApiUrl}/api/message`, {
        method: 'POST',
        headers: {
            "Authorization": "Bearer " + window.localStorage.getItem('jwt'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    });
    if (response.status !== 200) {
        throw Error('something went wrong');
    }
};

const seatInput = document.getElementById('amount');
seatInput.addEventListener('input', async () => {
    const jwt = await getJwtPayload();
    const seatAmount = document.getElementById('seatAmount');
    seatAmount.innerText = (seatInput.value === '') ? 'Ej ifylld' : seatInput.value;
    document.getElementById('changedBy').innerText = (seatAmount.innerText === 'Ej ifylld') ? 'Ej ifylld' : jwt['sub'];
    document.getElementById('status').innerText = (seatAmount.innerText === 'Ej ifylld') ? 'Ej ifylld' : 'Ej inskickad';
    await validateSeatInput(seatInput.value);
});

const validateSeatInput = async (seatAmount) => {
    const baseline = parseInt(document.getElementById('baseline').innerText);
    const seatAmountValue = parseInt(seatAmount);
    if (seatAmountValue > baseline) {
        const amount = document.getElementById('amount');
        const warningText = document.getElementById('extraWarning');
        const overuseText = document.getElementById('overuseText');
        amount.classList.add('warning');
        overuseText.innerText = `${seatAmountValue} - ${baseline} = ${seatAmountValue - baseline} st extra.`;
        warningText.style.display = 'block';
    } else {
        document.getElementById('overuseText').innerText = '';
        document.getElementById('extraWarning').style.display = 'none';
        document.getElementById('amount').classList.remove('warning');
    }

    if (seatAmountValue < (baseline * 0.5)) {
        const amount = document.getElementById('amount');
        amount.classList.add('warning');
        const lowWarning = document.getElementById('lowWarning');
        lowWarning.style.display = 'block';
    } else {
        document.getElementById('lowWarning').style.display = 'none';
        document.getElementById('amount').classList.remove('warning');
    }

    if (seatAmountValue > (baseline * 1.5)) {
        const amount = document.getElementById('amount');
        amount.classList.add('warning');
        const highWarning = document.getElementById('highWarning');
        highWarning.style.display = 'block';
    } else {
        document.getElementById('highWarning').style.display = 'none';
        document.getElementById('amount').classList.remove('warning');
    }

}

const getSeat = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('uuid');
    if (!uuid) {
        window.location.assign(`${baseUrl}/seat`);
    }
    const url = `${userApiUrl}/api/user/seat/${uuid}`;
    const token = window.localStorage.getItem("jwt");
    let response = await fetch(url, {
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + token,
        }
    });
    const json = await response.json();
    if (response.status !== 200) {
        throw Error('Could not get seat: ' + json.message);
    }

    return json;
}

const fillForm = (seat) => {
    document.getElementById('uuid').innerText = seat['uuid'];
    document.getElementById('baseline').innerText = seat['businessBaseline'];
    document.getElementById('changedBy').innerText = seat['completedByEmail'] ?? 'Ej ifylld';
    document.getElementById('forYearMonth').innerText = seat['forYearMonth'];
    document.getElementById('business').innerText = seat['businessName'];
    document.getElementById('seatAmount').innerText = seat['seatUsed'] ?? 'Ej ifylld';
    document.getElementById('status').innerText = (seat['status'] === 'FILL') ? 'Ej ifylld' : (seat['status'] === 'REVIEW') ? 'Granskas' : 'Godkänd';
    fillText(seat);


}
const fillText = (seat) => {
    const text = document.getElementById('text');
    switch (seat['status']) {
        case 'FILL':
            text.innerText = 'Vänligen fyll i denna månads seat-använding';
            document.getElementById('formFields').style.display = 'block';
            document.getElementById('registerButton').style.display = 'block';
            break;
        case 'REVIEW':
            text.innerText = 'Denna rapport är under granskning';
            document.getElementById('formFields').style.display = 'none';
            document.getElementById('registerButton').style.display = 'none';
            break;
        case 'COMPLETE':
            text.innerText = 'Denna rapport har blivit godkänd!';
            document.getElementById('formFields').style.display = 'none';
            document.getElementById('registerButton').style.display = 'none';
            text.style.color = '#99CC00';
            break;
    }
}