package gui;

import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class ReadyPanel extends JPanel
{
    private Player player;

    public ReadyPanel(Player player)
    {
        this.player = player;

        add(new ReadyCheckBox());
    }

    private class ReadyCheckBox extends JCheckBox implements Observer
    {
        public ReadyCheckBox()
        {
            addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    player.setReady(!player.isReady());
                }
            });

            player.addObserver(this);
        }

        public void update(Observable o, Object arg)
        {
            setSelected(player.isReady());
        }
    }
}
