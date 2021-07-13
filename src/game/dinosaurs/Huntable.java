package game.dinosaurs;

import java.util.Map;


/**
 * An interface of which all hunting Actors implement.
 *
 * @author Ci Leong Ong
 */
public interface Huntable {

    /**
     * Returns the blacklist that maps the ID of the prey the hunter has attacks,
     * to the number of turns since the hunter last attacked it.
     *
     * @return The blacklist
     */
    Map<Integer, Integer> getBlacklist();

    /**
     * Adds the attacked prey to the blacklist.
     * Called after the hunter has attacked its prey.
     *
     * @param id The ID of the prey
     */
    void addToBlacklist(int id);

}
