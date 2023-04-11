import {isAdmin, verifyJwt} from "../auth/auth.js";

const baseUrl = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");
window.addEventListener('load', async ev => {
    console.log('IN EVENTLISTENER')
    if (!(await isAdmin())) {
        return window.location.assign(baseUrl + '/auth/unauthorized');
    }
    const statusCode = await verifyJwt();
    if (statusCode === 401) return window.location.assign(baseUrl + '/auth/login');
    else if (statusCode === 403) return window.location.assign(baseUrl + '/auth/unauthorized');
    else if (statusCode !== 200) return window.location.assign(baseUrl + '/404');
})

function checkLogin() {
    const jwt = localStorage.getItem('jwt');
    if (!jwt) {
        // User is not logged in, redirect to login page
        window.location.href = "localhost:3000/login";
    }
}

//   function logOut() {
//     localStorage.removeItem('jwt');
//     // Redirect the user to the login page
//     window.location.href = "inlogg.html";
//   }