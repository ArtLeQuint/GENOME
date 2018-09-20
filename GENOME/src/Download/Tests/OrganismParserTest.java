package Download.Tests;

import Download.OrganismParser;
import Json.JSONException;
import Json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.Format;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class OrganismParserTest {

    private final static String s_ID = "27587";
    private final static String s_NAME = "Lachnospiraceae";
    private final static String s_KINGDOM = "Bacteria";
    private final static String s_GROUP = "Terrabacteria";
    private final static String s_SUBGROUP = "Firmicutes";
    private final static String s_RELEASE = "2017-09-28T00:00:00Z";
    private final static String s_MODIFICATION = "2017-09-28T00:00:00Z";
    private final static String s_VERSION = "1593911655818854400";
    private final static String s_REPLICONS = "NC_011961.1";

    private final static String s_ID_JS = "\"id\":" + s_ID;
    private final static String s_NAME_JS = "\"organism\":\"" + s_NAME + "\"";
    private final static String s_KINGDOM_JS = "\"kingdom\":\"" + s_KINGDOM + "\"";
    private final static String s_GROUP_JS = "\"group\":\"" + s_GROUP + "\"";
    private final static String s_SUBGROUP_JS = "\"subgroup\":\"" + s_SUBGROUP + "\"";
    private final static String s_RELEASE_JS = "\"release_date\":\"" + s_RELEASE + "\"";
    private final static String s_MODIFICATION_JS = "\"modify_date\":\"" + s_MODIFICATION + "\"";
    private final static String s_VERSION_JS = "\"_version_\":" + s_VERSION;
    private final static String s_REPLICONS_JS = "\"replicons\":\"unnamed:" + s_REPLICONS + "\"";

    static private OrganismParser m_OrganismParser;

    @BeforeAll
    static void setUp() throws JSONException {
        String json = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        m_OrganismParser = new OrganismParser(new JSONObject(json));
    }

    @Test
    void organismParser() {
        {
            final String jsonFail = "{" + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            new OrganismParser(new JSONObject(jsonFail));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            new OrganismParser(new JSONObject(jsonFail));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_GROUP_JS + "}";
            new OrganismParser(new JSONObject(jsonFail));
        }
        {
            final String jsonFail = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{\"id\": test," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_SUBGROUP_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{\"version\": test," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_ID_JS + "," + s_SUBGROUP_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
        {
            final String jsonFail = "{}";
            assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail)));
        }
    }

    @Test
    void getId() {
        assertTrue(s_ID.compareTo(String.valueOf(m_OrganismParser.getId())) == 0);
    }

    @Test
    void getName() {
        assertTrue(s_NAME.toLowerCase().compareTo(m_OrganismParser.getName()) == 0);
    }

    @Test
    void getKingdom() {
        assertTrue(s_KINGDOM.toLowerCase().compareTo(m_OrganismParser.getKingdom()) == 0);
    }

    @Test
    void getGroup() {
        assertTrue(s_GROUP.toLowerCase().compareTo(m_OrganismParser.getGroup()) == 0);
    }

    @Test
    void getSubGroup() {
        assertTrue(s_SUBGROUP.toLowerCase().compareTo(m_OrganismParser.getSubGroup()) == 0);
    }

    @Test
    void getReplicons() throws JSONException {
        assertTrue(s_REPLICONS.compareTo(m_OrganismParser.getReplicons().get(0).getKey()) == 0);
        String json = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_GROUP_JS +
                ",\"replicons\": \"DNA:NC_1256;MT:NC_1546;NC_785;test:NC_854;DNS:test\"}";
        OrganismParser parser = new OrganismParser(new JSONObject(json));
        assertEquals(2, parser.getReplicons().size());
        assertEquals("NC_1256", parser.getReplicons().get(0).getKey());
        assertEquals("DNA", parser.getReplicons().get(0).getValue());
        assertEquals("NC_1546", parser.getReplicons().get(1).getKey());
        assertEquals("MT", parser.getReplicons().get(1).getValue());
    }

    @Test
    void getModificationDate() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder date = new StringBuilder(formatter.format(m_OrganismParser.getModificationDate()));
        date.replace(10, 11, "T");
        date.replace(19, 20, "Z");
        assertTrue(s_MODIFICATION.compareTo(date.toString()) == 0);
    }

    @Test
    void getVersion() {
        assertTrue(s_VERSION.compareTo(String.valueOf(m_OrganismParser.getVersion())) == 0);
    }
}