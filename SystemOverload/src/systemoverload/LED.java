package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Collection class of game Lights. Provides functions for
changing light state and data for orientation for connecting.
Allows alternating pulse of lights for advanced game.

*****************************************************/

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

//JRA: Original Code Begin
public class LED {
		
	Light [] lights;
	int index = -1;
	
	public LED() {
		lights = new Light [] {
			      new Light(Resource.LEDColor.BLACK),
			      new Light(Resource.LEDColor.ORANGE),
			      new Light(Resource.LEDColor.BROWN),
			      new Light(Resource.LEDColor.WHITE),
			      new Light(Resource.LEDColor.TEAL),
			      new Light(Resource.LEDColor.LIME),
			      new Light(Resource.LEDColor.RED),
			      new Light(Resource.LEDColor.GREEN),
			      new Light(Resource.LEDColor.BLUE),
			      new Light(Resource.LEDColor.YELLOW),
			      new Light(Resource.LEDColor.PURPLE),
			      new Light(Resource.LEDColor.PINK),
			    };
	}
	
	public Light getAdjacent(int index) {
		int i;
		Light retVal = null;
		for (i = 0; i < 12; i++) {
			if (lights[i].adjIndex == index) { // == row && lights[i].y == col) {
				index = i;
				retVal = lights[i];
			}
		}
		return retVal;
	}

	public void render(boolean t) {
		int i;
		for(i = 0; i < 12; i++) {
			lights[i].render(t);
		}
	}
	
	public void repoll() {
		int i;
		for(i = 0; i < 12; i++) lights[i].repoll();
	}
	
	public class Light {
	
		Image offLight = null;
		Image redLight = null;
		Image blueLight = null;
		
		TileData adjTile = null;
		int adjIndex;
		int solderColor;		
		int lightDir;
		
		int freqRed, freqBlue, currentRed, currentBlue;		
		int x, y;
		
		private Light(Resource.LEDColor color) {
			solderColor = Resource.EMPTY;
			freqRed = freqBlue = currentRed = currentBlue = 0;
			try {
				Image tmp = new Image(Resource.TILE_GRAPHICS);
			
				switch (color) {
					case BLACK: //  row = 0; col = 4;
						offLight = tmp.getSubImage(8, 120, 48, 48); blueLight = tmp.getSubImage(8, 215, 48, 48); redLight = tmp.getSubImage(8, 310, 48, 48); 
						x = 420; y = 122; lightDir = Resource.NORTH; adjIndex = Resource.LED_BLACK_ADJ_INDEX; break;
					case ORANGE: // row = 0; col = 9; 
						offLight = tmp.getSubImage(57, 120, 48, 48); blueLight = tmp.getSubImage(57, 215, 48, 48); redLight = tmp.getSubImage(57, 310, 48, 48);
						x = 661; y = 122; lightDir = Resource.NORTH; adjIndex = Resource.LED_ORANGE_ADJ_INDEX; break;
					case BROWN: // row = 13; col = 4; 
						offLight = tmp.getSubImage(106, 120, 48, 48); blueLight = tmp.getSubImage(106, 215, 48, 48); redLight = tmp.getSubImage(106, 310, 48, 48);
						x = 420; y = 841; lightDir = Resource.SOUTH; adjIndex = Resource.LED_BROWN_ADJ_INDEX; break;
					case WHITE: // row = 13; col = 9; 
						offLight = tmp.getSubImage(155, 120, 48, 48); blueLight = tmp.getSubImage(155, 215, 48, 48); redLight = tmp.getSubImage(155, 310, 48, 48);
						x = 661; y = 841; lightDir = Resource.SOUTH; adjIndex = Resource.LED_WHITE_ADJ_INDEX; break;
					case TEAL: // row = 9; col = 23; 
						offLight = tmp.getSubImage(204, 120, 48, 48); blueLight = tmp.getSubImage(204, 215, 48, 48); redLight = tmp.getSubImage(204, 310, 48, 48);
						x = 1381; y = 600; lightDir = Resource.EAST; adjIndex = Resource.LED_TEAL_ADJ_INDEX; break;
					case LIME: // row = 4; col = 0; 
						offLight = tmp.getSubImage(253, 120, 48, 48); blueLight = tmp.getSubImage(253, 215, 48, 48); redLight = tmp.getSubImage(253, 310, 48, 48);
						x = 182; y = 360; lightDir = Resource.WEST; adjIndex = Resource.LED_LIME_ADJ_INDEX; break;
					case RED: // row = 0; col = 14; 
						offLight = tmp.getSubImage(8, 169, 48, 48); blueLight = tmp.getSubImage(8, 264, 48, 48); redLight = tmp.getSubImage(8, 359, 48, 48);
						x = 901; y = 122; lightDir = Resource.NORTH; adjIndex = Resource.LED_RED_ADJ_INDEX; break;
					case GREEN: // row = 0; col = 19; 
						offLight = tmp.getSubImage(57, 168, 48, 48); blueLight = tmp.getSubImage(57, 263, 48, 48); redLight = tmp.getSubImage(57, 359, 48, 48);
						x = 1139; y = 122; lightDir = Resource.NORTH; adjIndex = Resource.LED_GREEN_ADJ_INDEX; break;
					case BLUE: // row = 13; col = 14; 
						offLight = tmp.getSubImage(106, 168, 48, 48); blueLight = tmp.getSubImage(106, 263, 48, 48); redLight = tmp.getSubImage(106, 359, 48, 48);
						x = 901; y = 841; lightDir = Resource.SOUTH; adjIndex = Resource.LED_BLUE_ADJ_INDEX; break;
					case YELLOW: // row = 13; col = 19; 
						offLight = tmp.getSubImage(155, 168, 48, 48); blueLight = tmp.getSubImage(155, 263, 48, 48); redLight = tmp.getSubImage(155, 359, 48, 48);
						x = 1139; y = 841; lightDir = Resource.SOUTH; adjIndex = Resource.LED_YELLOW_ADJ_INDEX; break;
					case PURPLE: // row = 4; col = 23; 
						offLight = tmp.getSubImage(203, 168, 48, 48); blueLight = tmp.getSubImage(203, 264, 48, 48); redLight = tmp.getSubImage(203, 359, 48, 48);
						x = 1381; y = 360; lightDir = Resource.EAST; adjIndex = Resource.LED_PURPLE_ADJ_INDEX; break;
					case PINK: // row = 9; col = 0; 
						offLight = tmp.getSubImage(253, 168, 48, 48); blueLight = tmp.getSubImage(253, 264, 48, 48); redLight = tmp.getSubImage(253, 359, 48, 48);
						x = 182; y = 600; lightDir = Resource.WEST; adjIndex = Resource.LED_PINK_ADJ_INDEX; break;
				}
			
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adjTile = TileGrid.getInstance().getTile(adjIndex);
			adjTile.setMask(Resource.EMPTY);
			switch(lightDir) {
			case Resource.NORTH:
				adjTile.addState(Resource.PATH_NORTH); break;
			case Resource.SOUTH:
				adjTile.addState(Resource.PATH_SOUTH); break;
			case Resource.EAST:
				adjTile.addState(Resource.PATH_EAST); break;
			case Resource.WEST:
				adjTile.addState(Resource.PATH_WEST); break;
			}
			
		}
		
		private void setFrequency(int red, int blue) {
			if ((red | blue) != 0) this.adjTile.aiImportance = 0;
			freqRed = red;
			freqBlue = blue;
		}
		
		private void repoll() {
			//	Resource.write(lights[i].adjTile.xpos + 5, lights[i].adjTile.ypos + 5, "ADJ");
			if (adjTile.getColor() != Resource.EMPTY && adjTile.isState(lightDir)) solderColor = adjTile.getColor();
			if (solderColor == Resource.RED) setFrequency(1, 0);
			if (solderColor == Resource.BLUE) setFrequency(0, 1);
		}
		
		private void render(boolean trip) {
			if (freqRed + freqBlue == 0) {
				offLight.draw(x, y);
			} else {
				if (currentRed > currentBlue) {
					redLight.draw(x, y);
					if (trip) {
						currentRed--;
						GameData.getInstance().pulseRed();
					}
				} else if (currentBlue >= currentRed) {
					blueLight.draw(x, y);
					if (trip) {
						currentBlue--;
						GameData.getInstance().pulseBlue();
					}
				}
				if (currentRed + currentBlue <= 0) {
					currentRed = freqRed;
					currentBlue = freqBlue;
				}
			}
		}
	}
}
//JRA: Original Code End