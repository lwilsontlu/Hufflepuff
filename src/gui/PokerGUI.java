package gui;

import java.awt.*;
import javax.swing.*;

public class PokerGUI extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public PokerGUI(String server, int port)
    {
        super("Poker Client");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pokerPanel = new PokerPanel(server, port);
        add(pokerPanel);
    }
}
