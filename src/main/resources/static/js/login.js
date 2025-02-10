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
            return response.json().then((data) => {
                if (!response.ok) {
                    // Se a resposta não for OK, gera o erro a partir da resposta do servidor
                    throw new Error(data.error || "Erro desconhecido. Tente novamente.");
                }
                return data;
            });
        })
        .then((data) => {
            localStorage.setItem("token", data.token);
            window.location.href = data.redirectUrl;
        })
        .catch((error) => {
            alert("Erro: " + error.message); // Exibe o erro no alert
        });
});