// Christopher Larson 
// PokerGame.java

package poker;

import deck.DeckOfCards;
import deck.Hand;

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
    private int currentPlayers;

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
        currentPlayers = 0;
        state = State.PREGAME;
    }   

    /** Adds a Player to this Game.
     *
     *  @param p  the Player to be added
     */
    public void addPlayer(Player p)
    {
        if (currentPlayers == MAX_NUM_PLAYERS)
            throw new IndexOutOfBoundsException("Too many Players.");

        players[currentPlayers] = p;
        currentPlayers++;

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
        for (int i = 0; i < currentPlayers && !removed; i++)
        {
            if (players[i] == p)
            {
                removed = true;
                players[i].deleteObserver(this);

                for (int j = i; j < currentPlayers - 1; j++)
                {
                    players[j] = players[j + 1];
                }

                currentPlayers--;
                notifyChange();
            }
        }

        if (!removed)
            throw new RuntimeException("The Player was not found in the Game.");
    }

    /** Swaps out everyone's cards.
     */
    private void swapCards()
    {
        int i;
        int j;
        Hand playerHand;
        for (i = 0; i < currentPlayers; i++)
        {
            playerHand = players[i].getHand();
            for (j = playerHand.size() - 1; j >=0; j++)
            {
                if (players[i].getSwapCard(j))
                {
                    playerHand.remove(j);
                    players[i].setSwapCard(j, false);
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
        for (int i = 0; i < currentPlayers; i++)
        {
            p = players[i];
            p.getHand().emptyHand();
            while (!p.getHand().isFull())
                p.getHand().add(deck.deal());
        }

        notifyChange();
    }

    /** Returns the winners of this round.
     *
     *  @return an array containing the winners of this round
     */
    public Player[] getWinners()
    {
        int i;
        int j;

        for (j = 1; j < currentPlayers; j++)
        {
            for (i = j - 1; i >= 0
                && (players[i].compareTo(players[j]) > 0); i--)
            {
                players[i + 1] = players[i];
            }
            players[i + 1] = players[j];
        }

        int countWinners = 1;
        for (i = 0; i < currentPlayers - 1
            && players[i].compareTo(players[i + 1]) == 0; i++)
        {
            countWinners++;
        }

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
        if (allPlayersReady())
        {
            switch (state)
            {
                case PREGAME:
                    if (currentPlayers >= MIN_NUM_PLAYERS)
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
                    state = State.PREGAME;
                    break;
            }
            
            clearPlayersReady();
            notifyChange();
        }
    }

    /** Returns true if all Players are ready for the next State.
     */
    private boolean allPlayersReady()
    {
        boolean result = true;

        for (int i = 0; result && i < currentPlayers; i++)
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
        for (int i = 0; i < currentPlayers; i++)
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
}
