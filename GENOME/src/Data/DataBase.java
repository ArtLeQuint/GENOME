package Data;

import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Logs;
import Utils.Options;

import java.io.File;
import java.util.ArrayList;

public final class DataBase extends IDataBase {

    /**
     * Prefix used for serialization
     */
    static final String s_SERIALIZATION_PREFIX = Options.getDatabaseSerializationPrefix();
    /**
     * Array of this Database's Kingdom
     */
    private transient final ArrayList<Kingdom> m_KINGDOM;
    /**
     * Event to call when compute are finished
     */
    private transient final IDataBaseCallback m_EVENT;

    /**
     * Class constructor
     *
     * @param _name, the name of this DataBase
     * @param _event the event call when compute is finished
     */
    private DataBase(String _name, IDataBaseCallback _event) {
        super(_name);
        m_KINGDOM = new ArrayList<>();
        m_EVENT = _event;
    }

    /**
     * Class constructor when already exist
     *
     * @param _name, the name of this DataBase
     * @param _data, the previous version of this DataBase
     * @param _event the event call when compute is finished
     */
    private DataBase(String _name, IDataBase _data, IDataBaseCallback _event) {
        super(_name, _data);
        m_KINGDOM = new ArrayList<>();
        m_EVENT = _event;
    }

    /**
     * Load a DataBase with his name and affect the event
     * It create it if the file doesn't exist
     *
     * @param _name  the name of the file to load
     * @param _event the Callback you want to apply
     * @return the DataBase loaded or created
     */
    public static DataBase load(String _name, IDataBaseCallback _event) {
        final File saveFolder = new File(Options.getSerializeDirectory());
        if (!saveFolder.exists()) {
            if (!saveFolder.mkdir()) {
                Logs.warning("Enable to create folder : " + saveFolder.getName());
            }
        }
        final IDataBase result = IDataBase.load(s_SERIALIZATION_PREFIX + _name);

        if (result == null) {
            return new DataBase(_name, _event);
        } else {
            return new DataBase(_name, result, _event);
        }
    }

    /**
     * Get the Kingdoms of this DataBase
     *
     * @return the m_KINGDOM
     */
    public ArrayList<Kingdom> getKingdoms() {
        return m_KINGDOM;
    }

    /**
     * In case of all Kingdom are already finished
     *
     * @throws InvalidStateException if it can't be stopped
     */
    @Override
    public synchronized void stop() throws InvalidStateException {
        super.stop();
        if (super.getFinishedChildren() == m_KINGDOM.size()) {
            end();
        }
    }

    /**
     * Get the main part of the save path_name
     *
     * @return the main part of the save path_name
     */
    @Override
    public String getSavedName() {
        return s_SERIALIZATION_PREFIX + getName();
    }

    /**
     * Finish this DataBase if it can
     *
     * @param _kingdom, the Kingdom to finish
     * @throws InvalidStateException if it can't be finished
     */
    synchronized void finish(Kingdom _kingdom) throws InvalidStateException {
        if (super.contains(m_KINGDOM, _kingdom) && _kingdom.getState() != State.FINISHED) {
            for (Statistics stat : _kingdom.getStatistics().values()) {
                super.updateStatistics(stat);
                super.incrementGenomeNumber(stat.getType(), _kingdom.getTypeNumber(stat.getType()));
            }
            super.incrementGenericTotals(_kingdom);
            incrementFinishedChildren();
            if (super.getState() == IDataBase.State.STOPPED && super.getFinishedChildren() == m_KINGDOM.size()) {
                end();
            }
        }
    }

    /**
     * Add a Kingdom to the Database
     *
     * @param _kingdom, the Kingdom to insert
     * @throws AddException if _kingdom are already added
     */
    void addKingdom(Kingdom _kingdom) throws AddException {
        if (super.getState() == State.STARTED) {
            if (super.contains(m_KINGDOM, _kingdom))
                throw new AddException("Kingdom already added : " + _kingdom.getName());
            _kingdom.setIndex(m_KINGDOM.size());
            _kingdom.setParent(this);
            m_KINGDOM.add(_kingdom);
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
        super.finish();
        m_KINGDOM.clear();
        super.clear();
    }

}
