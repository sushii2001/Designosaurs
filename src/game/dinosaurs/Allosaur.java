package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.ActorCapability;
import game.Behaviour;
import game.Visitor;
import game.items.Corpse;
import game.items.Egg;

import java.util.*;


/**
 * A carnivorous dinosaur.
 * It can attack Stegosaur when hungry.
 * It can eat corpse and eggs of dinosaurs.
 * It can eat tiny dinosaurs in one bite.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class Allosaur extends Dinosaur implements Huntable {

    /**
     * Maps the ID of the preys it has attacked to the number of turns
     * since it last attacked it.
     */
    public final Map<Integer, Integer> blacklist = new HashMap<>();

    /**
     * Name of the dinosaur.
     */
    private static final String NAME = "Allosaur";

    /**
     * Display character of the dinosaur.
     */
    private static final char DISPLAY_CHAR = 'A';

    /**
     * Max hit points for an Allosaur.
     */
    private static final int MAX_HP = 100;

    /**
     * Max water level for a Allosaur.
     */
    private static final int MAX_WL = 100;

    /**
     * Adult stage age for an Allosaur.
     */
    private static final int ADULT_AGE = 50;

    /**
     * Baby stage starting hit points.
     */
    private static final int BABY_STARTING_HP = 20;

    /**
     * Baby stage starting water level.
     */
    private static final int BABY_STARTING_WL = 30;

    /**
     * Adult stage starting hit points.
     */
    private static final int ADULT_STARTING_HP = 80;

    /**
     * Adult stage starting water level.
     */
    private static final int ADULT_STARTING_WL = 60;

    /**
     * Constructor for constructing an Allosaur dinosaur.
     *
     * @param name Name of Allosaur.
     * @param displayChar Display character of Allosaur.
     * @param hitPoints The hit points of Allosaur.
     */
    private Allosaur(String name, char displayChar, int hitPoints, int waterLevel) {
        super(name, displayChar, hitPoints, waterLevel);
        maxHitPoints = MAX_HP;
        maxWaterLevel = MAX_WL;
        actionFactories = new Behaviour[] {
                new MatingBehaviour(),
                new ThirstyBehaviour(),
                new PreyingBehaviour(),
                new HungryBehaviour(),
                new WanderBehaviour()
        };
    }

    /**
     * Static factory for creating a new Allosaur.
     * This static method is able to specify the gender.
     *
     * @param growthStage The growth stage of the Allosaur.
     * @param gender The gender of the Allosaur.
     * @return An Allosaur.
     */
    public static Allosaur getNewDino(String growthStage, char gender) {
        if (gender != 'M' && gender != 'F')
            throw new IllegalArgumentException("gender must be either 'M' or 'F'");

        if (!growthStage.equals("baby") && !growthStage.equals("adult"))
            throw new IllegalArgumentException("growthStage must be either \"baby\" or \"adult\"");

        Allosaur newAllosaur;
        if (growthStage.equals("baby")) {
            newAllosaur = new Allosaur("Baby " + NAME,
                    Character.toLowerCase(DISPLAY_CHAR), BABY_STARTING_HP, BABY_STARTING_WL);
            newAllosaur.age = 0;
        } else {
            newAllosaur = new Allosaur(NAME, DISPLAY_CHAR, ADULT_STARTING_HP, ADULT_STARTING_WL);
            newAllosaur.age = ADULT_AGE;
        }

        if (gender == 'F')
            newAllosaur.addCapability(ActorCapability.FEMALE);

        return newAllosaur;
    }

    /**
     * Static method for creating Allosaur.
     * This static method will randomised a gender for the dinosaur.
     *
     * @param growthStage The growth stage of the Allosaur.
     * @return An Allosaur.
     */
    public static Allosaur getNewDino(String growthStage) {
        return getNewDino(growthStage, rand.nextBoolean() ? 'M' : 'F');
    }

    /**
     * Returns the blacklist that maps the ID of the prey the hunter has attacks,
     * to the number of turns since the hunter last attacked it.
     *
     * @return The blacklist
     */
    @Override
    public Map<Integer, Integer> getBlacklist() {
        return blacklist;
    }

    /**
     * Adds the attacked prey to the blacklist.
     * Called after the hunter has attacked its prey.
     *
     * @param id The ID of the prey
     */
    @Override
    public void addToBlacklist(int id) {
        blacklist.put(id, 0);
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
    @Override
    public Set<Class<?>> getValidFoods() {
        return new HashSet<>() {{
            add(Egg.class);
            add(Corpse.class);
        }};
    }

    /**
     * Get Allosaur's intrinsic weapon when doing an attack action,
     * as it should not have a real weapon.
     * If the Allosaur is an adult Allosaur, it will attack and heal for 20 hit points.
     * If it is a baby Allosaur, it will only attack and heal for 10 hit points.
     *
     * @return Allosaur's Intrinsic weapon.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        if (isAdult()) {
            heal(20);
            return new IntrinsicWeapon(20, "attacks");
        }
        else {
            heal(10);
            return new IntrinsicWeapon(10, "attacks");
        }
    }

    /**
     * Every turn for Allosaur, it will check if the flag preys (previously attacked dinosaurs) has
     * been unharmed by this Allosaur for 20 turns. If true, remove from flag candidates, otherwise increase
     * the turns of left unharmed.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return The action to be performed by this Allosaur this turn.
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        blacklist.values().remove(20);

        if (!blacklist.isEmpty()){
            for (Map.Entry<Integer, Integer> entry : blacklist.entrySet())
                entry.setValue(entry.getValue() + 1);
        }

        return super.playTurn(actions,lastAction, map, display);
    }

    /**
     * Checks if the Dinosaur is in adult growth stage with their age.
     *
     * @return True if age is larger or equal growth stage of adult Allosaur.
     * False otherwise.
     */
    @Override
    public boolean isAdult() {
        return age >= ADULT_AGE;
    }

    /**
     * Checks if Allosaur is hungry.
     *
     * @return True if the hit points of Allosaur is below 90.
     * False otherwise.
     */
    @Override
    public boolean isHungry() {
        return getHitPoints() < 90;
    }

    /**
     * Checks if Allosaur has enough hit points to bread with other dinosaur.
     *
     * @return True if the hit points of Allosaur is above or equal 50.
     * False otherwise.
     */
    @Override
    public boolean canBreed() {
        return getHitPoints() >= 50;
    }

    /**
     * Checks if Allosaur is alive with turns they have been unconscious.
     *
     * @return True if the turns they have been unconscious are less than 15.
     * False otherwise.
     */
    @Override
    public boolean isAlive() {
        return turnsBeingUnconscious < 15;
    }

    /**
     * Checks if Allosaur is giving birth.
     *
     * @return True if the turns of being pregnant is larger or equal 20.
     * False otherwise.
     */
    @Override
    public boolean isGivingBirth() {
        return turnsBeingPregnant >= 20;
    }

    /**
     * Get a new Allosaur egg.
     *
     * @return A new Allosaur egg.
     */
    @Override
    protected Egg getEgg() {
        return Egg.getEgg(getNewDino("baby"));
    }

    /**
     * Get the corpse of Allosaur.
     *
     * @return Corpse of Allosaur.
     */
    @Override
    public Corpse getCorpse() {
        return new Corpse("Dead " + name, 20, 50);
    }

}
