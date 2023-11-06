package model;

import common.GameConstants;

import java.util.LinkedList;
/**
 * Represents a player in the Mancala game.
 */
public class Player {
    /**
     * The name of the player, used to identify them.
     */
    private final String name;
    /**
     * A linked list of regular pits owned by the player.
     */
    private LinkedList<RegularPit> regularPits;
    /**
     * The player's large pit for collecting captured seeds.
     */
    private final LargePit largePit;
    /**
     * Constructs a `Player` object with the specified name and initializes their pits.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.initRegularPits();
        this.largePit = new LargePit(this);
    }
    /**
     * Initializes the player's regular pits with the specified number of seeds.
     */
    private void initRegularPits() {
        this.regularPits = new LinkedList<>();
        regularPits.addLast(new RegularPit(this, GameConstants.SEEDS_PER_PIT));
        while (regularPits.size() < GameConstants.PITS_PER_PLAYER) {
            RegularPit newPit = new RegularPit(this, GameConstants.SEEDS_PER_PIT);
            regularPits.getLast().setNextPit(newPit);
            regularPits.addLast(newPit);
        }
    }
    /**
     * Takes a turn and sows seeds from the selected regular pit.
     *
     * @param selectedPit The regular pit from which seeds will be sown.
     * @return The pit where the last seed was sown during the turn.
     */
    public Pit takeTurn(RegularPit selectedPit){
        int seeds = selectedPit.pickupSeeds();
        Pit currentPit = selectedPit;

        while (seeds > 0) {
            currentPit = currentPit.getNextPit();
            if (currentPit.canPutSeed(this)) {
                seeds--;
                currentPit.putSeed();
            }
        }
        return currentPit;
    }
    /**
     * Checks if the player can capture seeds from a regular pit.
     *
     * @param endPit The regular pit from which seeds may be captured.
     * @return `true` if seeds can be captured, `false` otherwise.
     */
    public boolean canCapture(RegularPit endPit){
        return endPit.getOwner().equals(this)
                && endPit.getSeeds() == 1
                && !endPit.getOppositePit().isEmpty();
    }
    /**
     * Captures seeds from an opponent's regular pit when specific conditions are met.
     *
     * @param endPit The regular pit from which seeds will be captured.
     */
    public void capture(RegularPit endPit){
        largePit.putMultipleSeeds(
                endPit.pickupSeeds() +
                endPit.getOppositePit().pickupSeeds());
    }
    /**
     * Checks if the player has no seeds left in their regular pits.
     *
     * @return `true` if there are no seeds left, `false` otherwise.
     */
    public boolean noSeedsLeft(){
        for (Pit pit : regularPits) {
            if(pit.seeds > 0){
                return false;
            }
        }

        return true;
    }
    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the player's regular pits.
     *
     * @return A linked list of regular pits owned by the player.
     */
    public LinkedList<RegularPit> getRegularPits() {
        return regularPits;
    }
    /**
     * Gets the player's large pit.
     *
     * @return The player's large pit for collecting captured seeds.
     */
    public LargePit getLargePit() {
        return largePit;
    }

    /**
     * Compares this player with another object for equality.
     *
     * @param obj The object to compare with this player.
     * @return `true` if the object is a player with the same name, `false` otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Player player){
            return this.name.equals(player.name);
        }

        return false;
    }
}
