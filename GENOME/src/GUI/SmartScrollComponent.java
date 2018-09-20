package GUI;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

class SmartScrollComponent implements AdjustmentListener {

    /**
     * True if the scroll bar must be adjusted
     */
    private boolean m_adjustScrollBar = true;
    /**
     * Value use to adjust
     */
    private int m_previousValue = -1;
    /**
     * Value use to adjust
     */
    private int m_previousMaximum = -1;

    /**
     * Specify how the SmartScrollComponent will function.
     *
     * @param scrollPane the scroll pane to monitor
     */
    public SmartScrollComponent(JScrollPane scrollPane) {
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(this);

        Component view = scrollPane.getViewport().getView();

        if (view instanceof JTextComponent) {
            JTextComponent textComponent = (JTextComponent) view;
            DefaultCaret caret = (DefaultCaret) textComponent.getCaret();
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }
    }

    /**
     * Invoked when the value of the adjustable has changed.
     *
     * @param e the event to be processed
     */
    @Override
    public void adjustmentValueChanged(final AdjustmentEvent e) {
        SwingUtilities.invokeLater(() -> checkScrollBar(e));
    }

    /**
     * Analyze every adjustment event to determine when the viewport
     * needs to be repositioned.
     *
     * @param e the adjustment event
     */
    private void checkScrollBar(AdjustmentEvent e) {
        JScrollBar scrollBar = (JScrollBar) e.getSource();
        BoundedRangeModel listModel = scrollBar.getModel();
        int value = listModel.getValue();
        int extent = listModel.getExtent();
        int maximum = listModel.getMaximum();

        boolean valueChanged = m_previousValue != value;
        boolean maximumChanged = m_previousMaximum != maximum;

        if (valueChanged && !maximumChanged) {
            m_adjustScrollBar = value + extent >= maximum;
        }

        if (m_adjustScrollBar) {
            scrollBar.removeAdjustmentListener(this);
            value = maximum - extent;
            scrollBar.setValue(value);
            scrollBar.addAdjustmentListener(this);
        }

        m_previousValue = value;
        m_previousMaximum = maximum;
    }
}