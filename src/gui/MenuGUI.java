// MenuGUI osztály
package gui;

import logic.DicsosegLista;
import logic.JatekAllapot;
import logic.JatekMentese;
import logic.Stopper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class MenuGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JatekGUI jatekGUI;
    private Stopper stopper;
    private DicsosegLista dicsoseglista = new DicsosegLista();

    private JTable dicsosegTable;
    private DefaultTableModel dicsosegTableModel;

    private String felhasznalonev; // Tárolja a játékos nevét

    private static final Color NEW_GAME_COLOR = new Color(60, 179, 113);
    private static final Color LOAD_GAME_COLOR = new Color(95, 158, 160);
    private static final Color RULES_COLOR = new Color(70, 130, 180);
    private static final Color HIGH_SCORE_COLOR = new Color(70, 130, 180);
    private static final Color EXIT_COLOR = new Color(205, 92, 92);

    public MenuGUI() {
        stopper = new Stopper();

        // Frame beállítások
        setupFrame();

        // Kártya elrendezés
        setupCardLayout();

        // Menü panel
        JPanel menuPanel = createMenuPanel();
        cardPanel.add(menuPanel, "Menu");

        // Felhasználónév panel
        FelhasznalonevGUI nevPanel = new FelhasznalonevGUI(this, jatekGUI);
        cardPanel.add(nevPanel, "Name");

        // Játék panel létrehozása egyszer
        jatekGUI = new JatekGUI(() -> cardLayout.show(cardPanel, "Menu"));
        cardPanel.add(jatekGUI, "Game");

        // Dicsőséglista panel
        JPanel dicsosegPanel = createDicsosegPanel();
        cardPanel.add(dicsosegPanel, "HighScores");

        // Kártya panel hozzáadása az ablakhoz
        add(cardPanel);
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("2048 Játék");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Középre igazítja az ablakot
    }

    private void setupCardLayout() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new BorderLayout());

        // Cím
        JLabel cim = new JLabel("2048 Játék", SwingConstants.CENTER);
        cim.setFont(new Font("Arial", Font.BOLD, 30));
        cim.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        menuPanel.add(cim, BorderLayout.NORTH);

        // Gombok panel
        JPanel gombPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        gombPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Gombok hozzáadása
        addMenuButtons(gombPanel);

        menuPanel.add(gombPanel, BorderLayout.CENTER);

        return menuPanel;
    }

    private void addMenuButtons(JPanel gombPanel) {
        JButton ujJatekGomb = createButton("Új Játék", NEW_GAME_COLOR, this::startNewGame);
        JButton regiJatekGomb = createButton("Régi játék betöltése", LOAD_GAME_COLOR, this::loadGame);
        JButton szabalyokGomb = createButton("Szabályok", RULES_COLOR, this::showRules);
        JButton dicsoseglistaGomb = createButton("Dicsőséglista", HIGH_SCORE_COLOR, this::showHighScores);
        JButton kilepesGomb = createButton("Kilépés", EXIT_COLOR, this::exitGame);

        gombPanel.add(ujJatekGomb);
        gombPanel.add(regiJatekGomb);
        gombPanel.add(szabalyokGomb);
        gombPanel.add(dicsoseglistaGomb);
        gombPanel.add(kilepesGomb);
    }

    private JButton createButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }

    void startNewGame(ActionEvent e) {
        jatekGUI.resetGame();
        jatekGUI.frissitTabla();
        FelhasznalonevGUI nevPanel = (FelhasznalonevGUI) cardPanel.getComponent(1);
        nevPanel.nevInputTorles();
        // Felhasználónév panel indítása
        cardLayout.show(cardPanel, "Name");
    }

    private void loadGame(ActionEvent e) {
        try {
            String fajlNev = "jatek_mentese.txt";

            JatekAllapot betoltottAllapot = JatekMentese.betoltes(fajlNev);
            jatekGUI.frissitBetoltesUtan(betoltottAllapot);
            cardLayout.show(cardPanel, "Game");

            SwingUtilities.invokeLater(jatekGUI::requestFocusInWindow);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Hiba történt a fájl betöltése során!", "Hiba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRules(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "A 2048 játék célja, hogy a csempéket összeolvasztva elérd a 2048 számot.\n" +
                        "Mozgasd a csempéket nyilakkal, és az azonos számú csempék összeolvadnak.",
                "Szabályok",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHighScores(ActionEvent e) {
        refreshHighScoresTable();
        cardLayout.show(cardPanel, "HighScores");
    }

    private void exitGame(ActionEvent e) {
        System.exit(0); // Kilép a programból
    }

    private JPanel createDicsosegPanel() {
        JPanel dicsosegPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Felhasználónév", "Pontszám", "Idő"};
        dicsosegTableModel = new DefaultTableModel(columnNames, 0);
        dicsosegTable = new JTable(dicsosegTableModel);

        refreshHighScoresTable();

        JScrollPane scrollPane = new JScrollPane(dicsosegTable);
        dicsosegPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backToMenuButton = new JButton("Vissza a menübe");
        backToMenuButton.setBackground(new Color(47, 79, 79));
        backToMenuButton.setForeground(Color.WHITE);

        backToMenuButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        dicsosegPanel.add(backToMenuButton, BorderLayout.SOUTH);

        return dicsosegPanel;
    }

    private void refreshHighScoresTable() {
        // Újratöltjük a dicsőséglista adatait a fájlból
        dicsoseglista.betoltAdatok();

        dicsosegTableModel.setRowCount(0); // Töröljük a meglévő sorokat
        List<DicsosegLista.Dicsoseg> lista = dicsoseglista.getDicsosegLista();

        for (DicsosegLista.Dicsoseg d : lista) {
            dicsosegTableModel.addRow(new Object[]{d.getFelhasznalonev(), d.getPontszam(), d.getIdo()});
        }
    }

    public void startGame() {
        jatekGUI.resetGame();
        jatekGUI.frissitTabla();
        cardLayout.show(cardPanel, "Game");
        SwingUtilities.invokeLater(jatekGUI::requestFocusInWindow);
    }

    public void setFelhasznalonev(String nev) {
        this.felhasznalonev = nev;
        jatekGUI.felhasznalonev = nev;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuGUI::new);
    }
}