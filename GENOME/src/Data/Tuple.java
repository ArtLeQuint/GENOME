package Data;

import java.io.Serializable;
import java.util.Arrays;

public final class Tuple implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Array to store float
     */
    private final float[] m_FLOATS;
    /**
     * Array to store long
     */
    private final long[] m_LONGS;

    /**
     * Class constructor
     */
    Tuple() {
        m_LONGS = new long[Statistics.StatLong.values().length];
        Arrays.fill(m_LONGS, 0L);
        m_FLOATS = new float[Statistics.StatFloat.values().length];
        Arrays.fill(m_FLOATS, 0.F);
    }

    /**
     * Get value from the enum
     *
     * @param _stat, the enum
     * @return the float get from the enum
     */
    public float get(Statistics.StatFloat _stat) {
        return m_FLOATS[_stat.ordinal()];
    }

    /**
     * Get value from the enum
     *
     * @param _stat, the enum
     * @return the long get from the enum
     */
    public long get(Statistics.StatLong _stat) {
        return m_LONGS[_stat.ordinal()];
    }

    /**
     * Set value of the enum
     *
     * @param _stat, the enum
     * @param _val,  the value to set
     */
    void set(Statistics.StatFloat _stat, float _val) {
        m_FLOATS[_stat.ordinal()] = _val;
    }

    /**
     * Increment the value of the enum
     *
     * @param _stat, the enum
     * @param _val,  the value to increment
     */
    void incr(Statistics.StatLong _stat, long _val) {
        m_LONGS[_stat.ordinal()] += _val;
    }

    /**
     * Decrement the value of the enum
     *
     * @param _stat, the enum
     * @param _val,  the value to decrement
     */
    void decr(Statistics.StatLong _stat, long _val) {
        m_LONGS[_stat.ordinal()] -= _val;
    }

}
