import {getJwtPayload, isAuthenticatedWithRedirect, loginWithRedirect} from "./auth.js";
import {baseUrl} from "../shared.js";

window.addEventListener('load', async () => {
    setInterval(async () => {
        await userPage();
    }, 900000)
})

const userPage = async () => {
    if (await isAuthenticatedWithRedirect() === false) {
        loginWithRedirect();
    }
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'USER')) {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
        console.log('Not authorized for user pages');
    }
}