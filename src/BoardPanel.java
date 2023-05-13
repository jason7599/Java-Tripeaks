package triPeaks;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BoardPanel extends JPanel implements MouseListener{
   
   private static final long serialVersionUID = 1L;
   private ArrayList<Card> Deck = new ArrayList<>(52);
   private ArrayList<Card> TableCards=new ArrayList<>(28);
   private Card PlayerCard;
   private Set<Card> FacingUp = new HashSet<Card>();
   private int tableCardsLeft=28;
   private Boolean isPaused=false;
   
   private Image BackGround;
   
   public BoardPanel() {
      addMouseListener(this);
      setFocusable(true);
      try {
         BackGround=ImageIO.read(new File("src/Background.jpg"));
      } catch(IOException e) {
         e.printStackTrace();
      }
      reset();
   }
   
   
   private void reset() {
      Deck.removeAll(Deck);
      TableCards.removeAll(TableCards);
      FacingUp.clear();
      PlayerCard=null;
      tableCardsLeft=28;
      for(CardSuit suit : CardSuit.values())
         for(CardRank rank : CardRank.values())
            Deck.add(new Card(-100,-100,suit,rank,false));
      Collections.shuffle(Deck);
      for(int i=0; i<28;i++) {
         TableCards.add(Deck.remove(0));
         if(i<3)
            TableCards.get(i).setPos(255+210*i, 200);
         else if(i<5)
            TableCards.get(i).setPos(220+70*(i-3), 250);
         else if(i<7)
            TableCards.get(i).setPos(430+70*(i-5), 250);
         else if(i<9)
            TableCards.get(i).setPos(630+70*(i-7), 250);
         else if(i<18)
            TableCards.get(i).setPos(185+70*(i-9), 300);
         else {
            TableCards.get(i).setPos(150+70*(i-18), 350);
            TableCards.get(i).flip();
            FacingUp.add(TableCards.get(i));
         }
      }
      PlayerCard=Deck.remove(0);

   }
   
   private void update() {
      repaint();
      if(Deck.size()==0) {
         if(tableCardsLeft!=0) {
            boolean lost=true;
            for(Card card : FacingUp)
               if(PlayerCard.isAvailable(card)) {
                  lost=false;
                  break;
               }
            if(lost) {
               System.out.println("You Lose!");
               isPaused=true;
               try {
                  TimeUnit.SECONDS.sleep(1);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               reset();
               isPaused=false;
            }
         }
      }
      if(tableCardsLeft==0) {
         System.out.println("You Win!");
         try {
            TimeUnit.SECONDS.sleep(1);
            isPaused=true;
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         reset();
         isPaused=false;
      }
   }
   
   private void flipCards() {

      for(int i=17;i>=0;i--) {
         if(TableCards.get(i)!=null) {
            if(i>8) {
               if(TableCards.get(i+9)==null && TableCards.get(i+10)==null) {
                  TableCards.get(i).flip();
                  FacingUp.add(TableCards.get(i));
               }
            }
            else if(i>6) {
               if(TableCards.get(i+8)==null && TableCards.get(i+9)==null) {
                  TableCards.get(i).flip();
                  FacingUp.add(TableCards.get(i));
               }
            }
            else if(i>4) {
               if(TableCards.get(i+7)==null && TableCards.get(i+8)==null) {
                  TableCards.get(i).flip();
                  FacingUp.add(TableCards.get(i));
               }
            }
            else if(i>2) {
               if(TableCards.get(i+6)==null && TableCards.get(i+7)==null) {
                  TableCards.get(i).flip();
                  FacingUp.add(TableCards.get(i));
               }
            }
            else {
               if(TableCards.get(2*i+3)==null && TableCards.get(2*i+4)==null) {
                  TableCards.get(i).flip();
                  FacingUp.add(TableCards.get(i));
               }
            }
         }
      }
      update();
   }
   
   public void paintComponent(Graphics g) {
	   update();
	   g.drawImage(BackGround, 0, 0, TriPeaksRunner.SCREEN_WIDTH, TriPeaksRunner.SCREEN_HEIGHT, this);
	   for(Card card : TableCards )
	      if(card!=null)
	         card.draw(g, this);
	   for(Card card : Deck )
	      card.draw(g, this);
	   PlayerCard.draw(g,this);
	   PlayerCard.flip();
	   PlayerCard.setPos(465, 550);
	   if(Deck.size()!=0) {
	      Deck.get(0).setPos(550, 550);
	      Deck.get(0).draw(g,this);
	   }
   }
   
   @Override
   public void mousePressed(MouseEvent e) {
	   if(isPaused)
		   return;
	  //auto();
      if(Deck.size()!=0) {
         Rectangle rect=Deck.get(0).getRect(); //new Card
         if(rect.contains(e.getPoint())) {
            PlayerCard=Deck.remove(0);
            //System.out.println(PlayerCard);
            System.out.println(Deck.size());
            update();
            return;
         }
      }
      for(Card card : FacingUp) {
         Rectangle rect = card.getRect();
         if(rect.contains(e.getPoint())) {
            if(PlayerCard.isAvailable(card)) {
               System.out.println("Click on "+ card);
               PlayerCard=card;
               TableCards.set(TableCards.indexOf(card), null);
               FacingUp.remove(card);
               tableCardsLeft--;
               //System.out.println(tableCardsLeft);
               flipCards();
               update();
               return;
            }
         }
      }
   }
   
   private void auto() {
	   if(isPaused)
		   return;
	   Card choice=null;
	   for(Card card : FacingUp)
		   if(PlayerCard.isAvailable(card))
			   choice=card;
	   if(choice==null) {
		   if(Deck.size()!=0) {
			   PlayerCard=Deck.remove(0);
			   choice=null;
			   update();
		   }
	   }
	   else {
		   PlayerCard=choice;
		   TableCards.set(TableCards.indexOf(choice), null);
		   FacingUp.remove(choice);
		   tableCardsLeft--;
		   flipCards();
		   update();
	   }
   }
   
   public void mouseClicked(MouseEvent e) {
      // TODO Auto-generated method stub
      //System.out.println("click??"+e.getClickCount());
   }
   public void mouseReleased(MouseEvent e) {
      // TODO Auto-generated method stub
   }
   public void mouseEntered(MouseEvent e) {
      // TODO Auto-generated method stub
   }

   public void mouseExited(MouseEvent e) {
      // TODO Auto-generated method stub
   }
   

}