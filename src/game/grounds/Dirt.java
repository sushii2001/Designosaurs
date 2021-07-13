package game.grounds;

import edu.monash.fit2099.engine.Ground;
import game.GroundCapability;


/**
 * A class that represents bare dirt.
 *
 * @author Ci Leong Ong
 */
public class Dirt extends Ground {

	/**
	 * Constructor.
	 */
	public Dirt() {
		super('.');
		addCapability(GroundCapability.FERTILE);
	}

}
