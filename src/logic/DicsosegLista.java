package logic;

import java.io.*;
import java.util.*;

public class DicsosegLista {
    private static final String FILENAME = "dicsoseg_lista.txt";
    private List<Dicsoseg> lista;

    public DicsosegLista() {
        lista = new ArrayList<>();
        betoltAdatok();
    }

    // Felhasználói eredmények tárolása
    public void ujEredmeny(String felhasznalonev, int pontszam, String ido) {
        Dicsoseg ujEredmeny = new Dicsoseg(felhasznalonev, pontszam, ido);
        boolean frissitett = false;

        // Ha van már rekord a felhasználónévhez, frissítjük, ha jobb a pontszám
        for (Dicsoseg d : lista) {
            if (d.getFelhasznalonev().equals(felhasznalonev)) {
                if (d.getPontszam() < pontszam) {
                    d.setPontszam(pontszam);
                    d.setIdo(ido);
                }
                frissitett = true;
                break;
            }
        }

        // Ha nincs még rekord, akkor hozzáadjuk
        if (!frissitett) {
            lista.add(ujEredmeny);
        }

        // Rendezés pontszám szerint csökkenő sorrendben
        lista.sort(Comparator.comparingInt(Dicsoseg::getPontszam).reversed());

        // Adatok mentése fájlba
        mentAdatok();
    }

    // Adatok mentése fájlba
    private void mentAdatok() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Dicsoseg d : lista) {
                writer.write(d.getFelhasznalonev() + "," + d.getPontszam() + "," + d.getIdo() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adatok betöltése fájlból
    public void betoltAdatok() {
        lista.clear(); // Töröljük a korábbi adatokat
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String felhasznalonev = parts[0];
                int pontszam = Integer.parseInt(parts[1]);
                String ido = parts[2];
                lista.add(new Dicsoseg(felhasznalonev, pontszam, ido));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Dicsőséglista lekérése
    public List<Dicsoseg> getDicsosegLista() {
        return lista;
    }

    // Dicsőség osztály
    public static class Dicsoseg {
        private String felhasznalonev;
        private int pontszam;
        private String ido;

        public Dicsoseg(String felhasznalonev, int pontszam, String ido) {
            this.felhasznalonev = felhasznalonev;
            this.pontszam = pontszam;
            this.ido = ido;
        }

        public String getFelhasznalonev() {
            return felhasznalonev;
        }

        public int getPontszam() {
            return pontszam;
        }

        public String getIdo() {
            return ido;
        }

        public void setPontszam(int pontszam) {
            this.pontszam = pontszam;
        }

        public void setIdo(String ido) {
            this.ido = ido;
        }
    }
}


