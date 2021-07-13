package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.Behaviour;
import game.GameLocation;
import game.grounds.WaterBody;

import java.util.Objects;


/**
 * A class for dinosaurs to find water source and quench their thirst.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class ThirstyBehaviour implements Behaviour {

    /**
     * Decides which action to return so the target can move towards or drink from a water source,
     * based on:
     *
     * @param dinosaur The thirsty Dinosaur Actor
     * @param map      the GameMap containing the Dinosaur
     * @return null if dinosaur is not thirsty or water source location not found.
     * DrinkAction if at least one of dinosaur's exits is a water source.
     * Utilises FollowBehaviour to approach the water source otherwise.
     */
    @Override
    public Action getAction(Dinosaur dinosaur, GameMap map) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        if (!dinosaur.isThirsty())
            return null;

        // Iterates through the nearest to farthest locations from here
        for (Location destination : (GameLocation) map.locationOf(dinosaur)) {
            if (isNextToWaterSource(destination)) {
                if (map.locationOf(dinosaur).equals(destination))
                    return new DrinkAction(dinosaur, destination);
                else
                    return new FollowBehaviour(destination).getAction(dinosaur, map);
            }
        }

        return null;
    }

    /**
     * Checks if the location is next to a water source.
     *
     * @param location The location to be checked
     * @return true if location is next to a water source, false otherwise
     */
    private boolean isNextToWaterSource(Location location) {
        Objects.requireNonNull(location, "Expected Location type argument, but null is received.");

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.getGround() instanceof WaterBody && ((WaterBody) destination.getGround()).hasSips())
                return true;
        }

        return false;
    }
}
