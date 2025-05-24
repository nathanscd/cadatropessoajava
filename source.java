import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CadastroProdutos extends JFrame {

    private JTextField nomeField, precoField, quantidadeField;
    private JTextArea areaProdutos;
    private ArrayList<Produto> listaProdutos;

    public CadastroProdutos() {
        super("Cadastro de Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        listaProdutos = new ArrayList<>();

        JPanel painelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelFormulario.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        painelFormulario.add(nomeField);

        painelFormulario.add(new JLabel("Preço:"));
        precoField = new JTextField();
        painelFormulario.add(precoField);

        painelFormulario.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField();
        painelFormulario.add(quantidadeField);

        JButton cadastrarBtn = new JButton("Cadastrar Produto");
        JButton limparBtn = new JButton("Limpar Campos");
        painelFormulario.add(cadastrarBtn);
        painelFormulario.add(limparBtn);

        areaProdutos = new JTextArea();
        areaProdutos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaProdutos);

        add(painelFormulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        cadastrarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarProduto();
            }
        });

        limparBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        setVisible(true);
    }

    private void cadastrarProduto() {
        String nome = nomeField.getText().trim();
        String precoTexto = precoField.getText().trim();
        String quantidadeTexto = quantidadeField.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double preco = Double.parseDouble(precoTexto);
            int quantidade = Integer.parseInt(quantidadeTexto);

            if (preco < 0 || quantidade < 0) {
                throw new NumberFormatException();
            }

            Produto produto = new Produto(nome, preco, quantidade);
            listaProdutos.add(produto);
            atualizarAreaProdutos();
            limparCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser numéricos e positivos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarAreaProdutos() {
        areaProdutos.setText("");
        for (Produto p : listaProdutos) {
            areaProdutos.append(p + "\n");
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        precoField.setText("");
        quantidadeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroProdutos());
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
