import java.util.Random;

public class AIPlayer extends Player {
	boolean random = false, delay = false;
	double[][] weights = null;
	public AIPlayer(String name) {
		super(name);
		weights = new double[4][];
		weights[0] = null;
		weights[1] = null;
		weights[2] = null;
		weights[3] = null;
	}
	
	public AIPlayer(String name, boolean random) {
		super(name);
		this.random = random;
		weights = new double[4][];
		weights[0] = null;
		weights[1] = null;
		weights[2] = null;
		weights[3] = null;
	}
	
	public AIPlayer(String name, boolean random, boolean delay) {
		super(name);
		this.random = random;
		this.delay = delay;
		weights = new double[4][];
		weights[0] = null;
		weights[1] = null;
		weights[2] = null;
		weights[3] = null;
	}
	
	public AIPlayer(String name, boolean random, double[][] weights) {
		this(name,random);
		this.weights = weights;
	}
	
	public boolean isRandom() {
		return random;
	}
	
	public boolean delay() {
		return delay;
	}
	
	public void isDelay(boolean delay) {
		this.delay = delay;
	}
	
	@Override
	public Card getMove(HeartsGame game, HeartsView view) {
		if (random)
			return getRandomMove(game);
		Strategy strategy;
		Strategy loseTrickStrategy = new LoseTrickStrategy(weights[0]);
		Strategy winJackStrategy = new WinJackStrategy(weights[1]);
		Strategy shootMoonStrategy = new ShootMoonStrategy(weights[2]);
		Strategy shootSunStrategy = new ShootSunStrategy(weights[3]);
		double loseTrick = loseTrickStrategy.evaluate(game);
		double winJack = winJackStrategy.evaluate(game);
		double shootMoon = shootMoonStrategy.evaluate(game);
		double shootSun = shootSunStrategy.evaluate(game);
		if (loseTrick > winJack && loseTrick > shootMoon && loseTrick > shootSun) {
			//System.out.println("Lose trick strategy");
			strategy = loseTrickStrategy;
		} else if (winJack > loseTrick && winJack > shootMoon && winJack > shootSun) {
			//System.out.println("Win jack strategy");
			strategy = winJackStrategy;
		} else if (shootMoon > loseTrick && shootMoon > winJack && shootMoon > shootSun) {
			//System.out.println("Shoot moon strategy");
			strategy = loseTrickStrategy;
		} else {
			//System.out.println("Shoot sun strategy");
			strategy = shootSunStrategy;
		}
		wait(game);
		return strategy.chooseMove(game);
	}
	
	public Card getRandomMove(HeartsGame game) {
		Random rand = new Random();
		Cards moves = getMoves(getHand(),game);
		if (moves.isEmpty()) {
			return null;
		}
		int move = rand.nextInt(moves.getCards().size());
		wait(game);
		return moves.getCards().get(move);
	}
	
	public void wait(HeartsGame game) {
		if (delay == false)
			return;
		int time = 1500;
		if (game.getCurrentTrick().getCards().size() == 4)
			time += 2500;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Cards getMoves(CardCollection cards, HeartsGame game) {
		Cards moves = new Cards();
		for (Card card : cards.getCards()) {
			if (game.isValidMoveSilent(card)) {
				moves.addCard(card);
			}
		}
		return moves;
	}
	
	public static Cards getAllDiscards(HeartsGame game) {
		Cards cards = new Cards();
		for (int i=0; i<game.getPlayers().length; i++) {
			for (Trick t : game.getPlayer(i).getTricksWon()) {
				for (Card c : t.getCards()) {
					cards.addCard(c);
				}
			}
		}
		return cards;
	}
	
	public static Cards getOpponentCardsAbove(Card card, HeartsGame game) {
		Cards discards = getAllDiscards(game);
		Hand myCards = game.getPlayerToPlay().getHand();
		Trick t = game.getCurrentTrick();
		Cards cardsAbove = new Cards();
		for (int number=card.getNumber(); number<=14; number++) {
			if (number==14)
				number = 1;
			
			cardsAbove.addCard(new Card(card.getSuit(),number));
			
			if (number==1)
				number=14;
		}
		cardsAbove.removeCard(card);
		for (Card c : discards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() > card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		for (Card c : t.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() > card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		for (Card c : myCards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() > card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		return cardsAbove;
	}
	
	public static Cards getOpponentCardsBelow(Card card, HeartsGame game) {
		Cards discards = getAllDiscards(game);
		Hand myCards = game.getPlayerToPlay().getHand();
		Trick t = game.getCurrentTrick();
		Cards cardsAbove = new Cards();
		for (int number=card.getValue()-1; number>=2; number--) {
			cardsAbove.addCard(new Card(card.getSuit(),number));
		}
		cardsAbove.removeCard(card);
		for (Card c : discards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() < card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		for (Card c : t.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() < card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		for (Card c : myCards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() < card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		return cardsAbove;
	}
	
	public static Cards getOpponentCardsOfSameSuit(Card card, HeartsGame game) {
		Cards discards = getAllDiscards(game);
		Hand myCards = game.getPlayerToPlay().getHand();
		Trick t = game.getCurrentTrick();
		Cards cardsOfSameSuit = new Cards();
		for (int i=1; i<=13; i++) {
			cardsOfSameSuit.addCard(new Card(card.getSuit(),i));
		}
		cardsOfSameSuit.removeCard(card);
		for (Card c : discards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				cardsOfSameSuit.removeCard(c);
			}
		}
		for (Card c : t.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				cardsOfSameSuit.removeCard(c);
			}
		}
		for (Card c : myCards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				cardsOfSameSuit.removeCard(c);
			}
		}
		return cardsOfSameSuit;
	}
	
	public static Cards getOpponentCardsAboveNotIncludingCurrentTrick(Card card, HeartsGame game) {
		Cards discards = getAllDiscards(game);
		Hand myCards = game.getPlayerToPlay().getHand();
		Cards cardsAbove = new Cards();
		for (int number=card.getNumber(); number<=14; number++) {
			if (number==14)
				number = 1;
			
			cardsAbove.addCard(new Card(card.getSuit(),number));
			
			if (number==1)
				number=14;
		}
		cardsAbove.removeCard(card);
		for (Card c : discards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() > card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		for (Card c : myCards.getCards()) {
			if (c.getSuit().equals(card.getSuit())) {
				if (c.getValue() > card.getValue()) {
					cardsAbove.removeCard(c);
				}
			}
		}
		return cardsAbove;
	}
}