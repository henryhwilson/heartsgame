import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class CardLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private static Map<String,Image> allImages;
	private Card card;
	private Image img;
	private static Image backImage;
	public boolean isShowing;

	public CardLabel(Card card, boolean isShowing) {
		super();
		this.card = card;
		this.img = allImages.get(card.toString());
		this.isShowing = isShowing;
	}
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		if (isShowing)
			page.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		else
			page.drawImage(backImage, 0, 0, getWidth(), getHeight(), null);
	}
	
	public Card getCard() {
		return card;
	}
	public void setCard(Card c) {
		card = c;
	}
	public static Image getImage(Card c) {
		BufferedImage img = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			img = ImageIO.read(classLoader.getResourceAsStream(c.toStringForImage()));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(Card.WIDTH,Card.HEIGHT,Image.SCALE_SMOOTH);
		return dimg;
	}
	public static void setAllImages() {
		allImages = new HashMap<String,Image>();
		for (int i=1; i<=13; i++) {
			for (String suit : Card.SUITS) {
				Card c = new Card(suit,i);
				BufferedImage img = null;
				try {
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					img = ImageIO.read(classLoader.getResourceAsStream(c.toStringForImage()));
				} catch (IOException e) {
				    e.printStackTrace();
				}
				Image dimg = img.getScaledInstance(Card.WIDTH,Card.HEIGHT,Image.SCALE_SMOOTH);
				allImages.put(c.toString(), dimg);
			}
		}
		BufferedImage img = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			img = ImageIO.read(classLoader.getResourceAsStream("b1fv.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		backImage = img.getScaledInstance(Card.WIDTH,Card.HEIGHT,Image.SCALE_SMOOTH);
	}
}