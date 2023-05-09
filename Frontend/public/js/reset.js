//import { json } from "body-parser";
import {isAdmin, isAuthenticated, isUser} from "./auth/auth.js";
import {baseUrl, userApiUrl} from "./shared.js";

const form = document.getElementById("reset-form");

form.addEventListener('submit', async (event) => {
    // Prevent default form submission
    event.preventDefault();
   
    
    // Get the email value
    const email = document.getElementById("email").value;
    const body = {
        email: form.elements['email'].value
    }
    console.log(body)
    // Send a password reset link to the provided email address
    const  apiUrl = `${userApiUrl}/api/auth/reset/start`;
    const response = await fetch(apiUrl, {
        method: "POST",
        body:JSON.stringify(email),
        headers: {
            'Content-Type': "application/json",
           // 'Authorization': `Bearer ${window.localStorage.getItem('jwt')}`
        }
        
    });

    //Check if the request was successful
    if (response.ok) {
        // Display success message to the user
        console.log('An email with instructions on how to reset your password has been sent to your inbox.');
    } else {
        // Display error message to the user
        console.log('There was an error processing your request. Please try again later.');
    }

});


