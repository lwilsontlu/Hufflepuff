package main;

import poker.*;
import deck.*;

public class TestPoker
{
    public static void main(String[] args)
    {
        PokerGame game = new PokerGame();

        // Adding Players
        Player p1 = new Player("Player 1");
        game.addPlayer(p1);

        Player p2 = new Player("Player 2");
        game.addPlayer(p2);

        // Both Players in, neither ready
        System.out.println("New Game:");
        System.out.println(game);

        // Players set to ready, so the Hands should be dealt
        System.out.println("\nPlayers ready.");
        p1.setReady(true);
        p2.setReady(true);

        System.out.println("Cards dealt:");
        System.out.println(game);

        // Swap these cards for testing purposes
        p1.setSwapCard(1, true);
        p1.setSwapCard(3, true);

        p2.setSwapCard(3, true);
        p2.setSwapCard(4, true);

        System.out.println("Swaps set:");
        System.out.println(game);

        System.out.println("\nPlayers ready.");
        p1.setReady(true);
        p2.setReady(true);

        // Round over, winner is decided
        System.out.println("Cards swapped:");
        System.out.println(game);
    }
}
