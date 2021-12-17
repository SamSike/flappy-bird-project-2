import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Rectangle;
import bagel.util.Point;
import java.util.Random;

/**
 * Set of Pipes in Flappy Birds Game. PipeSet is a Game Element.
 * PipeSet can move and thus collide with other movable objects.
 */
public class PipeSet extends GameElement implements Movable{

    // Object to rotate pipe by 180 degrees i.e PI radians
    private final static DrawOptions FLIP = new DrawOptions().setRotation(Math.PI);

    // Constants applicable to all PipeSets - SpawnRate changed for FPS reasons
    private final static int DEFAULT_SPAWN_RATE = 120;
    private final static int VELOCITY = 3;
    private final static int GAP_HEIGHT = 168;
    private final static int NUM_PIPE_BOX = 2;
    private final static int[] GAPS_Y = new int[]{100, 300, 500};
    private final static Image PLASTIC = new Image("project-2-skeleton/res/level/plasticPipe.png");
    private final static Image STEEL = new Image("project-2-skeleton/res/level-1/steelPipe.png");

    // PipeSet attributes that may be changed during runtime
    private static int spawnRate = DEFAULT_SPAWN_RATE;
    private static int level = 0;
    private static int xVelocity = VELOCITY;

    private final int TOP_Y;
    private final int BOTTOM_Y;
    private final boolean IS_PLASTIC;

    // Boundaries of pipes
    private Rectangle topBox;
    private Rectangle bottomBox;

    private Flame flame;


    /**
     * Renders Set of Pipes every frame if they are not destroyed
     */
    @Override
    public void render(){

        this.move();

        if(!IS_PLASTIC)
            flame.render();

        elementImage.draw(x, TOP_Y);
        elementImage.draw(x, BOTTOM_Y, FLIP);
    }


    /**
     * Moves pipes to the left
     */
    @Override
    public void move(){ x -= xVelocity; }


    /**
     * Sets xVelocity based on timescale
     * @param timescale for change in xVelocity and spawnRate of pipes and flames
     */
    public static void setXVelocity(Timescale timescale){

        xVelocity = (int)(VELOCITY * timescale.getTimescaleEffect());
        spawnRate = (int)(DEFAULT_SPAWN_RATE / timescale.getTimescaleEffect());
        Flame.setXVelocity();
    }


    /**
     * Changes to pipe on collision with a shot weapon
     * @param isRock for if Weapon is a rock
     * @return true if pipe is destroyed and false otherwise
     */
    public boolean collideWeapon(boolean isRock){ return IS_PLASTIC || !isRock; }


    /**
     * @return true if flame collides with bird or shot weapon
     */
    public boolean flameCollide(){ return flame.flameCollide(); }


    /**
     * Levels Up and Resets Timescale of PipeSet Class
     */
    public static void levelUp(){
        level++;
        PipeSet.setXVelocity(new Timescale());
    }


    /**
     * @return spawnRate
     */
    public static int getSpawnRate(){ return spawnRate; }


    /**
     * @return Number of Boxes belonging to Pipe and not Flame
     */
    public static int getNumPipeBox() { return NUM_PIPE_BOX; }


    /**
     * @return box array containing boxes of pipes (and flames for steel pipes)
     */
    @Override
    public Rectangle[] getBox() {

        topBox = elementImage.getBoundingBoxAt(new Point(x, TOP_Y));
        bottomBox = elementImage.getBoundingBoxAt(new Point(x, BOTTOM_Y));

        if(!IS_PLASTIC)
            return new Rectangle[]{topBox, bottomBox, flame.getBox()[0], flame.getBox()[1]};

        return new Rectangle[]{topBox, bottomBox};
    }


    /**
     * @return x-coordinate of top of gap
     */
    public double getX(){ return x;}


    /**
     * Returns y-coordinate of top of gap
     */
    public double getY(){

        topBox = elementImage.getBoundingBoxAt(new Point(x, TOP_Y));
        return topBox.bottom();
    }


    /**
     * Returns xVelocity
     */
    public static int getXVelocity() { return xVelocity; }


    /**
     * Default constructor initialises all attributes
     */
    PipeSet(){

        // Level 0 pipes can only be plastic. Level 1 pipes can randomly be plastic or steel.
        Image[] pipeImage = new Image[]{PLASTIC, STEEL};
        Random r = new Random();
        elementImage = pipeImage[r.nextInt(level + 1)];
        IS_PLASTIC = (elementImage == PLASTIC);

        // Only steel pipes spew flames.
        if(!IS_PLASTIC)
            flame = new Flame(this);

        // x and y are co-ordinates of the top of the center of the gap where y is randomly chosen depending on level
        x = Window.getWidth();

        if(Integer.compare(level, 0) == 0) {
            y = GAPS_Y[r.nextInt(GAPS_Y.length)];
        }

        // y can range from 100 to 500
        else{
            y = r.nextInt(GAPS_Y[GAPS_Y.length - 1] - GAPS_Y[0]) + GAPS_Y[0];
        }

        // topY and bottomY are co-ordinates of the centre of respective pipes
        TOP_Y = (int)(y - elementImage.getHeight()/2);
        BOTTOM_Y = (int)(TOP_Y + GAP_HEIGHT + elementImage.getHeight());
    }
}
