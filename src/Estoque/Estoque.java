package Estoque;

import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private List<Produto> produtos;

    public Estoque() {
        produtos = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto) {
    	boolean produtoExistente = false;
        
        for (Produto p : produtos) {
            if (p.getNome().equals(produto.getNome())) {
                produtoExistente = true;
                break;
            }
        }
        
        if (!produtoExistente) {
            produtos.add(produto);
            System.out.println("Produto adicionado com sucesso!");
        } else {
            System.out.println("Já existe um produto com o mesmo nome no estoque!");
        }
    }

    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }
    
    //usado para buscar por nome e remover por exemplo
    public Produto buscarProdutoPorNome(String nome) {
        for (Produto produto : produtos) {
            if (produto.getNome().equals(nome)) {
                return produto;
            }
        }
        return null;
    }
}
