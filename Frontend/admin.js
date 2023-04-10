const jwt = require('jsonwebtoken');





function checkLogin() {
    const jwt = localStorage.getItem('jwt');
    if (!jwt) {
      // User is not logged in, redirect to login page
      window.location.href = "inlogg.html";
    }
  }
//   function logOut() {
//     localStorage.removeItem('jwt');
//     // Redirect the user to the login page
//     window.location.href = "inlogg.html";
//   }