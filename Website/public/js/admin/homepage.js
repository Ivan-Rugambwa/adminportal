import {isAuthenticatedWithRedirect} from "../auth/auth.js";

window.addEventListener('load', async () => {
    await isAuthenticatedWithRedirect();
})