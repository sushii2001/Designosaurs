package game;

import edu.monash.fit2099.engine.Location;


/**
 * Provides logic to compute layer distance (how many steps an Actor needs to take to get there)
 * between 2 locations, assuming there is no obstacles in between.
 *
 * @author Ci Leong Ong
 */
public interface ComputeDistance {

    /**
     * Computes the layer distance between 2 locations.
     *
     * @param l1 Location 1
     * @param l2 Location 2
     * @return The layer distance between l1 & l2
     */
    default int distanceBetween(Location l1, Location l2) {
        return Math.max(Math.abs(l1.x()-l2.x()), Math.abs(l1.y()-l2.y()));
    }

}
