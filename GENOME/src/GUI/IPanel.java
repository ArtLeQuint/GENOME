package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.s_BLUEGRAY;
import static GUI.Constant.s_LIGHTGRAY;

abstract class IPanel extends JPanel {

    /**
     * Class constructor
     */
    IPanel(String _title) {
        final TitlePanel titlePanel = new TitlePanel(_title, 20, s_BLUEGRAY);
        this.setLayout(new BorderLayout());
        this.add(titlePanel, BorderLayout.NORTH);
        this.setBackground(s_LIGHTGRAY);
        createComponent();
        initLayout();
        addComponents();
        swagComponent();
    }

    /**
     * Create the components of the panel
     */
    protected abstract void createComponent();

    /**
     * Init the layouts of the panel
     */
    protected abstract void initLayout();

    /**
     * Add the components into the panel
     */
    protected abstract void addComponents();

    /**
     * Make components of the panel pretty
     */
    protected abstract void swagComponent();

}
