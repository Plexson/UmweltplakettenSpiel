import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 
 * @author Dominik Hannes 
 *Initalisiert das Auto
 *ändert und kontroliert die abgasplaketten
 *zeichnet das Auto
 *updatet position
 */
public class Auto extends GrafischesObjekt{
	private Color farbe;
	private int geschwindigkeit;
	private Euronorm plakette;
	private Image euroBild2,euroBild3,euroBild4, abgasBild2,abgasBild3,abgasBild4;
	private Euronorm abgase;
	
	public Auto(String name, int posY, int geschw) {
		this.name = name;
		this.posX = -200;
		this.posY = posY;
		this.geschwindigkeit = geschw;
		this.farbe = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0), (int) (Math.random() * 255.0));
		
		// Festlegen der initialen Plakette
		int temp = (int) (Math.random()*3);
		switch(temp) {
		case 0:
			this.plakette = Euronorm.Euro2;
			break;
		case 1:
			this.plakette = Euronorm.Euro3;
			break;
		case 2:
			this.plakette = Euronorm.Euro4;
			break;
		}
		
		// Festlegen der Abgase
		temp = (int) (Math.random()*3);
		switch(temp) {
		case 0:
			this.abgase = Euronorm.Euro2;
			break;
		case 1:
			this.abgase = Euronorm.Euro3;
			break;
		case 2:
			this.abgase = Euronorm.Euro4;
			break;
		}
		
		
		
		try {
			img = ImageIO.read(new File("auto.png"));
			euroBild2 = ImageIO.read(new File("plakette2.png"));
			euroBild3 = ImageIO.read(new File("plakette3.png"));
			euroBild4 = ImageIO.read(new File("plakette4.png"));
			
			abgasBild2 = ImageIO.read(new File("abgas4.png"));
			abgasBild3 = ImageIO.read(new File("abgas3.png"));
			abgasBild4 = ImageIO.read(new File("abgas2.png"));
			
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(0);
		}
		System.out.println("Auto erzeugt " + this.name);
		
		
	}
	
	public void aenderePlakette() {
		if( this.plakette == Euronorm.Euro2 ) this.plakette = Euronorm.Euro3;
		else if( this.plakette == Euronorm.Euro3 ) this.plakette = Euronorm.Euro4;
		else if( this.plakette == Euronorm.Euro4 ) this.plakette = Euronorm.Euro2;
	}

	public boolean hatKorrektePlakette() {
		return this.plakette == this.abgase;
	}
	
	public int getGeschwindigkeit() {
		return this.geschwindigkeit;
	};
	
	public void updatePosition(int framerate) {
		// Umrechnung von Geschwindigkeit in Bildschirm-Bewegung: 130km/h --> 400px/s
		int posXAenderung = (int) (1.0 / framerate * (400.0 * this.geschwindigkeit / 130.0));
		this.posX = this.posX + posXAenderung;
	//	System.out.println("Auto " + this.name + " bei (" + this.posX + "," + this.posY + ")");
	}
	
	public void zeichne(Graphics g) {
		g.drawImage(img, this.posX, this.posY, null);
		//g.setColor(this.farbe);
		//g.fillOval(this.posX + 77, this.posY + 62, 40, 40);
		
		// Plakette Zeichnen
		switch(this.plakette) {
		case Euro2:
			g.drawImage(euroBild2, this.posX + 77, this.posY + 62, null);
			break;
		case Euro3:
			g.drawImage(euroBild3, this.posX + 77, this.posY + 62, null);
			break;
		case Euro4:
			g.drawImage(euroBild4, this.posX + 77, this.posY + 62, null);
			break;		
		}
		
		// Abgase zeichnen
		switch(this.abgase) {
		case Euro2:
			g.drawImage(abgasBild2, this.posX - 177, this.posY + 60, null);
			break;
		case Euro3:
			g.drawImage(abgasBild3, this.posX - 105, this.posY + 75, null);
			break;
		case Euro4:
			g.drawImage(abgasBild4, this.posX - 50, this.posY + 80, null);
			break;		
		}
		
		
		g.setFont(new Font("Dialog", Font.BOLD, 18));
		g.setColor(Color.black);
		g.drawString(this.name, this.posX + 50, this.posY + 145);
	}
}
