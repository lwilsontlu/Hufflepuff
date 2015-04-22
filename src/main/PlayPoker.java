/*
I hope this works to some degree
or is at least some what usable.
I tried to do what I could with 
what we already have. Let me know
if you guys want me to something
else. Again, I'm sorry I can't 
make it out today.
Good Luck!

-Gunnar
*/
import deck.*;
import poker.*;

import java.util.Scanner;

public class PlayPoker
{
    public static void main(String[] args)
    {
        final int NUM_CARDS = 5; 

        Player[] players;
        int numPlayers;
        int index = -1;
        int i; //Used for loops of players
        int j; //Used for loops of hands

        Scanner scan = new Scanner(System.in);
        DeckOfCards deck = new DeckOfCards();
        PokerGame game = new PokerGame();

        System.out.println("Welcome to Poker!\n");

        System.out.print("How many players will there be?(2 - 5) :");
        numPlayers = scan.nextInt();
        
        while(numPlayers < 3 || numPlayers > 5)
        {
            System.out.print("How many players will there be?(2 - 5) :");
            numPlayers = scan.nextInt();
        }

        players = new Player[numPlayers];
        //game.setPlayers(players);

        //Set usernames
        for (i = 0; i < numPlayers; i++)
        {
            System.out.print("What is your username player " 
                + (i + 1) + "? :");
            players[i].setUsername(scan.nextLine().trim());
        }
        
        //Deal Hands
        game.nextState();
        for (j = 0; j < NUM_CARDS; j++ )
        {
            for (i = 0; i < numPlayers; i++)
            {
                //players[i].setHand(players[i].getHand().add(deck.deal()));
            }
        }

        //Play first round
        game.nextState();
        for (i = 0; i < numPlayers; i++)
        {
            System.out.println("\nPlayer " + (i + 1) + ", it is your turn.");
            System.out.println("This is your hand");
            System.out.println(players[i].getHand());
            while(index != 0)
            {
                System.out.println("Which card would you like to swap out? :");
                System.out.println("(Type the index of the card you would");
                System.out.println("like to swap or O to stop swapping)");
            
                index = scan.nextInt();
                
                while (index < 0 || index > 5)
                {
                    System.out.println("Which card would you like to swap out? :");
                    index = scan.nextInt();
                }

                if (index != 0)
                {
                    //swap cards 
                }
            }
        }

        //Play second round
        game.nextState();
        for (i = 0; i < numPlayers; i++)
        {
            System.out.println("\nPlayer " + (i + 1) + ", it is your turn.");
            System.out.println("This is your hand");
            System.out.println(players[i].getHand());
            while(index != 0)
            {
                System.out.println("Which card would you like to swap out? :");
                index = scan.nextInt();

                while (index < 0 || index > 5)
                {
                    System.out.println("Which card would you like to swap out? :");
                    index = scan.nextInt();
                }

                if (index != 0)
                {
                    //swap cards 
                }
            }
        }
    }
}
