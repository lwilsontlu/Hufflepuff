package gui;

import deck.*;
import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.PrintWriter;

public class HandPanel extends JPanel
{
    private JLabel[] cardLabels;

    private ImageIcon backImage;

    private PokerPanel client;

    public HandPanel(PokerPanel client, boolean canControl)
    {
        this.client = client;

        cardLabels = new JLabel[Hand.NUM_CARDS_IN_HAND];

        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        backImage = new ImageIcon(
            this.getClass().getResource("/resources/b2fv.gif"));

        this.setLayout(new GridLayout(1, Hand.NUM_CARDS_IN_HAND));

        int i;
        CardClickPanel temp;
        for (i = 0; i < Hand.NUM_CARDS_IN_HAND; i++)
        {
            cardLabels[i] = new JLabel("Test");
            temp = new CardClickPanel(client, i, canControl);
            temp.add(cardLabels[i]);
            add(temp);
        }
    }

    public void clearHand()
    {
        for (int i = 0; i < cardLabels.length; i++)
        {
            cardLabels[i].setVisible(false);
        }
    }

    public void setHand(Player player, boolean hideCards)
    {
        for (int i = 0; i < cardLabels.length; i++)
        {
            if (i < player.getHand().size())
            {
                cardLabels[i].setVisible(true);

                if (hideCards || player.getSwapCard(i))
                {
                    cardLabels[i].setIcon(backImage);
                }
                else
                {
                    Card oldCard = player.getHand().get(i);
                    GraphicalCard card = new GraphicalCard(oldCard);
                    cardLabels[i].setIcon(card.getImage());
                }
            }
            else
            {
                cardLabels[i].setVisible(false);
            }
        }
    }

    private class CardClickPanel extends JPanel
    {
        public CardClickPanel(PokerPanel o, int c, boolean a)
        {
            final PokerPanel out = o;
            final int card = c;
            final boolean canControl = a;

            addMouseListener(new MouseAdapter()
            {
                public void MouseClicked(MouseEvent e)
                {
                    if (canControl)
                    {
                        out.sendCommand("swap " + card);
                    }
                }
            });
        }
    }
}
