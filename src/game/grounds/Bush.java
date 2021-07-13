package game.grounds;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import game.ActorCapability;
import game.GroundCapability;
import game.items.Fruit;


/**
 * The class that represents the displayed Bush.
 * Some Actors can search the Bush for Fruit and heal.
 *
 * @author Ci Leong Ong
 */
public class Bush extends SearchableGround {

    /**
     * Constructor.
     */
    public Bush() {
        super('*');
        addCapability(GroundCapability.CRUSHABLE);
        addCapability(GroundCapability.SHORT_PLANT);
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
        if (target.hasCapability(ActorCapability.SHORT)) {
            int hitPoints = target.getHitPoints();
            int maxHitPoints = target.getMaxHitPoints();

            int amount;
            if (!scoutOnly)
                amount = removeItems(Fruit.class, (int) Math.ceil((double) (maxHitPoints - hitPoints) / 10));
            else
                amount = (int) getItemList().stream().filter(Fruit.class::isInstance).count();
            return amount>0 ? amount*10 : null;
        } else
            return null;
    }

    /**
     * On any turn, has 10% chance to produce one ripe fruit.
     *
     * @param location The Location of the Ground
     */
    @Override
    public void tick(Location location) {
        super.tick(location);

        if (rand.nextDouble() <= 0.1)
            itemList.add(new Fruit());
    }

}
