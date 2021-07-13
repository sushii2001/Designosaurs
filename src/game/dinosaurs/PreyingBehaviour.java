package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.ActorCapability;
import game.Behaviour;
import game.GameLocation;

import java.util.Objects;


/**
 * A class for carnivorous dinosaurs to hunt down preys.
 * Cannot attack the same prey for a number of turns.
 *
 * @author Toh Zhen Kang
 * @author Ci Leong Ong
 */
public class PreyingBehaviour implements Behaviour {

    /**
     * The hunter closes in on the prey if it is not next to it,
     * attacks the prey if next to it.
     * If no prey can be found, returns null.
     *
     * @param dinosaur The hunting dinosaur
     * @param map      The GameMap containing the dinosaur
     * @return null if the Actor acting is not hungry or there is no valid prey
     * AttackAction when the prey and carnivorous dinosaurs are adjacent
     * FollowBehaviour otherwise.
     */
    @Override
    public Action getAction(Dinosaur dinosaur, GameMap map) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        Huntable hunter;
        if (!(dinosaur instanceof Huntable) || !dinosaur.isHungry())
            return null;
        else
            hunter = (Huntable) dinosaur;

        Location preyLocation = null;
        for (Location destination: (GameLocation) map.locationOf(dinosaur)) {
            if (hasPrey(hunter, destination))
                preyLocation = destination;
        }

        if (preyLocation == null)
            return null;

        Dinosaur prey = (Dinosaur) preyLocation.getActor();
        Location hunterLocation = map.locationOf(dinosaur);

        // Iterates through the nearest to farthest locations from the hunter
        for (Exit exit : hunterLocation.getExits()) {
            Location destination = exit.getDestination();

            // Attacks if hunter is next to prey
            if (destination.containsAnActor() && destination.getActor().equals(prey)) {
                if (prey.hasCapability(ActorCapability.TINY) && !prey.hasCapability(ActorCapability.FLY)) {
                    new DieAction(false).execute(prey, map);
                    ((Actor) hunter).heal(((Actor) hunter).getMaxHitPoints());
                    System.out.printf("%s has eaten the %s%n", hunter, prey);
                    return null;
                } else {
                    hunter.addToBlacklist(prey.getDinoId());
                    return new AttackAction(prey);
                }
            }
        }

        return new FollowBehaviour(preyLocation).getAction((Dinosaur) hunter, map);
    }

    /**
     * Returns true if target location contains the hunter's prey.
     *
     * @param hunter   The hunting Actor
     * @param location The target Location
     * @return true if target location contains the hunter's prey, otherwise false.
     */
    private boolean hasPrey(Huntable hunter, Location location) {
        Objects.requireNonNull(hunter, "Expected Huntable type argument, but null is received.");
        Objects.requireNonNull(location, "Expected Location type argument, but null is received.");

        return location.containsAnActor() && location.getActor().hasCapability(ActorCapability.PREY)
                && !location.getActor().hasCapability(ActorCapability.FLY)
                && !location.getActor().equals(hunter)
                && !(hunter.getBlacklist().containsKey(((Dinosaur) location.getActor()).dinoId));
    }
}