import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class quadfusion {
    private JFrame frame;
    private JLabel[] gameOptions;
    private int selectedIndex;

    public quadfusion() {
        frame = new JFrame("QuadFusion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 1));
        frame.setResizable(false);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        String[] games = {"Flappy Bird", "Snake", "Dino Game", "Pong"};
        gameOptions = new JLabel[games.length];

        // Create option labels
        for (int i = 0; i < games.length; i++) {
            gameOptions[i] = new JLabel(games[i], SwingConstants.CENTER);
            gameOptions[i].setFont(new Font("Arial", Font.PLAIN, 24));
            gameOptions[i].setOpaque(true);
            gameOptions[i].setBackground(Color.LIGHT_GRAY);
            frame.add(gameOptions[i]);
        }

        selectedIndex = 0;
        updateSelection();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        selectedIndex = (selectedIndex - 1 + games.length) % games.length;
                        updateSelection();
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedIndex = (selectedIndex + 1) % games.length;
                        updateSelection();
                        break;
                    case KeyEvent.VK_ENTER:
                        openGame(games[selectedIndex]);
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        frame.setVisible(true);
        frame.setFocusable(true);
    }

    private void updateSelection() {
        for (int i = 0; i < gameOptions.length; i++) {
            if (i == selectedIndex) {
                gameOptions[i].setBackground(Color.YELLOW);
            } else {
                gameOptions[i].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void openGame(String gameName) {
        try {
            switch (gameName) {
                case "Flappy Bird":
                    FlappyBird.main(null); // Call the main method of FlappyBird class
                    break;
                case "Snake":
                    SnakeGame.main(null); // Call the main method of SnakeGame class
                    break;
                case "Dino Game":
                    App.main(null); // Call the main method of App class
                    break;
                case "Pong":
                    PongGame.main(null); // Call the main method of PongGame class
                    break;
                default:
                    throw new IllegalArgumentException("Unknown game: " + gameName);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading game: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(quadfusion::new);
    }
}


