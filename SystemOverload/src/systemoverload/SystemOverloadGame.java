package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Main Program for game.

*****************************************************/
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import systemoverload.DisplayPageState;

//JRA: Original Code Begin
public class SystemOverloadGame extends StateBasedGame {

    public static final int MAINMENUSTATE          = 0;
    public static final int HELPSCREENSTATE		   = 1;
    public static final int HIGHSCORESCREENSTATE   = 2;
    public static final int GAMEPLAYSTATE          = 3;	
    public static final int ABOUTSCREENSTATE	   = 4;
    public static final int CREDITSCREENSTATE	   = 5;
    public static final int GAMEOVERSTATE		   = 6;
    
	public SystemOverloadGame() {
		super("SystemOverload");
		// TODO Auto-generated constructor stub
		DisplayPageState dpsFactory = new DisplayPageState();
		this.addState(dpsFactory.makePage(DisplayPageState.states.HELP));
		this.addState(dpsFactory.makePage(DisplayPageState.states.HIGHSCORES));
		this.addState(dpsFactory.makePage(DisplayPageState.states.GAMEOVER));
		this.addState(dpsFactory.makePage(DisplayPageState.states.ABOUT));
		this.addState(dpsFactory.makePage(DisplayPageState.states.CREDITS));
		this.addState(dpsFactory.makePage(DisplayPageState.states.MAINMENU));
		this.addState(new GamePlayState(GAMEPLAYSTATE));
		this.enterState(MAINMENUSTATE);
	}

	@Override
	public void initStatesList(GameContainer gameContainer) throws SlickException {
		// TODO Auto-generated method stub
        this.getState(MAINMENUSTATE).init(gameContainer, this);
        this.getState(HELPSCREENSTATE).init(gameContainer, this);
        this.getState(ABOUTSCREENSTATE).init(gameContainer, this);
        this.getState(HIGHSCORESCREENSTATE).init(gameContainer, this);
        this.getState(CREDITSCREENSTATE).init(gameContainer, this);
        this.getState(GAMEPLAYSTATE).init(gameContainer, this);
        this.getState(GAMEOVERSTATE).init(gameContainer, this);
	}
	
    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new SystemOverloadGame());
         app.setShowFPS(false);
         app.setDisplayMode(1440, 900, true);
  //       app.setDisplayMode(800, 600, true);
       //  app.setFullscreen(true);
         app.start();
    }
}
//JRA: Original Code End