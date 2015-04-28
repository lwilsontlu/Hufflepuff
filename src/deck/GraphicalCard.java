// GraphicalCard.java
//
// This class represents a standard playing card (from a deck of 52 cards)
// with a corresponding graphical image

package deck;

import javax.swing.ImageIcon;

public class GraphicalCard extends Card
{
    private ImageIcon image;

    private static String imageFile[] = {
        "c2.gif", "c3.gif", "c4.gif", "c5.gif", "c6.gif", 
        "c7.gif", "c8.gif", "c9.gif", "c10.gif", "cj.gif",
        "cq.gif", "ck.gif", "c1.gif", "d2.gif", "d3.gif", 
        "d4.gif", "d5.gif", "d6.gif", "d7.gif", "d8.gif", 
        "d9.gif", "d10.gif", "dj.gif", "dq.gif", "dk.gif", 
        "d1.gif", "h2.gif", "h3.gif", "h4.gif", "h5.gif", 
        "h6.gif", "h7.gif", "h8.gif", "h9.gif", "h10.gif", 
        "hj.gif", "hq.gif", "hk.gif", "h1.gif", "s2.gif", 
        "s3.gif", "s4.gif", "s5.gif", "s6.gif", "s7.gif", 
        "s8.gif", "s9.gif", "s10.gif", "sj.gif", "sq.gif", 
        "sk.gif", "s1.gif"};

    public GraphicalCard()   // default constructor, create a random card
    {
        super();
        image = null;
    }

    public GraphicalCard(Card card)
    {
        this(card.getFace(), card.getSuit());
    }
    
    public GraphicalCard(int rank)   
    // Precondition:  0 <= rank <= MAX_RANK
    {
        super(rank);
        image = new ImageIcon(
            this.getClass().getResource("/resources/" + imageFile[rank]));
    }
    
    public GraphicalCard(int face, int suit)
    // Precondition:  TWO <= face <= ACE and CLUBS <= suit <= SPADES
    {
        super(face, suit);
        image = new ImageIcon(
            this.getClass().getResource("/resources/"
		        + imageFile[suit * CARDS_PER_SUIT + face]));
    }
   
    public ImageIcon getImage()
    {
        return image;
    }
}    
