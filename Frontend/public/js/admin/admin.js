import {isAdmin, verifyJwt} from "../auth/auth.js";
import {baseUrl, userApiUrl} from "../shared.js";

const userTable = document.getElementById('userTable');

window.addEventListener('load', async ev => {
    const users = await getUsers();
    fillTable(users);
})

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
        const row = userTableBody.insertRow();
        row.insertCell().innerHTML = user['uuid'];
        row.insertCell().innerHTML = user['email'];
        row.insertCell().innerHTML = user['firstName'];
        row.insertCell().innerHTML = user['lastName'];
        row.insertCell().innerHTML = user['businessName'];
        row.insertCell()
            .appendChild(createEdit(user))
            .appendChild(createDelete(user));
    })
}

const createEdit = (user) => {
    const editSpan = document.createElement('span');
    const edit = document.createElement('a');
    edit.setAttribute('class', 'editIcon')
    const href = `${baseUrl}/admin/user/edit?user=${user['uuid']}`;
    const editIcon = document.createElement('i')
    editIcon.setAttribute('class', 'fa-regular fa-pen-to-square')
    edit.appendChild(editIcon)
    edit.href = href;
    editSpan.appendChild(edit);
    return editSpan;
}
const createDelete = (user) => {
    const delSpan = document.createElement('span');
    const del = document.createElement('a');
    del.setAttribute('class', 'deleteIcon')
    const href = `${baseUrl}/admin/user/${user['uuid']}`;
    const delIcon = document.createElement('i')
    delIcon.setAttribute('class', 'fa-solid fa-trash')
    delIcon.setAttribute('style', 'color: #ff2828;')
    del.appendChild(delIcon)
    del.href = href;
    delSpan.appendChild(del);
    return delSpan;
}