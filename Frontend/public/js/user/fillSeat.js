import {getJwtPayload, isUser, verifyJwt} from "../auth/auth.js";

const baseUrl = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");


const getSeat = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('uuid');
    const url = 'http://wsprakt3.apendo.se:35462:35462/api/user/seat/' + uuid;
    const token = window.localStorage.getItem("jwt");
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    let res;

    if (!uuidRegex.test(uuid)) {
        return await getAllSeatsByBusiness();
    } else {
        await fetch(url, {
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(response => {
                if (response.ok) {
                    res = response.json()
                } else {
                    throw Error("Error getting seat: " + response.status)
                }
            })
            .catch(error => {
                console.error(error)
            })
        return res;
    }
}
const fillAll = async (seats) => {
    if (typeof seats === 'undefined') throw Error("Seat is undefined")
    seats.sort((a, b) => {
        return b['forYearMonth'].localeCompare(a['forYearMonth']);
    })
    seats.forEach((seat) => {
        const anchor = document.createElement('a');
        anchor.textContent = `${seat['businessName']} - ${seat['forYearMonth']}`;
        anchor.href = baseUrl + '/seat/report?uuid=' + seat['uuid'];
        document.querySelector('#for').appendChild(anchor);
        document.querySelector('#for').appendChild(document.createElement('br'));
    })
}
const fillSingle = async (seat) => {
    document.querySelector('#for').innerText = `${seat['businessName']} - ${seat['forYearMonth']}`;
    if (seat['status'] === 'COMPLETE') {
        document.getElementById('form-box').innerText = 'Seatanvänding: ' + seat['seatUsed'] +
            '\nDenna rapport har blivit ifylld.' +
            '\nIfylld av: ' + seat['completedByEmail'];
    } else if (seat['status'] === 'REVIEW') {
        document.getElementById('form-box').innerText = 'Seatanvänding: ' + seat['seatUsed'] +
            '\nDenna rapport väntar på att bli godkänd.' +
            '\nIfylld av: ' + seat['completedByEmail'];
    } else {
        await createForm(seat);
    }
}
const fill = async () => {
    const seat = await getSeat();
    const payload = await getJwtPayload();
    document.querySelector('#user').innerText = `Inloggad som\n${payload.sub}`;
    if (Array.isArray(seat)) {
        await fillAll(seat)
    } else {
        await fillSingle(seat)
    }

}
const getAllSeatsByBusiness = async () => {
    const payload = await getJwtPayload();
    console.log(payload['organization'])
    const url = 'http://83.233.216.66:35462/api/user/seat/business/' + payload['organization'];
    let res;
    await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": 'Bearer ' + window.localStorage.getItem('jwt')
        }
    })
        .then(response => res = response.json())
        .catch(error => {
            console.error(error)
        })
    return res;
}

const createForm = async (seat) => {
    const formBox = document.getElementById('form-box')
    formBox.innerHTML = '';
    const payload = await getJwtPayload();
    const form = document.createElement('form');
    form.setAttribute('id', 'seat-form');
    form.setAttribute('name', 'seat-form');

    const seatLabel = document.createElement('label');
    const seatInput = document.createElement('input');
    seatInput.type = 'number';
    seatInput.name = 'seat';
    seatInput.placeholder = 'Skriv in antal';
    seatLabel.innerText = 'Seatanvänding';
    seatLabel.setAttribute('for', 'seat')

    const emailInput = document.createElement('input');
    emailInput.type = 'text';
    emailInput.name = 'email';
    emailInput.value = payload['sub'];
    emailInput.setAttribute('type', 'hidden');

    const uuidInput = document.createElement('input');
    uuidInput.type = 'text';
    uuidInput.name = 'uuid';
    uuidInput.value = seat['uuid'];
    uuidInput.setAttribute('type', 'hidden');

    const forYearMonthInput = document.createElement('input');
    forYearMonthInput.type = 'text';
    forYearMonthInput.name = 'forYearMonth';
    forYearMonthInput.value = seat['forYearMonth'];
    forYearMonthInput.setAttribute('type', 'hidden');

    const businessNameInput = document.createElement('input');
    businessNameInput.type = 'text';
    businessNameInput.name = 'businessName';
    businessNameInput.value = seat['businessName'];
    businessNameInput.setAttribute('type', 'hidden');

    const submitBtn = document.createElement('button');


    submitBtn.type = 'submit';
    submitBtn.textContent = 'Skicka in';

    form.appendChild(seatLabel);
    form.appendChild(document.createElement('br'));
    form.appendChild(seatInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(emailInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(uuidInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(businessNameInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(forYearMonthInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(submitBtn);

    formBox.appendChild(form);
}
window.addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = document.forms['seat-form'];
    const url = 'http://localhost:9001/api/message';
    console.log(url)
    const body = {
        "amountOfSeatsUsed": form['seat'].value,
        "business": form['businessName'].value,
        "email": form['email'].value,
        "forYearMonth": form['forYearMonth'].value,
        "message": "report-response",
        "seatUuid": form['uuid'].value
    }
    console.log(body)
    await fetch(url, {
        method: 'POST',
        headers: {
            "Authorization": 'Bearer ' + window.localStorage.getItem('jwt'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    })
        .catch(error => {
            console.error(error)
        })
    await fill();
})
window.addEventListener('load', async ev => {
    if (!(await isUser())) {
        window.location.assign('http://localhost:3000/auth/unauthorized')
    }
    if (await verifyJwt() === 200) {
        await fill();
    } else {
        window.location.assign(`${baseUrl}/auth/login`)
    }
})


