import java.io.IOException;

public class Server {
    static void main() throws IOException {
        ServerPrizeDraw spd = new ServerPrizeDraw(1, 99);
        ServerAssign sa = new ServerAssign(spd);
        new Thread(sa).start();
        new Thread(spd).start();
        spd.input();
    }
}
