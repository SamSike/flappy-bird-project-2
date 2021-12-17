import bagel.*;
import java.util.ArrayList;

/**
 * Abstract class used to make levels in Flappy Bird Game.
 */
public class Level {

    // Frames for levelling up changed for FPS Reasons
    protected static final int FRAMES_LEVEL = 120;
    protected static final int FONT_SIZE = 48;
    protected static final int SCORE_X = 100, SCORE_Y = 100;
    protected static final int LOSS_GAP = 75;
    protected static final Font FONT = new Font("project-2-skeleton/res/font/slkscr.ttf", FONT_SIZE);
    protected static final String LEVEL_UP = "LEVEL-UP!";
    protected static final String START = "PRESS SPACE TO START";
    protected static final String SCORE = "SCORE: ";
    protected static final String GAME_OVER = "GAME OVER";

    protected boolean isGameOn;
    protected boolean isWin;
    protected boolean isLoss;

    protected int score;
    protected int frames;
    protected int levelUpFrames;
    protected int max_score;

    // 2 Queues of pipes for continuous spawning.
    protected ArrayList<PipeSet> pipes;
    protected ArrayList<PipeSet> scoredPipes;
    protected Image background;
    protected Timescale timescale;
    protected Bird bird;


    /**
     * Updates Level per frame
     * @param input for keys pressed
     */
    public void update(Input input){

        // Draws background
        background.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // Defeat
        if(isLoss)
            loss();

        // Levels up if max score is reached
        else if(Integer.compare(score, max_score) == 0)
            levelUp();

        // Game on Start Screen
        else if(!isGameOn) {
            startScreen();

            // Game starts if user presses SPACE
            if (input.wasPressed(Keys.SPACE))
                isGameOn = true;
        }

        // Game Started
        else {

            // Timescale changes - L for increase, K for decrease
            if(input.isDown(Keys.L) || input.isDown(Keys.K))
                changeTimescale(input);

            if(input.isDown(Keys.SPACE))
                bird.fly();

            // Spawning spawnable Game Elements
            spawn();

            // All pipes to right of bird
            for (PipeSet pipe : pipes)
                pipe.render();

            // All pipes to left of bird but not outside Window
            for (PipeSet pipe : scoredPipes)
                pipe.render();

            bird.render();

            // Updates and displays score, checks for collisions
            scoreUpdate();
            collisionCheck();
        }
        frames++;
    }


    /**
     * Start Screen for Level
     */
    void startScreen() { FONT.drawString(START, (Window.getWidth() - FONT.getWidth(START))/2.0, (Window.getHeight() + FONT_SIZE)/2.0); }


    /**
     * Collision checking for all GameElements present in Level
     */
    void collisionCheck(){

        // Checking for bird Out Of Bounds or colliding with any pipes: isLoss is if bird has any lives left
        bird.checkOOB();
        isLoss = bird.collidePipes(pipes);
        isGameOn = !isLoss;
    }


    /**
     * Updates and renders Score on Top left corner of Window
     */
    void scoreUpdate(){

        checkScoreIncrease();
        FONT.drawString(SCORE+score, SCORE_X, SCORE_Y);
    }


    /**
     * @return isWin is true if Level has been won
     */
    public boolean getIsWin(){ return isWin; }


    /**
     * Levelling up attributes to help switch to next level subclass
     */
    void levelUp(){

        // Level Up Screen displayed on Window as a sort of Loading Screen
        background.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        FONT.drawString(LEVEL_UP, (Window.getWidth() - FONT.getWidth(LEVEL_UP))/2.0, (Window.getHeight() + FONT_SIZE)/2.0);
        levelUpFrames++;

        if(Integer.compare(levelUpFrames, FRAMES_LEVEL) == 0) {
            levelUpChanges();
        }
    }


    /**
     * Changes in attributes from Levelling Up
     */
    void levelUpChanges(){
        isWin = true;
        isGameOn = false;
        bird.levelUp();
        PipeSet.levelUp();
    }


    /**
     * Spawn spawnable GameElements of Level
     */
    void spawn(){

        if(frames % (PipeSet.getSpawnRate()) == 0)
            pipes.add(new PipeSet());
    }


    /**
     * Every pipe's status is checked for score
     */
    void checkScoreIncrease(){

        // Loops to fully access all pipes
        for(int currentPipe = 0, removedPipes = 0; currentPipe < pipes.size() - removedPipes; currentPipe++)

            // Pipes which move past the bird add score
            if (pipes.get(currentPipe - removedPipes).getX() < bird.x){
                scoreIncrease();
                scoredPipes.add(pipes.get(currentPipe - removedPipes));
                pipes.remove(pipes.get(currentPipe - removedPipes));
            }

        for(int currentPipe = 0, removedPipes = 0; currentPipe < scoredPipes.size() - removedPipes; currentPipe++) {

            // Pipes which move past the left side of the screen get removed
            if (scoredPipes.get(currentPipe - removedPipes).getX() < 0){
                scoredPipes.remove(currentPipe - removedPipes);
                removedPipes++;
            }
        }
    }


    /**
     * Increases score
     */
    void scoreIncrease(){ score++; }


    /**
     * Changes timescale based on input
     * @param input for if L or K has been pressed
     */
    void changeTimescale(Input input){

        timescale.changeTimescale(input.isDown(Keys.L));
        PipeSet.setXVelocity(timescale);
    }


    /**
     * Loss Screen
     */
    void loss(){
        FONT.drawString(GAME_OVER,(Window.getWidth() - FONT.getWidth(GAME_OVER))/2.0, (Window.getHeight() + FONT_SIZE)/2.0);
        FONT.drawString(SCORE + score,(Window.getWidth() - FONT.getWidth(SCORE+score))/2.0, (Window.getHeight() + FONT_SIZE)/2.0 + LOSS_GAP);
    }

    /**
     * Default constructor which initialises all attributes
     */
    Level(){

        isGameOn = isWin = isLoss = false;
        frames = levelUpFrames = score = 0;
        background = new Image("project-2-skeleton/res/level-0/background.png");

        // Max Score changed to make it more playable
        max_score = 5;

        bird = Bird.getInstance();
        pipes = new ArrayList<>();
        scoredPipes = new ArrayList<>();
        timescale = new Timescale();

        // Resets Timescale for new levels
        PipeSet.setXVelocity(timescale);
    }
}
