package model;

/**
 * The RegularPit class represents a regular pit in the game board.
 */
public class RegularPit extends Pit {
    private RegularPit oppositePit;    // The opposite regular pit in the board.

    /**
     * Constructs a RegularPit with an owner and an initial number of seeds.
     *
     * @param owner The player who owns the pit.
     * @param seeds The initial number of seeds in the pit.
     */
    public RegularPit(Player owner, int seeds) {
        super(owner, seeds);
    }

    /**
     * Picks up all the seeds from the pit.
     *
     * @return The number of seeds picked up.
     */
    public int pickupSeeds() {
        int currentSeeds = this.seeds;
        this.seeds = 0;
        return currentSeeds;
    }

    /**
     * Checks if the current player can put a seed in this pit.
     * Regular pits always allow seed placement.
     *
     * @param player The player who wants to put a seed.
     * @return true for regular pits.
     */
    @Override
    public boolean canPutSeed(Player player) {
        return true;
    }

    /**
     * Gets the opposite regular pit in the board.
     *
     * @return The opposite regular pit.
     */
    public RegularPit getOppositePit() {
        return oppositePit;
    }

    /**
     * Sets the opposite regular pit in the board.
     *
     * @param oppositePit The opposite regular pit to set.
     */
    public void setOppositePit(RegularPit oppositePit) {
        this.oppositePit = oppositePit;
    }
}
