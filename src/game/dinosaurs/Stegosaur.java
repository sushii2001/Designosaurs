package game.dinosaurs;

import game.ActorCapability;
import game.Behaviour;
import game.Visitor;
import game.items.Corpse;
import game.items.Egg;
import game.items.Fruit;

import java.util.HashSet;
import java.util.Set;


/**
 * A herbivorous dinosaur.
 * It can eat fruits from bush and fruits from ground.
 * It is a prey for Allosaur dinosaurs.
 *
 * @author Ci Leong Ong
 * @author Toh Zhen Kang
 */
public class Stegosaur extends Dinosaur {

	/**
	 * Name of the dinosaur.
	 */
	private static final String NAME = "Stegosaur";

	/**
	 * Display character of the dinosaur.
	 */
	private static final char DISPLAY_CHAR = 'S';

	/**
	 * Max hit points for a Stegosaur.
	 */
	private static final int MAX_HP = 100;

	/**
	 * Max water level for a Stegosaur.
	 */
	private static final int MAX_WL = 100;

	/**
	 * Adult stage age for a Stegosaur.
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
	 * Constructor for constructing a Stegosaur dinosaur.
	 *
	 * @param name Name of Stegosaur.
	 * @param displayChar Display character of Stegosaur.
	 * @param hitPoints The hit points of Stegosaur.
	 */
	private Stegosaur(String name, char displayChar, int hitPoints, int waterLevel) {
		super(name, displayChar, hitPoints, waterLevel);
		maxHitPoints = MAX_HP;
		maxWaterLevel = MAX_WL;
		addCapability(ActorCapability.SHORT);
		addCapability(ActorCapability.PREY);
		actionFactories = new Behaviour[] {
				new MatingBehaviour(),
				new ThirstyBehaviour(),
				new HungryBehaviour(),
				new WanderBehaviour()
		};
	}

	/**
	 * Static method for creating a new Stegosaur.
	 * This static method is able to specify the gender.
	 *
	 * @param growthStage The growth stage of the Stegosaur.
	 * @param gender The gender of the Stegosaur.
	 * @return A Stegosaur.
	 */
	public static Stegosaur getNewDino(String growthStage, char gender) {
		if (gender != 'M' && gender != 'F')
			throw new IllegalArgumentException("gender must be either 'M' or 'F'");

		if (!growthStage.equals("baby") && !growthStage.equals("adult"))
			throw new IllegalArgumentException("growthStage must be either \"baby\" or \"adult\"");

		Stegosaur newStegosaur;
		if (growthStage.equals("baby")) {
			newStegosaur = new Stegosaur("Baby " + NAME,
					Character.toLowerCase(DISPLAY_CHAR), BABY_STARTING_HP, BABY_STARTING_WL);
			newStegosaur.age = 0;
		} else {
			newStegosaur = new Stegosaur(NAME, DISPLAY_CHAR, ADULT_STARTING_HP, ADULT_STARTING_WL);
			newStegosaur.age = ADULT_AGE;
		}

		if (gender == 'F')
			newStegosaur.addCapability(ActorCapability.FEMALE);

		return newStegosaur;
	}

	/**
	 * Static method for creating Stegosaur.
	 * This static method will randomised a gender for the dinosaur.
	 *
	 * @param growthStage The growth stage of the Stegosaur.
	 * @return A Stegosaur.
	 */
	public static Stegosaur getNewDino(String growthStage) {
		return getNewDino(growthStage, rand.nextBoolean() ? 'M' : 'F');
	}

	/**
	 * Checks if the Dinosaur is in adult growth stage with their age.
	 *
	 * @return True if age is larger or equal growth stage of adult Stegosaur.
	 * False otherwise.
	 */
	@Override
	public boolean isAdult() {
		return age >= ADULT_AGE;
	}

	/**
	 * Checks if Stegosaur is hungry.
	 *
	 * @return True if the hit points of Stegosaur is below 90.
	 * False otherwise.
	 */
	@Override
	public boolean isHungry() {
		return getHitPoints() < 90;
	}

	/**
	 * Checks if Stegosaur has enough hit points to bread with other dinosaur.
	 *
	 * @return True if the hit points of Stegosaur is above or equal 50.
	 * False otherwise.
	 */
	@Override
	public boolean canBreed() {
		return getHitPoints() >= 50;
	}

	/**
	 * Checks if Stegosaur is alive with turns they have been unconscious.
	 *
	 * @return True if the turns they have been unconscious are less than 20.
	 * False otherwise.
	 */
	@Override
	public boolean isAlive() {
		return turnsBeingUnconscious < 20;
	}

	/**
	 * Checks if Stegosaur is giving birth
	 *
	 * @return True if the turns of being pregnant is larger or equal 10.
	 * False otherwise.
	 */
	@Override
	public boolean isGivingBirth() {
		return turnsBeingPregnant >= 10;
	}

	/**
	 * Get a new Stegosaur egg.
	 *
	 * @return A new Stegosaur egg.
	 */
	@Override
	protected Egg getEgg() {
		return Egg.getEgg(getNewDino("baby"));
	}

	/**
	 * Get the corpse of Stegosaur.
	 *
	 * @return Corpse of Stegosaur.
	 */
	@Override
	public Corpse getCorpse() {
		return new Corpse("Dead " + name, 40, 50);
	}

	/**
	 * Accept the value that is meant for Stegosaur
	 *
	 * @param visitor Stegosaur
	 * @param <T> value data type
	 * @return value
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
			add(Fruit.class);
		}};
	}
}