package game.dinosaurs;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.Behaviour;
import game.GameLocation;

import java.util.Objects;


/**
 * Represents the behaviour where the Dinosaur goes to a predefined Ground,
 * to recharge its skills (represented by capabilities).
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class RechargeSkillBehaviour implements Behaviour {

    /**
     * The skill that needs to be recharged
     */
    Enum<?> actorSkill;

    /**
     * The identifier used to identify the Ground that can be used to recharge the skill
     */
    Enum<?> groundIdentifier;

    /**
     * Constructor.
     *
     * @param actorSkill The skill that needs to be recharged
     * @param groundIdentifier The identifier used to identify the Ground that can be used to recharge the skill
     */
    public RechargeSkillBehaviour(Enum<?> actorSkill, Enum<?> groundIdentifier) {
        Objects.requireNonNull(actorSkill, "Expected Enum type argument, but null is received.");
        Objects.requireNonNull(groundIdentifier, "Expected Enum type argument, but null is received.");

        this.actorSkill = actorSkill;
        this.groundIdentifier = groundIdentifier;
    }

    /**
     * If the dinosaur already has the skill (i.e. no recharging required), returns null.
     * Otherwise, call FollowBehaviour to close in on the target Ground.
     *
     * @param dinosaur the Actor acting
     * @param map the GameMap containing the Actor
     * @return Null if dinosaur has the skill or no location with groundIdentifier capability found.
     * Otherwise, uses MoveActorAction to approach the target Ground.
     */
    @Override
    public Action getAction(Dinosaur dinosaur, GameMap map) {
        Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
        Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

        if (dinosaur.hasCapability(actorSkill))
            return null;

        for (Location destination : (GameLocation) map.locationOf(dinosaur)) {
            if (destination.getGround().hasCapability(groundIdentifier))
                return new FollowBehaviour(destination).getAction(dinosaur, map);
        }

        return null;
    }
}
