package game.items;

import edu.monash.fit2099.engine.Location;
import game.GameWorld;
import game.dinosaurs.*;

import java.util.Objects;


/**
 * A class that represents dinosaur eggs.
 *
 * @author Ci Leong Ong
 */
public class Egg extends ConsumableItem {

    /**
     * Number of turns required to hatch the Egg.
     */
    private final int turnsToHatch;

    /**
     * Amount of eco points the Player will earn when hatched.
     */
    private final int hatchEarnings;

    /**
     * The Dinosaur that will hatch from the Egg.
     */
    private final Dinosaur babyDinosaur;

    /**
     * Number of turns since the Egg has been laid.
     */
    private int age;

    /**
     * Private constructor used by public static factories
     *
     * @param name Name of the Egg
     * @param turnsToHatch Turns before it's hatched
     * @param hatchEarnings Eco points earned by the Player if hatching is successful
     * @param babyDinosaur The baby dinosaur that will hatch from the Egg
     */
    private Egg(String name, int turnsToHatch, int hatchEarnings, Dinosaur babyDinosaur) {
        super(name, 'O', true);
        this.turnsToHatch = turnsToHatch;
        this.hatchEarnings = hatchEarnings;
        this.babyDinosaur = babyDinosaur;
        this.age = 0;
    }

    /**
     * Factory for constructing Stegosaur egg.
     *
     * @param babyStegosaur The Dinosaur that will hatch from the Egg
     */
    public static Egg getEgg(Stegosaur babyStegosaur) {
        return new Egg("Stegosaur Egg", 40, 100, babyStegosaur);
    }

    /**
     * Factory for constructing Brachiosaur egg.
     *
     * @param babyBrachiosaur The Dinosaur that will hatch from the Egg
     */
    public static Egg getEgg(Brachiosaur babyBrachiosaur) {
        return new Egg("Brachiosaur Egg", 20, 1000, babyBrachiosaur);
    }

    /**
     * Factory for constructing Allosaur egg.
     *
     * @param babyAllosaur The Dinosaur that will hatch from the Egg
     */
    public static Egg getEgg(Allosaur babyAllosaur) {
        return new Egg("Allosaur Egg", 50, 1000, babyAllosaur);
    }

    /**
     * Factory for constructing Pterodactyl egg.
     *
     * @param babyPterodactyl The Dinosaur that will hatch from the Egg
     */
    public static Egg getEgg(Pterodactyl babyPterodactyl) {
        return new Egg("Pterodactyl Egg", 50, 1000, babyPterodactyl);
    }

    /**
     * Defines the healing points for Pterodactyls, in tuple form.
     * Heals food points by 10, and heals 0 water points.
     */
    @Override
    public int[] visit(Allosaur allosaur) {
        return new int[] {10, 0};
    }

    /**
     * Defines the healing points for Pterodactyls, in tuple form.
     * Heals food points by 10, and heals 0 water points.
     */
    @Override
    public int[] visit(Pterodactyl pterodactyl) {
        return new int[] {10, 0};
    }

    /**
     * Increments the age of the Egg every turn,
     * once it hits the predefined number of turns,
     * and if there is no Actor in the same Location,
     * the Egg will hatch.
     *
     * @param currentLocation The Location of the Ground on which the Egg lies.
     */
    @Override
    public void tick(Location currentLocation) {
        age++;
        if (age >= turnsToHatch && Objects.isNull(currentLocation.getActor()))
            hatch(currentLocation);
    }

    /**
     * Hatches the Egg,
     * replaces the Egg with the Dinosaur Actor.
     *
     * @param currentLocation The location the Egg is at now.
     */
    private void hatch(Location currentLocation) {
        GameWorld.getPlayer().earnEcoPoints(hatchEarnings);
        currentLocation.addActor(babyDinosaur);
        currentLocation.removeItem(this);
    }
}
