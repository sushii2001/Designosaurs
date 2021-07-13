package game.items;

import edu.monash.fit2099.engine.WeaponItem;
import game.ItemCapability;


/**
 * A RANGED WeaponItem that is capable of killing Dinosaurs
 * in one or two hits.
 *
 * @author Toh Zhen Kang
 * @author Ci Leong Ong
 */
public class LaserGun extends WeaponItem {

    /**
     * Constructor.
     * Damage is irrelevant as the damage is dependent on the target's max HP,
     * the logic is handled by ShootAction.
     */
    public LaserGun() {
        super("Laser Gun", 'L', Integer.MAX_VALUE, "zaps");
        addCapability(ItemCapability.RANGED_WEAPON);
    }

}
