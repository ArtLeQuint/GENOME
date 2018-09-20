package Manager.Tests;

import Manager.ITask;
import Manager.ThreadManager;
import Utils.Logs;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalTest {

    @org.junit.jupiter.api.Test
    void threadManagerTest() {

        ThreadManager thr = new ThreadManager(Runtime.getRuntime().availableProcessors());

        // Store results of each threads
        ArrayList<ArrayList<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < 30; ++i) {
            ArrayList<Boolean> res1 = new ArrayList<>();
            results.add(res1);
            ArrayList<Boolean> res2 = new ArrayList<>();
            results.add(res2);

            assertTrue(thr.pushTask(new ITask("" + i) {
                @Override
                public void run() {
                    for (long j = 0; j < 10; ++j) {
                        assertTrue(res1.add(true));
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Logs.exception(e);
                        }
                    }
                }

                @Override
                public void cancel() {
                }
            }));

            assertTrue(thr.pushTask(new ITask("" + i) {
                @Override
                public void run() {
                    for (long j = 0; j < 10; ++j) {
                        assertTrue(res2.add(true));
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Logs.exception(e);
                        }
                    }
                }

                @Override
                public void cancel() {
                }
            }));
        }

        // Blocked until all tasks are finished
        thr.finalizeThreadManager(false);

        for (ArrayList<Boolean> li : results) {
            assertEquals(10, li.size());
            for (Boolean b : li) {
                assertTrue(b);
            }
        }
    }

}