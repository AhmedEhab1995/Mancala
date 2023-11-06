package core;

import model.Pit;
import model.Player;
import model.RegularPit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The `Board` class represents the game board used in the Mancala game, where two players compete.
 * It facilitates the initialization of the game board, connects opposite pits, and manages the circular
 * connections for seed sowing. Additionally, it provides a method to visually print the current state
 * of the game board.
 */
public class Board {
    private Player firstPlayer; // Represents the first player on the game board.
    private Player secondPlayer; // Represents the second player on the game board.
    /**
     * Private constructor to restrict external instantiation. Instances of the `Board` class are created
     * using the `create` method.
     */
    private Board() {}
    /**
     * Creates and initializes a new game board with two players.
     *
     * @param firstPlayerName  The name of the first player.
     * @param secondPlayerName The name of the second player.
     * @return A new game board with initialized players and connections between pits.
     */
    public static Board create(String firstPlayerName, String secondPlayerName) {
        Board board = new Board();
        board.firstPlayer = new Player(firstPlayerName);
        board.secondPlayer = new Player(secondPlayerName);

        board.connectOppositePits();
        board.formCycle();
        return board;
    }
    /**
     * Connects opposite pits of the first player to the corresponding pits of the second player.
     * This connection is crucial for capturing seeds during the game.
     */
    private void connectOppositePits() {
        IntStream.range(0, firstPlayer.getRegularPits().size())
            .forEach((index) -> {
                RegularPit currentPit = firstPlayer.getRegularPits().get(index);
                RegularPit opposite = secondPlayer.getRegularPits()
                        .get(secondPlayer.getRegularPits().size() - index - 1);
                currentPit.setOppositePit(opposite);
                opposite.setOppositePit(currentPit);
            });
    }
    /**
     * Forms a circular connection between pits to enable the circular movement of seeds during the game.
     */
    private void formCycle() {
        firstPlayer.getRegularPits().getLast().setNextPit(firstPlayer.getLargePit());
        firstPlayer.getLargePit().setNextPit(secondPlayer.getRegularPits().getFirst());
        secondPlayer.getRegularPits().getLast().setNextPit(secondPlayer.getLargePit());
        secondPlayer.getLargePit().setNextPit(firstPlayer.getRegularPits().getFirst());
    }
    /**
     * Prints a visual representation of the current game board's state, including the seeds in pits.
     */
    public void prettyPrint(){
        List<Pit> secondPlayerPits = new ArrayList<>(secondPlayer.getRegularPits());
        Collections.reverse(secondPlayerPits);

        List<Pit> pits = new ArrayList<>(secondPlayerPits);
        pits.add(secondPlayer.getLargePit());
        pits.add(firstPlayer.getLargePit());
        pits.addAll(firstPlayer.getRegularPits());

        List<String> values = new ArrayList<>();
        values.add(secondPlayer.getName());
        values.addAll(pits.stream()
                .map(pit -> String.valueOf(pit.getSeeds()))
                .toList());
        values.add(firstPlayer.getName());

        System.out.printf("""
                               %s
                     | %s | %s | %s | %s | %s | %s |
                (%s)                             (%s)
                     | %s | %s | %s | %s | %s | %s |
                               %s
                \n""", values.toArray());
    }
    /**
     * Retrieves the first player on the game board.
     *
     * @return The first player.
     */
    public Player getFirstPlayer() {
        return firstPlayer;
    }
    /**
     * Retrieves the second player on the game board.
     *
     * @return The second player.
     */
    public Player getSecondPlayer() {
        return secondPlayer;
    }
}
