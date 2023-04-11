import {getJwtPayload} from "../auth/auth.js";

export const verifyAdmin = async () => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    if (role.some(role => role['name'] !== 'ADMIN')) {
    }
}