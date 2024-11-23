package gui;

import javax.swing.*;
import logic.JatekAllapot;
import java.awt.*;

public class FelhasznalonevGUI extends JPanel {
    private JTextField nevField;
    private JButton startButton;
    private MenuGUI menuGUI;
    private JatekGUI jatekGUI;

    public FelhasznalonevGUI(MenuGUI menuGUI, JatekGUI jatekGUI) {
        this.menuGUI = menuGUI;
        this.jatekGUI = jatekGUI;
        setLayout(new BorderLayout());

        // Label at the top
        JLabel cim = new JLabel("Add meg a felhasználóneved!", SwingConstants.CENTER);
        cim.setFont(new Font("Arial", Font.BOLD, 24));
        cim.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(cim, BorderLayout.NORTH);

        // Input panel (username field)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nevField = new JTextField();
        nevField.setPreferredSize(new Dimension(200, 30)); // Fixed size
        inputPanel.add(nevField);
        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel gombPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center-align the button
        startButton = new JButton("Játék indítása");
        startButton.setPreferredSize(new Dimension(150, 30)); // Fixed size for button
        startButton.setBackground(new Color(60, 179, 113)); // Green background
        startButton.setForeground(Color.WHITE); // White text
        startButton.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger font
        gombPanel.add(startButton); // Add the button to the panel
        add(gombPanel, BorderLayout.SOUTH); // Add the button panel to the bottom

        // Button action listener
        startButton.addActionListener(e -> {
            String felhasznalonev = nevField.getText().trim(); // Remove extra spaces
            if (felhasznalonev.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kérlek, add meg a felhasználónevet!", "Hiba", JOptionPane.ERROR_MESSAGE);
            } else {
                // Set the username in JatekAllapot
                JatekAllapot allapot = new JatekAllapot();  // Get the existing instance
                allapot.setFelhasznaloNev(felhasznalonev);  // Set the username
                menuGUI.setFelhasznalonev(felhasznalonev);
                
                // Switch to the game panel
                menuGUI.startGame(); // Start the game in MenuGUI
            }
        });
    }

    // Optional method to clear the username input field
    public void nevInputTorles() {
        nevField.setText("");
    }
}
