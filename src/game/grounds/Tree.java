package game.grounds;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.NumberRange;
import game.ActorCapability;
import game.GameWorld;
import game.GroundCapability;
import game.items.Fruit;


/**
 * Represents Trees in the game that can change appearances when grew up,
 * can grow and drop Fruits on the Ground, and is searchable by Actors
 * for Fruit and heal.
 *
 * @author Ci Leong Ong
 */
public class Tree extends SearchableGround {

	/**
	 * Number of turns since creation.
	 */
	private int age = 0;

	/**
	 * Constructor.
	 */
	public Tree() {
		super('+');
		addCapability(GroundCapability.RECHARGE_FLIGHT);
		addCapability(GroundCapability.BLOCKS_SHORT_PLANT_GROWTH);
	}

	/**
	 * Returns the amount of food healing points available to the target Actor.
	 * Returns null if the ground is not a valid food source for the Actor.
	 *
	 * @param target    The Actor that will be feeding on here
	 * @param scoutOnly True if just checking whether this spot is valid, false if feeding on it
	 * @return The food healing points available to the target Actor
	 */
	@Override
	public Integer getFoodPoints(Actor target, boolean scoutOnly) {
		if (target.hasCapability(ActorCapability.TALL)) {
			int hitPoints = target.getHitPoints();
			int maxHitPoints = target.getMaxHitPoints();

			int amount;
			if (!scoutOnly)
				amount = removeItems(Fruit.class, (int) Math.ceil((double) (maxHitPoints - hitPoints) / 5));
			else
				amount = (int) getItemList().stream().filter(Fruit.class::isInstance).count();
			return amount>0 ? amount*5 : null;
		} else
			return null;
	}

	/**
	 * Increments the age of the Tree every turn,
	 * and updates its appearance if necessary.
	 *
	 * On any turn, has 50% to produce a Fruit,
	 * if this happens, the Player earns 1 eco point.
	 *
	 * On any turn, each fruit inside the tree has 5%
	 * chance to drop to the Ground as a Fruit object.
	 *
	 * @param location The location of the Tree
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);

		age++;
		if (age == 10)
			displayChar = 't';
		if (age == 20)
			displayChar = 'T';

		if (rand.nextBoolean()) {
			itemList.add(new Fruit());
			GameWorld.getPlayer().earnEcoPoints(1);
		}

		for (int ignored : new NumberRange(0, itemList.size())) {
			if (rand.nextDouble() <= 0.05)
				location.addItem(itemList.remove(0));
		}
	}
}
