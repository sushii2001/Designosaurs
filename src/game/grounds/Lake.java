package game.grounds;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import game.ActorCapability;
import game.items.Fish;

import java.util.Collections;


/**
 * A place where fish prospers and Actors can drink water and eat fish from.
 *
 * @author Ci Leong Ong
 */
public class Lake extends SearchableGround implements WaterBody {

    /**
     * Number of times an Actor can drink from here.
     * Replenished by rain.
     */
    private int sips;

    /**
     * Constructor.
     */
    public Lake() {
        super('~');
        sips = 25;
        itemList.addAll(Collections.nCopies(5, new Fish()));
    }

    /**
     * When there are less than 25 Fish, have 60% chance to add a new Fish every turn.
     *
     * @param location The location here
     */
    @Override
    public void tick(Location location) {
        super.tick(location);

        if (itemList.size() < 25 && rand.nextDouble() <= 0.6)
            itemList.add(new Fish());
    }

    /**
     * Returns the amount of food healing points available to the target Actor.
     * Returns null if the ground is not a valid food source for the Actor.
     *
     * @param target    The Actor that will be feeding on here
     * @param scoutOnly True if just checking whether this spot is valid, false if feeding on it
     * @return The food healing points available to the target Actor
     */
    @Override
    public Integer getFoodPoints(Actor target, boolean scoutOnly) {
        if (target.hasCapability(ActorCapability.FLY)) {
            int amount;
            if (!scoutOnly)
                amount = removeItems(Fish.class, rand.nextInt(2));
            else
                amount = (int) getItemList().stream().filter(Fish.class::isInstance).count();
            return amount>0 ? amount*5 : 0;
        } else
            return 0;
    }

    /**
     * Returns the amount of water healing points available to the target Actor.
     * Returns null if the ground is not a valid water source for the Actor.
     *
     * @param target    The Actor that will be drinking from here
     * @param scoutOnly True if just checking whether this spot is valid, false if drinking from it
     * @return The water healing points available to the target Actor
     */
    @Override
    public Integer getWaterPoints(Actor target, boolean scoutOnly) {
        if (!scoutOnly)
            decrementSips();
        return target.hasCapability(ActorCapability.FLY) ? 30 : 0;
    }

    /**
     * Return true if the water body allows Actors to drink from,
     * i.e. has sips left.
     *
     * @return true if the water body allows Actors to drink from, otherwise false
     */
    @Override
    public boolean hasSips() {
        return sips > 0;
    }

    /**
     * Add amount of sips to the pool of water.
     *
     * @param amount Amount of sips to be added to the ground.
     */
    @Override
    public void addSips(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException();
        sips += amount;
    }

    /**
     * Decrement number of sips by 1.
     */
    @Override
    public void decrementSips() {
        sips--;
    }

    /**
     * Only allows flying Actors to enter.
     *
     * @param actor the Actor to check
     * @return true if the Actor is flying, false otherwise
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return actor.hasCapability(ActorCapability.FLY);
    }
}
