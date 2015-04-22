package gui;

import poker.PokerGame;
import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JoinLeavePanel extends JPanel
{
    private Player player;
    private PokerGame game;

    private JButton joinButton;
    private JButton leaveButton;

    public JoinLeavePanel(Player player, PokerGame game)
    {
        this.player = player;
        this.game = game;

        joinButton = new JButton("Join");
        leaveButton = new JButton("Leave");

        joinButton.addActionListener(
            new JoinButtonListener(player, joinButton, leaveButton, true));
        leaveButton.addActionListener(
            new JoinButtonListener(player, joinButton, leaveButton, false));
        leaveButton.setEnabled(false);

        add(joinButton);
        add(leaveButton);
    }

    private class JoinButtonListener implements ActionListener
    {
        private JButton joinButton;
        private JButton leaveButton;
        private Player player;
        private boolean join;

        public JoinButtonListener(Player player, JButton joinButton,
            JButton leaveButton, boolean join)
        {
            this.player = player;
            this.joinButton = joinButton;
            this.leaveButton = leaveButton;
            this.join = join;
        }

        public void actionPerformed(ActionEvent e)
        {
            System.out.println(1);
            if (join)
            {
                game.addPlayer(player);
                System.out.println(2);
            }
            else
            {
                game.removePlayer(player);
                System.out.println(3);
            }
            joinButton.setEnabled(!join);
            leaveButton.setEnabled(join);
            System.out.println(4);
        }
    }
}
