import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel {
	Font mainFont = new Font("Agency FB", 0, 22);
	Font settingsFont = new Font("Agency FB", 0, 30);
	private Image image;
	private CardLayout layer;
	private JPanel userViewPort, settings;
	private boolean newGameHasBeenLaunched;
	private Head snake;
	private JButton resumeButton, survivalButton, arcadeButton, settingsButton, quitButton, settingsBackButton;
	private BufferedImage img = null;

	public Menu(JPanel userViewPort) {
		this.userViewPort = userViewPort;
		this.layer = (CardLayout) (userViewPort.getLayout());

		setLayout(new GridLayout(6, 0));
		add(Box.createRigidArea(new Dimension(300, 300)));
		setBorder(BorderFactory.createEmptyBorder(100, 100, 20, 100));
		setFocusable(true);
		// setBackground(Color.black);
		resumeButton = new JButton("Resume");
		resumeButton.setFont(mainFont);
		resumeButton.setBackground(Color.darkGray);
		resumeButton.setForeground(Color.black);
		resumeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layer.show(userViewPort, "Snake");
				snake.continueGame();
			}
		});
		survivalButton = new JButton("Play Survival");
		survivalButton.setFont(mainFont);
		survivalButton.setBackground(Color.darkGray);
		survivalButton.setForeground(Color.black);
		survivalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				snake.setGameMode(1);
				layer.show(userViewPort, "Snake");
				newGameHasBeenLaunched = true;
				snake.resetGame();
				addResumeButton();
				snake.continueGame();
				// userViewPort.grabFocus();

			}
		});
		arcadeButton = new JButton("Play Arcade");
		arcadeButton.setFont(mainFont);
		arcadeButton.setBackground(Color.darkGray);
		arcadeButton.setForeground(Color.black);
		arcadeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				snake.setGameMode(2);
				layer.show(userViewPort, "Snake");
				newGameHasBeenLaunched = true;
				snake.resetGame();
				addResumeButton();
				snake.continueGame();
			}
		});
		settingsButton = new JButton("Settings");
		settingsButton.setFont(mainFont);
		settingsButton.setBackground(Color.darkGray);
		settingsButton.setForeground(Color.black);
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				layer.show(userViewPort, "Settings");
				settings.grabFocus();
			}
		});
		quitButton = new JButton("Quit");
		quitButton.setFont(mainFont);
		quitButton.setBackground(Color.darkGray);
		quitButton.setForeground(Color.black);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		add(survivalButton);
		add(arcadeButton);
		add(settingsButton);
		add(quitButton);
		try {
			img = ImageIO.read(ResourceLoader.load("menu_background.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		createSnake();
		createSettings();

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (newGameHasBeenLaunched) {
					layer.show(userViewPort, "Snake");
					snake.continueGame();
				}
			}
		});
	}

	public void createSnake() {
		snake = new Head(userViewPort);
		userViewPort.add(snake, "Snake");
		snake.setMenu(this);
	}

	public void createSettings() {
		settings = new JPanel();
		///this.paintComponent(img);
		settings.setLayout(new GridLayout(6, 0));
		settings.add(Box.createRigidArea(new Dimension(300, 300)));
		settings.setBorder(BorderFactory.createEmptyBorder(100, 100, 20, 100));
		userViewPort.add(settings, "Settings");
		settings.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
					layer.show(userViewPort, "Menu");
					grabFocus();
				}
			}
		});

		ImageIcon tick = new ImageIcon("images/tick.png");
		ImageIcon untick = new ImageIcon("images/untick.png");

		JLabel gridLabel = new JLabel("Grid");
		gridLabel.setFont(settingsFont);
		JLabel gridTick = new JLabel();
		gridTick.setIcon(untick);
		gridTick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (gridTick.getIcon().equals(untick)) {
					gridTick.setIcon(tick);
					snake.gridOn(true);
				} else {
					gridTick.setIcon(untick);
					snake.gridOn(false);
				}

			}
		});

		JLabel musicLabel = new JLabel("Music");
		musicLabel.setFont(settingsFont);
		JLabel musicTick = new JLabel();
		musicTick.setIcon(tick);
		musicTick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (musicTick.getIcon().equals(untick)) {
					musicTick.setIcon(tick);

				} else {
					musicTick.setIcon(untick);

				}

			}
		});
		JLabel soundLabel = new JLabel("Sound");
		soundLabel.setFont(settingsFont);
		JLabel soundTick = new JLabel();
		soundTick.setIcon(tick);
		soundTick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (soundTick.getIcon().equals(untick)) {
					soundTick.setIcon(tick);

				} else {
					soundTick.setIcon(untick);

				}

			}
		});

		JPanel gridPanel = new JPanel(new BorderLayout());
		gridPanel.add(gridLabel, BorderLayout.WEST);
		gridPanel.add(gridTick, BorderLayout.EAST);
		settings.add(gridPanel);
		JPanel musicPanel = new JPanel(new BorderLayout());
		musicPanel.add(musicLabel, BorderLayout.WEST);
		musicPanel.add(musicTick, BorderLayout.EAST);
		settings.add(musicPanel);
		JPanel soundPanel = new JPanel(new BorderLayout());
		soundPanel.add(soundLabel, BorderLayout.WEST);
		soundPanel.add(soundTick, BorderLayout.EAST);
		settings.add(soundPanel);
		settings.add(Box.createRigidArea(new Dimension(300, 300)));

		settingsBackButton = new JButton("Back");
		settingsBackButton.setFont(mainFont);
		settingsBackButton.setBackground(Color.darkGray);
		settingsBackButton.setForeground(Color.black);
		settingsBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				layer.show(userViewPort, "Menu");
				grabFocus();
			}
		});

		settings.add(settingsBackButton);
	}

	public void addResumeButton() {
		if (newGameHasBeenLaunched) {
			this.removeAll();
			add(Box.createRigidArea(new Dimension(300, 300)));
			add(resumeButton);
			add(survivalButton);
			add(arcadeButton);
			add(settingsButton);
			add(quitButton);
			snake.setResumeButtonRemoved(false);
		}
	}

	public void removeResumeButton() {
		newGameHasBeenLaunched = false;
		remove(resumeButton);
		snake.setResumeButtonRemoved(true);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, settings);
		
	}
}
