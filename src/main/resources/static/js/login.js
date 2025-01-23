document.getElementById("loginForm").addEventListener("submit", function (event) {
        event.preventDefault();
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        fetch("/api/users/login", {
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
                // Armazena o token JWT no localStorage
                localStorage.setItem("token", data.token);
                alert("Login bem-sucedido!");
                // Redireciona para a página inicial
                window.location.href = "/homepage.html";
            })
            .catch((error) => {
                alert(error.message);
            });
    });