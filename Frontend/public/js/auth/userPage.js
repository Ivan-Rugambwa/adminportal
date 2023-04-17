import {getJwtPayload, isAuthenticated, loginWithRedirect} from "./auth.js";
import {baseUrl} from "../shared.js";

window.addEventListener('load', async ev => {
    while (true) {
        const timer = await new Promise(r => setTimeout(r, 900000));
        await userPage();
    }
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