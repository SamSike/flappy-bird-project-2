import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

import java.util.ArrayList;
import java.util.Random;

/**
 * Level 1 of Flappy Bird Game
 */
public class Level1 extends Level {

    protected final String START1 = "PRESS 'S' TO SHOOT";
    protected final String WIN = "CONGRATULATIONS!";
    protected final int START_GAP = 68;
    protected static Random rand;

    protected ArrayList<Weapon> weapons;

    /**
     * Default Constructor which initialises attributes
     */
    Level1(){

        // Max Score changed to make it more playable
        max_score = 10;
        background = new Image("project-2-skeleton/res/level-1/background.png");
        weapons = new ArrayList<>();
        rand = new Random();
    }

    /**
     * Updates Game Window every frame
     * @param input for keys pressed
     */
    @Override
    public void update(Input input) {

        super.update(input);

        if(isGameOn) {

            // Shoot Weapon
            if (input.isDown(Keys.S) && bird.getIsWeaponEquipped())
                bird.shoot();

            // Rendering all Level 1 specific GameElements
            for (Weapon weapon : weapons)
                weapon.render();
        }
    }

    /**
     * Start Screen of Level 1
     */
    @Override
    void startScreen() {

        super.startScreen();
        FONT.drawString(START1, (Window.getWidth() - FONT.getWidth(START1))/2.0, (Window.getHeight() + FONT_SIZE)/2.0 + START_GAP);
    }

    /**
     * Checking for collisions in Level 1
     */
    @Override
    void collisionCheck() {

        super.collisionCheck();

        // Checks for bird-weapon collisions if bird doesn't have a weapon equipped
        if(!bird.getIsWeaponEquipped())
            bird.collideWeapon(weapons);

        // Checks for pipe-weapon collisions
        for(Weapon weapon: weapons){

            // If weapon destroys a pipe, score is increased
            if(weapon.getIsShot() && weapon.collidePipe(pipes))
                    scoreIncrease();

            // If weapon collides with pipes or runs out of time, it is removed
            if(weapon.getIsDestroyed()) {
                weapons.remove(weapon);
                break;
            }
        }

        if(bird.getIsWeaponEquipped() && (bird.getEquippedWeapon().collidePipe(pipes) || bird.getEquippedWeapon().getIsDestroyed()))
            weapons.remove(bird.getEquippedWeapon());
    }


    /**
     * Spawning Level 1 GameElements
     */
    @Override
    void spawn() {

        super.spawn();

        // Weapons get spawned at halfway point between pipes for a specific timescale
        if(rand.nextBoolean() && Integer.compare(frames % Weapon.getSpawnRate(), Weapon.getSpawnRate()/2) == 0)
            weapons.add(new Weapon());
    }


    /**
     * Change timescale in Level 1
     * @param input for if L or K has been pressed
     */
    @Override
    void changeTimescale(Input input) {

        super.changeTimescale(input);
        Weapon.setSpawnRate();

        for(Weapon weapon: weapons)
            weapon.setXVelocity();
    }

    /**
     * Changes in attributes on levelling up
     */
    @Override
    void levelUpChanges() { isGameOn = false; }


    /**
     * Levels up to next level - in this case displays win screen
     */
    @Override
    void levelUp() {

        FONT.drawString(WIN, (Window.getWidth() - FONT.getWidth(WIN))/2.0, (Window.getHeight() + FONT_SIZE)/2.0);
        levelUpChanges();
    }
}
