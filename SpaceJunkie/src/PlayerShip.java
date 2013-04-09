import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class PlayerShip {
	public static enum Ship {STARTER, ESCAPE, NONE};
	ParticleSystem psys = null;
	HashMap<String, ConfigurableEmitter> primeEmitters = null;
	Image sImage = null;
	ArrayList<ConfigurableEmitterWrapper> cew = null;
	public float shipFacing = 0.0f;
	public int shipSpeed = 0;
	public float directionalVelocity = 0.0f;
	public float gimbleX;
	public float gimbleY;
	public float gimbleLimitX;
	public float gimbleLimitY;
	public float screenWidth;
	public float screenHeight;
	public float screenTop;
	public float screenLeft;
	public float shipHeight;
	public float shipWidth;
	public float adjustedImageX;
	public float adjustedImageY;
	public float adjustedMidpointX; // accounts for screenWidth, screenLeft & shipWidth
	public float adjustedMidpointY; // accounts for screenHeight, screenTop & shipHeight
	public float e1X, e1Y, e2X, e2Y;
	public float velX, velY, locX, locY;
	public float mass;
	
	PlayerShip(ParticleSystem ps, HashMap<String, ConfigurableEmitter> map) {
		primeEmitters = map;
		psys = ps;
		gimbleX = gimbleY = screenTop = screenLeft = velX = velY = locX = locY = 0;
		screenWidth = SpaceJunkieGame.SCREEN_WIDTH;
		screenHeight = SpaceJunkieGame.SCREEN_HEIGHT;
		cew = new ArrayList<ConfigurableEmitterWrapper>();
		changeShip(Ship.STARTER);
	}
	
	public void changeShip(Ship s) {
		int i;
		ConfigurableEmitterWrapper tmp1 = null;
		ConfigurableEmitterWrapper tmp2 = null;
		for (i = 0; i < cew.size(); i++) {
			psys.removeEmitter(cew.get(i).emitter);
		}
		cew.clear();
		switch(s) {
			case STARTER:
			try {
				sImage = new Image("res/ship.png");
				shipHeight = sImage.getHeight();
				shipWidth = sImage.getWidth();
				adjustedImageX = (screenWidth - shipWidth) / 2 + screenLeft;
				adjustedImageY = (screenHeight - shipHeight) / 2 + screenTop;
				adjustedMidpointX = adjustedImageX + (shipWidth / 2);
				adjustedMidpointY = adjustedImageY + (shipHeight / 2);
				tmp1 = new ConfigurableEmitterWrapper(primeEmitters.get("ShipExhaust").duplicate(), 13.0f, 20.0f, 180.0f);
				tmp2 = new ConfigurableEmitterWrapper(primeEmitters.get("ShipExhaust").duplicate(), -13.0f, 20.0f, 180.0f);
				tmp1.emitter.setEnabled(false);
				tmp2.emitter.setEnabled(false);
				psys.addEmitter(tmp1.emitter);
				psys.addEmitter(tmp2.emitter);
				cew.add(tmp1);
				cew.add(tmp2);
				mass = 1000000;
			} catch (SlickException e) {
				e.printStackTrace();
			}
				break;
			case ESCAPE:
				break;
			case NONE:
				break;
		}
	}

	public void update(Input in, int delta) {
		int i;
		ConfigurableEmitterWrapper tmp;

		if (in.isKeyPressed(Input.KEY_LEFT)) {
			directionalVelocity = (directionalVelocity > -0.1) ? directionalVelocity + -0.01f * delta : -0.1f;
		}
		if (in.isKeyPressed(Input.KEY_RIGHT)) {
			directionalVelocity = (directionalVelocity < 0.1) ? directionalVelocity + 0.01f * delta : 0.1f;
		}
		if (in.isKeyPressed(Input.KEY_SPACE)) directionalVelocity = 0.0f;
		if (shipFacing < 0) shipFacing += 360;
		if (shipFacing > 360) shipFacing -=360;
		sImage.rotate(directionalVelocity);
		shipFacing += directionalVelocity;
		
		for (i = 0; i < cew.size(); i++) {
			tmp = cew.get(i);
			tmp.emitter.setEnabled(shipSpeed > 0);
			tmp.reposition(shipFacing, adjustedMidpointX + gimbleX, adjustedMidpointY + gimbleY);
			if (i == 0) {
				e1X = tmp.xPos;
				e1Y = tmp.yPos;
			}
			if (i == 1) {
				e2X = tmp.xPos;
				e2Y = tmp.yPos;
			}
		}	
		
		if (in.isKeyPressed(Input.KEY_UP)) {
			directionalVelocity = 0.0f;
			if (shipSpeed < 3) {
				shipSpeed++;
				for (i = 0; i < cew.size(); i++) {
					((ConfigurableEmitter.SimpleValue)(cew.get(i).emitter.growthFactor)).setValue(-258 + (86 * shipSpeed));
				}
			}
		}
		if (in.isKeyPressed(Input.KEY_DOWN)) {
			directionalVelocity = 0.0f;
			if (shipSpeed > 0) {
				shipSpeed--;
				for (i = 0; i < cew.size(); i++) {
					((ConfigurableEmitter.SimpleValue)(cew.get(i).emitter.growthFactor)).setValue(-258 + (86 * shipSpeed));
				}
			}
		}
		if (shipSpeed > 0) {
			velX += (Math.sin(Math.toRadians(shipFacing)) * shipSpeed * delta) / mass;
			velY += (Math.cos(Math.toRadians(shipFacing)) * shipSpeed * delta) / mass;
		}
		gimbleLimitX = (float)Math.cos(Math.toRadians(shipFacing + 90.0)) * 70 * shipSpeed;
		gimbleLimitY = (float)Math.sin(Math.toRadians(shipFacing + 90.0)) * 70 * shipSpeed;

		gimbleX = valueMerge(gimbleLimitX, gimbleX);
		gimbleY = valueMerge(gimbleLimitY, gimbleY);
		
		locX += velX;
		locY += velY;
	}
	
	private float valueMerge(float limit, float value) {
		if (Math.abs(limit - value) < 0.1f) {
			return limit;
		} else if (limit > value) {
			return value + 0.05f;
		} else {
			return value - 0.05f;
		}
	}

	public void render() {
		sImage.draw(adjustedImageX + gimbleX, adjustedImageY + gimbleY); // 56x61 image
	}
	
	class ConfigurableEmitterWrapper {
		Vector2f vector = null;
		ConfigurableEmitter emitter;
		float xOffset, yOffset, xPos, yPos, angleOffset;
		
		ConfigurableEmitterWrapper(ConfigurableEmitter ce, float xoff, float yoff, float a) {
			xOffset = xoff;
			yOffset = yoff;
			emitter = ce;
			angleOffset = a;
		}
		
		void reposition(float dir, float midX, float midY) {
			float sin, cos, rad;
			rad = (float) Math.toRadians(dir);
			sin = (float) Math.sin(rad);
			cos = (float) Math.cos(rad);
		    xPos = (xOffset * cos) - (yOffset * sin) + midX; // should = xOffset @ 0 & 180; = -y @ 90; = y @ 270
		    yPos = (xOffset * sin) + (yOffset * cos) + midY; // should = yOffset @ 0; = x @ 90 & 270; = -y @ 180
			((ConfigurableEmitter.SimpleValue)(emitter.angularOffset)).setValue(dir + angleOffset);
			emitter.setPosition(xPos, yPos);
		}
	}
	
}
