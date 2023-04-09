<h1 align="center"> Adopet API </h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=orange&style=for-the-badge&logo=java)
![Badge Springboot](https://img.shields.io/static/v1?label=Springboot&message=v3.0.5&color=brightgreen&style=for-the-badge&logo=spring)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v15.2&color=blue&style=for-the-badge&logo=PostgreSQL)

## :book: Resumo do projeto
Adopet API é uma REST API de uma plataforma para conectar pessoas que desejam adotar animais de estimação e abrigos.

Projeto proposto pela Alura no Challenge Backend 6ª Edição.

## :bulb: Funcionalidades

### :bust_in_silhouette: Tutor
- `Cadastrar`: Salvar Tutor através de um **POST /api/tutors** com as informações de *name*, *email*, *password* e *confirmPassword*
em um JSON no corpo da requisição.</br></br>

- `Atualizar`: Atualizar Tutor através de um **PUT /api/tutors/{ID}**, onde *ID* é o identificador do Tutor, 
os novos dados do tutor devem ser enviados no corpo da requisição.</br></br>

- `Buscar por id`: Busca Tutor por ID através de um **GET /api/tutors/{ID}**, onde *{ID}* é o identificador do Tutor.</br></br>

- `Buscar todos`: Busca paginada de tutores através de um **GET /api/tutors**.</br></br>

- `Deletar`: Deletar Tutor através de um **DELETE /api/tutors/{ID}**, onde *{ID}* é o identificador do Tutor.</br>


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

## :gear: Atualizações semana 1
- [x] Modelagem da entidade Tutor.
- [x] Requisição POST para CRIAR Tutor.
- [x] Requisição PUT para ATUALIZAR Tutor.
- [x] Requisição GET para BUSCAR todos os Tutores.
- [x] Requisição GET para BUSCAR Tutor por ID.
- [x] Requisição DELETE para DELETAR Tutor.
- [x] Validações de todos os campos de Tutor.
- [x] Testes automatizados (Pelo menos os testes de unidade).