import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicGameState implements MouseListener {

	int stateID = 0;
	public GamePlayState(int state) {stateID = state;}
	public int getID() {return stateID;}
	private ScreenBuffer sbVisable = null;
	private int x_off = 0;
	private int y_off = 0;
	
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {
		container.getInput().enableKeyRepeat();
		sbVisable = new ScreenBuffer("res/mapdata.txt");
    }

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		sbVisable.render(x_off, y_off);			
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
		throws SlickException {
		
		int step = 1;
		if (gc.getInput().isKeyPressed(Input.KEY_Q)) {
			gc.exit();
		}
		if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			y_off+=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			y_off-=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
			x_off+=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			x_off-=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_I)) {
			y_off+=(10*step);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_K)) {
			y_off-=(10*step);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_J)) {
			x_off+=(10*step);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_L)) {
			x_off-=(10*step);
		}
		
		sbVisable.update(x_off, y_off);

	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
    	if (button == 0) {

		}
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {

	}
	
}
