[README.md](https://github.com/user-attachments/files/33742805/README.md)
# 🎟️ Plataforma de Eventos

Um sistema para cadastrar eventos, organizar ingressos e acompanhar a lotação de cada festa. Desenvolvi o projeto usando **Java, Spring Boot, React e MySQL**, com uma API REST conectando o site ao banco de dados.

## 💡 De onde veio a ideia

Tenho um amigo que gosta muito de dar festas na casa dele. Sempre começa do mesmo jeito: ele escolhe uma data, chama a galera no WhatsApp e, quando percebe, já tem um monte de gente confirmando presença.

O problema aparece quando a festa começa a crescer. Alguém pergunta se ainda tem vaga, outra pessoa quer levar mais dois amigos, tem gente que confirma e depois desiste. No meio de tanta mensagem, fica difícil saber quantas pessoas realmente vão, quais ingressos já foram emitidos e quanto a festa pode arrecadar.

Pensei que seria interessante transformar essa confusão em um sistema. Em vez de depender de conversa no grupo e anotação solta, daria para cadastrar cada festa, definir um limite de pessoas, registrar ingressos e acompanhar tudo em um lugar só.

Foi esse o cenário que escolhi para desenvolver a **Plataforma de Eventos**. Aproveitei a ideia para colocar em prática o que estava aprendendo na faculdade e entender melhor como um front end em React conversa com um back end em Java.

## 🎯 O que o projeto faz

A aplicação tem duas partes. A interface em React permite gerenciar os eventos. O Spring Boot disponibiliza também as operações relacionadas a ingressos e os dados de lotação e faturamento estimado.

| Funcionalidade | API | Interface React |
| :--- | :---: | :---: |
| Listar eventos | ✅ | ✅ |
| Cadastrar eventos com nome, local, capacidade e preço | ✅ | ✅ |
| Editar nome e local | ✅ | ✅ |
| Excluir eventos | ✅ | ✅ |
| Emitir ingressos para um evento | ✅ | ⏳ |
| Consultar ingressos emitidos | ✅ | ⏳ |
| Cancelar ingressos | ✅ | ⏳ |
| Consultar vagas disponíveis | ✅ | ⏳ |
| Consultar faturamento estimado | ✅ | ⏳ |

As funções de ingressos estão disponíveis na API e podem ser testadas pelo Postman. A interface atual está focada no gerenciamento dos eventos. **O sistema não realiza pagamentos reais.**

## 🛠️ Tecnologias

| Camada | Tecnologias | Para que usei |
| :--- | :--- | :--- |
| Interface | React, JavaScript, HTML, CSS e Vite | Formulários, listagem e atualização da tela |
| Servidor | Java e Spring Boot | API REST e regras de negócio |
| Persistência | Spring Data JPA e Hibernate | Salvar e consultar os registros |
| Banco de dados | MySQL | Armazenar eventos e ingressos |
| Ferramentas | Maven, Git, GitHub e Postman | Dependências, versionamento e testes das requisições |

## ⚙️ Como funciona

```text
Pessoa acessa a página
          ↓
       React
          ↓
    Requisição HTTP
          ↓
     Spring Boot
          ↓
      JPA
          ↓
       MySQL
```

Quando alguém cadastra uma festa, por exemplo, o React pega os valores preenchidos no formulário e envia uma requisição `POST /eventos`. O Spring Boot recebe os dados, valida as informações e salva o evento no MySQL. Depois, a página faz uma nova consulta para mostrar a lista atualizada.

O processo de edição é parecido. O usuário escolhe um evento, altera os campos e o React envia uma requisição `PUT` com o ID daquele registro.

## 🌐 Endpoints da API

### Eventos

| Método | Endpoint | Ação |
| :--- | :--- | :--- |
| `GET` | `/eventos` | Consultar os eventos |
| `POST` | `/eventos` | Cadastrar um evento |
| `PUT` | `/eventos/{id}` | Alterar nome e local |
| `DELETE` | `/eventos/{id}` | Excluir um evento |

Exemplo de cadastro:

```json
{
  "nome": "Festa de Halloween",
  "local": "Curitiba",
  "capacidade": 80,
  "precoIngresso": 35.00
}
```

### Ingressos

| Método | Endpoint | Ação |
| :--- | :--- | :--- |
| `POST` | `/eventos/{id}/ingressos` | Emitir ingresso |
| `GET` | `/eventos/{id}/ingressos` | Listar ingressos do evento |
| `DELETE` | `/ingressos/{id}` | Cancelar ingresso |
| `GET` | `/eventos/{id}/vagas` | Consultar vagas restantes |
| `GET` | `/eventos/{id}/faturamento` | Consultar faturamento estimado |

Exemplo de emissão de ingresso:

```json
{
  "nomeComprador": "João Silva"
}
```

A API verifica a capacidade cadastrada antes de emitir um novo ingresso. O faturamento é uma **estimativa calculada com base na quantidade de ingressos emitidos e no preço definido para o evento**, não um valor de pagamentos confirmados.

## 📁 Organização dos arquivos

```text
plataforma-eventos/
├── eventos/                     # Projeto Spring Boot
│   ├── src/main/java/com/eduardo/eventos/
│   │   ├── Evento.java
│   │   ├── Ingresso.java
│   │   ├── EventoController.java
│   │   ├── IngressoController.java
│   │   ├── EventoRepository.java
│   │   ├── IngressoRepository.java
│   │   └── EventosApplication.java
│   └── pom.xml
├── eventos-front/               # Projeto React
│   ├── src/
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   └── package.json
└── README.md
```

## 🚀 Como executar no computador

Para rodar o projeto, é necessário ter **Java compatível com o `pom.xml`**, **MySQL**, **Node.js** e **npm** instalados.

### 1. Baixar o projeto

```bash
git clone https://github.com/duds3b/plataforma-eventos.git
cd plataforma-eventos
```

### 2. Criar o banco de dados

No MySQL, execute:

```sql
CREATE DATABASE IF NOT EXISTS eventos_db;
```

### 3. Configurar o Spring Boot

Crie ou ajuste o arquivo `eventos/src/main/resources/application.properties` com as informações da sua instalação do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/eventos_db
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

O arquivo de configuração local é ignorado pelo Git para evitar a publicação de credenciais.

### 4. Iniciar a API

Abra um terminal na pasta `eventos` e, no Windows, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Também é possível iniciar pela classe `EventosApplication` no IntelliJ IDEA.

A API estará em **http://localhost:8080**. Para conferir se está funcionando, acesse **http://localhost:8080/eventos**.

### 5. Iniciar o React

Em outro terminal, entre na pasta `eventos-front` e execute:

```bash
npm install
npm run dev
```

Depois abra **http://localhost:5173**. O React e o Spring Boot precisam estar rodando ao mesmo tempo.

## 📚 O que aprendi construindo o projeto

Esse projeto foi uma oportunidade de sair dos exercícios isolados e ligar várias partes do desenvolvimento em uma aplicação só. Trabalhei com classes e relacionamentos em Java, controllers e repositórios no Spring Boot, requisições HTTP, persistência no MySQL e estados e formulários no React.

Uma das partes mais interessantes foi entender o caminho completo de uma informação: ela sai do formulário, passa pelo `fetch()`, chega ao controller, é salva no banco e depois aparece novamente na tela.

## 🔮 O que quero melhorar

| Próxima melhoria | Ideia |
| :--- | :--- |
| Painel de ingressos no React | Emitir, consultar e cancelar ingressos sem usar o Postman |
| Indicadores na interface | Mostrar vagas restantes e faturamento estimado |
| Edição mais completa | Alterar também capacidade e preço com validações no servidor |
| Login de organizadores | Separar o acesso aos eventos por usuário |
| Informações da festa | Adicionar data, horário, descrição e imagem |
| Testes e publicação | Automatizar testes e colocar o projeto no ar |
| Controle de concorrência | Evitar emissões acima da capacidade em acessos simultâneos |

## 👨‍💻 Desenvolvido por

**[duds3b](https://github.com/duds3b)**

Projeto desenvolvido para aprendizado e portfólio, com foco em Java, Spring Boot, React e MySQL.
