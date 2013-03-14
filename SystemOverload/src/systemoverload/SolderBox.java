package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Solderbox class is GUI display for player soldering.
State tracking/changing code for color awareness.

*****************************************************/
//JRA: Original Code Begin
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SolderBox {

	// use offsets since initial x,y is constructor input.
	private static final int EXIT_TOP = 275;
	private static final int EXIT_LEFT = 865;
	private static final int EXIT_HEIGHT = 28;
	private static final int EXIT_WIDTH = 28;

	private int x, y;
	Image frame = null;

	Image redBall = null;
	Image greyBall = null;
	Image blueBall = null;
	Image goldBall = null;
	Image redCBall = null;
	Image greyCBall = null;
	Image blueCBall = null;
	Image goldCBall = null;
	
	Image barNSRed = null;
	Image barNSBlue = null;
	Image barNSGrey = null;
	Image barEWRed = null;
	Image barEWBlue = null;
	Image barEWGrey = null;

	// Current State
	Image northBall = null;
	Image southBall = null;
	Image eastBall = null;
	Image westBall = null;
	Image centerBall = null;
	Image northBar = null;
	Image southBar = null;
	Image eastBar = null;
	Image westBar = null;

	TileData tdTile = null;
	boolean nActive, sActive, eActive, wActive, cActive;
	boolean nClickable, sClickable, eClickable, wClickable, cClickable;
	int hover;

	public SolderBox(int x, int y) {
		this.x = x;
		this.y = y;
		Image tmp;
		try {
			tmp = new Image(Resource.TILE_GRAPHICS);
			frame = tmp.getSubImage(307, 4, 600, 600);
			redBall = tmp.getSubImage(3, 54, 48, 48);
			greyBall = tmp.getSubImage(109, 54, 48, 48);
			blueBall = tmp.getSubImage(56, 54, 48, 48);
			goldBall = tmp.getSubImage(1, 508, 48, 48);
			redCBall = tmp.getSubImage(4, 409, 37, 37);
			blueCBall = tmp.getSubImage(45, 409, 37, 37);
			greyCBall = tmp.getSubImage(86, 409, 37, 37);
			goldCBall = tmp.getSubImage(48, 508, 37, 37);
			barEWGrey = tmp.getSubImage(1, 447, 119, 20);
			barEWBlue = tmp.getSubImage(1, 467, 119, 20);
			barEWRed = tmp.getSubImage(1, 487, 119, 20);			
			barNSGrey = barEWGrey.copy();
			barNSBlue = barEWBlue.copy();
			barNSRed = barEWRed.copy();
			barNSGrey.rotate(90.0f);
			barNSBlue.rotate(90.0f);
			barNSRed.rotate(90.0f);
			hover = Resource.EMPTY;
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Image getColorBar(int pos) {
		int position = pos & tdTile.getState();
		int color = tdTile.getColor();
		if (position == Resource.NORTH || position == Resource.SOUTH) {
			if (color == Resource.BLUE) return barNSBlue;
			if (color == Resource.RED) return barNSRed;
			return barNSGrey;
		} else if (position == Resource.EAST || position == Resource.WEST) {
			if (color == Resource.BLUE) return barEWBlue;
			if (color == Resource.RED) return barEWRed;
			return barEWGrey;
		}
		return null;
	}
	
	private Image getEndBall(TileData local, int direction) {
		int color = local.getColor();
		if (local.isState(direction)) {
			if (color == Resource.RED) return redBall;
			if (color == Resource.BLUE) return blueBall;
			else return greyBall;
		}
		return null;
	}
	
	private Image getNeighborBall(TileData base, int dir) {
		
		Image retVal = null;
		TileData neighbor = base.getNeighbor(dir);
		int opp = 0;
		
		switch (dir) {
			case Resource.NORTH: opp = Resource.SOUTH; break;
			case Resource.SOUTH: opp = Resource.NORTH; break;
			case Resource.EAST: opp = Resource.WEST; break;
			case Resource.WEST: opp = Resource.EAST; break;
		}
		
		if (base.isMask(dir)) return null;	// MASK
		else if ((retVal = getEndBall(base, dir)) != null) return retVal; // LOCAL
		else if (base.getNeighbor(dir) == null) return greyBall; // Unmasked NULL implied LED
		else if ((neighbor != null) && (dir == Resource.SOUTH) && neighbor.isState(Resource.GND)) // GROUND
			return (neighbor.isState(Resource.RED)) ? redBall : blueBall;
		else if (neighbor.isState(opp | Resource.RED)) return redBall; // REMOTE RED
		else if (neighbor.isState(opp | Resource.BLUE)) return blueBall; // REMOTE BLUE
		return greyBall;
	}
	
	public void configure(TileData td) {
		if (td != null && !td.isState(Resource.MRED)) {
			tdTile = td;
			northBall = getNeighborBall(td, Resource.NORTH);
			southBall = getNeighborBall(td, Resource.SOUTH);
			eastBall = getNeighborBall(td, Resource.EAST);
			westBall = getNeighborBall(td, Resource.WEST);

			if (td.isState(Resource.RED)) {
				centerBall = redCBall;
			} else if (td.isState(Resource.BLUE)) {
				centerBall = blueCBall;
			} else centerBall = greyCBall;
			
			northBar = getColorBar(Resource.NORTH);
			southBar = getColorBar(Resource.SOUTH);
			eastBar = getColorBar(Resource.EAST);
			westBar = getColorBar(Resource.WEST);
			nActive = sActive = eActive = wActive = cActive = false;
			nClickable = (northBar == null || northBall != null);
			sClickable = (southBar == null || southBall != null);
			eClickable = (eastBar == null || eastBall != null);
			wClickable = (westBar == null || westBall != null);
			cClickable = true;
		}
	}
	
	public boolean isActive() {
		return (tdTile != null); 
	}
	
	public void draw() {
		frame.draw(x, y);		
		if (northBar != null) northBar.draw(668, 385);
		if (southBar != null) southBar.draw(668, 475);
		if (eastBar != null) eastBar.draw(710, 435);
		if (westBar != null) westBar.draw(615, 435);
		if (centerBall != null) if (cActive | (hover == -1)) goldCBall.draw(708, 425); else centerBall.draw(708, 425);
		if (northBall != null) if (nActive | (hover == Resource.NORTH)) goldBall.draw(702, 335); else northBall.draw(702, 335);
		if (southBall != null) if (sActive | (hover == Resource.SOUTH)) goldBall.draw(702, 505); else southBall.draw(702, 505);
		if (eastBall != null) if (eActive | (hover == Resource.EAST)) goldBall.draw(786, 420); else eastBall.draw(786, 420);
		if (westBall != null) if (wActive | (hover == Resource.WEST)) goldBall.draw(616, 420); else westBall.draw(616, 420);
	}

	public void processInput(int x, int y, boolean clicked) {
		if (clicked && x >= EXIT_LEFT && x <= EXIT_LEFT + EXIT_WIDTH && y >= EXIT_TOP && y <= EXIT_TOP + EXIT_HEIGHT) {
			tdTile = null;
		} else {
			if (x > 708 && y > 425 && x < 708 + 37 && y < 425 + 37) { // Center
				if (clicked & cClickable) {
					cActive = !cActive;
					processClicks();
				}
				else hover = -1;
			}
			else if (x > 702 && y > 335 && x < 702 + 48 && y < 335 + 48) { // North
				if (clicked & nClickable) {
					nActive = !nActive;
					processClicks();
				}
				else hover = Resource.NORTH;
			}	
			else if (x > 702 && y > 505 && x < 702 + 48 && y < 505 + 48) { // South
				if (clicked & sClickable) {
					sActive = !sActive;
					processClicks();
				}
				else hover = Resource.SOUTH;
			}
			else if (x > 786 && y > 420 && x < 786 + 48 && y < 420 + 48) { // East
				if (clicked & eClickable) {
					eActive = !eActive;
					processClicks();
				}
				else hover = Resource.EAST;
			}		
			else if (x > 616 && y > 420 && x < 616 + 48 && y < 420 + 48) { // West
				if (clicked & wClickable) {
					wActive = !wActive;
					processClicks();
				}
				else hover = Resource.WEST;
			}
			else {
				hover = Resource.EMPTY;
			}
		}
	}
	
	private void processClicks() {
		if (cActive) {
			if (nActive) {
				if(GameData.getInstance().buyBlueTrace()) {
					tdTile.addState(Resource.NORTH);
					TileGrid.getInstance().colorize(tdTile);
					northBar = getColorBar(Resource.NORTH);
				}
				cActive = nActive = false;
			}
			if (sActive) {
				if(GameData.getInstance().buyBlueTrace()) {
					tdTile.addState(Resource.SOUTH);
					TileGrid.getInstance().colorize(tdTile);
					southBar = getColorBar(Resource.SOUTH);
				}
				cActive = sActive = false;
			}
			if (eActive) {
				if(GameData.getInstance().buyBlueTrace()) {
					tdTile.addState(Resource.EAST);
					TileGrid.getInstance().colorize(tdTile);
					eastBar = getColorBar(Resource.EAST);
				}
				cActive = eActive = false;
			}
			if (wActive) {
				if(GameData.getInstance().buyBlueTrace()) {
					tdTile.addState(Resource.WEST);
					TileGrid.getInstance().colorize(tdTile);
					westBar = getColorBar(Resource.WEST);
				}
				cActive = wActive = false;
			}
		}
		nClickable = ((northBar == null) && (northBall != null) && !(sActive || eActive || wActive));
		sClickable = ((southBar == null) && (southBall != null) && !(nActive || eActive || wActive));
		eClickable = ((eastBar == null) && (eastBall != null) && !(nActive || sActive || wActive));
		wClickable = ((westBar == null) && (westBall != null) && !(nActive || sActive || eActive));
		cClickable = true;
	}
}
//JRA: Original Code End