package gui;

import logic.JatekLogika;
import logic.Stopper;
import logic.DicsosegLista;
import logic.JatekAllapot;
import logic.JatekMentese;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Stack;

public class JatekGUI extends JPanel {

    private JatekLogika logika;
    protected JatekAllapot allapot;
    private Stopper stopper;
    private DicsosegLista dicsoseglista = new DicsosegLista();

    private JButton[][] gombok;
    private Runnable visszaAMenuhez;

    private Stack<JatekAllapot> elozoAllapotok;
    private JButton visszalepesGomb;
    private JLabel pontszamLabel;
    private JLabel idoLabel;
    private JLabel felhasznalonevLabel;

    public String felhasznalonev;

    public JatekGUI(Runnable visszaAMenuhez) {
        this.visszaAMenuhez = visszaAMenuhez;
        
        setupUIComponents();
        setupEventListeners();
    }
    
    // Getter for JatekAllapot
    public JatekAllapot getJatekAllapot() {
        return allapot;
    }
    

    public void setupUIComponents() {
        setFocusable(true);
        requestFocusInWindow();

        stopper = new Stopper();
        logika = new JatekLogika();
        allapot = new JatekAllapot();
        gombok = new JButton[4][4];
        elozoAllapotok = new Stack<>();

        setLayout(new BorderLayout());

        JPanel tablaPanel = new JPanel(new GridLayout(4, 4));
        add(tablaPanel, BorderLayout.CENTER);
        initGombok(tablaPanel);

        JPanel informacioPanel = createInfoPanel();
        add(informacioPanel, BorderLayout.NORTH);

        JPanel gombPanel = createButtonPanel();
        add(gombPanel, BorderLayout.SOUTH);

        frissitTabla();
    }

    private void initGombok(JPanel tablaPanel) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gombok[i][j] = new JButton();
                gombok[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                gombok[i][j].setEnabled(false);
                tablaPanel.add(gombok[i][j]);
            }
        }
    }

    private JPanel createInfoPanel() {
        JPanel informacioPanel = new JPanel();
        informacioPanel.setLayout(new BoxLayout(informacioPanel, BoxLayout.Y_AXIS));

        pontszamLabel = new JLabel("Pontszám: 0");
        pontszamLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pontszamLabel.setHorizontalAlignment(SwingConstants.CENTER);

        idoLabel = new JLabel("Idő: 00:00:000");
        idoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        idoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        
        felhasznalonevLabel = new JLabel("Név: " + felhasznalonev);
        felhasznalonevLabel.setFont(new Font("Arial", Font.BOLD, 18));
        felhasznalonevLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        informacioPanel.add(pontszamLabel);
        informacioPanel.add(idoLabel);
        informacioPanel.add(felhasznalonevLabel);  // Hozzáadjuk a felhasználónevet
        return informacioPanel;
    }
    

    private JPanel createButtonPanel() {
        JPanel gombPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        visszalepesGomb = createVisszalepesButton();
        JButton visszaMenubeGomb = createVisszaMenubeButton();
        JButton mentesGomb = createMentesButton();

        gombPanel.add(visszalepesGomb);
        gombPanel.add(visszaMenubeGomb);
        gombPanel.add(mentesGomb);

        return gombPanel;
    }

    private JButton createVisszalepesButton() {
        JButton button = new JButton("Visszalépés");
        button.setEnabled(false);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> undoMove());
        return button;
    }

    private JButton createVisszaMenubeButton() {
        JButton button = new JButton("Vissza a menübe");
        button.setBackground(new Color(47, 79, 79));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> visszaAMenuhez.run());
        return button;
    }

    private JButton createMentesButton() {
        JButton button = new JButton("Mentés");
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> saveGame());
        return button;
    }

    private void setupEventListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        new Timer(1, e -> updateTime()).start();
    }

    private void handleKeyPress(KeyEvent e) {
        if (!stopper.isRunning()) {
            stopper.start();
        }

        boolean tablaValtozott = false;
        saveCurrentState();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                logika.mozgatFel(allapot);
                tablaValtozott = true;
                break;
            case KeyEvent.VK_LEFT:
                logika.mozgatBalra(allapot);
                tablaValtozott = true;
                break;
            case KeyEvent.VK_DOWN:
                logika.mozgatLe(allapot);
                tablaValtozott = true;
                break;
            case KeyEvent.VK_RIGHT:
                logika.mozgatJobbra(allapot);
                tablaValtozott = true;
                break;
        }

        if (tablaValtozott) {
            handleTableChange();
        }
    }
    

    private void saveCurrentState() {
        allapot.elozoAllapot();
        JatekAllapot masolat = new JatekAllapot(allapot);
        elozoAllapotok.push(masolat);
    }

    private void handleTableChange() {
        visszalepesGomb.setEnabled(true);
        frissitTabla();
        pontszamLabel.setText("Pontszám: " + allapot.getPontszam());
        idoLabel.setText("Idő: " + stopper.getElteltIdo());
        felhasznalonevLabel.setText("Név: " + felhasznalonev);
        dicsoseglista.ujEredmeny(felhasznalonev, allapot.getPontszam(), stopper.getElteltIdo());

        if (!logika.vanMegLepes(allapot)) {
            endGame("Játék vége! Nincs több lépés.");
        } else if (logika.nyertE(allapot)) {
            endGame("Gratulálok, elérted a 2048-at!");
        }
    }

    private void endGame(String message) {
        stopper.stop();
        JOptionPane.showMessageDialog(null, message);
        dicsoseglista.ujEredmeny(felhasznalonev, allapot.getPontszam(), stopper.getElteltIdo());
        visszaAMenuhez.run();
        resetGame();
        frissitTabla();
    }

    private void updateTime() {
        if (stopper.isRunning()) {
            idoLabel.setText("Idő: " + stopper.getElteltIdo());
        }
    }

    private void undoMove() {
        if (!elozoAllapotok.isEmpty()) {
            allapot = elozoAllapotok.pop();
            frissitTabla();
            pontszamLabel.setText("Pontszám: " + allapot.getPontszam());
        }
        if (elozoAllapotok.isEmpty()) {
            visszalepesGomb.setEnabled(false);
        }
        requestFocusInWindow();
    }

    private void saveGame() {
        try {
            stopper.stop();
            String fajlNev = "jatek_mentese.txt";
            System.out.println(allapot.getFelhasznaloNev());
            dicsoseglista.ujEredmeny(felhasznalonev, allapot.getPontszam(), stopper.getElteltIdo());
            JatekMentese.mentes(fajlNev, allapot.getTabla(), allapot.getPontszam(), stopper, felhasznalonev);
            JOptionPane.showMessageDialog(null, "Játék mentve!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Hiba történt a mentés során: " + ex.getMessage());
        }
    }

    public void frissitTabla() {
        int[][] tabla = allapot.getTabla();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int ertek = tabla[i][j];
                gombok[i][j].setText(ertek == 0 ? "" : String.valueOf(ertek));
                gombok[i][j].setBackground(getSzin(ertek));
            }
        }
    }

    private Color getSzin(int ertek) {
        switch (ertek) {
            case 0: return new Color(204, 192, 179);
            case 2: return new Color(238, 228, 218);
            case 4: return new Color(237, 224, 200);
            case 8: return new Color(242, 177, 121);
            case 16: return new Color(245, 149, 99);
            case 32: return new Color(246, 124, 95);
            case 64: return new Color(246, 94, 59);
            case 128: return new Color(237, 207, 114);
            case 256: return new Color(237, 204, 97);
            case 512: return new Color(237, 200, 80);
            case 1024: return new Color(237, 197, 63);
            case 2048: return new Color(237, 194, 46);
            default: return Color.GRAY;
        }
    }

    public void frissitBetoltesUtan(JatekAllapot betoltottAllapot) {
        allapot.setTabla(betoltottAllapot.getTabla());
        allapot.setPontszam(betoltottAllapot.getPontszam());
        updateStopper(betoltottAllapot); // Stopper frissítése

        frissitTabla();
        pontszamLabel.setText("Pontszám: " + allapot.getPontszam());
        idoLabel.setText("Idő: " + stopper.getElteltIdo());

        elozoAllapotok.clear();
        visszalepesGomb.setEnabled(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void updateStopper(JatekAllapot betoltottAllapot) {
        String elapsedTimeStr = betoltottAllapot.getStopper().getElteltIdo();

        long elapsedMillis = convertToMillis(elapsedTimeStr);

        // Reseteljük a stoppert, hogy ne adódjon hozzá felesleges idő
        stopper.reset(); // A stopper nullázása, hogy ne hozzáadódjon több idő

        // Az új stopper kezdeti ideje
        stopper.setKezdoIdo(elapsedMillis); // A pontos kezdő idő beállítása a betöltött értékek alapján
    }
    
    private long convertToMillis(String elapsedTimeStr) {
        try {
            String[] parts = elapsedTimeStr.split(":");
            int minutes = Integer.parseInt(parts[0]);
            String[] secondsParts = parts[1].split("\\.");
            int seconds = Integer.parseInt(secondsParts[0]);
            int millis = Integer.parseInt(secondsParts[1]);

            // Teljes milliszekundum számítása
            return (minutes * 60 * 1000) + (seconds * 1000) + millis;
        } catch (Exception e) {
            // Hiba esetén visszatérünk 0-val
            return 0;
        }
    }

    public JatekAllapot getAllapot() {
        return allapot;
    }

    public void resetIdo(long idoMillis) {
        stopper.setKezdoIdo(idoMillis);
    }
    
    public void resetGame() {
        // Reseteljük az összes komponenst
        //allapot = new JatekAllapot();  // Új állapot létrehozása
        stopper.stop();  // Megállítjuk a stoppert
        stopper.reset();  // Reseteljük a stoppert
        elozoAllapotok.clear();  // Előző állapotok törlése
        allapot.resetAllapot();

        // UI frissítése
        frissitTabla();  // A táblát frissítjük, hogy a régi adatok eltűnjenek
        pontszamLabel.setText("Pontszám: 0");  // A pontszám nullázása
        idoLabel.setText("Idő: 00:00:000");  // Az idő nullázása
        felhasznalonevLabel.setText("Név: " + felhasznalonev);
        

        // A visszalépés gombot deaktiváljuk, mert nincs előző állapot
        visszalepesGomb.setEnabled(false);
    }
    
    

}
