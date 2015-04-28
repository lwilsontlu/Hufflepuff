package gui;

import poker.Player;

import java.awt.*;
import javax.swing.*;
import java.io.PrintWriter;

public class PlayerPanel extends JPanel
{
    private static final String UNJOINED_TEXT = "Unjoined";

    private JLabel nameLabel;
    private HandPanel handPanel;
    private ReadyPanel readyPanel;
    private boolean joined;

    public PlayerPanel(PokerPanel client, boolean canControl)
    {
        setLayout(new BorderLayout());

        joined = false;

        nameLabel = new JLabel(UNJOINED_TEXT);

        handPanel = new HandPanel(client, canControl);

        readyPanel = new ReadyPanel(client, canControl);

        add(nameLabel, BorderLayout.WEST);
        add(handPanel, BorderLayout.CENTER);
        add(readyPanel, BorderLayout.EAST);
    }

    public String getName()
    {
        if (joined)
            return nameLabel.getText();
        else
            return null;
    }

    public void clearPlayer()
    {
        if (joined)
            nameLabel.setText(UNJOINED_TEXT);
        joined = false;
        handPanel.clearHand();
    }

    public void setPlayer(Player player, boolean hideCards)
    {
        joined = true;
        nameLabel.setText(player.getUsername());
        readyPanel.setReady(player.isReady());
        handPanel.setHand(player, hideCards);
    }
    
    public void setPlayer(Player player)
    {
        setPlayer(player, false);
    }
}
