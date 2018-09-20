package Data;

import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Options;

import java.util.ArrayList;

public final class Kingdom extends IDataBase {

    /**
     * Prefix used for serialization
     */
    static final String s_SERIALIZATION_PREFIX = Options.getSerializationSpliter() + Options.getKingdomSerializationPrefix();
    /**
     * Array of this Kingdom's Group
     */
    private transient final ArrayList<Group> m_GROUPS;
    /**
     * Event to call when compute are finished
     */
    private transient final IKingdomCallback m_EVENT;
    /**
     * Reference to the parent
     */
    private transient DataBase m_parent;

    /**
     * Class constructor
     *
     * @param _name  the name of this Kingdom
     * @param _event the event call when compute is finished
     */
    private Kingdom(String _name, IKingdomCallback _event) {
        super(_name);
        m_GROUPS = new ArrayList<>();
        m_parent = null;
        m_EVENT = _event;
    }

    /**
     * Class constructor when already exist
     *
     * @param _name, the name of this Kingdom
     * @param _data, the previous version of this Kingdom
     * @param _event the event call when compute is finished
     */
    private Kingdom(String _name, IDataBase _data, IKingdomCallback _event) {
        super(_name, _data);
        m_GROUPS = new ArrayList<>();
        m_parent = null;
        m_EVENT = _event;
    }

    /**
     * Load a Kingdom with his name and affect the event
     * It create it if the file doesn't exist
     *
     * @param _name   the name of the file to load
     * @param _parent the parent DataBase (used to know the path_name)
     * @param _event  the Callback you want to apply
     * @return the IDatabase loaded or created
     */
    public static Kingdom load(String _name, DataBase _parent, IKingdomCallback _event) throws AddException, InvalidStateException {
        final IDataBase lastOne = IDataBase.load(_parent.getSavedName() + s_SERIALIZATION_PREFIX + _name);
        final Kingdom newOne;
        if (lastOne == null) {
            newOne = new Kingdom(_name, _event);
        } else {
            newOne = new Kingdom(_name, lastOne, _event);
            _parent.unload(newOne);
        }
        _parent.addKingdom(newOne);
        return newOne;
    }

    /**
     * Start
     *
     * @throws InvalidStateException if it can't be started
     */
    @Override
    public synchronized void start() throws InvalidStateException {
        if (m_parent == null)
            throw new InvalidStateException("Unable to start without been add in a DataBase : " + getName());
        super.start();
    }

    /**
     * In case of all Group are already finished
     *
     * @throws InvalidStateException if it can't be stopped
     */
    @Override
    public synchronized void stop() throws InvalidStateException {
        super.stop();
        if (super.getFinishedChildren() == m_GROUPS.size()) {
            end();
        }
    }

    /**
     * Get the Group of this Kingdom
     *
     * @return the m_GROUPS
     */
    public ArrayList<Group> getGroups() {
        return m_GROUPS;
    }

    /**
     * Get the main part of the save path_name
     *
     * @return the main part of the save path_name
     */
    @Override
    public String getSavedName() {
        return m_parent.getSavedName() + s_SERIALIZATION_PREFIX + getName();
    }

    /**
     * Set the parent
     *
     * @param _dataBase, the parent to set
     */
    void setParent(DataBase _dataBase) {
        m_parent = _dataBase;
    }

    /**
     * Finish this Kingdom if it can
     *
     * @param _group, the Group to finish
     * @throws InvalidStateException if it can't be finished
     */
    synchronized void finish(Group _group) throws InvalidStateException {
        if (super.contains(m_GROUPS, _group) && _group.getState() != State.FINISHED) {
            for (Statistics stat : _group.getStatistics().values()) {
                super.updateStatistics(stat);
                super.incrementGenomeNumber(stat.getType(), _group.getTypeNumber(stat.getType()));
            }
            super.incrementGenericTotals(_group);
            super.incrementFinishedChildren();
            if (getState() == State.STOPPED && super.getFinishedChildren() == m_GROUPS.size()) {
                end();
            }
        }
    }

    /**
     * Add a Group to this Kingdom
     *
     * @param _group, the Group to insert
     * @throws AddException if _group are already added
     */
    void addGroup(Group _group) throws AddException {
        if (super.getState() == State.STARTED) {
            if (super.contains(m_GROUPS, _group))
                throw new AddException("Group already added : " + _group.getName());
            _group.setIndex(m_GROUPS.size());
            _group.setParent(this);
            m_GROUPS.add(_group);
        }
    }

    /**
     * Call callback and clear data
     *
     * @throws InvalidStateException if an exception appear
     */
    private void end() throws InvalidStateException {
        super.computeStatistics();
        m_EVENT.finish(this);
        m_parent.finish(this);
        super.finish();
        m_GROUPS.clear();
        super.clear();
    }

}
