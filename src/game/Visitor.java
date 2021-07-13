package game;

import game.dinosaurs.Allosaur;
import game.dinosaurs.Brachiosaur;
import game.dinosaurs.Pterodactyl;
import game.dinosaurs.Stegosaur;


/**
 * Declare the visit operations for all the types of visitable classes.
 * Part of Visitor Pattern:
 * https://www.geeksforgeeks.org/visitor-design-pattern/
 *
 * @author Ci Leong Ong
 * @param <T> Return type
 */
public interface Visitor<T> {

    /**
     * Visit operation for stegosaurs.
     */
    T visit(Stegosaur stegosaur);

    /**
     * Visit operation for brachiosaurs.
     */
    T visit(Brachiosaur brachiosaur);

    /**
     * Visit operation for allosaurs.
     */
    T visit(Allosaur allosaur);

    /**
     * Visit operation for pterodactyls.
     */
    T visit(Pterodactyl pterodactyl);

}
