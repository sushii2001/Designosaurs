package game;

import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.GroundFactory;
import edu.monash.fit2099.engine.Location;

import java.util.List;


/**
 * Represents the map that the Player is interacting with.
 *
 * @author Ci Leong Ong
 */
class DinosaurMap extends GameMap {

    /**
     * Constructor that creates a map from a sequence of ASCII strings.
     *
     * @param groundFactory Factory to create Ground objects
     * @param lines         List of Strings representing rows of the map
     */
    DinosaurMap(GroundFactory groundFactory, List<String> lines) {
        super(groundFactory, lines);
    }

    /**
     * Update all Locations in the map based on the rules
     * that will be used to update the map every turn.
     */
    void updateInitialGround() {
        for (int x : widths) {
            for (int y : heights) {
                GameLocation here = (GameLocation) map[x][y];
                here.updateLocation();
            }
        }
    }

    /**
     * Connect the maps by either their x-axes or y-axes.
     * Actors can traverse through the connected maps as if they are the same map,
     * except when Player is on one map, the other will not be displayed.
     *
     * @param m1 Map 1 (if connected by x-axes, it will be at North, otherwise at West)
     * @param m2 Map 2
     * @param by The axis to connect the maps by
     */
    static void connectMaps(GameMap m1, GameMap m2, char by) {
        if (by == 'x') {
            if (m1.getXRange().max() != m2.getXRange().max())
                throw new RuntimeException("The length of the connecting axes does not match.");
            for (int x : m1.getXRange())
                m1.at(x, m1.getYRange().max()).addExit(new Exit("South", m2.at(x, 0), "2"));
            for (int x : m2.getXRange())
                m2.at(x, 0).addExit(new Exit("North", m1.at(x, m1.getYRange().max()), "8"));
        } else if (by == 'y') {
            if (m1.getYRange().max() != m2.getYRange().max())
                throw new RuntimeException("The length of the connecting axes does not match.");
            for (int y : m1.getYRange())
                m1.at(m1.getXRange().max(), y).addExit(new Exit("East", m2.at(0, y), "6"));
            for (int y : m2.getYRange())
                m2.at(0, y).addExit(new Exit("West", m1.at(m1.getXRange().max(), y), "4"));
        } else
            throw new IllegalArgumentException("Value of argument by cannot only be either 'x' or 'y'.");
    }

    /**
     * Overrides makeNewLocation of GameMap to use the
     * customised GameLocation that allows updates on
     * Ground per turn based on defined rules.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return A new GameLocation
     */
    @Override
    protected Location makeNewLocation(int x, int y) {
        return new GameLocation(this, x, y);
    }
}
