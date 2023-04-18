<h1 align="center"> Adopet API </h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=java)
![Badge Springboot](https://img.shields.io/static/v1?label=Springboot&message=v3.0.5&color=brightgreen&style=for-the-badge&logo=spring)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v15.2&color=blue&style=for-the-badge&logo=PostgreSQL)

## :book: Resumo do projeto
Adopet API é uma REST API de uma plataforma para conectar pessoas que desejam adotar animais de estimação e abrigos.

Projeto proposto pela Alura no Challenge Backend 6ª Edição.


## :toolbox: Tecnologias e ferramentas

- `IntelliJ`
- `Java 17`
- `Maven`
- `Spring Boot`
- `Spring Data JPA`
- `Docker`
- `PostgreSQL`
- `Flyway`
- `Lombok`
- `Mockito`
- `JUnit5`
- `Unit tests`

## :bulb: Funcionalidades

#### :bust_in_silhouette: Guardian
- `Cadastrar`: Salvar Guardian através de um **POST /api/guardians** com as informações de *name*, *email*, *password* e *confirmPassword*
em um JSON no corpo da requisição.</br>

- `Atualizar`: Atualizar Guardian através de um **PUT /api/guardians/{ID}**, onde *ID* é o identificador do Guardian, 
os novos dados do guardian devem ser enviados no corpo da requisição.</br>

- `Buscar por id`: Busca Guardian por ID através de um **GET /api/guardians/{ID}**, onde *{ID}* é o identificador do Guardian.</br>

- `Buscar todos`: Busca paginada de guardians através de um **GET /api/guardians**.</br>

- `Deletar`: Deletar Guardian através de um **DELETE /api/guardians/{ID}**, onde *{ID}* é o identificador do Guardian.</br>

#### :european_castle: Shelter
- `Cadastrar`: Salvar Shelter através de um **POST /api/shelters** com as informações de *name*
  em um JSON no corpo da requisição.</br>

- `Buscar todos`: Busca paginada de shelters através de um **GET /api/shelters**.</br>
 
- `Buscar por id`: Busca Shelter por ID através de um **GET /api/shelters/{ID}**, onde *{ID}* é o identificador do Shelter.</br>

- `Atualizar`: Atualizar Shelter através de um **PUT /api/shelters/{ID}**, onde *ID* é o identificador do Shelter,
  os novos dados do abrigo devem ser enviados no corpo da requisição.</br>

- `Deletar`: Deletar Shelter através de um **DELETE /api/shelters/{ID}**, onde *{ID}* é o identificador do Shelter.</br>

#### :cat: Pet
- `Cadastrar`: Salvar Pet através de um **POST /api/pets** com as informações de *name*, *description*, *age* e *shelterId*
  em um JSON no corpo da requisição.</br>

- `Buscar todos`: Busca paginada de pets através de um **GET /api/pets**.</br>

- `Buscar por id`: Busca Pet por ID através de um **GET /api/pets/{ID}**, onde *{ID}* é o identificador do Pet.</br>

- `Atualizar`: Atualizar Pet através de um **PUT /api/pets/{ID}**, onde *ID* é o identificador do Pet,
  os novos dados do pet devem ser enviados no corpo da requisição.</br>

- `Deletar`: Deletar Pet através de um **DELETE /api/pets/{ID}**, onde *{ID}* é o identificador do Pet.</br>

#### :heart_eyes_cat: Adoption
- `Adotar`: Solicitar uma adoção de um Pet através de um **POST /api/adoptions** com as informações *petId* 
  em um JSON no corpo da requisição. É necessário estar autenticado. Apenas *Guardians* podem solicitar uma adoção.

- `Buscar todos`: Busca paginada de adoções através de um **GET /api/adoptions**, retorna todas as adoções relacionadas 
  com o usuário logado.


## :gear: Atualizações semana 2
- [x] Modelagem da entidade Shelter.
- [x] Modelagem da entidade Pet.
- [x] Requisição POST para CRIAR Shelter.
- [x] Requisição PUT para ATUALIZAR Shelter.
- [x] Requisição GET para BUSCAR todos os Shelters.
- [x] Requisição GET para BUSCAR Shelter por ID.
- [x] Requisição DELETE para DELETAR Shelter.
- [x] Requisição POST para CRIAR Pet.
- [x] Requisição PUT para ATUALIZAR Pet.
- [x] Requisição GET para BUSCAR todos os Pets.
- [x] Requisição GET para BUSCAR Pet por ID.
- [x] Requisição DELETE para DELETAR Pet.
- [x] Validações de todos os campos de Shelter e Pet.
- [x] Regra de negócio para adoção.
- [ ] Testes automatizados (Pelo menos os testes de unidade).