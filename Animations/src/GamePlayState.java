import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//JRA: Original Code Begin
public class GamePlayState extends BasicGameState implements MouseListener {

	int stateID = 0;
	public GamePlayState(int state) {stateID = state;}
	public int getID() {return stateID;}
	private Image cursor = null;
	private ArrayList<Image> alTiles = null;
	private ScreenBuffer sbVisable = null;
	private static final int MAP_SIZE = 256;
	private static final int SCREEN_WIDTH = 1024;
	private static final int SCREEN_HEIGHT = 768;
	private static final double SIN45 = Math.sin(Math.toRadians(45));
	private static final double COS45 = Math.cos(Math.toRadians(45));
	// private int tzero_org_x = 
	private Image mark = null;
	private int X_OFF = 0;
	private int Y_OFF = 0;
	int TL_X, TL_Y;
	float FL_X, FL_Y;
	
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {
		int i;
		container.getInput().enableKeyRepeat();
		sbVisable = new ScreenBuffer();
		Image img0 = new Image("res/tile.png");
		Image img1 = new Image("res/tile2.png");
		Image imgx = new Image("res/tile_x.png");
		Image imgy = new Image("res/tile_y.png");
		cursor = new Image("res/mouse.png");
		mark = new Image("res/tile3.png");
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream("res/mapdata.txt"), "US-ASCII");
			alTiles = new ArrayList<Image>();
			while ((i = in.read()) != -1) {
				switch (i) {
				case '0': 
					alTiles.add(img0);
					break;
				case '1':
					alTiles.add(img1);
					break;
				case 'x':
					alTiles.add(imgx);
					break;
				case 'y':
					alTiles.add(imgy);
					break;
				}
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		int i, j, x, y;
		// Shifting works because effective tile dimensions are all base 2 values.
		for (i = 0; i < MAP_SIZE; i++) {
			x = i << 4;
			for (j = MAP_SIZE - 1; j >= 0; j--) {
		    	y = j << 4;
		        alTiles.get(i * MAP_SIZE + j).draw((x << 1) + (y << 1) + X_OFF, (x - y) + Y_OFF);
		    }
		}
		cursor.draw(512.0f, 384.0f);
		
		i = 20;
		g.drawString("Y Off: " + Y_OFF, 10, i += 15);
		g.drawString("X Off: " + X_OFF, 10, i += 15);
//		g.drawString("(0,0) YOff: " + (((-X_OFF - 2*-Y_OFF) / Math.sqrt(16*16+32*32))), 10, i += 15);
//		g.drawString("(0,0) XOff: " + (((-X_OFF + 2*-Y_OFF) / Math.sqrt(16*16+32*32))), 10, i += 15);
		g.drawString("(0,0) YOff: " + (((-X_OFF - 2*-Y_OFF)/68.0)), 10, i += 15);
		g.drawString("(0,0) XOff: " + (((-X_OFF + 2*-Y_OFF)/68.0)), 10, i += 15);
		
		g.drawString("TL Pixel: (" + -X_OFF + "," + -Y_OFF + ")", 10, i += 25);
		g.drawString("TR Pixel: (" + (-X_OFF + SCREEN_WIDTH) + "," + -Y_OFF + ")", 10, i += 15);
		g.drawString("BL Pixel: (" + -X_OFF + "," + (-Y_OFF + SCREEN_HEIGHT) + ")", 10, i += 15);
		g.drawString("BR Pixel: (" + (-X_OFF + SCREEN_WIDTH) + "," + (-Y_OFF + SCREEN_HEIGHT) + ")", 10, i += 15);
		g.drawString("Mid Pixel: (" + (-X_OFF + SCREEN_WIDTH/2) + "," + (-Y_OFF + SCREEN_HEIGHT/2) + ")", 10, i += 15);

		if (TL_X > -1 && TL_X < 256 && TL_Y > -1 && TL_Y < 256) {
			mark.draw((TL_X << 5 + TL_Y << 5) + X_OFF, (TL_X << 4) - (TL_Y << 4) + Y_OFF);
		}
		
		// alTiles.get(i * MAP_SIZE + j).draw((i * 32) + (j * 32) + X_OFF, (i * 16 - j * 16) + Y_OFF);
		// Tile @ (0,0): 
	
		g.drawString("Mid Point : (" + -.5f * ((-Y_OFF - SCREEN_HEIGHT/2)/17.0f + (-X_OFF - SCREEN_WIDTH/2)/34.0f) + "," +
				.5f * ((-Y_OFF - SCREEN_HEIGHT/2)/17.0f - (-X_OFF - SCREEN_WIDTH/2)/34.0f) + ")", 10, i += 25);

		g.drawString("(0,0) Point : (" + (.5f * (-Y_OFF/17.0f + -X_OFF/34.0f) - .5f) + "," +
				(-.5f * (-Y_OFF/17.0f - -X_OFF/34.0f)) + ")", 10, i += 15);

		g.drawString("(1024,0) Point : (" + (.5f * ((-Y_OFF)/17.0f + (-X_OFF - SCREEN_WIDTH)/34.0f) + .5f) + "," +
				(.5f * ((-Y_OFF)/17.0f - (-X_OFF - SCREEN_WIDTH)/34.0f) - .5f) + ")", 10, i += 15);

		g.drawString("(0,768) Point : (" + (-.5f * ((Y_OFF - SCREEN_HEIGHT)/17.0f + (X_OFF)/34.0f) - .5f) + "," +
				(.5f * ((-Y_OFF - SCREEN_HEIGHT)/17.0f - (-X_OFF)/34.0f) + .5f) + ")", 10, i += 15);

		g.drawString("(1024,768) Point : (" + (-.5f * ((-Y_OFF - SCREEN_HEIGHT)/17.0f + (-X_OFF - SCREEN_WIDTH)/34.0f) + .5f) + "," +
				(.5f * ((-Y_OFF - SCREEN_HEIGHT)/17.0f - (-X_OFF - SCREEN_WIDTH)/34.0f) + .5f) + ")", 10, i += 15);	
		g.drawString("TL_X: " + TL_X, 10, i += 15);
		g.drawString("TL_Y: " + TL_Y, 10, i += 15);
		g.drawString("FL_X: " + FL_X, 10, i += 15);
		g.drawString("FL_Y: " + FL_Y, 10, i += 15);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
		throws SlickException {
		int step = 1;
		if (gc.getInput().isKeyPressed(Input.KEY_Q)) {
			gc.exit();
		}
		if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			Y_OFF+=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			Y_OFF-=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
			X_OFF+=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			X_OFF-=step;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_I)) {
			Y_OFF+=(10*step);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_K)) {
			Y_OFF-=(10*step);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_J)) {
			X_OFF+=(10*step);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_L)) {
			X_OFF-=(10*step);
		}
		FL_X = .5f * (-Y_OFF/17.0f + -X_OFF/34.0f);
		FL_Y = .5f * (-Y_OFF/17.0f - -X_OFF/34.0f);
		TL_X = Math.round(FL_X);
		TL_Y = Math.round(FL_Y);
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
    	if (button == 0) {

		}
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {

	}
	
}
