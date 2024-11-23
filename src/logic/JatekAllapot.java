package logic;

import java.util.Random;

public class JatekAllapot {
	private static JatekAllapot instance;
	
	private int[][] tabla;
	private int pontszam;
	private Stopper stopper;
	private String felhasznaloNev;
	
	private int[][] elozotabla;
	private int elozopontszam;
	
	public JatekAllapot() {
		this.tabla = new int[4][4];
		this.pontszam = 0;
		
		this.elozotabla = new int[4][4];
		this.elozopontszam = 0;
		
		this.stopper = new Stopper(); 
		
		ujCsempeGenerator();
		ujCsempeGenerator(); //kezdéskor 2új csempe
	}
	
    public Stopper getStopper() {
        return stopper;
    }

    public void setStopper(Stopper stopper) {
        this.stopper = stopper;
    }
	
	public int[][] getTabla(){
		return tabla;
	}
	
	public void setTabla(int[][] tabla) {
        if (tabla.length == 4 && tabla[0].length == 4) { // Ellenőrzés, hogy 4x4-es legyen
            this.tabla = tabla;
        } else {
            throw new IllegalArgumentException("A tábla méretének 4x4-nek kell lennie.");
        }
    }
	
	public int getPontszam() {
		return pontszam;
	}
	
	public void setPontszam(int pontszam) {
        if (pontszam >= 0) { // Negatív pontszám nem lehetséges
            this.pontszam = pontszam;
        } else {
            throw new IllegalArgumentException("A pontszám nem lehet negatív.");
        }
    }
	
	public void elozoAllapot() {
		this.elozotabla = tablaMasolasa(tabla); // Tábla másolása
		this.elozopontszam = pontszam;         // Pontszám mentése
	}
	
    public void visszalep() {
        if (elozotabla != null) {
            this.tabla = tablaMasolasa(elozotabla); // Tábla visszaállítása
            this.pontszam = elozopontszam;         // Pontszám visszaállítása
        }
    }
    
    // Másoló konstruktor
    public JatekAllapot(JatekAllapot masik) {
        this.tabla = tablaMasolasa(masik.tabla);
        this.pontszam = masik.pontszam;

        this.elozotabla = tablaMasolasa(masik.elozotabla);
        this.elozopontszam = masik.elozopontszam;
    }
	
	public void ujCsempeGenerator() {
		Random rand = new Random();
		
		//90%-ban 2-es jön, különben 4
		int ertek = rand.nextInt(10);
		if(ertek < 2) {
			ertek = 4;
		}
		else {
			ertek = 2;
		}
		
		while(true) {
			int sor = rand.nextInt(4);
			int oszlop = rand.nextInt(4);
			
			//keresünk egy üres mezőt, oda rakjuk az új csempét
			if (tabla[sor][oszlop] == 0) {
				tabla[sor][oszlop] = ertek;
				break;
			}
		}
	}
	
	public void frissitTabla(int[][] ujTabla) {
		this.tabla = ujTabla;
	}
	
	public int[][] tablaMasolasa(int[][] tabla){
	    int[][] ujTabla = new int[4][4];
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            ujTabla[i][j] = tabla[i][j];
	        }
	    }
	    return ujTabla;
	}
	
	public void resetAllapot() {
	    this.tabla = new int[4][4];
	    this.pontszam = 0;

	    this.elozotabla = new int[4][4];
	    this.elozopontszam = 0;

	    if (this.stopper != null) {
	        this.stopper.stop(); // Megállítjuk az előző stoppert, ha fut
	    }
	    this.stopper = new Stopper(); // Új stopper példány

	    // Új játék kezdésekor generálunk 2 csempét
	    ujCsempeGenerator();
	    ujCsempeGenerator();
	}

	public String getFelhasznaloNev() {
        return felhasznaloNev;
    }

	public void setFelhasznaloNev(String felhasznalonev) {
	   this.felhasznaloNev = felhasznalonev; // Felhasználónév beállítása
	}
    
    public static JatekAllapot getInstance() {
        if (instance == null) {
            instance = new JatekAllapot();
        }
        return instance;
    }
	
	
}
