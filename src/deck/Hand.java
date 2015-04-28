package deck;

import java.util.Observable;
import java.io.Serializable;

/** A Hand of Cards.
 */
public class Hand extends Observable implements Comparable<Hand>, Serializable
{
    public static final int NUM_CARDS_IN_HAND = 5;
    public static final int NO_CARD = -1;

    public enum HandType
    {
        STRAIGHT_FLUSH,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        FLUSH,
        STRAIGHT,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD,
        INCOMPLETE
    }

    private Card[] hand;
    private int size;
    private HandType type;

    private int primaryCard;
    private int secondaryCard;

    /** Tests the Hand class.
     */
    public static void main(String[] args)
    {
        System.out.println("This program tests the Hand class.\n");

        DeckOfCards deck = new DeckOfCards();

        Hand hand1 = new Hand();
        while (!hand1.isFull())
            hand1.add(deck.deal());

        Hand hand2 = new Hand();
        while (!hand2.isFull())
            hand2.add(deck.deal());

        System.out.println("Hand 1:");
        System.out.println(hand1);
        System.out.println("\nHand 2:");
        System.out.println(hand2);

        if (hand1.compareTo(hand2) > 0)
            System.out.println("\nHand 1 wins.");
        else if (hand1.compareTo(hand2) < 0)
            System.out.println("\nHand 2 wins.");
        else
            System.out.println("\nTie.");
    }

    /** Creates a new empty Hand.
     */
    public Hand()
    {
        this.hand = new Card[NUM_CARDS_IN_HAND];
        type = HandType.INCOMPLETE;
        size = 0;
        primaryCard = NO_CARD;
        secondaryCard = NO_CARD;
    }

    /** Returns the Card at index i in the Hand.
     *
     *  @param i  the index of the Card to get
     *
     *  @return the Card at index i in the Hand
     */
    public Card get(int i)
    {
        if (i < 0 || i >= NUM_CARDS_IN_HAND)
            throw new IndexOutOfBoundsException("Hand index out of range");

        if (i > size - 1)
            return null;

        return hand[i];
    }

    /** Returns and removes the Card at index i in the Hand.
     *
     *  @param i  the index of the Card to get
     *
     *  @return the Card at index i in the Hand
     */
    public Card remove(int i)
    {
        if (i < 0 || i >= NUM_CARDS_IN_HAND)
            throw new IndexOutOfBoundsException("Hand index out of range");

        if (i > size - 1)
            return null;

        Card result = hand[i];
        
        for (int j = i; j < NUM_CARDS_IN_HAND - 1; j++)
            hand[j] = hand[j + 1];
        size--;

        updateHandType();

        notifyChange();

        return result;
    }

    /** Adds a Card to the Hand. 
     *
     *  @param c  the Card to add
     */
    public void add(Card c)
    {
        if (size == NUM_CARDS_IN_HAND)
            throw new IndexOutOfBoundsException("Cannot add to a full hand");

        int i;
        for (i = size; i > 0 && c.getFace() < hand[i - 1].getFace(); i--)
        {
            hand[i] = hand[i - 1];
        }
        hand[i] = c;

        size++;

        updateHandType();

        notifyChange();
    }

    /** Empties the hand of any Cards.
     */
    public void emptyHand()
    {
        size = 0;
        type = HandType.INCOMPLETE;

        notifyChange();
    }

    /** Returns the number of Cards currently in the Hand.
     *
     *  @return the number of Cards currently in the Hand
     */
    public int size()
    {
        return size;
    }

    /** Returns true if the Hand is full.
     *
     *  @return true if the Hand is full.
     */
    public boolean isFull()
    {
        return size() == NUM_CARDS_IN_HAND;
    }

    /** Returns the HandType of the Hand.
     *
     *  @return the HandType of the Hand
     */
    public HandType getHandType()
    {
        return type;
    }
    
    /** Updates the type of the Hand to one of the values of HandType.
     */
    private void updateHandType()
    {
        primaryCard = NO_CARD;
        secondaryCard = NO_CARD;

        if (size() == NUM_CARDS_IN_HAND)
        {
            boolean straight = isStraight();
            boolean flush = isFlush();

            if (straight && flush)
            {
                type = HandType.STRAIGHT_FLUSH;
            }
            else if (isFourOfAKind())
            {
                type = HandType.FOUR_OF_A_KIND;
            }
            else if (flush)
            {
                type = HandType.FLUSH;
            }
            else if (straight)
            {
                type = HandType.STRAIGHT;
            }
            else if (isFullHouse())
            {
                type = HandType.FULL_HOUSE;
            }
            else if (isThreeOfAKind())
            {
                type = HandType.THREE_OF_A_KIND;
            }
            else if (isTwoPair())
            {
                type = HandType.TWO_PAIR;
            }
            else if (isPair())
            {
                type = HandType.ONE_PAIR;
            }
            else
            {
                type = HandType.HIGH_CARD;
            }
        }
        else
        {
            type = HandType.INCOMPLETE;
        }
    }


    /** Returns true if this hand is four of a kind.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is four of a kind
     */
    private boolean isFourOfAKind()
    {
        boolean early = hand[0].getFace() == hand[1].getFace()
                     && hand[1].getFace() == hand[2].getFace()
                     && hand[2].getFace() == hand[3].getFace();

        boolean late = hand[1].getFace() == hand[2].getFace()
                    && hand[2].getFace() == hand[3].getFace()
                    && hand[3].getFace() == hand[4].getFace();

        if (early || late)
        {
            primaryCard = hand[2].getFace();
            return true;
        }
        else
        {
            return false;
        }
    }

    /** Returns true if this hand is a full house.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is a full house
     */
    private boolean isFullHouse()
    {
        boolean early = hand[0].getFace() == hand[1].getFace()
                     && hand[1].getFace() == hand[2].getFace()
                     && hand[3].getFace() == hand[4].getFace();

        boolean late = hand[0].getFace() == hand[1].getFace()
                    && hand[2].getFace() == hand[3].getFace()
                    && hand[3].getFace() == hand[4].getFace();

        if (early || late)
        {
            primaryCard = hand[2].getFace();
            return true;
        }
        else
        {
            return false;
        }
    }

    /** Returns true if this hand is a flush.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is a flush
     */
    private boolean isFlush()
    {
        boolean flush = true;
        int suit = hand[0].getSuit();
        for (int i = 1; i < NUM_CARDS_IN_HAND; i++)
            if (hand[i].getSuit() != suit)
                flush = false;

        return flush;
    }

    /** Returns true if this hand is a straight.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is a straight
     */
    private boolean isStraight()
    {
        // A wheel is the only time in poker where an Ace is low
        if (hand[0].getFace() == 2 && hand[1].getFace() == 3
            && hand[2].getFace() == 4 && hand[3].getFace() == 5
            && hand[4].getFace() == Card.ACE)
        {
            primaryCard = 5;
            return true;
        }

        for (int i = 1; i < size; i++)
        {
            if (hand[i].getFace() != hand[i - 1].getFace() + 1)
                return false;
        }
        
        primaryCard = hand[4].getFace();

        return true;
    }

    /** Returns true if this hand is three of a kind.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is three of a kind
     */
    private boolean isThreeOfAKind()
    {
        boolean early = hand[0].getFace() == hand[1].getFace()
                     && hand[1].getFace() == hand[2].getFace();

        boolean middle = hand[1].getFace() == hand[2].getFace()
                      && hand[2].getFace() == hand[3].getFace();

        boolean late = hand[2].getFace() == hand[3].getFace()
                    && hand[3].getFace() == hand[4].getFace();

        if (early || middle || late)
        {
            primaryCard = hand[2].getFace();
            return true;
        }
        else
        {
            return false;
        }
    }

    /** Returns true if this hand is two pairs.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is two pairs
     */
    private boolean isTwoPair()
    {
        boolean early = hand[0].getFace() == hand[1].getFace()
                     && hand[2].getFace() == hand[3].getFace();

        boolean split = hand[0].getFace() == hand[1].getFace()
                     && hand[3].getFace() == hand[4].getFace();

        boolean late = hand[1].getFace() == hand[2].getFace()
                    && hand[3].getFace() == hand[4].getFace();

        if (early || split || late)
        {
            primaryCard = Math.max(hand[1].getFace(), hand[3].getFace());
            secondaryCard = Math.min(hand[1].getFace(), hand[3].getFace());
            return true;
        }
        else
        {
            return false;
        }
    }

    /** Returns true if this hand is a pair.
     *
     *  @precondition Hand is full
     *
     *  @return true if this hand is a pair
     */
    private boolean isPair()
    {
        for (int i = 1; i < NUM_CARDS_IN_HAND; i++)
            if (hand[i].getFace() == hand[i - 1].getFace())
            {
                primaryCard = hand[i].getFace();
                return true;
            }

        return false;
    }

    /** Compares this Hand to another Hand.
     *
     *  @param h  the Hand to compare with
     *
     *  @return a negative integer if this Hand is less than h, a positive
     *  integer if this Hand is greater than h, or 0 if they are equivalent.
     */
    public int compareTo(Hand h)
    {
        // If the Hands have different HandTypes, compare them by HandType
        if (type.ordinal() != h.type.ordinal())
        {
            return h.type.ordinal() - type.ordinal();
        }
        // Otherwise compare them by primary, secondary, or hand order
        else
        {
            // If the Hands have different primary cards, compare those
            if (h.primaryCard != primaryCard)
            {
                return primaryCard - h.primaryCard;
            }
            // Otherwise compare them by secondary or hand order
            else
            {
                // If the Hands have different secondary cards, compare
                if (h.secondaryCard != secondaryCard)
                {
                    return secondaryCard - h.secondaryCard;
                }
                // Otherwise compare them by hand order
                else
                {
                    // Sequentially compare each non-type card in both hands
                    int i = NUM_CARDS_IN_HAND - 1;
                    int j = NUM_CARDS_IN_HAND - 1;

                    while (i >= 0 && j >= 0)
                    {
                        while (hand[i].getFace() == primaryCard
                            || hand[i].getFace() == secondaryCard)
                            i--;

                        while (h.hand[j].getFace() == h.primaryCard
                            || h.hand[j].getFace() == h.secondaryCard)
                            j--;

                        if (hand[i].getFace() != h.hand[j].getFace())
                            return hand[i].getFace() - h.hand[j].getFace();

                        i--;
                        j--;
                    }

                    return 0;
                }
            }
        }
    }

    /** Notifies Observers that the Hand has changed.
     */
    private void notifyChange()
    {
        setChanged();
        notifyObservers();
    }

    /** Returns a String representation of the Hand.
     *
     *  @return a String representation of the Hand.
     */
    public String toString()
    {
        String result = "";

        for (Card c : hand)
            result += c + "\n";

        result += type + " " + primaryCard + " " + secondaryCard;

        return result;
    }
}
