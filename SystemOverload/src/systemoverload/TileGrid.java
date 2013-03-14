package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Singleton class representing Game Tile Grid. A collection
class that implements Energy Power flow functions. Also
wraps rendering control for TileData.

*****************************************************/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

//JRA: Original Code Begin
public class TileGrid {

	private ArrayList<TileData> tileList = null;
	private ImageFetcher ifImages = null;
	private int mousePosition = -1;
	private static TileGrid instance = null;
	
	public static TileGrid getInstance() {
		if (instance == null) {
			instance = new TileGrid();
		}
		return instance;
	}
	
	private TileGrid() {

		ifImages = new ImageFetcher();
		TileData tmp = null;
		tileList = new ArrayList<TileData>();
		for (int row = 0; row < Resource.NUMROWS; row++) for (int col = 0; col < Resource.NUMCOLS; col++) {
			tmp = new TileData(Resource.GRID_X_OFFSET + Resource.TILESIZE * col, Resource.GRID_Y_OFFSET + Resource.TILESIZE * row, row * Resource.NUMCOLS + col);
			if (row == 0) tmp.addMask(Resource.NORTH);
			if (row == Resource.NUMROWS - 1) tmp.addMask(Resource.SOUTH);
			if (col == 0) tmp.addMask(Resource.WEST);
			if (col == Resource.NUMCOLS - 1) tmp.addMask(Resource.EAST);
			tileList.add(tmp);
		}
	}
	
	private TileData reposition(int x, int y) {
		TileData retVal = null;
		if (mousePosition > -1) clearCurrentTile();
		if (!(x < 0 || y < 0 || x >= Resource.NUMCOLS * Resource.TILESIZE || y >= Resource.NUMROWS * Resource.TILESIZE)) {
			
			mousePosition = (y / Resource.TILESIZE * Resource.NUMCOLS) + (x / Resource.TILESIZE);
			retVal = tileList.get(mousePosition);
		}
		return retVal;
	}
	
	private boolean isSolderable(TileData td) {
		int base = td.getState();
		if (td.isState(Resource.GND)) return false;
		if (((base | td.getMask()) & (Resource.ALLDIR)) == Resource.ALLDIR) return false;
		return true;
	}
	
	public TileData processInput(int x, int y, Resource.PlayerState ps) {
		TileData retVal = null;		
		x -= Resource.GRID_X_OFFSET;
		y -= Resource.GRID_Y_OFFSET;
		if ((retVal = reposition(x, y)) != null) {
			if (ps == Resource.PlayerState.GROUND) {
				if (retVal.index < Resource.NUMCOLS 
						|| retVal.index == Resource.LED_BLACK_ADJ_INDEX
						|| retVal.index == Resource.LED_ORANGE_ADJ_INDEX
						|| retVal.index == Resource.LED_BROWN_ADJ_INDEX
						|| retVal.index == Resource.LED_WHITE_ADJ_INDEX
						|| retVal.index == Resource.LED_TEAL_ADJ_INDEX
						|| retVal.index == Resource.LED_LIME_ADJ_INDEX
						|| retVal.index == Resource.LED_RED_ADJ_INDEX
						|| retVal.index == Resource.LED_GREEN_ADJ_INDEX
						|| retVal.index == Resource.LED_BLUE_ADJ_INDEX
						|| retVal.index == Resource.LED_YELLOW_ADJ_INDEX
						|| retVal.index == Resource.LED_PURPLE_ADJ_INDEX
						|| retVal.index == Resource.LED_PINK_ADJ_INDEX) retVal.addState(Resource.MRED);
				
				
				else retVal.addState(Resource.MGREEN);
			} else if (ps == Resource.PlayerState.SOLDER) {
				if (isSolderable(retVal)) retVal.addState(Resource.MGREEN);
				else retVal.addState(Resource.MRED);
			} 
		}
		return retVal;	
	}
	
	private void colorizeHelper(TileData td, int dir) {
		TileData tmp = null;
		if (td.isState(dir) 																									// Pointing +i
				&& (tmp = td.getNeighbor(dir)) != null 																			// With a valid +i Neighbor
					&& (tmp.isState(Resource.oppositeDirection(dir)) || (dir == Resource.SOUTH && tmp.isState(Resource.GND))) 	// That points -i or (i = s & is gnd)
						&& (tmp.getColor() & Resource.ALLCOLOR) != Resource.EMPTY) {											// That is not colorless
	
			td.addState(tmp.getColor());
			if (dir != Resource.NORTH && td.isState(Resource.NORTH) && (tmp = td.getNeighbor(Resource.NORTH)) != null && tmp.isState(Resource.SOUTH) && tmp.getColor() == Resource.EMPTY) colorize(tmp);
			if (dir != Resource.SOUTH && td.isState(Resource.SOUTH) && (tmp = td.getNeighbor(Resource.SOUTH)) != null && tmp.isState(Resource.NORTH) && tmp.getColor() == Resource.EMPTY) colorize(tmp);
			if (dir != Resource.EAST && td.isState(Resource.EAST) && (tmp = td.getNeighbor(Resource.EAST)) != null && tmp.isState(Resource.WEST) && tmp.getColor() == Resource.EMPTY) colorize(tmp);
			if (dir != Resource.WEST && td.isState(Resource.WEST) && (tmp = td.getNeighbor(Resource.WEST)) != null && tmp.isState(Resource.EAST) && tmp.getColor() == Resource.EMPTY) colorize(tmp);
		}
	}

	public void colorize(TileData td) {
			
		if (td != null) { // Tile is not currently colored
			colorizeHelper(td, Resource.NORTH);
			colorizeHelper(td, Resource.SOUTH);
			colorizeHelper(td, Resource.EAST);
			colorizeHelper(td, Resource.WEST);
		}
	}
	
	private void clearCurrentTile() {
		int tmp = tileList.get(mousePosition).getState();
		tmp &= ~(Resource.MGREEN | Resource.MRED);
		tileList.get(mousePosition).setState(tmp);
		mousePosition = -1;
	}
		
	public void drawTiles() {
		Iterator<TileData> iter = tileList.iterator();
		while(iter.hasNext()) {
			ifImages.render(iter.next());
		}
	}	
	public TileData getTile(int index) {
		return tileList.get(index);
	}
	
	private class ImageFetcher {
	
		HashMap<Integer, Image> hmMasterTiles = null;
		
		ImageFetcher() {
			hmMasterTiles = new HashMap<Integer, Image>();

			try {
				// There are only 3 wire images, we copy and rotate to get other directions.
				Image tileImages = new Image(Resource.TILE_GRAPHICS);
				
				Image greyNorth = tileImages.getSubImage(255, 53, 48, 48);
				Image blueNorth = tileImages.getSubImage(157, 53, 48, 48);
				Image redNorth = tileImages.getSubImage(206, 53, 48, 48);

				Image greySouth = greyNorth.copy();
				Image greyEast = greyNorth.copy();
				Image greyWest = greyNorth.copy();
				Image blueSouth = blueNorth.copy();
				Image blueEast = blueNorth.copy();
				Image blueWest = blueNorth.copy();
				Image redSouth = redNorth.copy();
				Image redEast = redNorth.copy();
				Image redWest = redNorth.copy();
				
				greySouth.rotate(180.0f);
				blueSouth.rotate(180.0f);
				redSouth.rotate(180.0f);
				greyWest.rotate(270.0f);
				blueWest.rotate(270.0f);
				redWest.rotate(270.0f);
				greyEast.rotate(90.0f);
				blueEast.rotate(90.0f);
				redEast.rotate(90.0f);
				
				hmMasterTiles.put(Resource.OPENSET, tileImages.getSubImage(124, 408, 48, 48));
				hmMasterTiles.put(Resource.CLOSEDSET, tileImages.getSubImage(172, 408, 48, 48));
				hmMasterTiles.put(Resource.TRACE, tileImages.getSubImage(124, 456, 48, 48));
				hmMasterTiles.put(Resource.TAIL, tileImages.getSubImage(172, 456, 48, 48));
				
				hmMasterTiles.put(Resource.NORTH, greyNorth);
				hmMasterTiles.put(Resource.NORTH | Resource.BLUE, blueNorth);
				hmMasterTiles.put(Resource.NORTH | Resource.RED, redNorth);
				hmMasterTiles.put(Resource.SOUTH, greySouth);
				hmMasterTiles.put(Resource.SOUTH | Resource.BLUE, blueSouth);
				hmMasterTiles.put(Resource.SOUTH | Resource.RED, redSouth);
				hmMasterTiles.put(Resource.EAST, greyEast);
				hmMasterTiles.put(Resource.EAST | Resource.BLUE, blueEast);
				hmMasterTiles.put(Resource.EAST | Resource.RED, redEast);
				hmMasterTiles.put(Resource.WEST, greyWest);
				hmMasterTiles.put(Resource.WEST | Resource.BLUE, blueWest);
				hmMasterTiles.put(Resource.WEST | Resource.RED, redWest);
				hmMasterTiles.put(Resource.MGREEN, tileImages.getSubImage(3, 4, 48, 48));
				hmMasterTiles.put(Resource.MRED, tileImages.getSubImage(56, 4, 48, 48));
				hmMasterTiles.put(Resource.EMPTY, tileImages.getSubImage(157, 4, 48, 48));
				hmMasterTiles.put(Resource.GND | Resource.BLUE, tileImages.getSubImage(255, 4, 48, 48));
				hmMasterTiles.put(Resource.GND | Resource.RED, tileImages.getSubImage(206, 4, 48, 48));
				
			} catch (SlickException e) {e.printStackTrace();}
		}

		void render(TileData tData) {
			
			int color = tData.getState() & Resource.ALLCOLOR;
			
			// Either render empty or draw full tile w/ mouse effects.
			if ((tData.getState() & ~(Resource.MGREEN | Resource.MRED)) == Resource.EMPTY) {
				hmMasterTiles.get(Resource.EMPTY).draw(tData.xpos, tData.ypos);
			} else if (tData.isState(Resource.GND)) {
				// Draw colored ground
// BAD!!!
				if (color == 6) color = Resource.RED;
				hmMasterTiles.get(Resource.GND | color).draw(tData.xpos, tData.ypos);
			} else {
				if (tData.isState(Resource.NORTH)) hmMasterTiles.get(Resource.NORTH | color).draw(tData.xpos, tData.ypos);
				if (tData.isState(Resource.SOUTH)) hmMasterTiles.get(Resource.SOUTH | color).draw(tData.xpos, tData.ypos);				
				if (tData.isState(Resource.EAST)) hmMasterTiles.get(Resource.EAST | color).draw(tData.xpos, tData.ypos);				
				if (tData.isState(Resource.WEST)) hmMasterTiles.get(Resource.WEST | color).draw(tData.xpos, tData.ypos);				
			}
			// Draw Mouse Effects
	//		if (tData.isState(Resource.MGREEN) || tData.isState(Resource.MRED)) {
	//			hmMasterTiles.get((tData.getState() & (Resource.MGREEN | Resource.MRED))).draw(tData.xpos, tData.ypos);
	//		}
			if (tData.isState(Resource.MGREEN)) {
				hmMasterTiles.get(Resource.MGREEN).draw(tData.xpos, tData.ypos);
			} else if (tData.isState(Resource.MRED)) {
				hmMasterTiles.get(Resource.MRED).draw(tData.xpos, tData.ypos);
			}
			// Draw Debug Info
			if (tData.isState(Resource.OPENSET)) hmMasterTiles.get(Resource.OPENSET).draw(tData.xpos, tData.ypos);
			if (tData.isState(Resource.CLOSEDSET)) hmMasterTiles.get(Resource.CLOSEDSET).draw(tData.xpos, tData.ypos);
			if (tData.isState(Resource.TRACE)) hmMasterTiles.get(Resource.TRACE).draw(tData.xpos, tData.ypos);
			if (tData.isState(Resource.TAIL)) hmMasterTiles.get(Resource.TAIL).draw(tData.xpos, tData.ypos);
		}
	}
}
//JRA: Original Code End