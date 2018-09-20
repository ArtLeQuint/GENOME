package Data;

import java.io.Serializable;
import java.util.stream.IntStream;

public class Statistics implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * Type of this Statistic
     */
    private final Type m_TYPE;
    /**
     * Array to store trinucleotide statistics
     */
    private final Tuple[] m_TRINUCLEOTIDE_TABLE;
    /**
     * Array to store dinucleotide statistics
     */
    private final Tuple[] m_DINUCLEOTIDE_TABLE;
    /**
     * Number total of trinucleotide
     */
    private long m_totalTrinucleotide;
    /**
     * Number total of dinucleotide
     */
    private long m_totalDinucleotide;
    /**
     * The number of CDS sequences
     */
    private long m_CDSNumber;
    /**
     * The number of valid CDS sequences
     */
    private long m_validCDSNumber;

    /**
     * Class constructor
     */
    Statistics(Type _type) {
        m_TYPE = _type;
        m_TRINUCLEOTIDE_TABLE = new Tuple[Trinucleotide.values().length];
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> m_TRINUCLEOTIDE_TABLE[i] = new Tuple());
        m_DINUCLEOTIDE_TABLE = new Tuple[Dinucleotide.values().length];
        IntStream.range(0, Dinucleotide.values().length).parallel().forEach(i -> m_DINUCLEOTIDE_TABLE[i] = new Tuple());
        m_totalTrinucleotide = 0L;
        m_totalDinucleotide = 0L;
        m_CDSNumber = 0L;
        m_validCDSNumber = 0L;
    }

    /**
     * Get the type of this Replicon
     *
     * @return the type
     */
    public final Type getType() {
        return m_TYPE;
    }

    /**
     * get the total trinucleotide number
     *
     * @return the m_totalTrinucleotide
     */
    public final long getTotalTrinucleotide() {
        return m_totalTrinucleotide;
    }

    /**
     * get the total dinucleotide number
     *
     * @return the m_totalDinucleotide
     */
    public final long getTotalDinucleotide() {
        return m_totalDinucleotide;
    }

    /**
     * @return the m_TRINUCLEOTIDE_TABLE
     */
    public final Tuple[] getTriTable() {
        return m_TRINUCLEOTIDE_TABLE;
    }

    /**
     * @return the m_TRINUCLEOTIDE_TABLE
     */
    public final Tuple[] getDiTable() {
        return m_DINUCLEOTIDE_TABLE;
    }

    /**
     * Get the number of CDS
     *
     * @return the number of CDS
     */
    public final long getCDSNumber() {
        return m_CDSNumber;
    }

    /**
     * Get the number of valid CDS
     *
     * @return the number of valid CDS
     */
    public final long getValidCDSNumber() {
        return m_validCDSNumber;
    }

    /**
     * Update statistics
     *
     * @param _stats, the stats use to update
     */
    final void update(Statistics _stats) {
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_TRINUCLEOTIDE_TABLE[i];
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });
        IntStream.range(0, Dinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_DINUCLEOTIDE_TABLE[i];
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
        });
        m_totalTrinucleotide += _stats.m_totalTrinucleotide;
        m_totalDinucleotide += _stats.m_totalDinucleotide;
        m_CDSNumber += _stats.m_CDSNumber;
        m_validCDSNumber += _stats.m_validCDSNumber;
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    final void compute() {
        if (m_totalTrinucleotide != 0) {
            for (Tuple row : m_TRINUCLEOTIDE_TABLE) {
                row.set(StatFloat.FREQ0, row.get(StatLong.PHASE0) / (float) m_totalTrinucleotide);
                row.set(StatFloat.FREQ1, row.get(StatLong.PHASE1) / (float) m_totalTrinucleotide);
                row.set(StatFloat.FREQ2, row.get(StatLong.PHASE2) / (float) m_totalTrinucleotide);
            }
        }
        if (m_totalDinucleotide != 0) {
            for (Tuple row : m_DINUCLEOTIDE_TABLE) {
                row.set(StatFloat.FREQ0, row.get(StatLong.PHASE0) / (float) m_totalDinucleotide);
                row.set(StatFloat.FREQ1, row.get(StatLong.PHASE1) / (float) m_totalDinucleotide);
            }
        }
    }

    /**
     * Increment by 1 the value of a trinucleotide for a stat
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    final void incrementStat(Trinucleotide _tri, StatLong _stat) {
        m_TRINUCLEOTIDE_TABLE[_tri.ordinal()].incr(_stat, 1L);
    }

    /**
     * Increment by 1 the value of a dinucleotide for a stat
     *
     * @param _di,   the Dinucleotide to set
     * @param _stat, the statistic to set
     */
    final void incrementStat(Dinucleotide _di, StatLong _stat) {
        m_DINUCLEOTIDE_TABLE[_di.ordinal()].incr(_stat, 1L);
    }

    /**
     * Increment the value of total trinucleotide by the parameter
     *
     * @param _inc, the value to increment
     */
    final void incrementTriTotal(long _inc) {
        m_totalTrinucleotide += _inc;
    }

    /**
     * Increment the value of total trinucleotide by the parameter
     *
     * @param _inc, the value to increment
     */
    final void incrementDiTotal(long _inc) {
        m_totalDinucleotide += _inc;
    }

    /**
     * Increment the number of CDS sequence
     *
     * @param _long, the value to increment
     */
    final void incrementCDS(long _long) {
        m_CDSNumber += _long;
    }

    /**
     * Increment the number of valid CDS sequence
     *
     * @param _long, the value to increment
     */
    final void incrementValidCDS(long _long) {
        m_validCDSNumber += _long;
    }

    /**
     * Unload data
     *
     * @param _stats the data to unload
     */
    void unload(Statistics _stats) {
        m_totalTrinucleotide -= _stats.m_totalTrinucleotide;
        m_totalDinucleotide -= _stats.m_totalDinucleotide;
        m_CDSNumber -= _stats.m_CDSNumber;
        m_validCDSNumber -= _stats.m_validCDSNumber;
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_TRINUCLEOTIDE_TABLE[i];
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });
        IntStream.range(0, Dinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_DINUCLEOTIDE_TABLE[i];
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
        });

    }

    /**
     * List of the 64 trinucleotide
     */
    public enum Trinucleotide {
        AAA,
        AAC,
        AAG,
        AAT,
        ACA,
        ACC,
        ACG,
        ACT,
        AGA,
        AGC,
        AGG,
        AGT,
        ATA,
        ATC,
        ATG,
        ATT,
        CAA,
        CAC,
        CAG,
        CAT,
        CCA,
        CCC,
        CCG,
        CCT,
        CGA,
        CGC,
        CGG,
        CGT,
        CTA,
        CTC,
        CTG,
        CTT,
        GAA,
        GAC,
        GAG,
        GAT,
        GCA,
        GCC,
        GCG,
        GCT,
        GGA,
        GGC,
        GGG,
        GGT,
        GTA,
        GTC,
        GTG,
        GTT,
        TAA,
        TAC,
        TAG,
        TAT,
        TCA,
        TCC,
        TCG,
        TCT,
        TGA,
        TGC,
        TGG,
        TGT,
        TTA,
        TTC,
        TTG,
        TTT
    }

    /**
     * List of the 16 dinucleotide
     */
    public enum Dinucleotide {
        AA,
        AC,
        AG,
        AT,
        CA,
        CC,
        CG,
        CT,
        GA,
        GC,
        GG,
        GT,
        TA,
        TC,
        TG,
        TT
    }

    /**
     * List of statistics (float)
     */
    public enum StatFloat {
        FREQ0,
        FREQ1,
        FREQ2
    }

    /**
     * List of statistics (long)
     */
    public enum StatLong {
        PHASE0,
        PHASE1,
        PHASE2,
        PREF0,
        PREF1,
        PREF2
    }

    /**
     * This enumeration represent the type of a Statistic
     */
    public enum Type {
        CHROMOSOME,
        MITOCHONDRION,
        PLASMID,
        DNA,
        CHLOROPLAST,
        SEGMENT,
        LINKAGE,
        PLASTID,
        CIRCLE,
        RNA,
        PLTD,
        UNKNOWN,
        UNNAMED;

        /**
         * Get the Type from a String
         *
         * @param _s the String to check
         * @return the corresponding type
         */
        public static Type isTypeOf(String _s) {
            if (_s.contains("CHLOROPLAST")) {
                return CHLOROPLAST;
            } else if (_s.contains("CHROMOSOME")) {
                return CHROMOSOME;
            } else if (_s.contains("PLASMID")) {
                return PLASMID;
            } else if (_s.contains("SEGMENT") || _s.contains("SEGMEN")) {
                return SEGMENT;
            } else if (_s.contains("LINKAGE")) {
                return LINKAGE;
            } else if (_s.contains("PLASTID")) {
                return PLASTID;
            } else if (_s.contains("CIRCLE")) {
                return CIRCLE;
            } else if (_s.contains("PLTD")) {
                return PLTD;
            } else if (_s.contains("UNKNOWN")) {
                return UNKNOWN;
            } else if (_s.contains("UNNAMED")) {
                return UNNAMED;
            } else if (_s.contains("MITOCHONDRION") || _s.contains("MT")) {
                return MITOCHONDRION;
            } else if (_s.contains("DNA") || _s.contains("DN")) {
                return DNA;
            } else if (_s.contains("RNA") || _s.contains("RN")) {
                return RNA;
            }
            return null;
        }
    }
}
