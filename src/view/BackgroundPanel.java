package view;

import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel {
    private Image bg;

    public BackgroundPanel(Image bg) {
        this.bg = bg;
        setLayout(null); // vẫn giữ layout null
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}
