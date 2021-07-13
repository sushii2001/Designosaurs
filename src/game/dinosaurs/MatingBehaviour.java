package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.*;

import java.util.Objects;


/**
 * A class that figures out the actions of two compatible dinosaurs
 * when they are available to mate.
 *
 * @author Toh Zhen Kang
 * @author Ci Leong Ong
 */
public class MatingBehaviour implements Behaviour {

    /**
     * Approaches the suitable partner if there is one and this dinosaur is not next to it,
     * mate with the partner if already next to it.
     * Returns null if no suitable partner found.
     *
     * @param dinosaur the Actor acting
     * @param map the GameMap containing the Actor
     * @return Null if there are no valid partners,
     * MateAction if the compatible partners are adjacent,
     * MoveActorAction otherwise.
     */
    @Override
    public Action getAction(Dinosaur dinosaur, GameMap map) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        // Iterates through nearest to farthest locations from where the dinosaur is
        for (Location location: (GameLocation) map.locationOf(dinosaur)) {
            if (location.containsAnActor()
                    && location.getActor().getClass().isInstance(dinosaur)
                    && isCompatibleWith(dinosaur, (Dinosaur) location.getActor(), map)) {

                Dinosaur partner = (Dinosaur) location.getActor();

                for (Exit exit : map.locationOf(dinosaur).getExits()) {
                    Location destination = exit.getDestination();
                    if (destination.containsAnActor() && destination.getActor().equals(partner))
                        return new MateAction(dinosaur, partner);
                }

                return new FollowBehaviour(map.locationOf(partner)).getAction(dinosaur, map);
            }
        }
        return null;
    }

    /**
     * Returns true if d1 and d2 can mate with each other.
     *
     * @param d1 Dinosaur 1
     * @param d2 Dinosaur 2
     * @return True if d1 and d2 can mate with each other
     */
    private boolean isCompatibleWith(Dinosaur d1, Dinosaur d2, GameMap map) {
        Objects.requireNonNull(d1, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(d2, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        return d1.isFertile(map) && d2.isFertile(map) &&
                isOppositeGender(d1, d2) && isSameSpecies(d1, d2);
    }

    /**
     * Returns true if d1 and d2 are of opposite gender.
     *
     * @param d1 Dinosaur 1
     * @param d2 Dinosaur 2
     * @return True if d1 and d2 are of opposite gender
     */
    private boolean isOppositeGender(Dinosaur d1, Dinosaur d2) {
        Objects.requireNonNull(d1, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(d2, "Expected Dinosaur type argument, but null is received.");

        return (!d1.hasCapability(ActorCapability.FEMALE) && d2.hasCapability(ActorCapability.FEMALE)) ||
                (d1.hasCapability(ActorCapability.FEMALE) && !d2.hasCapability(ActorCapability.FEMALE));
    }

    /**
     * Returns true if d1 and d2 are of same species.
     *
     * @param d1 Dinosaur 1
     * @param d2 Dinosaur 2
     * @return True if d1 and d2 are of same species
     */
    private boolean isSameSpecies(Dinosaur d1, Dinosaur d2) {
        Objects.requireNonNull(d1, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(d2, "Expected Dinosaur type argument, but null is received.");

        return d1.getClass() == d2.getClass();
    }
}