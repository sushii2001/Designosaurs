package game;

import edu.monash.fit2099.engine.*;
import game.grounds.SearchableGround;
import game.items.Corpse;


/**
 * The Player of the game.
 *
 * @author Ci Leong Ong
 */
public class Player extends Actor {

	/**
	 * Menu used for displaying the selections of actions the Player can choose from.
	 */
	private final Menu menu = new Menu();

	/**
	 * Stores the amount of eco points the Player has.
	 * Can be used to purchase Items at VendingMachine placed near spawn.
	 */
	private int ecoPoints;

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
		ecoPoints = 0;
	}

	/**
	 * Check if the player can afford the price.
	 *
	 * @param price The price used to check against.
	 * @return True if the player can afford the price.
	 * False otherwise.
	 */
	public boolean canAfford(int price) {
		return ecoPoints >= price;
	}

	/**
	 * Increment player's eco points.
	 *
	 * @param amount Number of eco points to add to the Player.
	 */
	public void earnEcoPoints(int amount) {
		ecoPoints += amount;
	}

	/**
	 * Deduction of player's eco points when spending it.
	 *
	 * @param amount Number of eco points spent.
	 */
	public void spendEcoPoints(int amount) {
		ecoPoints = Math.max(ecoPoints-amount, 0);
	}

	/**
	 * Returns the Player's Corpse.
	 * We assume Player is immortal as once he dies, the game ends.
	 * Therefore, the stats of Player's Corpse is set to 0,
	 * as it will not be used.
	 *
	 * @return The Player's Corpse
	 */
	@Override
	public Corpse getCorpse() {
		return new Corpse("Dead " + name, 0, 0);
	}

	/**
	 * Select and return an action to perform on the current turn.
	 *
	 * @param actions    Collection of possible actions
	 * @param lastAction The Action the Player took last turn.
	 * @param map        The map containing the Actor
	 * @param display    The I/O object to which messages may be written
	 * @return The action to be performed by the Player this turn
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		System.out.printf("Eco points: %d%n", ecoPoints);

		actions.add(new QuitAction());

		Ground groundHere = map.locationOf(this).getGround();

		if (groundHere instanceof SearchableGround) {
			for (Item item : ((SearchableGround) groundHere).getItemList())
				actions.add(new SearchItemAction((SearchableGround) groundHere, item));
		}

		for (Item item : getInventory()) {
			if (item.hasCapability(ItemCapability.RANGED_WEAPON))
				actions.add(new ShootAction(this, (Weapon) item, map));
		}

		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();

		return menu.showMenu(this, actions, display);
	}
}
