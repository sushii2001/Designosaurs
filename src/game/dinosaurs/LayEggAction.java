package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.ActorCapability;
import game.items.Egg;

import java.util.Objects;


/**
 * Handles Dinosaurs' lay egg action/logic.
 *
 * @author Ci Leong Ong
 */
public class LayEggAction extends Action {

    /**
     * The Egg that will be laid.
     */
    private final Egg egg;

    /**
     * Constructor.
     *
     * @param egg The Egg that will be laid
     */
    public LayEggAction(Egg egg) {
        Objects.requireNonNull(egg, "Expected Egg type argument, but null is received.");
        this.egg = egg;
    }

    /**
     * Perform the Action.
     *
     * @param parent The parent Actor that will be laying the Egg
     * @param map The map the parent is on
     * @return A description of what happened that can be displayed to the user
     */
    @Override
    public String execute(Actor parent, GameMap map) {
        Objects.requireNonNull(parent, "Expected Actor type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        map.locationOf(parent).addItem(egg);
        parent.removeCapability(ActorCapability.PREGNANT);
        return menuDescription(parent);
    }

    /**
     * Returns a descriptive string.
     *
     * @param parent The parent Actor that will be laying the Egg
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor parent) {
        Objects.requireNonNull(parent, "Expected Actor type argument, but null is received.");
        return String.format("%s lays an egg.", parent);
    }

}
