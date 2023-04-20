import {baseUrl, userApiUrl} from "../../shared.js";
import {getJwtPayload, isAuthenticatedWithRedirect} from "../../auth/auth.js";

let isBlurred = false;

const getUsers = async () => {
    const url = `${userApiUrl}/api/admin/user`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    });
    let users = await response.json();


    users.sort((a, b) => {
        if (a['businessName'] === null) return +1;
        if (b['businessName'] === null) return -1;
        return a['businessName'].localeCompare(b['businessName'])
    });
    return users;
}

const getPending = async () => {
    const url = `${userApiUrl}/api/admin/preregister`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    });
    let pendingUsers = await response.json();

    pendingUsers.sort((a, b) => {
        return a['businessName'].localeCompare(b['businessName'])
    });
    return pendingUsers;
}

const fillTable = async (users, pendingUsers) => {
    document.getElementById('userTableBody').innerHTML = '';
    document.getElementById('adminTableBody').innerHTML = '';
    document.getElementById('pendingUsersTableBody').innerHTML = '';
    const jwt = await getJwtPayload();
    const jwtEmail = jwt['sub'];
    let tableBody;

    users.forEach(user => {
        if (user['roleNames'].some(roleName => roleName === 'ADMIN')) {
            tableBody = document.getElementById('adminTableBody');
        } else {
            tableBody = document.getElementById('userTableBody');
        }
        const uuid = user['uuid'];
        const row = tableBody.insertRow();
        row.insertCell().innerHTML = uuid;
        row.insertCell().innerHTML = user['email'];
        row.insertCell().innerHTML = user['firstName'];
        row.insertCell().innerHTML = user['lastName'];
        if (tableBody.id !== 'adminTableBody') {
            row.insertCell().innerHTML = user['businessName'];
        }
        if (user['email'] !== jwtEmail) {
            row.insertCell()
                .appendChild(createEdit(uuid, 'user'))
                .appendChild(createDelete(uuid, 'user'));
        } else {
            row.insertCell().innerHTML = 'N/A';
        }

    })
    let pendingUsersTableBody;
    pendingUsers.forEach(pending => {
        if (pending['roleName'] === 'ADMIN') {
            pendingUsersTableBody = document.getElementById('pendingAdminsTableBody');
        } else {
            pendingUsersTableBody = document.getElementById('pendingUsersTableBody');
        }
        const uuid = pending['uuid'];
        const row = pendingUsersTableBody.insertRow();
        row.insertCell().innerHTML = uuid;
        row.insertCell().innerHTML = pending['email'];
        row.insertCell().innerHTML = pending['firstName'];
        row.insertCell().innerHTML = pending['lastName'];
        if (pendingUsersTableBody.id !== 'pendingAdminsTableBody') {
            row.insertCell().innerHTML = pending['businessName'];
        }
        row.insertCell()
            .appendChild(createEdit(uuid, 'pending'))
            .appendChild(createDelete(uuid, 'pending'));
    })
}

const createEdit = (uuid, type) => {
    const editSpan = document.createElement('span');
    const edit = document.createElement('a');
    edit.setAttribute('class', 'editIcon');
    const href = (type === 'user') ? `${baseUrl}/admin/user/edit?user=${uuid}` : `${baseUrl}/admin/pending/edit?user=${uuid}`;
    const editIcon = document.createElement('i');
    editIcon.setAttribute('class', 'fa-regular fa-pen-to-square');
    editIcon.setAttribute('type', type);
    edit.appendChild(editIcon);
    edit.href = href;
    editSpan.appendChild(edit);
    return editSpan;
}
const createDelete = (uuid, type) => {
    const delSpan = document.createElement('span');
    const delIcon = document.createElement('button');
    delIcon.setAttribute('type', 'button');
    delIcon.setAttribute('class', 'fa-solid fa-trash deleteButton');
    delIcon.setAttribute('style', 'color: #ff2828;');
    delIcon.setAttribute('uuid', uuid.toString());
    delIcon.setAttribute('type', type);
    delSpan.appendChild(delIcon);
    return delSpan;
}

const deleteUser = async (uuid) => {

    const response = await fetch(`${userApiUrl}/api/admin/user/${uuid}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    });
    if (response.status !== 204) {
        const data = await response.json();
        throw new Error('Failed deleting user: ' + data.message)
    }
}
const deletePending = async (uuid) => {
    const response = await fetch(`${userApiUrl}/api/admin/preregister/${uuid}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    });
    if (response.status !== 204) {
        const data = await response.json();
        throw new Error('Failed deleting user: ' + data.message)
    }
}
const updateTables = async () => {
    document.getElementById('userTableBody').innerHTML = '';
    document.getElementById('adminTableBody').innerHTML = '';
    document.getElementById('pendingUsersTableBody').innerHTML = '';
    refresh.style.cursor = 'wait';
    refresh.disabled = true;
    const spinning = 'fa-solid fa-arrow-rotate-right fa-spin';
    const still = 'fa-solid fa-arrow-rotate-right';
    const refreshIcon = document.getElementById('refreshIcon');
    refreshIcon.setAttribute('class', spinning);
    refreshIcon.style.pointerEvents = 'none';

    const users = getUsers();
    const pendingUsers = getPending();
    const promises = await Promise.all([users, pendingUsers, new Promise(r => setTimeout(r, 400))])

    const timer = new Promise(r => setTimeout(r, 1600));
    fillTable(promises[0], promises[1]);

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
    await updateTables();
})

const tables = document.getElementById('tables');
const cancel = document.getElementById('cancel');
const confirm = document.getElementById('confirm');
const refresh = document.getElementById('refresh');

tables.addEventListener('click', async ev => {
    if (ev.target.classList.contains('deleteButton')) {
        ev.preventDefault();
        const uuid = ev.target.getAttribute('uuid');
        const type = ev.target.getAttribute('type');
        console.log(uuid)
        document.getElementById('confirm').setAttribute('uuid', uuid);
        document.getElementById('confirm').setAttribute('type', type);
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
    const type = ev.target.getAttribute('type');
    if (type === 'pending') {
        await deletePending(uuid);
    } else {
        await deleteUser(uuid);
    }
    await updateTables();
})

refresh.addEventListener('click', async ev => {
    ev.preventDefault();
    await isAuthenticatedWithRedirect();
    await updateTables();
})