package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import game.grounds.SearchableGround;

import java.util.Random;


/**
 * Player searches for Item from within the SearchableGround.
 *
 * @author Ci Leong Ong
 */
public class SearchItemAction extends Action {

    /**
     * Used to determine whether the item can be found.
     */
    private final Random rand = new Random();

    /**
     * Target Ground to be searched for item.
     */
    private final SearchableGround groundHere;

    /**
     * The Item the Player is trying to search for.
     */
    private final Item item;

    /**
     * Constructor.
     *
     * @param ground Target Plant to be searched for Fruits
     */
    public SearchItemAction(SearchableGround ground, Item item) {
        groundHere = ground;
        this.item = item;
    }

    /**
     * Has 40% chance to obtain the Item from the SearchableGround.
     * Player gains 10 eco points if so.
     *
     * @param player The Actor searching the SearchableGround
     * @param map The map the Actor is on
     * @return A description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor player, GameMap map) {
        if (rand.nextDouble() <= 0.4) {
            groundHere.getItemList().remove(item);
            player.addItemToInventory(item);
            GameWorld.getPlayer().earnEcoPoints(10);
            return String.format("Player found %s from the %s.", item, groundHere.getClass().getSimpleName());
        } else
            return "Player could not find anything useful.";
    }

    /**
     * Returns a descriptive string.
     *
     * @param actor The Actor searching for item.
     * @return The text we put on the menu.
     */
    @Override
    public String menuDescription(Actor actor) {
        return String.format("Player searches for %s", item);
    }

}
