package model;

/**
 * The LargePit class represents a large pit in the game board.
 * Large pits are owned by players and can store multiple seeds.
 */
public class LargePit extends Pit {
    /**
     * Constructs a LargePit with an owner and initializes it with zero seeds.
     *
     * @param owner The player who owns the large pit.
     */
    public LargePit(Player owner) {
        super(owner, 0);
    }

    /**
     * Checks if the current player can put a seed in this large pit.
     * Large pits can only be used by their respective owners.
     *
     * @param player The player who wants to put a seed.
     * @return true if the player can put a seed, false otherwise.
     */
    @Override
    public boolean canPutSeed(Player player) {
        return player.equals(owner);
    }

    /**
     * Adds multiple seeds to the large pit.
     *
     * @param seeds The number of seeds to add.
     */
    public void putMultipleSeeds(int seeds) {
        this.seeds += seeds;
    }
}
