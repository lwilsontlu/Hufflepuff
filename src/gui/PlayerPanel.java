package gui;

import poker.PokerGame;
import poker.Player;

import java.util.Observer;
import java.util.Observable;
import java.awt.*;
import javax.swing.*;

public class PlayerPanel extends JPanel implements Observer
{
    public static final String UNJOINED_TEXT = "Unjoined";

    private PokerGame game;
    private Player player;
    private JLabel nameLabel;
    private HandPanel handPanel;
    private ReadyPanel readyPanel;
    private JoinLeavePanel joinLeavePanel;

    public PlayerPanel(PokerGame game)
    {
        this.game = game;
        player = new Player("Player");

        setLayout(new GridLayout(1, 4));

        nameLabel = new JLabel(UNJOINED_TEXT);
        handPanel = new HandPanel(player);
        readyPanel = new ReadyPanel(player);
        joinLeavePanel = new JoinLeavePanel(player, game);

        add(nameLabel);
        add(handPanel);
        add(readyPanel);
        add(joinLeavePanel);
    }

    public void setPlayer(Player p)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void update(Observable o, Object arg)
    {
        if (player != null)
        {
            nameLabel.setText(player.getUsername());
        }
        else
        {
            nameLabel.setText(UNJOINED_TEXT);
        }
    }
}
