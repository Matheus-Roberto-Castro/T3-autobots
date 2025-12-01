# **API Automanager — Atividade**
##Tecnologias Utilizadas

- Java 17

- Spring Boot

- Spring Web

- Spring Data JPA

- Spring HATEOAS

- H2 Database

- Lombok

- Maven

## Como Executar o Projeto

###Pré-requisitos

- JDK 17 instalado

- Maven instalado

### Rodando o projeto

mvn spring-boot:run

### A API ficará disponível em:

http://localhost:8080


# Endpoints da API

## A API contém seis módulos principais, cada um com operações CRUD completas:

/empresas

/usuarios

/veiculos

/mercadorias

/servicos

/vendas

##Cada seção abaixo contém os endpoints e JSONs de exemplo para testes em Postman/Thunder Client.

###1. EMPRESA
**GET /empresas**

Retorna todas as empresas.

**GET /empresas/{id}**

Retorna uma empresa pelo ID.

**POST /empresas**

Cadastro de empresa.

Exemplo de JSON:

```json
{
  "nomeFantasia": "Auto Peças Brasil",
  "razaoSocial": "Auto Peças Brasil LTDA",
  "cnpj": "12.345.678/0001-99"
}
```

**PUT /empresas/{id}**

Atualização de empresa.

Exemplo de JSON:

```json
{
  "nomeFantasia": "Auto Peças Atualizada",
  "razaoSocial": "Auto Peças Atualizada LTDA",
  "cnpj": "12.345.678/0001-99"
}
```

**DELETE /empresas/{id}**

Remove a empresa informada.

Associação de Usuário a Empresa
POST /empresas/{idEmpresa}/usuarios/{idUsuario}

Associa um usuário a uma empresa.

**DELETE /empresas/{idEmpresa}/usuarios/{idUsuario}**

Remove a associação entre usuário e empresa.

### 2. USUÁRIO
**GET /usuarios**

Retorna todos os usuários.

**GET /usuarios/{id}**

Retorna um usuário pelo ID.

**POST /usuarios**

Cadastro de usuário.

Exemplo de JSON:

```json
{
  "nome": "João da Silva",
  "email": "joao@email.com",
  "telefones": [
    { "ddd": "12", "numero": "999999999" }
  ]
}
```

**PUT /usuarios/{id}**

Atualização de usuário.

Exemplo de JSON:

```json
{
  "nome": "João Atualizado",
  "email": "joao.novo@email.com"
}
```

**DELETE /usuarios/{id}**

Remove o usuário informado.

Criar Credencial para Usuário
POST /usuarios/{id}/credenciais

Exemplo de JSON:

```json
{
  "nomeUsuario": "joaosilva",
  "senha": "123456"
}
```

### 3. VEÍCULO
**GET /veiculos**

Retorna todos os veículos.

**GET /veiculos/{id}**

Retorna um veículo pelo ID.

**POST /veiculos**

Cadastro de veículo.

Exemplo de JSON:

```json
{
  "modelo": "Fiat Uno",
  "marca": "Fiat",
  "ano": 2012,
  "placa": "ABC-1234"
}
```

**PUT /veiculos/{id}**

Atualização de veículo.

```json
{
  "modelo": "Fiat Argo",
  "marca": "Fiat",
  "ano": 2020,
  "placa": "XYZ-9876"
}
```

**DELETE /veiculos/{id}**

Remove o veículo informado.

### 4. MERCADORIA
**GET /mercadorias**

Retorna todas as mercadorias.

**GET /mercadorias/{id}**

Retorna uma mercadoria pelo ID.

**POST /mercadorias**

Cadastro de mercadoria.

Json de exemplo:
```json
{
  "nome": "Pneu Aro 15",
  "descricao": "Pneu Continental 195/65",
  "preco": 350.90
}
```

**PUT /mercadorias/{id}**

Atualização de mercadoria.

```json
{
  "nome": "Pneu Aro 16",
  "preco": 399.90
}
```

**DELETE /mercadorias/{id}**

Remove a mercadoria informada.

### 5. SERVIÇO
**GET /servicos**

Retorna todos os serviços.

**GET /servicos/{id}**

Retorna um serviço pelo ID.

**POST /servicos**

Cadastro de serviço.

```json
{
  "descricao": "Troca de óleo",
  "preco": 120.00
}
```

**PUT /servicos/{id}**

Atualização de serviço.

```json
{
  "descricao": "Troca de óleo sintético",
  "preco": 180.00
}
```

**DELETE /servicos/{id}**

Remove o serviço informado.

### 6. VENDA
**GET /vendas**

Retorna todas as vendas.

**GET /vendas/{id}**

Retorna uma venda pelo ID.

**POST /vendas**

Cadastro de venda.

```json
{
  "clienteId": 1,
  "funcionarioId": 2,
  "veiculoId": 3,
  "valor": 599.90,
  "data": "2025-01-10"
}
```

**PUT /vendas/{id}**

Atualização de venda.

```json
{
  "valor": 699.90
}
```

**DELETE /vendas/{id}**

Remove a venda informada.
