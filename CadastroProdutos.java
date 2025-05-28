import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class SistemaCadastroProdutos extends JFrame {
    private JTextField campoNome, campoPreco, campoQuantidade;
    private JTextArea areaProdutos;
    private ArrayList<Produto> produtos;

    public SistemaCadastroProdutos() {
        super("Cadastro de Produtos");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        produtos = new ArrayList<>();
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
        JButton botaoListar = new JButton("Listar");
        JButton botaoLimpar = new JButton("Limpar");
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoListar);
        painelBotoes.add(botaoLimpar);
        areaProdutos = new JTextArea();
        areaProdutos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaProdutos);
        add(painelFormulario, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarProduto();
            }
        });

        botaoListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarProdutos();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        setVisible(true);
    }

    private void cadastrarProduto() {
        String nome = campoNome.getText().trim();
        String precoStr = campoPreco.getText().trim();
        String quantidadeStr = campoQuantidade.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            if (preco <= 0 || quantidade <= 0) {
                throw new NumberFormatException();
            }
            Produto produto = new Produto(nome, preco, quantidade);
            produtos.add(produto);
            limparCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser valores numéricos positivos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarProdutos() {
        areaProdutos.setText("Produtos cadastrados: \n");
        if (produtos.isEmpty()) {
            areaProdutos.setText("Nenhum produto cadastrado.");
        } else {
            for (Produto p : produtos) {
                areaProdutos.append(p.toString() + "\n");
            }
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoPreco.setText("");
        campoQuantidade.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaCadastroProdutos());
    }

    class Produto {
        private String nome;
        private double preco;
        private int quantidade;

        public Produto(String nome, double preco, int quantidade) {
            this.nome = nome;
            this.preco = preco;
            this.quantidade = quantidade;
        }

        public String toString() {
            return "Nome: " + nome + " | Preço: R$" + String.format("%.2f", preco) + " | Quantidade: " + quantidade;
        }
    }
}
