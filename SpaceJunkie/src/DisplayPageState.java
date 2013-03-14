import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class DisplayPageState extends BasicGameState implements MouseListener {
	
	int stateID = 0;
	
	public DisplayPageState() {}
	protected DisplayPageState(int state) {stateID = state;}
	public int getID() {return stateID;}

	public DisplayPageState makePage(int s) {
		switch(s) {
			case SpaceJunkieGame.MAINMENUSTATE: return new MainMenuPage(SpaceJunkieGame.MAINMENUSTATE);
			case SpaceJunkieGame.GAMEOVERSTATE: return new GameOverPage(SpaceJunkieGame.GAMEOVERSTATE);
		}
		return null;
	}
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
	}

	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {		
	}
		
	public class MainMenuPage extends DisplayPageState {

		Image logo = null;
		ImageButton play = null;
		ImageButton exit = null;
		
		public MainMenuPage(int state) {super(state);}
		
		public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
			Image tmp = new Image("res/menu.png");
			logo = new Image("res/Logo.png");
			play = new ImageButton(tmp.getSubImage(0, 1, 126, 68), tmp.getSubImage(0, 76, 126, 68), 470, 300);
			exit = new ImageButton(tmp.getSubImage(0, 152, 126, 68), tmp.getSubImage(0, 221, 126, 68), 470, 400);
		}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
			logo.draw(270, 100);
			play.draw();
			exit.draw();
		}
		
		public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
			int x = gc.getInput().getMouseX();
			int y = gc.getInput().getMouseY();
			boolean b = gc.getInput().isMousePressed(0);
			play.checkBounds(x, y, b);
			exit.checkBounds(x, y, b);
			if (play.getClicked()) sbg.enterState(SpaceJunkieGame.GAMEPLAYSTATE);
			if (exit.getClicked()) gc.exit();
		}

	}
	
	public class GameOverPage extends DisplayPageState {
		
		public GameOverPage(int state) {super(state);}
		
		public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		}
		
		public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		}
		
		public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		}

	}
}
