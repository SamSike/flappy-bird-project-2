import bagel.Image;

/**
 * Parent class for all Game Elements that have co-ordinates, an image and need to be rendered.
 */
public abstract class GameElement{

    protected double x;
    protected double y;
    protected Image elementImage;

    public void render(){
        elementImage.draw(x, y);
    }
}
