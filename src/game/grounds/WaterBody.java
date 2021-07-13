package game.grounds;


/**
 * Added to Grounds with the ability to collect water,
 * and allow Actors to drink from.
 *
 * @author Ci Leong Ong
 */
public interface WaterBody {

    /**
     * Return true if the water body allows Actors to drink from.
     *
     * @return true if the water body allows Actors to drink from, otherwise false
     */
    boolean hasSips();

    /**
     * Add amount of sips to the pool of water.
     *
     * @param amount Amount of sips to be added to the ground.
     */
    void addSips(int amount);

    /**
     * Decrement number of sips by 1.
     */
    void decrementSips();

}
