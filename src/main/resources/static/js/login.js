document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error("Credenciais inválidas");
            }
            return response.json();
        })
        .then((data) => {
            // Redireciona com base na resposta do backend
            window.location.href = data.redirectUrl;
        })
        .catch((error) => {
            alert(error.message);
        });
});