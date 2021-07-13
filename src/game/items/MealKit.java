package game.items;


/**
 * MealKit that Player can only be fed to Dinosaurs,
 * completely heals the Dinosaur, if done so.
 *
 * @author Ci Leong Ong
 */
public abstract class MealKit extends ConsumableItem {

    /**
     * Constructor.
     *
     * @param name        A descriptive name of the MealKit
     * @param displayChar The character that will be used to display it
     */
    public MealKit(String name, char displayChar) {
        super(name, displayChar, true);
    }

}
