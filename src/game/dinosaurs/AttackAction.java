package game.dinosaurs;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;


/**
 * Special Action for attacking other Actors.
 *
 * @author Ci Leong Ong
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;

	/**
	 * Random number generator,
	 * used to simulate probability
	 */
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
	}

	/**
	 * The actor attempts to attack the target,
	 * has 50% chance to succeed.
	 * Execute DieAction on the target if it is dead.
	 *
	 * @param actor The attacking Actor.
	 * @param map The map the Actor is on.
	 * @return A description of what happened that can be displayed to the user.
	 */
	@Override
	public String execute(Actor actor, GameMap map) {

		if (rand.nextBoolean())
			return actor + " misses " + target + ".";

		Weapon weapon = actor.getWeapon();

		int damage = weapon.damage();
		String result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";

		target.hurt(damage);
		if (!target.isConscious()) {
			new DieAction(true).execute(target, map);
			for (Item item : target.getInventory())
				item.getDropAction().execute(target, map);
			map.removeActor(target);
			result += System.lineSeparator() + target + " is killed.";
		}

		return result;
	}

	/**
	 * Returns a descriptive string.
	 *
	 * @param actor The attacking Actor.
	 * @return The text we put on the menu
	 */
	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks " + target;
	}
}
