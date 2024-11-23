package logic;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import gui.JatekGUI;

public class JatekMentese {

    // Játék mentése szövegfájlba
	public static void mentes(String fajlNev, int[][] tabla, int pontszam, Stopper ido, String felhasznalonev) {
	    try (FileWriter writer = new FileWriter(fajlNev)) {
	        // Tábla sorainak írása
	        writer.write("tabla:\n");
	        for (int i = 0; i < tabla.length; i++) {
	            writer.write("[");
	            for (int j = 0; j < tabla[i].length; j++) {
	                writer.write(tabla[i][j] + "");
	                if (j < tabla[i].length - 1) writer.write(", ");
	            }
	            writer.write("]\n");
	        }

	        // Pontszám, idő és felhasználónév írása
	        writer.write("pontszam: " + pontszam + "\n");
	        writer.write("ido: " + ido.getElteltIdo() + "\n");
	        writer.write("felhasznaloNev: " + felhasznalonev + "\n");

	    } catch (IOException e) {
	        throw new RuntimeException("Hiba történt a mentés során: " + e.getMessage(), e);
	    }
	}

	public static JatekAllapot betoltes(String fajlNev) throws IOException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(fajlNev))) {
	        String line;
	        int[][] tabla = new int[4][4];
	        int pontszam = 0;
	        long ido = 0;
	        String felhasznalonev = "";  // Felhasználónév változó
	        int row = 0;

	        while ((line = reader.readLine()) != null) {
	            line = line.trim();
	            if (line.startsWith("tabla:")) {
	                continue;
	            } else if (line.startsWith("pontszam:")) {
	                pontszam = Integer.parseInt(line.split(":")[1].trim());
	            } else if (line.startsWith("ido:")) {
	                String elapsedTime = line.substring(4).trim(); // "ido:" utáni rész kivágása
	                ido = convertToMillis(elapsedTime); // Konvertálás milliszekundumra
	            } else if (line.startsWith("felhasznaloNev:")) {
	                felhasznalonev = line.split(":")[1].trim(); // Felhasználónév kiolvasása
	            } else if (line.startsWith("[")) {
	                String[] elements = line.substring(1, line.length() - 1).split(", ");
	                for (int col = 0; col < elements.length; col++) {
	                    tabla[row][col] = Integer.parseInt(elements[col]);
	                }
	                row++;
	            }
	        }

	        JatekAllapot allapot = new JatekAllapot();
	        allapot.setTabla(tabla);
	        allapot.setPontszam(pontszam);
	        allapot.setFelhasznaloNev(felhasznalonev);  // Felhasználónév beállítása

	        Stopper stopper = new Stopper();
	        stopper.setKezdoIdo(ido);  // Állítsd be az időt
	        allapot.setStopper(stopper);

	        return allapot;
	    }
	}
    
    private static long convertToMillis(String formattedTime) {
        try {
            String[] parts = formattedTime.split(":");
            int minutes = Integer.parseInt(parts[0]);

            int seconds = 0;
            int milliseconds = 0;

            if (parts.length > 1) {
                String[] secMillis = parts[1].split("\\.");
                seconds = Integer.parseInt(secMillis[0]);

                if (secMillis.length > 1) {
                    milliseconds = Integer.parseInt(secMillis[1]);
                }
            }

            return (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
        } catch (Exception e) {
            System.err.println("Hiba az idő konvertálásakor: " + e.getMessage());
            return 0;
        }
    }



}
    