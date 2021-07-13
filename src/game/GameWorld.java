package game;

import edu.monash.fit2099.engine.*;
import game.dinosaurs.Dinosaur;
import game.grounds.WaterBody;

import java.util.Objects;
import java.util.Random;


/**
 * The world that all the maps are in,
 * represents the virtual world that we are interacting with.
 * The world stops running when player is absent.
 *
 * @author Ci Leong Ong
 */
public class GameWorld extends World {

    /**
     * Used to simulate probability.
     */
    private static final Random rand = new Random();

    /**
     * Stores the only Player in the game.
     */
    private static Player player;

    /**
     * Number of turns since start of the game.
     */
    private int turns;

    /**
     * Maximum number of turns before the game ends.
     * Only used in Challenge Mode.
     */
    private final Integer maxTurns;

    /**
     * The target eco points if reached before maxTurns,
     * ends the game. Only used in Challenge Mode.
     */
    private final Integer winningPoints;

    /**
     * Constructor.
     *
     * @param display the Display that will display this World.
     */
    public GameWorld(Display display, Integer maxTurns, Integer winningPoints) {
        super(display);
        this.maxTurns = maxTurns;
        this.winningPoints = winningPoints;
        turns = 0;
    }

    /**
     * Store the Player to be accessed later.
     * The map is drawn just before the Player's turn
     *
     * @param player   The player to add
     * @param location The Location where the player is to be added
     */
    public void addPlayer(Player player, Location location) {
        super.addPlayer(player, location);
        GameWorld.player = player;
    }

    /**
     * Returns the only player in the world,
     * so we do not have to loop through all actors and find the player.
     *
     * @return The only player in the game
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Run the game.
     *
     * On each iteration the loop does the following: - displays the player's
     * map - processes the actions of every Actor in the game, regardless of map
     *
     * Every 10 turns, there is a chance of raining that will add water points to
     * unconscious Dinosaurs and add sips to the water bodies.
     *
     * @throws IllegalStateException if the player doesn't exist
     */
    @Override
    public void run() {
        if (player == null)
            throw new IllegalStateException();

        // initialize the last action map to nothing actions;
        for (Actor actor : actorLocations)
            lastActionMap.put(actor, new DoNothingAction());

        // This loop is basically the whole game
        while (stillRunning()) {
            GameMap playersMap = actorLocations.locationOf(player).map();
            playersMap.draw(display);

            int sipsAdded = 0;
            int rainDrops = 0;
            if (turns%10 == 0 && rand.nextDouble() <= 0.2) {
                System.out.println("###   It's raining!   ###");
                double rainfall = rand.nextDouble() * 0.5 + 0.1;
                sipsAdded = (int) Math.floor(rainfall * 20);
                rainDrops = 10;
            }

            // Display number of moves left
            if (isChallengeMode())
                System.out.printf("###   %d moves left!   ###%n", maxTurns - turns);

            // Process all the actors.
            for (Actor actor : actorLocations) {
                if (stillRunning())
                    processActorTurn(actor);
            }

            // Tick over all the maps. For the map stuff.
            for (GameMap gameMap : gameMaps) {
                gameMap.tick();
                for (int x : gameMap.getXRange()) {
                    for (int y: gameMap.getYRange()) {
                        Location here = gameMap.at(x, y);
                        if (here.getGround() instanceof WaterBody)
                            ((WaterBody) here.getGround()).addSips(sipsAdded);
                        if (here.containsAnActor() && !here.getActor().equals(player))
                            ((Dinosaur) here.getActor()).quenchThirst(rainDrops);
                    }
                }
            }

            turns++;
        }

        display.println(endGameMessage());
    }

    /**
     * Returns true if the game is still running.
     * The game is considered to still be running if the player is still around.
     * If the game is running in Challenge Mode, checks whether the target points is reached,
     * or has exceeded the maximum number of turns.
     *
     * @return true if the player is still on the map.
     */
    @Override
    protected boolean stillRunning() {
        if (isChallengeMode())
            return super.stillRunning() && !player.canAfford(winningPoints) && turns < maxTurns;
        else
            return super.stillRunning();
    }

    /**
     * Return a string that can be displayed when the game ends.
     * If the game is running in Challenge Mode,
     * returns Player won/lost depending on eco points earned before the game ended.
     * Otherwise returns "Game Over".
     *
     * @return End game message
     */
    @Override
    protected String endGameMessage() {
        if (isChallengeMode()) {
            if (player.canAfford(winningPoints))
                return "###   Player won!   ###";
            else
                return "###   Player lost.   ###";
        } else
            return super.endGameMessage();
    }

    /**
     * Checks whether the World is running on Challenge Mode.
     *
     * @return true if on Challenge mode, false otherwise.
     */
    private boolean isChallengeMode() {
        return Objects.nonNull(maxTurns) && Objects.nonNull(winningPoints);
    }

}
