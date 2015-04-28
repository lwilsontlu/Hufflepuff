package server;

import poker.PokerGame;
import poker.Player;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;
import java.io.*;

public class PokerPlayerThread extends Thread implements Observer
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
        String commandLine;
        String[] playerCommand;
        int card;
        boolean swap;

        boolean inGame = true;

        while (inGame)
        {
            try
            {
                commandLine = inFromClient.nextLine();
                playerCommand = commandLine.split(" ");

                System.out.println(player.getUsername() + ": " + commandLine);

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
                        game.deleteObserver(this);
                        inGame = false;
                    }
                }
            }
            catch (NoSuchElementException e)
            {
                game.removePlayer(player);
                game.deleteObserver(this);
                inGame = false;
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
            PokerGamePacket packet = new PokerGamePacket(game);

            outToClient.writeObject(packet);
            outToClient.flush();
        }
        catch (IOException e)
        {
            System.err.println(player.getUsername() + " disconnected.");
        }
    }
}
