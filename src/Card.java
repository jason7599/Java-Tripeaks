package triPeaks;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

enum CardSuit {SPADES,DIAMONDS,HEARTS,CLUBS};
enum CardRank {ACE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN,JACK,QUEEN,KING};

public class Card extends Rectangle{
	
	private static final long serialVersionUID = 1L;
	private static final int CARD_WIDTH=70;
	private static final int CARD_HEIGHT=100;
	
	private Image sprite;
	private CardSuit suit;
	private CardRank rank;
	private boolean facingUp;
	
	public Card(int x, int y, CardSuit suit, CardRank rank, boolean facingUp) {
		this.x=x;
		this.y=y;
		this.width=CARD_WIDTH;
		this.height=CARD_HEIGHT;
		this.suit=suit;
		this.rank=rank;
		this.facingUp=facingUp;
		updateSprite();
	}
	
	private void updateSprite() {
		try {
			sprite=ImageIO.read(new File(getImageDir()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,width,height);
	}
	
	public boolean isAvailable(Card otherCard) {
		return Math.abs((rank.ordinal()-otherCard.rank.ordinal())%11)==1;
	}
	
	public void flip() {
		facingUp=true;
		updateSprite();
	}
	
	public void setPos(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void draw(Graphics g, Component c) {
		g.drawImage(sprite, x, y, width, height, c);
	}
	
	public String toString() {
		return rank.toString()+" OF "+suit.toString();
	}
	
	private String getImageDir() {
		String dir="src/CardSprites/";
		if (!facingUp)
			return dir+"back-side.png";
		int rankNum=rank.ordinal();
		if (rankNum==0)
			dir+="ace";
		else if(rankNum==10)
			dir+="jack";
		else if(rankNum==11)
			dir+="queen";
		else if(rankNum==12)
			dir+="king";
		else
			dir+=rankNum+1;
		dir+="_of_"+suit.toString().toLowerCase()+".png";
		return dir;
	}

}
