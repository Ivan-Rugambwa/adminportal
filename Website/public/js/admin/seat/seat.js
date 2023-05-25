import {baseUrl, userApiUrl} from "../../shared.js";
import {isAuthenticatedWithRedirect} from "../../auth/auth.js";

const seatTable = document.getElementById('seatTable');

let isBlurred = false;

const getSeats = async () => {
    const url = `${userApiUrl}/api/admin/seat`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    });
    let seats = await response.json();

    seats.sort((a, b) => {
        if (a['businessName'] === b['businessName']) return b['forYearMonth'].localeCompare(a['forYearMonth']);
        return a['businessName'].localeCompare(b['businessName']);
    });
    return seats;
}

const fillTable = (seats) => {
    document.getElementById('seatTableBody').innerHTML = '';
    let tableBody = document.getElementById('seatTableBody');

    seats.forEach(seat => {
        const status = (seat['status'] === "FILL") ? "Ej ifylld" : (seat['status'] === "REVIEW") ? "Granskas" : "Slutförd";

        const uuid = seat['uuid'];
        const row = tableBody.insertRow();
        row.insertCell().innerHTML = uuid;
        row.insertCell().innerHTML = seat['businessName'];
        row.insertCell().innerHTML = seat['businessBaseline'];
        row.insertCell().innerHTML = status;
        row.insertCell().innerHTML = seat['completedByEmail'];
        row.insertCell().innerHTML = seat['lastChangeDate'];
        row.insertCell().innerHTML = seat['seatUsed'];
        row.insertCell().innerHTML = seat['forYearMonth'];
        row.insertCell()
            .appendChild(createEdit(uuid))
            .appendChild(createDelete(uuid));
    })
}

const createEdit = (uuid) => {
    const editSpan = document.createElement('span');
    const edit = document.createElement('a');
    edit.setAttribute('class', 'editIcon');
    const href = `${baseUrl}/admin/seat/edit?seat=${uuid}`;
    const editIcon = document.createElement('i');
    editIcon.setAttribute('class', 'fa-regular fa-pen-to-square');
    edit.appendChild(editIcon);
    edit.href = href;
    editSpan.appendChild(edit);
    return editSpan;
}
const createDelete = (uuid) => {
    const delSpan = document.createElement('span');
    const delIcon = document.createElement('button');
    delIcon.setAttribute('type', 'button');
    delIcon.setAttribute('class', 'fa-solid fa-trash deleteButton');
    delIcon.setAttribute('style', 'color: #ff2828;');
    delIcon.setAttribute('uuid', uuid.toString());
    delSpan.appendChild(delIcon);
    return delSpan;
}

const deleteSeat = async (uuid) => {

    const response = await fetch(`${userApiUrl}/api/admin/seat/${uuid}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    })
    if (response.status !== 204) {
        const data = await response.json();
        throw new Error('Failed deleting user: ' + data.message)
    }
    await updateTables();
}
const updateTables = async () => {
    document.getElementById('seatTableBody').innerHTML = '';
    refresh.style.cursor = 'wait';
    refresh.disabled = true;
    const spinning = 'fa-solid fa-arrow-rotate-right fa-spin';
    const still = 'fa-solid fa-arrow-rotate-right';
    const refreshIcon = document.getElementById('refreshIcon');
    refreshIcon.setAttribute('class', spinning);
    refreshIcon.style.pointerEvents = 'none';

    const seats = getSeats();
    const promises = await Promise.all([seats, new Promise(r => setTimeout(r, 400))])

    const timer = new Promise(r => setTimeout(r, 1600));
    fillTable(promises[0]);

    await Promise.all([timer]);
    refreshIcon.setAttribute('class', still);
    refreshIcon.style.pointerEvents = 'auto';
    refresh.disabled = false;
    refresh.style.cursor = 'pointer';
}

const toggleBlur = () => {
    const outerConfirmDeletePrompt = document.getElementById('outerConfirmDeletePrompt');
    const blur = document.getElementById('blur');
    if (isBlurred) {
        blur.style.display = 'none';
        outerConfirmDeletePrompt.style.display = 'none';
        isBlurred = false;
    } else {
        blur.style.display = 'block';
        outerConfirmDeletePrompt.style.display = 'block';
        isBlurred = true;
    }
}

window.addEventListener('load', async () => {
    await isAuthenticatedWithRedirect();
    await updateTables()
    console.log("loading tables");
})


const cancel = document.getElementById('cancel');
const confirm = document.getElementById('confirm');
const refresh = document.getElementById('refresh');

seatTable.addEventListener('click', async ev => {
    if (ev.target.getAttribute('class').includes('deleteButton')) {
        ev.preventDefault();
        const uuid = ev.target.getAttribute('uuid');
        console.log(uuid)
        document.getElementById('confirm').setAttribute('uuid', uuid);
        toggleBlur();
    }
})

cancel.addEventListener('click', ev => {
    ev.preventDefault()
    toggleBlur();
})

confirm.addEventListener('click', async ev => {
    ev.preventDefault()
    toggleBlur();
    const uuid = ev.target.getAttribute('uuid');
    await deleteBusiness(uuid);
    await updateTables();
})

refresh.addEventListener('click', async ev => {
    ev.target.disabled = true;
    ev.preventDefault();
    await updateTables();
    ev.target.disabled = false;
})


