package game.items;

import edu.monash.fit2099.engine.Item;
import game.Visitor;
import game.dinosaurs.Allosaur;
import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Pterodactyl;
import game.dinosaurs.Stegosaur;


/**
 * All Items that could be fed/eaten by Dinosaurs extend this class.
 *
 * @author Ci Leong Ong
 */
public abstract class ConsumableItem extends Item implements Visitor<int[]> {

    /**
     * Constructor
     * @param name        Name of the consumable
     * @param displayChar Character used to display the consumable on the displayed map
     * @param portable    If the consumable can be stored in Player's inventory
     */
    public ConsumableItem(String name, char displayChar, boolean portable) {
        super(name, displayChar, portable);
    }

    /**
     * Visit operation for Stegosaurs.
     * Returns the healing points for Stegosaurs, in tuple form.
     * 1st element is the food healing points.
     * 2nd element is the water healing points.
     * If the tuple is null, the consumable is not a valid food for Stegosaurs.
     */
    @Override
    public int[] visit(Stegosaur stegosaur) {
        return null;
    }

    /**
     * Visit operation for Brachiosaurs.
     * Returns the healing points for Brachiosaurs, in tuple form.
     * 1st element is the food healing points.
     * 2nd element is the water healing points.
     * If the tuple is null, the consumable is not a valid food for Brachiosaurs.
     */
    @Override
    public int[] visit(Brachiosaur brachiosaur) {
        return null;
    }

    /**
     * Visit operation for Allosaurs.
     * Returns the healing points for Allosaurs, in tuple form.
     * 1st element is the food healing points.
     * 2nd element is the water healing points.
     * If the tuple is null, the consumable is not a valid food for Allosaurs.
     */
    @Override
    public int[] visit(Allosaur allosaur) {
        return null;
    }

    /**
     * Visit operation for Pterodactyls.
     * Returns the healing points for Pterodactyls, in tuple form.
     * 1st element is the food healing points.
     * 2nd element is the water healing points.
     * If the tuple is null, the consumable is not a valid food for Pterodactyls.
     */
    @Override
    public int[] visit(Pterodactyl pterodactyl) {
        return null;
    }

}
