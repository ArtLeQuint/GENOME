package Download;

import Json.JSONException;
import Json.JSONObject;
import Utils.Logs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OrganismParser {

    /**
     * Key to get replicons
     */
    private static final String s_REPLICON = "replicons";
    /**
     * Key to get the id
     */
    private static final String s_ID = "id";
    /**
     * Key to get the name
     */
    private static final String s_NAME = "organism";
    /**
     * Key to get the kingdom
     */
    private static final String s_KINGDOM = "kingdom";
    /**
     * Key to get the group
     */
    private static final String s_GROUP = "group";
    /**
     * Key to get the subgroup
     */
    private static final String s_SUBGROUP = "subgroup";
    /**
     * Key to get the modification date
     */
    private static final String s_MODIFICATION_DATE = "modify_date";
    /**
     * Key to get the release date
     */
    private static final String s_RELEASE_DATE = "release_date";
    /**
     * Key to get the version
     */
    private static final String s_VERSION = "_version_";
    /**
     * Regex used to get only replicon which contain NC_
     */
    private static final String s_NC_REGEX = "NC_[^(/|;| |\n|:)]*";
    /**
     * Regex used tu get good CDS type
     */
    private static final String s_TYPE_REGEX = "(CHLOROPLAST)|(CHROMOSOME)|(PLASMID)|(SEGMENT)|(SEGMEN)|(LINKAGE)|(PLASTID)|(CIRCLE)|(PLTD)|(UNKNOWN)|(UNNAMED)|(MITOCHONDRION)|(MT)|(DNA)|(DN)|(RNA)|(RN)";
    /**
     * Object to parse
     */
    private final JSONObject m_OBJECT;
    /**
     * List of replicon's ID of this organism
     */
    private final ArrayList<Map.Entry<String, String>> m_REPLICONS;
    /**
     * ID of this organism
     */
    private long m_id;
    /**
     * Name of this organism
     */
    private String m_name;
    /**
     * Kingdom of this organism
     */
    private String m_kingdom;
    /**
     * Group of this organism
     */
    private String m_group;
    /**
     * Subgroup of this organism
     */
    private String m_subGroup;
    /**
     * Version of this organism
     */
    private long m_version;
    /**
     * Modification date of this organism
     */
    private Date m_modificationDate;

    /**
     * Class constructor
     *
     * @param _obj, the json object to parse
     * @throws JSONException, if an error occurred
     */
    public OrganismParser(JSONObject _obj) throws JSONException {
        m_OBJECT = _obj;
        m_id = -1;
        m_name = null;
        m_kingdom = null;
        m_group = null;
        m_subGroup = null;
        m_version = -1;
        m_modificationDate = new Date();
        m_REPLICONS = new ArrayList<>();
        parse();
    }

    /**
     * Get the id
     *
     * @return the id
     */
    public long getId() {
        return m_id;
    }

    /**
     * Get the name
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Get the kingdom name
     *
     * @return the kingdom name
     */
    public String getKingdom() {
        return m_kingdom;
    }

    /**
     * Get the group name
     *
     * @return the group name
     */
    public String getGroup() {
        return m_group;
    }

    /**
     * Get the subGroup name
     *
     * @return the subGroup name
     */
    public String getSubGroup() {
        return m_subGroup;
    }

    /**
     * Get the replicon's id list
     *
     * @return the replicon's id list
     */
    public ArrayList<Map.Entry<String, String>> getReplicons() {
        return m_REPLICONS;
    }

    /**
     * Get the modification date
     *
     * @return the modification date
     */
    public Date getModificationDate() {
        return m_modificationDate;
    }

    /**
     * Get the version of the organism
     *
     * @return the version's number
     */
    public long getVersion() {
        return m_version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != OrganismParser.class)
            return false;
        OrganismParser orgObj = (OrganismParser) obj;
        return m_kingdom.equals(orgObj.m_kingdom) && m_group.equals(orgObj.m_group) && m_subGroup.equals(orgObj.m_subGroup) && m_name.equals(orgObj.m_name);
    }

    /**
     * Parse the JSonObject to get all data
     *
     * @throws JSONException if it's impossible to parse data
     */
    private void parse() throws JSONException {
        try {
            m_id = m_OBJECT.getLong(s_ID);
            m_name = m_OBJECT.getString(s_NAME).replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase();
            m_kingdom = m_OBJECT.getString(s_KINGDOM).replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase();
            m_group = m_OBJECT.getString(s_GROUP).replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase();
            m_subGroup = m_OBJECT.getString(s_SUBGROUP).replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase();
            m_version = m_OBJECT.getLong(s_VERSION);
        } catch (JSONException e) {
            final String message = "Unable to get basic data : " + m_OBJECT;
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }

        LocalDateTime dateTime = null;
        try {
            // Replicons formatting
            if (m_OBJECT.has(s_REPLICON)) {
                final Pattern patternNC = Pattern.compile(s_NC_REGEX, Pattern.CASE_INSENSITIVE);
                final Pattern patternType = Pattern.compile(s_TYPE_REGEX, Pattern.CASE_INSENSITIVE);

                for (String sequence : m_OBJECT.getString(s_REPLICON).split(";")) {
                    sequence = sequence.toUpperCase();
                    final Matcher mNC = patternNC.matcher(sequence);

                    if (mNC.find()) {
                        final String sequenceId = mNC.group(0);

                        final String[] data = sequence.split(":");
                        if (data.length == 2) {
                            final String sequenceInfo = data[0];
                            final Matcher mType = patternType.matcher(sequenceInfo);
                            if (mType.find()) {
                                m_REPLICONS.add(new Map.Entry<String, String>() {
                                    @Override
                                    public String getKey() {
                                        return sequenceId;
                                    }

                                    @Override
                                    public String getValue() {
                                        return sequenceInfo;
                                    }

                                    @Override
                                    public String setValue(String _value) {
                                        return null;
                                    }
                                });
                            } else {
                                Logs.info("Unable to get type of : '" + m_name + "' from sequence : " + sequence, false);
                            }
                        } else {
                            Logs.info("Unable to get information of : '" + m_name + "' from sequence : " + sequence, false);
                        }
                    }

                }
            }

            // Dates formatting
            final DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
            if (m_OBJECT.has(s_MODIFICATION_DATE)) {
                dateTime = LocalDateTime.parse(m_OBJECT.getString(s_MODIFICATION_DATE), format);
            } else if (m_OBJECT.has(s_RELEASE_DATE)) {
                dateTime = LocalDateTime.parse(m_OBJECT.getString(s_RELEASE_DATE), format);
            }
        } catch (JSONException e) {
            final String message = "Unable to get specifics data : " + m_OBJECT;
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }

        if (dateTime != null) {
            final Calendar cal = Calendar.getInstance();
            cal.set(dateTime.getYear(), dateTime.getMonthValue() - 1, dateTime.getDayOfMonth(), 0, 0, 0);
            m_modificationDate = cal.getTime();
        }
    }
}
