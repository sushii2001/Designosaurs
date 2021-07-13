package game.grounds;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;


/**
 * Represents the Wall terrain that surrounds the Player's spawn.
 */
public class Wall extends Ground {

	/**
	 * Constructor.
	 */
	public Wall() {
		super('#');
	}

	/**
	 * Prohibits other Actors from entering Player's spawn,
	 * except from the entrance.
	 * Reduces the chance of NPCs invading Player's spawn.
	 *
	 * @param actor the Actor to check
	 * @return False
	 */
	@Override
	public boolean canActorEnter(Actor actor) {
		return false;
	}

	/**
	 * Blocks all thrown objects from entering Player's spawn.
	 *
	 * @return True
	 */
	@Override
	public boolean blocksThrownObjects() {
		return true;
	}
}
