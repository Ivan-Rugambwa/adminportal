import {isAdmin, verifyJwt} from "../../auth/auth.js";
import {baseUrl, userApiUrl} from "../../shared.js";

const userTable = document.getElementById('userTable');
const register = document.getElementById('registerButton');
let isBlurred = false;

const getUsers = async () => {
    const url = `${userApiUrl}/api/admin/user/only`;
    let res;
    await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    })
        .then(response => res = response.json())
        .catch(error => {
            console.error(error);
        })
    return res;
}

const fillTable = (users) => {
    const userTableBody = document.getElementById('userTableBody');

    users.forEach(user => {
        const uuid = user['uuid'];
        const row = userTableBody.insertRow();
        row.insertCell().innerHTML = uuid;
        row.insertCell().innerHTML = user['email'];
        row.insertCell().innerHTML = user['firstName'];
        row.insertCell().innerHTML = user['lastName'];
        row.insertCell().innerHTML = user['businessName'];
        row.insertCell()
            .appendChild(createEdit(uuid))
            .appendChild(createDelete(uuid));
    })
}

const createEdit = (uuid) => {
    const editSpan = document.createElement('span');
    const edit = document.createElement('a');
    edit.setAttribute('class', 'editIcon');
    const href = `${baseUrl}/admin/user/edit?user=${uuid}`;
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
    delIcon.setAttribute('id', 'delete-' + uuid.toString());
    delSpan.appendChild(delIcon);
    return delSpan;
}

const deleteUser = async (uuid) => {

    const response = await fetch(`${userApiUrl}/api/admin/user/${uuid}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
    })
    if(response.status !== 204){
        const data = await response.json();
        throw new Error('Failed deleting user: ' + data.message)
    }
    await updateUsersTable();
}
const updateUsersTable = async () => {
    document.getElementById('userTableBody').innerHTML = '';
    const users = await getUsers();
    fillTable(users);
}

const confirmDelete = (elementId) => {
    console.log(elementId)
    const deleteButton = document.getElementById(elementId);
    toggleBlur();
}
const toggleBlur = () => {
    const outerConfirmDeletePrompt = document.getElementById('outerConfirmDeletePrompt');
    if (isBlurred){
        outerConfirmDeletePrompt.style.display = 'none';
        console.log('is true')
        isBlurred = false;
    } else {
        console.log('is false')
        outerConfirmDeletePrompt.style.display = 'block';
        isBlurred = true;
    }
}

window.addEventListener('load', async ev => {
    await updateUsersTable()
})

userTable.addEventListener('click', async ev => {
    if (ev.target.getAttribute('class').includes('deleteButton')) {
        ev.preventDefault();
        confirmDelete(ev.target['id'], isBlurred);
        if (isBlurred) {
            // await deleteUser(ev.target['id'])
        }
    }
})

const cancel = document.getElementById('cancel');
cancel.addEventListener('click', ev => {
    ev.preventDefault()
    toggleBlur();
})

const confirm = document.getElementById('confirm');
confirm.addEventListener('click', ev => {
    ev.preventDefault()
    toggleBlur();
})


