import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Set of Flames in Flappy Birds Game. Flame is a Game Element.
 * Flame can move and thus collide with other movable objects.
 */
public class Flame extends GameElement implements Movable{

    // Frames that Flame stays on changed due to FPS Reasons
    private final static int STAY_FRAMES = 30;
    private final static int SPAWN_RATE = 20;
    private final static int GAP_HEIGHT = 168;
    private final static DrawOptions FLIP = new DrawOptions().setRotation(Math.PI);
    private final static Image FLAME = new Image("project-2-skeleton/res/level-1/flame.png");

    private static int xVelocity = PipeSet.getXVelocity();

    private final double TOP_Y;
    private final double BOTTOM_Y;

    private int frames;
    private Rectangle topBox;
    private Rectangle bottomBox;


    /**
     * Renders Set of Flames
     */
    @Override
    public void render() {

        //  Flame is rendered Every SPAWN_RATE frames for STAY_FRAMES
        if(frames % (SPAWN_RATE + STAY_FRAMES) < STAY_FRAMES){
            elementImage.draw(x, TOP_Y);
            elementImage.draw(x, BOTTOM_Y, FLIP);
        }

        this.move();
        frames++;
    }


    /**
     * Moves flames to the left
     */
    @Override
    public void move() {
        x -= xVelocity;
    }


    /**
     * Sets xVelocity to xVelocity of PipeSet class
     */
    public static void setXVelocity(){
        xVelocity = PipeSet.getXVelocity();
    }


    /**
     * Checks for collision with bird or shot weapon only if flame is being rendered
     * @return true for collision
     */
    public boolean flameCollide() { return frames % (SPAWN_RATE + STAY_FRAMES) < STAY_FRAMES; }


    /**
     * Returns box array
     */
    @Override
    public Rectangle[] getBox() {

        topBox = elementImage.getBoundingBoxAt(new Point(x, TOP_Y));
        bottomBox = elementImage.getBoundingBoxAt(new Point(x, BOTTOM_Y));
        return new Rectangle[]{topBox, bottomBox};
    }


    /**
     * Constructor that initiates set of Flames based on set of Pipes
     * @param pipe for co-ordinates
     */
    Flame(PipeSet pipe){

        elementImage = FLAME;
        frames = 0;
        x = Window.getWidth();
        y = pipe.getY();

        // topY and bottomY are co-ordinates of the centre of respective pipes
        TOP_Y = y + elementImage.getHeight()/2.0;
        BOTTOM_Y = TOP_Y + GAP_HEIGHT - elementImage.getHeight();
    }
}
