package GUI;

import Utils.Logs;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import static GUI.Constant.*;

public final class LogsPanel extends IPanel {

    /**
     * The title of the informationPanel
     */
    private final static String s_TITLE = "Logs";

    /**
     * Area to set text
     */
    private JTextPane m_textPane;
    /**
     * Use to add scroll bar
     */
    private ScrollPanel m_container;

    /**
     * Class constructor
     */
    LogsPanel() {
        super(s_TITLE);
    }

    /**
     * Create the components of the panel
     */
    protected void createComponent() {
        m_textPane = new JTextPane();
        m_container = new ScrollPanel(m_textPane);
        new SmartScrollComponent(m_container);
    }

    /**
     * Init the layouts of the panel
     */
    protected void initLayout() {
    }

    /**
     * Add the components into the panel
     */
    protected void addComponents() {
        super.add(m_container);
    }

    /**
     * Make components of the panel pretty
     */
    protected void swagComponent() {
        m_textPane.setBackground(s_LIGHTGRAY);
        m_textPane.setFont(new Font(s_FONT, Font.PLAIN, 14));
        m_textPane.setForeground(s_WHITE);
        m_textPane.setEditable(false);
        m_textPane.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));
    }

    /**
     * Update the log written in the LogsPanel
     * Write the new log, and deletes part of it after a certain length
     *
     * @param _log  the log to write into the LogsPanel
     * @param _type the type of the log (info, warning or exception)
     */
    void updateLog(String _log, Logs.Type _type) {
        Color m_logColour;
        switch (_type) {
            case WARNING:
                m_logColour = s_ORANGE;
                break;
            case EXCEPTION:
                m_logColour = s_RED;
                break;
            case NOTICE:
                m_logColour = s_BLUE;
                break;
            default:
                m_logColour = s_WHITE;
                break;
        }
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, m_logColour);
        int len = m_textPane.getDocument().getLength();
        m_textPane.setCharacterAttributes(aset, true);
        try {
            m_textPane.getDocument().insertString(len, _log + "\n", aset);
        } catch (BadLocationException e) {
            Logs.exception(e);
        }
        if (m_textPane.getText().split("\n").length > 1000) {
            try {
                m_textPane.getDocument().remove(0, len / 20);
            } catch (BadLocationException e) {
                Logs.exception(e);
            }
        }

    }
}
