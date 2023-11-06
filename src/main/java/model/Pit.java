package model;

/**
 * The abstract Pit class represents a pit in the game board.
 * It can be a regular pit or a large pit.
 */
public abstract class Pit {
    protected Player owner;    // The player who owns the pit
    protected int seeds;       // The number of seeds in the pit
    protected Pit nextPit;     // The next pit in the player's sequence

    /**
     * Constructs a Pit with an owner and an initial number of seeds.
     *
     * @param owner The player who owns the pit.
     * @param seeds The initial number of seeds in the pit.
     */
    public Pit(Player owner, int seeds) {
        this.owner = owner;
        this.seeds = seeds;
    }

    /**
     * Checks if the current player can put a seed in this pit.
     *
     * @param player The player who wants to put a seed.
     * @return true if the player can put a seed, false otherwise.
     */
    public abstract boolean canPutSeed(Player player);

    /**
     * Adds one seed to the pit.
     */
    public void putSeed() {
        this.seeds++;
    }

    /**
     * Checks if the pit is empty (contains no seeds).
     *
     * @return true if the pit is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.seeds == 0;
    }

    /**
     * Gets the next pit in the player's sequence.
     *
     * @return The next pit.
     */
    public Pit getNextPit() {
        return this.nextPit;
    }

    /**
     * Sets the next pit in the player's sequence.
     *
     * @param nextPit The next pit to set.
     */
    public void setNextPit(Pit nextPit) {
        this.nextPit = nextPit;
    }

    /**
     * Gets the owner of the pit.
     *
     * @return The owner of the pit.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Gets the number of seeds in the pit.
     *
     * @return The number of seeds in the pit.
     */
    public int getSeeds() {
        return seeds;
    }
}
