package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;


/**
 * Quits the game, by removing the Player from the map he is standing on.
 * The World stops running after that.
 *
 * @author Ci Leong Ong
 */
public class QuitAction extends Action {

    /**
     * Perform the Action.
     *
     * @param player The actor performing the action.
     * @param map The map the actor is on.
     * @return A description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor player, GameMap map) {
        map.removeActor(player);
        return String.format("%s has quited the game.", player);
    }

    /**
     * Returns a descriptive string.
     *
     * @param player The Player.
     * @return The text we put on the menu.
     */
    @Override
    public String menuDescription(Actor player) {
        return String.format("%s quits the game.", player);
    }

    /**
     * Returns the key used in the menu to trigger this Action.
     *
     * @return "Q", as in quit.
     */
    @Override
    public String hotkey() {
        return "Q";
    }
}
