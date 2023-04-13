import { baseUrl, userApiUrl } from "../shared.js";


const get = document.getElementById("getUser");
get.addEventListener("click",ev =>{
  ev.preventDefault()
  getUser();
})

function getUser() {
  const userId = document.getElementById("uuid").value;
  const firstName = document.getElementById("firstName").value;
  const lastName = document.getElementById("lastName").value;
  const email = document.getElementById("email").value;
  const url = `${userApiUrl}/api/admin/user`;


  fetch(url, {
    method: "GET",
    headers: {
      'Authorization': `Bearer ${window.localStorage.getItem("jwt")}`,
      "Content-Type": "application/json",
    },
  })
    .then(response => response.json())
    .then(data => {
      console.log(data);
      const tableBody = document.getElementById("myTable");
      // remove any existing rows from the table
      //  tableBody.innerHTML = '';
      // loop through the data and create a new row for each object
      data.forEach(obj => {
        const row = document.createElement('tr');
        const uuidCell = document.createElement('td');
        const firstNameCell = document.createElement('td');
        const lastNameCell = document.createElement('td');
        const emailCell = document.createElement('td');
        uuidCell.innerText = obj.uuid;
        firstNameCell.innerText = obj.firstName;
        lastNameCell.innerText = obj.lastName;
        emailCell.innerText = obj.email;
        row.appendChild(uuidCell);
        row.appendChild(firstNameCell);
        row.appendChild(lastNameCell);
        row.appendChild(emailCell);
        tableBody.appendChild(row);
      });
    })
}