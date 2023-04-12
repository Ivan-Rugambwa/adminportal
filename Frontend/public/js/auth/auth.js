import {baseUrl, userApiUrl} from "../shared.js";

export const verifyJwt = async () => {
    let isValid = 500;
    let jwt = window.localStorage.getItem("jwt");

    if (typeof jwt != 'string' && jwt.trim().length > 0) {
        console.error('No jwt found');
    }
    const url = `${userApiUrl}/api/auth/validate`;

    if (jwt.startsWith('Bearer ')) {
        jwt = jwt.substring(7);
    }

    const data = {token: jwt};
    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                console.log('JWT is valid');
            }
            isValid = response.status;
        })
        .catch(error => {
            console.error('There was an error: ' + error);
        })
    return isValid;
}
export const useRefreshToken = () => {
    let refreshToken = window.localStorage.getItem("refreshToken");
    if (typeof refreshToken != 'string' && refreshToken.trim().length > 0) {
        console.error('No refresh token found');
        return;
    }

    const url = `${userApiUrl}/api/auth/refresh`;
    const data = {refreshToken: refreshToken};

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.status === 200) {
                console.log('Refresh token is valid');
            } else {
                console.error('Refresh token is not valid');
                return;
            }
            return response.json();
        })
        .then(body => {
            window.localStorage.setItem('refreshToken', body.refreshToken);
            window.localStorage.setItem('jwt', body.accessToken);
            console.log("Set new access token and refresh token");
        })
        .catch(error => {
            console.error(error);
            console.error('Failed to refresh tokens');
        })
}
export const login = async () => {
    let email = "harry@stone.com";
    let password = "testtest";
    const url = `${userApiUrl}/api/auth/authenticate`;
    const data = {email: email, password: password};

    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => response.json())
        .then(data => {
            window.localStorage.setItem("jwt", data.accessToken);
            window.localStorage.setItem("refreshToken", data.refreshToken);
        }).catch(error => {
            console.error(error)
        })
}
export const getJwtPayload = async () => {
    if (await verifyJwt() !== 200) {
        window.location.assign(`${baseUrl}/auth/login`);
        throw Error("JWT invalid")
    }
    const base64Url = window.localStorage.getItem("jwt").split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

export const isAdmin = async () => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    return role.some(role => role['name'] === 'ADMIN')
}
export const isUser = async () => {
    const payload = await getJwtPayload();
    const role = payload['role'];
    return role.some(role => role['name'] === 'USER')
}