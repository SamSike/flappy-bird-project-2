import bagel.Image;

/**
 * Life Bar of Bird in Flappy Birds Game. Life Bar is a Game Element.
 */
public class LifeBar extends GameElement{

    private final static Image EMPTY_HEART = new Image("project-2-skeleton/res/level/noLife.png");
    private final static int MAX_LEVEL0 = 3;
    private final static int GAP = 50;
    private final static int DEFAULT_X = 100;
    private final static int DEFAULT_Y = 15;

    private int lives;
    private int maxLives;
    private int level;


    /**
     * Rendering Life Bar every frame - Full and Empty hearts are rendered.
     */
    @Override
    public void render(){

        int i=0;

        for( ; i < lives; i++)
            elementImage.draw(x + (i * GAP), y);

        for( ; i < maxLives; i++)
            EMPTY_HEART.draw(x + (i * GAP), y);
    }


    /**
     * Decreases 1 life.
     */
    public void setLifeLost(){
        lives --;
    }


    /**
     * Checks if any lives are left.
     * @return 0 for true, 1 for false
     */
    public boolean isLivesOver(){
        return Integer.compare(lives, 0) == 0;
    }


    /**
     * Default constructor initialises number of lives and default heart image
     */
    LifeBar(){

        elementImage = new Image("project-2-skeleton/res/level/fullLife.png");
        x = DEFAULT_X + elementImage.getWidth()/2.0;
        y = DEFAULT_Y + elementImage.getHeight()/2.0;
        lives = maxLives = MAX_LEVEL0;
        level = 0;
    }


    /**
     * Level Up by 1, renews and doubles max lives
     */
    public void levelUp(){

        level++;
        lives = MAX_LEVEL0 * (level + 1);
        maxLives = lives;
    }
}
