import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerPrizeDraw implements BetGame, Runnable{
    private final int port = 88;
    ServerSocket serverSocket;
    List<Socket> listeners;
    private List<Player> players;
    int boundLeft;
    int boundRight;

    ServerPrizeDraw(int boundLeft, int boundRight) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.players = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.boundLeft = boundLeft;
        this.boundRight = boundRight;
    }

    @Override
    public void addPlayer(Player p) {
        players.add(p);
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public void setNumbersBound(int l, int r) {
        this.boundLeft = l;
        this.boundRight = r;
    }

    @Override
    public List<Integer> getNumbersBound() {
        return List.of(boundLeft, boundRight);
    }

    void input() throws IOException {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("r para aleatorio ou um valor entre " + boundLeft + " ou " + boundRight );
            String in = sc.nextLine();
            if(in.equals("r")){
                broadcastMessage(randomizeWinner());
            } else {
                try{
                    int v = Integer.parseInt(in);
                    broadcastMessage(staticWinner(v));
                }
                catch (NumberFormatException e){
                    System.out.println("Invalido tente dnv");
                }
            }
        }
    }

    void broadcastMessage(String message) throws IOException {
        for(var s : listeners){
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
            out.println(message);
        }
    }

    String genereteResult(int value){
        String winners = "Vencedores: ";
        for(var p : players){
            if(p.getBet() == value){
                winners += p.getName() + " - ";
            }
        }
        winners += "Valor Escolhido: " + value;

        return winners;
    }

    @Override
    public String randomizeWinner() {
        Random rand = new Random();
        int v = rand.nextInt((boundRight - boundLeft) + 1) + boundLeft;

        return genereteResult(v);
    }

    @Override
    public String staticWinner(int value) {
        return genereteResult(value);
    }

    @Override
    public void run() {
        while (true){
            try {
                Socket s = serverSocket.accept();
                System.out.println(s.getInetAddress() + " entrou no server de resposta");
                listeners.add(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
