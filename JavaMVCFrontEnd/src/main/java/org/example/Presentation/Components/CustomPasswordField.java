package org.example.Presentation.Components;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CustomPasswordField extends JPasswordField {
    private final Image icon;
    private final String placeholder;
    private boolean showingPlaceholder;

    public CustomPasswordField() {
        this("");
    }

    public CustomPasswordField(String placeholder) {
        this.placeholder = placeholder;
        this.showingPlaceholder = true;

        setBorder(BorderFactory.createEmptyBorder(5, 32, 5, 5));
        setOpaque(false);
        icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/lock.png"))).getImage();

        setForeground(Color.GRAY);
        setText(placeholder);
        setEchoChar((char) 0);

        addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(Color.BLACK);
                    setEchoChar('●');
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (getText().isEmpty()) {
                    setEchoChar((char) 0);
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    showingPlaceholder = true;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int radius = 5;
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        if (icon != null) {
            int iconSize = 25;
            int iconY = (getHeight() - iconSize) / 2;
            g2.drawImage(icon, 8, iconY, iconSize, iconSize, this);
        }
        super.paintComponent(g);
        g2.dispose();
    }
}
