import java.util.List;

public interface BetGame {
    void addPlayer(Player p);
    List<Player> getPlayers();
    void setNumbersBound(int l, int r);
    List<Integer> getNumbersBound();
    String randomizeWinner();
    String staticWinner(int value);
}
