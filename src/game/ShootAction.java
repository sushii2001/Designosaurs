package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.DieAction;

import java.util.Random;


/**
 * Handles ranged weapon shooting action/logic,
 * performed exclusively by Player.
 * Implements ComputeActorDistance to calculate
 * the distance between two Actors.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class ShootAction extends Action implements ComputeDistance {

    /**
     * Random used to determine if the shot is a critical hit.
     */
    private final Random rand = new Random();

    /**
     * The victim Actor.
     */
    private final Actor target;

    /**
     * The ranged weapon used to attack the target.
     */
    private final Weapon weapon;

    /**
     * Constructor.
     *
     * @param shooter The shooting Actor.
     * @param weapon The ranged weapon used to shoot.
     * @param map The map that the shooter is on.
     */
    public ShootAction(Actor shooter, Weapon weapon, GameMap map) {
        this.target = acquireTarget(shooter, map);
        this.weapon = weapon;
    }

    /**
     * Shooter shoots.
     * Damage is dependent on the target's max HP.
     * Has 50% chance to deal critical hit, kills the target immediately.
     * Has 50% chance to deal normal damage, decreases the target's HP by 80% of its max HP.
     *
     * @param shooter The actor that will shoot the target
     * @param map     The map the shooter is on.
     * @return A description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor shooter, GameMap map) {
        String result;

        if (target == null)
            return String.format("%s %s the air", shooter, weapon.verb());

        int damage = rand.nextBoolean() ? target.getMaxHitPoints() : (int) (target.getMaxHitPoints() * 0.8);
        target.hurt(damage);
        result = String.format("%s zapped %s.%n", shooter, target);
        result += String.format("%s %s %s for %s damage.%n", shooter, weapon.verb(), target, damage);

        if (!target.isConscious()) {
            new DieAction(true).execute(target, map);
            for (Item item : target.getInventory())
                item.getDropAction().execute(target, map);
            map.removeActor(target);
            result += target + " is killed.";
        }

        return result;
    }

    /**
     * Returns a descriptive string.
     *
     * @param shooter The shooter
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor shooter) {
        if (target == null)
            return String.format("%s %s the air.", shooter, weapon.verb());
        else
            return String.format("%s %s %s.", shooter, weapon.verb(), target);
    }

    /**
     * Returns the target closest to the shooter that is within range on the map.
     *
     * @param shooter The shooting Actor
     * @param map     The map that the shooter and target are on
     * @return        The target closest to the shooter on the map
     */
    private Actor acquireTarget(Actor shooter, GameMap map) {
        int SHOOTING_RANGE = 20;
        Location here = map.locationOf(shooter);

        // Iterate through locations from nearest to farthest from here
        for (Location location : (GameLocation) here) {
            if (distanceBetween(here, location) >= SHOOTING_RANGE)
                break;
            else if (location.containsAnActor() && !location.getActor().equals(shooter))
                return location.getActor();
        }

        return null;
    }
}