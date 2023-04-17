import {getJwtPayload, isAuthenticated} from "./auth.js";
import {baseUrl} from "../shared.js";

// window.addEventListener('click', async ev => {
//     ev.preventDefault();
//     console.log(ev.target.id);
//     console.log(ev.target.id.value);
//     await adminPage();
// })

window.addEventListener('load', async ev => {
    while (true) {
        const timer = await new Promise(r => setTimeout(r, 900000));
        await adminPage();
    }
})
export const adminPage = async () => {
    if (await isAuthenticated() === false) {
        console.log('bad');
        // loginWithRedirect();
    }
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'ADMIN')) {
        window.location.assign(`${baseUrl}/auth/unauthorized`);
        console.log('Not authorized for admin pages');
    }
}
