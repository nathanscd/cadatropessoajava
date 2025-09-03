# Sistema de Cadastro de Produtos 📝

Este é um aplicativo de desktop simples para gerenciar o cadastro de produtos, construído em **Java Swing** e utilizando **PostgreSQL** como banco de dados. Ele permite realizar as operações básicas de CRUD (Criar, Ler, Atualizar, Excluir) em um banco de dados.

## Funcionalidades 

  * **Cadastrar Produto**: Insere um novo produto no banco de dados, com nome, preço e quantidade. O nome do produto deve ser único.
  * **Consultar Produto**: Busca um produto pelo nome e exibe suas informações na área de texto.
  * **Atualizar Produto**: Altera o preço e a quantidade de um produto existente.
  * **Excluir Produto**: Remove um produto do banco de dados com base no nome.
  * **Limpar Campos**: Limpa todos os campos de entrada e a área de texto de resultados.

-----

## Pré-requisitos 🛠️

Para rodar esta aplicação, você precisará ter o seguinte instalado e configurado:

  * **Java Development Kit (JDK)**: Versão 8 ou superior.
  * **PostgreSQL**: O banco de dados PostgreSQL deve estar instalado e rodando.
  * **Driver JDBC para PostgreSQL**: Este projeto usa o driver JDBC para se conectar ao banco de dados. Certifique-se de que o arquivo `.jar` do driver (por exemplo, `postgresql-42.2.27.jar`) está incluído no classpath do seu projeto. Você pode baixá-lo do [repositório oficial do PostgreSQL JDBC](https://jdbc.postgresql.org/).

-----

## Configuração do Banco de Dados ⚙️

1.  **Crie o banco de dados**: A aplicação está configurada para se conectar a um banco de dados chamado `postgres` no seu `localhost` na porta padrão `5432`. Se você usa um nome de banco de dados, usuário ou senha diferentes, altere as seguintes constantes no código:

    ```java
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    ```

2.  **A aplicação cuida do resto**: O código irá criar automaticamente a tabela `produtos` se ela ainda não existir, com a seguinte estrutura:

    ```
    CREATE TABLE IF NOT EXISTS produtos (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(100) UNIQUE NOT NULL,
        preco NUMERIC(10,2) NOT NULL,
        quantidade INTEGER NOT NULL
    );
    ```

-----

## Como Rodar a Aplicação ▶️

1.  Clone ou baixe o código-fonte.
2.  Abra o projeto em sua IDE (como VS Code, IntelliJ IDEA ou Eclipse).
3.  Adicione o arquivo `.jar` do driver JDBC do PostgreSQL ao classpath do projeto.
4.  Execute a classe `SistemaCadastroProdutos`.

A interface gráfica irá aparecer, e você poderá começar a gerenciar seus produtos.

-----

## Estrutura do Código 📂

O projeto é composto por uma única classe: `SistemaCadastroProdutos`.

  * `SistemaCadastroProdutos` (Classe principal): Gerencia a interface gráfica (JFrame, JPanels, JButtons, JTextFields, JTextArea), a lógica de negócios e as interações com o banco de dados.
      * `conectarBanco()`: Estabelece a conexão com o banco de dados PostgreSQL.
      * `criarTabelaProdutos()`: Cria a tabela `produtos` no banco de dados, se ela não existir.
      * `validarCampos()`: Valida se os dados inseridos nos campos de texto são válidos (não vazios, numéricos e positivos).
      * `cadastrarProduto()`, `consultarProduto()`, `atualizarProduto()`, `excluirProduto()`: Métodos que executam as operações de banco de dados usando `PreparedStatement` para segurança e eficiência.
      * `limparCampos()`: Limpa os campos da interface.
      * `main()`: O ponto de entrada da aplicação, que inicia a interface gráfica na *Event Dispatch Thread (EDT)*.

## Contribuições 🤝

Sinta-se à vontade para forkar o projeto e enviar *pull requests*\!
