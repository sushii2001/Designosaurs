package game.items;

import game.dinosaurs.Pterodactyl;


/**
 * Represents fish Item that can be eaten by Pterodactyls.
 *
 * @author Ci Leong Ong
 */
public class Fish extends ConsumableItem {

    /**
     * Constructor.
     */
    public Fish() {
        super("Fish", '>', true);
    }

    /**
     * Defines the healing points for Pterodactyls, in tuple form.
     * Heals food points by 5, and heals 0 water points.
     */
    @Override
    public int[] visit(Pterodactyl pterodactyl) {
        return new int[] {5, 0};
    }

}
