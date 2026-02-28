import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame {

    private JTextField playerWinsField = new JTextField("0",5);
    private JTextField computerWinsField = new JTextField("0",5);
    private JTextField tiesField = new JTextField("0",5);
    private JTextArea resultsArea = new JTextArea(10,30);

    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    private int playerRockCount = 0;
    private int playerPaperCount = 0;
    private int playerScissorsCount = 0;
    private String lastPlayerMove = null;

    private Strategy randomStrategy = new RandomStrategy();
    private Strategy cheatStrategy = new Cheat();

    private Random rand = new Random();

    /**
     * Constructor builds GUI.
     */
    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createStatsPanel(), BorderLayout.CENTER);
        add(createResultsPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Make Your Move"));

        ImageIcon rockIcon = new ImageIcon("src/image/rock.jpeg");
        Image rockImg = rockIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JButton rockBtn = new JButton("Rock", new ImageIcon(rockImg));

        ImageIcon paperIcon = new ImageIcon("src/image/paper.jpeg");
        Image paperImg = paperIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JButton paperBtn = new JButton("Paper", new ImageIcon(paperImg));

        ImageIcon scissorsIcon = new ImageIcon("src/image/scissor.png");
        Image scissorsImg = scissorsIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JButton scissorsBtn = new JButton("Scissors", new ImageIcon(scissorsImg));

        ImageIcon quitIcon = new ImageIcon("src/image/quit.jpeg");
        Image quitImg = quitIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton quitBtn = new JButton("Quit", new ImageIcon(quitImg));

        ActionListener moveHandler = e -> {
            String move = e.getActionCommand().substring(0,1).toUpperCase();
            playGame(move);
        };

        rockBtn.addActionListener(moveHandler);
        paperBtn.addActionListener(moveHandler);
        scissorsBtn.addActionListener(moveHandler);
        quitBtn.addActionListener(e -> System.exit(0));

        panel.add(rockBtn);
        panel.add(paperBtn);
        panel.add(scissorsBtn);
        panel.add(quitBtn);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.setBorder(new TitledBorder("Game Statistics"));

        playerWinsField.setEditable(false);
        computerWinsField.setEditable(false);
        tiesField.setEditable(false);

        panel.add(new JLabel("Player Wins:"));
        panel.add(playerWinsField);
        panel.add(new JLabel("Computer Wins:"));
        panel.add(computerWinsField);
        panel.add(new JLabel("Ties:"));
        panel.add(tiesField);

        return panel;
    }

    private JScrollPane createResultsPanel() {
        resultsArea.setEditable(false);
        resultsArea.setBorder(new TitledBorder("Game Results"));
        return new JScrollPane(resultsArea);
    }

    private void playGame(String playerMove) {
        updatePlayerCounts(playerMove);

        Strategy strategy = chooseStrategy();
        String computerMove = strategy.getMove(playerMove);

        String result = determineWinner(playerMove, computerMove);
        resultsArea.append(result + "\n");

        lastPlayerMove = playerMove;
        updateStatsFields();
    }

    private Strategy chooseStrategy() {
        int probability = rand.nextInt(100) + 1;

        if (probability <= 10) return cheatStrategy;
        if (probability <= 30) return new LeastUsedStrategy();
        if (probability <= 50) return new MostUsedStrategy();
        if (probability <= 70) return new LastUsedStrategy();
        return randomStrategy;
    }

    private void updatePlayerCounts(String move) {
        switch (move) {
            case "R": playerRockCount++; break;
            case "P": playerPaperCount++; break;
            case "S": playerScissorsCount++; break;
        }
    }

    private String determineWinner(String player, String computer) {
        if (player.equals(computer)) {
            ties++;
            return "Tie game. Computer move: " + computer;
        }

        boolean playerWinsRound =
                (player.equals("R") && computer.equals("S")) ||
                        (player.equals("P") && computer.equals("R")) ||
                        (player.equals("S") && computer.equals("P"));

        if (playerWinsRound) {
            playerWins++;
            return "Player wins! Computer: " + computer;
        } else {
            computerWins++;
            return "Computer wins! Computer: " + computer;
        }
    }

    private void updateStatsFields() {
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    /**
     * Inner Strategy: Least Used
     */
    private class LeastUsedStrategy implements Strategy {
        public String getMove(String playerMove) {
            int min = Math.min(playerRockCount,
                    Math.min(playerPaperCount, playerScissorsCount));

            if (min == playerRockCount) return "P";
            if (min == playerPaperCount) return "S";
            return "R";
        }
    }

    /**
     * Inner Strategy: Most Used
     */
    private class MostUsedStrategy implements Strategy {
        public String getMove(String playerMove) {
            int max = Math.max(playerRockCount,
                    Math.max(playerPaperCount, playerScissorsCount));

            if (max == playerRockCount) return "P";
            if (max == playerPaperCount) return "S";
            return "R";
        }
    }

    /**
     * Inner Strategy: Last Used
     */
    private class LastUsedStrategy implements Strategy {
        public String getMove(String playerMove) {
            if (lastPlayerMove == null)
                return randomStrategy.getMove(playerMove);

            switch (lastPlayerMove) {
                case "R": return "P";
                case "P": return "S";
                case "S": return "R";
                default: return "R";
            }
        }
    }

    /**
     * Program entry point.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RockPaperScissorsFrame::new);
    }
}