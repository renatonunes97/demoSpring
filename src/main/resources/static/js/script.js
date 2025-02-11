function getJwtToken() {
    return localStorage.getItem("token");
}


function fetchUsers() {
    // Chama a API diretamente, o navegador enviará automaticamente os cookies HTTP Only
    fetch('/api/users/getUsers', {
        method: 'GET',
         headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na requisição: ${response.status}`);
            }
            return response.json();
        })
        .then(users => {
            const tableBody = document.getElementById('userTableBody');
            tableBody.innerHTML = ''; // Limpa os dados antigos

            users.forEach(user => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.address}</td>
                    <td>${user.email}</td>
                     <td>
                        <!-- Botão de deletar -->
                        <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Deletar</button>
                    </td>
                `;

                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Erro ao buscar usuários:', error);
        });
}

/*
document.getElementById("fetchHelloButton").addEventListener("click", function () {
    console.log("Enviando requisição para '/api/users/hello'");

    fetch('/api/users/hello', {
        method: 'GET',
    })
        .then(response => {
            console.log("Resposta da API:", response);
            if (!response.ok) {
                throw new Error(`Erro na requisição: ${response.status}`);
            }
            return response.text();
        })
        .then(data => console.log("Dados recebidos:", data))
        .catch(error => console.error("Erro ao buscar dados:", error));
});
*/
  document.addEventListener('DOMContentLoaded', function () {
            const statusLabel = document.getElementById("fetchHelloAuth");
            statusLabel.textContent = "Carregando...";  // Atualiza a label para "Carregando..."

            // Faz a requisição para a API
            fetch('/api/users/hello', {
                method: 'GET',
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erro na requisição');
                    }
                    return response.text();
                })
                .then(data => {
                    // Atualiza a label com os dados recebidos da API
                    statusLabel.textContent = `${data}`;
                })
                .catch(error => {
                    // Caso haja um erro, exibe a mensagem de erro na label
                    statusLabel.textContent = 'Erro ao buscar dados.';
                    console.error('Erro ao buscar dados:', error);
                });
        });


function deleteUser(userId) {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
        fetch(`/api/users?userId=${userId}`, {
            method: 'DELETE',
        })
        .then(response => response.text()) // Converte a resposta para texto
        .then(message => {
            alert(message); // Exibe a mensagem retornada pelo backend
            fetchUsers(); // Atualiza a lista de usuários
        })
        .catch(error => {
            console.error('Erro ao deletar o usuário:', error);
            alert('Erro ao deletar o usuário!'); // Mensagem genérica de erro
        });
    }
}

// Chama a função ao carregar a página
document.addEventListener('DOMContentLoaded', fetchUsers);


document.getElementById("logoutButton").addEventListener("click", function () {
    fetch("/api/auth/logout", {
        method: "GET",
        credentials: "include" // Envia os cookies na requisição
    })
    .then((response) => {
        if (response.redirected) {
            window.location.href = response.url; // Redireciona corretamente
        } else {
            throw new Error("Erro ao realizar logout.");
        }
    })
    .catch((error) => {
        alert(error.message);
    });
});