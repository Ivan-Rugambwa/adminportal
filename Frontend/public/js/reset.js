import {isAdmin, isAuthenticated, isUser} from "./auth/auth.js";
import {baseUrl, userApiUrl} from "./shared.js";

const form = document.getElementById("reset-form");

form.addEventListener('submit', async (event) => {
    // Prevent default form submission
    event.preventDefault();

    // Get the email value
    const email = document.getElementById("email").value;

    // Send a password reset link to the provided email address
    const apiUrl = `${userApiUrl}/api/auth/reset/start`;
    const response = await fetch(apiUrl, {
        method: "POST",
        body: email,
        headers: {
            "Content-Type": "application/json",
        },
        
    });

    // Check if the request was successful
    // if (response.ok) {
    //     // Display success message to the user
    //     alert('An email with instructions on how to reset your password has been sent to your inbox.');
    // } else {
    //     // Display error message to the user
    //     alert('There was an error processing your request. Please try again later.');
    // }
});
