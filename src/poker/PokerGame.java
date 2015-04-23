// Christopher Larson 
// PokerGame.java

package poker;

import deck.*;

import java.util.Observable;
import java.util.Observer;

public class PokerGame extends Observable implements Observer
{
    public enum State
    {
        PREGAME, DEALT, POSTGAME;
    }

    public static final int MAX_NUM_PLAYERS = 5;
    public static final int MIN_NUM_PLAYERS = 2;
    
    private DeckOfCards deck;
    private Player[] players;
    private int numPlayers;

    private State state;
    
    /** Starts the poker game.
     */
    public static void main(String[] args)
    {
        new PokerGame();
    }

    /** Creates the poker game.
     */
    public PokerGame()
    {
        players = new Player[MAX_NUM_PLAYERS];
        deck = new DeckOfCards();
        numPlayers = 0;
        state = State.PREGAME;
    }   

    /** Adds a Player to this Game.
     *
     *  @param p  the Player to be added
     */
    public void addPlayer(Player p)
    {
        if (numPlayers == MAX_NUM_PLAYERS)
            throw new IndexOutOfBoundsException("Too many Players.");

        players[numPlayers] = p;
        numPlayers++;

        p.addObserver(this);

        notifyChange();

        nextState();
    }

    /** Removes a Player from this Game.
     *
     *  @param p  the Player to be removed
     */
    public void removePlayer(Player p)
    {
        boolean removed = false;
        for (int i = 0; i < numPlayers && !removed; i++)
        {
            if (players[i] == p)
            {
                removed = true;
                players[i].deleteObserver(this);

                for (int j = i; j < numPlayers - 1; j++)
                {
                    players[j] = players[j + 1];
                }

                numPlayers--;

                nextState();

                notifyChange();
            }
        }

        if (!removed)
            throw new RuntimeException("The Player was not found in the Game.");
    }

    public boolean isFull()
    {
        return numPlayers == MAX_NUM_PLAYERS;
    }

    public boolean hasPlayerWithUsername(String username)
    {
        for (int i = 0; i < numPlayers; i++)
        {
            if (players[i].getUsername().equals(username))
                return true;
        }
        return false;
    }

    /** Swaps out everyone's cards.
     */
    private void swapCards()
    {
        int i;
        int j;
        Hand playerHand;
        for (i = 0; i < numPlayers; i++)
        {
            playerHand = players[i].getHand();
            for (j = playerHand.size() - 1; j >=0; j--)
            {
                if (players[i].getSwapCard(j))
                {
                    players[i].setSwapCard(j, false);
                    playerHand.remove(j);
                }
            }
            
            while (!playerHand.isFull())
            {
                playerHand.add(deck.deal());
            }
        }

        notifyChange();
    }
    
    /** Deals all new Hands to the Players
     */
    private void dealHand()
    {
        deck.shuffle();

        Player p;
        for (int i = 0; i < numPlayers; i++)
        {
            p = players[i];
            p.getHand().emptyHand();
            while (!p.getHand().isFull())
                p.getHand().add(deck.deal());
        }

        notifyChange();
    }

    /** Clears all Players' Hands
     */
    private void emptyHands()
    {
        for (int i = 0; i < numPlayers; i++)
        {
            players[i].getHand().emptyHand();
        }
    }

    public Player[] getPlayers()
    {
        Player[] result = new Player[numPlayers];;

        for (int i = 0; i < numPlayers; i++)
            result[i] = players[i];

        return result;
    }

    /** Returns the winners of this round.
     *
     *  @return an array containing the winners of this round
     */
    public Player[] getWinners()
    {
        int i;
        int j;

        // Sort Players
        int maxIndex;
        Player temp;
        for (i = 0; i < numPlayers - 1; i++)
        {
            maxIndex = i;
            for (j = i + 1; j < numPlayers; j++)
            {
                if (players[j].compareTo(players[maxIndex]) > 0)
                {
                    maxIndex = j;
                }
            }
            temp = players[i];
            players[i] = players[maxIndex];
            players[maxIndex] = temp;
        }

        // Count winners (most of the time this will be 1)
        int countWinners = 1;
        for (i = 0; i < numPlayers - 1
            && players[i].compareTo(players[i + 1]) == 0; i++)
        {
            countWinners++;
        }

        // Make a new winners array
        Player[] winners = new Player[countWinners];
        for (i = 0; i < countWinners; i++)
        {
            winners[i] = players[i];
        }

        return winners;
    }

    /** Attempts to move to the next stage of the game.
     */
    public void nextState()
    {
        if (numPlayers < MIN_NUM_PLAYERS)
        {
            state = State.PREGAME;
        }
        else if (allPlayersReady())
        {
            clearPlayersReady();

            switch (state)
            {
                case PREGAME:
                    if (numPlayers >= MIN_NUM_PLAYERS)
                    {
                        dealHand();
                        state = State.DEALT;
                    }
                    break;
                case DEALT:
                    swapCards();
                    state = State.POSTGAME;
                    break;
                case POSTGAME:
                    emptyHands();
                    state = State.PREGAME;
                    break;
            }

            notifyChange();
        }
    }

    public State getState()
    {
        return state;
    }

    /** Returns true if all Players are ready for the next State.
     */
    private boolean allPlayersReady()
    {
        boolean result = true;

        for (int i = 0; result && i < numPlayers; i++)
        {
            if (!players[i].isReady())
                result = false;
        }

        return result;
    }

    /** Clears all Players' ready state for the next stage of the Game.
     */
    private void clearPlayersReady()
    {
        for (int i = 0; i < numPlayers; i++)
            players[i].setReady(false);
    }

    /** Notifies Observers that the Game has changed.
     */
    private void notifyChange()
    {
        setChanged();
        notifyObservers();
    }

    /** Attempt to move to the next State whenever a Player is updated.
     */
    public void update(Observable o, Object arg)
    {
        nextState();
    }

    /** Returns a String representation of the Game
     *
     *  @return a String representation of the Game
     */
    public String toString()
    {
        String result = "";

        for (int i = 0; i < numPlayers; i++)
            result += players[i] + "\n\n";

        result += state;

        if (state == State.POSTGAME)
        {
            result += "\nWinners:\n";

            for (Player p : getWinners())
                result += p.getUsername() + "\n";
        }

        return result;
    }
}
