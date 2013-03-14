package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Static class for global/constant game data and
data related helper functions.

*****************************************************/
//JRA: Original Code Begin
public class Resource {
	
	public static final String DISPLAY_BACKGROUND = "resources/CircuitBackground6.jpg";
	public static final String PLAY_BACKGROUND = "resources/CircuitBackground9.jpg";
	public static final String LOGO = "resources/Logo4.png";
	public static final String MENU_GRAPHICS = "resources/MenuOptions2.png";
	public static final String TILE_GRAPHICS = "resources/tiles.png";
	
	public static enum Color {BLUE, GREY, NONE, RED};
	public static enum LEDColor {BLACK, ORANGE, BROWN, WHITE, TEAL, LIME, RED, GREEN, BLUE, YELLOW, PURPLE, PINK};
	
	public static enum PlayerState {GROUND, INIT, SOLDER, HOLD, WAIT, PLACEIC, PROGIC}; // used for AI decision states and player UI modal states.
	
	// Common Mask Values
	public static final int EMPTY 	= 0;
	public static final int GND		= 1;
	public static final int RED 	= 2;
	public static final int BLUE  	= 4;
	public static final int NORTH	= 8;
	public static final int SOUTH	= 16;
	public static final int EAST	= 32;
	public static final int WEST	= 64;
	public static final int MGREEN  = 128;
	public static final int MRED	= 256;
	
	// AI suggested paths
	public static final int PATH_NORTH = 512;
	public static final int PATH_SOUTH = 1024;
	public static final int PATH_EAST = 2048;
	public static final int PATH_WEST = 4096;
	
	public static final int OPENSET = 8192;
	public static final int CLOSEDSET = 16384;
	public static final int TRACE = 32768;
	public static final int TAIL = 65536;
	
	public static final int INIT	= -1;
	public static final int ALLDIR  = NORTH | SOUTH | EAST | WEST;
	public static final int ALLCOLOR = RED | BLUE;
	public static final int PATH_MASK = ~(PATH_NORTH | PATH_SOUTH | PATH_EAST | PATH_WEST);
	
	public static final int TILESIZE = 48;
	public static final int NUMROWS = 14;
	public static final int NUMCOLS = 24;
	public static final int GRID_X_OFFSET = 228;
	public static final int GRID_Y_OFFSET = 169;
	
	public static final int SLEEP = 1200;
	
	public static final int LED_BLACK_ADJ_INDEX = 4;
	public static final int LED_ORANGE_ADJ_INDEX = 9;
	public static final int LED_RED_ADJ_INDEX = 14;
	public static final int LED_GREEN_ADJ_INDEX = 19;

	public static final int LED_BROWN_ADJ_INDEX = 316;
	public static final int LED_WHITE_ADJ_INDEX = 321;
	public static final int LED_TEAL_ADJ_INDEX = 239;
	public static final int LED_LIME_ADJ_INDEX = 96;
	public static final int LED_BLUE_ADJ_INDEX = 326;
	public static final int LED_YELLOW_ADJ_INDEX = 331;
	public static final int LED_PURPLE_ADJ_INDEX = 119;
	public static final int LED_PINK_ADJ_INDEX = 216;
	
	public static int oppositeDirection(int dir) {
		if (dir == NORTH) return SOUTH;
		if (dir == SOUTH) return NORTH;
		if (dir == EAST) return WEST;
		if (dir == WEST) return EAST;
		return EMPTY;
	}
}
//JRA: Original Code End
