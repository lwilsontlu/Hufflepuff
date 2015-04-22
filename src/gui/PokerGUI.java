package gui;

import java.awt.*;
import javax.swing.*;

public class PokerGUI extends JFrame
{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public PokerGUI()
    {
        super("Poker Client");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pokerPanel = new PokerPanel();
        add(pokerPanel);
    }
}
