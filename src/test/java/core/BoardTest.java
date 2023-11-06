package core;

import model.Player;
import model.RegularPit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    private Board board;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final String firstPlayerName = "Player 1";
    private final String secondPlayerName = "Player 2";

    @BeforeEach
    public void setUp() {
        board = Board.create(firstPlayerName, secondPlayerName);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void playerNamesShouldBeInitializedCorrectly() {
        assertEquals(firstPlayerName, board.getFirstPlayer().getName());
        assertEquals(secondPlayerName, board.getSecondPlayer().getName());
    }

    @Test
    public void oppositePitsShouldBeConnected() {
        LinkedList<RegularPit> firstPlayerPits = board.getFirstPlayer().getRegularPits();
        LinkedList<RegularPit> secondPlayerPits = board.getSecondPlayer().getRegularPits();

        IntStream.range(0, firstPlayerPits.size())
            .forEach((index) -> {
                RegularPit currentPit = firstPlayerPits.get(index);
                RegularPit oppositePit = secondPlayerPits
                        .get(secondPlayerPits.size() - index - 1);

                assertEquals(currentPit.getOppositePit(), oppositePit);
                assertEquals(oppositePit.getOppositePit(), currentPit);
            });
    }

    @Test
    public void pitsShouldBeCircular(){
        Player firstPlayer = board.getFirstPlayer();
        Player secondPlayer = board.getSecondPlayer();

        // firstPlayer: Last regular pit should point to the large pit
        assertEquals(firstPlayer.getLargePit(),
                firstPlayer.getRegularPits().getLast().getNextPit());

        // firstPlayer: Large pit should point to the first regular pit of secondPlayer
        assertEquals(secondPlayer.getRegularPits().getFirst(),
                firstPlayer.getLargePit().getNextPit());

        // secondPlayer: Last regular pit should point to the large pit
        assertEquals(secondPlayer.getLargePit(),
                secondPlayer.getRegularPits().getLast().getNextPit());

        // secondPlayer: Large pit should point to the first regular pit of firstPlayer
        assertEquals(firstPlayer.getRegularPits().getFirst(),
                secondPlayer.getLargePit().getNextPit());
    }

    @Test
    public void shouldBeAbleToPrettyPrint() {
        String expectedOutput = """
                               Player 2
                     | 4 | 4 | 4 | 4 | 4 | 4 |
                (0)                             (0)
                     | 4 | 4 | 4 | 4 | 4 | 4 |
                               Player 1
                \n""";

        board.prettyPrint();
        assertEquals(expectedOutput, outContent.toString());
    }
}
