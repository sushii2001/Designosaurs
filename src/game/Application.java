package game;

import java.util.*;

import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.FancyGroundFactory;
import game.dinosaurs.Allosaur;
import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Pterodactyl;
import game.dinosaurs.Stegosaur;
import game.grounds.*;


/**
 * The main operational logic of the game.
 *
 * @author Ci Leong Ong
 */
public class Application {

	/**
	 * Maximum number of turns before the game ends.
	 * Only used in Challenge Mode.
	 */
	private static Integer maxTurns;

	/**
	 * The target eco points if reached before maxTurns,
	 * ends the game. Only used in Challenge Mode.
	 */
	private static Integer winningPoints;

	/**
	 * The driver method.
	 */
	public static void main(String[] args) {
		while (true) {
			int gameMode = promptGameMode();

			if (gameMode == 3)
				break;

			GameWorld world = new GameWorld(new Display(), maxTurns, winningPoints);

			FancyGroundFactory groundFactory =
					new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(), new Lake(), new VendingMachine());

			List<String> map1 = Arrays.asList(
					"................................................................................",
					"................................................................................",
					"................................................................................",
					"................................................................................",
					"...............~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~...............",
					"...............~################################################~...............",
					"...............~#______________________________________________#~...............",
					"...............~#___#####__#####___####___###___#___#__####____#~...............",
					"...............~#___#______#______#______#___#__##__#__#___#___#~...............",
					"...............~#___#####__#####__#______#___#__#_#_#__#___#___#~...............",
					"...............~#_______#__#______#______#___#__#__##__#___#___#~...............",
					"...............~#___#####__#####___####___###___#___#__####____#~...............",
					"...............~#______________________________________________#~...............",
					"...............~#_____________#___#____#____####_______________#~...............",
					"...............~#_____________##_##___#_#___#___#______________#~...............",
					"...............~#_____________#_#_#__#####__####_______________#~...............",
					"...............~#_____________#___#__#___#__#__________________#~...............",
					"...............~#_____________#___#__#___#__#__________________#~...............",
					"...............~#______________________________________________#~...............",
					"...............~################################################~...............",
					"...............~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~...............",
					"................................................................................",
					"................................................................................",
					"................................................................................",
					"................................................................................");

			DinosaurMap triassicMap = new DinosaurMap(groundFactory, map1);
			world.addGameMap(triassicMap);

			List<String> map2 = Arrays.asList(
					"................................................................................",
					"................................................................................",
					".....#######....................................................................",
					".....#_____#....................................................................",
					".....#_____#....................................................................",
					".....###.###....................................................................",
					".......$........................................................................",
					"......................................+++.......................................",
					".......................................++++.....................................",
					"...................................+++++........................................",
					".....................................++++++.....................................",
					"......................................+++.......................................",
					".....................................+++........................................",
					"................................................................................",
					"............+++.................................................................",
					".............+++++........................~~~...................................",
					"...............++........................~~~~~~..........+++++..................",
					".............+++........................~~~~~~~~~......++++++++.................",
					"............+++.........................~~~~~~~.........+++.....................",
					"..........................................~~~~..................................",
					".........................................................................++.....",
					"........................................................................++.++...",
					".........................................................................++++...",
					"..........................................................................++....",
					"................................................................................");

			DinosaurMap jurassicMap = new DinosaurMap(groundFactory, map2);
			world.addGameMap(jurassicMap);

			// Update ground for every Dirt to have 1% chance to turn into Bush before game starts
			triassicMap.updateInitialGround();
			jurassicMap.updateInitialGround();

			DinosaurMap.connectMaps(triassicMap, jurassicMap, 'x');

			Player player = new Player("Player", '@', 100);
			world.addPlayer(player, jurassicMap.at(9, 4));

			jurassicMap.at(34, 16).addActor(Stegosaur.getNewDino("adult"));
			jurassicMap.at(32, 12).addActor(Stegosaur.getNewDino("adult"));
			jurassicMap.at(40, 19).addActor(Allosaur.getNewDino("adult"));
			jurassicMap.at(50, 12).addActor(Allosaur.getNewDino("adult"));

			// Add 2 male & 2 female brachiosaurs to the map as instructed
			jurassicMap.at(37, 10).addActor(Brachiosaur.getNewDino("adult", 'M'));
			jurassicMap.at(39, 9).addActor(Brachiosaur.getNewDino("adult", 'M'));
			jurassicMap.at(37, 11).addActor(Brachiosaur.getNewDino("adult", 'F'));
			jurassicMap.at(36, 9).addActor(Brachiosaur.getNewDino("adult", 'F'));

			jurassicMap.at(47, 10).addActor(Pterodactyl.getNewDino("adult", 'M'));
			jurassicMap.at(49, 9).addActor(Pterodactyl.getNewDino("adult", 'M'));
			jurassicMap.at(47, 11).addActor(Pterodactyl.getNewDino("adult", 'F'));
			jurassicMap.at(46, 9).addActor(Pterodactyl.getNewDino("adult", 'F'));

			world.run();
		}
	}

	/**
	 * Repeatedly prompts the user for game mode,
	 * until the selection is valid.
	 *
	 * @return The chosen game mode, represented by an integer in range 1..3
	 */
	private static int promptGameMode() {
		while (true) {
			int selectedMode = promptInt("""
                    
                    Choose game mode:
                    1. Challenge Mode
                    2. Sandbox Mode
                    3. Quit Game
                    """);
			if (selectedMode == 1) {
				maxTurns = promptInt("Choose number of moves      : ");
				winningPoints = promptInt("Choose number of eco points : ");
				return selectedMode;
			} else if (selectedMode == 2) {
				maxTurns = null;
				winningPoints = null;
				return selectedMode;
			} else if (selectedMode == 3)
				return selectedMode;
			else
				System.out.println("Please select a valid game mode.");
		}
	}

	/**
	 * Repeatedly prompts the user for an integer input,
	 * until the input is valid.
	 *
	 * @param promptMessage The message used to prompt the user for input
	 * @return An integer input
	 */
	private static int promptInt(String promptMessage) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print(promptMessage);
			try {
				int input = sc.nextInt();
				sc.nextLine();
				return input;
			} catch (InputMismatchException ignored) {
				System.out.println("Input must be an integer.");
				sc.nextLine();
			}
		}
	}

}
