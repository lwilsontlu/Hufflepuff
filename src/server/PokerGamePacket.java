package server;

import poker.*;
import deck.Card;
import deck.Hand;

public class PokerGamePacket
{
    private PokerGame game;
    private String name;
    private Player[] players;

    public PokerGamePacket(PokerGame game, Player player)
    {
        this.game = game;
        this.name = player.getUsername();

        players = game.getPlayers();
    }

    public PokerGame.State getState()
    {
        return game.getState();
    }

    public Player getPlayer(int i)
    {
        if (i < 0 || i > players.length - 1)
            return null;

        return players[i];
    }

    public String toString()
    {
        String result = "";

        if (game.getState() == PokerGame.State.PREGAME)
        {
            result += "newRound\n";
        }

        result += "playerList ";
        int i;
        int j;

        for (i = 0; i < players.length; i++)
        {
            if (!players[i].getUsername().equals(name))
                result += players[i].getUsername() + " ";
        }
        result += "\n";

        Player p;
        Card c;
        int rank;
        for (i = 0; i < players.length; i++)
        {
            p = players[i];

            for (j = 0; j < Hand.NUM_CARDS_IN_HAND; j++)
            {
                c = p.getHand().get(j);
                
                if (c != null && !p.getSwapCard(j) && (
                    (p.getUsername().equals(name)
                     || game.getState() == PokerGame.State.POSTGAME)))
                {
                    rank = c.getRank();
                }
                else
                {
                    // Invalid card, so back will be used
                    rank = -1;
                }

                result += "player " + p.getUsername() + " card "
                    + j + " " + rank + "\n";
            }

            result += "player " + p.getUsername() + " ready "
                + p.isReady() + "\n";
        }

        if (game.getState() == PokerGame.State.POSTGAME) 
        {
            Player[] winners = game.getWinners();

            result += "winner ";
            for (Player w : winners)
            {
                result += w.getUsername() + " ";
            }
            result += "\n";
        }

        return result;
    }
}
