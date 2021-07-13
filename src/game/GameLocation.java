package game;

import edu.monash.fit2099.engine.*;
import game.grounds.Bush;
import game.grounds.Dirt;

import java.util.*;


/**
 * Represents a Location in the map.
 * Extends from Location class to update the Location on each turn,
 * based on customised set of rules.
 *
 * Provides an iterator for looping through Locations from nearest to farthest from here,
 * achieved using Breadth First Search (BFS) algorithm.
 *
 * @author Ci Leong Ong
 */
public class GameLocation extends Location implements Iterable<Location> {

    /**
     * An Iterator over the Location from nearest to farthest from the current
     * Location. Achieved using Breadth First Search (BFS) algorithm.
     *
     * @author Ci Leong Ong
     */
    private static class GameLocationIterator implements Iterator<Location> {

        /**
         * Stores in order the Location that will be visited.
         */
        private final Queue<Location> queue;

        /**
         * Tracks visited Locations, and also Locations added to the queue,
         * as the graph contains cycles.
         */
        private final Set<Location> visited;

        /**
         * The Actor standing on this Location.
         */
        private final Actor actorHere;

        /**
         * Constructor.
         *
         * @param location Location here
         * @param actor    Actor that is standing on this Location
         */
        public GameLocationIterator(Location location, Actor actor) {
            queue = new LinkedList<>();
            queue.add(location);
            visited = new HashSet<>();
            actorHere = actor;
        }

        /**
         * Returns true if there is unexplored Locations left.
         * (The map is not fully explored yet)
         *
         * @return true if there is unexplored Locations left. false otherwise.
         */
        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        /**
         * Returns the next closest Location from here.
         * Computed using BFS.
         *
         * @return The next closest Location.
         */
        @Override
        public Location next() {
            if (!hasNext())
                return null;

            Location location = queue.remove();
            visited.add(location);
            for (Exit exit : location.getExits()) {
                Location destination = exit.getDestination();
                if (!visited.contains(destination)
                        && (Objects.isNull(actorHere) || destination.getGround().canActorEnter(actorHere))) {
                    queue.add(destination);
                    visited.add(destination);
                }
            }

            return location;
        }
    }

    /**
     * Used to simulate probability.
     */
    private final Random rand = new Random();

    /**
     * Constructor.
     *
     * @param map The map that contains this location
     * @param x x coordinate of this location within the map
     * @param y y coordinate of this location within the map
     */
    public GameLocation(GameMap map, int x, int y) {
        super(map, x, y);
    }

    /**
     * Returns an iterator over Locations from nearest to farthest from here.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Location> iterator() {
        return new GameLocationIterator(this, getActor());
    }

    /**
     * Called once per turn, to update the Ground terrain,
     * and also ticks everything that is in the Location.
     * Overridden to update the Ground type on each Location by defined rules.
     */
    @Override
    public void tick() {
        updateLocation();
        super.tick();
    }

    /**
     * Updates this Location based on the defined set of rules.
     */
    void updateLocation() {
        if (getGround().hasCapability(GroundCapability.FERTILE) && countNeighbour(GroundCapability.BLOCKS_SHORT_PLANT_GROWTH) == 0) {
            if ((countNeighbour(GroundCapability.SHORT_PLANT)>=2 && rand.nextDouble()<=0.1) || rand.nextDouble() <= 0.01)
                setGround(new Bush());
        } else if (Objects.nonNull(getActor()) &&
                getActor().hasCapability(ActorCapability.CRUSH_GROUND) &&
                getGround().hasCapability(GroundCapability.CRUSHABLE) && rand.nextBoolean())
            setGround(new Dirt());
    }

    /**
     * Count number of neighbouring Locations with Ground with target capability.
     *
     * @param capability An enumeration used to identify Grounds
     * @return Number of neighbouring Locations with Ground that has target capability.
     */
    private int countNeighbour(Enum<?> capability) {
        Objects.requireNonNull(capability);
        return (int) getExits().stream().map(exit -> exit.getDestination().getGround())
                .filter(ground -> ground.hasCapability(capability)).count();
    }

}
