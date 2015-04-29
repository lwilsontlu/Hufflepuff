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

        JPanel namePanel = new JPanel();
        namePanel.setPreferredSize(new Dimension(100, 100));
        namePanel.setLayout(new GridLayout());
        nameLabel = new JLabel(UNJOINED_TEXT);
        namePanel.add(nameLabel);

        handPanel = new HandPanel(client, canControl);

        readyPanel = new ReadyPanel(client, canControl);

        add(namePanel, BorderLayout.WEST);
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

    public void clearName()
    {
        nameLabel.setText(UNJOINED_TEXT);
        joined = false;
        handPanel.clearHand();
        setVisible(false);
    }

    public void setName(String name)
    {
        nameLabel.setText(name);
        joined = true;
        setVisible(true);
    }

    public void setReady(boolean ready)
    {
        readyPanel.setReady(ready);
    }

    public void setCard(int index, int rank)
    {
        handPanel.setCard(index, rank);
    }
}
