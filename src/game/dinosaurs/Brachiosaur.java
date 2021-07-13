package game.dinosaurs;

import game.ActorCapability;
import game.Behaviour;
import game.Visitor;
import game.items.Corpse;
import game.items.Egg;


/**
 * A herbivorous Dinosaur.
 * It can trample Bushes when it steps on it.
 * It can only eat Fruits in Trees.
 *
 * @author Ci Leong Ong
 */
public class Brachiosaur extends Dinosaur {

    /**
     * Name for this Dinosaur.
     */
    private static final String NAME = "Brachiosaur";

    /**
     * Display character in game.
     */
    private static final char DISPLAY_CHAR = 'B';

    /**
     * Max hit points for a Brachiosaur.
     */
    private static final int MAX_HP = 160;

    /**
     * Max water level for a Brachiosaur.
     */
    private static final int MAX_WL = 200;

    /**
     * Adult stage age for a Brachiosaur.
     */
    private static final int ADULT_AGE = 50;

    /**
     * Baby stage starting hit points.
     */
    private static final int BABY_STARTING_HP = 10;

    /**
     * Baby stage starting water level.
     */
    private static final int BABY_STARTING_WL = 40;

    /**
     * Adult stage starting hit points.
     */
    private static final int ADULT_STARTING_HP = 100;

    /**
     * Adult stage starting water level.
     */
    private static final int ADULT_STARTING_WL = 60;

    /**
     * Constructor for constructing Brachiosaur dinosaur.
     *
     * @param name Name of the dinosaur.
     * @param displayChar Display character of the dinosaur.
     * @param hitPoints Hit points of the dinosaur.
     */
    private Brachiosaur(String name, char displayChar, int hitPoints, int waterLevel) {
        super(name, displayChar, hitPoints, waterLevel);
        maxHitPoints = MAX_HP;
        maxWaterLevel = MAX_WL;
        addCapability(ActorCapability.TALL);
        addCapability(ActorCapability.CRUSH_GROUND);
        actionFactories = new Behaviour[] {
                new MatingBehaviour(),
                new ThirstyBehaviour(),
                new HungryBehaviour(),
                new WanderBehaviour()
        };
    }

    /**
     * Static method for creating a new Brachiosaur.
     * This static method is able to specify the gender.
     *
     * @param growthStage The growth stage of the Brachiosaur.
     * @param gender The gender of the Brachiosaur.
     * @return A Brachiosaur.
     */
    public static Brachiosaur getNewDino(String growthStage, char gender) {
        if (gender != 'M' && gender != 'F')
            throw new IllegalArgumentException("gender must be either 'M' or 'F'");

        if (!growthStage.equals("baby") && !growthStage.equals("adult"))
            throw new IllegalArgumentException("growthStage must be either \"baby\" or \"adult\"");

        Brachiosaur newBrachiosaur;
        if (growthStage.equals("baby")) {
            newBrachiosaur = new Brachiosaur("Baby " + NAME,
                    Character.toLowerCase(DISPLAY_CHAR), BABY_STARTING_HP, BABY_STARTING_WL);
            newBrachiosaur.age = 0;
        } else {
            newBrachiosaur = new Brachiosaur(NAME, DISPLAY_CHAR, ADULT_STARTING_HP, ADULT_STARTING_WL);
            newBrachiosaur.age = ADULT_AGE;
        }

        if (gender == 'F')
            newBrachiosaur.addCapability(ActorCapability.FEMALE);

        return newBrachiosaur;
    }

    /**
     * Static method for creating Brachiosaur.
     * This static method will randomised a gender for the dinosaur.
     *
     * @param growthStage The growthStage of the Brachiosaur.
     * @return A Brachiosaur.
     */
    public static Brachiosaur getNewDino(String growthStage) {
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
     * Checks if the Dinosaur is in adult growth stage with their age.
     *
     * @return True if age is larger or equal growth stage of adult Brachiosaur.
     * False otherwise.
     */
    @Override
    public boolean isAdult() {
        return age >= ADULT_AGE;
    }

    /**
     * Checks if Brachiosaur is hungry.
     *
     * @return True if the hit points of Brachiosaur is below 140.
     * False otherwise.
     */
    @Override
    public boolean isHungry() {
        return getHitPoints() < 140;
    }

    /**
     * Checks if Brachiosaur has enough hit points to bread with other dinosaur.
     *
     * @return True if the hit points of Brachiosaur is above or equal 70.
     * False otherwise.
     */
    @Override
    public boolean canBreed() {
        return getHitPoints() >= 70;
    }

    /**
     * Checks if Brachiosaur is alive with turns they have been unconscious.
     *
     * @return True if the turns they have been unconscious are less than 15.
     * False otherwise.
     */
    @Override
    public boolean isAlive() {
        return turnsBeingUnconscious < 15;
    }

    /**
     * Checks if Brachiosaur is giving birth
     *
     * @return True if the turns of being pregnant is larger or equal 30.
     * False otherwise.
     */
    @Override
    public boolean isGivingBirth() {
        return turnsBeingPregnant >= 30;
    }

    /**
     * Get a new Brachiosur egg.
     *
     * @return A new Brachiosaur egg.
     */
    @Override
    protected Egg getEgg() {
        return Egg.getEgg(getNewDino("baby"));
    }

    /**
     * Get the corpse of Brachiosaur.
     *
     * @return Corpse of Brachiosaur.
     */
    @Override
    public Corpse getCorpse() {
        return new Corpse("Dead " + name, 40, 100);
    }

}