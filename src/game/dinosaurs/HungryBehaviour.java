package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.Behaviour;
import game.GameLocation;
import game.grounds.SearchableGround;

import java.util.Objects;


/**
 * A class that makes dinosaurs move to a food source and eat it.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class HungryBehaviour implements Behaviour {

    /**
     * Decides which action to return based on the attributes of the
     *
     * @param dinosaur The Actor acting.
     * @param map      The GameMap containing the Actor.
     * @return null if dinosaur is not hungry or no food source found,
     * EatItemOnGroundAction if the food source found is on ground,
     * EatFromSearchableGroundAction if the food source found is in plant.
     */
    @Override
    public Action getAction(Dinosaur dinosaur, GameMap map) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        if (!dinosaur.isHungry())
            return null;

        // Iterates through locations from nearest to the farthest from here
        for (Location destination : (GameLocation) map.locationOf(dinosaur)) {
            if (isOnGroundFood(dinosaur, destination)) {
                if (map.locationOf(dinosaur).equals(destination))
                    return new EatItemOnGroundAction(dinosaur);
                else
                    return new FollowBehaviour(destination).getAction(dinosaur, map);
            }

            if (isInGroundFood(dinosaur, destination)) {
                if (map.locationOf(dinosaur).equals(destination))
                    return new EatFromSearchableGroundAction(dinosaur);
                else
                    return new FollowBehaviour(destination).getAction(dinosaur, map);
            }
        }

        return null;
    }

    /**
     * Checks whether the target location contains at least one valid food for the dinosaur.
     *
     * @param dinosaur The Dinosaur used to check for valid food
     * @param location The Location to check if food exists on Ground
     * @return true if there is valid food for the dinosaur, false otherwise
     */
    private boolean isOnGroundFood(Dinosaur dinosaur, Location location) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(location, "Expected Location type argument, but null is received.");

        for (Item item : location.getItems()) {
            if (dinosaur.isSafeToEatFrom(location) && dinosaur.getValidFoods().contains(item.getClass()))
                return true;
        }

        return false;
    }

    /**
     * Checks whether the target location ground is a valid feeding source for the dinosaur.
     *
     * @param dinosaur The Dinosaur used to check for valid food
     * @param location The Location to check if food exists within the Ground
     * @return true if there is valid food for the dinosaur, false otherwise
     */
    private boolean isInGroundFood(Dinosaur dinosaur, Location location) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(location, "Expected Location type argument, but null is received.");

        return location.getGround() instanceof SearchableGround
                && ((SearchableGround) location.getGround()).getFoodPoints(dinosaur, true) != null;
    }

}