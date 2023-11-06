package model;

import common.GameConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PitTest {
    private Player firstPlayer;
    private Player secondPlayer;

    @BeforeEach
    public void initPlayers(){
        firstPlayer = new Player("FirstPlayer");
        secondPlayer = new Player("SecondPlayer");
    }

    @Test
    public void shouldBeAbleToPutSeedsInMyOpponentRegularPits(){
        RegularPit regularPit = firstPlayer.getRegularPits().get(0);
        assertTrue(regularPit.canPutSeed(firstPlayer));
        assertTrue(regularPit.canPutSeed(secondPlayer));
    }

    @Test
    public void shouldNotBeAbleToPutSeedsInMyOpponentLargePit(){
        LargePit largePit = firstPlayer.getLargePit();
        assertTrue(largePit.canPutSeed(firstPlayer));
        assertFalse(largePit.canPutSeed(secondPlayer));
    }

    @Test
    public void shouldBeAbleToPutOneSeedInPits(){
        RegularPit regularPit = firstPlayer.getRegularPits().get(0);
        regularPit.putSeed();
        assertEquals(5, regularPit.getSeeds());
    }

    @Test
    public void shouldBeAbleToPutMultipleSeedsInLargePits(){
        LargePit largePit = firstPlayer.getLargePit();
        largePit.putMultipleSeeds(3);
        assertEquals(3, largePit.getSeeds());
    }

    @Test
    public void shouldBeAbleToPickupSeedsFromRegularPits(){
        RegularPit regularPit = firstPlayer.getRegularPits().get(0);
        int seeds = regularPit.pickupSeeds();
        assertEquals(seeds, GameConstants.SEEDS_PER_PIT);
        assertEquals(0, regularPit.getSeeds());
    }

    @Test
    public void shouldBeAbleToCheckIfEmpty(){
        RegularPit regularPit = new RegularPit(firstPlayer, 0);
        assertTrue(regularPit.isEmpty());

        regularPit.putSeed();
        assertFalse(regularPit.isEmpty());
    }
}
