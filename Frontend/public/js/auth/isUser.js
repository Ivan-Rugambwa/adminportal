import {getJwtPayload} from "./auth.js";
import {baseUrl} from "../shared.js";

window.addEventListener('load', async ev => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'USER')) {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
        console.log('Not authorized for user pages');
    }
})