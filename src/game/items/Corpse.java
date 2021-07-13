package game.items;

import edu.monash.fit2099.engine.Location;
import game.dinosaurs.Allosaur;
import game.dinosaurs.Pterodactyl;


/**
 * Represents the Corpse seen on the Ground.
 * Edible by carnivorous Actors.
 * Rots away after a certain number of turns if left on Ground.
 *
 * @author Ci Leong Ong
 */
public class Corpse extends ConsumableItem {

    /**
     * Number of turns before the Corpse rots away.
     */
    private final int maxAge;

    /**
     * How much a carnivorous Dinosaur can heal
     * should it consume this Corpse
     */
    private int healingPoints;

    /**
     * Number of turns it has been on the Ground
     */
    private int age;

    /**
     * Constructor.
     *
     * @param name          The name of the dead Actor
     * @param maxAge        The maximum number of turns the Corpse can stay on the Ground
     * @param healingPoints How much a carnivorous Actor can heal should it consume the Corpse
     */
    public Corpse(String name, int maxAge, int healingPoints) {
        super(name + " corpse", '%', true);
        age = 0;
        this.maxAge = maxAge;
        this.healingPoints = healingPoints;
    }

    /**
     * Defines the healing points for Allosaurs, in tuple form.
     * Heals food points by the healingPoints of the Corpse, and heals 0 water points.
     */
    @Override
    public int[] visit(Allosaur allosaur) {
        return new int[] {healingPoints, 0};
    }

    /**
     * Defines the healing points for Pterodactyls, in tuple form.
     * Heals food points by the healingPoints of the Corpse, and heals 0 water points.
     * Decrements the HP of the Corpse.
     */
    @Override
    public int[] visit(Pterodactyl pterodactyl) {
        healingPoints -= 10;
        return new int[] {10, 0};
    }

    /**
     * Increments the age of the Corpse every turn,
     * once it hits the predefined number of turns,
     * the Corpse will rot away and disappear.
     *
     * @param currentLocation The Location of the Ground on which the Egg lies.
     */
    @Override
    public void tick(Location currentLocation) {
        age++;
        if (age == maxAge || healingPoints <= 0)
            currentLocation.removeItem(this);
    }

}