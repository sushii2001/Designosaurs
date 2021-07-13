package edu.monash.fit2099.interfaces;

import game.items.Corpse;


/**
 * This interface provides the ability to add methods to Actor, without modifying code in the engine,
 * or downcasting references in the game.
 *
 * @author Ci Leong Ong
 */
public interface ActorInterface {

    /**
     * Returns the Actor's current hit points.
     *
     * @return Actor's current hit points
     */
    default Integer getHitPoints() {
        return null;
    }

    /**
     * Returns the Actor's max hit points.
     *
     * @return Actor's max hit points
     */
    default Integer getMaxHitPoints() {
        return null;
    }

    /**
     * Creates and returns the corpse of the Actor.
     *
     * @return The corpse of the Actor
     */
    Corpse getCorpse();

}
