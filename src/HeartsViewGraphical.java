import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HeartsViewGraphical extends JFrame implements HeartsView,ActionListener {
	
	private static final long serialVersionUID = 1L;
	private int APPLET_WIDTH = 1000, APPLET_HEIGHT = 720, OUTSIDE_PADDING = 20;
	private int STACK_WIDTH = 12*Hand.PADDING+Card.WIDTH, STACK_HEIGHT = Card.HEIGHT;
	private int MIDDLE_W = APPLET_WIDTH/2, MIDDLE_H = APPLET_HEIGHT/2;
	private int[] x = {MIDDLE_W-STACK_WIDTH/2,OUTSIDE_PADDING,MIDDLE_W-STACK_WIDTH/2,APPLET_WIDTH-STACK_WIDTH-OUTSIDE_PADDING};
	private int[] y = {APPLET_HEIGHT-STACK_HEIGHT-OUTSIDE_PADDING-20,MIDDLE_H-STACK_HEIGHT/2-20,OUTSIDE_PADDING*2,MIDDLE_H-STACK_HEIGHT/2-20};
	private int[] trickX = {MIDDLE_W-Card.WIDTH/2,MIDDLE_W-Card.WIDTH/2-OUTSIDE_PADDING*2,MIDDLE_W-Card.WIDTH/2,MIDDLE_W-Card.WIDTH/2+OUTSIDE_PADDING*2};
	private int[] trickY = {MIDDLE_H-Card.HEIGHT/2+OUTSIDE_PADDING*2,MIDDLE_H-Card.HEIGHT/2,MIDDLE_H-Card.HEIGHT/2-OUTSIDE_PADDING*2,MIDDLE_H-Card.HEIGHT/2};
	private Canvas canvas;
	private JMenuBar menu;
	private JMenu fileMenu,editMenu,gameMenu,viewMenu;
	private JMenuItem newItem,mainMenuItem,openItem,saveItem,exportItem,undoItem,redoItem,editPlayerItem,redealItem,scoreItem,fullScreenItem;
	private JMenuItem startTrainingGameItem,exitTrainingItem;
	private JCheckBoxMenuItem isDelayItem, showAICardsItem;
	private HomePanel home;
	private HeartsGame game;
	private Card moveChosen = null;
	BufferedImage feltImage = null;
	ArrayList<int[]> scores = new ArrayList<int[]>();
	int[] finalScore = {0,0,0,0};
	private Card hover = null;
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	private int AIWins = 0;
	private int randomWins = 0;
	private boolean isTrainingGame = false, isDelay = true, showCards = false;
	private String[] players;
	private ArrayList<SwingWorker<Card,Card>> workers = new ArrayList<SwingWorker<Card,Card>>();
	Random rand = new Random();
	double rand1,rand2;
	double[] randoms = new double[15];


	public HeartsViewGraphical() {
		super("Hearts");
		setSize(APPLET_WIDTH,APPLET_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(900, 648));
		setVisible(false);
		setLayout(new BorderLayout());
		loadImages();
	    // needed on mac os x
	    System.setProperty("apple.laf.useScreenMenuBar", "true");
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		menu = new JMenuBar();

		
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		gameMenu = new JMenu("Game");
		viewMenu = new JMenu("View");
		
		newItem = new JMenuItem("Start New Game");
		newItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		newItem.addActionListener(this);
		mainMenuItem = new JMenuItem("Exit to Main Menu");
		mainMenuItem.setEnabled(false);
		mainMenuItem.addActionListener(this);
		openItem = new JMenuItem("Open");
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		saveItem = new JMenuItem("Save");
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		saveItem.setEnabled(false);
		startTrainingGameItem = new JMenuItem("Start AI Training Game");
		startTrainingGameItem.addActionListener(this);
		exportItem = new JMenuItem("Export");
		exportItem.setEnabled(false);
		undoItem = new JMenuItem("Undo");
		undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		undoItem.setEnabled(false);
		redoItem = new JMenuItem("Redo");
		redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		redoItem.setEnabled(false);
		editPlayerItem = new JMenuItem("Edit Player Names");
		editPlayerItem.setEnabled(false);
		editPlayerItem.addActionListener(this);
		redealItem = new JMenuItem("Redeal");
		redealItem.setEnabled(false);
		redealItem.addActionListener(this);
		scoreItem = new JMenuItem("Score");
		scoreItem.setEnabled(false);
		isDelayItem = new JCheckBoxMenuItem("Toggle AI Delay");
		isDelayItem.addActionListener(this);
		isDelayItem.setState(true);
		showAICardsItem = new JCheckBoxMenuItem("Toggle show/hide AI cards");
		showAICardsItem.addActionListener(this);
		showAICardsItem.setState(false);
		exitTrainingItem = new JMenuItem("Exit Training Mode");
		exitTrainingItem.setEnabled(false);
		exitTrainingItem.addActionListener(this);
		fullScreenItem = new JMenuItem("Maximize");
		fullScreenItem.addActionListener(this);
		fileMenu.add(newItem);
		fileMenu.add(mainMenuItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		gameMenu.add(editPlayerItem);
		gameMenu.add(redealItem);
		gameMenu.add(scoreItem);
		gameMenu.add(exportItem);
		gameMenu.add(isDelayItem);
		gameMenu.add(showAICardsItem);
		gameMenu.add(startTrainingGameItem);
		gameMenu.add(exitTrainingItem);
		viewMenu.add(fullScreenItem);
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(gameMenu);
		menu.add(viewMenu);
		setJMenuBar(menu);
		HeartsViewGraphical.enableOSXFullscreen(this);
		resetPlayerNames();
		showHome();
	}
	
	public void resetPlayerNames() {
		players = new String[4];
		if (isTrainingGame) {
			players[0] = "PC 1";
			players[1] = "Mac 1";
			players[2] = "PC 1";
			players[3] = "Mac 2";
		} else {
			players[0] = "Player 1";
			players[1] = "Player 2";
			players[2] = "Player 3";
			players[3] = "Player 4";
		}
	}
	
	public Player[] getPlayers() {
		Player[] playerArray = new Player[4];
		if (isTrainingGame) {
			double[] loseTrick = {50,3,50,100,50,2,2,5,50,2,100,50,1,20,1,1,20,1,50,100,2,5};
			double[] winJack = {50,3,4,50,100,50,100,40,2,2,5,50,2,50,20,25,30,100,50,50,1,20,25,30,1,20,1,1,50,20,1,50,50,100,100,100,30,20,25,30,10};
			double[] shootMoon = {40,10,2,2,50,2,80,100,1,20,1,1,20,1,14.0};
			double[] shootSun = {40,10,2,2,50,2,80,100,1,20,1,1,20,1,16.0};
			double[] loseTrick1 = {50,3,50,100,50,2,2,5,50,2,100,50,1,20,1,1,20,1,50,100,2,5};
			double[] winJack1 = {50,3,4,50,100,50,100,40,2,2,5,50,2,50,20,25,30,100,50,50,1,20,25,30,1,20,1,1,50,20,1,50,50,100,100,100,30,20,25,30,10};
			double[] shootMoon1 = {40,10,2,2,50,2,80,100,1,20,1,1,20,1,rand1};
			double[] shootSun1 = {40,10,2,2,50,2,80,100,1,20,1,1,20,1,rand2};
			double[][] weights1 = {loseTrick,winJack,shootMoon,shootSun};
			double[][] weights2 = {loseTrick1,winJack1,randoms,randoms};
			playerArray[0] = new AIPlayer(players[0],false,weights1);
			playerArray[1] = new AIPlayer(players[1],false,weights2);
			playerArray[2] = new AIPlayer(players[2],false,weights1);
			playerArray[3] = new AIPlayer(players[3],false,weights2);
		} else {
			playerArray[0] = new HumanPlayer(players[0]);
			playerArray[1] = new AIPlayer(players[1],false,isDelay);
			playerArray[2] = new AIPlayer(players[2],false,isDelay);
			playerArray[3] = new AIPlayer(players[3],false,isDelay);
		}
		return playerArray;
	}
	
	public void showHome() {
		getContentPane().removeAll();
		//System.out.println("Loading home screen");
		mainMenuItem.setEnabled(false);
		redealItem.setEnabled(false);
		editPlayerItem.setEnabled(false);
		home = new HomePanel();
		home.setLayout(null);
		if (canvas != null) {
			this.remove(canvas);
		}
		this.add(home,BorderLayout.CENTER);
		setVisible(true);
		repaint();
	}
	
	public void startGame() {
		//System.out.println("Starting game");
		getContentPane().removeAll();
		stopAllWorkers();
		canvas = new Canvas();
		canvas.setLayout(null);
		this.add(canvas,BorderLayout.CENTER);
		game = new HeartsGame(getPlayers(),this);
		display(game);
		mainMenuItem.setEnabled(true);
		redealItem.setEnabled(true);
		editPlayerItem.setEnabled(true);
	}
	
	public void gameOver() {
		int[] roundScore = new int[4];
		for (int i=0; i<game.getPlayers().length; i++) {
			if (game.getPlayer(i).shotTheSun(game)) {
				if (game.getPlayer(i) instanceof HumanPlayer) {
					String answer = getAnswer("Shot the sun! Add or \"subtract\" 26 points?");
					if (answer.equals("subtract")) {
						for (int j=0; j<4; j++) {
							roundScore[j] = 0;
							if (i == j)
								roundScore[j] = -26;
							for (Trick t : game.getPlayer(j).getTricksWon()) {
								if (t.containsCard(Card.JACK_OF_DIAMONDS)) {
									roundScore[j] -= 10;
								}
							}
							finalScore[j] += roundScore[j];
						}
						break;
					} else {
						for (int j=0; j<4; j++) {
							roundScore[j] = 26;
							if (i == j)
								roundScore[j] = 0;
							for (Trick t : game.getPlayer(j).getTricksWon()) {
								if (t.containsCard(Card.JACK_OF_DIAMONDS)) {
									roundScore[j] -= 10;
								}
							}
							finalScore[j] += roundScore[j];
						}
						break;
					}
				} else {
					for (int j=0; j<4; j++) {
						roundScore[j] = 26;
						if (i == j)
							roundScore[j] = 0;
						for (Trick t : game.getPlayer(j).getTricksWon()) {
							if (t.containsCard(Card.JACK_OF_DIAMONDS)) {
								roundScore[j] -= 10;
							}
						}
						finalScore[j] += roundScore[j];
					}
					break;
				}
			} else if (game.getPlayer(i).shotTheMoon(game)) {
				if (game.getPlayer(i) instanceof HumanPlayer) {
					String answer = getAnswer("Shot the moon! Add or \"subtract\" 13 points?");
					if (answer.equals("subtract")) {
						for (int j=0; j<4; j++) {
							roundScore[j] = 0;
							if (i == j)
								roundScore[j] = -13
								;
							for (Trick t : game.getPlayer(j).getTricksWon()) {
								if (t.containsCard(Card.JACK_OF_DIAMONDS)) {
									roundScore[j] -= 10;
								}
							}
							finalScore[j] += roundScore[j];
						}
						break;
					} else {
						for (int j=0; j<4; j++) {
							roundScore[j] = 13;
							if (i == j)
								roundScore[j] = 0;
							for (Trick t : game.getPlayer(j).getTricksWon()) {
								if (t.containsCard(Card.JACK_OF_DIAMONDS)) {
									roundScore[j] -= 10;
								}
							}
							finalScore[j] += roundScore[j];
						}
						break;
					}
				} else {
					for (int j=0; j<4; j++) {
						roundScore[j] = 13;
						if (i == j)
							roundScore[j] = 0;
						for (Trick t : game.getPlayer(j).getTricksWon()) {
							if (t.containsCard(Card.JACK_OF_DIAMONDS)) {
								roundScore[j] -= 10;
							}
						}
						finalScore[j] += roundScore[j];
					}
					break;
				}
			} else {
				roundScore[i] = 0;
				for (Trick t : game.getPlayer(i).getTricksWon()) {
					roundScore[i] += t.getPoints();
				}
				finalScore[i] += roundScore[i];
			}
		}
		scores.add(roundScore);
		boolean isLoser = false;
		for (int i=0; i<finalScore.length; i++) {
			if (finalScore[i] >= 100) {
				isLoser = true;
			}
		}
		if (isLoser) {
			int lowest = 200;
			int winner = 0;
			for (int i=0; i<finalScore.length; i++) {
				if (finalScore[i] < lowest) {
					winner = i;
					lowest = finalScore[i];
				}
			}
			reportToUser("Game over. " + game.getPlayer(winner).getName() + " wins!");
			if (isTrainingGame) {
				if (winner == 0 || winner == 2) {
					randomWins++;
				} else {
					AIWins++;
				}
				reportToUser("Mac: " + AIWins + " PC: " + randomWins);
			}
			this.remove(canvas);
			showHome();
		} else {
			if (isTrainingGame == false) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reportToUser("New round starting.");
			}
			this.remove(canvas);
			startGame();
		}
	}
	
	public void setGame(HeartsGame game) {
		this.game = game;
	}
	
	public void display(HeartsGame game1) {
		setGame(game1);
		canvas.resetCards();
		setVisible(true);
		if (game1.getPlayerToPlay() instanceof AIPlayer) {
			SwingWorker<Card,Card> AIWorker = new SwingWorker<Card,Card>() {

				@Override
				protected Card doInBackground() throws Exception {
					workers.remove(this);
					return game.getPlayerToPlay().getMove(game,null);
				}
				
				@Override
				public void done() {
					try {
						reportPlay(get(),null);
						//repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
				
			};
			AIWorker.execute();
			workers.add(AIWorker);
		}
	}

	public Card getUserPlay(HeartsGame game) {
		while (moveChosen == null) {

		}
//		if (moveChosen != null)
//			//System.out.println("Made move: " + moveChosen);
//		else
//			//System.out.println('.');
		return moveChosen;
	}

	@Override
	public String getAnswer(String question) {
		String inputValue = JOptionPane.showInputDialog(question);
		return inputValue;
	}

	public int getIntAnswer(String question) {
		boolean valid = false;  // Has a valid number been read?
		int answer = 0;         // The answer to the question

		while(!valid) {
			// Asks user for a number
			String inputValue = JOptionPane.showInputDialog(question);
			try {
				answer = Integer.parseInt(inputValue);
				valid = true;   // If got to here we have a valid integer
			}
			catch(NumberFormatException ex) {
				reportToUser("\"" + inputValue + "\" is not a valid integer");
				valid = false;
			}
		}
		return answer;
	}

	@Override
	public void reportToUser(String message) {
		// Reports something to the user
		JOptionPane.showMessageDialog(null, message,
				message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void reportPlay(Card chosenPlay, String name) {
		//System.out.println(chosenPlay);
		if (chosenPlay == null || game == null)
			return;
		if (game.isValidMoveSilent(chosenPlay))
			game.makeMove(chosenPlay);
		else
			return;
		if (isTrainingGame == false)
			repaint();
		if (game.gameIsOver()) {
			gameOver();
		} else if (game.getPlayerToPlay() instanceof AIPlayer) {
			//System.out.println("AI is moving");
			SwingWorker<Card,Card> AIWorker = new SwingWorker<Card,Card>() {

				@Override
				protected Card doInBackground() throws Exception {
					workers.remove(this);
					if (game != null)
						return game.getPlayerToPlay().getMove(game,null);
					else
						return null;
				}
				
				@Override
				public void done() {
					try {
						reportPlay(get(),null);
						if (isTrainingGame == false)
							repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
				
			};
			AIWorker.execute();
			workers.add(AIWorker);
		}
	}
    
    private class Canvas extends JPanel implements MouseListener {
    	private static final long serialVersionUID = 1L;
		private ArrayList<CardLabel> cards;
		public Canvas() {
			super();
			addMouseListener(this);
		}
		public void paintComponent(Graphics page)
        {
			super.paintComponent(page);
			updateDimensions();
			//System.out.println("Starting paintComponent");
			//System.out.println("Printing background");
			if (feltImage != null)
				page.drawImage(feltImage, 0, 0, APPLET_WIDTH, APPLET_HEIGHT, null);
			//System.out.println("Printed background");
			//System.out.println("Removing all cardLabels");
			for (JLabel cL : cards) {
				this.remove(cL);
			}
			//System.out.println("Removed all cardLabels");
			//System.out.println("Reset cards");
			resetCards();
			//System.out.println("Done reset cards");
			//System.out.println("Adding cardLabels");
			for (int i=cards.size()-1; i>=0; i--) {
				this.add(cards.get(i));
			}
			//System.out.println("Added cardLabels. Printing strings");
			for (int i=0; i<game.getPlayers().length; i++) {
				String BACKGROUND_STRING = game.getPlayer(i).getName();
				Font BG_STRING_FONT = new Font("arial",Font.PLAIN,16);
				if (game.getPlayerToPlayNum()==i) {
					BG_STRING_FONT = new Font("arial",Font.BOLD,16);
					BACKGROUND_STRING += "'s turn";
					page.setColor(Color.WHITE);
				} else {
					page.setColor(Color.BLACK);
				}
				BACKGROUND_STRING += " ("+ game.getPlayer(i).getTricksWon().size()+" tricks)";
				page.setFont(BG_STRING_FONT);
				page.drawString(BACKGROUND_STRING, x[i], y[i]-10);
				BG_STRING_FONT = new Font("arial",Font.PLAIN,16);
				String score = finalScore[i]+" points";
				page.setFont(BG_STRING_FONT);
				FontMetrics fontMetrics = getFontMetrics(BG_STRING_FONT);
				int w = fontMetrics.stringWidth(score);
				page.drawString(score,x[i]+STACK_WIDTH-w,y[i]-10);
				page.drawLine(x[i], y[i]-5, x[i]+STACK_WIDTH, y[i]-5);
				page.setColor(Color.black);
			}
			//System.out.println("Printed strings");
        }
		public void resetCards() {
			cards = new ArrayList<CardLabel>();
			if (game != null) {     	
            	for (int i=0; i<game.getPlayers().length; i++) {		
            		Hand hand = game.getPlayer(i).getHand();
            		int iter = 0;
            		for (Card card : hand.getCards()) {
            			boolean isShowing = game.getPlayer(i) instanceof HumanPlayer ? true:false;
            			CardLabel toAdd = new CardLabel(card,isShowing||showCards);
            			cards.add(toAdd);
            			toAdd.setSize(Card.WIDTH,Card.HEIGHT);
            			if (hover != null && card.isEqualTo(hover) && game.getPlayer(i) instanceof HumanPlayer) {
            				//System.out.println("PRINTING WITH ISACTIVE");
            				toAdd.setLocation(x[i]+iter,y[i]-5);
            			}
            			else {
            				toAdd.setLocation(x[i]+iter,y[i]);
            			}
            			toAdd.setVisible(true);
            			if (game.getPlayerToPlayNum() == i && game.getPlayer(i) instanceof HumanPlayer)
            			{
            				toAdd.addMouseListener(this);
            			}
            			iter += Hand.PADDING;
            		}
            	}
        		int player = game.getCurrentTrick().getPlayerLead();
            	for (int i=0; i<game.getCurrentTrick().getCards().size(); i++) {
            		CardLabel toAdd = new CardLabel(game.getCurrentTrick().getPlayerCard(player),true);
            		cards.add(toAdd);
        			toAdd.setSize(Card.WIDTH,Card.HEIGHT);
        			toAdd.setLocation(trickX[player],trickY[player]);
        			toAdd.setVisible(true);
            		player = HeartsGame.getNextPlayer(player);
            	}
            }
		}
		@Override
		public void mouseClicked(MouseEvent e) {

		}
		@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println("Mouse clicked "+e.getSource());
			for (CardLabel j : cards) {
				if (j == e.getSource()) {
					//System.out.println("Card selected");
					if (game.isValidMove(j.getCard())) {
						//System.out.println("Valid move");
						reportPlay(j.getCard(),null);
					}
				}
			}			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			//System.out.println("Mouse entered "+e.getSource());
			for (CardLabel j : cards) {
				if (j == e.getSource()) {
					//System.out.println("Card hovered");
					if (game.isValidMoveSilent(j.getCard())) {
						//System.out.println("Valid move");
						hover = j.getCard();
						repaint();
					}
				}
			}
			if (e.getSource() == this) {
				hover = null;
				repaint();
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if (hover != null) {
				hover = null;
				repaint();
			}
			//System.out.println("Mouse exited");
			
		}
    }
    
    private class HomePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		JButton quitButton,startButton;
		public HomePanel() {
			setLayout(null);
			quitButton = new JButton("Quit");
			quitButton.setBounds(APPLET_WIDTH/2-100, 250, 200, 30);
			quitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					System.exit(0);
				}
			});
			startButton = new JButton("Start New Game");
			startButton.setBounds(APPLET_WIDTH/2-100, 200, 200, 30);
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					resetScore();
					startGame();
				}
			});		     
			add(startButton);
			add(quitButton);
		}
		
		public void paintComponent(Graphics page) {
			updateDimensions();
			quitButton.setBounds(APPLET_WIDTH/2-100, 250, 200, 30);
			startButton.setBounds(APPLET_WIDTH/2-100, 200, 200, 30);
			if (feltImage != null)
				page.drawImage(feltImage, 0, 0, APPLET_WIDTH, APPLET_HEIGHT, null);
			final String BACKGROUND_STRING = "Hearts";
			final Font BG_STRING_FONT = new Font("arial",Font.BOLD,48);
			FontMetrics fontMetrics = getFontMetrics(BG_STRING_FONT);
			int w = fontMetrics.stringWidth(BACKGROUND_STRING);
			int h = fontMetrics.getHeight();
			int bgStringX = (APPLET_WIDTH - w) / 2;
			int bgStringY = (APPLET_HEIGHT - h) / 2;
			page.setFont(BG_STRING_FONT);
			page.setColor(Color.BLACK);
			page.drawString(BACKGROUND_STRING, bgStringX, bgStringY-200);
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newItem) {
			System.out.println("New");
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure? You will lose this entire match.", "New Game?",  JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				game = null;
				resetScore();
				startGame();
			}		
		} else if (e.getSource() == mainMenuItem) {
			System.out.println("Menu");
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure? You will lose progress for this game.", "Quit?",  JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				resetScore();
				showHome();
			}
		} else if (e.getSource() == redealItem) {
			System.out.println("Redeal");
			game = null;
			startGame();
		} else if (e.getSource() == editPlayerItem) {
			String[] names = new String[4];
			for (int i=0; i<names.length; i++) {
				names[i] = game.getPlayer(i).getName();
			}
			EditPlayerDialog editPlayers = new EditPlayerDialog(this,names);
			editPlayers.setLocationRelativeTo(this);
			editPlayers.setLocation(300,200);
			editPlayers.setSize(275,200);
			editPlayers.setVisible(true);
			names = editPlayers.getNames();
			if (names != null)
			{
				for (int i=0; i<names.length; i++) {
					game.getPlayer(i).setName(names[i]);
					players[i] = names[i];
				}
				repaint();
			}
		} else if (e.getSource() == fullScreenItem) {
//			//setResizable(false);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
//			//device.setFullScreenWindow(this);
//			com.apple.eawt.event;
//			com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(window,true);
//			com.apple.eawt.Application.getApplication().requestToggleFullScreen(window);
		} else if (e.getSource() == isDelayItem) {
			isDelay = isDelayItem.getState();
			if (game != null) {
				for (Player player : game.getPlayers()) {
					if (player instanceof AIPlayer) {
						((AIPlayer) player).isDelay(isDelay);
					}
				}
			}
		} else if (e.getSource() == startTrainingGameItem) {
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure? You will lose this entire match.", "New Training Game?",  JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				game = null;
				isTrainingGame = true;
				exitTrainingItem.setEnabled(true);
				resetPlayerNames();
				resetScore();
				AIWins = 0;
				randomWins = 0;
				for (int i=0; i<randoms.length; i++) {
					randoms[i] = rand.nextDouble()*100.0;
					System.out.println(randoms[i]);
				}
				startGame();
			}	
		} else if (e.getSource() == exitTrainingItem) {
			game = null;
			isTrainingGame = false;
			exitTrainingItem.setEnabled(false);
			resetPlayerNames();
			resetScore();
			AIWins = 0;
			randomWins = 0;
			game = null;
			showHome();
		} else if (e.getSource() == showAICardsItem) {
			showCards = showAICardsItem.getState();
			if (game != null)
				canvas.repaint();
		}
		
	}
	
	/**
	 * @param window
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void enableOSXFullscreen(Window window) {
	    try {
	        Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
	        Class params[] = new Class[]{Window.class, Boolean.TYPE};
	        Method method = util.getMethod("setWindowCanFullScreen", params);
	        method.invoke(util, window, true);
	    } catch (ClassNotFoundException e1) {
	    } catch (Exception e) {
	    }
	}
	
	public void resetScore() {
		scores = new ArrayList<int[]>();
		finalScore = new int[4];
	}
	
	public void stopAllWorkers() {
		for (SwingWorker<Card,Card> worker : workers) {
			worker.cancel(true);
		}
		workers = new ArrayList<SwingWorker<Card,Card>>();
	}
	
	public void loadImages() {
		SwingWorker<BufferedImage,BufferedImage> worker = new SwingWorker<BufferedImage,BufferedImage>() {

			@Override
			protected BufferedImage doInBackground() throws Exception {
				CardLabel.setAllImages();
				BufferedImage img = null;
				try {                
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					img = ImageIO.read(classLoader.getResourceAsStream("background_felt.png"));
				} catch (IOException ex) {}
				return img;
			}

			@Override
			public void done() {
				try {
					feltImage = get();
					repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		};
		worker.execute();
		
	}
	
	public void updateDimensions() {
		APPLET_WIDTH = getContentPane().getSize().width;
		APPLET_HEIGHT = getContentPane().getSize().height;
		OUTSIDE_PADDING = 20;
		STACK_WIDTH = 12*Hand.PADDING+Card.WIDTH;
		STACK_HEIGHT = Card.HEIGHT;
		MIDDLE_W = APPLET_WIDTH/2;
		MIDDLE_H = APPLET_HEIGHT/2;
		x[0] = MIDDLE_W-STACK_WIDTH/2;
		x[1] = OUTSIDE_PADDING;
		x[2] = MIDDLE_W-STACK_WIDTH/2;
		x[3] = APPLET_WIDTH-STACK_WIDTH-OUTSIDE_PADDING;
		y[0] = APPLET_HEIGHT-STACK_HEIGHT-OUTSIDE_PADDING-20;
		y[1] = MIDDLE_H-STACK_HEIGHT/2-20;
		y[2] = OUTSIDE_PADDING*2;
		y[3] = MIDDLE_H-STACK_HEIGHT/2-20;
		trickX[0] = MIDDLE_W-Card.WIDTH/2;
		trickX[1] = MIDDLE_W-Card.WIDTH/2-OUTSIDE_PADDING*2;
		trickX[2] = MIDDLE_W-Card.WIDTH/2;
		trickX[3] = MIDDLE_W-Card.WIDTH/2+OUTSIDE_PADDING*2;
		trickY[0] = MIDDLE_H-Card.HEIGHT/2+OUTSIDE_PADDING*2;
		trickY[1] = MIDDLE_H-Card.HEIGHT/2;
		trickY[2] = MIDDLE_H-Card.HEIGHT/2-OUTSIDE_PADDING*2;
		trickY[3] = MIDDLE_H-Card.HEIGHT/2;
	}

}