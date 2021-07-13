package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import game.GameWorld;
import game.items.ConsumableItem;

import java.util.Objects;


/**
 * Feeds an Actor an Item,
 * healing effect varies across dinosaur species.
 *
 * @author Ci Leong Ong
 */
public class FeedAction extends Action {

    /**
     * The target Actor that will feed on the Item.
     */
    private final Dinosaur target;

    /**
     * The Item that the Actor will consume.
     */
    private final Item item;

    /**
     * The number of food points the target Actor will gain.
     */
    private final int foodPoints;

    /**
     * The number of water points the target Actor will gain.
     */
    private final int waterPoints;

    /**
     * Constructor.
     *
     * @param target The target Actor that will feed on the Item
     * @param item   The Item that the Actor will consume
     */
    public FeedAction(Dinosaur target, Item item) {
        Objects.requireNonNull(target, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(item, "Expected Item type argument, but null is received.");

        this.target = target;
        this.item = item;

        int[] tuple = target.accept((ConsumableItem) item);
        foodPoints = tuple[0];
        waterPoints = tuple[1];
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

        GameWorld.getPlayer().earnEcoPoints(10);
        actor.removeItemFromInventory(item);
        target.heal(foodPoints);
        target.quenchThirst(waterPoints);
        return String.format("%s healed %s's food level by %d and water level by %d with %s.", actor, target, foodPoints, waterPoints, item);
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
        return String.format("Player heals %s with %s", target, item);
    }

}
