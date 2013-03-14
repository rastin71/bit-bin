import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;

public class EmitterRecord {
	
	int statePosition = 0;
	int stateValue = 0;
	int groupPosition = 10000;
	int groupSub = 10000;
	ConfigurableEmitter ce;
	float step = 1;
	
	float Color1_Position;
	float Color2_Position;
	Color Color1_RGB;
	Color Color2_RGB;
	UnicodeFont ucFont = null;
	Image activeIndicator = null;
	Image activeGroup = null;
	
	public EmitterRecord(ConfigurableEmitter cEmitter) {
		ucFont = new UnicodeFont(new Font("resources/samplefont.ttf", Font.PLAIN, 16));
	    ucFont.addAsciiGlyphs();
	    ucFont.addGlyphs(400, 600);
	    ucFont.getEffects().add(new ColorEffect(java.awt.Color.orange));
	    ce = cEmitter;
		Image menuOptions;
		try {
			menuOptions = new Image("resources/menumap.png");
			activeIndicator = menuOptions.getSubImage(0, 58, 25, 25);
			activeGroup = menuOptions.getSubImage(25, 58, 25, 25);
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
	    try {
			ucFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void nextState() {
		if (statePosition < 18) statePosition++;
		else statePosition = 0;
	}
	
	public void backState() {
		if (statePosition > 0) statePosition--;
		else statePosition = 18;
	}
	
	public void nextGroup() {
		groupPosition++;
	}
	
	public void backGroup() {
		groupPosition--;
	}
	
	public void backValue() {
		changeValue(-step);
	}
	
	public void nextValue() {
		changeValue(step);
	}
	
	public void stepUp() {
		step *= 2;
	}
	
	public void stepDown() {
		step /= 2;
	}
	
	public void groupSubUp() {
		groupSub--;
	}
	
	public void groupSubDown() {
		groupSub++;
	}
	
	public void changeGroup() {
		ConfigurableEmitter.ColorRecord tmp = (ConfigurableEmitter.ColorRecord)(ce.colors.get(groupSub % ce.colors.size()));
		tmp.col.r = (float)Math.random();
		tmp.col.g = (float)Math.random();
		tmp.col.b = (float)Math.random();
	}
	
	public void changeValue(float val) {
		
		switch(statePosition) {
			case 0:
				updateRangeValue(ce.spawnInterval, val);
				break;
			case 1:
				updateRangeValue(ce.spawnCount, val);
				break;
			case 2:
				updateRangeValue(ce.initialLife, val);		
				break;
			case 3:
				updateRangeValue(ce.initialSize, val);
				break;
			case 4:
				updateRangeValue(ce.xOffset, val);
				break;
			case 5:
				updateRangeValue(ce.yOffset, val);
				break;
			case 6:
				updateRangeValue(ce.initialDistance, val);
				break;
			case 7:
				updateRangeValue(ce.speed, val);
				break;
			case 8:
				updateRangeValue(ce.length, val);
				break;
			case 9:
				updateRangeValue(ce.emitCount, val);
				break;
			case 10:
				ce.spread.setValue(ce.spread.getValue() + val);
				break;
			case 11:
				((ConfigurableEmitter.SimpleValue)(ce.angularOffset)).setValue(ce.angularOffset.getValue(0) + val);
				break;
			case 12:
				((ConfigurableEmitter.SimpleValue)(ce.growthFactor)).setValue(ce.growthFactor.getValue(0) + val);
				break;
			case 13:
				((ConfigurableEmitter.SimpleValue)(ce.gravityFactor)).setValue(ce.gravityFactor.getValue(0) + val);
				break;
			case 14:
				((ConfigurableEmitter.SimpleValue)(ce.windFactor)).setValue(ce.windFactor.getValue(0) + val);
				break;
			case 15:
				((ConfigurableEmitter.SimpleValue)(ce.startAlpha)).setValue(ce.startAlpha.getValue(0) + val);
				break;
			case 16:
				((ConfigurableEmitter.SimpleValue)(ce.endAlpha)).setValue(ce.endAlpha.getValue(0) + val);
				break;
			case 17:
				if (stateValue % 2 == 0) {
					ce.setPosition(ce.getX() + val, ce.getY());
				} else {
					ce.setPosition(ce.getX(), ce.getY() + val);			
				}
				break;
			case 18:
				if (stateValue % 2 == 0) {
					ce.useAdditive = !ce.useAdditive;
				} else {
					ce.useOriented = !ce.useOriented;	
				}
				break;	
		}	

	}
	
	private void updateRangeValue(ConfigurableEmitter.Range r, float val) {
		switch(stateValue % 3) {
		case 0:
			r.setEnabled(!r.isEnabled());
			break;
		case 1:
			r.setMin(r.getMin() + val);
			break;
		case 2:
			r.setMax(r.getMax() + val);
			break;
		}
	}
	
	public void nextOption() {
		stateValue++;
	}
	
	public void render() {
		int i;
		
		ucFont.drawString(365, 40, Float.toString(step));
		switch(statePosition) {
		case 0:
			ucFont.drawString(100, 10, "Spawn Interval");
			ucFont.drawString(150, 32, "Enabled: " + ce.spawnInterval.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.spawnInterval.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.spawnInterval.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 1:
			ucFont.drawString(100, 10, "Spawn Count");
			ucFont.drawString(150, 32, "Enabled: " + ce.spawnCount.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.spawnCount.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.spawnCount.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 2:
			ucFont.drawString(100, 10, "Initial Life");
			ucFont.drawString(150, 32, "Enabled: " + ce.initialLife.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.initialLife.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.initialLife.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 3:
			ucFont.drawString(100, 10, "Initial Size");
			ucFont.drawString(150, 32, "Enabled: " + ce.initialSize.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.initialSize.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.initialSize.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 4:
			ucFont.drawString(100, 10, "X Offset");
			ucFont.drawString(150, 32, "Enabled: " + ce.xOffset.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.xOffset.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.xOffset.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 5:
			ucFont.drawString(100, 10, "Y Offset");
			ucFont.drawString(150, 32, "Enabled: " + ce.yOffset.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.yOffset.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.yOffset.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 6:
			ucFont.drawString(100, 10, "Initial Distance");
			ucFont.drawString(150, 32, "Enabled: " + ce.initialDistance.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.initialDistance.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.initialDistance.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 7:
			ucFont.drawString(100, 10, "Speed");
			ucFont.drawString(150, 32, "Enabled: " + ce.speed.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.speed.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.speed.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 8:
			ucFont.drawString(100, 10, "Length");
			ucFont.drawString(150, 32, "Enabled: " + ce.length.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.length.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.length.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 9:
			ucFont.drawString(100, 10, "Emit Count");
			ucFont.drawString(150, 32, "Enabled: " + ce.emitCount.isEnabled());
			ucFont.drawString(150, 52, "Minimum: " + ce.emitCount.getMin());
			ucFont.drawString(150, 73, "Maximum: " + ce.emitCount.getMax());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 3));
			break;
		case 10:
			ucFont.drawString(100, 10, "Spread");
			ucFont.drawString(150, 32, "Value: " + ce.spread.getValue());
			activeIndicator.draw(120, 30);
			break;
		case 11:
			ucFont.drawString(100, 10, "Angular Offset");
			ucFont.drawString(150, 32, "Value: " + ce.angularOffset.getValue(0));
			activeIndicator.draw(120, 30);
			break;
		case 12:
			ucFont.drawString(100, 10, "Growth Factor");
			ucFont.drawString(150, 32, "Value: " + ce.growthFactor.getValue(0));
			activeIndicator.draw(120, 30);
			break;
		case 13:
			ucFont.drawString(100, 10, "Gravity Factor");
			ucFont.drawString(150, 32, "Value: " + ce.gravityFactor.getValue(0));
			activeIndicator.draw(120, 30);
			break;
		case 14:
			ucFont.drawString(100, 10, "Wind Factor");
			ucFont.drawString(150, 32, "Value: " + ce.windFactor.getValue(0));
			activeIndicator.draw(120, 30);
			break;
		case 15:
			ucFont.drawString(100, 10, "Start Alpha");
			ucFont.drawString(150, 32, "Value: " + ce.startAlpha.getValue(0));
			activeIndicator.draw(120, 30);
			break;
		case 16:
			ucFont.drawString(100, 10, "End Alpha");
			ucFont.drawString(150, 32, "Value: " + ce.endAlpha.getValue(0));
			activeIndicator.draw(120, 30);
			break;
		case 17:
			ucFont.drawString(100, 10, "Position");
			ucFont.drawString(150, 32, "X: " + ce.getX());
			ucFont.drawString(150, 53, "Y: " + ce.getY());
			activeIndicator.draw(120, 30 + 20 * (stateValue % 2));
			break;
		case 18:
			ucFont.drawString(100, 10, "Settings");
			ucFont.drawString(150, 32,"Use Additive: " + ce.useAdditive);
			ucFont.drawString(150, 53,"Use Oriented: " + ce.useOriented);
			activeIndicator.draw(120, 30 + 20 * (stateValue % 2));
			break;
		}	
		
		switch(groupPosition % 2) {
		case 0:
			ucFont.drawString(540, 10, "Color");
			ConfigurableEmitter.ColorRecord tmp;
			for (i = 0; i < ce.colors.size(); i++) {
				tmp = (ConfigurableEmitter.ColorRecord)(ce.colors.get(i));
				ucFont.drawString(560, 30 + 20 * i, "Position: " + tmp.pos);
				ucFont.drawString(680, 30 + 20 * i, "Red: " + tmp.col.r);
				ucFont.drawString(850, 30 + 20 * i, "Green: " + tmp.col.g);
				ucFont.drawString(1040, 30 + 20 * i, "Blue: " + tmp.col.b);
			}
			activeGroup.draw(530, 30 + 20 * (groupSub % ce.colors.size()));
			break;
		case 1:
			ucFont.drawString(540, 10, "Velocity");

			Vector2f tmpV;
			ucFont.drawString(560, 30, "Min: " + ce.velocity.getMin() + " Max: " + ce.velocity.getMax());
			for (i = 0; i < ce.velocity.getCurve().size(); i++) {
				tmpV = (Vector2f)(ce.velocity.getCurve().get(i));
				ucFont.drawString(560, 50 + 20 * i, "X: " + tmpV.x + " Y: " + tmpV.y);
			}
			activeGroup.draw(530, 30 + 20 * (groupSub % (ce.velocity.getCurve().size() + 1)));
		}
	}
}
