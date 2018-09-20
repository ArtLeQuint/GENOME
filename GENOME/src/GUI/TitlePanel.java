package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.s_FONT;
import static GUI.Constant.s_WHITE;

final class TitlePanel extends JPanel {

    /**
     * The title
     */
    private final JLabel m_TITLE;
    /**
     * The font size
     */
    private final int m_SIZE;
    /**
     * The background color
     */
    private final Color m_BACKGROUND;

    /**
     * Class constructor
     *
     * @param _title      the title
     * @param _size       the font size
     * @param _background the background color
     */
    TitlePanel(String _title, int _size, Color _background) {
        super();

        m_TITLE = new JLabel(_title);
        m_SIZE = _size;
        m_BACKGROUND = _background;

        initLayout();
        addComponents();
        swagComponent();
    }

    /**
     * Init the layouts of the panel
     */
    private void initLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        m_TITLE.setLayout(new GridBagLayout());
    }

    /**
     * Add the components into the panel
     */
    private void addComponents() {
        this.add(Box.createHorizontalGlue());
        this.add(m_TITLE, BorderLayout.CENTER);
        this.add(Box.createHorizontalGlue());
    }

    /**
     * Make components of the panel pretty
     */
    private void swagComponent() {
        this.setBackground(m_BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(m_SIZE / 2, m_SIZE / 2, m_SIZE / 2, m_SIZE / 2));
        m_TITLE.setForeground(s_WHITE);
        m_TITLE.setFont(new Font(s_FONT, Font.PLAIN, m_SIZE));
    }

}
