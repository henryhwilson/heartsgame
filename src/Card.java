import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Card {
	private int number;
	private String suit;
	public static final String[] SUITS = {"spade","heart","diamond","club"};
	public static final Card TWO_OF_CLUBS = new Card(SUITS[3],2);
	public static final Card QUEEN_OF_SPADES = new Card(SUITS[0],12);
	public static final Card JACK_OF_DIAMONDS = new Card(SUITS[2],11);
	public static final Card ACE_OF_SPADES = new Card(SUITS[0],1);
	public static final Card KING_OF_SPADES = new Card(SUITS[0],13);
	public static final Card ACE_OF_DIAMONDS = new Card(SUITS[2],1);
	public static final Card KING_OF_DIAMONDS = new Card(SUITS[2],13);
	public static final Card QUEEN_OF_DIAMONDS = new Card(SUITS[2],12);
	public static final String SPADES = "spade", HEARTS = "heart", DIAMONDS="diamond", CLUBS="club";
	public static final int WIDTH = 100, HEIGHT = 145;
	
	public Card(String suit, int number) throws IndexOutOfBoundsException {
		if (number >= 1 && number <= 13 && (suit.equals(SPADES) || suit.equals(HEARTS) || suit.equals(DIAMONDS) || suit.equals(CLUBS))) {
			this.number = number;
			this.suit = suit;
		} else {
			System.out.println("ERROR! SUIT: " + suit + " NUMBER: " + number + " CLUBS: "+ CLUBS);
			if (suit.equals(CLUBS)) {
				System.out.println("REally bad");
			}
			this.number = -1;
			this.suit = "null";
			throw new IndexOutOfBoundsException();
		}
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getValue() {
		if (number >= 2 && number <= 13)
			return number;
		else if (number == 1)
			return 14;
		else {
			System.err.print("Invalid card.");
			return 0;
		}
	}
	public String getSuit() {
		return suit;
	}
	
	public int getPoints() {
		if (number == 12 && suit.equals("spade"))
			return 13;
		else if (number == 11 && suit.equals("diamond"))
			return -10;
		else if (suit.equals("heart"))
			return 1;
		else
			return 0;
	}
	
	public String toString() {
		String card = "";
		if (number >= 2 && number <= 10)
			card = "" + number;
		else if (number == 1)
			card = "Ace";
		else if (number == 11)
			card = "Jack";
		else if (number == 12)
			card = "Queen";
		else if (number == 13)
			card = "King";
		else
			card = "Null";
		card += " of ";
		card += suit.substring(0,1).toUpperCase() + suit.substring(1) + "s";
		return card;
	}
	
	public String toStringForImage() {
		boolean classicWindows = false;
		
		if (!classicWindows) {
			String card = "";
			if (number >= 2 && number <= 10)
				card = "" + number;
			else if (number == 1)
				card = "ace";
			else if (number == 11)
				card = "jack";
			else if (number == 12)
				card = "queen";
			else if (number == 13)
				card = "king";
			else
				card = "null";
			card += "_of_";
			card += suit + "s";
			//		if (number >= 11 && number <= 13)
			//			card += "2";
			card += ".png";
			return card;
		} else {
			String card = suit.substring(0,1);
			if (number >= 1 && number <= 10) {
				card += number;
			} else if (number == 11) {
				card += "j";
			} else if (number == 12) {
				card += "q";
			} else if (number == 13) {
				card += "k";
			}
			return card + ".png";
		}
	}
	
	public String toStringShort() {
		return getStringOfSuit() + getStringOfNumber();
	}
	
	public String getStringOfNumber() {
		String card = "";
		if (number >= 2 && number <= 10)
			card = "" + number;
		else if (number == 1)
			card = "A";
		else if (number == 11)
			card = "J";
		else if (number == 12)
			card = "Q";
		else if (number == 13)
			card = "K";
		else
			card = "N";
		return card;
	}
	
	public String getStringOfSuit() {
		if (suit.equals("spade")) {
			return "\u2660";
		} else if (suit.equals("heart")) {
			return "\u2665";
		} else if (suit.equals("diamond")) {
			return "\u2666";
		} else if (suit.equals("club")) {
			return "\u2663";
		} else {
			return "Null";
		}
	}
	
	public boolean isEqualTo(Card other) {
		if (other.getNumber() == number && other.getSuit().equals(suit)) {
			return true;
		} else {
			return false;
		}
	}

	public Card copyCard() {
		return new Card(suit,number);
	}

	public void draw(Graphics page, int x, int y) {
		try {                
			BufferedImage image = ImageIO.read(new File("/Users/hwilson/Downloads/cards/"+toStringForImage()));
			page.drawImage(image, x, y, WIDTH, HEIGHT, null);
		} catch (IOException ex) {
			return;
		}
	}
}