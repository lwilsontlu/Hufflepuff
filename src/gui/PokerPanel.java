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
    private Socket connection;

    private String name;

    public PokerPanel(String server, int port)
    {
        try
        {
            connection = new Socket(server, port);

            name = JOptionPane.showInputDialog("Enter your name:");
            PrintWriter outToServer =
                new PrintWriter(connection.getOutputStream());
            ObjectInputStream ois =
                new ObjectInputStream(connection.getInputStream());

            outToServer.println(name);
            outToServer.flush();

            Object in = ois.readObject();

            String[] response = null;
            if (in instanceof String)
            {
                response = ((String) in).split(" ", 2);
            }
            else
            {
                System.exit(1);
            }

            System.out.println(response[0]);
            System.out.println(response[1]);
            if (response[0].equals("accept"))
            {
                setLayout(new GridLayout(PokerGame.MAX_NUM_PLAYERS, 1));
                playerPanels = new PlayerPanel[PokerGame.MAX_NUM_PLAYERS];

                int i;
                for (i = 0; i < PokerGame.MAX_NUM_PLAYERS; i++)
                {
                    playerPanels[i] = new PlayerPanel(outToServer,
                        i == PokerGame.MAX_NUM_PLAYERS - 1);
                    add(playerPanels[i]);
                }

                new PokerIncomingInformationThread(ois).start();
            }
            else
            {
                add(new JLabel(response[1]));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class PokerIncomingInformationThread extends Thread
    {
        private ObjectInputStream inFromServer;

        public PokerIncomingInformationThread(ObjectInputStream inFromServer)
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
                    Object in = inFromServer.readObject();
                    System.out.println("Received packet from server.");

                    if (in instanceof PokerGamePacket)
                    {
                        PokerGamePacket packet = (PokerGamePacket) in;

                        int count = 0;
                        int i;
                        Player player;
                        for (i = 0; i < playerPanels.length; i++)
                        {
                            player = packet.getPlayer(i);

                            if (player != null)
                            {
                                if (player.getUsername().equals(name))
                                {
                                    playerPanels[playerPanels.length - 1]
                                        .setPlayer(player, false);
                                }
                                else
                                {
                                    playerPanels[count].setPlayer(player,
                                        packet.getState()
                                            == PokerGame.State.POSTGAME);

                                    count++;
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
