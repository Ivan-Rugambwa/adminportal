import {isAdmin, isUser} from "./auth/auth.js";


const admin = document.getElementById('admin');
admin.addEventListener('click', async ev => {
    const baseUrl = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");
    ev.preventDefault();

    if (await isAdmin()) {
        window.location.assign("http://localhost:3000/admin")
    } else {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
    }
})

const user = document.getElementById('user');
user.addEventListener('click', async ev => {
    const baseUrl = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");
    ev.preventDefault();

    if (await isUser()) {
        window.location.assign("http://localhost:3000/seat/report")
    } else {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
    }
})