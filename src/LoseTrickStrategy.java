/**
 * LoseTrickStrategy
 * This strategy is based on losing the queen of spades & tricks with hearts in them
 * Substrategies include:
 * 	- playing low spades to avoid getting queen discard (if don't have queen)
 *  - leading a low heart if possible
 *  - discarding hearts & queen of spades if can't win trick
 *  - ducking tricks that contain hearts (if possible)
 *  
 * @author hwilson
 *
 */
public class LoseTrickStrategy implements Strategy {
	private double[] weights;
	private static double[] weightsDefault = {50,3,50,100,50,2,2,5,50,2,100,50,1,20,1,1,20,1,50,100,2,5};
	
	public LoseTrickStrategy(double[] weights) {
		if (weights == null)
			this.weights = weightsDefault;
		else
			this.weights = weights;
	}

	@Override
	public Card chooseMove(HeartsGame game) {
		Player me = game.getPlayerToPlay();
		Cards moves = AIPlayer.getMoves(me.getHand(),game);
		if (moves.isEmpty()) {
			return null;
		}
		Cards discards = AIPlayer.getAllDiscards(game);
		int bestMoveIndex = 0;
		double bestScore = -99999.0, currentScore = 0.0;
		for (int i=0; i<moves.getCards().size(); i++) { 	// If on lead, first trick, won't go here
			currentScore = 0.0;
			Card c = moves.getCards().get(i);
			if (game.getCurrentTrick().getCards().size() == 4) { // On lead, not first trick
				//System.out.println("On lead, not first trick");
				if (me.getHand().containsCard(Card.QUEEN_OF_SPADES) == false // If I don't have Q_OF_S, lead a low spade
					&& discards.containsCard(Card.QUEEN_OF_SPADES) == false
					&& c.getSuit().equals(Card.SPADES)
					&& c.getValue() <= 11) {
					int otherSpades = AIPlayer.getOpponentCardsOfSameSuit(c,game).getCards().size();
					currentScore += weights[0];
					currentScore += (11-c.getValue())*weights[20];
					currentScore -= otherSpades*weights[1];
				} else if (me.getHand().containsCard(Card.QUEEN_OF_SPADES) && c.getSuit().equals(Card.SPADES)) {
					currentScore -= weights[2]; // If we have the Q_OF_S, don't play a spade
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						if (AIPlayer.getOpponentCardsOfSameSuit(Card.QUEEN_OF_SPADES,game).getCards().size() == 1
								&& AIPlayer.getOpponentCardsAbove(Card.QUEEN_OF_SPADES,game).getCards().size() == 1) {
							currentScore += weights[3]; // If only spade left is A or K of spades, play it
						} else {
							currentScore -= weights[4];
						}
					}
				}
				int cardsBelow = AIPlayer.getOpponentCardsBelow(c,game).getCards().size();
				int cardsAbove = AIPlayer.getOpponentCardsAbove(c,game).getCards().size();
				currentScore -= cardsBelow*weights[5];
				currentScore += cardsAbove*weights[6];
				//System.out.println("Cards above " + c.toStringShort() + " = " + cardsAbove);
				//System.out.println("Cards below " + c.toStringShort() + " = " + cardsBelow);
				if (c.getSuit().equals(Card.HEARTS)) {
					if (cardsBelow <= 2 && cardsAbove >= 3) {
						currentScore += weights[7]; // Lead a low heart if it makes sense to
					}
				}
			} else if (discards.getCards().size() == 0) { // Not on lead, first trick
				//System.out.println("Not on lead, first trick");
				currentScore += weights[8];
				currentScore += c.getValue()*weights[9];
			} else { // Not on lead, not first trick
				//System.out.println("Not on lead, not first trick");
				if (game.getCurrentTrick().leadSuit() != ""  // If we have a void in the lead suit
						&& game.getCurrentTrick().leadSuit() != c.getSuit()) {
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						currentScore += weights[10];
					} else if (c.getSuit().equals(Card.HEARTS)) {
						currentScore += weights[11] + c.getValue();
					} else {
						int cardsBelow = AIPlayer.getOpponentCardsBelow(c,game).getCards().size();
						currentScore += cardsBelow * weights[12];
					}
				} else {
					Card wonSoFar = game.getCurrentTrick().getCardWonSoFar();
					if (c.getValue() > wonSoFar.getValue()) {
						currentScore -= weights[13];
						if (game.getCurrentTrick().getCards().size() == 3) {
							currentScore += c.getValue() * weights[14];
						} else {
							currentScore -= c.getValue() * weights[15];
						}
					} else if (c.getValue() < wonSoFar.getValue()) {
						currentScore += weights[16];
						currentScore += c.getValue() * weights[17];
					}
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						currentScore -= weights[18];
						if (game.getCurrentTrick().containsCard(Card.ACE_OF_SPADES) 
								|| game.getCurrentTrick().containsCard(Card.KING_OF_SPADES)) {
							currentScore += weights[19];
						}
					}
				}
			}
			//System.out.println("Evaluation for " + c.toStringShort() + " = "+currentScore);
			if (currentScore > bestScore) {
				bestMoveIndex = i;
				bestScore = currentScore;
			}
		}
		return moves.getCards().get(bestMoveIndex);
	}

	@Override
	public double evaluate(HeartsGame game) {
		return weights[21];
	}
	
}