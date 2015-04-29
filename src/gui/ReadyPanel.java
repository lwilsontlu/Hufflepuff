package gui;

import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.PrintWriter;

public class ReadyPanel extends JPanel
{
    private PokerPanel client;
    private boolean canControl;
    private ReadyButton readyBox;
    private JLabel readyLabel;

    public ReadyPanel(PokerPanel client, boolean canControl)
    {
        this.client = client;
        this.canControl = canControl;

        setPreferredSize(new Dimension(100, 100));

        if (canControl)
        {
            readyBox = new ReadyButton("Ready");
            readyBox.setEnabled(false);
            add(readyBox);
        }
        else
        {
            readyLabel = new JLabel("Not ready");
            add(readyLabel);
        }
    }

    public void setReady(boolean ready)
    {
        if (canControl)
        {
            readyBox.setEnabled(!ready);
        }
        else
        {
            if (ready)
            {
                readyLabel.setText("Ready");
            }
            else
            {
                readyLabel.setText("Not ready");
            }
        }
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
                    if (canControl)
                    {
                        client.sendCommand("ready");
                    }
                }
            });
        }
    }
}
