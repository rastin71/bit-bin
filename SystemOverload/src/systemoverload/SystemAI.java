package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Top level AI Wrapper. Transitions AI state. Places inital
ground after player selects, then traces out, then calls A*
to grow until no viable targets remain.

*****************************************************/
//JRA: Original Code Begin
public class SystemAI {
	
	// The A* component of this class works a bit different because we have multiple viable destinations.
	// Our open set is also the total collection of squares we control.
	// What we want at the end is the best choice segment to trace.
	//
	//  A* will run on each LED not already controlled by RED/BLUE.
	//  LEDs that are slightly closer for the RED player will be preferred. 
	
	
	// Wrapper for all System AI.
	// GamePlayState passes in a PlayerIntent object to processAI.
	// PlayerIntent will record results of all AI intents.
	Resource.PlayerState aiState = Resource.PlayerState.HOLD;
	TileData redGround = null;
	int playerGround;
	AStar aStar = null;
	LED led = null;
	
	int clock;
	
	public SystemAI(LED l) {
		led = l;
		aiState = Resource.PlayerState.HOLD;
		clock = 0;
		aStar = new AStar(l);
	}
	
	public void setPlayerGround(int index) {
		playerGround = index;
		aiState = Resource.PlayerState.GROUND;
	}
	
	public void processAI() {
		TileData next = null;
		
		if (aiState == Resource.PlayerState.GROUND){
			System.out.println("PLACING RED GROUND!!!");
			// Place Red Ground
			redGround = placeGround2();
			aiState = Resource.PlayerState.INIT;
			
		} else if (aiState == Resource.PlayerState.INIT) {
			// After Ground place no need for pathfinding, first move must be north to take first north tile.
			next = redGround.getNeighbor(Resource.NORTH);
			if (next != null && next.getColor() == Resource.EMPTY && GameData.getInstance().buyRedTrace()) {
				next.addState(Resource.SOUTH);
				TileGrid.getInstance().colorize(next);
				aStar.addRedTile(next);
				aiState = Resource.PlayerState.SOLDER;
			}
		} else if (aiState == Resource.PlayerState.SOLDER){
			// Do Intelligent Stuff
			if (!aStar.plot()) aiState = Resource.PlayerState.WAIT;
			else if (GameData.getInstance().buyRedTrace()) {
				aStar.getTargetTile().addState(aStar.getTargetTrace());
				TileGrid.getInstance().colorize(aStar.getTargetTile());
				if (aStar.getTargetTile().getColor() == Resource.RED) aStar.addRedTile(aStar.getTargetTile());		
			}
		} else if (aiState == Resource.PlayerState.WAIT) {
			System.out.println("No Target");
			aiState = Resource.PlayerState.PLACEIC;
		} else if (aiState == Resource.PlayerState.PLACEIC) {
			
		}
	}
	
	private TileData placeGround() {
		int index;
		TileData tmpTile = null;
		index = Resource.NUMCOLS + (int)(Math.random()*(((Resource.NUMROWS - 1) * Resource.NUMCOLS) - 9)); // rnd nbr should be #tiles - (1 row + 8 lights + 1 playerpick) 312?
		if (index >= Resource.LED_LIME_ADJ_INDEX) index++;
		if (index >= Resource.LED_PURPLE_ADJ_INDEX) index++;
		if (index >= Resource.LED_PINK_ADJ_INDEX) index++;
		if (index >= Resource.LED_TEAL_ADJ_INDEX) index++;
		if (index >= Resource.LED_BROWN_ADJ_INDEX) index++;
		if (index >= Resource.LED_WHITE_ADJ_INDEX) index++;
		if (index >= Resource.LED_BLUE_ADJ_INDEX) index++;
		if (index >= Resource.LED_YELLOW_ADJ_INDEX) index++;
		if (index >= playerGround) index++;
		tmpTile = TileGrid.getInstance().getTile(index);
		tmpTile.setState(Resource.RED | Resource.GND);
		return tmpTile;
	}

	private TileData placeGround2() {
		TileData tmpTile = null;
		tmpTile = TileGrid.getInstance().getTile(24 * 6 - 1);
		tmpTile.setState(Resource.RED | Resource.GND);
		return tmpTile;
	}
	
}
//JRA: Original Code End