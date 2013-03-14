package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Class TileData represents a single game tile. Contains
coordinate rendering offsets, location data, AI weights,
content and access masks and helper functions that allow
access to neighbors.

*****************************************************/
//JRA: Original Code Begin
public class TileData {
	
	int xpos, ypos;
	int row, col;
	int index;
	private int tState = Resource.EMPTY;
	private int tMask = Resource.EMPTY;
	int estimatedTraceCost;
	double aiImportance = 0.0f;
	
	public TileData(int x, int y , int i) {
		xpos = x;
		ypos = y;
		index = i;
		row = i / Resource.NUMCOLS;
		col = i % Resource.NUMCOLS;
	}
	
	public int getState() {return tState;}
	public boolean setState(int val) {tState = val; return true;}
	public boolean addState(int val) {tState |= val; return true;}
	public void clearState(int val) {tState &= ~val;}
	
	public int getMask() {return tMask;}
	public boolean setMask(int val) {tMask = val; return true;}
	public boolean addMask(int val) {tMask |= val; return true;}
	public void clearMask(int val) {tMask &= ~val;}
	
	public TileData getNeighbor(int dir) {
		if (dir == Resource.NORTH) {
			if (index < Resource.NUMCOLS) return null;		
			else return TileGrid.getInstance().getTile(index - Resource.NUMCOLS);
			 
		} else if (dir == Resource.SOUTH) {
			if (index >= Resource.NUMCOLS * (Resource.NUMROWS - 1)) return null;		
			else return TileGrid.getInstance().getTile(index + Resource.NUMCOLS);
			
		} else if (dir == Resource.EAST) {
			if (index % Resource.NUMCOLS == Resource.NUMCOLS - 1) return null;		
			else return TileGrid.getInstance().getTile(index + 1);
			
		} else if (dir == Resource.WEST) {
			if (index % Resource.NUMCOLS == 0) return null;		
			else return TileGrid.getInstance().getTile(index - 1);
			
		} return null;
	}
	
	public int getTileEstDistance(TileData dest) {
		return Math.abs(dest.row - row) + Math.abs(dest.col - col);
	}

	public boolean isState(int state) {
		return (tState & state) == state;
	}
	public boolean isMask(int state) {
		return (tMask & state) == state;
	}
	public int getColor() {
		return (tState & Resource.ALLCOLOR);
	}
}
//JRA: Original Code End