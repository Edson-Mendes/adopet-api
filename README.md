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
os novos dados do guardian devem ser enviados no corpo da requisição.
  - Apenas o próprio usuário Guardian pode atualizar seus dados.
  - É necessário estar autenticado.<br>

- `Buscar por id`: Busca Guardian por ID através de um **GET /api/guardians/{ID}**, onde *{ID}* é o identificador do Guardian.
  - É necessário estar autenticado.<br>

- `Buscar todos`: Busca paginada de guardians através de um **GET /api/guardians**.
  - É necessário estar autenticado.<br>

- `Deletar`: Deletar Guardian através de um **DELETE /api/guardians/{ID}**, onde *{ID}* é o identificador do Guardian.</br>
  - Apenas o próprio usuário Guardian pode se deletar.
  - É necessário estar autenticado.<br>

#### :european_castle: Shelter
- `Cadastrar`: Salvar Shelter através de um **POST /api/shelters** com as informações de *name*
  em um JSON no corpo da requisição.</br>

- `Buscar todos`: Busca paginada de shelters através de um **GET /api/shelters**.
  - É necessário estar autenticado.<br>
 
- `Buscar por id`: Busca Shelter por ID através de um **GET /api/shelters/{ID}**, onde *{ID}* é o identificador do Shelter.
  - É necessário estar autenticado.<br>

- `Atualizar`: Atualizar Shelter através de um **PUT /api/shelters/{ID}**, onde *ID* é o identificador do Shelter,
  os novos dados do abrigo devem ser enviados no corpo da requisição.
  - Apenas o próprio usuário Shelter pode atualizar seus dados.
  - É necessário estar autenticado.<br>

- `Deletar`: Deletar Shelter através de um **DELETE /api/shelters/{ID}**, onde *{ID}* é o identificador do Shelter.
  - Apenas o próprio usuário Shelter pode se deletar.
  - É necessário estar autenticado.<br>

#### :cat: Pet
- `Cadastrar`: Salvar Pet através de um **POST /api/pets** com as informações de *name*, *description*, *age* e *shelterId*
  em um JSON no corpo da requisição.</br>
  - Apenas Shelters podem cadastrar Pets.

- `Buscar todos`: Busca paginada de pets através de um **GET /api/pets**.</br>
  - É necessário estar autenticado.<br>

- `Buscar por id`: Busca Pet por ID através de um **GET /api/pets/{ID}**, onde *{ID}* é o identificador do Pet.</br>
  - É necessário estar autenticado.<br>

- `Atualizar`: Atualizar Pet através de um **PUT /api/pets/{ID}**, onde *ID* é o identificador do Pet,
  os novos dados do pet devem ser enviados no corpo da requisição.
  - Apenas o Shelter que cadastrou o Pet pode atualiza-lo.<br>

- `Deletar`: Deletar Pet através de um **DELETE /api/pets/{ID}**, onde *{ID}* é o identificador do Pet.
  - Apenas o Shelter que cadastrou o Pet pode deleta-lo.
  - Pet relacionado a uma Adoption não pode ser deletado.

#### :heart_eyes_cat: Adoption
- `Adotar`: Solicitar uma adoção de um Pet através de um **POST /api/adoptions** com as informações *petId* 
  em um JSON no corpo da requisição. É necessário estar autenticado. Apenas *Guardians* podem solicitar uma adoção.
  - Apenas usuários do tipo guardian podem solicitar uma adoção.
  - Apenas Pets não adotados podem receber uma solicitação de adoção.<br>

- `Buscar todos`: Busca paginada de adoções através de um **GET /api/adoptions**, retorna todas as adoções relacionadas 
  com o usuário logado.
  - É necessário estar autenticado.
  - Busca somente adoções relacionadas ao usuário autenticado (Shelter ou Guardian).<br>

- `Atualizar status`: Atualização de status através de um **PUT /api/adoptions/{ID}/status** com a informação *status* 
  em um JSON no corpor da requisição. Os status possíveis são *ANALYSING*, *CONCLUDED* e *CANCELED*. Apenas usuários do tipo 
  Shelter atualizar status.
  - Uma adoção só pode ter o status atualizado pelo Shelter relacionado na adoção.<br>

- `Deletar`: Deletar uma adoção através de um **DELETE /api/adoptions/{ID}**, onde *{ID}* é o identificador da Adoção.
  - Uma adoção só pode ser deletada pelo Shelter relacionado na adoção.<br>


## :gear: Atualizações semana 3 e 4
- [x] Autenticação de Tutores.
- [x] Autenticação de Abrigos.
- [x] Paginação das buscas.
- [x] Testes de Unidade.
- [ ] Testes de Integração.
- [ ] Deploy da Aplicação.
- [ ] Integração com Front-End.