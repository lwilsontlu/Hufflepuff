package main;

import gui.PokerGUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PlayPoker
{
    public static void main(String[] args)
    {
        String server;
        int port = -1;

        if (args.length > 0)
        {
            server = args[0];
        }
        else
        {
            server = JOptionPane.showInputDialog(
                "Enter the server address:");
        }

        boolean valid = false;

        if (args.length > 1)
        {
            try
            {
                port = Integer.parseInt(args[1]);
                valid = true;
            }
            catch (NumberFormatException e)
            {
                valid = false;
            }
        }

        while (!valid)
        {
            try
            {
                port = Integer.parseInt(JOptionPane.showInputDialog(
                    "Enter the server port:"));
                valid = (port > 0);
            }
            catch (NumberFormatException e)
            {
                valid = false;
            }
        }

        JFrame game = new PokerGUI(server, port);
        game.setVisible(true);
    }
}
