import java.util.ArrayList;

public class Trick extends CardCollection {
	private int playerOnLead;
	
	public Trick(int playerOnLead) {
		super();
		this.playerOnLead = playerOnLead;
	}
	
	public void addCard(Card c) {
		if (cards.size() >= 4) {
			return;
		}
		cards.add(c);
	}
	
	public String leadSuit() {
		if (cards.size() > 0) {
			return cards.get(0).getSuit();
		} else {
			return "";
		}
	}
	
	public int getPlayerWon() {
		int playerWon = playerOnLead;
		int playerIterator = playerOnLead;
		int highestNum = 0;
		for (Card c : cards) {
			if (c.getValue() > highestNum) {
				if (c.getSuit().equals(leadSuit())) {
					highestNum = c.getValue();
					playerWon = playerIterator;
				}
			}
			playerIterator = HeartsGame.getNextPlayer(playerIterator);
		}
		return playerWon;
	}
	
	public Card getCardWonSoFar() {
		int playerIterator = playerOnLead;
		int highestNum = 0;
		Card best = null;
		for (Card c : cards) {
			if (c.getValue() > highestNum) {
				if (c.getSuit().equals(leadSuit())) {
					highestNum = c.getValue();
					best = c.copyCard();
				}
			}
			playerIterator = HeartsGame.getNextPlayer(playerIterator);
		}
		return best;
	}
	
	public int getPlayerLead() {
		return playerOnLead;
	}
	
	public boolean leadSuitIsntBlank() {
		return leadSuit() != "" && cards.size() < 4;
	}
	
	public Card getPlayerCard(int player) {
		if (player == playerOnLead) {
			if (cards.size() > 0)
				return cards.get(0);
		} else if (player == HeartsGame.getNextPlayer(playerOnLead)) {
			if (cards.size() > 1)
				return cards.get(1);
		} else if (player == HeartsGame.getNextPlayer(HeartsGame.getNextPlayer(playerOnLead))) {
			if (cards.size() > 2)
				return cards.get(2);
		} else if (player == HeartsGame.getNextPlayer(HeartsGame.getNextPlayer(HeartsGame.getNextPlayer(playerOnLead)))) {
			if (cards.size() > 3)
				return cards.get(3);
		}
		return null;
	}

	public boolean containsCard(String suit, int number) {
		for (Card card : cards) {
			if (card.getSuit().equals(suit) && card.getNumber() == number) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsCard(Card card) {
		for (Card c : cards) {
			if (card.isEqualTo(c)) {
				return true;
			}
		}
		return false;
	}

	public Trick copyTrick() {
		ArrayList<Card> copyCards = new ArrayList<Card>();
		for (Card card : cards) {
			copyCards.add(card.copyCard());
		}
		Trick copyTrick = new Trick(playerOnLead);
		copyTrick.cards = copyCards;
		return copyTrick;
	}
	
	public String toString() {
		String returnString = "";
		for (Card card : cards) {
			returnString += card.toStringShort() + " ";
		}
		return returnString;
	}
}