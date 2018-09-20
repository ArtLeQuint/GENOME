package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ICallbackTest {

    @Test
    void stopFinish() throws InvalidStateException, AddException {

        final ArrayList<Boolean> check = new ArrayList<>();
        check.add(false);
        check.add(false);
        check.add(false);
        check.add(false);
        check.add(false);

        DataBase dataBase = DataBase.load("DataBase", _dataBase -> {
            check.set(0, true);
            assertEquals(1, _dataBase.getStatistics().size());
        });
        dataBase.start();

        Kingdom kingdom = Kingdom.load("Kingdom", dataBase, _kingdom -> {
            check.set(1, true);
            assertEquals(1, _kingdom.getStatistics().size());
        });
        kingdom.start();

        Group group = Group.load("Group", kingdom, _group -> {
            check.set(2, true);
            assertEquals(1, _group.getStatistics().size());
        });
        group.start();

        SubGroup subGroup = SubGroup.load("SubGroup", group, _subGroup -> {
            check.set(3, true);
            assertEquals(1, _subGroup.getStatistics().size());
        });
        subGroup.start();

        Organism organism = Organism.load("Organism", 1234L, 4321L, subGroup, true, _organism -> {
            check.set(4, true);
            assertEquals(1, _organism.getStatistics().size());
            assertEquals(1, _organism.getReplicons().size());
        });
        organism.start();

        ArrayList<StringBuilder> sequences = new ArrayList<>();
        sequences.add(new StringBuilder("ACGTACGTACGT"));
        sequences.add(new StringBuilder("ACGTACGTACGTACG"));
        Replicon replicon = new Replicon(Statistics.Type.CHLOROPLAST, "Replicon", 2, 1, sequences);
        organism.addReplicon(replicon);

        organism.stop();
        group.stop();
        subGroup.stop();
        kingdom.stop();
        dataBase.stop();

        organism.finish();

        assertEquals(true, check.get(0));
        assertEquals(true, check.get(1));
        assertEquals(true, check.get(2));
        assertEquals(true, check.get(3));
        assertEquals(true, check.get(4));

        assertEquals(IDataBase.State.FINISHED, organism.getState());
        assertEquals(IDataBase.State.FINISHED, group.getState());
        assertEquals(IDataBase.State.FINISHED, subGroup.getState());
        assertEquals(IDataBase.State.FINISHED, kingdom.getState());
        assertEquals(IDataBase.State.FINISHED, dataBase.getState());
    }

    @Test
    void finishStop() throws InvalidStateException, AddException {

        final ArrayList<Boolean> check = new ArrayList<>();
        check.add(false);
        check.add(false);
        check.add(false);
        check.add(false);
        check.add(false);

        DataBase dataBase = DataBase.load("DataBase", _dataBase -> {
            check.set(0, true);
            assertEquals(1, _dataBase.getStatistics().size());
        });
        dataBase.start();

        Kingdom kingdom = Kingdom.load("Kingdom", dataBase, _kingdom -> {
            check.set(1, true);
            assertEquals(1, _kingdom.getStatistics().size());
        });
        kingdom.start();

        Group group = Group.load("Group", kingdom, _group -> {
            check.set(2, true);
            assertEquals(1, _group.getStatistics().size());
        });
        group.start();

        SubGroup subGroup = SubGroup.load("SubGroup", group, _subGroup -> {
            check.set(3, true);
            assertEquals(1, _subGroup.getStatistics().size());
        });
        subGroup.start();

        Organism organism = Organism.load("Organism", 1234L, 4321L, subGroup, true, _organism -> {
            check.set(4, true);
            assertEquals(1, _organism.getStatistics().size());
            assertEquals(1, _organism.getReplicons().size());
        });
        organism.start();

        ArrayList<StringBuilder> sequences = new ArrayList<>();
        sequences.add(new StringBuilder("ACGTACGTACGT"));
        sequences.add(new StringBuilder("ACGTACGTACGTACG"));
        Replicon replicon = new Replicon(Statistics.Type.CHLOROPLAST, "Replicon", 2, 1, sequences);
        organism.addReplicon(replicon);

        organism.stop();
        organism.finish();

        group.stop();
        subGroup.stop();
        kingdom.stop();
        dataBase.stop();

        assertEquals(true, check.get(0));
        assertEquals(true, check.get(1));
        assertEquals(true, check.get(2));
        assertEquals(true, check.get(3));
        assertEquals(true, check.get(4));

        assertEquals(IDataBase.State.FINISHED, organism.getState());
        assertEquals(IDataBase.State.FINISHED, group.getState());
        assertEquals(IDataBase.State.FINISHED, subGroup.getState());
        assertEquals(IDataBase.State.FINISHED, kingdom.getState());
        assertEquals(IDataBase.State.FINISHED, dataBase.getState());
    }
}