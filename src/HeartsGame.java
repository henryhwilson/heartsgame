public class HeartsGame {
	private Player[] players;
	private int playerToPlay;
	private static final int NUM_PLAYERS = 4;
	private boolean isOnFirstPlay;
	private boolean isOnFirstLead;
	private boolean heartsBroken;
	private Trick currentTrick;
	private HeartsView view;
	
	public HeartsGame(HeartsView view) {
		this.view = view;
		players = new Player[4];
		for (int i=0; i<NUM_PLAYERS; i++) {
			players[i] = new HumanPlayer("Player " + (i+1));
		}
		deal();
	}
	
	public HeartsGame(Player[] players, HeartsView view) {
		this.view = view;
		this.players = players;
		deal();
	}
	
	public void deal() {
		Deck deck = new Deck();
		deck.shuffle();
		int playerToGetCard = 0;
		while (deck.hasNextCard()) {
			players[playerToGetCard].getHand().addCard(deck.getNextCard());
			playerToGetCard++;
			if (playerToGetCard == 4)
				playerToGetCard = 0;
		}
		for (int i=0; i<NUM_PLAYERS; i++) {
			if (players[i].getHand().containsCard(Card.TWO_OF_CLUBS)) {
				playerToPlay = i;
				break;
			}
		}
		isOnFirstPlay = true;
		isOnFirstLead = true;
		heartsBroken = false;
		currentTrick = new Trick(playerToPlay);
		sortAll();
	}
	
	public Player getPlayer(int player) {
		return players[player];
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public int getPlayerToPlayNum() {
		return playerToPlay;
	}
	
	public Player getPlayerToPlay() {
		return players[getPlayerToPlayNum()];
	}
	
	public void sortAll() {
		for (int i=0; i<players.length; i++) {
			players[i].getHand().sortBySuitAlternating();;
		}
	}
	
	public boolean gameIsOver() {
		if (players[0].getHand().getCards().size() > 0 || currentTrick.getCards().size() != 4)
			return false;
		else
			return true;
	}
	
	public void makeMove(Card move) {
		if (currentTrick.getCards().size() == 4) {
			currentTrick = new Trick(playerToPlay);
		}
//		view.reportPlay(move,getPlayerToPlay().getName());
		currentTrick.addCard(move);
		getPlayerToPlay().getHand().removeCard(move);
		isOnFirstLead = false;
		if (currentTrick.getCards().size() == 4) {
			playerToPlay = currentTrick.getPlayerWon();
			//view.reportToUser(getPlayerToPlay().getName() + " won the trick");
			Trick copy = currentTrick.copyTrick();
			getPlayerToPlay().addTrick(copy);
			if (heartsBroken == false)
				if (currentTrick.containsPenaltyCards())
					heartsBroken = true;
			isOnFirstPlay = false;
		} else {
			playerToPlay = getNextPlayer(playerToPlay);
		}
	}
	
	public boolean isValidMove(Card move) {
		if (!getPlayerToPlay().getHand().containsCard(move)) {
			view.reportToUser("You can't play a card you don't own!");
			return false; // Don't own card
		}
		if (isOnFirstLead && move.isEqualTo(Card.TWO_OF_CLUBS) != true) {
			view.reportToUser("You must lead the two of clubs!");
			return false; // Must play two of clubs
		}
		if (currentTrick.getCards().size() != 0 && currentTrick.getCards().size() != 4 && move.getSuit().equals(currentTrick.leadSuit()) == false && getPlayerToPlay().getHand().isVoid(currentTrick.leadSuit()) == false){
			view.reportToUser("You must follow suit.");
			return false; // Must follow suit, since don't have void in lead suit
		}
		if (isOnFirstPlay && isOnFirstLead != true) {
			if (move.getSuit().equals(Card.HEARTS) || move.isEqualTo(Card.QUEEN_OF_SPADES)) {
				if (getPlayerToPlay().getHand().allPenaltyCards() == false) {
					view.reportToUser("You cannot play hearts or the queen of spades on the first trick.");
					return false; // Can't play hearts or queen of spades on first trick unless all penalty cards
				}
			}
		}
		if ((currentTrick.getCards().size() == 0  || currentTrick.getCards().size() == 4)  && move.getSuit().equals(Card.HEARTS)) {
			if (heartsBroken == false && getPlayerToPlay().getHand().allHearts() == false) {
				view.reportToUser("Hearts are not yet broken. Try another card.");
				return false; // Hearts not yet broken, and don't have all hearts
			}
		}
		return true;
	}
	
	public boolean isValidMoveSilent(Card move) {
		if (!getPlayerToPlay().getHand().containsCard(move)) {
			return false; // Don't own card
		}
		if (isOnFirstLead && move.isEqualTo(Card.TWO_OF_CLUBS) != true) {
			return false; // Must play two of clubs
		}
		if (currentTrick.getCards().size() != 0 && currentTrick.getCards().size() != 4 && move.getSuit().equals(currentTrick.leadSuit()) == false && getPlayerToPlay().getHand().isVoid(currentTrick.leadSuit()) == false){
			return false; // Must follow suit, since don't have void in lead suit
		}
		if (isOnFirstPlay && isOnFirstLead != true) {
			if (move.getSuit().equals(Card.HEARTS) || move.isEqualTo(Card.QUEEN_OF_SPADES)) {
				if (getPlayerToPlay().getHand().allPenaltyCards() == false) {
					return false; // Can't play hearts or queen of spades on first trick unless all penalty cards
				}
			}
		}
		if ((currentTrick.getCards().size() == 0  || currentTrick.getCards().size() == 4)  && move.getSuit().equals(Card.HEARTS)) {
			if (heartsBroken == false && getPlayerToPlay().getHand().allHearts() == false) {
				return false; // Hearts not yet broken, and don't have all hearts
			}
		}
		return true;
	}
	
	public static int getNextPlayer(int player) {
		if (player == 3)
			return 0;
		else
			return player + 1;
	}
	
	public Trick getCurrentTrick() {
		return currentTrick;
	}
}