package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
AStar logic adapted for game.

Notes:
	Each use of logic probes all available LEDs.
	"Origin" is set of all controlled (RED) tiles.
	Tracks all best paths for targets accumulating heuristic values of 
		1/(0.001 + (TargetDistance^3)) to each origin adjacent tile.
	Randomly chooses target adjacent tile based on range of sum of heuristics.
	Treat all player (BLUE) tiles as obstacles.

*****************************************************/
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//JRA: Original Code Begin
public class AStar {

	private Set<TileData> targets = null;
	private Set<TileData> closedSet = null;
	private Set<TileData> openSet = null;
	private Set<TileData> redTiles = null;
	private int openDistance2Target;
	TileData targetTile = null;
	int targetTrace = Resource.INIT;
	
	public AStar(LED l) {
		redTiles = new HashSet<TileData>();
		closedSet = new HashSet<TileData>();
		openSet = new HashSet<TileData>();
		targets = new HashSet<TileData>();
		int i;
		for (i = 0; i < 12; i++) {
			targets.add(l.lights[i].adjTile);
			l.lights[i].adjTile.aiImportance = 100;
		}
	}
	
	public void addRedTile(TileData tile) {
		tile.estimatedTraceCost = 0;
		tile.aiImportance = 0;
		redTiles.add(tile);
	}
	
	public boolean plot() {
		if (!checkTargets()) { // Remove attached LEDs, Reset current build selection.
			resetHeuristics(); // Reset all adjacent Red aiImportance values to 0.
			Iterator<TileData> iter = targets.iterator(); 
			while(iter.hasNext()) plotAStar(iter.next()); // plot all targets
			pickTarget(); // choose best tile
		}
		return (targetTile != null); // return if found.
	}
	
	private void plotAStar(TileData dest) {
		prepareOpenCloseSets(dest);
		TileData current = null;
		while (!closedSet.contains(dest) && !openSet.isEmpty()) {
			if ((current = getCurrent(dest)) == null) expand(dest);
			else addToClosedSet(current, dest);		
		}
		definePath(dest);		
	}
	
	private void pickTarget() {
		
		Set<TileData> adjacent = new HashSet<TileData>();
		Iterator<TileData> iter = redTiles.iterator();
		TileData tmp = null;
		TileData next = null;
		double range = 0.0;
		double choice = 0.0;
		boolean done = false;
		
		while (iter.hasNext()) { // loop through reds, load adjacent if importance set. Compute range

			tmp = iter.next();
//			System.out.println("Check RED Tile " + tmp.index + " " + tmp.aiImportance + " " + tmp.estimatedTraceCost);
//			tmp.addState(Resource.TRACE);
			
			next = tmp.getNeighbor(Resource.NORTH);
	//		System.out.println("Check RED_N Tile" + next.index + " " + next.aiImportance + " " + next.estimatedTraceCost);
			if (next != null && next.aiImportance > 0 && !adjacent.contains(next)) {
				range += next.aiImportance;
				adjacent.add(next);
			}

			next = tmp.getNeighbor(Resource.SOUTH);
	//		System.out.println("Check RED_S Tile" + next.index + " " + next.aiImportance + " " + next.estimatedTraceCost);
			if (next != null && next.aiImportance > 0 && !adjacent.contains(next)) {			
				adjacent.add(next);
				range += next.aiImportance;
			}
			
			next = tmp.getNeighbor(Resource.EAST);
	//		System.out.println("Check RED_E Tile" + next.index + " " + next.aiImportance + " " + next.estimatedTraceCost);
			if (next != null && next.aiImportance > 0 && !adjacent.contains(next)) {
				adjacent.add(next);
				range += next.aiImportance;
			}
			
			next = tmp.getNeighbor(Resource.WEST);
	//		System.out.println("Check RED_W Tile" + next.index + " " + next.aiImportance + " " + next.estimatedTraceCost);
			if (next != null && next.aiImportance > 0 && !adjacent.contains(next)) {
				adjacent.add(next);
				range += next.aiImportance;
			}
			
		} 
		
		if (range > 0.0) { // there is a winner
			choice = Math.random() * range;
			System.out.println("choice/range: " + choice + " / " + range);
			iter = adjacent.iterator();
			while (!done && iter.hasNext()) {
				tmp = iter.next();
				if (choice <= tmp.aiImportance) {
					done = true;
					if (tmp.estimatedTraceCost == 2) { // 2 traces from red means we want to extend a neighboring red towards this tile.
						if ((next = tmp.getNeighbor(Resource.EAST)) != null && redTiles.contains(next)) {
							targetTile = next;
							targetTrace = Resource.WEST;
						}
						else if ((next = tmp.getNeighbor(Resource.WEST)) != null && redTiles.contains(next)) {
							targetTile = next;
							targetTrace = Resource.EAST;
						}
						else if ((next = tmp.getNeighbor(Resource.NORTH)) != null && redTiles.contains(next)) {
							targetTile = next;
							targetTrace = Resource.SOUTH;
						}
						else if ((next = tmp.getNeighbor(Resource.SOUTH)) != null && redTiles.contains(next)) {
							targetTile = next;
							targetTrace = Resource.NORTH;
						}
					} else { // 1 trace from red means we want to route a neighboring red to center of this tile.
						targetTile = tmp;
						if ((next = tmp.getNeighbor(Resource.EAST)) != null && redTiles.contains(next) && next.isState(Resource.WEST)) targetTrace = Resource.EAST;
						else if ((next = tmp.getNeighbor(Resource.WEST)) != null && redTiles.contains(next) && next.isState(Resource.EAST)) targetTrace = Resource.WEST;
						else if ((next = tmp.getNeighbor(Resource.NORTH)) != null && redTiles.contains(next) && next.isState(Resource.SOUTH)) targetTrace = Resource.NORTH;
						else if ((next = tmp.getNeighbor(Resource.SOUTH)) != null && redTiles.contains(next) && next.isState(Resource.NORTH)) targetTrace = Resource.SOUTH;
					}
				} else choice -= tmp.aiImportance;
			}
		//	targetTile.addState(Resource.TAIL);
		} else { // no winner
			targetTile = null;
			targetTrace = Resource.INIT;
		}

	}
	
	public TileData getTargetTile() {
		return targetTile;
	}
	
	public int getTargetTrace() {
		return targetTrace;
	}
	
	private void resetHeuristics() {
		Iterator<TileData> iter = redTiles.iterator();
		TileData tmp = null;
		TileData next = null;
		while(iter.hasNext()) {
			tmp = iter.next();
			if ((next = tmp.getNeighbor(Resource.NORTH)) != null && next.aiImportance < 100) next.aiImportance = 0;
			if ((next = tmp.getNeighbor(Resource.SOUTH)) != null && next.aiImportance < 100) next.aiImportance = 0;
			if ((next = tmp.getNeighbor(Resource.EAST)) != null && next.aiImportance < 100) next.aiImportance = 0;
			if ((next = tmp.getNeighbor(Resource.WEST)) != null && next.aiImportance < 100) next.aiImportance = 0;
		}
	}
	
	private void prepareOpenCloseSets(TileData d) {
		closedSet.clear();
		openSet.clear();
		TileData tmp;
		openDistance2Target = Resource.INIT;
		Iterator<TileData> iter = redTiles.iterator();		
		while(iter.hasNext()) {
			tmp = iter.next();
			addToOpenSet(tmp, Resource.EMPTY, d);
		}
	}
	
	private boolean checkTargets() {
		boolean retVal = false;
		Iterator<TileData> iter = targets.iterator();
		TileData tmp = null;
		targetTile = null;
		targetTrace = Resource.INIT;
		
		while(iter.hasNext()) {
			tmp = iter.next();
			tmp.addState(Resource.TAIL);
			if (tmp.getColor() == Resource.BLUE) iter.remove();
			else if (redTiles.contains(tmp)) {
				if (tmp.isState(Resource.PATH_NORTH)) {
					if (tmp.isState(Resource.NORTH)) { 
						iter.remove();
					} else {
						targetTile = tmp;
						targetTrace = Resource.NORTH;
						retVal = true;
					}
				}
				else if (tmp.isState(Resource.PATH_SOUTH)) {
					if (tmp.isState(Resource.SOUTH)) { 
						iter.remove();
					} else {
						targetTile = tmp;
						targetTrace = Resource.SOUTH;
						retVal = true;
					}
				}
				if (tmp.isState(Resource.PATH_EAST)) {
					if (tmp.isState(Resource.EAST)) { 
						iter.remove();
					} else {
						targetTile = tmp;
						targetTrace = Resource.EAST;
						retVal = true;
					}
				}
				if (tmp.isState(Resource.PATH_WEST)) {
					if (tmp.isState(Resource.WEST)) { 
						iter.remove();
					} else {
						targetTile = tmp;
						targetTrace = Resource.WEST;
						retVal = true;
					}
				}
			}

		}
		return retVal;
	}
	
	private void expand(TileData dest) {
		// If a pass at expanding the ClosedSet does not add any nodes that are closer to the dest an expand pass is run before the next attempt.
		HashSet<TileData> copySet = new HashSet<TileData>(openSet);
		Iterator<TileData> iter = copySet.iterator();
		
		while(iter.hasNext()) {
			addToClosedSet(iter.next(), dest);
		}
	}
	
	private void addToOpenSet(TileData base, int dir, TileData dest) {
		TileData neighbor = null;
		TileData tmp = null;
		
		if (dir == Resource.EMPTY) {
			tmp = base;
		} else if ((neighbor = base.getNeighbor(dir)) != null && neighbor.getColor() == Resource.EMPTY && !openSet.contains(neighbor) && !closedSet.contains(neighbor)) {
			tmp = neighbor;
			tmp.estimatedTraceCost = base.estimatedTraceCost + ((base.isState(dir))? 0 : 1) + ((tmp.isState(Resource.oppositeDirection(dir)))? 0 : 1);
		}
			
		if (tmp != null) {
			if (tmp.getTileEstDistance(dest) < openDistance2Target || openDistance2Target == Resource.INIT) openDistance2Target = tmp.getTileEstDistance(dest);
			tmp.addState(Resource.OPENSET);
			openSet.add(tmp);
		}
	}
	
	private TileData getCurrent(TileData d) {
		TileData retVal = null;
		TileData tmp = null;
		boolean done = false;
		Iterator<TileData> iter = openSet.iterator();
		while(!done && iter.hasNext()) {
			tmp = iter.next();
			if (tmp.getTileEstDistance(d) == openDistance2Target) {
				retVal = tmp;
				done = true;
			}
		}
		return retVal;
	}
	
	private void definePath(TileData dest) {
		TileData tmp = null;
		if (dest.estimatedTraceCost > 2) {
			PathMaker pm = new PathMaker(closedSet, dest);
			pm.cull();	
		} else {
			targetTile = dest;
			if ((tmp = dest.getNeighbor(Resource.NORTH)) != null && redTiles.contains(tmp) && tmp.isState(Resource.SOUTH)) targetTrace = Resource.NORTH;
			if ((tmp = dest.getNeighbor(Resource.SOUTH)) != null && redTiles.contains(tmp) && tmp.isState(Resource.NORTH)) targetTrace = Resource.SOUTH;
			if ((tmp = dest.getNeighbor(Resource.EAST)) != null && redTiles.contains(tmp) && tmp.isState(Resource.WEST)) targetTrace = Resource.EAST;
			if ((tmp = dest.getNeighbor(Resource.WEST)) != null && redTiles.contains(tmp) && tmp.isState(Resource.EAST)) targetTrace = Resource.WEST;
		}
	}
	
	private void addToClosedSet(TileData tile, TileData dest) {
		// Called on a tile to add it to the closed list. Assume only called on tile in the open list.	
		addToOpenSet(tile, Resource.NORTH, dest);
		addToOpenSet(tile, Resource.SOUTH, dest);
		addToOpenSet(tile, Resource.EAST, dest);
		addToOpenSet(tile, Resource.WEST, dest);

		openSet.remove(tile);
		closedSet.add(tile);
		tile.addState(Resource.CLOSEDSET);
	}

	class PathMaker {
		
		Set<TileData> trace; // Set of explored nodes
		Set<TileData> tails; // Set of equal ends
		int highestCost;     // Value of current cull length
		int index;
		
		public PathMaker(Set<TileData> t, TileData dest) {
			index = dest.index;
			trace = t;
			tails = new HashSet<TileData>();
			tails.add(dest); // place target dest in ends set.
			highestCost = dest.estimatedTraceCost;
		}
		
		void cull() {
			/*  Backtrack from tailset along traceset.
			 *  Make a new tailset from the lowest cost trace tiles that are adjacent to tailset.
			 *  Remove all tiles from the traceset that are not cheaper than new tailset.
			 *  Repeat until cost of tailset < 3
			 *  Intent is to have a final set of tiles that are cheapest cost adjacent tiles for current target.
			 *  Each of which gets a heuristic add.
			 */
			TileData tmp = null;
			while(backTrace());
			Iterator<TileData> iter = tails.iterator();
			while(iter.hasNext()) {
				tmp = iter.next();
				tmp.addState(Resource.TRACE);
				System.out.println("Post Backtrace: " + tmp.index + " " + tmp.estimatedTraceCost + " " + highestCost + " " + index);
				tmp.aiImportance += 1.0/(double)(.001 + (highestCost * highestCost * highestCost));
			}
		}
		
		boolean backTrace() { 

//			System.out.println("Backtrace Called Tailsize:" + tails.size());
			Iterator<TileData> iter = tails.iterator();
			TileData[] neighbors = new TileData[4];
			Set<TileData> newTail = new HashSet<TileData>();
			TileData tmp = null;
			int cheapestNewTail = Resource.INIT;
			int i;
			
			while(iter.hasNext()) { // Loop through Tails
				tmp = iter.next();
				iter.remove();		// Tails die once used
				
				neighbors[0] = tmp.getNeighbor(Resource.NORTH); // get all neighbors
				neighbors[1] = tmp.getNeighbor(Resource.SOUTH);
				neighbors[2] = tmp.getNeighbor(Resource.EAST);
				neighbors[3] = tmp.getNeighbor(Resource.WEST);
				
				for (i = 0; i < 4; i++) { // Find Cheapest Cost, in trace neighbor
					if (neighbors[i] != null) {
						if (!trace.contains(neighbors[i])) {
							neighbors[i] = null; // not in trace
						} else {
							if (cheapestNewTail == Resource.INIT || cheapestNewTail > neighbors[i].estimatedTraceCost) cheapestNewTail = neighbors[i].estimatedTraceCost;
						}
					}
				}
				
				for (i = 0; i < 4; i++) {	// Loop through Neighbors - if valid, in trace and lower than rest - use to find min
					if (neighbors[i] != null && neighbors[i].estimatedTraceCost == cheapestNewTail) {
			//			System.out.println("NewTail " + neighbors[i].index + "/" + neighbors[i].aiImportance);
						newTail.add(neighbors[i]);
					}
				}
			}
			iter = newTail.iterator();
			while(iter.hasNext()) {  // Loop through newTail remove longer tails.
				tmp = iter.next();
				if (tmp.estimatedTraceCost != cheapestNewTail) iter.remove();
			}
			
			iter = trace.iterator();
			while(iter.hasNext()) {
				tmp = iter.next();
				if (tmp.estimatedTraceCost >= cheapestNewTail) iter.remove();
			}
			tails = newTail;		
		//	System.out.println("Min = " + cheapestNewTail);
			return cheapestNewTail > 2; // return if there should be another pass. If min is 3 or more then it can't have tiles adjacent to current reds.
		}
	}
}	
//JRA: Original Code END
