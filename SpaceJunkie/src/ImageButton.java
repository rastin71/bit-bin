import org.newdawn.slick.Image;

//JRA: Original Code Begin
public class ImageButton {

	Image active = null;
	Image inactive = null;
	boolean enabled = false;
	boolean clicked = false;
	int xpos = 0;
	int ypos = 0;

	public ImageButton(Image a, Image i, int x, int y) {
		active = a; inactive = i; xpos = x; ypos = y;
	}

	public boolean getClicked() {
		boolean retVal = clicked;
		clicked = false;
		return retVal;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public void draw() {
		getImage().draw(xpos, ypos);
	}

	public boolean checkBounds(int x, int y, boolean c) {
		// is the Mouse over the image? Toggles active/inactive image.
		Image img = getImage();
		enabled = (x >= xpos && x <= xpos + img.getWidth() && y >= ypos && y <= ypos + img.getHeight());
		clicked |= (enabled & c);
		return enabled;
	}

	private Image getImage() {
		if (enabled) return active;
		return inactive;
	}
}
//JRA: Original Code End
