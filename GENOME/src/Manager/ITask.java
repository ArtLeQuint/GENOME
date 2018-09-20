package Manager;

public abstract class ITask implements Runnable {

    /**
     * ITask's name
     */
    private final String m_NAME;

    /**
     * Class constructor
     *
     * @param _name, the name
     */
    public ITask(String _name) {
        m_NAME = _name;
    }

    /**
     * Get the name of this ITask
     *
     * @return the name
     */
    public String getName() {
        return m_NAME;
    }

    /**
     * Task to execute
     */
    @Override
    public abstract void run();

    /**
     * Task to cancel
     */
    public abstract void cancel();
}
