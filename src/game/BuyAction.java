package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;


/**
 * Represents the Player's interaction with the VendingMachine.
 *
 * @author Ci Leong Ong
 */
public class BuyAction extends Action {

    /**
     * The Player that will be performing this action.
     */
    private final Player player;

    /**
     * The Item that the player is buying.
     */
    private final Item item;

    /**
     * The price of the Item.
     */
    private final int price;

    /**
     * Constructor.
     *
     * @param item  The Item to be bought by the Player
     * @param price The price of the Item
     */
    public BuyAction(Item item, int price) {
        this.player = GameWorld.getPlayer();
        this.item = item;
        this.price = price;
    }

    /**
     * Player buys the Item by spending eco points.
     * The Item is then added to the Player's inventory.
     *
     * @param actor The Player performing the Action.
     * @param map The map the Player is on.
     * @return A description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (!player.canAfford(price))
            throw new RuntimeException();
        else {
            player.spendEcoPoints(price);
            player.addItemToInventory(item);
            return String.format("%s bought %s with %d eco points.", player, item, price);
        }
    }

    /**
     * Returns a descriptive string.
     *
     * @param actor The Player
     * @return The text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("Buy %s with %d eco points.", item, price);
    }

}
