
console.log("Hello Servlet")
const handleClick = () => {
    window.location.href = "dashboard.html";
}
document.getElementById("btn").addEventListener("click", handleClick);



// login functions start

const loginForm = document.getElementById('loginForm');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');
const usernameError = document.getElementById('usernameError');
const passwordError = document.getElementById('passwordError');

loginForm.addEventListener('submit', function(e) {
    e.preventDefault();
    let hasError = false;
    usernameError.classList.add('d-none');
    passwordError.classList.add('d-none');

    // Example: hardcoded valid credentials
    const validUsername = 'admin';
    const validPassword = '1234';

    if (usernameInput.value !== validUsername) {
        usernameError.textContent = 'Invalid username';
        usernameError.classList.remove('d-none');
        hasError = true;
    }
    if (passwordInput.value !== validPassword) {
        passwordError.textContent = 'Invalid password';
        passwordError.classList.remove('d-none');
        hasError = true;
    }
    if (!hasError) {
        // Redirect or show success
        window.location.href = 'customer.html';
    }
});