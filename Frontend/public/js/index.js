import {isAdmin, isUser} from "./auth/auth.js";
import {baseUrl} from "./shared.js";

const login = document.getElementById('login');
login.setAttribute(`href`,`${baseUrl}/auth/login`);

const admin = document.getElementById('admin');
admin.setAttribute(`href`,`${baseUrl}/admin`);

const user = document.getElementById('user');
user.setAttribute(`href`, `${baseUrl}/seat/report`);

admin.addEventListener('click', async ev => {
    const baseUrl = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");
    ev.preventDefault();

    if (await isAdmin()) {
        window.location.assign(`${baseUrl}/admin`);
    } else {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
    }
})

user.addEventListener('click', async ev => {
    const baseUrl = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");
    ev.preventDefault();

    if (await isUser()) {
        window.location.assign(`${baseUrl}/seat/report`);
    } else {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
    }
})