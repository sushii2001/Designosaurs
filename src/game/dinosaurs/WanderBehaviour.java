package game.dinosaurs;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.Behaviour;


/**
 * Allows Actors to wander around randomly without a clear direction.
 */
public class WanderBehaviour implements Behaviour {

	/**
	 * Random number generator
	 */
	private final Random random = new Random();

	/**
	 * Returns a MoveAction to wander to a random location, if possible.  
	 * If no movement is possible, returns null.
	 * 
	 * @param dinosaur the Actor enacting the behaviour
	 * @param map the map that actor is currently on
	 * @return an Action, or null if no MoveAction is possible
	 */
	@Override
	public Action getAction(Dinosaur dinosaur, GameMap map) {
		Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
		Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

		ArrayList<Action> actions = new ArrayList<>();

		for (Exit exit : map.locationOf(dinosaur).getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(dinosaur))
            	actions.add(exit.getDestination().getMoveAction(dinosaur, "around", exit.getHotKey()));
        }
		
		if (!actions.isEmpty())
			return actions.get(random.nextInt(actions.size()));
		else
			return null;

	}

}
