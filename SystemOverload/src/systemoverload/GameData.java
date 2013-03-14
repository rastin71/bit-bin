package systemoverload;
/*****************************************************
System Overload Game
James "Ron" Astin
ID:  928260631
CS 313 AI and Game Design
Fall 2012
------------------------------------------------------
Singleton class representing Game Energy storage and use.

*****************************************************/

//JRA: Original Code Begin
public class GameData {

	int systemEnergy = 0;
	int redEnergy = 0;
	int blueEnergy = 0;
	int costTrace = 0;
	int pulseCost = 0;
	
	private static GameData instance = null;
	
	private GameData() {}
	
	public boolean gameOver() {
		return (systemEnergy <= 0);
	}
	
	public static GameData getInstance() {
		if (instance == null) {
			instance = new GameData();
			instance.init();
		} return instance;
	}
	
	public void pulseBlue() {
		if (systemEnergy > 0) {
			systemEnergy -= pulseCost;
			blueEnergy += pulseCost;
		}
	}
	
	public void pulseRed() {
		if (systemEnergy > 0) {
			systemEnergy -= pulseCost;
			redEnergy += pulseCost;
		}
	}
	
	public int getSystemEnergy() {
		return systemEnergy;
	}
	
	public void setSystemEnergy(int systemEnergy) {
		this.systemEnergy = systemEnergy;
	}
	
	public int getRedEnergy() {
		return redEnergy;
	}
	
	public void setRedEnergy(int redEnergy) {
		this.redEnergy = redEnergy;
	}
	
	public int getBlueEnergy() {
		return blueEnergy;
	}
	
	public void setBlueEnergy(int blueEnergy) {
		this.blueEnergy = blueEnergy;
	}
	
	public boolean buyBlueTrace() {
		if (blueEnergy < costTrace) return false;
		else {
			blueEnergy -= costTrace;
			return true;
		}
	}
	
	public boolean buyRedTrace() {
		if (redEnergy < costTrace) return false;
		else {
			redEnergy -= costTrace;
			return true;
		}		
	}
	
	public void init() {
		redEnergy = blueEnergy = 1000;
		systemEnergy = 10000;
		costTrace = 50;
		pulseCost = 5;
	}

}
//JRA: Original Code End
