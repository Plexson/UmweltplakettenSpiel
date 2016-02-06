import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
/**
 * 
 * @author Dominik Hannes
 *Initalisiert die Grafik ausgabe
 */
public class Zeichenflaeche extends JPanel {
	private static final long serialVersionUID = 1L;

	private List<GrafischesObjekt> elemente = new ArrayList<GrafischesObjekt>();
	private Graphics bufferImageGraphics;
	private Image bufferImage;
	private int breite;
	private int hoehe;
	private int richtigeAutos, verpassteAutos, leben=1, level;
	
/**
 * initalisiert die größe des Spielfelds
 * @param breite
 * @param hoehe
 */
	public Zeichenflaeche(int breite, int hoehe) {
		this.breite = breite;
		this.hoehe = hoehe;
	}

	/**
	 * fügt ein grafisches objekt der zuzeichnen liste hinzu
	 * und sortiert  diese beim hinzufügen eines Elements
	 * @param element
	 */
	public void add(GrafischesObjekt element) {
		this.elemente.add(element);
		// Sortieren der Grafischen Elemente
		Collections.sort(elemente);			
	}

	/**
	 * löscht ein bestimmtes element aus der list
	 * @param element
	 */
	public void remove(GrafischesObjekt element) {
		this.elemente.remove(element);
	}
	
	/**
	 * löscht sämtlichen Inhalt aus der liste
	 */
	public void clear() {
		this.elemente.clear();
	}

	
	/**
	 * übergibt den aktuellen Spielestatuts
	 * für die Statusleiste
	 * @param richtigeAutos
	 * @param verpassteAutos
	 * @param leben
	 * @param level
	 */
	public void setSpielstatus(int richtigeAutos, int verpassteAutos, int leben, int level) {
		this.richtigeAutos = richtigeAutos;
		this.verpassteAutos = verpassteAutos;
		this.leben = leben;
		this.level = level;
	}
	
	/**
	 * leert die grafikfläche
	 * zeichnet alle grafikelemente
	 * zeichet die Statusleiste
	 * Zeigt Gameover, falls leben =0
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (bufferImage == null) {
			bufferImage = this.createImage(this.breite, this.hoehe);
			bufferImageGraphics = bufferImage.getGraphics();
		}
		bufferImageGraphics.clearRect(0, 0, breite, hoehe);

		// zeichnet alle grafischen Ekemente
		for (GrafischesObjekt element : elemente) {
			element.zeichne(bufferImageGraphics);
		}
		g.drawImage(bufferImage, 0, 0, this);
		
		//Zeichnet Statusleiste
		g.setFont(new Font("Dialog", Font.BOLD, 18));
		g.setColor(Color.black);
		g.drawString("Richtige Autos: " + this.richtigeAutos + " von " + level*10 +
				" --- Verpasste Autos: " + this.verpassteAutos +
				" --- Level: "	+ this.level +
				" --- Leben: " + this.leben
				, 0,20);
		
		if( leben <= 0 ) {
			// Zeichne Game Over
			g.setFont(new Font("Dialog", Font.BOLD, 56));
			g.setColor(Color.red);
			g.drawString("GAME OVER", 300, 300);
		}
		
		
		
	}

	public void zeichne(Graphics g) {
		paint(g);
	}
}