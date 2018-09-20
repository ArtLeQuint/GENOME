package GUI;

import Utils.Logs;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static GUI.Constant.*;

public final class MainFrame extends JFrame implements MouseMotionListener, MouseListener {

    /**
     * Default Toolkit of the frame
     */
    private static final Toolkit s_TOOLKIT = Toolkit.getDefaultToolkit();
    /**
     * Dimension of the screen (mainscreen if multi-screen)
     */
    private static final Dimension s_DIM = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * Default width size
     */
    private static final int s_DEFAULT_FRAME_WIDTH = 300;
    /**
     * Default height size
     */
    private static final int s_DEFAULT_FRAME_HEIGHT = 300;
    /**
     * Origin point to print the frame on the screen
     */
    private static final Point s_INITIAL_LOCATION = new Point((int) s_TOOLKIT.getScreenSize().getWidth() / 2 - s_DEFAULT_FRAME_WIDTH / 2, (int) s_TOOLKIT.getScreenSize().getHeight() / 2 - s_DEFAULT_FRAME_HEIGHT / 2);
    /**
     * Default Dimension of the frame
     */
    private static final Dimension s_INITIAL_DIMENSION = new Dimension(s_DEFAULT_FRAME_WIDTH, s_DEFAULT_FRAME_HEIGHT);

    /**
     * Hitbox area of the mouse
     */
    private static final int s_CURSOR_AREA = 6;
    /**
     * The mainframe iself
     */
    private static MainFrame s_mainFrame;

    /**
     * Saved location (for frame moves events)
     */
    private final Point m_INITIAL_LOCATION;
    /**
     * Minimal width of the frame
     */
    private final int m_MIN_WIDTH;
    /**
     * Minimal height of the frame
     */
    private final int m_MIN_HEIGHT;
    /**
     * For frame drag&drop
     */
    private Point m_start_drag;
    /**
     * For frame drag&drop
     */
    private Point m_start_loc;
    /**
     * Header Panel
     */
    private JPanel m_header;
    /**
     * Main panel of the frame
     */
    private JPanel m_main;
    /**
     * Footer Panel
     */
    private TitlePanel m_footer;
    /**
     * Panel containing the menu
     */
    private JPanel m_menuPanel;
    /**
     * Label containing the main title
     */
    private JLabel m_mainTitle;
    /**
     * Label containing the second title
     */
    private JLabel m_secondTitle;
    /**
     * Close button of the app (menu)
     */
    private JButton m_closeB;
    /**
     * Fullscreen button (menu)
     */
    private JButton m_maximizeB;
    /**
     * Minimize the app into the taskbar (menu)
     */
    private JButton m_minimizeB;
    /**
     * Splitpanel betweenthe filetree and the right part
     */
    private JSplitPane m_mainContainer;
    /**
     * Splipanel between the InformationPanel and the LogsPanel
     */
    private JSplitPane m_rightContainer;
    /**
     * The main panel of left side of the jsplitpane
     */
    private JPanel m_leftContainer;
    /**
     * Panel containing the activityPanel and the progressBar
     */
    private JPanel m_leftSouthContainer;
    /**
     * The progressbar displaying the progress of the program
     */
    private ProgressBar m_progessBar;
    /**
     * The panel containing the tree of files downloaded
     */
    private TreePanel m_treePanel;
    /**
     * Thepanel containing the information of a selected file
     */
    private InformationPanel m_informationsPanel;
    /**
     * The panel containing the buttons to control the download
     */
    private ActivityPanel m_activityPanel;
    /**
     * The panel displaying the logs
     */
    private LogsPanel m_logsPanel;

    /**
     * Class constructor
     */
    private MainFrame() {
        super(s_TITLE);
        m_INITIAL_LOCATION = s_INITIAL_LOCATION;
        m_MIN_WIDTH = (int) s_INITIAL_DIMENSION.getWidth();
        m_MIN_HEIGHT = (int) s_INITIAL_DIMENSION.getHeight();
        initFrame();
        initComponents();
        initLayout();
        addComponents();
        swagComponents();
        addListener();
    }

    /**
     * Get the singleton
     *
     * @return the singleton
     */
    public static MainFrame getSingleton() {
        if (s_mainFrame == null) {
            s_mainFrame = new MainFrame();
        }
        return s_mainFrame;
    }

    /**
     * Get the current location of the mouse on the frame
     *
     * @param e     the event triggered
     * @param frame the frame to track
     * @return the point where the event is triggered
     */
    private static Point getScreenLocation(MouseEvent e, JFrame frame) {
        final Point cursor = e.getPoint();
        final Point view_location = frame.getLocationOnScreen();
        return new Point((int) (view_location.getX() + cursor.getX()), (int) (view_location.getY() + cursor.getY()));
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        moveOrFullResizeFrame(e);
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        final Point cursorLocation = e.getPoint();
        final int xPos = cursorLocation.x;
        final int yPos = cursorLocation.y;

        if (xPos >= s_CURSOR_AREA && xPos <= getWidth() - s_CURSOR_AREA && yPos >= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        else if (xPos >= getWidth() - s_CURSOR_AREA && yPos >= s_CURSOR_AREA && yPos <= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        else if (xPos <= s_CURSOR_AREA && yPos >= s_CURSOR_AREA && yPos <= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        else if (xPos >= s_CURSOR_AREA && xPos <= getWidth() - s_CURSOR_AREA && yPos <= s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        else if (xPos <= s_CURSOR_AREA && yPos <= s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        else if (xPos >= getWidth() - s_CURSOR_AREA && yPos <= s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        else if (xPos >= getWidth() - s_CURSOR_AREA && yPos >= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        else if (xPos <= s_CURSOR_AREA && yPos >= getHeight() - s_CURSOR_AREA)
            setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
        else
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        final Object sourceObject = e.getSource();
        if (sourceObject instanceof JPanel) {
            if (e.getClickCount() == 2) {
                if (getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))
                    doubleClicktoFullScreen();
            }
        }
    }

    /**
     * Reset JTree
     */
    public void resetTree() {
        SwingUtilities.invokeLater(() -> m_treePanel.resetTree());
    }

    /**
     * Update JTree
     *
     * @param _path the path use to updateTree JTree
     */
    public void updateTree(String _path) {
        SwingUtilities.invokeLater(() -> m_treePanel.update(_path));
    }

    /**
     * Display log
     *
     * @param _log to display
     */
    public void updateLog(String _log, Logs.Type _type) {
        SwingUtilities.invokeLater(() -> m_logsPanel.updateLog(_log, _type));
    }

    /**
     * Update the value of the progress bar
     *
     * @param _value the value to set
     */
    public void updateProgresseValue(int _value) {
        SwingUtilities.invokeLater(() -> m_progessBar.setValue(_value));
    }

    /**
     * Update the information to display
     *
     * @param _component the component to set
     */
    public void updateInformation(JComponent _component) {
        SwingUtilities.invokeLater(() -> m_informationsPanel.updateInformation(_component));
    }

    /**
     * Update the maximum of the progress bar
     *
     * @param _max the max to set
     */
    public void updateProgresseMax(int _max) {
        SwingUtilities.invokeLater(() -> m_progessBar.setMaximum(_max));
    }

    /**
     * Add tree action
     *
     * @param _treeListener the start action
     */
    public void addTreeListener(TreePanel.TreeListener _treeListener) {
        SwingUtilities.invokeLater(() -> m_treePanel.addTreeListener(_treeListener));
    }

    /**
     * Add start action
     *
     * @param _activityListener the start action
     */
    public void addStartListener(ActivityPanel.ActivityListener _activityListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.addtStartListener(_activityListener));
    }

    /**
     * Add stop action
     *
     * @param _activityListener the stop action
     */
    public void addStopListener(ActivityPanel.ActivityListener _activityListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.addStopListener(_activityListener));
    }

    /**
     * Add pause action
     *
     * @param _activityListener the pause action
     */
    public void addPauseListener(ActivityPanel.ActivityListener _activityListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.addPauseListener(_activityListener));
    }

    /**
     * Add resume action
     *
     * @param _activityListener the resume action
     */
    public void addResumeListener(ActivityPanel.ActivityListener _activityListener) {
        SwingUtilities.invokeLater(() -> m_activityPanel.addResumeListener(_activityListener));
    }

    /**
     * Drag and drop the Frame
     */
    @Override
    public void mousePressed(MouseEvent e) {
        m_start_drag = getScreenLocation(e, this);
        m_start_loc = getLocation();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Basic frame inits
     */
    private void initFrame() {
        addMouseMotionListener(this);
        addMouseListener(this);
        setSize(m_MIN_WIDTH, m_MIN_HEIGHT);
        setLocation(m_INITIAL_LOCATION);
        setUndecorated(true);
        setSize((s_DIM.width / 2), (s_DIM.height / 2));
        initIcone();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    /**
     * Initialize the components
     */
    private void initComponents() {
        m_header = new JPanel();
        m_main = new JPanel();
        m_footer = new TitlePanel("Application cree par -- Romain M. -- Florian H. -- Vincent H. -- Sami F. -- Arthur D.  -- Romain T. -- Adele M.", 12, s_DARKGRAY);

        m_menuPanel = new JPanel();
        m_mainTitle = new JLabel("GENOME");
        m_secondTitle = new JLabel("Statistiques sur les genes de la base GenBank");

        m_closeB = new JButton();
        m_maximizeB = new JButton();
        m_minimizeB = new JButton();

        m_progessBar = new ProgressBar();
        m_treePanel = new TreePanel();
        m_informationsPanel = new InformationPanel();
        m_logsPanel = new LogsPanel();
        m_activityPanel = new ActivityPanel();

        m_leftContainer = new JPanel();
        m_leftSouthContainer = new JPanel();
        m_rightContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_informationsPanel, m_logsPanel);
        m_mainContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_leftContainer, m_rightContainer);
    }

    /**
     * Initialize layout types
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        m_main.setLayout(new BorderLayout());
        m_header.setLayout(new BorderLayout());

        m_menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        m_leftContainer.setLayout(new BorderLayout());
        m_leftSouthContainer.setLayout(new BorderLayout());
    }

    /**
     * Add components into layouts
     */
    private void addComponents() {
        super.add(m_header, BorderLayout.NORTH);
        super.add(m_main, BorderLayout.CENTER);
        super.add(m_footer, BorderLayout.SOUTH);

        m_header.add(m_menuPanel, BorderLayout.NORTH);
        m_header.add(m_mainTitle, BorderLayout.CENTER);
        m_header.add(m_secondTitle, BorderLayout.SOUTH);

        m_menuPanel.add(m_minimizeB);
        m_menuPanel.add(m_maximizeB);
        m_menuPanel.add(m_closeB);

        m_main.add(m_mainContainer, BorderLayout.CENTER);
        m_leftContainer.add(m_treePanel, BorderLayout.CENTER);
        m_leftContainer.add(m_leftSouthContainer, BorderLayout.SOUTH);

        m_leftSouthContainer.add(m_progessBar, BorderLayout.NORTH);
        m_leftSouthContainer.add(m_activityPanel, BorderLayout.SOUTH);
    }

    /**
     * Swag the interface
     */
    private void swagComponents() {
        getRootPane().setBorder(BorderFactory.createLineBorder(s_DARKGRAY, 10));

        m_menuPanel.setBackground(s_DARKGRAY);
        m_mainTitle.setFont(new Font(s_FONT, Font.PLAIN, 28));
        m_mainTitle.setForeground(s_WHITE);
        m_secondTitle.setFont(new Font(s_FONT, Font.PLAIN, 18));
        m_secondTitle.setForeground(s_BLUE);

        swagMenuButton(m_closeB, "Ressources/close.png");
        swagMenuButton(m_minimizeB, "Ressources/minimize.png");
        swagMenuButton(m_maximizeB, "Ressources/maximize.png");

        m_header.setBackground(s_DARKGRAY);
        m_main.setBackground(s_LIGHTGRAY);
        m_main.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));

        m_mainContainer.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return (new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(s_DARKGRAY);
                        Dimension dim = splitPane.getSize();
                        g.fillRect(0, 0, dim.width, dim.height);
                    }
                });
            }
        });
        m_mainContainer.setDividerSize(3);
        m_mainContainer.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        m_mainContainer.setDividerLocation(.25d);
        m_mainContainer.setResizeWeight(0.25d);

        m_rightContainer.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return (new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(s_DARKGRAY);
                        Dimension dim = splitPane.getSize();
                        g.fillRect(0, 0, dim.width, dim.height);
                    }
                });
            }
        });
        m_rightContainer.setDividerSize(3);
        m_rightContainer.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        m_rightContainer.setDividerLocation(.5d);
        m_rightContainer.setResizeWeight(0.5d);

        m_leftContainer.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));

        m_activityPanel.setPreferredSize(new Dimension(120, 30));
    }

    /**
     * Swag menu button
     *
     * @param _button button to swag
     * @param _path   path to the icon
     */
    private void swagMenuButton(JButton _button, String _path) {
        _button.setMargin(s_INSETS);
        _button.setBackground(s_DARKGRAY);
        _button.setForeground(s_WHITE);
        _button.setFont(new Font(s_FONT, Font.BOLD, 16));
        _button.setBorder(BorderFactory.createEmptyBorder());
        _button.setHorizontalAlignment(SwingConstants.CENTER);
        _button.setVerticalAlignment(SwingConstants.CENTER);
        _button.setPreferredSize(new Dimension(30, 30));

        _button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                _button.setBackground(s_BLUEGRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                _button.setBackground(null);
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
            _button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            Logs.exception(e);
        }
    }

    /**
     * Add listener on components
     * Manage the action to do when a listener is triggered
     */
    private void addListener() {
        m_menuPanel.addMouseListener(this);
        m_menuPanel.addMouseMotionListener(this);

        m_closeB.addActionListener(e -> System.exit(0));
        m_minimizeB.addActionListener(e -> setState(Frame.ICONIFIED));
        m_maximizeB.addActionListener(e -> {
            if (getExtendedState() == MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }

    /**
     * Move or fullscreen the frame depending of the event triggered
     *
     * @param e the event triggered
     */
    private void moveOrFullResizeFrame(MouseEvent e) {
        final Object sourceObject = e.getSource();
        final Point current = getScreenLocation(e, this);
        final Point offset = new Point((int) current.getX() - (int) m_start_drag.getX(), (int) current.getY() - (int) m_start_drag.getY());

        if (sourceObject instanceof JPanel
                && getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))
            setLocation((int) (m_start_loc.getX() + offset.getX()), (int) (m_start_loc.getY() + offset.getY()));
        else if (!getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))) {
            final int oldLocationX = (int) getLocation().getX();
            final int oldLocationY = (int) getLocation().getY();
            int newLocationX = (int) (m_start_loc.getX() + offset.getX());
            int newLocationY = (int) (m_start_loc.getY() + offset.getY());
            final boolean N_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            final boolean NE_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
            final boolean NW_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            final boolean E_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            final boolean W_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            final boolean S_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            final boolean SW_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
            boolean setLocation = false;
            int newWidth = e.getX();
            int newHeight = e.getY();

            if (NE_Resize) {
                newHeight = getHeight() - (newLocationY - oldLocationY);
                newLocationX = (int) getLocation().getX();
                setLocation = true;
            } else if (E_Resize) {
                newHeight = getHeight();
            } else if (S_Resize) {
                newWidth = getWidth();
            } else if (N_Resize) {
                newLocationX = (int) getLocation().getX();
                newWidth = getWidth();
                newHeight = getHeight() - (newLocationY - oldLocationY);
                setLocation = true;
            } else if (NW_Resize) {
                newWidth = getWidth() - (newLocationX - oldLocationX);
                newHeight = getHeight() - (newLocationY - oldLocationY);
                setLocation = true;
            } else if (SW_Resize) {
                newWidth = getWidth() - (newLocationX - oldLocationX);
                newLocationY = (int) getLocation().getY();
                setLocation = true;
            }

            if (W_Resize) {
                newWidth = getWidth() - (newLocationX - oldLocationX);
                newLocationY = (int) getLocation().getY();
                newHeight = getHeight();
                setLocation = true;
            }

            if (newWidth >= (int) s_TOOLKIT.getScreenSize().getWidth() || newWidth <= m_MIN_WIDTH) {
                newLocationX = oldLocationX;
                newWidth = getWidth();
            }

            if (newHeight >= (int) s_TOOLKIT.getScreenSize().getHeight() - 30 || newHeight <= m_MIN_HEIGHT) {
                newLocationY = oldLocationY;
                newHeight = getHeight();
            }

            if (newWidth != getWidth() || newHeight != getHeight()) {
                setSize(newWidth, newHeight);
                if (setLocation) {
                    setLocation(newLocationX, newLocationY);
                }
            }
        }
    }

    /**
     * Double-Click to fullscreen the Frame or to go back to the previous size of the frame
     */
    private void doubleClicktoFullScreen() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}

