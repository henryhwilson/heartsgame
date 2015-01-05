public interface Strategy {
	public Card chooseMove(HeartsGame game);
	public double evaluate(HeartsGame game);
}