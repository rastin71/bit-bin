import java.util.ArrayList;

public class ScreenBuffer {
	
	ArrayList<TileMarker> alMarkers = null;

	public ScreenBuffer() {
		alMarkers = new ArrayList<TileMarker>();
	}
	
	class TileMarker {
		int start;
		int length;
		
		TileMarker(int s, int l) {
			start = s;
			length = l;
		}
	}
}
