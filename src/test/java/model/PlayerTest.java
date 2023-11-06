package model;

import common.GameConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerTest {
    private Player activePlayer;
    private Player opponent;

    @BeforeEach
    public void setUp() {
        activePlayer = new Player("ActivePlayer");
        opponent = new Player("Opponent");
    }

    @Test
    public void playerShouldBeInitializedCorrectly(){
        assertEquals("ActivePlayer", activePlayer.getName());
        assertEquals(GameConstants.PITS_PER_PLAYER, activePlayer.getRegularPits().size());

        // Verify the initialization of the regular pits
        for(RegularPit regularPit : activePlayer.getRegularPits()){
            assertNotNull(regularPit);
            assertEquals(GameConstants.SEEDS_PER_PIT, regularPit.getSeeds());
            assertEquals(activePlayer, regularPit.getOwner());

            // All pits (except the last) should point to the next regular pit
            if(!regularPit.equals(activePlayer.getRegularPits().getLast())){
                Pit nextPit = regularPit.getNextPit();
                assertNotNull(nextPit);
                assertTrue(nextPit instanceof RegularPit);
                assertEquals(activePlayer, nextPit.getOwner());
            }
        }

        // Verify the initialization of the large pit
        assertNotNull(activePlayer.getLargePit());
        assertEquals(activePlayer, activePlayer.getLargePit().getOwner());
    }

    @Test
    public void shouldBeAbleToTakeTurn(){
        // Play from the first pit
        LinkedList<RegularPit> regularPits = activePlayer.getRegularPits();
        Pit endPit = activePlayer.takeTurn(regularPits.get(0));

        // The last pit should be a regular pit owned by the player
        assertTrue(endPit instanceof RegularPit);
        assertEquals(activePlayer, endPit.getOwner());

        // Verify the seeds in the selected pit and the following pits
        assertTrue(regularPits.get(0).isEmpty());
        IntStream.range(1, GameConstants.SEEDS_PER_PIT + 1)
            .forEach(index -> assertEquals(
                    GameConstants.SEEDS_PER_PIT + 1,
                    regularPits.get(1).getSeeds()));
    }

    @Test
    public void shouldNotBeAbleToCaptureWhenEndPitBelongsToOpponent(){
        RegularPit endPit = new RegularPit(opponent, GameConstants.SEEDS_PER_PIT);
        assertFalse(activePlayer.canCapture(endPit));
    }

    @Test
    public void shouldNotBeAbleToCaptureWhenEndPitWasNotEmpty(){
        RegularPit endPit = new RegularPit(activePlayer, GameConstants.SEEDS_PER_PIT);
        assertFalse(activePlayer.canCapture(endPit));
    }

    @Test
    public void shouldNotBeAbleToCaptureWhenEndPitHasAnEmptyOppositePit(){
        RegularPit endPit = new RegularPit(activePlayer, 1);
        endPit.setOppositePit(new RegularPit(opponent, 0));
        assertFalse(activePlayer.canCapture(endPit));
    }

    @Test
    public void shouldBeAbleToCapture(){
        RegularPit endPit = new RegularPit(activePlayer, 1);
        RegularPit oppositePit = new RegularPit(opponent, GameConstants.SEEDS_PER_PIT);
        endPit.setOppositePit(oppositePit);
        assertTrue(activePlayer.canCapture(endPit));

        activePlayer.capture(endPit);
        assertTrue(endPit.isEmpty());
        assertTrue(oppositePit.isEmpty());
        assertEquals(GameConstants.SEEDS_PER_PIT + 1,
                activePlayer.getLargePit().getSeeds());
    }

    @Test
    public void shouldBeAbleToCheckIfNoSeedsAreLeft(){
        // Clear all regular pits
        for (RegularPit regularPit : activePlayer.getRegularPits()){
            regularPit.pickupSeeds();
        }

        assertTrue(activePlayer.noSeedsLeft());
        activePlayer.getRegularPits().get(0).putSeed();
        assertFalse(activePlayer.noSeedsLeft());
    }

    @Test
    public void shouldBeAbleToCheckIfPlayersAreEqual(){
        assertNotEquals(activePlayer, opponent);
        assertNotEquals(activePlayer, new Object());

        activePlayer = new Player("Test");
        opponent = new Player("Test");
        assertEquals(activePlayer, opponent);
    }
}
