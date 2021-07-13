package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.*;
import game.items.ConsumableItem;
import game.items.Egg;

import java.util.*;


/**
 * A dinosaur class that contains all the dinosaur logic.
 * Its subclasses only handles the logic that are exclusive to the species.
 * To implement any additional functionalities, or change current functionalities,
 * extend this class and override the methods.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public abstract class Dinosaur extends Actor implements Visitable {

    /**
     * Randomize gender for dinosaurs
     */
    protected static final Random rand = new Random();

    /**
     * Stores the total number of Dinosaurs that have been on the map.
     * Used to generate unique ID for each Dinosaur.
     */
    protected static int dinoCounter = 0;

    /**
     * A list of Behaviours.
     * Every turn, the list will be looped through and the Action decided by the Behaviours will be executed.
     * Order matters, Behaviours with lower index in the list have higher priority.
     */
    protected Behaviour[] actionFactories;

    /**
     * Unique ID for Dinosaur
     */
    protected final int dinoId;

    /**
     * Defines dinosaur's max water level capacity
     */
    protected int maxWaterLevel;

    /**
     * Keep tracks of dinosaur's current water level capacity
     */
    protected int waterLevel;

    /**
     * Age of the Dinosaur.
     * Direct representation of how many turns has passed since it's born.
     */
    protected int age;

    /**
     * Number of turns the Dinosaur has been pregnant.
     */
    protected int turnsBeingPregnant;

    /**
     * Number of turns the Dinosaur has been unconscious.
     */
    protected int turnsBeingUnconscious;

    /**
     * Constructor for creating dinosaurs.
     *
     * @param name The name of the dinosaur.
     * @param displayChar The displaying character of the dinosaur.
     * @param hitPoints The hit points of the dinosaur.
     */
    protected Dinosaur(String name, char displayChar, int hitPoints, int waterLevel) {
        super(name, displayChar, hitPoints);
        this.waterLevel = waterLevel;

        this.dinoId = ++dinoCounter;
    }

    /**
     * Returns a set of classes representing the food that this Dinosaur can eat.
     *
     * @return Set of classes representing the food that this Dinosaur can eat
     */
    public Set<Class<?>> getValidFoods() {
        return new HashSet<>();
    }

    /**
     * During every turn, the dinosaur's age is incremented, and the if the age is higher or equal the adult growth stage,
     * it will add a capability ADULT and change its display character to uppercase.
     * Dinosaur will be labeled as fertile by adding capability FERTILE, if it fulfills the requirement to be fertile,
     * else the fertile status is removed.
     * If the dinosaur is pregnant, the turn of being pregnant is incremented, and if the turns being pregnant
     * has reached a certain number, it will lay a dinosaur egg of its kind and remove the pregnant capability.
     * The turns of being pregnant is also removed.
     * Every turn, the dinosaur will also lose 1 hit point, if its hit point reaches 0, it will be unconscious,
     * and the turns of being unconscious will be recorded and it will only do the do nothing action until the player
     * feeds and heal the dinosaur or the turns of being unconscious reached a certain number and it dies. The dinosaur
     * is then replace with its corpse.
     * If the dinosaur's hit points are below a certain threshold, it will add the hungry capability else the removed.
     * During every turn, the dinosaur will loop through a list of behaviours to behave which ever it meets the
     * requirement first and the action returned from the behaviour is executed by the dinosaur.
     * If there is no action returned from the list of behaviours, the dinosaur will do nothing.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return The action to be performed by the dinosaur this turn.
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        Location currentLocation = map.locationOf(this);
        int xLoc = currentLocation.x();
        int yLoc = currentLocation.y();

        age++;
        if (isAdult()) {
            name = this.getClass().getSimpleName();
            displayChar = Character.toUpperCase(displayChar);
        }

        // Label dinosaur as fertile if it is able to mate
        if (isFertile(map))
            System.out.printf("%s at (%d, %d) is looking for mating partners.%n", this, xLoc, yLoc);

        // Pregnancy
        if (hasCapability(ActorCapability.PREGNANT)) {
            turnsBeingPregnant++;
            if (isGivingBirth()) {
                System.out.println(new LayEggAction(getEgg()).execute(this, map));
                turnsBeingPregnant = 0;
            }
        }

        // Unconscious & Death
        hurt(1);
        dehydrate(1);

        if (!isConscious()) {
            System.out.printf("%s at (%d, %d) is unconscious!%n", name, xLoc, yLoc);
            turnsBeingUnconscious++;
            if (!isAlive())
                System.out.println(new DieAction(true).execute(this, map));
            return new DoNothingAction();
        } else
            turnsBeingUnconscious = 0;

        // Hunger
        if (isHungry())
            System.out.printf("%s at (%d, %d) is getting hungry!%n", this, xLoc, yLoc);

        // Thirst
        if (isThirsty())
            System.out.printf("%s at (%d, %d) is getting thirsty!%n", this, xLoc, yLoc);

        for (Behaviour factory : actionFactories) {
            Action action = factory.getAction(this, map);
            if (action != null)
                return action;
        }
        return new DoNothingAction();
    }

    /**
     * Allows actor to commit some actions when adjacent to the dinosaur.
     * Player is able to heal this dinosaur when standing beside it, by looping through the player's inventory
     * and if it can heal, it is added into the list of actions.
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return A list of executable actions to this dinosaur.
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = new Actions();

        if (otherActor.equals(GameWorld.getPlayer())) {
            for (Item item : otherActor.getInventory()) {
                if (item instanceof ConsumableItem && Objects.nonNull(this.accept((ConsumableItem) item)))
                    actions.add(new FeedAction(this, item));
            }
        }

        return actions;
    }

    /**
     * Abstract method for different dinosaurs to check if they are hungry.
     *
     * @return True if the hit points of dinosaur is below a certain integer.
     * False otherwise.
     */
    public abstract boolean isHungry();

    /**
     * Abstract method for different dinosaurs to check if they are thirsty.
     *
     * @return True if the water level of dinosaur is below a certain integer.
     * False otherwise.
     */
    public boolean isThirsty(){
        return waterLevel < 40;
    }

    /**
     * Check whether the location is safe to feed from
     * e.g. Check if there are predators etc.
     *
     * @param location The location to check for
     * @return true if location is safe, false otherwise
     */
    public boolean isSafeToEatFrom(Location location) {
        return true;
    }

    /**
     * Abstract method for getting a new dinosaur egg.
     *
     * @return A new dinosaur egg.
     */
    protected abstract Egg getEgg();

    /**
     * Checks if the Dinosaur is in adult growth stage with their age.
     *
     * @return True if age is larger or equal growth stage of adult dinosaur.
     * False otherwise.
     */
    protected abstract boolean isAdult();

    /**
     * Checks if the dinosaur has enough hit points to bread with other dinosaur.
     *
     * @return True if the hit points of dinosaur is above or equal a certain integer.
     * False otherwise.
     */
    protected abstract boolean canBreed();

    /**
     * Check if the dinosaur is still alive.
     *
     * @return True if dinosaur still have hit points.
     * False otherwise.
     */
    protected abstract boolean isAlive();

    /**
     * Check if dinosaur is ready to give birth.
     *
     * @return True if dinosaur pregnant turns has reached.
     * False otherwise.
     */
    protected abstract boolean isGivingBirth();

    /**
     * Returns the unique ID of the Dinosaur.
     *
     * @return The unique ID of the Dinosaur.
     */
    public int getDinoId() {
        return dinoId;
    }

    /**
     * Getter for dinosaur's hit points.
     *
     * @return Integer of dinosaur's hit points.
     */
    @Override
    public Integer getHitPoints() {
        return hitPoints;
    }

    /**
     * Getter for dinosaur's max hit points.
     *
     * @return Integer of dinosaur's max hit points.
     */
    @Override
    public Integer getMaxHitPoints() {
        return maxHitPoints;
    }

    /**
     * Getter for dinosaur's max water level capacity.
     *
     * @return Integer of dinosaur's max water level capacity.
     */
    public Integer getMaxWaterLevel() {
        return maxWaterLevel;
    }

    /**
     * Dehydrate actor's water level.
     *
     * If the Actor's waterLevel go down to zero, it will unconscious.
     *
     * @param points number of waterLevel to deduct.
     */
    public void dehydrate(int points) {
        waterLevel -= points;
    }

    /**
     * Quench actor's water level.
     *
     * Drinking sips from lake will increment actor's water level.
     *
     * @param points number of waterLevel to add.
     */
    public void quenchThirst(int points) {
        waterLevel += points;
        waterLevel = Math.min(waterLevel, maxWaterLevel);
    }

    /**
     * Perform actions that this Dinosaur displays before eating.
     */
    public void executePreEatingAction() {

    }

    /**
     * Check if dinosaur is conscious.
     *
     * @return True if dinosaur's hit points and water level are above 0.
     * False otherwise.
     */
    @Override
    public boolean isConscious() {
        return super.isConscious() && waterLevel > 0;
    }

    /**
     * Returns true if Dinosaur is fertile.
     * i.e. Has met all the requirements required as an individual to mate with others.
     *
     * @return True if the Dinosaur is fertile, otherwise false
     */
    public boolean isFertile(GameMap map) {
        return isConscious() && isAdult() && canBreed() && !hasCapability(ActorCapability.PREGNANT);
    }

    /**
     * Defines how Dinosaurs should be behave as a String.
     *
     * @return String representation of the Dinosaur
     */
    @Override
    public String toString() {
        return String.format("%s[%d]", name, dinoId);
    }

}