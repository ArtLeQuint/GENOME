package GUI;

import Utils.Logs;
import Utils.Options;

import javax.swing.*;
import java.awt.*;

import static GUI.Constant.*;

public class WarningFrame extends JFrame {

    /**
     * Class constructor
     */
    public WarningFrame() {
        super();
        final JPanel panel = new IPanel(s_TITLE) {

            private JPanel m_container;
            private JLabel m_label1;
            private JLabel m_label2;
            private JLabel m_label3;

            protected void createComponent() {
                m_container = new JPanel();
                m_label1 = new JLabel("Impossible de relancer le programme.");
                m_label2 = new JLabel("- Soit le programme est encore en cours d'exécution et attend la fin des threads.");
                m_label3 = new JLabel("- Soit la précédente exécution a été interrompue prématurément. Dans ce cas,veuillez supprimer le fichier  \"" + Options.getMutexFileName() + "\".");
            }

            protected void initLayout() {
                m_container.setLayout(new BoxLayout(m_container, BoxLayout.Y_AXIS));
            }

            protected void addComponents() {
                m_container.add(m_label1);
                m_container.add(m_label2);
                m_container.add(m_label3);
                super.add(m_container);
            }

            protected void swagComponent() {
                m_label1.setBackground(s_LIGHTGRAY);
                m_label1.setForeground(s_WHITE);
                m_label1.setFont(new Font(s_FONT, Font.PLAIN, 15));
                m_label2.setBackground(s_LIGHTGRAY);
                m_label2.setForeground(s_WHITE);
                m_label2.setFont(new Font(s_FONT, Font.PLAIN, 15));
                m_label3.setBackground(s_LIGHTGRAY);
                m_label3.setForeground(s_WHITE);
                m_label3.setFont(new Font(s_FONT, Font.PLAIN, 15));
                m_container.setBackground(s_LIGHTGRAY);
                m_container.setBorder(BorderFactory.createLineBorder(s_LIGHTGRAY, 10));
            }
        };
        initIcone();
        setResizable(false);
        setSize(830, 180);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
    }

    /**
     * Initialize the icon of the app
     */
    private void initIcone() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("Ressources/logo.bioinfo.png"))));
        } catch (Exception e) {
            Logs.exception(e);
        }
    }
}
