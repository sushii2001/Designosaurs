package game.items;

import edu.monash.fit2099.engine.Location;
import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Stegosaur;


/**
 * Represents the PortableItem Fruit that grows on Trees and Bushes,
 * drops from Trees, and can be fed to herbivorous Dinosaurs by the Player.
 *
 * @author Ci Leong Ong
 */
public class Fruit extends ConsumableItem {

    /**
     * Number of turns the Fruit has been on the Ground
     */
    private int age;

    /**
     * Constructor
     */
    public Fruit() {
        super("Fruit", 'F', true);
        age = 0;
    }

    /**
     * Defines the healing points for Stegosaurs, in tuple form.
     * Heals food points by 20, and heals 0 water points.
     */
    @Override
    public int[] visit(Stegosaur stegosaur) {
        return new int[] {20, 0};
    }

    /**
     * Defines the healing points for Brachiosaurs, in tuple form.
     * Heals food points by 20, and heals 0 water points.
     */
    @Override
    public int[] visit(Brachiosaur brachiosaur) {
        return new int[] {20, 0};
    }

    /**
     * Increments the age of the Fruit every turn,
     * once it hits 15 turns, the Fruit will rot away.
     *
     * @param currentLocation The location of the ground on which the Fruit lies.
     */
    @Override
    public void tick(Location currentLocation) {
        age++;
        if (age == 15)
            currentLocation.removeItem(this);
    }

}
