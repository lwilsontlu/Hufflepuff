package gui;

import deck.*;
import poker.Player;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class HandPanel extends JPanel implements Observer
{
    private Player player;
    private JLabel[] cardLabels;

    private ImageIcon backImage;

    public HandPanel(Player player)
    {
        this.player = player;
        cardLabels = new JLabel[Hand.NUM_CARDS_IN_HAND];

        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        backImage = new ImageIcon(
            this.getClass().getResource("/resources/b2fv.gif"));

        this.setLayout(new GridLayout(1, Hand.NUM_CARDS_IN_HAND));

        int i;
        CardClickPanel temp;
        for (i = 0; i < Hand.NUM_CARDS_IN_HAND; i++)
        {
            cardLabels[i] = new JLabel("test");
            temp = new CardClickPanel(player, i);
            temp.add(cardLabels[i]);
            add(temp);
        }

        player.addObserver(this);
    }

    public void update(Observable o, Object arg)
    {
        int i;
        GraphicalCard card;
        for (i = 0; i < Hand.NUM_CARDS_IN_HAND; i++)
        {
            card = (GraphicalCard) player.getHand().get(i);
            if (card != null)
            {
                cardLabels[i].setVisible(true);

                if (player.getSwapCard(i))
                {
                    cardLabels[i].setIcon(backImage);
                }
                else
                {
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
        public CardClickPanel(Player p, int c)
        {
            final Player player = p;
            final int card = c;

            addMouseListener(new MouseAdapter()
            {
                public void MouseClicked(MouseEvent e)
                {
                    player.setSwapCard(card, !player.getSwapCard(card));
                }
            });
        }
    }
}
