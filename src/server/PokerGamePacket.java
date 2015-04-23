package server;

import poker.*;

import java.io.Serializable;

public class PokerGamePacket implements Serializable
{
    private PokerGame game;
    private Player[] players;

    public PokerGamePacket(PokerGame game, Player player)
    {
        this.game = game;

        players = game.getPlayers();

        if (game.getState() != PokerGame.State.POSTGAME)
        {
            for (int i = 0; i < players.length; i++)
            {
                if (players[i].getUsername() != player.getUsername())
                    players[i] = (HiddenPlayer) players[i];
            }
        }
    }
}
