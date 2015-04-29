package poker;

import deck.Hand;

import java.util.Observer;
import java.util.Observable;

/** A Player in a game of Poker.
 */
public class Player extends Observable implements Observer
{
    private String username;
    private int balance;
    private Hand hand;
    private boolean[] swapCards;
    private boolean ready;

    public Player()
    {
        this("");
    }

    /** Constructor of the Player class
     *  
     *  @param username The username of the Player
     */
    public Player(String username)
    {
        this.username = username;
        this.balance = 0;
        hand = new Hand();
        hand.addObserver(this);
        swapCards = new boolean[Hand.NUM_CARDS_IN_HAND];
        ready = false;
    }

    /** Returns true if the Player is ready to move to the next game state.
     *
     *  @return true if the Player is ready to move to the next game state
     */
    public boolean isReady()
    {
        return ready;
    }

    /** Changes the Player's state of readiness.
     *
     *  @param b  the Player's new state of readiness
     */
    public void setReady(boolean b)
    {
        ready = b;
        notifyChange();
    }

    /** Sets which Cards the Player wants to swap out.
     *
     *  @param i  the index of the Card to be swapped
     *  @param b  true if the Player wants to swap the Card at index i
     */
    public void setSwapCard(int i, boolean b)
    {
        if (i < 0 || i > swapCards.length)
            throw new IndexOutOfBoundsException("Invalid index in setSwapCard");

        swapCards[i] = b;
        notifyChange();
    }

    /** Gets whether the the Player wants to swap out a given Card.
     *
     *  @param i  the index of the Card to be swapped
     *
     *  @return true if the Player wants to swap the Card at index i
     */
    public boolean getSwapCard(int i)
    {
        if (i < 0 || i > swapCards.length)
            throw new IndexOutOfBoundsException("Invalid index in getSwapCard");

        return swapCards[i];
    }

    /** Returns the Player's name.
     *
     *  @return the Player's name
     */
    public String getUsername()
    {
        return username;
    }

    /** Sets the username of the Player
     *
     * @param name The name entered by the Player
     */
    public void setUsername(String name)
    {
        username = name;
        notifyChange();
    }

    /** Returns the Player's Hand.
     *
     *  @return the Player's Hand
     */
    public Hand getHand()
    {
        return hand;
    }

    /** Return the balance of the Player
     *
     * @returns balance The balance of the Player
     */
    public int getBalance()
    {
        return balance;
    }

    /** Changes the balance of the Player
     *
     * @param winnings The amount to change the balance
     */
    public void changeBalance(int winnings)
    {
        balance += winnings;
        notifyChange();
    }

    /** Notifies Observers that the Player has changed.
     */
    private void notifyChange()
    {
        setChanged();
        notifyObservers();
    }

    /** Process changes to the Player's Hand.
     */
    public void update(Observable o, Object arg)
    {
        if (o == hand)
        {
            notifyChange();
        }
    }
    
    /** Compares two player's hands
     *
     *  @param otherPlayer The other player to be compared to
     */
    public int compareTo(Player otherPlayer)
    {
        return hand.compareTo(otherPlayer.getHand());
    }

    /** Returns a String representation of the Player.
     *
     *  @return a String representation of the Player
     */
    public String toString()
    {
        String result = "Username: " + username + "\n";

        for (int i = 0; i < hand.size(); i++)
        {
            if (swapCards[i])
                result += "+ ";
            else
                result += "- ";

            result += hand.get(i) + "\n";
        }

        result += hand.getHandType() + "\n";

        if (ready)
            result += "Ready.";
        else
            result += "Not ready.";

        return result;
    }
}
