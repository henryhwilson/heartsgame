import java.util.ArrayList;

public class Hand extends CardCollection {
	public static final int PADDING = 17;
	
	public Hand() {
		super();
	}
	
	public void addCard(Card c) {
		cards.add(c);
	}
	
	public boolean allPenaltyCards() {
		for (Card card : cards) {
			if (card.getSuit().equals(Card.HEARTS) == false) {
				if (!card.isEqualTo(Card.QUEEN_OF_SPADES))
					return false;
			}
		}
		return true;
	}
	
	public boolean allHearts() {
		for (Card card : cards) {
			if (card.getSuit().equals(Card.HEARTS) == false) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isVoid(String suit) {
		for (Card c : cards) {
			if (c.getSuit().equals(suit)) {
				return false;
			}
		}
		return true;
	}
	
	public void sortBySuitAscending() {
		ArrayList<Card> newCards = new ArrayList<Card>();
		for (String suit : Card.SUITS) {
			for (int number=2; number<=14; number++) {
				if (number == 14)
					number = 1;
				for (int i=0; i<cards.size(); i++) {
					Card c = cards.get(i);
					if (c.getNumber() == number && c.getSuit().equals(suit)) {
						cards.remove(i);
						newCards.add(c);
					}
				}
				if (number == 1)
					number = 14;
			}
		}
		cards = newCards;
	}
	
	public void sortBySuitAlternating() {
		ArrayList<Card> newCards = new ArrayList<Card>();
		String[] suitsAlternating = {Card.CLUBS,Card.DIAMONDS,Card.SPADES,Card.HEARTS};
		for (String suit : suitsAlternating) {
			for (int number=2; number<=14; number++) {
				if (number == 14)
					number = 1;
				for (int i=0; i<cards.size(); i++) {
					Card c = cards.get(i);
					if (c.getNumber() == number && c.getSuit().equals(suit)) {
						cards.remove(i);
						newCards.add(c);
					}
				}
				if (number == 1)
					number = 14;
			}
		}
		cards = newCards;
	}
}