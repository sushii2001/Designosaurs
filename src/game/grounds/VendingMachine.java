package game.grounds;

import edu.monash.fit2099.engine.*;
import game.BuyAction;
import game.GameWorld;
import game.Player;
import game.dinosaurs.Allosaur;
import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Pterodactyl;
import game.dinosaurs.Stegosaur;
import game.items.*;

import java.util.HashMap;
import java.util.Map;


/**
 * The class that represents the vending machine on the map
 * that enables user to purchase items.
 *
 * @author Ci Leong Ong
 */
public class VendingMachine extends Ground {

    /**
     * Contains the names of products on sale as key,
     * and their price as values.
     */
    private final Map<String, Integer> productsOnSale;

    /**
     * Constructor.
     * Places all available products in the map.
     */
    public VendingMachine() {
        super('$');
        productsOnSale = new HashMap<>();
        productsOnSale.put("Fruit", 30);
        productsOnSale.put("VegetarianMealKit", 100);
        productsOnSale.put("CarnivoreMealKit", 500);
        productsOnSale.put("StegosaurEgg", 200);
        productsOnSale.put("BrachiosaurEgg", 500);
        productsOnSale.put("AllosaurEgg", 1000);
        productsOnSale.put("PterodactylEgg", 400);
        productsOnSale.put("LaserGun", 500);
        productsOnSale.put("WaterBottle", 100);
    }

    /**
     * Allows Player to purchase any item sold by the VendingMachine,
     * if he stands adjacent to it.
     *
     * @param actor The Actor that stands adjacent to the VendingMachine
     * @param location The current Location
     * @param direction The direction of the Ground from the Actor
     * @return A list of Action that contains a BuyAction for all Items sold, each
     */
    @Override
    public Actions allowableActions(Actor actor, Location location, String direction) {
        Actions actions = new Actions();
        if (actor.equals(GameWorld.getPlayer())) {
            for (Map.Entry<String, Integer> entry : productsOnSale.entrySet()) {
                Item item = nameToItem(entry.getKey());
                int price = entry.getValue();
                if (((Player) actor).canAfford(price))
                    actions.add(new BuyAction(item, price));
            }
        }
        return actions;
    }

    /**
     * Player can purchase from adjacent blocks.
     * It does not make sense for the Player to stand on the VendingMachine.
     *
     * @param actor the Actor to check
     * @return False
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Blocks all thrown object.
     *
     * @return True
     */
    @Override
    public boolean blocksThrownObjects() {
        return true;
    }

    /**
     * Converts the Item's name to its object.
     *
     * @param itemName Name of item
     * @return Item that corresponds to the given name
     */
    private Item nameToItem(String itemName) {
        return switch (itemName) {
            case "Fruit" -> new Fruit();
            case "VegetarianMealKit" -> new VegetarianMealKit();
            case "CarnivoreMealKit" -> new CarnivoreMealKit();
            case "StegosaurEgg" -> Egg.getEgg(Stegosaur.getNewDino("baby"));
            case "BrachiosaurEgg" -> Egg.getEgg(Brachiosaur.getNewDino("baby"));
            case "AllosaurEgg" -> Egg.getEgg(Allosaur.getNewDino("baby"));
            case "PterodactylEgg" -> Egg.getEgg(Pterodactyl.getNewDino("baby"));
            case "LaserGun" -> new LaserGun();
            case "WaterBottle" -> new WaterBottle();
            default -> null;
        };
    }

}
