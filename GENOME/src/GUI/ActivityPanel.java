package GUI;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.s_BLUEGRAY;
import static GUI.Constant.s_CHARCOAL;

public final class ActivityPanel extends JPanel {

    /**
     * The panel containing the component
     */
    private JPanel m_container;

    /**
     * The button to start the program
     */
    private ButtonComponent m_start;

    /**
     * The button to pause the execution of the program
     */
    private ButtonComponent m_pause;

    /**
     * The button to resume the execution after a pause
     */
    private ButtonComponent m_resume;

    /**
     * The button to stop the execution of the program
     */
    private ButtonComponent m_stop;

    /**
     * Class constructor
     */
    ActivityPanel() {
        createComponent();
        initLayout();
        addComponents();
        swagComponent();
    }

    /**
     * Add start action
     *
     * @param _activityListener the start action
     */
    void addtStartListener(ActivityListener _activityListener) {
        m_start.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(m_start);
                m_container.add(m_pause, 0);
                m_container.revalidate();
                m_container.repaint();
                m_pause.resetColor();
            }
        });
    }

    /**
     * Add stop action
     *
     * @param _activityListener the stop action
     */
    void addStopListener(ActivityListener _activityListener) {
        m_stop.addActionListener(e -> {
            _activityListener.activityEvent();
            m_container.remove(0);
            m_container.add(m_start, 0);
            m_container.revalidate();
            m_container.repaint();
            m_start.resetColor();
        });
    }

    /**
     * Add pause action
     *
     * @param _activityListener the pause action
     */
    void addPauseListener(ActivityListener _activityListener) {
        m_pause.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(m_pause);
                m_container.add(m_resume, 0);
                m_container.revalidate();
                m_container.repaint();
                m_resume.resetColor();
            } else {
                m_container.remove(0);
                m_container.add(m_start, 0);
                m_container.revalidate();
                m_container.repaint();
                m_start.resetColor();
            }
        });
    }

    /**
     * Add resume action
     *
     * @param _activityListener the resume action
     */
    void addResumeListener(ActivityListener _activityListener) {
        m_resume.addActionListener(e -> {
            if (_activityListener.activityEvent()) {
                m_container.remove(m_resume);
                m_container.add(m_pause, 0);
                m_container.revalidate();
                m_container.repaint();
                m_pause.resetColor();
            } else {
                m_container.remove(0);
                m_container.add(m_start, 0);
                m_container.revalidate();
                m_container.repaint();
                m_start.resetColor();
            }
        });
    }

    /**
     * Create the components of the activity panel
     */
    private void createComponent() {
        m_container = new JPanel();
        m_start = new ButtonComponent("Ressources/play.png");
        m_pause = new ButtonComponent("Ressources/pause.png");
        m_resume = new ButtonComponent("Ressources/resume.png");
        m_stop = new ButtonComponent("Ressources/stop.png");
    }

    /**
     * Initialize the layout of the activity panel
     */
    private void initLayout() {
        this.setLayout(new BorderLayout());
        m_container.setLayout(new GridLayout(1, 2));
    }

    /**
     * Add the components into the activity panel
     */
    private void addComponents() {
        m_container.add(m_start);
        m_container.add(m_stop);
        super.add(m_container);
    }

    /**
     * Make the Panel pretty
     */
    private void swagComponent() {
        super.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        m_container.setBackground(s_BLUEGRAY);
    }

    /**
     * Use to set set action of each button
     */
    public interface ActivityListener {
        boolean activityEvent();
    }
}
