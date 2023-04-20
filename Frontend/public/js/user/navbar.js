import {baseUrl} from "../shared.js";
import {getJwtPayload, logoutUser} from "../auth/auth.js";

window.addEventListener('load', async () => {
    await createNavBar();
    logoutListener();
})

export const createNavBar = async () => {
    const jwt = await getJwtPayload();
    const leftLogoDiv = document.createElement('div');
    leftLogoDiv.setAttribute('id', 'navLogo');
    const midDiv = document.createElement('div');
    midDiv.setAttribute('id', 'navUserName');
    const rightLinkDiv = document.createElement('div');
    rightLinkDiv.setAttribute('id', 'navLinks');

    const icon = document.createElement('img');
    icon.setAttribute('src', '../../images/apendo-logga-green-large.png');
    icon.setAttribute('id', 'navIcon');
    const navTitle = document.createElement('div');
    navTitle.setAttribute('id', 'navTitle');
    navTitle.innerText = 'Seatportal';

    midDiv.innerText = `${jwt['firstName']} ${jwt['lastName']}\n${jwt['organization']}`;

    const nav = document.querySelector('nav');
    const seat = document.createElement('a');
    const logout = document.createElement('a');
    logout.setAttribute('id', 'logout');


    seat.innerText = 'Seat \nRapporter';
    logout.innerText = 'Logga ut';

    seat.setAttribute('class', 'navLink');
    logout.setAttribute('class', 'navLink');

    seat.classList.add('active');


    seat.setAttribute('href', `${baseUrl}/seat`);
    logout.setAttribute('href', `${baseUrl}/`);

    leftLogoDiv.appendChild(icon)
    leftLogoDiv.appendChild(navTitle);


    rightLinkDiv.appendChild(seat);
    rightLinkDiv.appendChild(logout);

    nav.appendChild(leftLogoDiv);
    nav.appendChild(midDiv)
    nav.appendChild(rightLinkDiv);
}
const logoutListener = () => {
    let logout = document.getElementById('logout');
    logout.addEventListener('click', ev => {
        ev.preventDefault();
        logoutUser();
    })
}
