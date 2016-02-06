import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 
 * @author Dominik Hannes
 *initiziert das baumobjekt
 *und zeichnet ihn an einen bestimmte position
 */
public class Baum extends GrafischesObjekt{
	/**
	 * ini das baum objekt
	 * @param posY
	 * @param posX
	 */
	public Baum(int posY, int posX) {
		this.posX = posX;
		this.posY = posY;
		try {
			img = ImageIO.read(new File("baum.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(0);
		}
		System.out.println("Baum erzeugt");
	}
	
	/**
	 * implementiert die Zeichenroutine des Baums
	 */
	public void zeichne(Graphics g) {
		// Versetzt zeichnen, wegen Höhenausgleich Baum - Auto
		g.drawImage(img, this.posX, this.posY-40, null);
	}
}
