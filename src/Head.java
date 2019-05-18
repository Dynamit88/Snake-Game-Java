import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Head extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	// REAL SIZE: y= 495 x= 525
	private final int BLOCK_SIZE = 15;
	private int speed, direction, randomX, randomY, snakeLength, gameMode, level, Stringindex;
	private int[] x = new int[577];
	private int[] y = new int[577];
	private boolean gameLost, pause, resumeButtonRemoved, gridOn;
	private String gameOverString = "Game Over";
	private Timer speedTimer, levelTimer, gameOverTimer;
	private JPanel userViewPort;
	private JLabel snakeLengthCount, centralJlabel;
	private Font fontLevel = new Font("Arial", 30, 30);
	private CardLayout layer;
	private Menu menu;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameLost) {
			if (direction != 0 && !pause) {
				move();
			}
			if (x[0] == randomX && y[0] == randomY) {
				snakeLength++;
				snakeLengthCount.setText("Snake Length: " + (getSnakeLength() - 3));
				generateRandomPoint();

				if (getSnakeLength() % 5 == 0) {
					level++;
					if (speed > 60) {
						speed = speed - 5;
						speedTimer.setDelay(speed);
					}
					centralJlabel.setText("Level " + level);
					centralJlabel.setVisible(true);
					levelTimer.restart();
				}
			}
			if (snakeLength > 3) {
				for (int i = 1; i < snakeLength; i++) {
					if (x[0] == x[i] && y[0] == y[i]) {
						gameLost = true;
					}
				}
			}
			if (gameMode == 1) {
				if (x[0] == -15 || x[0] == 525 || y[0] == -15 || y[0] == 495) {
					gameLost = true;
				}
			}
			repaint();
		} else if (gameLost && !pause) {
			centralJlabel.setText("");
			centralJlabel.setVisible(true);
			speedTimer.stop();
			Stringindex = 0;
			gameOverTimer.restart();
			if (!resumeButtonRemoved) {
				menu.removeResumeButton();
			}
			pause();
		}
	}

	public void setResumeButtonRemoved(boolean resumeButtonRemoved) {
		this.resumeButtonRemoved = resumeButtonRemoved;
	}
	
	

	public Head(JPanel userViewPort) {
		this.userViewPort = userViewPort;
		layer = (CardLayout) (userViewPort.getLayout());
		speed = 200;
		speedTimer = new Timer(speed, this);
		levelTimer = new Timer(3000, hideLevelMessageDelay);
		// gameOverTimer = new Timer(3000, exitDelay);
		gameOverTimer = new Timer(100, gameOverMessage);
		setLayout(new BorderLayout());

		snakeLengthCount = new JLabel();
		snakeLengthCount.setForeground(Color.RED);
		centralJlabel = new JLabel("", SwingConstants.CENTER);
		centralJlabel.setFont(fontLevel);
		centralJlabel.setForeground(Color.LIGHT_GRAY);
		add(snakeLengthCount, BorderLayout.NORTH);
		add(centralJlabel, BorderLayout.CENTER);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				// System.out.println(userViewPort.getComponent(1).isShowing());
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (userViewPort.getComponent(0).isVisible()) {
						layer.show(userViewPort, "Menu");
						pause();
						menu.grabFocus();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (direction != 4) {
						direction = 3;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (direction != 3) {
						direction = 4;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (direction != 1) {
						direction = 2;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (direction != 2) {
						direction = 1;
					}
				}
			}
		});
	}

	public void resetGame() {
		setBackground(Color.black);
		snakeLength = 3;
		direction = 0;
		level = 1;
		for (int i = 0; i < snakeLength; i++) {
			x[i] = 255;
			y[i] = 225 + i * BLOCK_SIZE;
		}
		generateRandomPoint();
		speedTimer.start();
		gameLost = false;
		snakeLengthCount.setText("");
		centralJlabel.setVisible(false);
		continueGame();
	}

	public void move() {
		for (int i = snakeLength; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		if (direction == 3) {
			if (x[0] == 0 && gameMode == 2) {
				x[0] = 525;
			}
			x[0] -= BLOCK_SIZE;
		} else if (direction == 4) {
			if (x[0] == 510 && gameMode == 2) {
				x[0] = -15;
			}
			x[0] += BLOCK_SIZE;
		} else if (direction == 1) {
			if (y[0] == 0 && gameMode == 2) {
				y[0] = 495;
			}
			y[0] -= BLOCK_SIZE;
		} else if (direction == 2) {
			if (y[0] == 480 && gameMode == 2) {
				y[0] = -15;
			}
			y[0] += BLOCK_SIZE;
		}
	}

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.setColor(Color.green);
		for (int i = 0; i < snakeLength; i++) {
			g.fillRect(x[i], y[i], BLOCK_SIZE, BLOCK_SIZE);
		}

		if (gridOn) {
			for (int y = BLOCK_SIZE; y < 515; y = y + BLOCK_SIZE) {
				g.drawLine(y, 0, y, 495);
			}
			for (int x = BLOCK_SIZE; x < 495; x = x + BLOCK_SIZE) {
				g.drawLine(0, x, 525, x);
			}
		}
		g.setColor(Color.ORANGE);
		g.fillRect(randomX, randomY, BLOCK_SIZE, BLOCK_SIZE);

	}

	private void generateRandomPoint() {
		randomX = ThreadLocalRandom.current().nextInt(0, 510);
		randomY = ThreadLocalRandom.current().nextInt(0, 480);
		while (randomX % 15 != 0) {
			if (randomX % 15 > 7) {
				randomX++;
			} else {
				randomX--;
			}
		}
		while (randomY % 15 != 0) {
			if (randomY % 15 > 7) {
				randomY++;
			} else {
				randomY--;
			}
		}
	}

	public void pause() {
		pause = true;
	}

	public void continueGame() {
		pause = false;
		this.grabFocus();
	}

	public void gridOn(boolean gridOn) {
		this.gridOn = gridOn;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public int getSnakeLength() {
		return snakeLength;
	}

	ActionListener hideLevelMessageDelay = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			centralJlabel.setVisible(false);
			levelTimer.stop();
		}
	};
	ActionListener gameOverMessage = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (Stringindex < gameOverString.length()) {
				centralJlabel.setText(centralJlabel.getText() + gameOverString.charAt(Stringindex));
				Stringindex++;
			} else {
				gameOverTimer.stop();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				layer.show(userViewPort, "Menu");
			}
		}
	};
}
