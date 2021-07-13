package game.grounds;

import edu.monash.fit2099.engine.*;

import java.util.*;


/**
 * Ground holding items that can be searched from by Actors.
 *
 * @author Ci Leong Ong
 */
public abstract class SearchableGround extends Ground {

    /**
     * Used to simulate probability to add Items.
     */
    protected Random rand = new Random();

    /**
     * List of Items that the SearchableGround is holding.
     */
    protected List<Item> itemList;

    /**
     * Constructor.
     *
     * @param displayChar The character used to display this in displayed map.
     */
    public SearchableGround(char displayChar) {
        super(displayChar);
        itemList = new ArrayList<>();
    }

    /**
     * Returns the amount of food healing points available to the target Actor.
     * Returns null if the ground is not a valid food source for the Actor.
     *
     * @param target    The Actor that will be feeding on here
     * @param scoutOnly True if just checking whether this spot is valid, false if feeding on it
     * @return The food healing points available to the target Actor
     */
    public Integer getFoodPoints(Actor target, boolean scoutOnly) {
        return null;
    }

    /**
     * Returns the amount of water healing points available to the target Actor.
     * Returns null if the ground is not a valid water source for the Actor.
     *
     * @param target    The Actor that will be drinking from here
     * @param scoutOnly True if just checking whether this spot is valid, false if drinking from it
     * @return The water healing points available to the target Actor
     */
    public Integer getWaterPoints(Actor target, boolean scoutOnly) {
        return null;
    }

    /**
     * Return the list of items this is holding.
     *
     * @return The list of items this is holding.
     */
    public List<Item> getItemList(){
        return itemList;
    }

    /**
     * Remove quantity amount of Items of itemClass from the list.
     *
     * @param itemClass Class of Item to be removed
     * @param quantity  Number of items to be removed
     * @return Number of items successfully removed
     */
    protected int removeItems(Class<?> itemClass, int quantity) {
        int removed = 0;
        int index = 0;
        while (removed < quantity && index < itemList.size()) {
            if (itemClass.isInstance(itemList.get(index))) {
                itemList.remove(index);
                removed++;
            } else
                index++;
        }
        return removed;
    }

}
