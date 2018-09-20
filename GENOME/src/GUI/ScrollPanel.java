package GUI;


import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static GUI.Constant.s_DARKGRAY;
import static GUI.Constant.s_LIGHTGRAY;

final class ScrollPanel extends JScrollPane {

    /**
     * Class constructor
     *
     * @param _component, the component to add scroll bar
     */
    ScrollPanel(Component _component) {
        super(_component, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setPaint(s_DARKGRAY);
                g2.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 5, 5);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setPaint(s_LIGHTGRAY);
                g2.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                g2.dispose();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                Dimension zeroDim = new Dimension(0, 0);
                button.setPreferredSize(zeroDim);
                button.setMinimumSize(zeroDim);
                button.setMaximumSize(zeroDim);
                return button;
            }
        });
        super.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setPaint(s_DARKGRAY);
                g2.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 5, 5);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setPaint(s_LIGHTGRAY);
                g2.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                g2.dispose();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                Dimension zeroDim = new Dimension(0, 0);
                button.setPreferredSize(zeroDim);
                button.setMinimumSize(zeroDim);
                button.setMaximumSize(zeroDim);
                return button;
            }
        });
        super.getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 10));
        super.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        super.setBackground(s_LIGHTGRAY);
        super.setBorder(BorderFactory.createEmptyBorder());
        super.repaint();
        super.updateUI();
    }

}
