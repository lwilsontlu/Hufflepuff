package server;

import poker.*;

import java.io.Serializable;

public class PokerGamePacket implements Serializable
{
    private PokerGame.State state;
    private Player[] players;

    public PokerGamePacket(PokerGame game)
    {
        this.state = game.getState();

        players = game.getPlayers();
    }

    public PokerGame.State getState()
    {
        return state;
    }

    public Player getPlayer(int i)
    {
        if (i < 0 || i > players.length - 1)
            return null;

        return players[i];
    }

    public String toString()
    {
        String result = state + "\n";

        for (int i = 0; i < players.length; i++)
        {
            result += players[i] + "\n";
        }

        return result;
    }
}
