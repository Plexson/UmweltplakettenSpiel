import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import sun.audio.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

/**Hauptklasse
 * Ablaufsteuerung
 * Timer
 * Auto- und Baummanagement
 * 
 * @author Dominik, Hannes
 *
 */

// Beachte: AutoSpiel erbt von JFrame
class UmweltplakettenSpiel extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int FENSTER_BREITE = 1024;
	private static final int FENSTER_HOEHE = 786;
	private static final int FRAMERATE = 25;
	
	private Zeichenflaeche zeichenflaeche;
	private Timer timer = new Timer();
	private List<Auto> elemente = Collections.synchronizedList(new ArrayList<Auto>());
	
	private int verpassteAutos = 0;
	private int richtigeAutos = 0;
	private int leben = 5;
	private int level = 0;
	private int randomGeneratorNumber = 0;
	private int init = 1; // Initialzustand, Spiel l‰uft noch nicht
	
	/**
	 * Konstruktor des Spiels
	 * initalisiert Startbutton
	 * 				Grafikoberfl‰che
	 * 				Musik
	 */
	public UmweltplakettenSpiel() {
		// Zeichenfl√§che und Oberfl√§che initialisieren, bezieht sich auf die Funktionalit√§t von JFrame
		super("Umweltplaketten-Spiel");
		setBounds(50, 50, FENSTER_BREITE, FENSTER_HOEHE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();
		
		// Zeichenfl√§che anlegen und dem Fenster hinzuf√ºgen
		zeichenflaeche = new Zeichenflaeche(FENSTER_BREITE, FENSTER_HOEHE);
		container.add(zeichenflaeche);
		
		// Einen Neues-Auto-Button anlegen und dem Fenster anf√ºgen.
		// (F√ºr das sp√§tere Spiel nicht ben√∂tigt, da Autos nach dem
		// Zufallsprinzip erzeugt werden sollten)
		JButton buttonNeuesSpiel = new JButton("Neues Spiel");
		getContentPane().add(buttonNeuesSpiel, BorderLayout.SOUTH);
		buttonNeuesSpiel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				init = 0; // Spiel l‰uft los bzw. l‰uft weiter
				generiereLevel(1);
			}
		});
		
		// An den Container wird ein MouseListener angehangen.
		// Dieser registriert Mauseingaben und gibt die Informationen weiter
		container.addMouseListener(new MouseAdapter() {
			@Override
			/**
			 * Fragt Mausevent ab
			 */
			public void mousePressed(MouseEvent e) {
				KlickBeiKoord(e.getX(), e.getY());
			}
		});
		
		// darstellen
		setVisible(true);
		
		music();
		
		// Ein timer gesteuerter Task zur Spielberechnung
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			/**
			 * randomGenerator zur Bestimmung des Spawn Zeitpunkts der Autos
			 * ruft Levelerzeugungsroutine, sofern die benˆtigte Punkte Zahl erreicht wurde
			 * ruft loescheAutos und aktualisiereszene
			 * ruft gegebenenfalls neues Auto auf
			 * ‹bergib Zeichenfl‰che die aktuellen Daten des Spiels
			 */
			public void run() {
				if( init == 0) { // Nur, wenn wir nicht im Initialzustand sind
					if( randomGeneratorNumber == 0 ) randomGeneratorNumber = 100 - (int)(Math.random()*19*level);
					randomGeneratorNumber--;
					
					if( richtigeAutos == level*10 ) {
						if( level < 5 ) level++;  // maximal Level 5
						generiereLevel(level);
					}
					if( leben > 0 && level > 0 ) {
						loescheAutosRechts();
						aktualisiereSzene();
						if(randomGeneratorNumber == 1) neuesAuto();
						
					}
					// ‹bergib Zeichenfl‰che die aktuellen Daten des Spiels
					zeichenflaeche.setSpielstatus(richtigeAutos, verpassteAutos, leben, level);
					zeichenflaeche.repaint();
				}
			}
		}, (1000 / FRAMERATE), (1000 / FRAMERATE));
	}

	/**
	 *  Hauptdialog anlegen
	 * @param arg
	 */
	public static void main(String arg[]) {
		new UmweltplakettenSpiel();
	}

	/**
	 * Copy&Paste aus dem Internet, zur Besseren Stimmung ;)
	*/
	
	 public static void music() 
	    {       
	        AudioPlayer MGP = AudioPlayer.player;
	        AudioStream BGM;
	        AudioData MD;

	        ContinuousAudioDataStream loop = null;

	        try
	        {
	            InputStream test = new FileInputStream("music.wav");
	            BGM = new AudioStream(test);
	            AudioPlayer.player.start(BGM);
	            //MD = BGM.getData();
	            //loop = new ContinuousAudioDataStream(MD);

	        }
	        catch(FileNotFoundException e){
	            System.out.print(e.toString());
	        }
	        catch(IOException error)
	        {
	            System.out.print(error.toString());
	        }
	        MGP.start(loop);
	    }
	 
	 /**
	  * setzrt die elemente und zeichenliste zur¸ck
	  * erzeugt baeume  abh‰ngig vom level
	  * @param level
	  */
	public void generiereLevel(int level) {
		this.level = level;
		richtigeAutos=0;
		verpassteAutos=0;
		if( level == 1 ) leben=10; // Setze Leben nur bei Eintritt in Level 1 zur¸ck
		randomGeneratorNumber=100; // Hˆhere Wartezeit zu Levelbeginn
		synchronized(elemente) {   // Lˆsche die Elementelisten
			elemente.clear();
			zeichenflaeche.clear();
		}
		erzeugeBaeume(level*3);
		
		
	}
	
	/**
	 *  Methode zum Erzeugen eines neuen Autos (mit Zufallswerten)
	 */
	private void neuesAuto() {
		String name = "IL-SSE " + String.format("%03d", elemente.size() + 1);
		int posY = (int) (Math.random() * (FENSTER_HOEHE - 200));
		int geschwindigkeit = (int) (30+level*5 + Math.random() * 30+level*10); // 30+level*5..100+level*5 km/h -> Levelabh. Geschwi.
		Auto neuesAuto = new Auto(name, posY, geschwindigkeit);
		synchronized (elemente) {
			elemente.add(neuesAuto);
			zeichenflaeche.add(neuesAuto);
		}
	}
	
	/**
	 * 
	 * L√∂sche Autos, die rechts den Bildschirmrand verlassen haben
	 * z‰hlt wieviele autos  mit plakete richtig sind und welche falsch sind
	 */
	private void loescheAutosRechts() {
		synchronized (elemente) {
			Iterator<Auto> it = elemente.iterator();
			while (it.hasNext()) {
				Auto auto = it.next();
				if (auto.getPosX() > FENSTER_BREITE+200) // +200 f¸r Auspuffgase
					{
					// Entferne Auto
					it.remove();
					elemente.remove(auto);
					zeichenflaeche.remove(auto);
					
					// Abfrage der Plakette <-> Abgas
					if( auto.hatKorrektePlakette() ) {
						richtigeAutos++;
					}
					else {
						verpassteAutos++;
						leben --;
					}
					
					
					System.out.println("Auto gel√∂scht " + auto.getName());
				}
			}
		}
	}
	
	/**
	 * erzeugt eine gewisse anzahl von b‰umen an zuf‰lligen positionen
	 * @param anzahl
	 */
	
	private void erzeugeBaeume(int anzahl) {
		// Erzeuge Anzahl B‰ume
		int posX;
		int posY;
		for( int i = 0; i < anzahl; i++) {
			// Zufallszahlen f¸r Position X,Y
			posX = (int) (Math.random() * (FENSTER_BREITE-160));
			
			// posY zwischen 40 und Fensterhˆhe -200 -> selbe Anordnung wie die B‰ume
			// versetztes Zeichnen erfolgz in Baum
			posY = (int) (Math.random() * (FENSTER_HOEHE-200))+40;
			Baum neuerBaum = new Baum(posY, posX);
			// Anh‰ngen an die Zeichenfl‰che
			zeichenflaeche.add(neuerBaum);
		}

	}
	
	/**
	 * 
	 * Aktualisiere die Position aller Autos
	*/
	private void aktualisiereSzene() {
		synchronized (elemente) {
			for (Auto auto : elemente) {
				auto.updatePosition(FRAMERATE);
			}
		}
	}
	
	/**
	 * 
	 * Reaktion auf Mausklick
	 * pr¸ft f¸r alle autos, ob das Fenster um die Plakette
	 * angeklickt wurde
	 * falls ja, dann ‰ndere die Plakette
	 */
	private void KlickBeiKoord(int klickX, int klickY) {
		System.out.println("Mausklick bei: (" + klickX + "," + klickY + ")");
		
		// alle Autos durchgehen
		for (Auto auto : elemente) {
			if( auto.getPosX()+65 <= klickX && klickX <= auto.getPosX()+122 
				&& auto.getPosY()+65 <= klickY && klickY <= auto.getPosY()+104 ) {
				
				auto.aenderePlakette();
				
				System.out.println(auto.getName() + " ge‰ndert");
			}
		}
		
	}
}
