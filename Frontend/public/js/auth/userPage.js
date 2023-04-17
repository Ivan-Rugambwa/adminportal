import {getJwtPayload, isAuthenticated, loginWithRedirect} from "./auth.js";
import {baseUrl} from "../shared.js";

window.addEventListener('load', async ev => {
    await userPage();
})

window.addEventListener('click', async ev => {
    await userPage();
})

const userPage = async () => {
    if (await isAuthenticated() === false) {
        loginWithRedirect();
    }
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'USER')) {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
        console.log('Not authorized for user pages');
    }
}