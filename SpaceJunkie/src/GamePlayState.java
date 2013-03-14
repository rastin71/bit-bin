import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//JRA: Original Code Begin
public class GamePlayState extends BasicGameState implements MouseListener {

	int stateID = 0;
	PlayerShip pShip = null;
	private ParticleSystem effectSystem; // stores all particle effects
	HashMap<String, ConfigurableEmitter> primeEmitters = null;
	ConfigurableEmitter stars = null;
	public GamePlayState(int state) {stateID = state;}
	public int getID() {return stateID;}
	
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {
		int i;
		container.getInput().enableKeyRepeat();
		ConfigurableEmitter tmp = null;
		primeEmitters = new HashMap<String, ConfigurableEmitter>();
		try {
			effectSystem = ParticleIO.loadConfiguredSystem("res/emitters.xml");
			for (i = 0; i < effectSystem.getEmitterCount(); i++) {
				tmp = (ConfigurableEmitter)effectSystem.getEmitter(i);
				tmp.setEnabled(false);
				primeEmitters.put(tmp.name, tmp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	pShip = new PlayerShip(effectSystem, primeEmitters);
    	stars = primeEmitters.get("Stars");
    	stars.setEnabled(true);
    	stars.setPosition(0.0f, 0.0f);
    }

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		int y = 50; 
		effectSystem.render();
		pShip.render();
		g.drawString("Ship Facing: " + pShip.shipFacing, 10, y += 20);
		g.drawString("Ship Speed: " + pShip.shipSpeed, 10, y += 20);
		g.drawString("Dir Vel: " + pShip.directionalVelocity, 10, y += 20);
		g.drawString("Gimble X: " + pShip.gimbleX, 10, y += 20);
		g.drawString("Gimble Y: " + pShip.gimbleY, 10, y += 20);
		g.drawString("Screen Width: " + pShip.screenWidth, 10, y += 20);
		g.drawString("Screen Height: " + pShip.screenHeight, 10, y += 20);
		g.drawString("Screen Top: " + pShip.screenTop, 10, y += 20);
		g.drawString("Screen Left: " + pShip.screenLeft, 10, y += 20);
		g.drawString("Ship Height: " + pShip.shipHeight, 10, y += 20);
		g.drawString("Ship Width: " + pShip.shipWidth, 10, y += 20);
		g.drawString("Adj Ship X: " + pShip.adjustedImageX, 10, y += 20);
		g.drawString("Adj Ship Y: " + pShip.adjustedImageY, 10, y += 20);
		g.drawString("Adj Mid X: " + pShip.adjustedMidpointX, 10, y += 20);
		g.drawString("Adj Mid Y: " + pShip.adjustedMidpointY, 10, y += 20);
		g.drawString("Emitter1 X: " + pShip.e1X, 10, y += 20);
		g.drawString("Emitter1 Y: " + pShip.e1Y, 10, y += 20);
		g.drawString("Emitter2 X: " + pShip.e2X, 10, y += 20);
		g.drawString("Emitter2 Y: " + pShip.e2Y, 10, y += 20);
		g.drawString("Ship Velocity X: " + pShip.velX, 10, y += 20);
		g.drawString("Ship Velocity Y: " + pShip.velY, 10, y += 20);
		g.drawString("Ship Location X: " + pShip.locX, 10, y += 20);
		g.drawString("Ship Location Y: " + pShip.locY, 10, y += 20);		g.drawString("Sin(" + pShip.shipFacing + "): " + Math.sin(Math.toRadians(pShip.shipFacing)), 10, y += 20);
		g.drawString("Cos(" + pShip.shipFacing + "): " + Math.cos(Math.toRadians(pShip.shipFacing)), 10, y += 20);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
		throws SlickException {
		pShip.update(gc.getInput(), delta);
		effectSystem.update(delta);
		((ConfigurableEmitter.SimpleValue)(stars.gravityFactor)).setValue(pShip.velY * 10);
		((ConfigurableEmitter.SimpleValue)(stars.windFactor)).setValue(-pShip.velX * 10);

	}

}
