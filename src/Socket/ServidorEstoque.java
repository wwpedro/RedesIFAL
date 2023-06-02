package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Estoque.Estoque;
import Estoque.Produto;

public class ServidorEstoque {

    private static Estoque estoque;

    public static void main(String[] args) {
        estoque = new Estoque(); 

        try {
            ServerSocket serverSocket = new ServerSocket(8087);
            System.out.println("Servidor iniciado. Aguardando conexões...");

            do {
                Socket socket = serverSocket.accept();
                System.out.println("Conexão estabelecida com o cliente: " + socket.getInetAddress().getHostAddress());

                // THREAD permite que mais de uma tarefa fique em execução simultaneamente
                Thread clienteThread = new Thread(new ClienteHandler(socket));
                clienteThread.start();
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String mensagem;
                while ((mensagem = reader.readLine()) != null) {
                    String[] partes = mensagem.split(":");
                    String comando = partes[0];

                    if (comando.equals("ADICIONAR")) {
                        String nome = partes[1];
                        double preco = Double.parseDouble(partes[2]);
                        int quantidade = Integer.parseInt(partes[3]);
                        Produto produto = new Produto(nome, preco, quantidade);
                        estoque.adicionarProduto(produto);
                        writer.println("Produto adicionado com sucesso.");
                    } else if (comando.equals("REMOVER")) {
                        String nome = partes[1];
                        Produto produto = estoque.buscarProdutoPorNome(nome);
                        if (produto != null) {
                            estoque.removerProduto(produto);
                            writer.println("Produto removido com sucesso.");
                        } else {
                            writer.println("Produto não encontrado no estoque.");
                        }
                    } else if (comando.equals("LISTAR")) {
                        StringBuilder resposta = new StringBuilder();
                        for (Produto produto : estoque.getProdutos()) {
                            resposta.append(produto.getNome()).append(" - ").append(produto.getQuantidade()).append(" unidades\n");
                        }
                        writer.println(resposta.toString() + "\n");
                    }else {
                        writer.println("Comando inválido.");
                    }
                }

                System.out.println("Cliente desconectado: " + socket.getInetAddress().getHostAddress());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}