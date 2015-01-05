/**
 * ShootSunStrategy
 * This strategy tries to shoot the sun by winning all hearts first, then queen of spades
 *  
 * @author hwilson
 *
 */
public class ShootMoonStrategy implements Strategy {
	private double[] weights = null;
	private static double[] weightsDefault = {40,10,2,2,50,2,80,100,1,20,1,1,20,1,14.0};
	
	public ShootMoonStrategy(double[] weights) {
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
		double bestScore = -99999, currentScore = 0;
		for (int i=0; i<moves.getCards().size(); i++) { 	// If on lead, first trick, won't go here
			currentScore = 0.0;
			Card c = moves.getCards().get(i);
			if (game.getCurrentTrick().getCards().size() == 4) { // On lead, not first trick
				//System.out.println("On lead, not first trick");
				int cardsBelow = AIPlayer.getOpponentCardsBelow(c,game).getCards().size();
				int cardsAbove = AIPlayer.getOpponentCardsAbove(c,game).getCards().size();
				if (cardsAbove==0) {
					currentScore += weights[0];
					if (c.getSuit().equals(Card.HEARTS)){ 
						currentScore += weights[1];
					}
				}
				currentScore += cardsBelow*weights[2];
				currentScore -= cardsAbove*weights[3];
				//System.out.println("Cards above " + c.toStringShort() + " = " + cardsAbove);
				//System.out.println("Cards below " + c.toStringShort() + " = " + cardsBelow);
			} else if (discards.getCards().size() == 0) { // Not on lead, first trick
				//System.out.println("Not on lead, first trick");
				currentScore += weights[4];
				currentScore -= c.getValue()*weights[5];
			} else { // Not on lead, not first trick
				//System.out.println("Not on lead, not first trick");
				if (game.getCurrentTrick().leadSuit() != ""  // If we have a void in the lead suit
						&& game.getCurrentTrick().leadSuit() != c.getSuit()) {
					//System.out.println("Void in the lead suit");
					if (c.isEqualTo(Card.QUEEN_OF_SPADES)) {
						currentScore -= weights[6];
					} else if (c.getSuit().equals(Card.HEARTS)) {
						currentScore -= weights[7];
					} 

					int cardsAbove = AIPlayer.getOpponentCardsAbove(c,game).getCards().size();
					currentScore += cardsAbove * weights[8];

				} else {
					Card wonSoFar = game.getCurrentTrick().getCardWonSoFar();
					if (c.getValue() > wonSoFar.getValue()) {
						currentScore += weights[9];
						if (game.getCurrentTrick().getCards().size() == 3) {
							currentScore -= c.getValue() * weights[10];
						} else {
							currentScore += c.getValue() * weights[11];
						}
					} else if (c.getValue() < wonSoFar.getValue()) {
						currentScore -= weights[12];
						currentScore -= c.getValue() * weights[13];
					}
				}
			}
			if (currentScore > bestScore) {
				bestMoveIndex = i;
				bestScore = currentScore;
			}
		}
		return moves.getCards().get(bestMoveIndex);
	}

	@Override
	public double evaluate(HeartsGame game) {
		int totalCardsBelow=0, totalCardsAbove=0;
		for (Card c : game.getPlayerToPlay().getHand().getCards()) {
			totalCardsBelow += AIPlayer.getOpponentCardsBelow(c,game).getCards().size();
			totalCardsAbove += AIPlayer.getOpponentCardsAbove(c,game).getCards().size();
		}
		if (game.getPlayerToPlay().canShootMoon(game)) {
			double differential = (1.0*totalCardsBelow - 1.0*totalCardsAbove)/(totalCardsBelow * 1.0);
			//System.out.println("Moon: " + differential*16.0);
			return differential * weights[14];
		} else {
			return 0.0;
		}
	}
	
}