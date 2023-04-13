import {isAdmin, verifyJwt} from "../../auth/auth.js";
import {baseUrl, userApiUrl} from "../../shared.js";

const userTable = document.getElementById('userTable');


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
    edit.setAttribute('class', 'editIcon')
    const href = `${baseUrl}/admin/user/edit?user=${uuid}`;
    const editIcon = document.createElement('i')
    editIcon.setAttribute('class', 'fa-regular fa-pen-to-square')
    edit.appendChild(editIcon)
    edit.href = href;
    editSpan.appendChild(edit);
    return editSpan;
}
const createDelete = (uuid) => {
    const delSpan = document.createElement('span');
    const delIcon = document.createElement('button')
    delIcon.setAttribute('type', 'button')
    delIcon.setAttribute('class', 'fa-solid fa-trash deleteButton')
    delIcon.setAttribute('style', 'color: #ff2828;')
    delIcon.setAttribute('id', uuid.toString())
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
const waitForElm = (selector) => {
    return new Promise(resolve => {
        if (document.querySelector(selector)) {
            return resolve(document.querySelector(selector));
        }

        const observer = new MutationObserver(mutations => {
            if (document.querySelector(selector)) {
                resolve(document.querySelector(selector));
                observer.disconnect();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    });
}
const updateUsersTable = async () => {
    document.getElementById('userTableBody').innerHTML = '';
    const users = await getUsers();
    fillTable(users);
}

window.addEventListener('load', async ev => {
    await updateUsersTable()
})

window.addEventListener('click', async ev => {
    console.log(ev.target)
    if (ev.target.getAttribute('class').includes('deleteButton')) {
        ev.preventDefault();
        await deleteUser(ev.target['id'])
    }

})