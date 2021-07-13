package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.grounds.SearchableGround;

import java.util.Objects;


/**
 * Action representing Dinosaurs eating food inside Grounds.
 *
 * @author Ci Leong Ong
 */
public class EatFromSearchableGroundAction extends Action {

    /**
     * The Dinosaur that will be eating from the ground here
     */
    private final Dinosaur target;

    /**
     * The Ground the target is standing on
     */
    private SearchableGround groundHere;

    /**
     * Amount of food points target will heal
     */
    private Integer foodHealingPoints;

    /**
     * Amount of water points target will heal
     */
    private Integer waterHealingPoints;

    /**
     * Constructor.
     *
     * @param target The eating Dinosaur
     */
    public EatFromSearchableGroundAction(Dinosaur target) {
        Objects.requireNonNull(target, "Expected Dinosaur type argument, but null is received.");
        this.target = target;
    }

    /**
     * Perform the Action.
     *
     * @param actor The Actor performing the action
     * @param map   The map the Actor is on
     * @return A description of what happened that can be displayed to the user
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        groundHere = (SearchableGround) map.locationOf(actor).getGround();

        foodHealingPoints = groundHere.getFoodPoints(target, false);
        waterHealingPoints = groundHere.getWaterPoints(target, false);

        if (Objects.nonNull(foodHealingPoints))
            target.heal(foodHealingPoints);

        if (Objects.nonNull(waterHealingPoints))
            target.quenchThirst(waterHealingPoints);

        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string.
     *
     * @param actor The Actor performing the action
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");

        if (Objects.nonNull(foodHealingPoints) && Objects.nonNull(waterHealingPoints))
            return String.format("%s gained %d food points & %d water points by feeding on %s.",
                    actor, foodHealingPoints, waterHealingPoints, groundHere.getClass().getSimpleName());
        else if (Objects.nonNull(foodHealingPoints))
            return String.format("%s gained %d food points by feeding on %s.",
                    actor, foodHealingPoints, groundHere.getClass().getSimpleName());
        else if (Objects.nonNull(waterHealingPoints))
            return String.format("%s gained %d water points by feeding on %s.",
                    actor, waterHealingPoints, groundHere.getClass().getSimpleName());
        else
            return String.format("%s cannot eat from %s.", actor, groundHere.getClass().getSimpleName());
    }

}
