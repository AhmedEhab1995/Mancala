package core;

import common.GameConstants;
import exception.EmptyPitSelectedException;
import exception.InvalidPitNumberException;
import model.Pit;
import model.RegularPit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameTest {
    @Mock
    private ConsoleInputReader mockInputReader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock user input for selecting player names
        when(mockInputReader.readLine(ConsoleInputReader.InputType.FIRST_PLAYER_NAME))
                .thenReturn("Player 1");
        when(mockInputReader.readLine(ConsoleInputReader.InputType.SECOND_PLAYER_NAME))
                .thenReturn("Player 2");
    }

    @Test
    public void newGamesShouldBeInitializedCorrectly(){
        Game game = Game.create(mockInputReader);
        assertNotNull(game.getBoard());
        assertNotNull(game.getActivePlayer());
        assertEquals(game.getBoard().getFirstPlayer(), game.getActivePlayer());
    }

    @Test
    public void selectingNonNumericPitNumberShouldThrowAnException() {
        Game game = Game.create(mockInputReader);
        assertThrows(InvalidPitNumberException.class, () -> game.validateMove("x"));
    }

    @Test
    public void selectingTooLargePitNumberShouldThrowAnException() {
        Game game = Game.create(mockInputReader);
        assertThrows(InvalidPitNumberException.class, () -> game.validateMove("10"));
    }

    @Test
    public void selectingTooSmallPitNumberShouldThrowAnException() {
        Game game = Game.create(mockInputReader);
        assertThrows(InvalidPitNumberException.class, () -> game.validateMove("0"));
    }

    @Test
    public void selectingAnEmptyPitShouldThrowAnException() {
        Game game = Game.create(mockInputReader);

        // Clear the first regular pit of the active player
        game.getActivePlayer()
                .getRegularPits()
                .getFirst()
                .pickupSeeds();

        assertThrows(EmptyPitSelectedException.class, () -> game.validateMove("1"));
    }

    @Test
    public void selectingFirstPitShouldNotThrowAnException() {
        Game game = Game.create(mockInputReader);
        assertDoesNotThrow(() -> game.validateMove("1"));
    }

    @Test
    public void selectingLastPitShouldNotThrowAnException() {
        Game game = Game.create(mockInputReader);
        assertDoesNotThrow(() -> game.validateMove(
                String.valueOf(GameConstants.PITS_PER_PLAYER)));
    }

    @Test
    public void selectingValidPitNumberShouldReturnTheSelectedPit() {
        Game game = Game.create(mockInputReader);

        // Mock user input for selecting a pit
        when(mockInputReader.readLine(ConsoleInputReader.InputType.PIT_NUMBER))
                .thenReturn("1");

        assertEquals(game.getActivePlayer().getRegularPits().getFirst(),
                game.askActivePlayerToPlay());
    }

    @Test
    public void gameShouldEndInDrawWhenBothPlayersHaveTheSameScore(){
        Game game = Game.create(mockInputReader);

        // Clear the first player's pits to simulate the game ending
        game.getActivePlayer().getRegularPits()
                .forEach(RegularPit::pickupSeeds);

        // Game should end in a draw since the large pits were not touched
        assertEquals(GameResult.DRAW, game.start());
    }

    @Test
    public void firstPlayerShouldWinWhenHavingHigherScore(){
        Game game = Game.create(mockInputReader);

        // Clear the first player's pits to simulate the game ending
        game.getActivePlayer().getRegularPits()
                .forEach(RegularPit::pickupSeeds);

        // Increase the seeds in the first player's large pit
        game.getActivePlayer().getLargePit().putSeed();

        // Game should end in a draw since the large pits were not touched
        assertEquals(GameResult.FIRST_PLAYER_WON, game.start());
    }

    @Test
    public void secondPlayerShouldWinWhenHavingHigherScore(){
        Game game = Game.create(mockInputReader);

        // Clear the first player's pits to simulate the game ending
        game.getActivePlayer()
                .getRegularPits()
                .forEach(RegularPit::pickupSeeds);

        // Increase the seeds in the first player's large pit
        game.getBoard()
            .getSecondPlayer()
            .getLargePit()
            .putSeed();

        // Game should end in a draw since the large pits were not touched
        assertEquals(GameResult.SECOND_PLAYER_WON, game.start());
    }

    @Test
    public void currentPlayerShouldGetAnotherTurnWhenLandingInHisOwnLargePit(){
        Game game = Game.create(mockInputReader);
        Pit endPit = game.getActivePlayer().getLargePit();
        assertEquals(game.getActivePlayer(), game.nextPlayer(endPit));
    }

    @Test
    public void currentPlayerShouldNotGetAnotherTurnWhenLandingInHisOwnRegularPit(){
        Game game = Game.create(mockInputReader);
        Pit endPit = game.getActivePlayer()
                .getRegularPits()
                .getFirst();
        assertEquals(game.getBoard().getSecondPlayer(), game.nextPlayer(endPit));
    }

    @Test
    public void currentPlayerShouldNotGetAnotherTurnWhenLandingInOpponentRegularPit(){
        Game game = Game.create(mockInputReader);
        Pit endPit = game.getBoard()
                .getSecondPlayer()
                .getRegularPits()
                .getFirst();
        assertEquals(game.getBoard().getSecondPlayer(), game.nextPlayer(endPit));
    }

    @Test
    public void shouldGetCorrectOpponent(){
        Game game = Game.create(mockInputReader);
        assertEquals(game.getBoard().getSecondPlayer(),
                game.getOpponent(game.getBoard().getFirstPlayer()));
        assertEquals(game.getBoard().getFirstPlayer(),
                game.getOpponent(game.getBoard().getSecondPlayer()));
    }
}
