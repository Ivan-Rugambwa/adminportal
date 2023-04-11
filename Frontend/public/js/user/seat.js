import {getJwtPayload, isUser, verifyJwt} from "../auth/auth";

window.addEventListener('load', async ev => {
    if (!(await isUser())) {
        window.location.assign('http://localhost:3000/unauthorized')
    }
    const statusCode = await verifyJwt();
    console.log(statusCode)
    if (statusCode !== 200) {
        window.location.assign('http://localhost:3000/unauthorized')
    } else {
        await setName();
        const seat = await getSeat();
        console.log(seat)
        fillSeat(seat);


    }
})

const getSeat = async () => {
    const uuid = '65ff0289-9e00-4574-9f34-8f24b9a03ef4';
    const url = 'http://83.233.216.66:35462/api/admin/seat/' + uuid;
    const token = window.localStorage.getItem("jwt");
    let res;

    await fetch(url, {
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + token,
        }
    })
        .then(response => {
            if (response.ok) {
                res = response.json()
            } else throw Error("Error getting seat: " + response.status)
        })
        .catch(error => {
            console.error(error)
        })
    return res;
}

async function setName() {

    const data = await getJwtPayload();
    document.getElementById("account").innerText += data['sub'];

}

const fillSeat = (seat) => {

    document.getElementById('for').innerText = `${seat.businessName} - ${seat.forYearMonth}`;

    document.getElementById("uuid").innerText = seat.uuid;
    document.getElementById("businessName").innerText = seat.businessName;
    document.getElementById("completedByEmail").innerText = (seat.completedByEmail) ?? 'Ej ifylld';
    document.getElementById("isCompleted").innerText = (seat.isCompleted) ? 'Ja' : 'Nej';
    document.getElementById("lastChangeDate").innerText = (seat.lastChangeDate ?? 'Har ej ändrats');
    document.getElementById("seatUsed").innerText = (seat.seatUsed ?? 'Seat har inte fyllts i än');
    document.getElementById("forYearMonth").innerText = seat.forYearMonth;
}