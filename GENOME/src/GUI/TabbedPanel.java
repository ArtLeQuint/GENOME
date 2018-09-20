package GUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

import static GUI.Constant.*;

public class TabbedPanel extends JTabbedPane {

    /**
     * Class constructor
     */
    public TabbedPanel() {
        boolean tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");
        boolean contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
        setBorder(BorderFactory.createLineBorder(s_DARKGRAY, 10));
        setUI(new BasicTabbedPaneUI() {
            @Override
            protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
                g.setColor(s_LIGHTGRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintTabArea(g, tabPlacement, selectedIndex);
            }

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(!isSelected ? s_BLUEGRAY : s_CHARCOAL);
                switch (tabPlacement) {
                    case LEFT:
                        g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2); // bottom-left highlight
                        g.drawLine(x, y + 2, x, y + h - 3); // left highlight
                        g.drawLine(x + 1, y + 1, x + 1, y + 1); // top-left highlight
                        g.drawLine(x + 2, y, x + w - 1, y); // top highlight
                        g.drawLine(x + 2, y + h - 2, x + w - 1, y + h - 2); // bottom shadow
                        g.drawLine(x + 2, y + h - 1, x + w - 1, y + h - 1); // bottom dark shadow
                        break;
                    case RIGHT:
                        g.drawLine(x, y, x + w - 3, y); // top highlight
                        g.drawLine(x, y + h - 2, x + w - 3, y + h - 2); // bottom shadow
                        g.drawLine(x + w - 2, y + 2, x + w - 2, y + h - 3); // right shadow
                        g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1); // top-right dark shadow
                        g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2); // bottom-right dark shadow
                        g.drawLine(x + w - 1, y + 2, x + w - 1, y + h - 3); // right dark shadow
                        g.drawLine(x, y + h - 1, x + w - 3, y + h - 1); // bottom dark shadow
                        break;
                    case BOTTOM:
                        g.drawLine(x, y, x, y + h - 3); // left highlight
                        g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2); // bottom-left highlight
                        g.drawLine(x + 2, y + h - 2, x + w - 3, y + h - 2); // bottom shadow
                        g.drawLine(x + w - 2, y, x + w - 2, y + h - 3); // right shadow
                        g.drawLine(x + 2, y + h - 1, x + w - 3, y + h - 1); // bottom dark shadow
                        g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2); // bottom-right dark shadow
                        g.drawLine(x + w - 1, y, x + w - 1, y + h - 3); // right dark shadow
                        break;
                    case TOP:
                    default:
                        g.drawLine(x, y + 2, x, y + h - 1); // left highlight
                        g.drawLine(x + 1, y + 1, x + 1, y + 1); // top-left highlight
                        g.drawLine(x + 2, y, x + w - 3, y); // top highlight
                        g.drawLine(x + w - 2, y + 2, x + w - 2, y + h - 1); // right shadow
                        g.drawLine(x + w - 1, y + 2, x + w - 1, y + h - 1); // right dark-shadow
                        g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1); // top-right shadow
                }
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(!isSelected ? s_BLUEGRAY : s_CHARCOAL);
                switch (tabPlacement) {
                    case LEFT:
                        g.fillRect(x + 1, y + 1, w - 1, h - 3);
                        break;
                    case RIGHT:
                        g.fillRect(x, y + 1, w - 2, h - 3);
                        break;
                    case BOTTOM:
                        g.fillRect(x + 1, y, w - 3, h - 1);
                        break;
                    case TOP:
                    default:
                        g.fillRect(x + 1, y + 1, w - 3, h - 1);
                }
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                int width = tabPane.getWidth();
                int height = tabPane.getHeight();
                Insets insets = tabPane.getInsets();
                Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

                int x = insets.left;
                int y = insets.top;
                int w = width - insets.right - insets.left;
                int h = height - insets.top - insets.bottom;

                switch (tabPlacement) {
                    case LEFT:
                        x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                        if (tabsOverlapBorder) {
                            x -= tabAreaInsets.right;
                        }
                        w -= (x - insets.left);
                        break;
                    case RIGHT:
                        w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                        if (tabsOverlapBorder) {
                            w += tabAreaInsets.left;
                        }
                        break;
                    case BOTTOM:
                        h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                        if (tabsOverlapBorder) {
                            h += tabAreaInsets.top;
                        }
                        break;
                    case TOP:
                    default:
                        y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                        if (tabsOverlapBorder) {
                            y -= tabAreaInsets.bottom;
                        }
                        h -= (y - insets.top);
                }

                if (tabPane.getTabCount() > 0 && (contentOpaque || tabPane.isOpaque())) {
                    g.setColor(s_CHARCOAL);
                    g.fillRect(x, y, w, h);
                }

                paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
                paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
                paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
                paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            }

            protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
                Rectangle selRect = selectedIndex < 0 ? null :
                        getTabBounds(selectedIndex, calcRect);

                g.setColor(s_CHARCOAL);

                if (tabPlacement != TOP || selectedIndex < 0 || (selRect.y + selRect.height + 1 < y) || (selRect.x < x || selRect.x > x + w)) {
                    g.drawLine(x, y, x + w - 2, y);
                } else {
                    g.drawLine(x, y, selRect.x - 1, y);
                    if (selRect.x + selRect.width < x + w - 2) {
                        g.drawLine(selRect.x + selRect.width, y,
                                x + w - 2, y);
                    } else {
                        g.drawLine(x + w - 2, y, x + w - 2, y);
                    }
                }
            }

            protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
                                                      int selectedIndex,
                                                      int x, int y, int w, int h) {
                Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

                g.setColor(s_CHARCOAL);

                if (tabPlacement != LEFT || selectedIndex < 0 || (selRect.x + selRect.width + 1 < x) || (selRect.y < y || selRect.y > y + h)) {
                    g.drawLine(x, y, x, y + h - 2);
                } else {
                    g.drawLine(x, y, x, selRect.y - 1);
                    if (selRect.y + selRect.height < y + h - 2) {
                        g.drawLine(x, selRect.y + selRect.height,
                                x, y + h - 2);
                    }
                }
            }

            @Override
            protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
                Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

                g.setColor(s_CHARCOAL);

                if (tabPlacement != BOTTOM || selectedIndex < 0 || (selRect.y - 1 > h) || (selRect.x < x || selRect.x > x + w)) {
                    g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
                    g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
                } else {
                    g.drawLine(x + 1, y + h - 2, selRect.x - 1, y + h - 2);
                    g.drawLine(x, y + h - 1, selRect.x - 1, y + h - 1);
                    if (selRect.x + selRect.width < x + w - 2) {
                        g.setColor(s_CHARCOAL);
                        g.drawLine(selRect.x + selRect.width, y + h - 2, x + w - 2, y + h - 2);
                        g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y + h - 1);
                    }
                }

            }

            @Override
            protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
                Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

                g.setColor(s_CHARCOAL);

                if (tabPlacement != RIGHT || selectedIndex < 0 || (selRect.x - 1 > w) || (selRect.y < y || selRect.y > y + h)) {
                    g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 3);
                    g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
                } else {
                    g.drawLine(x + w - 2, y + 1, x + w - 2, selRect.y - 1);
                    g.drawLine(x + w - 1, y, x + w - 1, selRect.y - 1);

                    if (selRect.y + selRect.height < y + h - 2) {
                        g.drawLine(x + w - 2, selRect.y + selRect.height,
                                x + w - 2, y + h - 2);
                        g.drawLine(x + w - 1, selRect.y + selRect.height,
                                x + w - 1, y + h - 2);
                    }
                }
            }
        });
    }
}
