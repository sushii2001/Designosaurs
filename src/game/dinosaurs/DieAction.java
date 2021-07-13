package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

import java.util.Objects;


/**
 * Called when an Actor is dead,
 * responsible for replacing the Actor with its Corpse.
 * Drops all Items the dying Actor is carrying,
 * and removes the Actor from the map.
 *
 * @author Ci Leong Ong
 */
public class DieAction extends Action {

    /**
     * Decides whether a Corpse will be dropped after death.
     */
    private final boolean dropCorpse;

    /**
     * Constructor.
     *
     * @param dropCorpse Decides whether a Corpse will be dropped after death
     */
    public DieAction(boolean dropCorpse) {
        this.dropCorpse = dropCorpse;
    }

    /**
     * Perform the Action.
     *
     * @param target The dead Actor
     * @param map The map the actor is on
     * @return A description of what happened that can be displayed to the user
     */
    @Override
    public String execute(Actor target, GameMap map) {
        Objects.requireNonNull(target, "Expected Actor type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        for (Item item : target.getInventory())
            item.getDropAction().execute(target, map);

        if (dropCorpse)
            map.locationOf(target).addItem(target.getCorpse());

        map.removeActor(target);

        return menuDescription(target);
    }

    /**
     * Returns a descriptive string.
     *
     * @param target The dead Actor
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor target) {
        Objects.requireNonNull(target, "Expected Actor type argument, but null is received.");
        return String.format("%s is dead.", target);
    }

}
