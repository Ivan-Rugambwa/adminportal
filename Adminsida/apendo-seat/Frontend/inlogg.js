
function getInfo() {
   
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const apiUrl = "http://wsprakt3.apendo.se:9000/api/auth/authenticate";

		

    function requestBody(email, password) {
        console.log(email, password);
        let payload = {
            "email": email,
            "password": password
			
        }
        return JSON.stringify(payload);
    };
    fetch(apiUrl, {
        method: "POST",
        body: requestBody(email, password),
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            console.log(response);
            // handle response here
        })
        .catch((error) => {
            console.error(error);
            // handle error here
        });

    
}





// var objPeople = [
// 	{ // Object @ 0 index
// 		email: "ivan.rugambwa@apendo.se",
// 		password: "potatis1"
// 	},
// 	{ // Object @ 1 index
// 		email: "kim.almroth@apendo.se",
// 		password: "potatis2"
// 	},
// 	{ // Object @ 1 index
// 		email: "kristoffer.hogberg@apendo.se",
// 		password: "potatis3"
// 	}
// ]

// function getInfo() {
// 	var email = document.getElementById('email').value
// 	var password = document.getElementById('password').value

// 	for(var i = 0; i < objPeople.length; i++) {
// 		// check is user input matches username and password of a current index of the objPeople array
// 		if(email == objPeople[i].email && password == objPeople[i].password) {
// 			console.log(email + " is logged in!!!")
// 			// stop the function if this is found to be true
//             window.location.href = 'http://127.0.0.1:5501/adminportal.html';
// 			return
            
// 		}
// 	}
// 	console.log("incorrect email or password")
// }

