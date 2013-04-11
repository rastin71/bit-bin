import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ScreenBuffer {
	
	ArrayList<Image> alTiles = null;
	Image cursor = null;
	Image mark = null;
	long tl_x, tl_y, tr_x, tr_y, bl_x, bl_y, br_x, br_y;
	
	public ScreenBuffer(String mapfile) {
		
		int i;
		try {
			Image img0 = new Image("res/tile.png");
			Image img1 = new Image("res/tile2.png");
			Image imgx = new Image("res/tile_x.png");
			Image imgy = new Image("res/tile_y.png");
			
			cursor = new Image("res/mouse.png");
			mark = new Image("res/tile3.png");
			
			InputStreamReader in = new InputStreamReader(new FileInputStream(mapfile), "US-ASCII");
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
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
	public void render(int x_off, int y_off) {
		int i, j, x, y;
		// Shifting works because effective tile dimensions are all base 2 values.
		for (i = 0; i < Const.MAP_SIZE; i++) {
			x = i << 4;
			for (j = Const.MAP_SIZE - 1; j >= 0; j--) {
		    	y = j << 4;
		        alTiles.get(i * Const.MAP_SIZE + j).draw((x << 1) + (y << 1) + x_off, (x - y) + y_off);
		    }
		}
		cursor.draw(512.0f, 384.0f);
		
		if (tl_x > -1 && tl_x < 256 && tl_y > -1 && tl_y < 256) {
			mark.draw((tl_x << 5) + (tl_y << 5) + x_off, (tl_y << 4) - (tl_x << 4) + y_off);
		}
		if (tr_x > -1 && tr_x < 256 && tr_y > -1 && tr_y < 256) {
			mark.draw((tr_x << 5) + (tr_y << 5) + x_off, (tr_y << 4) - (tr_x << 4) + y_off);
		}
		if (bl_x > -1 && bl_x < 256 && bl_y > -1 && bl_y < 256) {
			mark.draw((bl_x << 5) + (bl_y << 5) + x_off, (bl_y << 4) - (bl_x << 4) + y_off);
		}
		if (br_x > -1 && br_x < 256 && br_y > -1 && br_y < 256) {
			mark.draw((br_x << 5) + (br_y << 5) + x_off, (br_y << 4) - (br_x << 4) + y_off);
		}
	}
	
	public void update(int x_off, int y_off) {
		long base_x = Math.round((-x_off + 2*y_off)/64.0);
		long base_y = Math.round((-x_off - 2*y_off)/64.0);
		tl_x = base_x;
		tl_y = base_y - 1;		
		tr_x = base_x + 16;
		tr_y = base_y + 15;
		bl_x = base_x - 24;
		bl_y = base_y + 23;
		br_x = base_x - 8;
		br_y = base_y + 39;
	}
	
}
