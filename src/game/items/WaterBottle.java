package game.items;

import game.dinosaurs.Allosaur;
import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Pterodactyl;
import game.dinosaurs.Stegosaur;


/**
 * Refills the water points of Dinosaurs that drink it to maximum.
 *
 * @author Ci Leong Ong
 */
public class WaterBottle extends ConsumableItem {

    /**
     * Constructor.
     * Water bottle can be brought around by the Player.
     */
    public WaterBottle() {
        super("Water Bottle", 'W', true);
    }

    /**
     * Defines the healing points for Stegosaurs, in tuple form.
     * Heals 0 food points, and heals water points to maximum.
     */
    @Override
    public int[] visit(Stegosaur stegosaur) {
        return new int[] {0, stegosaur.getMaxWaterLevel()};
    }

    /**
     * Defines the healing points for Brachiosaurs, in tuple form.
     * Heals 0 food points, and heals water points to maximum.
     */
    @Override
    public int[] visit(Brachiosaur brachiosaur) {
        return new int[] {0, brachiosaur.getMaxWaterLevel()};
    }

    /**
     * Defines the healing points for Allosaurs, in tuple form.
     * Heals 0 food points, and heals water points to maximum.
     */
    @Override
    public int[] visit(Allosaur allosaur) {
        return new int[] {0, allosaur.getMaxWaterLevel()};
    }

    /**
     * Defines the healing points for Pterodactyls, in tuple form.
     * Heals 0 food points, and heals water points to maximum.
     */
    @Override
    public int[] visit(Pterodactyl pterodactyl) {
        return new int[] {0, pterodactyl.getMaxWaterLevel()};
    }

}
