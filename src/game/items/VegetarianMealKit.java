package game.items;

import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Stegosaur;


/**
 * MealKit that can only be fed to herbivorous Dinosaurs,
 * completely heals the their food points.
 *
 * @author Ci Leong Ong
 */
public class VegetarianMealKit extends MealKit {

    /**
     * Constructor.
     */
    public VegetarianMealKit() {
        super("Vegetarian Meal Kit", 'V');
    }

    /**
     * Defines the healing points for Stegosaurs, in tuple form.
     * Heals food points to maximum, and heals 0 water points.
     */
    @Override
    public int[] visit(Stegosaur stegosaur) {
        return new int[] {stegosaur.getMaxHitPoints(), 0};
    }

    /**
     * Defines the healing points for Brachiosaurs, in tuple form.
     * Heals food points to maximum, and heals 0 water points.
     */
    @Override
    public int[] visit(Brachiosaur brachiosaur) {
        return new int[] {brachiosaur.getMaxHitPoints(), 0};
    }

}
