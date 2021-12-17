import bagel.Image;
import bagel.Window;
import bagel.util.Rectangle;
import bagel.util.Point;
import java.util.ArrayList;

/**
 * Bird in Flappy Birds Game. Bird is a Game Element.
 * Bird can move and thus collide with other movable objects.
 */
public class Bird extends GameElement implements Movable{

    private final static int DEFAULT_X = 200;
    private final static int DEFAULT_Y = 350;
    private final static double GRAVITY = 0.4;
    private final static double MAX_VELOCITY = 10;
    private final static double FLY_VELOCITY = 6;

    private final static Image BIRD_UP_0 = new Image("project-2-skeleton/res/level-0/birdWingUp.png");
    private final static Image BIRD_DOWN_0 = new Image("project-2-skeleton/res/level-0/birdWingDown.png");
    private final static Image BIRD_UP_1 = new Image("project-2-skeleton/res/level-1/birdWingUp.png");
    private final static Image BIRD_DOWN_1 = new Image("project-2-skeleton/res/level-1/birdWingDown.png");
    private final static Image[] BIRD_UP = new Image[]{BIRD_UP_0, BIRD_UP_1};
    private final static Image[] BIRD_DOWN = new Image[]{BIRD_DOWN_0, BIRD_DOWN_1};
    private final LifeBar health;

    // Bird is a Singleton class
    private static Bird _instance = null;

    private Rectangle box;
    private Rectangle weaponBox;
    private Weapon equippedWeapon;


    private double yVelocity;
    private boolean isWeaponEquipped;
    private int level;
    private int frame;

    /**
     * Renders bird on the screen in wings down (or wings up every 10th frame) and forms box around image
     * to help with collision detection
     */
    @Override
    public void render() {

        frame++;

        if(frame % 10 == 0) {
            wingUp();
            super.render();
            wingDown();
        }

        elementImage.draw(x, y);
        box = elementImage.getBoundingBoxAt(new Point(x, y));
        health.render();
        this.move();
    }

    /**
     * Moves bird down depending on bird velocity and gravity
     */
    @Override
    public void move() {

        yVelocity = Double.min(MAX_VELOCITY, yVelocity + GRAVITY);
        y += yVelocity;
    }

    /**
     * Collision Detection with pipes or flame
     * Every pipe and flame is checked for collision
     * @param pipes ArrayList
     * @return true if all lives are over
     */
    public boolean collidePipes(ArrayList<PipeSet> pipes) {

        // If no pipes exist to the right of bird
        if(pipes.size() == 0)
            return health.isLivesOver();

        // First pipe to the right of the bird is the only one that can collide
        for(int pipeBox = 0; pipeBox < pipes.get(0).getBox().length; pipeBox++)

            // Check for collision of bird (and equipped weapon if exists) with pipe or flame
            if(box.intersects(pipes.get(0).getBox()[pipeBox]) || (isWeaponEquipped && weaponBox.intersects(pipes.get(0).getBox()[pipeBox]))){

                // Successful collision with pipe or flame
                if(pipeBox < PipeSet.getNumPipeBox() || pipes.get(0).flameCollide()) {
                    pipes.remove(pipes.get(0));
                    health.setLifeLost();
                    break;
                }

                return true;
            }

        // Lives are over
        return health.isLivesOver();
    }

    /**
     * Checks if Bird collides with weapon
     * @param weapons which is an ArrayList of class Weapon
     * @return true if new weapon has been equipped
     */
    public boolean collideWeapon(ArrayList<Weapon> weapons){

        // If weapon is already equipped, no bird-weapon collisions can happen
        if(isWeaponEquipped)
            return false;

        for(Weapon weapon: weapons)

            // Bird-weapon collision is checked one by one
            if(box.intersects(weapon.getBox()[0])){
                weapon.collideBird();
                equip(weapon);
                return true;
            }

        return false;
    }


    /**
     * Checking for Out of Bounds
     */
    public void checkOOB(){

        if(y < 0 || y > Window.getHeight()){
            health.setLifeLost();
            respawn();
        }
    }


    /**
     * Bird flies up with fly speed and wings up
     */
    public void fly(){

        yVelocity = -FLY_VELOCITY;
        wingUp();
    }


    /**
     * Bird in Wing Up orientation
     */
    private void wingUp(){ elementImage = BIRD_UP[level]; }


    /**
     * Bird in Wing Down orientation
     */
    private void wingDown(){ elementImage = BIRD_DOWN[level]; }


    /**
     * Respawns bird in default position and bird flies
     */
    private void respawn(){

        x = DEFAULT_X;
        y = DEFAULT_Y;
    }


    /**
     * Levels up - makes bird more badass and able to use weapons
     */
    public void levelUp(){

        level++;
        wingDown();
        health.levelUp();
    }


    /**
     * Returns box around bird
     */
    @Override
    public Rectangle[] getBox() { return new Rectangle[]{box}; }


    /**
     * Equips Weapon
     */
    public void equip(Weapon weapon){

        isWeaponEquipped = true;
        equippedWeapon = weapon;
        weaponBox = weapon.getBox()[0];
    }


    /**
     * Unequips Weapon when shot
     */
    public void shoot(){

        isWeaponEquipped = false;
        equippedWeapon.shoot();
    }


    /**
     * @return weapon that is equipped
     */
    public Weapon getEquippedWeapon(){ return equippedWeapon; }


    /**
     * @return true if bird has a weapon equipped
     */
    public boolean getIsWeaponEquipped(){ return isWeaponEquipped; }


    /**
     * Default constructor of bird, initialises velocity, weapon status, level and frame
     */
    private Bird(){

        respawn();
        fly();
        isWeaponEquipped = false;
        level = 0;
        frame = 0;

        health = new LifeBar();
    }


    /**
     * Method to make an instance of Bird if none exists, else points to existing instance
     * @return instance of Bird
     */
    public static Bird getInstance(){
        if(_instance == null)
            _instance = new Bird();
        return _instance;
    }
}
