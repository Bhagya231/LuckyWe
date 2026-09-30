import { login, register } from "./api.js";

const loginForm = document.getElementById("login-form");

if (loginForm) {

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value.trim();
        const emailError = document.getElementById("email-error");
        const passwordError = document.getElementById("password-error");
        const errorMessage = document.getElementById("error-message");
        emailError.textContent = "";
        passwordError.textContent = "";
        errorMessage.classList.add("d-none");
        let isValid = true;

        if (email === "") {
            emailError.textContent = "Please enter your email.";
            isValid = false;
        }
        if (password === "") {
            passwordError.textContent = "Please enter your password.";
            isValid = false;
        }
        if (!isValid) {
            return;
        }
        try {
            const request = {
                email: email,
                password: password
            };
            const response = await login(request);
            console.log("Login successful:", response);
            localStorage.setItem("user", JSON.stringify(response));


             localStorage.setItem("jwtToken", response.token);

            console.log("Token saved:  ", response.token);


            if (response.role === "CUSTOMER")
            {
                window.location.href = "product.html"; 

            } else if (response.role === "ADMIN") 
            {
                window.location.href = "admin.html";
            }
        } catch (error) {
            console.error("Login error:", error);
            errorMessage.textContent = error.message;
            errorMessage.classList.remove("d-none");
        }

    });
}


const registerForm = document.getElementById("register-form");
if (registerForm) {

    registerForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const address = document.getElementById("address").value.trim();
        const password = document.getElementById("password").value.trim();

        const nameError = document.getElementById("name-error");
        const emailError = document.getElementById("email-error");
        const phoneError = document.getElementById("phone-error");
        const addressError = document.getElementById("address-error");
        const passwordError = document.getElementById("password-error");
        const errorMessage = document.getElementById("error-message");

        nameError.textContent = "";
        emailError.textContent = "";
        phoneError.textContent = "";
        addressError.textContent = "";
        passwordError.textContent = "";

        errorMessage.textContent = "";
        errorMessage.classList.add("d-none");

        let isValid = true;

        if (name === "") {
            nameError.textContent = "Please enter your name.";
            isValid = false;
        }
        if (email === "") {
            emailError.textContent = "Please enter your email.";
            isValid = false;
        }
        if (!/^[0-9]{10}$/.test(phone)) {
            phoneError.textContent = "Phone number must contain 10 digits.";
            isValid = false;
        }
        if (address === "") {
            addressError.textContent = "Please enter your address.";
            isValid = false;
        }
        if (password === "") {
            passwordError.textContent = "Please enter your password.";
            isValid = false;
        }
        if (!isValid) {
            return;
        }

        try {

            const request = { name: name, email: email, phone: phone, address: address, password: password};

            const response = await register(request);

            console.log("Registration successful:", response);

            alert("Registration successful! Please login.");
           
            window.location.href = "login.html";

        } catch (error) {

            console.error("Registration error:", error);

            errorMessage.textContent = error.message;
            errorMessage.classList.remove("d-none");
        }

    });
}