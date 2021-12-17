import bagel.util.Rectangle;

/**
 * Interface implemented by classes with objects movable on the screen which can thus collide
 */
public interface Movable {
    public void move();
    Rectangle[] getBox();
}
