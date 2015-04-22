// GraphicalDeck.java
//
// Represents a graphical deck of cards

package deck;

public class GraphicalDeck extends DeckOfCards
{
    // Builds deck of graphical cards and shuffles it
    // Does not call super() because that would create
    // deck of non-graphical cards.
    public GraphicalDeck()
    {
        int i;
        deck = new GraphicalCard[NUM_CARDS];  
        for (i = 0; i < NUM_CARDS; i++)
            deck[i] = new GraphicalCard(i);   
        shuffle();        // shuffle method sets top
    }

    public GraphicalCard deal()
    {
        return (GraphicalCard) super.deal();
    }
}
