package Data;

import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Options;

import java.util.ArrayList;

public final class SubGroup extends IDataBase {

    /**
     * Prefix used for serialization
     */
    static final String s_SERIALIZATION_PREFIX = Options.getSerializationSpliter() + Options.getSubGroupSerializationPrefix();
    /**
     * Array of this SubGroup's Organisms
     */
    private transient final ArrayList<Organism> m_ORGANISM;
    /**
     * Event to call when compute are finished
     */
    private transient final ISubGroupCallback m_EVENT;
    /**
     * Reference to the parent
     */
    private transient Group m_parent;

    /**
     * Class constructor
     *
     * @param _name, the name of this SubGroup
     * @param _event the event call when compute is finished
     */
    private SubGroup(String _name, ISubGroupCallback _event) {
        super(_name);
        m_ORGANISM = new ArrayList<>();
        m_parent = null;
        m_EVENT = _event;
    }

    /**
     * Class constructor when already exist
     *
     * @param _name, the name of this SubGroup
     * @param _data, the previous version of this SubGroup
     * @param _event the event call when compute is finished
     */
    private SubGroup(String _name, IDataBase _data, ISubGroupCallback _event) {
        super(_name, _data);
        m_ORGANISM = new ArrayList<>();
        m_parent = null;
        m_EVENT = _event;
    }

    /**
     * Load a Subgroup with his name and affect the event
     * It create it if the file doesn't exist
     *
     * @param _name   the name of the file to load
     * @param _parent the parent Group (used to know the path_name)
     * @param _event  the Callback you want to apply
     * @return the Subgroup loaded or created
     */
    public static SubGroup load(String _name, Group _parent, ISubGroupCallback _event) throws AddException, InvalidStateException {
        final IDataBase lastOne = IDataBase.load(_parent.getSavedName() + s_SERIALIZATION_PREFIX + _name);
        final SubGroup newOne;
        if (lastOne == null) {
            newOne = new SubGroup(_name, _event);
        } else {
            newOne = new SubGroup(_name, lastOne, _event);
            _parent.unload(newOne);
        }
        _parent.addSubGroup(newOne);
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
            throw new InvalidStateException("Unable to start without been add in a Group : " + getName());
        super.start();
    }

    /**
     * In case of all Organism are already finished
     *
     * @throws InvalidStateException if it can't be sopped
     */
    @Override
    public synchronized void stop() throws InvalidStateException {
        super.stop();
        if (super.getFinishedChildren() == m_ORGANISM.size()) {
            end();
        }
    }

    /**
     * Get the Organism of this SubGroup
     *
     * @return the m_groups
     */
    public ArrayList<Organism> getOrganisms() {
        return m_ORGANISM;
    }

    /**
     * Get the Group's name
     *
     * @return the Group's name
     */
    public String getGroupName() {
        return m_parent.getName();
    }

    /**
     * Get the Kingdom's name
     *
     * @return the Kingdom's name
     */
    public String getKingdomName() {
        return m_parent.getKingdomName();
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
     * @param _group, the parent to set
     */
    void setParent(Group _group) {
        m_parent = _group;
    }

    /**
     * Finish this Subgroup if it can
     *
     * @param _organism, the Organism to finish
     * @throws InvalidStateException if it can't be finished
     */
    synchronized void finish(Organism _organism) throws InvalidStateException {
        if (super.contains(m_ORGANISM, _organism) && _organism.getState() != State.FINISHED) {
            for (Statistics stat : _organism.getStatistics().values()) {
                super.updateStatistics(stat);
                super.incrementGenomeNumber(stat.getType(), _organism.getTypeNumber(stat.getType()));
            }
            super.incrementGenericTotals(_organism);
            super.incrementFinishedChildren();
            if (super.getState() == State.STOPPED && super.getFinishedChildren() == m_ORGANISM.size()) {
                end();
            }
        }
    }

    /**
     * Add an Organism to this SubGroup
     *
     * @param _organism, the Organism to insert
     * @throws AddException if _organism are already added
     */
    void addOrganism(Organism _organism) throws AddException {
        if (super.getState() == State.STARTED) {
            if (super.contains(m_ORGANISM, _organism))
                throw new AddException("Organism already added : " + _organism.getName());
            _organism.setIndex(m_ORGANISM.size());
            _organism.setParent(this);
            m_ORGANISM.add(_organism);
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
        m_ORGANISM.clear();
        super.clear();
    }
}
