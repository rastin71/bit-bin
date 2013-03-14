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
	Image background = null;
	UnicodeFont ucFont = null;
	ImageButton exit = null;
	ImageButton enter = null;
	
	protected DisplayPageState(int state) {stateID = state;}
	public int getID() {return stateID;}
	
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		Image menuOptions = new Image("resources/menumap.png");
		enter = new ImageButton(menuOptions.getSubImage(0, 29, 75, 29), menuOptions.getSubImage(0, 0, 75, 29), 700, 200);
		exit = new ImageButton(menuOptions.getSubImage(75, 29, 50, 29), menuOptions.getSubImage(75, 0, 50, 29), 710, 280);
		arg0.setMouseCursor("resources/mouse.png", 16, 16);
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		enter.draw();
		exit.draw();
	}

	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		if (enter.getClicked()) sbg.enterState(ParticleTest.GAMEPLAYSTATE);
		if (exit.getClicked()) gc.exit();
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
    	if (button == 0) {
			enter.checkBounds(x, y, true);
			exit.checkBounds(x, y, true);
		}
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		enter.checkBounds(newx, newy, false);
		exit.checkBounds(newx, newy, false);
	}
}
