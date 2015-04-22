package gui;

import poker.PokerGame;

import java.awt.*;
import javax.swing.*;

public class PokerPanel extends JPanel
{
    private PokerGame game;

    public PokerPanel()
    {
        game = new PokerGame();

        setLayout(new GridLayout(PokerGame.MAX_NUM_PLAYERS, 1));

        int i;
        for (i = 0; i < PokerGame.MAX_NUM_PLAYERS; i++)
        {
            add(new PlayerPanel(game));
        }
    }
}
