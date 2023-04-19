### Endpoint /api/guardian
- Criar Guardian - POST /api/guardians
  - [x] Qualquer um pode criar Guardian.
  - [x] Não é necessário estar autenticado.<br>

- Atualizar Guardian - PUT /api/guardians/ID
  - [x] Apenas **o próprio** usuário guardian pode atualizar seus dados.
  - [x] É necessário estar autenticado.<br>

- Buscar por ID - GET /api/guardians/ID
  - [ ] Qualquer **usuário autenticado** pode buscar Guardian por ID.<br>

- Buscar todos os Guardians - GET /api/guardians
  - [ ] Qualquer **usuário autenticado** pode buscar **todos** os guardians.<br>

- Deletar Guardian por ID - DELETE /api/guardians/ID
  - [ ] Apenas o próprio usuário Guardian pode se deletar.
  - [x] É necessário estar autenticado.
  - [ ] A ação de deletar não remove a entidade da base de dados, apenas muda o status para DELETED.<br>

### Endpoint /api/shelter
- Criar Shelter - POST /api/shelter
  - [ ] Qualquer um pode criar Shelter.
  - [x] Não é necessário estar autenticado.<br>

- Atualizar Shelter - PUT /api/shelters/ID
  - [ ] Apenas **o próprio** usuário shelter pode atualizar seus dados.
  - [x] É necessário estar autenticado.<br>

- Buscar por ID - GET /api/shelters/ID
  - [ ] Qualquer **usuário autenticado** pode buscar Shelter por ID.<br>

- Buscar todos os Shelters - GET /api/shelters
  - [ ] Qualquer **usuário autenticado** pode buscar **todos** os shelters.<br>

- Deletar Shelter por ID - DELETE /api/shelters/ID
  - [ ] Apenas o próprio usuário Shelter pode se deletar.
  - [x] É necessário estar autenticado.
  - [ ] A ação de deletar não remove a entidade da base de dados, apenas muda o status para DELETED.<br>

### Endpoint /api/pet
- Criar Pet - POST /api/pet
  - [ ] Apenas usuário Shelter pode cadastrar Pet.
  - [x] É necessário estar autenticado.<br>

- Atualizar Pet - PUT /api/pets/ID
  - [ ] Apenas o usuário shelter que cadastrou o Pet pode atualiza-lo.
  - [x] É necessário estar autenticado.<br>

- Buscar por ID - GET /api/pets/ID
  - [ ] Qualquer **usuário autenticado** pode buscar Pet por ID.<br>

- Buscar todos os Pets - GET /api/pets
  - [ ] Qualquer **usuário autenticado** pode buscar **todos** os pets.
  - [x] A busca retorna apenas os pets **não adotados**<br>

- Deletar Pet por ID - DELETE /api/pets/ID
  - [ ] Apenas o usuário shelter que cadastrou o Pet pode deleta-lo.
  - [x] É necessário estar autenticado.
  - [ ] A ação de deletar não remove a entidade da base de dados, apenas muda o status para DELETED.<br>

### Endpoint /api/adoptions
- Adotar Pet - POST /api/adoptions
  - [x] Apenas usuários Guardian podem realizar adoção.
  - [x] É necessário estar autenticado.
  - [ ] Apenas Pets não adotados podem se adotados.<br>

- Buscar todas as adoções - GET /api/adoptions
  - [x] É necessário estar autenticado.
  - [x] Essa busca retorna as adoções relacionadas com o usuário autenticado.

- Atualizar status da Adoção - PUT /api/adoptions/ID/status
  - [x] É necessário estar autenticado.
  - [x] Apenas usuários Shelter podem atualizar status.
  - [x] Quando o status é alterado para *CONCLUDED* a flag Pet.adopted é alterado para *true*.
  - [x] Quando o status é alterado para *ANALYSING* ou *CANCELED*. a flag Pet.adopted é alterado para *false*.<br>