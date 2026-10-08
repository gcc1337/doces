public class Player{
    private String name;
    private int bet;

    public String getName() {
        return name;
    }

    public int getBet() {
        return bet;
    }

    Player(String name, int bet){
        this.name = name;
        this.bet = bet;
    }
}