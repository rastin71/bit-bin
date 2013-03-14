import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicGameState implements MouseListener, KeyListener {
	int stateID = 0;
	Image background = null;
	// Image activeIndicator = null;
	ImageButton exit = null;	
	ImageButton back = null;
	ImageButton next = null;
	ImageButton upVal = null;
	ImageButton downVal = null;
	ImageButton offUp = null;
	ImageButton offDown = null;
	ImageButton backGroup = null;
	ImageButton nextGroup = null;
	EmitterRecord er;
	
	private ConfigurableEmitter smallExplosionEmitter; // initial explosion - will be duplicated and placed as needed
	private ParticleSystem effectSystem; // stores all particle effects
	protected GamePlayState(int state) {stateID = state;}
	public int getID() {return stateID;}
	
	
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		Image menuOptions = new Image("resources/menumap.png");
//		activeIndicator = menuOptions.getSubImage(0, 58, 27, 37);
//		activeGroup = menuOptions.getSubImage(0, 58, 27, 37);
		exit = new ImageButton(menuOptions.getSubImage(75, 29, 50, 29), menuOptions.getSubImage(75, 0, 50, 29), 1340, 20);	
		back = new ImageButton(menuOptions.getSubImage(0, 144, 32, 32), menuOptions.getSubImage(0, 112, 32, 32), 5, 5);	
		next = new ImageButton(menuOptions.getSubImage(32, 144, 32, 32), menuOptions.getSubImage(32, 112, 32, 32), 38, 5);	
		upVal = new ImageButton(menuOptions.getSubImage(96, 144, 32, 32), menuOptions.getSubImage(96, 112, 32, 32), 38, 38);
		downVal = new ImageButton(menuOptions.getSubImage(64, 144, 32, 32), menuOptions.getSubImage(64, 112, 32, 32), 5, 38);
		offUp = new ImageButton(menuOptions.getSubImage(96, 144, 32, 32), menuOptions.getSubImage(96, 112, 32, 32), 383, 5);
		offDown = new ImageButton(menuOptions.getSubImage(64, 144, 32, 32), menuOptions.getSubImage(64, 112, 32, 32), 350, 5);
		backGroup = new ImageButton(menuOptions.getSubImage(0, 144, 32, 32), menuOptions.getSubImage(0, 112, 32, 32), 435, 5);
		nextGroup = new ImageButton(menuOptions.getSubImage(32, 144, 32, 32), menuOptions.getSubImage(32, 112, 32, 32), 468, 5);
		try
	    {   
	    	effectSystem = ParticleIO.loadConfiguredSystem("resources/shipexhaust.xml");         
	        smallExplosionEmitter = (ConfigurableEmitter)effectSystem.getEmitter(0);
	        smallExplosionEmitter.setEnabled(true);
	//        smallExplosionEmitter.colors.clear();
	        smallExplosionEmitter.setPosition(600, 500);
//		    smallExplosionEmitter.addColorPoint(0.0f, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
	//	    smallExplosionEmitter.addColorPoint(1.0f, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));		    
	        er = new EmitterRecord(smallExplosionEmitter);
	        smallExplosionEmitter.setImageName("resources/circle.png");
	     }
	     catch(Exception e)
	     {
	        Sys.alert("Error", "Error adding explosion\nCheck for explosion.xml");
	        System.exit(0);
	     }
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		exit.draw();
		back.draw();
		next.draw();
		upVal.draw();
		downVal.draw();
		offUp.draw();
		offDown.draw();
		backGroup.draw();
		nextGroup.draw();
		// In render method
		effectSystem.render();
		er.render();
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		if (exit.getClicked()) gc.exit();
		if (next.getClicked()) er.nextState();
		if (back.getClicked()) er.backState();
		if (offUp.getClicked()) er.stepUp();
		if (offDown.getClicked()) er.stepDown();
		if (upVal.getClicked()) er.nextValue();
		if (downVal.getClicked()) er.backValue();
		if (nextGroup.getClicked()) er.nextGroup();
		if (backGroup.getClicked()) er.backGroup();		
		effectSystem.update(delta);
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
    	if (button == 0) {
    		exit.checkBounds(x, y, true);
    		back.checkBounds(x, y, true);
    		next.checkBounds(x, y, true);
    		upVal.checkBounds(x, y, true);
    		downVal.checkBounds(x, y, true);
    		offUp.checkBounds(x, y, true);
    		offDown.checkBounds(x, y, true);
    		nextGroup.checkBounds(x, y, true);
    		backGroup.checkBounds(x, y, true);
		}
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		exit.checkBounds(newx, newy, false);
		back.checkBounds(newx, newy, false);
		next.checkBounds(newx, newy, false);
		upVal.checkBounds(newx, newy, false);
		downVal.checkBounds(newx, newy, false);
		offUp.checkBounds(newx, newy, false);
		offDown.checkBounds(newx, newy, false);
		nextGroup.checkBounds(newx, newy, false);
		backGroup.checkBounds(newx, newy, false);
	}
	
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_TAB) er.nextOption();
		if (key == Input.KEY_UP) er.groupSubUp();
		if (key == Input.KEY_DOWN) er.groupSubDown();
		if (key == Input.KEY_SPACE) er.changeGroup();
	}
	   
}
