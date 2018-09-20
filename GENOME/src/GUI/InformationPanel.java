package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public final class InformationPanel extends IPanel {

    /**
     * The title of the informationPanel
     */
    private static final String s_TITLE = "Informations";

    /**
     * The container of the informationPanel
     */
    private JComponent m_container;

    /**
     * The scrollpanel of the informationPanel
     */
    private ScrollPanel m_scroll;

    /**
     * Class constructor
     */
    InformationPanel() {
        super(s_TITLE);
    }

    /**
     * Make the component beautiful
     *
     * @param _component the component to modify
     * @return the beautiful component
     */
    public static JComponent swagComponent(JComponent _component) {
        _component.setBackground(s_LIGHTGRAY);
        _component.setFont(new Font(s_FONT, Font.PLAIN, 14));
        _component.setForeground(s_WHITE);
        return _component;
    }

    /**
     * Create the components of the informationPanel
     */
    protected void createComponent() {
        m_container = new JPanel();
        m_scroll = new ScrollPanel(m_container);
    }

    /**
     * Init the layout of the informationPanel
     */
    protected void initLayout() {
    }

    /**
     * Add the component into the informationPanel
     */
    protected void addComponents() {
        super.add(m_scroll);
    }

    /**
     * Make the Panel pretty
     */
    protected void swagComponent() {
        swagComponent(m_container);
    }

    /**
     * Update the information present inside of the Component
     *
     * @param _component the component to update
     */
    void updateInformation(JComponent _component) {
        super.remove(m_scroll);
        m_container = _component;
        m_container.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));
        m_scroll = new ScrollPanel(m_container);
        swagComponent();
        super.add(m_scroll);
        super.repaint();
        super.updateUI();
    }
}
