package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteEstoque {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8087);
            System.out.println("Conexão estabelecida com o servidor.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String opcao;
            do {
                exibirMenu();
                opcao = reader.readLine();

                switch (opcao) {
                    case "1":
                        adicionarProduto(reader, writer, socketReader);
                        break;
                    case "2":
                        removerProduto(reader, writer, socketReader);
                        break;
                    case "0":
                        System.out.println("Encerrando o cliente...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (!opcao.equals("0"));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exibirMenu() {
        System.out.println("\nMenu do Cliente:");
        System.out.println("1. Adicionar produto");
        System.out.println("2. Remover produto");
        System.out.println("0. Sair");
        System.out.print("Digite a opção desejada: ");
    }

    private static void adicionarProduto(BufferedReader reader, PrintWriter writer, BufferedReader socketReader) throws IOException {
        System.out.print("Digite o nome do produto: ");
        String nome = reader.readLine();

        System.out.print("Digite o preço do produto: ");
        String preco = reader.readLine();

        System.out.print("Digite a quantidade do produto: ");
        String quantidade = reader.readLine();

        writer.println("ADICIONAR:" + nome + ":" + preco + ":" + quantidade);

        String resposta = socketReader.readLine();
        System.out.println(resposta);
    }

    private static void removerProduto(BufferedReader reader, PrintWriter writer, BufferedReader socketReader) throws IOException {
        System.out.print("Digite o nome do produto a ser removido: ");
        String nome = reader.readLine();

        writer.println("REMOVER:" + nome);

        String resposta = socketReader.readLine();
        System.out.println(resposta);
    }

}
