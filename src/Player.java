import java.util.ArrayList;

public abstract class Player {
	private Hand hand;
	private String name;
	private ArrayList<Trick> tricksWon;
	
	public Player(String name) {
		hand = new Hand();
		tricksWon = new ArrayList<Trick>();
		this.name = name;
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String str) {
		name = str;
	}
	
	public ArrayList<Trick> getTricksWon() {
		return tricksWon;
	}
	
	public void addTrick(Trick t) {
		tricksWon.add(t);
	}
	
	public Card getMove(HeartsGame game, HeartsView view) {
		return view.getUserPlay(game);
	}
	
	public boolean shotTheMoon(HeartsGame game) {
		for (Player player : game.getPlayers()) {
			if (player == this) {
				continue;
			} else {
				for (Trick t : player.getTricksWon()) {
					for (Card c : t.getCards()) {
						if (c.getSuit().equals(Card.HEARTS)) {
							return false;
						}
					}
				}
			}
		}
		System.out.println(getName() + " shot the moon");
		return true;
	}
	
	public boolean shotTheSun(HeartsGame game) {
		for (Player player : game.getPlayers()) {
			if (player == this) {
				continue;
			} else {
				for (Trick t : player.getTricksWon()) {
					for (Card c : t.getCards()) {
						if (c.getSuit().equals(Card.HEARTS) || c.isEqualTo(Card.QUEEN_OF_SPADES)) {
							return false;
						}
					}
				}
			}
		}
		System.out.println(getName() + " shot the sun");
		return true;
	}
	
	public boolean canShootMoon(HeartsGame game) {
		for (Player player : game.getPlayers()) {
			if (player == this) {
				continue;
			} else {
				for (Trick t : player.getTricksWon()) {
					for (Card c : t.getCards()) {
						if (c.getSuit().equals(Card.HEARTS)) {
							return false;
						}
					}
				}
			}
		}
		if (game.getCurrentTrick().getCards().size() != 4) {
			boolean isHeart = false;
			for (Card c : game.getCurrentTrick().getCards()) {
				if (c.getSuit().equals(Card.HEARTS)) {
					isHeart = true;
					break;
				}
			}
			if (isHeart) {
				boolean canBeat = false;
				Card toBeat = game.getCurrentTrick().getCardWonSoFar();
				for (Card c : this.getHand().getCards()) {
					if (c.getSuit().equals(c.getSuit()) && c.getValue() > toBeat.getValue()) {
						canBeat = true;
						break;
					}
				}
				if (!canBeat) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean canShootSun(HeartsGame game) {
		for (Player player : game.getPlayers()) {
			if (player == this) {
				continue;
			} else {
				for (Trick t : player.getTricksWon()) {
					for (Card c : t.getCards()) {
						if (c.getSuit().equals(Card.HEARTS) || c.isEqualTo(Card.QUEEN_OF_SPADES)) {
							return false;
						}
					}
				}
			}
		}
		if (game.getCurrentTrick().getCards().size() != 4) {
			boolean isHeart = false;
			for (Card c : game.getCurrentTrick().getCards()) {
				if (c.getSuit().equals(Card.HEARTS) || c.equals(Card.QUEEN_OF_SPADES)) {
					isHeart = true;
					break;
				}
			}
			if (isHeart) {
				boolean canBeat = false;
				Card toBeat = game.getCurrentTrick().getCardWonSoFar();
				for (Card c : this.getHand().getCards()) {
					if (c.getSuit().equals(c.getSuit()) && c.getValue() > toBeat.getValue()) {
						canBeat = true;
						break;
					}
				}
				if (!canBeat) {
					return false;
				}
			}
		}
		return true;
	}
}