import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

class Client {
    void main() throws IOException {
        String host = "127.0.0.1";
        int port1 = 85;
        System.out.println("Conectando...");
        Socket s = new Socket(host, port1);
        System.out.println("Conectado!");
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);

        Scanner sc = new Scanner(System.in);

        try{
            //Identificar-se: enviar uma String (ex.: seu nome, RA ou email) + \n.
            System.out.print("Indentificacao: ");
            out.println(sc.nextLine());

            //Receber uma mensagem de boas-vindas (String).
            System.out.println(in.readLine());

            //Receber uma mensagem de prompt (String) solicitando um palpite;
            System.out.println(in.readLine());

            //enviar o palpite (int) + \n.
            Integer bet = null;
            try{
                bet = sc.nextInt();
            }catch (InputMismatchException e){
                bet = null;
            }
            if(bet != null)
            {
                String b = bet + "\n";
                out.println(b);
            }
            else{
                System.out.println("Entrada invalida");
                s.close();
                return;
            }
            //Receber, em três linhas, nesta ordem:
            //a) Concorrência até o momento da inscrição (int)
            //b) Probabilidade de ganhar (double)
            //c) Seu número da sorte (int)
            System.out.println("Concorrentes: " +  in.readLine());
            System.out.println("Probabilidade de vitoria: " + in.readLine());
            System.out.println("Numero escolhido: " + in.readLine());
        }catch (SocketException e){
            System.out.println("A conexao com o server foi fechada");
            return;
        }

        s.close();

        int port2 = 88;
        //Aguardar o sorteio conectando ao mesmo host na porta 88 (TCP) e ler a mensagem final
        s = new Socket(host, port2);
        BufferedReader res = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
        System.out.println(res.readLine());

        s.close();
    }
}
