<h1 align="center"> Adopet API </h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=java)
![Badge Springboot](https://img.shields.io/static/v1?label=Springboot&message=v3.0.5&color=brightgreen&style=for-the-badge&logo=spring)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v15.2&color=blue&style=for-the-badge&logo=PostgreSQL)

## :book: Resumo do projeto
Adopet API é uma REST API de uma plataforma para conectar pessoas que desejam adotar animais de estimação e abrigos.

Projeto proposto pela Alura no Challenge Backend 6ª Edição.

## :bulb: Funcionalidades

#### :bust_in_silhouette: Tutor
- `Cadastrar`: Salvar Tutor através de um **POST /api/tutors** com as informações de *name*, *email*, *password* e *confirmPassword*
em um JSON no corpo da requisição.</br>

- `Atualizar`: Atualizar Tutor através de um **PUT /api/tutors/{ID}**, onde *ID* é o identificador do Tutor, 
os novos dados do tutor devem ser enviados no corpo da requisição.</br>

- `Buscar por id`: Busca Tutor por ID através de um **GET /api/tutors/{ID}**, onde *{ID}* é o identificador do Tutor.</br>

- `Buscar todos`: Busca paginada de tutores através de um **GET /api/tutors**.</br>

- `Deletar`: Deletar Tutor através de um **DELETE /api/tutors/{ID}**, onde *{ID}* é o identificador do Tutor.</br>

#### :european_castle: Shelter
- `Cadastrar`: Salvar Shelter através de um **POST /api/shelters** com as informações de *name*
  em um JSON no corpo da requisição.</br>

- `Buscar todos`: Busca paginada de shelters através de um **GET /api/shelters**.</br>
- 
- `Buscar por id`: Busca Shelter por ID através de um **GET /api/shelters/{ID}**, onde *{ID}* é o identificador do Shelter.</br>

## :toolbox: Tecnologias

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
- `Testes de unidade`

## :gear: Atualizações semana 2
- [x] Modelagem da entidade Shelter.
- [x] Modelagem da entidade Pet.
- [x] Requisição POST para CRIAR Shelter.
- [ ] Requisição PUT para ATUALIZAR Shelter.
- [x] Requisição GET para BUSCAR todos os Shelters.
- [x] Requisição GET para BUSCAR Shelter por ID.
- [ ] Requisição DELETE para DELETAR Shelter.
- [ ] Requisição POST para CRIAR Pet.
- [ ] Requisição PUT para ATUALIZAR Pet.
- [ ] Requisição GET para BUSCAR todos os Pets.
- [ ] Requisição GET para BUSCAR Pet por ID.
- [ ] Requisição DELETE para DELETAR Pet.
- [ ] Validações de todos os campos de Shelter e Pet.
- [ ] Regra de negócio para adoção.
- [ ] Testes automatizados (Pelo menos os testes de unidade).