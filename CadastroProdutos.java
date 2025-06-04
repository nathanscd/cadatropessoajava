import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class SistemaCadastroProdutos extends JFrame {
    private JTextField campoNome, campoPreco, campoQuantidade;
    private JTextArea areaProdutos;
    private Connection conn;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public SistemaCadastroProdutos() {
        super("Cadastro de Produtos");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        conectarBanco();
        criarTabelaProdutos();

        JPanel painelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        campoNome = new JTextField();
        campoPreco = new JTextField();
        campoQuantidade = new JTextField();

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);
        painelFormulario.add(new JLabel("Preço:"));
        painelFormulario.add(campoPreco);
        painelFormulario.add(new JLabel("Quantidade:"));
        painelFormulario.add(campoQuantidade);

        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoConsultar = new JButton("Consultar");
        JButton botaoAtualizar = new JButton("Atualizar");
        JButton botaoExcluir = new JButton("Excluir");
        JButton botaoLimpar = new JButton("Limpar");

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoConsultar);
        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);

        areaProdutos = new JTextArea(10, 50);
        areaProdutos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaProdutos);

        add(painelFormulario, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        botaoCadastrar.addActionListener(e -> cadastrarProduto());
        botaoConsultar.addActionListener(e -> consultarProduto());
        botaoAtualizar.addActionListener(e -> atualizarProduto());
        botaoExcluir.addActionListener(e -> excluirProduto());
        botaoLimpar.addActionListener(e -> limparCampos());

        setVisible(true);
    }

    private void conectarBanco() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("Conexão estabelecida com sucesso!");
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver PostgreSQL não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void criarTabelaProdutos() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                "id SERIAL PRIMARY KEY," +
                "nome VARCHAR(100) UNIQUE NOT NULL," +
                "preco NUMERIC(10,2) NOT NULL," +
                "quantidade INTEGER NOT NULL" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar tabela: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos(boolean validarNome) {
        String nome = campoNome.getText().trim();
        String precoStr = campoPreco.getText().trim();
        String quantidadeStr = campoQuantidade.getText().trim();

        if (validarNome && nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            if (preco <= 0 || quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser positivos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void cadastrarProduto() {
        if (!validarCampos(true)) {
            return;
        }

        String nome = campoNome.getText().trim();
        double preco = Double.parseDouble(campoPreco.getText().trim());
        int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setDouble(2, preco);
            pstmt.setInt(3, quantidade);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparCampos();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                JOptionPane.showMessageDialog(this, "Produto com esse nome já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarProduto() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto para consultar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT preco, quantidade FROM produtos WHERE nome = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");
                areaProdutos.setText("Produto encontrado:\n");
                areaProdutos.append("Nome: " + nome + "\n");
                areaProdutos.append(String.format("Preço: R$ %.2f\n", preco));
                areaProdutos.append("Quantidade: " + quantidade + "\n");

                campoPreco.setText(String.valueOf(preco));
                campoQuantidade.setText(String.valueOf(quantidade));
            } else {
                areaProdutos.setText("Produto não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        if (!validarCampos(true)) {
            return;
        }

        String nome = campoNome.getText().trim();
        double preco = Double.parseDouble(campoPreco.getText().trim());
        int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

        String verificaSql = "SELECT id FROM produtos WHERE nome = ?";
        try (PreparedStatement verificaStmt = conn.prepareStatement(verificaSql)) {
            verificaStmt.setString(1, nome);
            ResultSet rs = verificaStmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado para atualização.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE produtos SET preco = ?, quantidade = ? WHERE nome = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, preco);
            pstmt.setInt(2, quantidade);
            pstmt.setString(3, nome);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                limparCampos();
                areaProdutos.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "DELETE FROM produtos WHERE nome = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                limparCampos();
                areaProdutos.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado para exclusão.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoPreco.setText("");
        campoQuantidade.setText("");
        areaProdutos.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaCadastroProdutos::new);
    }
}
