import {getJwtPayload} from "./auth.js";
import {baseUrl} from "../shared.js";

window.addEventListener('load', async ev => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'ADMIN')) {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
        console.log('Not authorized for admin pages');
    } else {
        document.querySelector('html').style.display = 'block';
    }

})