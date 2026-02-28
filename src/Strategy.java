/**
 * Strategy interface for determining computer move.
 */
public interface Strategy {
    /**
     * Determines the computer's move.
     * @param playerMove R, P, or S
     * @return computer move R, P, or S
     */
    String getMove(String playerMove);
}
