package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.ActorCapability;
import game.Behaviour;
import game.GroundCapability;
import game.Visitor;
import game.items.*;

import java.util.HashSet;
import java.util.Set;


/**
 * A tiny flying dinosaur.
 * It can fly across the map and transverse through water to eat fish and quench thirst.
 * Can be eaten by Allosaur as whole.
 * It can eat corpse of dinosaurs but slowly.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class Pterodactyl extends Dinosaur {

    /**
     * Identify this dinosaur's current location
     */
    private Location currentLocation;

    /**
     * Remaining flying turns of this dinosaur.
     */
    private int flyingTurns;

    /**
     * Name of the dinosaur.
     */
    private static final String NAME = "Pterodactyl";

    /**
     * Display character of the dinosaur.
     */
    private static final char DISPLAY_CHAR = 'P';

    /**
     * Max hit points for an Pterodactyl.
     */
    private static final int MAX_HP = 100;

    /**
     * Max water level for a Pterodactyl.
     */
    private static final int MAX_WL = 100;

    /**
     * Adult stage age for an Pterodactyl.
     */
    private static final int ADULT_AGE = 30;

    /**
     * Baby stage starting hit points.
     */
    private static final int BABY_STARTING_HP = 10;

    /**
     * Baby stage starting water level.
     */
    private static final int BABY_STARTING_WL = 30;

    /**
     * Adult stage starting hit points.
     */
    private static final int ADULT_STARTING_HP = 50;

    /**
     * Adult stage starting water level.
     */
    private static final int ADULT_STARTING_WL = 60;

    /**
     * Constructor for creating dinosaurs.
     *
     * @param name        The name of the dinosaur.
     * @param displayChar The displaying character of the dinosaur.
     * @param hitPoints   The hit points of the dinosaur.
     */
    private Pterodactyl(String name, char displayChar, int hitPoints, int waterLevel) {
        super(name, displayChar, hitPoints, waterLevel);
        maxHitPoints = MAX_HP;
        maxWaterLevel = MAX_WL;
        addCapability(ActorCapability.TINY);
        addCapability(ActorCapability.PREY);
        actionFactories = new Behaviour[] {
                new RechargeSkillBehaviour(ActorCapability.FLY, GroundCapability.RECHARGE_FLIGHT),
                new MatingBehaviour(),
                new ThirstyBehaviour(),
                new HungryBehaviour(),
                new WanderBehaviour()
        };
    }

    /**
     * Static method for creating a new Pterodactyl.
     * This static method is able to specify the gender.
     *
     * @param growthStage The growth stage of the Pterodactyl.
     * @param gender The gender of the Pterodactyl.
     * @return A Pterodactyl.
     */
    public static Pterodactyl getNewDino(String growthStage, char gender) {
        if (gender != 'M' && gender != 'F')
            throw new IllegalArgumentException("gender must be either 'M' or 'F'");

        if (!growthStage.equals("baby") && !growthStage.equals("adult"))
            throw new IllegalArgumentException("growthStage must be either \"baby\" or \"adult\"");

        Pterodactyl newPterodactyl;
        if (growthStage.equals("baby")) {
            newPterodactyl = new Pterodactyl("Baby " + NAME,
                    Character.toLowerCase(DISPLAY_CHAR), BABY_STARTING_HP, BABY_STARTING_WL);
            newPterodactyl.age = 0;
        } else {
            newPterodactyl = new Pterodactyl(NAME, DISPLAY_CHAR, ADULT_STARTING_HP, ADULT_STARTING_WL);
            newPterodactyl.age = ADULT_AGE;
        }

        if (gender == 'F')
            newPterodactyl.addCapability(ActorCapability.FEMALE);

        return newPterodactyl;
    }

    /**
     * Static method for creating Pterodactyl.
     * This static method will randomised a gender for the dinosaur.
     *
     * @param growthStage The growth stage of the Pterodactyl.
     * @return A Pterodactyl.
     */
    public static Pterodactyl getNewDino(String growthStage) {
        return getNewDino(growthStage, rand.nextBoolean() ? 'M' : 'F');
    }

    /**
     * Accepts the visitor, and calls the visit() method of it,
     * to perform the declared operation for the visitable.
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Returns a set of classes representing the food that this Dinosaur can eat.
     *
     * @return Set of classes representing the food that this Dinosaur can eat
     */
    public Set<Class<?>> getValidFoods() {
        return new HashSet<>() {{
            add(Corpse.class);
            add(Egg.class);
        }};
    }

    /**
     * Every turn for Pterodactyl, it will check if this Pterodactyl should has the capability fly
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return The action to be performed by this Allosaur this turn.
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        this.currentLocation = map.locationOf(this);
        setFlyingTurns();
        if (this.flyingTurns > 0)
            addCapability(ActorCapability.FLY);

        System.out.println("Can Fly" + this.hasCapability(ActorCapability.FLY));

        return super.playTurn(actions,lastAction, map, display);
    }

    /**
     * Determines Pterodactyl flying turns.
     * Decrements if above 30, restore to 30 if it is on tree, else remove capability fly when below or equal 0.
     */
    private void setFlyingTurns() {
        if (flyingTurns > 0 && !currentLocation.getGround().hasCapability(GroundCapability.RECHARGE_FLIGHT))
            flyingTurns--;
        if (currentLocation.getGround().hasCapability(GroundCapability.RECHARGE_FLIGHT))
            flyingTurns = 30;
        else if (flyingTurns <= 0)
            removeCapability(ActorCapability.FLY);
    }

    /**
     * Perform actions that Pterodactyls display before eating.
     */
    @Override
    public void executePreEatingAction() {
        removeCapability(ActorCapability.FLY);
    }

    /**
     * Checks if the Dinosaur is in adult growth stage with their age.
     *
     * @return True if age is larger or equal growth stage of adult Pterodactyl.
     * False otherwise.
     */
    @Override
    protected boolean isAdult() {
        return age >= ADULT_AGE;
    }

    /**
     * Checks if Pterodactyl is hungry.
     *
     * @return True if the hit points of Pterodactyl is below 90.
     * False otherwise.
     */
    @Override
    public boolean isHungry() {
        return getHitPoints() < 90;
    }

    /**
     * Check whether the location is safe to feed from
     * e.g. Check if there are predators etc.
     *
     * @param location The location to check for
     * @return true if location is safe, false otherwise
     */
    @Override
    public boolean isSafeToEatFrom(Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.containsAnActor() && !destination.getActor().equals(this))
                return false;
        }

        return true;
    }

    /**
     * Checks if Pterodactyl has enough hit points to bread with other dinosaur.
     *
     * @return True if the hit points of Pterodactyl is above or equal 50.
     * False otherwise.
     */
    @Override
    protected boolean canBreed() {
        return this.getHitPoints() >= 50;
    }

    /**
     * Checks if Pterodactyl is alive with turns they have been unconscious.
     *
     * @return True if the turns they have been unconscious are less than 20.
     * False otherwise.
     */
    @Override
    protected boolean isAlive() {
        return turnsBeingUnconscious < 20;
    }

    /**
     * Checks if Pterodactyl is giving birth
     *
     * @return True if the turns of being pregnant is larger or equal 10.
     * False otherwise.
     */
    @Override
    protected boolean isGivingBirth() {
        return (this.turnsBeingPregnant >= 10
                && (this.currentLocation.getGround().hasCapability(GroundCapability.RECHARGE_FLIGHT))) ;
    }

    /**
     *
     * Get a new Pterodactyl egg.
     *
     * @return A new Pterodactyl egg.
     */
    @Override
    protected Egg getEgg() {
        return Egg.getEgg(getNewDino("baby"));
    }

    /**
     * Get the corpse of Pterodactyl.
     *
     * @return Corpse of Pterodactyl.
     */
    @Override
    public Corpse getCorpse() {
        return new Corpse("Dead " + name, 40, 30);
    }

    /**
     * Returns true if Dinosaur is fertile.
     * i.e. Has met all the requirements required as an individual to mate with others.
     *
     * @return True if the Dinosaur is fertile, otherwise false
     */
    public boolean isFertile(GameMap map) {
        return super.isFertile(map) && map.locationOf(this).getGround().hasCapability(GroundCapability.RECHARGE_FLIGHT);
    }

}
