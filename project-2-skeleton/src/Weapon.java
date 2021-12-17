import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Weapon in Level 1 of Flappy Birds Game. Weapon is a Game Element.
 *  Weapon can move and thus collide with other movable objects.
 */
public class Weapon extends GameElement implements Movable{

    // All provided images
    private final static Image ROCK = new Image("project-2-skeleton/res/level-1/rock.png");
    private final static Image BOMB = new Image("project-2-skeleton/res/level-1/bomb.png");

    // Constants applicable to entire class
    private final static int MAX_SPAWN = 500;
    private final static int MIN_SPAWN = 100;
    private final static int SHOT_VELOCITY = 5;
    private final static int BIRD_VELOCITY = 0;
    private final static int SHOOT_FRAMES = 25;

    private static int spawnRate = PipeSet.getSpawnRate();

    // Weapon is a rock or not a rock (a bomb)
    private final boolean IS_ROCK;

    private int xVelocity;
    private boolean isEquipped;
    private boolean isShot;
    private boolean isDestroyed;
    private int frames;
    private Rectangle box;

    /**
     * Renders Weapon on Screen
     */
    @Override
    public void render() {

        // Weapon only lasts for SHOOT_FRAMES after being shot
        if(isShot)
            frames--;

        if(Integer.compare(frames, 0) == 0)
            isDestroyed = true;

        super.render();
        this.move();
    }


    /**
     * Moves weapon to left if unequipped and to right if shot
     */
    @Override
    public void move() {

        // Weapon is rendered on centre of right border of bird
        if(isEquipped) {
            x = Bird.getInstance().x + Bird.getInstance().elementImage.getWidth()/2.0;
            y = Bird.getInstance().y;
        }

        else
            x -= xVelocity;
    }


    /**
     * Getting Equipped by bird
     */
    public void collideBird() {

        isEquipped = true;
        xVelocity = BIRD_VELOCITY;
    }


    /**
     * Checking and Effects of collisions with pipes and flames
     * @param pipes arraylist
     * @return true for destroying a pipe
     */
    public boolean collidePipe(ArrayList<PipeSet> pipes){

        // A weapon that is not shot can not collide or be destroyed
        if(!isShot)
            return false;

        for(PipeSet pipe: pipes)

            // Checking all boxes of pipe
            for(int pipeBox = 0; pipeBox < pipe.getBox().length; pipeBox++)

                // Weapon destroyed for successful collision with pipe or flame
                if(box.intersects(pipe.getBox()[pipeBox])){
                    isDestroyed = true;

                    // Check for destruction of pipe (unless Weapon collided with flame)
                    if(pipe.collideWeapon(IS_ROCK) && pipeBox < PipeSet.getNumPipeBox()) {
                        pipes.remove(pipe);
                        return true;
                    }

                    return false;
                }

        return false;
    }


    /**
     * Sets spawnRate of Weapon class according to timescale
     */
    public static void setSpawnRate(){ spawnRate = PipeSet.getSpawnRate(); }

    /**
     * @return isShot
     */
    public boolean getIsShot(){ return isShot; }


    /**
     * Changes movement of weapon when shot
     */
    public void shoot(){

        xVelocity = -SHOT_VELOCITY;
        isShot = true;
        isEquipped = false;
    }


    /**
     * @return true if shot weapon is destroyed
     */
    public boolean getIsDestroyed(){ return isDestroyed; }


    /**
     * @return box around weapon for collision detection
     */
    @Override
    public Rectangle[] getBox() {

        box = elementImage.getBoundingBoxAt(new Point(x, y));
        return new Rectangle[]{box};
    }


    /**
     * @return spawnRate
     */
    public static int getSpawnRate(){ return spawnRate; }


    /**
     * Updates xVelocity based on PipeSet xVelocity
     */
    public void setXVelocity(){

        // If Weapon is equipped, speed doesn't change. If never equipped
        if(!isEquipped && !isShot)
            xVelocity = PipeSet.getXVelocity();
    }


    /**
     * Default constructor for Weapon class that initialises all attributes
     */
    Weapon(){

        // Weapon is either a rock or not a rock (bomb)
        Random r = new Random();
        IS_ROCK = r.nextBoolean();
        elementImage = IS_ROCK ? ROCK : BOMB;

        // Spawn Position of weapon
        x = Window.getWidth();
        y = r.nextInt(MAX_SPAWN - MIN_SPAWN) + MIN_SPAWN;

        xVelocity = PipeSet.getXVelocity();
        isEquipped = isShot = isDestroyed = false;
        frames = (IS_ROCK ? SHOOT_FRAMES : SHOOT_FRAMES * 2);
    }
}
