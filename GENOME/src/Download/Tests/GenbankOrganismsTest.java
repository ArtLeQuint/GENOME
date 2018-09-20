package Download.Tests;

import Download.GenbankOrganisms;
import Download.OrganismParser;
import Exception.MissException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenbankOrganismsTest {
    @Test
    void genbankOrganism() {

        GenbankOrganisms go = new GenbankOrganisms();
        try {
            go.downloadOrganisms();
        } catch (MissException e) {
            assertTrue(true, e.toString());
        }

        ArrayList<String> kingdom = new ArrayList<>();
        ArrayList<ArrayList<String>> group = new ArrayList<>();
        ArrayList<ArrayList<String>> subgroup = new ArrayList<>();
        ArrayList<ArrayList<String>> name = new ArrayList<>();

        String lastKingdom = "";
        String lastGroup = "";
        String lastSubGroup = "";

        int totalOrganism = 0;
        while (go.hasNext()) {
            OrganismParser ro = go.getNext();

            assertTrue(ro.getId() != -1);
            assertTrue(ro.getName() != null, "id : " + ro.getId());
            assertTrue(ro.getKingdom() != null, "id : " + ro.getId() + ", name : " + ro.getName());
            assertTrue(ro.getGroup() != null, "id : " + ro.getId() + ", name : " + ro.getName());
            assertTrue(ro.getSubGroup() != null, "id : " + ro.getId() + ", name : " + ro.getName());
            assertTrue(ro.getVersion() != -1, "id : " + ro.getId() + ", name : " + ro.getName());
            assertTrue(ro.getModificationDate() != null, "id : " + ro.getId() + ", name : " + ro.getName());

            for (Map.Entry<String, String> CDS : ro.getReplicons()) {
                assertTrue(CDS.getKey().indexOf("NC_") == 0, "id : " + ro.getId() + ", name : " + ro.getName() + ", Replicon : " + CDS.getKey());
                assertNotNull(CDS.getValue(), "id : " + ro.getId() + ", name : " + ro.getName() + ", Replicon : " + CDS.getKey());
            }

            totalOrganism++;

            String kin = ro.getKingdom().toUpperCase();
            String gro = ro.getGroup().toUpperCase();
            String sub = ro.getSubGroup().toUpperCase();

            if (kin.compareTo(lastKingdom) != 0) {
                kingdom.add(kin);
                group.add(new ArrayList<>());
                group.get(group.size() - 1).add(gro);
                subgroup.add(new ArrayList<>());
                subgroup.get(subgroup.size() - 1).add(sub);
                name.add(new ArrayList<>());
                name.get(name.size() - 1).add(ro.getName());
            } else {
                if (gro.compareTo(lastGroup) != 0) {
                    group.get(group.size() - 1).add(gro);
                    subgroup.add(new ArrayList<>());
                    subgroup.get(subgroup.size() - 1).add(sub);
                    name.add(new ArrayList<>());
                    name.get(name.size() - 1).add(ro.getName());
                } else {
                    if (sub.compareTo(lastSubGroup) != 0) {
                        subgroup.get(subgroup.size() - 1).add(sub);
                        name.add(new ArrayList<>());
                        name.get(name.size() - 1).add(ro.getName());
                    } else {
                        name.get(name.size() - 1).add(ro.getName());
                    }
                }
            }

            lastKingdom = kin;
            lastGroup = gro;
            lastSubGroup = sub;
        }

        boolean kingdomSorted = true;
        for (int i = 0; i < kingdom.size(); ++i) {
            for (int j = i + 1; j < kingdom.size(); ++j) {
                if (kingdom.get(i).compareTo(kingdom.get(j)) == 0) {
                    kingdomSorted = false;
                }
            }
        }
        assertTrue(kingdomSorted);

        for (ArrayList<String> arr : group) {
            boolean groupSorted = true;
            for (int i = 0; i < arr.size(); ++i) {
                for (int j = i + 1; j < arr.size(); ++j) {
                    if (arr.get(i).compareTo(arr.get(j)) == 0) {
                        groupSorted = false;
                    }
                }
            }
            assertTrue(groupSorted);
        }

        for (ArrayList<String> arr : subgroup) {
            boolean subSorted = true;
            for (int i = 0; i < arr.size(); ++i) {
                for (int j = i + 1; j < arr.size(); ++j) {
                    if (arr.get(i).compareTo(arr.get(j)) == 0) {
                        subSorted = false;
                    }
                }
            }
            assertTrue(subSorted);
        }

        for (ArrayList<String> arr : name) {
            boolean nameUnique = true;
            for (int i = 0; i < arr.size(); ++i) {
                for (int j = 0; j < arr.size(); ++j) {
                    if (j != i) {
                        if (arr.get(i).compareTo(arr.get(j)) == 0) {
                            nameUnique = false;
                        }
                    }
                }
            }
            assertTrue(nameUnique);
        }

        assertEquals(totalOrganism, go.getTotalCount());
    }

}