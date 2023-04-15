import {baseUrl} from "./shared.js";

window.addEventListener('load', ev => {
    createNavBar();
})

export const createNavBar = () => {
    const nav = document.querySelector('nav');
    const home = document.createElement('a');
    const user = document.createElement('a');
    const business = document.createElement('a');
    const seat = document.createElement('a');
    const logout = document.createElement('a');

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

    nav.appendChild(home);
    nav.appendChild(user);
    nav.appendChild(business);
    nav.appendChild(seat);
    nav.appendChild(logout);
}