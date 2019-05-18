import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Dimension STATIC_SIZE = new Dimension(530, 530);
	private CardLayout layer;
	private JPanel userViewPort;

	JPanel buttonPanel;

	public Frame() {
		super("Pitonda 3000");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(STATIC_SIZE);
		setResizable(false);
		setLocationRelativeTo(null);
	//	setFocusable(true);
		//requestFocusInWindow();
		//snake = new Head();
		userViewPort = new JPanel(new CardLayout());
		Menu menu = new Menu(userViewPort);
		//userViewPort.add(snake, "Snake");
		userViewPort.add(menu, "Menu");
		layer = (CardLayout) (userViewPort.getLayout());
		layer.show(userViewPort, "Menu");
		add(userViewPort);
		setFocusable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Frame();

	}
}
