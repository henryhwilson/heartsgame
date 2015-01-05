import java.util.Collections;
import java.util.Random;

public class Deck extends CardCollection {
	public Deck() {
		super();
		for (String suit : Card.SUITS) {
			for (int number=1; number<=13; number++) {
				cards.add(new Card(suit,number));
			}
		}
	}
	
	public void shuffle() {
		long seed = System.nanoTime();
		Collections.shuffle(cards, new Random(seed));
	}
	
	public Card getNextCard() {
		return cards.remove(0);
	}
	
	public boolean hasNextCard() {
		return !cards.isEmpty();
	}
}