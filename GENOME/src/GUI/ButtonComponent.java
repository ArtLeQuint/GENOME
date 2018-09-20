package GUI;

import Utils.Logs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static GUI.Constant.*;

final class ButtonComponent extends JButton {

    /**
     * Class constructor
     *
     * @param _path, the icon path
     */
    ButtonComponent(String _path) {
        super.setMargin(s_INSETS);
        super.setBackground(s_BLUEGRAY);
        super.setForeground(s_WHITE);
        super.setFont(new Font(s_FONT, Font.BOLD, 16));
        super.setBorder(BorderFactory.createEmptyBorder());
        super.setHorizontalAlignment(SwingConstants.CENTER);
        super.setVerticalAlignment(SwingConstants.CENTER);
        super.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(s_DARKGRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(s_BLUEGRAY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });

        try {
            Image img = ImageIO.read(getClass().getResource(_path)).getScaledInstance(20, 20, Image.SCALE_DEFAULT);
            super.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            Logs.exception(e);
        }
    }

    /**
     * Reset the color of the button
     */
    void resetColor() {
        setBackground(s_BLUEGRAY);
    }

}
