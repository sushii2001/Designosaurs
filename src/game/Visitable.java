package game;


/**
 * Declares the accept operation. This is the entry point which enables
 * an object to be 'visited' by the visitor object.
 * Part of Visitor Pattern:
 * https://www.geeksforgeeks.org/visitor-design-pattern/
 *
 * @author Ci Leong Ong
 */
public interface Visitable {

    /**
     * Accepts the visitor, and calls the visit() method of it,
     * to perform the declared operation for the visitable.
     */
    <T> T accept(Visitor<T> visitor);

}
