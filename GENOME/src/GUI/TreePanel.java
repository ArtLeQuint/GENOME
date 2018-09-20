package GUI;

import Utils.Options;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

import static GUI.Constant.*;

public final class TreePanel extends IPanel {

    private static final String s_TITLE = "Arborescence des fichiers";

    private DefaultTreeModel m_treeModel;
    private JTree m_tree;
    private ScrollPanel m_scrollPane;
    private JPanel m_legend;

    private JPanel m_orangeContainer;
    private Circle m_orange;
    private JLabel m_orangeLabel;

    private JPanel m_blueContainer;
    private Circle m_blue;
    private JLabel m_blueLabel;

    private JPanel m_greenContainer;
    private Circle m_green;
    private JLabel m_greenLabel;

    private JPanel m_container;

    /**
     * Class constructor
     */
    TreePanel() {
        super(s_TITLE);
    }

    /**
     * Create the components of the panel
     */
    protected void createComponent() {
        m_container = new JPanel();
        m_legend = new JPanel();

        m_orangeContainer = new JPanel();
        m_orange = new Circle(s_ORANGE);
        m_orangeLabel = new JLabel("actualisé");

        m_blueContainer = new JPanel();
        m_blue = new Circle(s_BLUE);
        m_blueLabel = new JLabel("créé");

        m_greenContainer = new JPanel();
        m_green = new Circle(s_GREEN);
        m_greenLabel = new JLabel("terminé");

        m_tree = new JTree();
        m_scrollPane = new ScrollPanel(m_tree);

        new SmartScrollComponent(m_scrollPane);
    }

    /**
     * Init the layouts of the panel
     */
    protected void initLayout() {
        m_container.setLayout(new BorderLayout());
        m_orangeContainer.setLayout(new BorderLayout());
        m_blueContainer.setLayout(new BorderLayout());
        m_greenContainer.setLayout(new BorderLayout());
        m_legend.setLayout(new GridLayout(1, 3));
    }

    /**
     * Add the components into the panel
     */
    protected void addComponents() {
        m_container.add(m_scrollPane, BorderLayout.CENTER);
        m_container.add(m_legend, BorderLayout.NORTH);

        m_legend.add(m_orangeContainer);

        m_orangeContainer.add(m_orange, BorderLayout.WEST);
        m_orangeContainer.add(m_orangeLabel, BorderLayout.CENTER);

        m_legend.add(m_blueContainer);

        m_blueContainer.add(m_blue, BorderLayout.WEST);
        m_blueContainer.add(m_blueLabel, BorderLayout.CENTER);

        m_legend.add(m_greenContainer);

        m_greenContainer.add(m_green, BorderLayout.WEST);
        m_greenContainer.add(m_greenLabel, BorderLayout.CENTER);

        super.add(m_container);
    }

    /**
     * Make components of the panel pretty
     */
    protected void swagComponent() {
        final DefaultMutableTreeNode root = loadTree();
        m_treeModel = new DefaultTreeModel(root);

        m_tree.setModel(m_treeModel);
        m_tree.setRootVisible(false);
        m_tree.setShowsRootHandles(true);
        m_tree.setBackground(s_LIGHTGRAY);

        m_tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
                Node node = (Node) ((DefaultMutableTreeNode) value).getUserObject();

                switch (node.getState()) {
                    case CREATE:
                        setForeground(s_BLUE);
                        break;
                    case UPDATE:
                        setForeground(s_ORANGE);
                        break;
                    case FINISH:
                        setForeground(s_GREEN);
                        break;
                    case DEFAULT:
                        break;
                }

                return this;
            }
        });

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) m_tree.getCellRenderer();
        renderer.setTextSelectionColor(s_WHITE);
        renderer.setBackgroundSelectionColor(s_BLUEGRAY);
        renderer.setBackgroundNonSelectionColor(s_LIGHTGRAY);
        renderer.setTextNonSelectionColor(s_WHITE);
        renderer.setBorderSelectionColor(s_BLACK);
        renderer.setLeafIcon(null);

        m_scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        m_orange.setPreferredSize(new Dimension(30, 30));
        m_orangeLabel.setFont(new Font(s_FONT, Font.PLAIN, 15));
        m_orangeLabel.setForeground(s_WHITE);
        m_orangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_orangeContainer.setBackground(s_LIGHTGRAY);

        m_blue.setPreferredSize(new Dimension(30, 30));
        m_blueLabel.setFont(new Font(s_FONT, Font.PLAIN, 15));
        m_blueLabel.setForeground(s_WHITE);
        m_blueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_blueContainer.setBackground(s_LIGHTGRAY);

        m_green.setPreferredSize(new Dimension(30, 30));
        m_greenLabel.setFont(new Font(s_FONT, Font.PLAIN, 15));
        m_greenLabel.setForeground(s_WHITE);
        m_greenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_greenContainer.setBackground(s_LIGHTGRAY);

        m_legend.setBackground(s_LIGHTGRAY);
        m_legend.setBorder(BorderFactory.createLineBorder(s_CHARCOAL));
        m_legend.setMinimumSize(new Dimension(250, 30));
    }

    /**
     * Add listener
     *
     * @param _treeListener action to call
     */
    void addTreeListener(TreeListener _treeListener) {
        m_tree.addTreeSelectionListener(e -> _treeListener.treeEvent(getFileName(e.getPath())));
    }

    /**
     * Update tree with nwe path
     *
     * @param _path the path to add
     */
    synchronized void update(String _path) {
        String table[] = _path.split(Options.getSerializationSpliter());
        String temp;
        DefaultMutableTreeNode actual = (DefaultMutableTreeNode) m_treeModel.getRoot(), progressing;
        Boolean found;
        if (table.length >= 2) {
            if (!table[0].startsWith(Options.getDatabaseSerializationPrefix())) {
                return;
            }
            temp = table[0].substring(2, table[0].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(new Node(temp, State.CREATE));
                actual.add(progressing);
                m_treeModel.reload();
            } else {
                if (table.length == 2) {
                    ((Node) progressing.getUserObject()).setState(State.FINISH);
                    m_treeModel.nodeChanged(progressing);
                } else if (((Node) progressing.getUserObject()).getState() == State.DEFAULT) {
                    ((Node) progressing.getUserObject()).setState(State.UPDATE);
                    m_treeModel.nodeChanged(progressing);
                }
            }
            actual = progressing;
        }
        if (table.length >= 3) {
            if (!table[1].startsWith(Options.getKingdomSerializationPrefix())) {
                return;
            }
            temp = table[1].substring(2, table[1].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(new Node(temp, State.CREATE));
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            } else {
                if (table.length == 3) {
                    ((Node) progressing.getUserObject()).setState(State.FINISH);
                    m_treeModel.nodeChanged(progressing);
                } else if (((Node) progressing.getUserObject()).getState() == State.DEFAULT) {
                    ((Node) progressing.getUserObject()).setState(State.UPDATE);
                    m_treeModel.nodeChanged(progressing);
                }
            }
            actual = progressing;
        }
        if (table.length >= 4) {
            if (!table[2].startsWith(Options.getGroupSerializationPrefix())) {
                return;
            }
            temp = table[2].substring(2, table[2].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(new Node(temp, State.CREATE));
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            } else {
                if (table.length == 4) {
                    ((Node) progressing.getUserObject()).setState(State.FINISH);
                    m_treeModel.nodeChanged(progressing);
                } else if (((Node) progressing.getUserObject()).getState() == State.DEFAULT) {
                    ((Node) progressing.getUserObject()).setState(State.UPDATE);
                    m_treeModel.nodeChanged(progressing);
                }
            }
            actual = progressing;
        }
        if (table.length >= 5) {
            if (!table[3].startsWith(Options.getSubGroupSerializationPrefix())) {
                return;
            }
            temp = table[3].substring(3, table[3].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(new Node(temp, State.CREATE));
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            } else {
                if (table.length == 5) {
                    ((Node) progressing.getUserObject()).setState(State.FINISH);
                    m_treeModel.nodeChanged(progressing);
                } else if (((Node) progressing.getUserObject()).getState() == State.DEFAULT) {
                    ((Node) progressing.getUserObject()).setState(State.UPDATE);
                    m_treeModel.nodeChanged(progressing);
                }
            }
            actual = progressing;
        }
        if (table.length >= 6) {
            if (!table[4].startsWith(Options.getOrganismSerializationPrefix())) {
                return;
            }
            temp = table[4].substring(2, table[4].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(new Node(temp, State.FINISH));
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            } else {
                ((Node) progressing.getUserObject()).setState(State.FINISH);
                m_treeModel.nodeChanged(progressing);
            }
        }
    }

    /**
     * Reset tree node status
     */
    void resetTree() {
        DefaultMutableTreeNode actual = ((DefaultMutableTreeNode) m_treeModel.getRoot()).getNextNode();
        while (actual != null) {
            ((Node) actual.getUserObject()).setState(State.DEFAULT);
            m_treeModel.nodeChanged(actual);
            actual = actual.getNextNode();
        }
    }

    /**
     * Initial load
     *
     * @return the default mutable tree node
     */
    private DefaultMutableTreeNode loadTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Node("root", State.DEFAULT));
        File path = new File(Options.getSerializeDirectory());
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                Arrays.sort(files);
                DefaultMutableTreeNode db = new DefaultMutableTreeNode("");
                DefaultMutableTreeNode kg = new DefaultMutableTreeNode("");
                DefaultMutableTreeNode gp = new DefaultMutableTreeNode("");
                DefaultMutableTreeNode su = new DefaultMutableTreeNode("");
                for (File f : files) {
                    if (f.getName().endsWith(Options.getDateModifSerializeExtension())) {
                        continue;
                    }

                    String table[] = f.getName().split(Options.getSerializationSpliter());
                    String temp;
                    if (table.length >= 2) {
                        if (!table[0].startsWith(Options.getDatabaseSerializationPrefix())) {
                            continue;
                        }
                        temp = table[0].substring(2, table[0].length()).replaceAll("_", " ");
                        if (!temp.equals(db.toString())) {
                            db = new DefaultMutableTreeNode(new Node(temp, State.DEFAULT));
                            root.add(db);
                        }
                    }
                    if (table.length >= 3) {
                        if (!table[1].startsWith(Options.getKingdomSerializationPrefix())) {
                            continue;
                        }
                        temp = table[1].substring(2, table[1].length()).replaceAll("_", " ");
                        if (!temp.equals(kg.toString())) {
                            kg = new DefaultMutableTreeNode(new Node(temp, State.DEFAULT));
                            db.add(kg);
                        }
                    }
                    if (table.length >= 4) {
                        if (!table[2].startsWith(Options.getGroupSerializationPrefix())) {
                            continue;
                        }
                        temp = table[2].substring(2, table[2].length()).replaceAll("_", " ");
                        if (!temp.equals(gp.toString())) {
                            gp = new DefaultMutableTreeNode(new Node(temp, State.DEFAULT));
                            kg.add(gp);
                        }
                    }
                    if (table.length >= 5) {
                        if (!table[3].startsWith(Options.getSubGroupSerializationPrefix())) {
                            continue;
                        }
                        temp = table[3].substring(3, table[3].length()).replaceAll("_", " ");
                        if (!temp.equals(su.toString())) {
                            su = new DefaultMutableTreeNode(new Node(temp, State.DEFAULT));
                            gp.add(su);
                        }
                    }
                    if (table.length >= 6) {
                        if (!table[4].startsWith(Options.getOrganismSerializationPrefix())) {
                            continue;
                        }
                        temp = table[4].substring(2, table[4].length()).replaceAll("_", " ");
                        su.add(new DefaultMutableTreeNode(new Node(temp, State.DEFAULT)));
                    }
                }
            }
        }
        return root;

    }

    /**
     * Get file real path
     *
     * @param _path the path
     * @return the real path
     */
    private String getFileName(TreePath _path) {
        String result = "";
        Object[] table = _path.getPath();
        if (table.length >= 2)
            result += Options.getDatabaseSerializationPrefix() + table[1].toString().replaceAll(" ", "_");
        if (table.length >= 3)
            result += Options.getSerializationSpliter() + Options.getKingdomSerializationPrefix() + table[2].toString().replaceAll(" ", "_");
        if (table.length >= 4)
            result += Options.getSerializationSpliter() + Options.getGroupSerializationPrefix() + table[3].toString().replaceAll(" ", "_");
        if (table.length >= 5)
            result += Options.getSerializationSpliter() + Options.getSubGroupSerializationPrefix() + table[4].toString().replaceAll(" ", "_");
        if (table.length >= 6)
            result += Options.getSerializationSpliter() + Options.getOrganismSerializationPrefix() + table[5].toString().replaceAll(" ", "_");
        return result;
    }

    /**
     * State of each node
     */
    private enum State {
        CREATE,
        UPDATE,
        FINISH,
        DEFAULT
    }

    /**
     * Tree listener
     */
    public interface TreeListener {
        void treeEvent(String _path);
    }

    private class Node {

        private final String m_name;
        private State m_state;

        private Node(String _name, State _state) {
            m_name = _name;
            m_state = _state;
        }

        @Override
        public String toString() {
            return m_name;
        }

        private State getState() {
            return m_state;
        }

        private void setState(State _state) {
            m_state = _state;
        }

    }

    private class Circle extends JPanel {

        private final Color m_color;

        private Circle(Color _color) {
            super();
            m_color = _color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(m_color);
            int sx = 12, sy = 12;
            g2.fillOval(getWidth() / 2 - sx / 2, getHeight() / 2 - sy / 2, sx, sy);
            g2.dispose();
            setBackground(m_color);
        }
    }
}
