import java.awt.Graphics;
import java.awt.Image;
/**
 * 
 * @author Dominik Hannes
 *abstrakte Klasse bildet Interface für alle grafischenObjekte(Baum, Auto)
 *bietet gemeinsame methoden getpos, getname
 */
public abstract class GrafischesObjekt implements Comparable<GrafischesObjekt> {
	// Abstrakte Klasse zur Verwaltung aller Grafischen Elemente
	protected int posX;
	protected int posY;
	protected String name;
	protected Image img = null;
	
	public int getPosX() {
		return this.posX;
	}
	
	public int getPosY() {
		return this.posY;
	}
	
	public String getName() {
		return this.name;
	}
	
	// Zeichne Funktion, die von Bäumen / Autos implementiert wird.
	public abstract void zeichne(Graphics g);
	
	// Vergleichen der Grafischen Elemente durch die PosY
	@Override
    public int compareTo(GrafischesObjekt object) {
		return (this.posY) - object.getPosY();
    }
}
