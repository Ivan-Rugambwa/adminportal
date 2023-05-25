import {getJwtPayload, isAuthenticatedWithRedirect, isUser} from "../auth/auth.js";
import {baseUrl, userApiUrl} from "../shared.js";

const getSeat = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('uuid');
    const url = `${userApiUrl}/api/user/seat/${uuid}`;
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
        createSeatRow(seat);
    });

}
const createSeatRow = (seat) => {
    const seatFrame = document.getElementById('seatFrame');
    const anchor = document.createElement('a');
    anchor.setAttribute('class', 'seatLink');
    const seatName = document.createElement('div');
    seatName.setAttribute('class', 'seatName');
    const seatStatus = document.createElement('div');
    seatStatus.setAttribute('class', 'seatStatus');

    seatName.innerText = `${seat['businessName']} - ${seat['forYearMonth']}`;
    anchor.href = `${baseUrl}/seat/report?uuid=${seat['uuid']}`;

    seatStatus.innerText = (seat['status'] === 'FILL') ? 'Ej ifylld' : (seat['status'] === 'REVIEW') ? 'Granskas' : 'Godkänd';
    switch (seat['status']) {
        case 'FILL':
            seatStatus.innerText = 'Ej ifylld';
            seatStatus.style.backgroundColor = '#E34234';
            break;
        case 'REVIEW':
            seatStatus.innerText = 'Granskas';
            seatStatus.style.backgroundColor = '#FAE500';
            break;
        case 'COMPLETE':
            seatStatus.innerText = 'Godkänd';
            seatStatus.style.backgroundColor = '#99CC00';
            break;
    }

    anchor.appendChild(seatName);
    anchor.appendChild(seatStatus);
    seatFrame.appendChild(anchor);
}
// const fillSingle = async (seat) => {
//     if (seat['status'] === 'COMPLETE') {
//         document.getElementById('form-box').innerText = 'Seatanvänding: ' + seat['seatUsed'] +
//             '\nDenna rapport har blivit godkänd.' +
//             '\nIfylld av: ' + seat['completedByEmail'];
//     } else if (seat['status'] === 'REVIEW') {
//         document.getElementById('form-box').innerText = 'Seatanvänding: ' + seat['seatUsed'] +
//             '\nDenna rapport väntar på att bli godkänd.' +
//             '\nIfylld av: ' + seat['completedByEmail'];
//     } else {
//         await createForm(seat);
//     }
// }
const fill = async () => {
    const seat = await getSeat();
    if (Array.isArray(seat)) {
        await fillAll(seat)
    }
    // else {
    //     await fillSingle(seat)
    // }

}
const getAllSeatsByBusiness = async () => {
    const payload = await getJwtPayload();
    const url = `${userApiUrl}/api/user/seat/business/${payload['organization']}`;
    let res;
    await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": 'Bearer ' + window.localStorage.getItem('jwt')
        }
    })
        .then(response => res = response.json())
        .catch(error => {
            console.error(error);
        })
    return res;
}

// const createForm = async (seat) => {
//     const formBox = document.getElementById('form-box')
//     formBox.innerHTML = '';
//     const payload = await getJwtPayload();
//     const form = document.createElement('form');
//     form.setAttribute('id', 'seat-form');
//     form.setAttribute('name', 'seat-form');
//
//     const seatLabel = document.createElement('label');
//     const seatInput = document.createElement('input');
//     seatInput.type = 'number';
//     seatInput.name = 'seat';
//     seatInput.placeholder = 'Skriv in antal';
//     seatLabel.innerText = 'Seatanvänding';
//     seatLabel.setAttribute('for', 'seat')
//
//     const emailInput = document.createElement('input');
//     emailInput.type = 'text';
//     emailInput.name = 'email';
//     emailInput.value = payload['sub'];
//     emailInput.setAttribute('type', 'hidden');
//
//     const uuidInput = document.createElement('input');
//     uuidInput.type = 'text';
//     uuidInput.name = 'uuid';
//     uuidInput.value = seat['uuid'];
//     uuidInput.setAttribute('type', 'hidden');
//
//     const forYearMonthInput = document.createElement('input');
//     forYearMonthInput.type = 'text';
//     forYearMonthInput.name = 'forYearMonth';
//     forYearMonthInput.value = seat['forYearMonth'];
//     forYearMonthInput.setAttribute('type', 'hidden');
//
//     const businessNameInput = document.createElement('input');
//     businessNameInput.type = 'text';
//     businessNameInput.name = 'businessName';
//     businessNameInput.value = seat['businessName'];
//     businessNameInput.setAttribute('type', 'hidden');
//
//     const submitBtn = document.createElement('button');
//
//
//     submitBtn.type = 'submit';
//     submitBtn.textContent = 'Skicka in';
//
//     form.appendChild(seatLabel);
//     form.appendChild(document.createElement('br'));
//     form.appendChild(seatInput);
//     form.appendChild(document.createElement('br'));
//     form.appendChild(emailInput);
//     form.appendChild(document.createElement('br'));
//     form.appendChild(uuidInput);
//     form.appendChild(document.createElement('br'));
//     form.appendChild(businessNameInput);
//     form.appendChild(document.createElement('br'));
//     form.appendChild(forYearMonthInput);
//     form.appendChild(document.createElement('br'));
//     form.appendChild(submitBtn);
//
//     formBox.appendChild(form);
// }
window.addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = document.forms['seat-form'];
    const url = 'http://localhost:9001/api/message';
    const body = {
        "amountOfSeatsUsed": form['seat'].value,
        "business": form['businessName'].value,
        "email": form['email'].value,
        "forYearMonth": form['forYearMonth'].value,
        "message": "report-response",
        "seatUuid": form['uuid'].value
    }
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
window.addEventListener('load', async () => {
    if (!(await isUser())) {
        window.location.assign(`${baseUrl}/auth/unauthorized`)
    }
    if (await isAuthenticatedWithRedirect() === false) {
    }
    await fill();
})


