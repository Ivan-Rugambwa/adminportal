import {getJwtPayload, isAuthenticatedWithRedirect, loginWithRedirect} from "./auth.js";
import {baseUrl} from "../shared.js";

window.addEventListener('load', async () => {
    setInterval(async () => {
        await adminPage();
    }, 900000);
})
export const adminPage = async () => {
    if (await isAuthenticatedWithRedirect() === false) {
        loginWithRedirect();
    }
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'ADMIN')) {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
    }
}
