package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.ActorCapability;

import java.util.Objects;


/**
 * Handles Dinosaur mating action/logic.
 *
 * @author Toh Zhen Kang
 */
public class MateAction extends Action {

    /**
     * The two Dinosaurs that will mate.
     */
    private final Dinosaur d1, d2;

    /**
     * Constructor.
     *
     * @param d1 Dinosaur 1 that will mate with Dinosaur 2
     * @param d2 Dinosaur 2 that will mate with Dinosaur 1
     */
    public MateAction(Dinosaur d1, Dinosaur d2) {
        Objects.requireNonNull(d1, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(d2, "Expected Dinosaur type argument, but null is received.");

        this.d1 = d1;
        this.d2 = d2;
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        if (d1.hasCapability(ActorCapability.FEMALE))
            d1.addCapability(ActorCapability.PREGNANT);
        else
            d2.addCapability(ActorCapability.PREGNANT);
        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string.
     *
     * @param actor The actor performing the action
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        Objects.requireNonNull(actor, "Expected Actor type argument, but null is received.");
        return String.format("### %s and %s has mated ###", d1, d2);
    }

}
