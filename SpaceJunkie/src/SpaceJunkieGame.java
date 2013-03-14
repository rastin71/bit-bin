import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SpaceJunkieGame extends StateBasedGame {

	public static final int GAMEPLAYSTATE		   = 0;
	public static final int MAINMENUSTATE		   = 1;			
    public static final int GAMEOVERSTATE		   = 2;
    public static final int SCREEN_WIDTH		   = 1024;
    public static final int SCREEN_HEIGHT		   = 768;
    
	public SpaceJunkieGame() {
		super("SpaceJunkie");
		DisplayPageState dpsFactory = new DisplayPageState();
		this.addState(dpsFactory.makePage(MAINMENUSTATE));
		this.addState(dpsFactory.makePage(GAMEOVERSTATE));
		this.addState(new GamePlayState(GAMEPLAYSTATE));
		this.enterState(MAINMENUSTATE);
	}

	public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(MAINMENUSTATE).init(gameContainer, this);
        this.getState(GAMEPLAYSTATE).init(gameContainer, this);
        this.getState(GAMEOVERSTATE).init(gameContainer, this);
	}
	
    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new SpaceJunkieGame());
         app.setShowFPS(true);
         app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, true);
         app.start();
    }
}

