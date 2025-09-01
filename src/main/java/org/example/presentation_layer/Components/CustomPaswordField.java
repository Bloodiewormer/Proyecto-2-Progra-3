package org.example.presentation_layer.Components;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CustomPaswordField extends JPasswordField {
    private final Image icon; // Declare the icon field

    public CustomPaswordField() {
        setBorder(BorderFactory.createEmptyBorder(5, 32, 5, 5)); // Padding for icon
        setOpaque(false);
        // Load the icon from resources
        icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/lock.png"))).getImage();

        setForeground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE); // White background
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int radius = 5;
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw the icon on the left inside the field
        if (icon != null) {
            int iconSize = 25;
            int iconY = (getHeight() - iconSize) / 2;
            g2.drawImage(icon, 8, iconY, iconSize, iconSize, this);
        }
        super.paintComponent(g);
        g2.dispose();
    }
}

