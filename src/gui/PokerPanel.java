package gui;

import poker.PokerGame;
import poker.Player;
import server.PokerGamePacket;

import java.awt.*;
import javax.swing.*;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class PokerPanel extends JPanel
{
    private PlayerPanel[] playerPanels;

    PrintWriter outToServer;

    private String name;

    private boolean alreadyShownWinnersThisRound;

    public PokerPanel(String server, int port)
    {
        alreadyShownWinnersThisRound = false;

        try
        {
            name = JOptionPane.showInputDialog("Enter your name:")
                .replaceAll(" ", "");

            Socket connection = new Socket(server, port);

            outToServer =
                new PrintWriter(connection.getOutputStream());
            Scanner inFromServer =
                new Scanner(connection.getInputStream());

            sendCommand(name);

            String[] response = inFromServer.nextLine().split(" ", 2);

            if (response[0].equals("accept"))
            {
                setLayout(new GridLayout(PokerGame.MAX_NUM_PLAYERS, 1));
                playerPanels = new PlayerPanel[PokerGame.MAX_NUM_PLAYERS];

                int i;
                for (i = 0; i < PokerGame.MAX_NUM_PLAYERS; i++)
                {
                    playerPanels[i] = new PlayerPanel(this,
                        i == PokerGame.MAX_NUM_PLAYERS - 1);
                    add(playerPanels[i]);
                }

                playerPanels[playerPanels.length - 1].setName(name);

                new PokerIncomingInformationThread(inFromServer).start();
            }
            else
            {
                add(new JLabel(response[1]));
            }
        }
        catch (Exception e)
        {
            add(new JLabel("Could not connect to server."));
        }
    }

    public void setAlreadyShownWinnersThisRound(boolean b)
    {
        alreadyShownWinnersThisRound = b;
    }

    public void sendCommand(String command)
    {
        outToServer.println(command);
        outToServer.flush();
    }

    private class PokerIncomingInformationThread extends Thread
    {
        private Scanner inFromServer;

        public PokerIncomingInformationThread(Scanner inFromServer)
        {
            this.inFromServer = inFromServer;
        }

        public void run()
        {
            boolean reading = true;

            while (reading)
            {
                try
                {
                    String command = inFromServer.nextLine().trim();

                    String[] args = command.split(" ");

                    PlayerPanel changePanel = null;
                    String userName;
                    int index;
                    int rank;
                    boolean ready;
                    boolean found;
                    if (args.length > 0)
                    {
                        if (args[0].equals("playerList"))
                        {
                            for (int i = 0; i < playerPanels.length - 1; i++)
                            {
                                if (i < args.length - 1)
                                {
                                    playerPanels[i].setName(args[i + 1]);
                                }
                                else
                                {
                                    playerPanels[i].clearName();
                                }
                            }
                        }
                        else if (args[0].equals("newRound"))
                        {
                            alreadyShownWinnersThisRound = false;
                        }
                        else if (args[0].equals("winner")
                            && !alreadyShownWinnersThisRound)
                        {
                            String winString = "Congratulations!\n";
                            winString += "Winner(s):\n\n";

                            for (int i = 1; i < args.length; i++)
                            {
                                winString += args[i] + "\n";
                            }

                            JOptionPane.showMessageDialog(PokerPanel.this,
                                winString);

                            alreadyShownWinnersThisRound = true;
                        }
                        else if (args[0].equals("player") && args.length > 3)
                        {
                            found = false;

                            userName = args[1];

                            if (userName.equals(name))
                            {
                                changePanel
                                    = playerPanels[playerPanels.length - 1];
                                found = true;
                            }
                            else
                            {
                                //other player handle

                                for (int i = 0;
                                    i < playerPanels.length - 1 && !found; i++)
                                {
                                    if (playerPanels[i].getName() != null
                                        && playerPanels[i].getName()
                                            .equals(userName))
                                    {
                                        changePanel = playerPanels[i];
                                        found = true;
                                    }
                                }
                            }

                            if (found)
                            {
                                changePanel.setName(userName);

                                if (args[2].equals("card") && args.length > 4)
                                {
                                    index = Integer.parseInt(args[3]);
                                    rank = Integer.parseInt(args[4]);

                                    changePanel.setCard(index, rank);
                                }
                                else if (args[2].equals("ready"))
                                {
                                    changePanel.setReady(
                                        Boolean.parseBoolean(args[3]));
                                }
                            }
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    reading = false;
                }
            }
        }
    }
}
