package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Main Game Object. Main Game Mouse Handler. Runs AI pulses
on delayed clock ticks.

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
public class GamePlayState extends BasicGameState implements MouseListener {

	int stateID = 0;
	static final int EXIT_IMAGE_TOP = 40;
	static final int EXIT_IMAGE_LEFT = 1365;
	
	Image background = null;
	ImageButton exit = null;
	LED lights = null;

	boolean trip;
	TileGrid tGrid = null;
	SolderBox solderBox = null;
	UnicodeFont ucFont = null;
	GameData gameData = null;
	int clock = 0;
	SystemAI systemAI = null;
	Resource.PlayerState player_state = Resource.PlayerState.GROUND;
	
	public GamePlayState(int state) {
		stateID = state;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {

		background = new Image(Resource.PLAY_BACKGROUND);
		lights = new LED();		
		Image menuOptions = new Image(Resource.MENU_GRAPHICS);
		exit = new ImageButton(menuOptions.getSubImage(378, 340, 50, 60), menuOptions.getSubImage(320, 340, 50, 60), EXIT_IMAGE_LEFT, EXIT_IMAGE_TOP);
		tGrid = TileGrid.getInstance();
        ucFont = new UnicodeFont(new Font("resources/samplefont.ttf", Font.PLAIN, 16));
        ucFont.addAsciiGlyphs();
        ucFont.addGlyphs(400, 600);
        ucFont.getEffects().add(new ColorEffect(java.awt.Color.orange));
        ucFont.loadGlyphs();
        solderBox = new SolderBox(530, 250);
    	gameData = GameData.getInstance();
    	gameData.init();
    	systemAI = new SystemAI(lights);
    	trip = false;
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		background.draw(0,0);
		lights.render(trip);
		ucFont.drawString(285, 60, Integer.toString(gameData.blueEnergy));
		ucFont.drawString(778, 46, Integer.toString(gameData.systemEnergy));
		ucFont.drawString(1280, 60, Integer.toString(gameData.redEnergy));
		exit.draw();
		tGrid.drawTiles();
		if (solderBox.isActive()) solderBox.draw();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
		throws SlickException {
		
		if (clock <= 0) {
			systemAI.processAI();
			lights.repoll();
			trip = true;
			clock = Resource.SLEEP;
		} else {
			trip = false;
			clock--;
		}

		if (exit.getClicked()) {
			gc.pause();
			sbg.enterState(SystemOverloadGame.MAINMENUSTATE);
		}
		
		if (gameData.gameOver()) {
			sbg.enterState(SystemOverloadGame.GAMEOVERSTATE);
		}
		

		
	}

	public int getID() {return stateID;}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// Notification that mouse cursor was moved
		if (solderBox.isActive()) {
			solderBox.processInput(newx, newy, false);
		}
		else {
			tGrid.processInput(newx, newy, player_state);
		}
		exit.checkBounds(newx, newy, false);
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
    // Notification that a mouse button was clicked.
		TileData tmp = null;
		if (button == 0) {
			if (player_state == Resource.PlayerState.GROUND && (tmp = tGrid.processInput(x, y, player_state)) != null && tmp.isState(Resource.MGREEN)) {
				placeGround(tmp, Resource.BLUE);
				player_state = Resource.PlayerState.SOLDER;
				systemAI.setPlayerGround(tmp.index);
			} else if (solderBox.isActive()) {
				solderBox.processInput(x, y, true);
			} else if (player_state == Resource.PlayerState.SOLDER && (tmp = tGrid.processInput(x, y, player_state)) != null) { // Grid Click
				solderBox.configure(tmp);
			}
			// Non-Grid Clicks
			exit.checkBounds(x, y, true);
		}
	}
	
	private void placeGround(TileData tile, int color) {
		TileData tmp = null;
		tile.setState(color | Resource.GND);
		if ((tmp = tile.getNeighbor(Resource.SOUTH)) != null) tmp.addMask(Resource.NORTH);
		if ((tmp = tile.getNeighbor(Resource.EAST)) != null) tmp.addMask(Resource.WEST);
		if ((tmp = tile.getNeighbor(Resource.WEST)) != null) tmp.addMask(Resource.EAST);
	} 
}
//JRA: Original Code End
