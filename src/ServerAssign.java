import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServerAssign implements Runnable{
    private final int port = 85;
    BetGame game;
    private ServerSocket serverSocket;

    ServerAssign(BetGame game) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.game = game;
    }

    void startConnections() throws IOException {
        System.out.println("Recebendo conexao...");
        while (true){
            Socket client = serverSocket.accept();
            System.out.println(client.getInetAddress() + " conectou");
            new Thread(new ClientHandle(client, game)).start();
        }
    }

    @Override
    public void run() {
        try {
            startConnections();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ClientHandle implements Runnable{
        Socket s;
        BufferedReader in;
        PrintWriter out;
        BetGame game;

        ClientHandle(Socket s, BetGame game) throws IOException {
            this.s = s;
            s.setSoTimeout(10000);
            in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
            this.game = game;
        }

        @Override
        public void run() {
            try {
                String name = null;
                Integer bet = null;
                name = in.readLine();

                out.println("Seja bem vindo ao jogo "+ name + "!");
                var bounds = game.getNumbersBound();
                out.println("Digite um palpite entre " + bounds.get(0) + " e "+ bounds.get(1) + ".");
                try{
                    bet = Integer.parseInt(in.readLine());
                }catch (NumberFormatException e){
                    bet = null;
                }

                if(bet == null || (bet < bounds.get(0) || bet > bounds.get(1))){
                    out.println("Entrada Invalida, conecte novamente");
                }
                else{
                    game.addPlayer(new Player(name, bet));
                    List<Player> playerList = game.getPlayers();
                    out.println(playerList.size()-1);
                    out.println((1.00f/playerList.size()*100));
                    out.println(bet);
                }
                s.close();
            } catch (SocketTimeoutException ex){
                System.out.println("O cliente " + s.getInetAddress() + " demerou muito e foi desconectado");
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                System.out.println(s.getInetAddress() + "Saiu antes de terminar a configuracao");;
            }
        }
    }
}