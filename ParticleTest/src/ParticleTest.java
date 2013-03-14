import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

public class ParticleTest extends StateBasedGame {
	
    public static final int MAINMENUSTATE          = 0;
    public static final int GAMEPLAYSTATE		   = 1;
	
	ParticleTest() {
		super("ParticleTest");
		this.addState(new DisplayPageState(MAINMENUSTATE));
		this.addState(new GamePlayState(GAMEPLAYSTATE));
		this.enterState(MAINMENUSTATE);
	}

	@Override
	public void initStatesList(GameContainer gameContainer) throws SlickException {
		// TODO Auto-generated method stub
        this.getState(MAINMENUSTATE).init(gameContainer, this);
        this.getState(GAMEPLAYSTATE).init(gameContainer, this);
	}
	
    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new ParticleTest());
         app.setShowFPS(false);
         app.setDisplayMode(1440, 900, false);
//  	 app.setFullscreen(true);
         app.start();
    }
}