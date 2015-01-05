import java.util.Scanner;

public class HeartsViewTextual implements HeartsView {
	
	Scanner s = new Scanner(System.in);

	public void display(HeartsGame game) {
		Player player = game.getPlayerToPlay();
		System.out.println();
		System.out.println(player.getName()+"'s turn");
		for (String suit : Card.SUITS) { 
			String suitDisplay = suit.substring(0,1).toUpperCase() + suit.substring(1) + "s:";
			System.out.print(suitDisplay);
			for (Card card : player.getHand().getCards()) {
				if (card.getSuit().equals(suit)) {
					System.out.print(" " + card.getStringOfSuit() + card.getStringOfNumber());
				}
			}
			System.out.println();
		}
	}

	public Card getUserPlay(HeartsGame game) {
		Card card;
		do {
			System.out.print(game.getPlayerToPlay().getName() + ", enter a card to play: ");
			String input = s.nextLine();
			String suit = input.substring(0,1);
			if (suit.equals("s"))
				suit = "spade";
			else if (suit.equals("h"))
				suit = "heart";
			else if (suit.equals("d"))
				suit = "diamond";
			else if (suit.equals("c"))
				suit = "club";
			int number = Integer.parseInt(input.substring(1));
			card = new Card(suit,number);
		} while (!game.isValidMove(card)); // NOTE: THIS CHECKS FOR BOTH BOUNDARY ERRORS AND PLACEMENT ERRORS		
		return card;
	}

	public void reportPlay(Card chosenPlay, String name) {
		System.out.println(name + " played the " + chosenPlay);
	}

	public void reportToUser(String message) {
		System.out.println(message);
	}

	public String getAnswer(String question) {
		System.out.print(question);
		return s.nextLine();
	}

	public int getIntAnswer(String question) {
		System.out.print(question);
		return s.nextInt();
	}
	
}