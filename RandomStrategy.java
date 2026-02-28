import java.util.Random;

/**
 * Random move strategy.
 */
public class RandomStrategy implements Strategy {

    private final Random rand = new Random();
    private final String[] moves = {"R","P","S"};

    @Override
    public String getMove(String playerMove) {
        return moves[rand.nextInt(3)];
    }
}
