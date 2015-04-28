package server;

import poker.PokerGame;
import poker.Player;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.Observer;
import java.util.Observable;
import java.util.Scanner;
import java.io.*;

public class PokerPlayerThread implements Observer, Runnable
{
    private ObjectOutputStream outToClient;
    private Scanner inFromClient;
    private PokerGame game;
    private Player player;

    public PokerPlayerThread(ObjectOutputStream outToClient,
        Scanner inFromClient, PokerGame game, Player player)
    {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;

        this.game = game;
        this.player = player;

        game.addObserver(this);
        
        game.addPlayer(player);
    }

    public void run()
    {
        String[] playerCommand;
        int card;
        boolean swap;

        boolean inGame = true;

        while (inGame)
        {
            try
            {
                playerCommand = inFromClient.nextLine().split(" ");

                if (playerCommand.length > 0)
                {
                    if (playerCommand[0].equals("ready"))
                    {
                        player.setReady(true);
                    }
                    else if (playerCommand[0].equals("swap"))
                    {
                        if (playerCommand.length > 1)
                        {
                            card = Integer.parseInt(playerCommand[1]);

                            player.setSwapCard(card, !player.getSwapCard(card));
                        }
                    }
                    else if (playerCommand[0].equals("quit"))
                    {
                        game.removePlayer(player);
                        inGame = false;
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("Invalid command from "
                    + player.getUsername());
            }
        }
    }

    public void update(Observable o, Object arg)
    {
        // Send information about the game to the player
        // TODO: make PokerGamePacket object
        try
        {
            PokerGamePacket packet = new PokerGamePacket(game, player);

            outToClient.writeObject(packet);
            outToClient.flush();
        }
        catch (IOException e)
        {
            System.err.println("Failed to write object to player "
                + player.getUsername());
        }
    }
}
