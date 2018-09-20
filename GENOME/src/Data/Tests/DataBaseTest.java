package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataBaseTest {

    static private DataBase db;

    @BeforeAll
    static void setUp() throws InvalidStateException {
        db = DataBase.load("", _dataBase -> {
        });
        db.start();
    }

    @AfterAll
    static void tear() throws InvalidStateException {
        db.stop();
    }

    @Test
    void addKingdom() throws AddException, InvalidStateException {
        Kingdom.load("", db, _kingdom -> {
        });
        assertEquals(1, db.getKingdoms().size());
        Kingdom.load("", db, _kingdom -> {
        });
        assertEquals(2, db.getKingdoms().size());
    }

    @Test
    void getKingdoms() throws AddException, InvalidStateException {
        Kingdom k1 = Kingdom.load("one", db, _kingdom -> {
        });
        Kingdom k2 = Kingdom.load("two", db, _kingdom -> {
        });
        assertEquals(true, db.getKingdoms().contains(k1));
        assertEquals(true, db.getKingdoms().contains(k2));
    }

    @Test
    void getModificationDate() {
        Calendar caldb = Calendar.getInstance();
        caldb.setTime(db.getModificationDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        assertEquals(caldb.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        assertEquals(caldb.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        assertEquals(caldb.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void dataBase() throws AddException, InvalidStateException {
        DataBase dataBase = DataBase.load("d1", _dataBase -> {
        });
        assertEquals(IDataBase.State.CREATED, dataBase.getState());

        // Database start and stop
        Kingdom k1 = Kingdom.load("k1", dataBase, _kingdom -> {
        });
        assertEquals(false, dataBase.getKingdoms().contains(k1));
        dataBase.start();
        k1 = Kingdom.load("k1", dataBase, _kingdom -> {
        });
        assertEquals(true, dataBase.getKingdoms().contains(k1));

        Kingdom k2 = Kingdom.load("k2", dataBase, _kingdom -> {
        });
        assertEquals(true, dataBase.getKingdoms().contains(k2));

        dataBase.stop();

        Kingdom k3 = Kingdom.load("k3", dataBase, _kingdom -> {
        });
        assertEquals(false, dataBase.getKingdoms().contains(k3));
        assertEquals(IDataBase.State.STOPPED, dataBase.getState());

        // Group start and Kingdom start and stop
        for (Kingdom k : dataBase.getKingdoms()) {
            Group g1 = Group.load("g1_" + k.getName(), k, _group -> {
            });
            assertEquals(false, k.getGroups().contains(g1));
            k.start();
            g1 = Group.load("g1_" + k.getName(), k, _group -> {
            });
            assertEquals(true, k.getGroups().contains(g1));
            g1.start();

            Group g2 = Group.load("g2_" + k.getName(), k, _group -> {
            });
            assertEquals(true, k.getGroups().contains(g2));
            k.stop();
            g2.start();

            Group g3 = Group.load("g3_" + k.getName(), k, _group -> {
            });
            assertEquals(false, k.getGroups().contains(g3));
            assertEquals(IDataBase.State.STOPPED, k.getState());
            assertThrows(InvalidStateException.class, g3::start);
        }

        // Group stop
        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                SubGroup s1 = SubGroup.load("s1_" + g.getName(), g, _subGroup -> {
                });
                assertEquals(true, g.getSubGroups().contains(s1));

                SubGroup s2 = SubGroup.load("s2_" + g.getName(), g, _subGroup -> {
                });
                assertEquals(true, g.getSubGroups().contains(s2));

                g.stop();
                SubGroup s3 = SubGroup.load("s3_" + g.getName(), g, _subGroup -> {
                });
                assertEquals(false, g.getSubGroups().contains(s3));
                assertEquals(IDataBase.State.STOPPED, g.getState());
            }
        }

        ArrayList<Organism> list = new ArrayList<>();

        // Subgroup start and stop
        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                for (SubGroup s : g.getSubGroups()) {
                    Organism o1 = Organism.load("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, s, true, _organism -> {
                    });
                    assertEquals(false, s.getOrganisms().contains(o1));
                    s.start();
                    o1 = Organism.load("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, s, true, _organism -> {
                    });
                    assertEquals(true, s.getOrganisms().contains(o1));
                    list.add(o1);

                    Organism o2 = Organism.load("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, s, true, _organism -> {
                    });
                    assertEquals(true, s.getOrganisms().contains(o2));
                    s.stop();
                    list.add(o2);

                    Organism o3 = Organism.load("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, s, true, _organism -> {
                    });
                    assertEquals(false, s.getOrganisms().contains(o3));
                    assertEquals(IDataBase.State.STOPPED, s.getState());
                }
            }
        }

        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                for (SubGroup s : g.getSubGroups()) {
                    for (Organism o : s.getOrganisms()) {
                        o.start();
                        ArrayList<StringBuilder> sequences = new ArrayList<>();
                        sequences.add(new StringBuilder("ACGTACGTACGT"));
                        sequences.add(new StringBuilder("ACGTACGTACGTACG"));

                        Replicon r1 = new Replicon(Replicon.Type.CHLOROPLAST, "r1_" + o.getName(), 2, 1, sequences);
                        assertEquals(true, o.addReplicon(r1));
                        assertEquals(Replicon.Type.CHLOROPLAST, r1.getType());

                        Replicon r2 = new Replicon(Replicon.Type.CHLOROPLAST, "r2_" + o.getName(), 2, 1, sequences);
                        assertEquals(true, o.addReplicon(r2));
                        assertEquals(Replicon.Type.CHLOROPLAST, r2.getType());

                        Replicon r3 = new Replicon(Replicon.Type.MITOCHONDRION, "r3_" + o.getName(), 2, 1, sequences);
                        assertEquals(true, o.addReplicon(r3));
                        assertEquals(Replicon.Type.MITOCHONDRION, r3.getType());

                        assertThrows(Exception.class, () -> o.addReplicon(r3));
                        o.stop();
                    }
                }
            }
        }

        for (Organism o : list) {
            o.finish();
        }
    }

    @Test
    void nameTest() throws AddException, InvalidStateException {

        Kingdom k = Kingdom.load("KINGDOM", db, _kingdom -> {
        });
        k.start();

        Group g = Group.load("GROUP", k, _group -> {
        });
        g.start();

        SubGroup s = SubGroup.load("SUBGROUP", g, _subGroup -> {
        });
        s.start();

        Organism o = Organism.load("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, s, true, _organism -> {
        });

        assertEquals("KINGDOM", o.getKingdomName());
        assertEquals("GROUP", o.getGroupName());
        assertEquals("SUBGROUP", o.getSubGroupName());

        assertEquals("KINGDOM", s.getKingdomName());
        assertEquals("GROUP", s.getGroupName());

        assertEquals("KINGDOM", g.getKingdomName());

        s.stop();
        g.stop();
        k.stop();
    }


}