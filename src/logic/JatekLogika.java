package logic;

import java.util.Scanner;

public class JatekLogika {
	
	public void mozgatBalra(JatekAllapot allapot) {
	    int[][] tabla = allapot.getTabla();
	    int[][] eredetiTabla = allapot.tablaMasolasa(tabla);
	    
	    for (int i = 0; i < 4; i++) {
	        int[] ujSor = new int[4];
	        boolean[] osszeolvadt = new boolean[4];
	        int index = 0;
	        int pontszam = allapot.getPontszam();

	        for (int j = 0; j < 4; j++) {
	            if (tabla[i][j] != 0) {
	                if (index > 0 && ujSor[index - 1] == tabla[i][j] && !osszeolvadt[index - 1]) {
	                    // Összeolvadás
	                    ujSor[index - 1] *= 2;
	                    pontszam += ujSor[index - 1];
	                    osszeolvadt[index - 1] = true;
	                } else {
	                    // Mozgatás
	                    ujSor[index++] = tabla[i][j];
	                }
	            }
	        }
	        // Új sor beállítása
	        tabla[i] = ujSor;
	        allapot.setPontszam(pontszam);
	    }
	    
	    if (tablaValtozott(eredetiTabla, tabla)) {
	    	allapot.ujCsempeGenerator();
	    }
	}

		
	
	public void mozgatJobbra(JatekAllapot allapot) {
	    int[][] tabla = allapot.getTabla();
	    int[][] eredetiTabla = allapot.tablaMasolasa(tabla);
	    
	    for (int i = 0; i < 4; i++) {
	        int[] ujSor = new int[4];
	        boolean[] osszeolvadt = new boolean[4]; // Nyomon követjük, hol történt már összeolvadás
	        int index = 3;
	        int pontszam = allapot.getPontszam();

	        for (int j = 3; j >= 0; j--) {
	            if (tabla[i][j] != 0) {
	                if (index < 3 && ujSor[index + 1] == tabla[i][j] && !osszeolvadt[index + 1]) {
	                    // Összeolvadás, ha a következő hely értéke azonos, és nem történt még ott összeolvadás
	                    ujSor[index + 1] *= 2;
	                    pontszam += ujSor[index + 1];
	                    osszeolvadt[index + 1] = true; // Jelöljük, hogy itt már történt összeolvadás
	                } else {
	                    // Mozgatás
	                    ujSor[index--] = tabla[i][j];
	                }
	            }
	        }
	        // Új sor beállítása
	        tabla[i] = ujSor;
	        allapot.setPontszam(pontszam);
	    }
	    if (tablaValtozott(eredetiTabla, tabla)) {
	    	allapot.ujCsempeGenerator();
	    }
	}

	
	public void mozgatFel(JatekAllapot allapot) {
	    int[][] tabla = allapot.getTabla();
	    int[][] eredetiTabla = allapot.tablaMasolasa(tabla);
	    
	    for (int j = 0; j < 4; j++) {
	        int[] ujOszlop = new int[4];
	        boolean[] osszeolvadt = new boolean[4];
	        int index = 0;
	        int pontszam = allapot.getPontszam();

	        for (int i = 0; i < 4; i++) {
	            if (tabla[i][j] != 0) {
	                if (index > 0 && ujOszlop[index - 1] == tabla[i][j] && !osszeolvadt[index - 1]) {
	                    // Összeolvadás
	                    ujOszlop[index - 1] *= 2;
	                    pontszam += ujOszlop[index - 1];
	                    osszeolvadt[index - 1] = true;
	                } else {
	                    // Mozgatás
	                    ujOszlop[index++] = tabla[i][j];
	                }
	            }
	        }
	        for (int i = 0; i < 4; i++) {
	            tabla[i][j] = ujOszlop[i];
	        }
	        allapot.setPontszam(pontszam);
	    }
	    if (tablaValtozott(eredetiTabla, tabla)) {
	    	allapot.ujCsempeGenerator();
	    }
	}

	
	public void mozgatLe(JatekAllapot allapot) {
	    int[][] tabla = allapot.getTabla();
	    int[][] eredetiTabla = allapot.tablaMasolasa(tabla);
	    
	    for (int j = 0; j < 4; j++) {
	        int[] ujOszlop = new int[4];
	        boolean[] osszeolvadt = new boolean[4];
	        int index = 3;
	        int pontszam = allapot.getPontszam();

	        for (int i = 3; i >= 0; i--) {
	            if (tabla[i][j] != 0) {
	                if (index < 3 && ujOszlop[index + 1] == tabla[i][j] && !osszeolvadt[index + 1]) {
	                    // Összeolvadás
	                    ujOszlop[index + 1] *= 2;
	                    pontszam += ujOszlop[index + 1];
	                    osszeolvadt[index + 1] = true;
	                } else {
	                    // Mozgatás
	                    ujOszlop[index--] = tabla[i][j];
	                }
	            }
	        }
	        for (int i = 0; i < 4; i++) {
	            tabla[i][j] = ujOszlop[i];
	        }
	        allapot.setPontszam(pontszam);
	    }
	    if (tablaValtozott(eredetiTabla, tabla)) {
	    	allapot.ujCsempeGenerator();
	    }
	}
	
	public boolean nyertE(JatekAllapot allapot) {
	    int[][] tabla = allapot.getTabla();
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            if (tabla[i][j] == 2048) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	private boolean tablaValtozott(int[][] regiTabla, int[][] ujTabla) {
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            if (regiTabla[i][j] != ujTabla[i][j]) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	public boolean vanMegLepes(JatekAllapot allapot) {
	    int[][] tabla = allapot.getTabla();
	    
	    // Ellenőrizd az üres mezőket
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            if (tabla[i][j] == 0) {
	                return true;
	            }
	        }
	    }
	    
	    // Ellenőrizd az összeolvadható mezőket (balra/jobbra/fel/le)
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            if (j < 3 && tabla[i][j] == tabla[i][j + 1]) return true; // Jobbra összeolvadás
	            if (i < 3 && tabla[i][j] == tabla[i + 1][j]) return true; // Lefelé összeolvadás
	        }
	    }
	    return false; // Nincs több érvényes mozdulat
	}

	
	private static void kiirPontszam(JatekAllapot allapot) {
	    System.out.println("Pontszám: " + allapot.getPontszam());
	}

}


