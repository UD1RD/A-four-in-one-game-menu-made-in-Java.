import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GP extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	static final int WIDTH = 500;
	static final int HEIGHT = 500;
	static final int UNIT_SIZE = 20;
	static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

	// Game states
	enum STATE {
		MENU,
		GAME,
		END
	};

	STATE state = STATE.MENU;

	// Snake body coordinates
	final int x[] = new int[NUMBER_OF_UNITS];
	final int y[] = new int[NUMBER_OF_UNITS];

	int length;
	int foodEaten;
	int foodX;
	int foodY;
	char direction;
	boolean running = false;
	Random random;
	Timer timer;

	GP() {
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.DARK_GRAY);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.addMouseListener(new MyMouseAdapter());
		startMenu();
	}

	public void startMenu() {
		state = STATE.MENU;
		repaint();
	}

	public void play() {
		resetGame();
		addFood();
		running = true;
		state = STATE.GAME;

		timer = new Timer(80, this);
		timer.start();
	}

	public void endGame() {
		running = false;
		timer.stop();
		state = STATE.END;
		repaint();
	}

	public void resetGame() {
		length = 5;
		foodEaten = 0;
		direction = 'D';
		running = false;

		// Reset snake's position
		for (int i = 0; i < length; i++) {
			x[i] = 0;
			y[i] = 0;
		}

		if (timer != null) {
			timer.stop();
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		if (state == STATE.MENU) {
			drawMenu(graphics);
		} else if (state == STATE.GAME) {
			draw(graphics);
		} else if (state == STATE.END) {
			drawEndScreen(graphics);
		}
	}

	public void drawMenu(Graphics graphics) {
		graphics.setColor(Color.white);
		graphics.setFont(new Font("Sans serif", Font.BOLD, 50));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Snake Game", (WIDTH - metrics.stringWidth("Snake Game")) / 2, HEIGHT / 3);

		graphics.setFont(new Font("Sans serif", Font.PLAIN, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Click to Start", (WIDTH - metrics.stringWidth("Click to Start")) / 2, HEIGHT / 2);
	}

	public void drawEndScreen(Graphics graphics) {
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Sans serif", Font.BOLD, 50));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 3);

		graphics.setColor(Color.white);
		graphics.setFont(new Font("Sans serif", Font.PLAIN, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, HEIGHT / 2);

		graphics.drawString("Click to Restart", (WIDTH - metrics.stringWidth("Click to Restart")) / 2, (HEIGHT / 2) + 50);
	}

	public void move() {
		for (int i = length; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
			case 'L':
				x[0] -= UNIT_SIZE;
				break;
			case 'R':
				x[0] += UNIT_SIZE;
				break;
			case 'U':
				y[0] -= UNIT_SIZE;
				break;
			case 'D':
				y[0] += UNIT_SIZE;
				break;
		}
	}

	public void checkFood() {
		if (x[0] == foodX && y[0] == foodY) {
			length++;
			foodEaten++;
			addFood();
		}
	}

	public void draw(Graphics graphics) {
		if (running) {
			graphics.setColor(new Color(210, 115, 90));
			graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

			graphics.setColor(Color.white);
			graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

			for (int i = 1; i < length; i++) {
				graphics.setColor(new Color(40, 200, 150));
				graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}

			graphics.setColor(Color.white);
			graphics.setFont(new Font("Sans serif", Font.PLAIN, 25));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
		}
	}

	public void addFood() {
		foodX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		foodY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void checkHit() {
		for (int i = length; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}

		if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
			running = false;
		}

		if (!running) {
			endGame();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkFood();
			checkHit();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (state == STATE.GAME) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						if (direction != 'R') {
							direction = 'L';
						}
						break;

					case KeyEvent.VK_RIGHT:
						if (direction != 'L') {
							direction = 'R';
						}
						break;

					case KeyEvent.VK_UP:
						if (direction != 'D') {
							direction = 'U';
						}
						break;

					case KeyEvent.VK_DOWN:
						if (direction != 'U') {
							direction = 'D';
						}
						break;
				}
			}
		}
	}

	public class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (state == STATE.MENU) {
				play(); // Start the game from the menu
			} else if (state == STATE.END) {
				startMenu(); // Go back to the main menu after game over
			}
		}
	}
}








































