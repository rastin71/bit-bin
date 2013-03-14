package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Class Collection representing all non-game game screens.

Each page extends class DisplayPageState, overriding mouse
handler and rendering functions. 
*****************************************************/

import java.awt.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//JRA: Original Code Begin
public class DisplayPageState extends BasicGameState implements MouseListener {

	public enum states {MAINMENU, HELP, HIGHSCORES, ABOUT, CREDITS, GAMEOVER};
	
    public static final int MAINMENUSCREENSTATE    = 0;
    public static final int HELPSCREENSTATE		   = 1;
    public static final int HIGHSCORESCREENSTATE   = 2;
    public static final int GAMEPLAYSTATE          = 3;	
    public static final int ABOUTSCREENSTATE	   = 4;
    public static final int CREDITSCREENSTATE	   = 5;
    public static final int GAMEOVERSTATE		   = 6;	
	
	int stateID = 0;
	Image background = null;
	UnicodeFont ucFont = null;
	ImageButton exit = null;
	
	public DisplayPageState() {}
	protected DisplayPageState(int state) {stateID = state;}
	public int getID() {return stateID;}

	public DisplayPageState makePage(states s) {
		switch(s) {
		case MAINMENU: return new MainMenuPage(MAINMENUSCREENSTATE);
		case HELP: return new HelpPage(HELPSCREENSTATE);
		case HIGHSCORES: return new HighscorePage(HIGHSCORESCREENSTATE);
		case ABOUT: return new AboutPage(ABOUTSCREENSTATE);
		case CREDITS: return new CreditsPage(CREDITSCREENSTATE);
		case GAMEOVER: return new GameOverPage(GAMEOVERSTATE);
		}
		return null;
	}
	
	// Override MouseListener
	public void mouseClicked(int button, int x, int y, int clickCount) {
    // Notification that a mouse button was clicked.
		if (button == 0) exit.checkBounds(x, y, true);
	}
	
	// Override MouseListener
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	// Notification that mouse cursor was moved
		exit.checkBounds(newx, newy, false);
	}
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		background = new Image(Resource.DISPLAY_BACKGROUND);
		Image menuOptions = new Image(Resource.MENU_GRAPHICS);
		exit = new ImageButton(menuOptions.getSubImage(378, 340, 50, 60), menuOptions.getSubImage(320, 340, 50, 60), 1300, 40);
        ucFont = new UnicodeFont(new Font("resources/samplefont.ttf", Font.BOLD, 30));
        ucFont.addAsciiGlyphs();
        ucFont.addGlyphs(400, 600);
        ucFont.getEffects().add(new ColorEffect(java.awt.Color.orange));
        ucFont.loadGlyphs();
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		background.draw(0,0);
		exit.draw();
	}

	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {		
		if (exit.getClicked()) sbg.enterState(SystemOverloadGame.MAINMENUSTATE);
	}
	
	public class AboutPage extends DisplayPageState {
		
		public AboutPage(int state) {super(state);}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			super.render(arg0, arg1, arg2);
			ucFont.drawString(100, 100, "SystemOverload employes the following technologies:");
			ucFont.drawString(150, 150, "Java / LWJGL / Slick2D");
			ucFont.drawString(100, 250, "All graphics created with GNU Gimp");
		}
	}
	
	public class CreditsPage extends DisplayPageState {
		
		public CreditsPage(int state) {super(state);}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			super.render(arg0, arg1, arg2);
			 ucFont.drawString(100, 100, "SystemOverload was written by James \"Ron\" Astin in Fall 2012 for");
	         ucFont.drawString(100, 150, "Prof Bryant York's CS 313 AI & Game Design class. All artwork was");
	         ucFont.drawString(100, 200, "created using the excellent Gimp tool. Inspirational credit is due to");
	         ucFont.drawString(100, 250, "Andrew Braybrook for his 1985 Paradroid which I played on the");
	         ucFont.drawString(100, 300, "Commodore 64.");
		}
	}
	
	public class GameOverPage extends DisplayPageState {
		
		public GameOverPage(int state) {super(state);}
		
		public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {		
			if (exit.isClicked()) {
				GameData.getInstance().init();
			}
			super.update(gc, sbg, arg2);
		}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			super.render(arg0, arg1, arg2);
			GameData gData = GameData.getInstance();
			ucFont.drawString(300,  60, (gData.redEnergy < gData.blueEnergy) ? "WELCOME ADMINISTRATOR." : "ACCESS DENIED!!! SECURITY LOCKOUT INPLACE.");
			ucFont.drawString(300, 120, "Player: " + Integer.toString(gData.blueEnergy));
			ucFont.drawString(300, 180, "System: " + Integer.toString(gData.redEnergy));
		}
	}
	
	public class HelpPage extends DisplayPageState {
		
		public HelpPage(int state) {super(state);}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			super.render(arg0, arg1, arg2);
			ucFont.drawString(100, 100, "Goal: Gain control of LED units and acquire more power than the system before depletion.");
			ucFont.drawString(150, 150, "Step 1: Place Ground unit");
			ucFont.drawString(150, 200, "Step 2: Directly connect to LED light");
			ucFont.drawString(150, 250, "Step 3: Place Logic ICs to gain additional control");
			ucFont.drawString(150, 300, "Step 4: Reprogram unfavorable ICs and defend reprogram attempts of beneficial ICs");
			ucFont.drawString(150, 350, "Step 5: Have more power reserves than the system at time of depletion.");
		}
	}
	
	public class HighscorePage extends DisplayPageState {
		
		public HighscorePage(int state) {super(state);}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			super.render(arg0, arg1, arg2);
			ucFont.drawString(100, 100, "SystemOverload Highscores:");
		}
	}
	
	public class MainMenuPage extends DisplayPageState {

		ImageButton start = null;
		ImageButton help = null;
		ImageButton high = null;
		ImageButton about = null;
		ImageButton credits = null;
		Image logo = null;
		
		public MainMenuPage(int state) {super(state);}
		
		public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {

			Image menuOptions = new Image(Resource.MENU_GRAPHICS);
	        ucFont = new UnicodeFont(new Font("resources/samplefont.ttf", Font.BOLD, 30));
	        ucFont.addAsciiGlyphs();
	        ucFont.addGlyphs(400, 600);
	        ucFont.getEffects().add(new ColorEffect(java.awt.Color.orange));
	        ucFont.loadGlyphs();
			background = new Image(Resource.DISPLAY_BACKGROUND);
		    logo = new Image(Resource.LOGO);

		    					//Green, Yellow
			start = new ImageButton(menuOptions.getSubImage(14, 8, 195, 70), 
								menuOptions.getSubImage(170, 180, 195, 70), 640, 290);
			help = new ImageButton(menuOptions.getSubImage(13, 172, 150, 75),
								menuOptions.getSubImage(520, 175, 150, 75), 660, 380);
			high = new ImageButton(menuOptions.getSubImage(239, 92, 410, 75),
								menuOptions.getSubImage(250, 260, 410, 75), 535, 470);
			about = new ImageButton(menuOptions.getSubImage(1, 90, 238, 75),
								menuOptions.getSubImage(0, 253, 238, 75), 620, 650);
			credits = new ImageButton(menuOptions.getSubImage(371, 11, 265, 75),
								menuOptions.getSubImage(0, 335, 265, 75), 600, 560);
			exit = new ImageButton(menuOptions.getSubImage(226, 3, 140, 75),
								menuOptions.getSubImage(368, 175, 140, 75), 670, 740);
		}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			background.draw(0,0);
			logo.draw(70, 0);
			start.draw(); //640, 290);
			help.draw(); //660, 380);
			high.draw(); //535, 470);
			credits.draw(); //600, 560);
			about.draw(); //620, 650);
			exit.draw();// 670, 740);
		}
		
		public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
			if (start.getClicked()) {
				if (gc.isPaused()) gc.resume();
	    		sbg.enterState(SystemOverloadGame.GAMEPLAYSTATE);
			} else if (help.getClicked()) sbg.enterState(SystemOverloadGame.HELPSCREENSTATE);
			else if (high.getClicked()) sbg.enterState(SystemOverloadGame.HIGHSCORESCREENSTATE);
			else if (credits.getClicked()) sbg.enterState(SystemOverloadGame.CREDITSCREENSTATE);
			else if (about.getClicked()) sbg.enterState(SystemOverloadGame.ABOUTSCREENSTATE);
			else if (exit.getClicked()) gc.exit();
		}
		
		public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// Notification that mouse cursor was moved
			start.checkBounds(newx, newy, false);
			help.checkBounds(newx, newy, false);
			high.checkBounds(newx, newy, false);
			credits.checkBounds(newx, newy, false);
			about.checkBounds(newx, newy, false);
			exit.checkBounds(newx, newy, false);
		}
	
		public void mouseClicked(int button, int x, int y, int clickCount) {
	    // Notification that a mouse button was clicked.
			start.checkBounds(x, y, true);
			help.checkBounds(x, y, true);
			high.checkBounds(x, y, true);
			credits.checkBounds(x, y, true);
			about.checkBounds(x, y, true);
			exit.checkBounds(x, y, true);
		}

	}
}
//JRA: Original Code END
