package game.dinosaurs;

import edu.monash.fit2099.engine.*;
import game.Behaviour;
import game.ComputeDistance;

import java.util.Objects;


/**
 * A class that figures out a MoveAction that will move the actor one step 
 * closer to a target Actor.
 *
 * @author Ci Leong Ong
 */
public class FollowBehaviour implements Behaviour, ComputeDistance {

	/**
	 * Target location that the Actor is trying to get to
	 */
	private final Location objective;

	/**
	 * Constructor.
	 * 
	 * @param destination the Actor to follow
	 */
	public FollowBehaviour(Location destination) {
		Objects.requireNonNull(destination, "Expected Location type argument, but null is received.");
		this.objective = destination;
	}

	@Override
	public Action getAction(Dinosaur dinosaur, GameMap map) {
		Objects.requireNonNull(dinosaur, "Expected Dinosaur type argument, but null is received.");
		Objects.requireNonNull(map, "Expected GameMap type argument, but null is received.");

		Location here = map.locationOf(dinosaur);
		Location there = objective;

		int currentDistance = distanceBetween(here, there);
		for (Exit exit : here.getExits()) {
			Location destination = exit.getDestination();
			if (destination.canActorEnter(dinosaur)) {
				int newDistance = distanceBetween(destination, there);
				if (newDistance < currentDistance)
					return new MoveActorAction(destination, exit.getName());
			}
		}
		return null;
	}

}