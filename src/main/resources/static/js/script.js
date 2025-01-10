document.getElementById("fetchHelloButton").addEventListener("click", function () {
    fetch('/api/users/hello')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            document.getElementById("responseContainer").innerHTML = `
                <div class="alert alert-success" role="alert">
                    ${data}
                </div>
            `;
        })
        .catch(error => {
            document.getElementById("responseContainer").innerHTML = `
                <div class="alert alert-danger" role="alert">
                    Ocorreu um erro: ${error}
                </div>
            `;
        });
});

function fetchUsers() {
    fetch('/api/users')
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

// Função para excluir um usuário
function deleteUser(userId) {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
        fetch(`/api/users?userId=${userId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (response.ok) {
                alert('Usuário deletado com sucesso!');
                fetchUsers(); // Atualiza a lista de usuários
            } else {
                // Se a resposta não for OK, mostramos o erro retornado pelo backend
                return response.text().then(message => {
                    alert('Erro: ' + message); // Exibe a mensagem de erro
                });
            }
        })
        .catch(error => {
            console.error('Erro ao deletar o usuário:', error);
            alert('Erro ao deletar o usuário!,'); // Exibe uma mensagem genérica de erro
        });
    }
}

// Chama a função ao carregar a página
document.addEventListener('DOMContentLoaded', fetchUsers);