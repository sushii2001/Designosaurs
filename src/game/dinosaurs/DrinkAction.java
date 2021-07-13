package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.ActorCapability;
import game.grounds.WaterBody;

import java.util.Objects;


/**
 * The action of Dinosaurs drinking water from a water body.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class DrinkAction extends Action {

    /**
     * The water source that the target will be drinking from
     */
    private WaterBody waterBody;

    /**
     * The amount of water points the target will restore
     */
    private int waterHealingPoints;

    /**
     * The target Dinosaur that will be performing the action
     */
    private final Dinosaur target;

    /**
     * Constructor.
     *
     * @param target   The target Dinosaur that will be drinking
     * @param location The location next to a water source
     */
    public DrinkAction(Dinosaur target, Location location) {
        Objects.requireNonNull(target, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(location, "Expected Location type argument, but null is received.");

        this.target = target;

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.getGround() instanceof WaterBody && ((WaterBody) destination.getGround()).hasSips()) {
                waterBody = (WaterBody) destination.getGround();
                break;
            }
        }
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action
     * @param map   The map the actor is on
     * @return A description of what happened that can be displayed to the user
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        waterBody.decrementSips();
        waterHealingPoints = target.hasCapability(ActorCapability.TALL) ? 80 : 30;
        target.quenchThirst(waterHealingPoints);
        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string.
     *
     * @param actor The Actor performing the Action
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        return String.format("%s restored %d water level by drinking water.", actor, waterHealingPoints);
    }
}
