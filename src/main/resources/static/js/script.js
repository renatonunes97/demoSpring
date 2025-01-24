function getJwtToken() {
    return localStorage.getItem("token");
}


function fetchUsers() {
 console.log(localStorage.getItem("token"));
   fetch('/api/users', {
           method: 'GET',
           headers: {
               'Authorization': `Bearer ${getJwtToken()}` // Adiciona o token JWT no cabeçalho
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




// Atualizando a requisição para incluir o token JWT
document.getElementById("fetchHelloButton").addEventListener("click", function () {

    console.log("Token enviado no cabeçalho Authorization:", token);

    fetch('/api/users/hello', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${getJwtToken()}` // Adiciona o token JWT no cabeçalho
        }
    })
    .then(response => {
        console.log("Resposta da API:", response);
        return response.text();
    })
    .then(data => console.log("Dados recebidos:", data))
    .catch(error => console.error("Erro:", error));
});

function deleteUser(userId) {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
        fetch(`/api/users?userId=${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getJwtToken()}` // Adiciona o token JWT no cabeçalho
            }
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
            alert('Erro ao deletar o usuário!'); // Exibe uma mensagem genérica de erro
        });
    }
}

// Chama a função ao carregar a página
document.addEventListener('DOMContentLoaded', fetchUsers);