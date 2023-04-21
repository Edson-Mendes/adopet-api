### Endpoint /api/guardian
- Criar Guardian - POST /api/guardians
  - [x] Qualquer um pode criar Guardian.
  - [x] Não é necessário estar autenticado.<br>

- Atualizar Guardian - PUT /api/guardians/ID
  - [x] Apenas **o próprio** usuário guardian pode atualizar seus dados.
  - [x] É necessário estar autenticado.<br>

- Buscar por ID - GET /api/guardians/ID
  - [x] Qualquer **usuário autenticado** pode buscar Guardian por ID.<br>
  - [x] Não retorna guardian deletado. 

- Buscar todos os Guardians - GET /api/guardians
  - [x] Qualquer **usuário autenticado** pode buscar **todos** os guardians.<br>
  - [x] Não devolver guardians deletados.

- Deletar Guardian por ID - DELETE /api/guardians/ID
  - [x] Apenas o próprio usuário Guardian pode se deletar.
  - [x] É necessário estar autenticado.
  - [x] A ação de deletar não remove a entidade da base de dados, apenas muda a flag DELETED para true.<br>

### Endpoint /api/shelter
- Criar Shelter - POST /api/shelter
  - [x] Qualquer um pode criar Shelter.
  - [x] Não é necessário estar autenticado.<br>

- Atualizar Shelter - PUT /api/shelters/ID
  - [x] Apenas **o próprio** usuário shelter pode atualizar seus dados.
  - [x] É necessário estar autenticado.<br>

- Buscar por ID - GET /api/shelters/ID
  - [x] Qualquer **usuário autenticado** pode buscar Shelter por ID.<br>
  - [x] Não retorna shelter deletado.

- Buscar todos os Shelters - GET /api/shelters
  - [x] Qualquer **usuário autenticado** pode buscar **todos** os shelters.<br>
  - [x] Não retorna shelters deletados.

- Deletar Shelter por ID - DELETE /api/shelters/ID
  - [x] Apenas o próprio usuário Shelter pode se deletar.
  - [x] É necessário estar autenticado.
  - [x] A ação de deletar não remove a entidade da base de dados, apenas muda a flag deleted para true<br>

### Endpoint /api/pet
- Criar Pet - POST /api/pet
  - [x] Apenas usuário Shelter pode cadastrar Pet.
  - [x] É necessário estar autenticado.<br>

- Atualizar Pet - PUT /api/pets/ID
  - [x] Apenas o usuário shelter que cadastrou o Pet pode atualiza-lo.
  - [x] É necessário estar autenticado.<br>

- Buscar por ID - GET /api/pets/ID
  - [x] Qualquer **usuário autenticado** pode buscar Pet por ID.<br>
  - [x] Pet que esta em Shelter *deletado* não é retornado.

- Buscar todos os Pets - GET /api/pets
  - [x] Qualquer **usuário autenticado** pode buscar **todos** os pets.
  - [x] A busca retorna apenas os pets **não adotados**<br>
  - [x] A busca não retorna pets que estão em shelters deletados.

- Deletar Pet por ID - DELETE /api/pets/ID
  - [x] Apenas o usuário shelter que cadastrou o Pet pode deleta-lo.
  - [x] É necessário estar autenticado.
  - [x] Pet que estiver relacionado a uma adoção não pode ser deletado.<br>

### Endpoint /api/adoptions
- Adotar Pet - POST /api/adoptions
  - [x] Apenas usuários Guardian podem realizar adoção.
  - [x] É necessário estar autenticado.
  - [ ] Apenas Pets não adotados podem ser adotados.<br>

- Buscar todas as adoções - GET /api/adoptions
  - [x] É necessário estar autenticado.
  - [x] Essa busca retorna as adoções relacionadas com o usuário autenticado.

- Atualizar status da Adoção - PUT /api/adoptions/ID/status
  - [x] É necessário estar autenticado.
  - [x] Apenas usuários Shelter podem atualizar status.
  - [x] Quando o status é alterado para *CONCLUDED* a flag Pet.adopted é alterado para *true*.
  - [x] Quando o status é alterado para *ANALYSING* ou *CANCELED*. a flag Pet.adopted é alterado para *false*.<br>