package gui;

import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.PrintWriter;

public class ReadyPanel extends JPanel
{
    private PrintWriter outToServer;
    private boolean canControl;
    ReadyButton readyBox;

    public ReadyPanel(PrintWriter outToServer, boolean canControl)
    {
        this.outToServer = outToServer;
        this.canControl = canControl;

        readyBox = new ReadyButton("Ready");
        add(readyBox);
    }

    public void setReady(boolean ready)
    {
        readyBox.setEnabled(ready);
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
                        outToServer.println("ready");
                        outToServer.flush();
                    }
                }
            });
        }
    }
}
