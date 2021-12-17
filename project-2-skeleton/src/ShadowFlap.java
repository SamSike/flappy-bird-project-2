import bagel.*;

import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Sameer Sikka
 *
 * Developer Comment:
 * I have tried so much to fix the flames. Literally everything else including its collision detection works.
 * But the y co-ordinate of the gap of the pipes refuses to make peace with the flames. the flames are never aligned.
 * Sometimes they are in the top Pipe. sometimes in the bottom. It is 5 am and I can not deal with this anymore.
 * Also it seems like every time you press a key, multiple "input.isDown" methods for the same key are called
 * Especially in the timescale department.
 *
 */
public class ShadowFlap extends AbstractGame {

    boolean isGameOn;
    boolean isLoss;
    boolean isWin;

    int level;

    ArrayList<Level> levels;

    /**
     * Initialises attributes of ShadowFlap
     */
    ShadowFlap() {

        super(1024, 768, "Flappy Bird ++");
        level = 0;
        isLoss = isWin = isGameOn = false;

        levels = new ArrayList<>();
        levels.add(new Level());
        levels.add(new Level1());
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {

        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        if(input.isDown(Keys.ESCAPE))
            Window.close();

        // If level is won, switch to next level
        if (levels.get(level).getIsWin())
            level++;

        // Runs current level
        levels.get(level).update(input);
    }
}
