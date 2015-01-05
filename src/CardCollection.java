import java.util.ArrayList;

public class CardCollection {
	protected ArrayList<Card> cards;
	
	public CardCollection() {
		cards = new ArrayList<Card>();
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public boolean isEmpty() {
		if (cards.size() == 0)
			return true;
		else
			return false;
	}
	
	public int getPoints() {
		int total = 0;
		for (Card card : cards) {
			total += card.getPoints();
		}
		return total;
	}
	
	public boolean containsPenaltyCards() {
		for (Card card : cards) {
			if (card.getPoints() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsCard(Card card) {
		for (Card c : cards) {
			if (card.isEqualTo(c)) {
				return true;
			} else {
//				System.out.println(c + " isn't equal to " + card);
			}
		}
		return false;
	}
	
	public boolean containsCard(String suit, int number) {
		for (Card card : cards) {
			if (card.getSuit().equals(suit) && card.getNumber() == number) {
				return true;
			}
		}
		return false;
	}
	
	public void removeCard(Card card) {
		for (int i=0; i<cards.size(); i++) {
			if (card.isEqualTo(cards.get(i))) {
				cards.remove(i);
				return;
			}
		}
	}
}