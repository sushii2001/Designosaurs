package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.ActorCapability;
import game.items.ConsumableItem;

import java.util.Objects;


/**
 * For dinosaurs to feed on Items on the Ground.
 * e.g. Fallen Fruits from Trees.
 *
 * @author Ci Leong Ong
 */
public class EatItemOnGroundAction extends Action {

    /**
     * The Actor that will be eating.
     */
    private final Dinosaur target;

    /**
     * The Item the target will be feeding on.
     */
    private Item food;

    /**
     * The food points the target will restore.
     */
    private int foodHealingPoints;

    /**
     * The water points the target will restore.
     */
    private int waterHealingPoints;

    /**
     * Constructor.
     *
     * @param target The Actor that will feed on the Item
     */
    public EatItemOnGroundAction(Dinosaur target) {
        Objects.requireNonNull(target, "Expected Dinosaur type argument, but null is received.");
        this.target = target;
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return A description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        for (Item item : map.locationOf(target).getItems()) {
            int[] tuple = target.accept((ConsumableItem) item);

            // If the item is a valid food for target
            if (Objects.nonNull(tuple)) {

                // Perform actions that target displays before eating
                target.executePreEatingAction();

                food = item;

                foodHealingPoints = tuple[0];
                waterHealingPoints = tuple[1];

                target.heal(foodHealingPoints);
                target.quenchThirst(waterHealingPoints);

                if (!actor.hasCapability(ActorCapability.TINY))
                    map.locationOf(target).removeItem(food);

                break;
            }
        }

        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string.
     *
     * @param actor The actor performing the action.
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        return String.format("%s eats %s and healed food level %d and water level %d.",
                actor, food, foodHealingPoints, waterHealingPoints);
    }

}