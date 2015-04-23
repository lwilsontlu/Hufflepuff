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

    public PokerPlayerThread(Socket connection, PokerGame game, Player player)
    {
        try
        {
            outToClient = new ObjectOutputStream(connection.getOutputStream());
            inFromClient = new Scanner(connection.getInputStream());

            this.game = game;
            this.player = player;
            
            game.addPlayer(player);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
                        if (playerCommand.length > 2)
                        {
                            card = Integer.parseInt(playerCommand[1]);
                            swap = Boolean.parseBoolean(playerCommand[2]);

                            player.setSwapCard(card, swap);
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
