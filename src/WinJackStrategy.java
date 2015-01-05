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
public class WinJackStrategy implements Strategy {
	private double[] weights = null;
	private static double[] weightsDefault = {50,3,4,50,100,50,100,40,2,2,5,50,2,50,20,25,30,100,50,50,1,20,25,30,1,20,1,1,50,20,1,50,50,100,100,100,30,20,25,30,10};
	
	public WinJackStrategy(double[] weights) {
		if (weights == null)
			this.weights = weightsDefault;
		else
			this.weights = weights;
	}
	@Override
	public Card chooseMove(HeartsGame game) {
		Player me = game.getPlayerToPlay();
		Cards moves = AIPlayer.getMoves(me.getHand(),game);
		if (moves.isEmpty())
			return null;
		Cards discards = AIPlayer.getAllDiscards(game);
		int bestMoveIndex = 0;
		double bestScore = -99999.0;
		double currentScore = 0;
		for (int i=0; i<moves.getCards().size(); i++) { 	// If on lead, first trick, won't go here
			currentScore = 0;
			Card c = moves.getCards().get(i);
			if (game.getCurrentTrick().getCards().size() == 4) { // On lead, not first trick
				//System.out.println("On lead, not first trick");
				if (me.getHand().containsCard(Card.QUEEN_OF_SPADES) == false // If I don't have Q_OF_S, lead a low spade
					&& discards.containsCard(Card.QUEEN_OF_SPADES) == false
					&& c.getSuit().equals(Card.SPADES)
					&& c.getValue() <= 11) {
					int otherSpades = AIPlayer.getOpponentCardsOfSameSuit(c,game).getCards().size();
					currentScore += weights[0];
					currentScore += (11-c.getValue())*weights[1];
					currentScore -= otherSpades*weights[2];
				} else if (me.getHand().containsCard(Card.QUEEN_OF_SPADES) && c.getSuit().equals(Card.SPADES)) {
					currentScore -= weights[3]; // If we have the Q_OF_S, don't play a spade
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						if (AIPlayer.getOpponentCardsOfSameSuit(Card.QUEEN_OF_SPADES,game).getCards().size() == 1
								&& AIPlayer.getOpponentCardsAbove(Card.QUEEN_OF_SPADES,game).getCards().size() == 1) {
							currentScore += weights[4]; // If only spade left is A or K of spades, play it
						} else {
							currentScore -= weights[5];
						}
					}
				}
				int cardsBelow = AIPlayer.getOpponentCardsBelow(c,game).getCards().size();
				int cardsAbove = AIPlayer.getOpponentCardsAbove(c,game).getCards().size();
				if (cardsAbove==0 && c.isEqualTo(Card.JACK_OF_DIAMONDS)) {
					currentScore += weights[6];
				}
				else if (cardsAbove!=0 && c.isEqualTo(Card.JACK_OF_DIAMONDS)) { 
					currentScore -= weights[7];
				}
				currentScore -= cardsBelow*weights[8];
				currentScore += cardsAbove*weights[9];
				//System.out.println("Cards above " + c.toStringShort() + " = " + cardsAbove);
				//System.out.println("Cards below " + c.toStringShort() + " = " + cardsBelow);
				if (c.getSuit().equals(Card.HEARTS)) {
					if (cardsBelow <= 2 && cardsAbove >= 3) {
						currentScore += weights[10]; // Lead a low heart if it makes sense to
					}
				}
			} else if (discards.getCards().size() == 0) { // Not on lead, first trick
				//System.out.println("Not on lead, first trick");
				currentScore += weights[11];
				currentScore += c.getValue()*weights[12];
				if (c.isEqualTo(Card.JACK_OF_DIAMONDS)) {
					currentScore -= weights[13];
				} else if (c.isEqualTo(Card.QUEEN_OF_DIAMONDS)) {
					currentScore -= weights[14];
				} else if (c.isEqualTo(Card.KING_OF_DIAMONDS)) {
					currentScore -= weights[15];
				} else if (c.isEqualTo(Card.ACE_OF_DIAMONDS)) {
					currentScore -= weights[16];
				}
			} else { // Not on lead, not first trick
				//System.out.println("Not on lead, not first trick");
				if (game.getCurrentTrick().leadSuit() != ""  // If we have a void in the lead suit
						&& game.getCurrentTrick().leadSuit() != c.getSuit()) {
					//System.out.println("Void in the lead suit");
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						currentScore += weights[17];
					} else if (c.getSuit().equals(Card.HEARTS)) {
						currentScore += weights[18] + c.getValue()*weights[20];
					} else if (c.isEqualTo(Card.JACK_OF_DIAMONDS)) {
						currentScore -= weights[19];
					} else if (c.isEqualTo(Card.QUEEN_OF_DIAMONDS)) {
						currentScore -= weights[21];
					} else if (c.isEqualTo(Card.KING_OF_DIAMONDS)) {
						currentScore -= weights[22];
					} else if (c.isEqualTo(Card.ACE_OF_DIAMONDS)) {
						currentScore -= weights[23];
					} else {
						int cardsBelow = AIPlayer.getOpponentCardsBelow(c,game).getCards().size();
						currentScore += cardsBelow * weights[24];
					}
				} else {
					Card wonSoFar = game.getCurrentTrick().getCardWonSoFar();
					if (c.getValue() > wonSoFar.getValue()) {
						currentScore -= weights[25];
						if (game.getCurrentTrick().getCards().size() == 3) {
							currentScore += c.getValue() * weights[26];
						} else {
							currentScore -= c.getValue() * weights[27];
						}
						if (game.getCurrentTrick().containsCard(Card.JACK_OF_DIAMONDS)) {
							currentScore += weights[28];
						}
					} else if (c.getValue() < wonSoFar.getValue()) {
						currentScore += weights[29];
						currentScore += c.getValue() * weights[30];
						if (game.getCurrentTrick().containsCard(Card.JACK_OF_DIAMONDS)) {
							currentScore -= weights[31];
						}
					}
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						currentScore -= weights[32];
						if (game.getCurrentTrick().containsCard(Card.ACE_OF_SPADES) 
								|| game.getCurrentTrick().containsCard(Card.KING_OF_SPADES)) {
							currentScore += weights[33];
						}
					} else if (c.isEqualTo(Card.JACK_OF_DIAMONDS)) {
						if (AIPlayer.getOpponentCardsAboveNotIncludingCurrentTrick(c,game).isEmpty()) {
							currentScore += weights[34];
						} else if (game.getCurrentTrick().getCards().size() == 3
								&& game.getCurrentTrick().getCardWonSoFar().getValue() <= 10) { 		
							currentScore += weights[35];
						} else {
							currentScore -= weights[36];
						}
					} else if (c.isEqualTo(Card.QUEEN_OF_DIAMONDS)) {
						currentScore -= weights[37];
					} else if (c.isEqualTo(Card.KING_OF_DIAMONDS)) {
						currentScore -= weights[38];
					} else if (c.isEqualTo(Card.ACE_OF_DIAMONDS)) {
						currentScore -= weights[39];
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
		Cards discards = AIPlayer.getAllDiscards(game);
		if (discards.containsCard(Card.JACK_OF_DIAMONDS)) {
			return 0.0;
		} else {
			return weights[40];
		}
	}
	
}