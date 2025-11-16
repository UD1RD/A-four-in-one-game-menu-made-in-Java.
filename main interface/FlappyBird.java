import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, KeyListener {
    
    private JFrame frame;
    private Timer timer;
    private int ticks, yMotion;
    private ArrayList<Rectangle> pipes;
    private int birdX = 200, birdY = 300, birdWidth = 20, birdHeight = 20;
    private boolean gameOver, started;
    
    public FlappyBird() {
        frame = new JFrame("Flappy Bird");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new GamePanel());
        frame.setResizable(false);
        frame.addKeyListener(this);
        frame.setVisible(true);
        
        pipes = new ArrayList<>();
        addPipe(true);
        addPipe(true);

        timer = new Timer(20, this);
        timer.start();
    }
    
    public static void main(String[] args) {
        new FlappyBird();
    }

    private void addPipe(boolean start) {
        int gap = 300;
        int width = 100;
        int height = 50 + new Random().nextInt(300);

        if (start) {
            pipes.add(new Rectangle(800, 0, width, height));
            pipes.add(new Rectangle(800, height + gap, width, 600 - height - gap));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 600, 0, width, height));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, height + gap, width, 600 - height - gap));
        }
    }

    private void paintPipe(Graphics g, Rectangle pipe) {
        g.setColor(Color.green.darker());
        g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
    }

    private void movePipes() {
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 10;
        }
        
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);

            if (pipe.x + pipe.width < 0) {
                pipes.remove(pipe);

                if (pipe.y == 0) {
                    addPipe(false);
                }
            }
        }
    }

    private void jump() {
        if (gameOver) {
            birdY = 300;
            pipes.clear();
            yMotion = 0;
            addPipe(true);
            addPipe(true);
            gameOver = false;
        }

        if (!started) {
            started = true;
        }

        if (yMotion > 0) {
            yMotion = 0;
        }

        yMotion -= 10;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;

        ticks++;

        if (started) {
            movePipes();
            
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            birdY += yMotion;

            for (Rectangle pipe : pipes) {
                if (pipe.intersects(new Rectangle(birdX, birdY, birdWidth, birdHeight))) {
                    gameOver = true;
                    birdY = pipe.y > 0 ? pipe.y - birdHeight : pipe.height;
                }
            }

            if (birdY > 580 || birdY < 0) {
                gameOver = true;
            }

            if (gameOver) {
                birdY = 300;
            }
        }

        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Background
            g.setColor(Color.cyan);
            g.fillRect(0, 0, 800, 600);
            
            // Ground
            g.setColor(Color.orange);
            g.fillRect(0, 550, 800, 50);
            
            g.setColor(Color.green);
            g.fillRect(0, 540, 800, 10);

            // Bird
            g.setColor(Color.red);
            g.fillRect(birdX, birdY, birdWidth, birdHeight);
            
            // Pipes
            for (Rectangle pipe : pipes) {
                paintPipe(g, pipe);
            }

            // Game over message
            if (gameOver) {
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Game Over", 250, 300);
            }

            if (!started) {
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawString("Press SPACE to Start", 100, 300);
            }
        }
    }
}
