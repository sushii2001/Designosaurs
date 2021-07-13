package game.items;

import game.dinosaurs.Allosaur;
import game.dinosaurs.Pterodactyl;


/**
 * MealKit that can only be fed to carnivorous Actors,
 * completely heals the Actor.
 *
 * @author Ci Leong Ong
 */
public class CarnivoreMealKit extends MealKit {

    /**
     * Constructor.
     */
    public CarnivoreMealKit() {
        super("Carnivore Meal Kit", 'C');
    }

    /**
     * Defines the healing points for Allosaurs, in tuple form.
     * Heals food points to maximum, and heals 0 water points.
     */
    @Override
    public int[] visit(Allosaur allosaur) {
        return new int[] {allosaur.getMaxHitPoints(), 0};
    }

    /**
     * Defines the healing points for Pterodactyls, in tuple form.
     * Heals food points to maximum, and heals 0 water points.
     */
    @Override
    public int[] visit(Pterodactyl pterodactyl) {
        return new int[] {pterodactyl.getMaxHitPoints(), 0};
    }

}
