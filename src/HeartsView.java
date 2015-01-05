public interface HeartsView {
	public void display(HeartsGame game);
	public Card getUserPlay(HeartsGame game);
	public void reportPlay(Card chosenPlay, String name);
	public void reportToUser(String message);
	public String getAnswer(String question);
	public int getIntAnswer(String question);
}