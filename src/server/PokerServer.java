package server;

import poker.PokerGame;
import poker.Player;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.Scanner;


public class PokerServer
{
    public static final int DEFAULT_PORT = 7777;

    private int port;
    private PokerGame game;

    public static void main(String[] args)
    {
        PokerServer server;
        int serverPort;

        if (args.length > 0)
        {
            try
            {
                serverPort = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                serverPort = DEFAULT_PORT;
            }
        }
        else
        {
            serverPort = DEFAULT_PORT;
        }

        new PokerServer(serverPort).startServer();
    }

    public PokerServer(int port)
    {
        this.port = port;
        this.game = new PokerGame();
    }

    public void startServer()
    {
        try
        {
            ServerSocket welcomeSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            Socket connection;
            ObjectOutputStream outToClient;
            Scanner inFromClient;
            String message;
            boolean accept;
            String username;
            Player player;
            PokerPlayerThread playerThread;

            while (true)
            {
                connection = welcomeSocket.accept();
                outToClient = 
                    new ObjectOutputStream(connection.getOutputStream());
                inFromClient = new Scanner(connection.getInputStream());

                username = inFromClient.nextLine().trim();

                System.out.println("Client \"" + username + "\" connected.");

                if (game.isFull())
                {
                    message = "The game is full.";
                    accept = false;
                }
                else if (game.hasPlayerWithUsername(username))
                {
                    message = "There is already a Player with that username.";
                    accept = false;
                }
                else
                {
                    message = "Connected.";
                    accept = true;
                }

                System.out.println(message);
                if (accept)
                {
                    outToClient.writeObject("accept " + message);

                    player = new Player(username);

                    playerThread =
                        new PokerPlayerThread(outToClient, inFromClient,
                            game, player);
                }
                else
                {
                    outToClient.writeObject("deny " + message);
                }

                outToClient.flush();
            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }
}
