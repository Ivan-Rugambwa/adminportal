import {baseUrl} from "../shared.js";
import {logoutUser} from "../auth/auth.js";

window.addEventListener('load', async () => {
    createNavBar();
    logoutListener();
})

export const createNavBar = () => {
    const leftLogoDiv = document.createElement('div');
    leftLogoDiv.setAttribute('id', 'navLogo');
    const rightLinkDiv = document.createElement('div');
    rightLinkDiv.setAttribute('id', 'navLinks');

    const icon = document.createElement('img');
    icon.setAttribute('src', '../images/apendo-logga-green-large.png');
    icon.setAttribute('id', 'navIcon');
    const navTitle = document.createElement('div');
    navTitle.setAttribute('id', 'navTitle');
    navTitle.innerText = 'Adminportal';
    const nav = document.querySelector('nav');
    const home = document.createElement('a');
    const user = document.createElement('a');
    const business = document.createElement('a');
    const seat = document.createElement('a');
    const logout = document.createElement('a');
    logout.setAttribute('id', 'logout');

    home.innerText = 'Hem';
    user.innerText = 'Användare';
    business.innerText = 'Företag';
    seat.innerText = 'Seat \nRapporter';
    logout.innerText = 'Logga ut';

    home.setAttribute('class', 'navLink');
    user.setAttribute('class', 'navLink');
    business.setAttribute('class', 'navLink');
    seat.setAttribute('class', 'navLink');
    logout.setAttribute('class', 'navLink');

    const path = window.location.pathname;
    let active = (path === '/admin') ? home
        : (path === '/admin/user') ? user
            : (path === '/admin/business') ? business
                : seat
    active.classList.add('active');

    home.setAttribute('href', `${baseUrl}/admin`);
    user.setAttribute('href', `${baseUrl}/admin/user`);
    business.setAttribute('href', `${baseUrl}/admin/business`);
    seat.setAttribute('href', `${baseUrl}/admin/seat`);
    logout.setAttribute('href', `${baseUrl}/`);

    leftLogoDiv.appendChild(icon)
    leftLogoDiv.appendChild(navTitle);
    rightLinkDiv.appendChild(home);
    rightLinkDiv.appendChild(user);
    rightLinkDiv.appendChild(business);
    rightLinkDiv.appendChild(seat);
    rightLinkDiv.appendChild(logout);

    nav.appendChild(leftLogoDiv);
    nav.appendChild(rightLinkDiv);
}
const logoutListener = () => {
    let logout = document.getElementById('logout');
    logout.addEventListener('click', ev => {
        ev.preventDefault();
        logoutUser();
    })
}
