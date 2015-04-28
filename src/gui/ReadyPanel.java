package gui;

import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.PrintWriter;

public class ReadyPanel extends JPanel
{
    PokerPanel client;
    private boolean canControl;
    ReadyButton readyBox;

    public ReadyPanel(PokerPanel client, boolean canControl)
    {
        this.client = client;
        this.canControl = canControl;

        readyBox = new ReadyButton("Ready");
        readyBox.setEnabled(false);
        add(readyBox);
    }

    public void setReady(boolean ready)
    {
        readyBox.setEnabled(!ready);
    }

    private class ReadyButton extends JButton
    {
        public ReadyButton(String text)
        {
            super(text);

            addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Button clicked");
                    if (canControl)
                    {
                        System.out.println("Sending ready");

                        client.sendCommand("ready");
                    }
                }
            });
        }
    }
}
