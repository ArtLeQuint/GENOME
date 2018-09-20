package Manager.Tests;

import Manager.ITask;
import Manager.ThreadManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadManagerTest {

    private static ThreadManager threadManager;

    @BeforeAll
    static void setUp() {
        threadManager = new ThreadManager(Runtime.getRuntime().availableProcessors());

    }

    @AfterAll
    static void finalizeThreadManager() {
        assertTimeout(Duration.ofSeconds(1), () -> threadManager.finalizeThreadManager(false));
    }

    @Test
    void pushTask() {
        assertTrue(threadManager.pushTask(new ITask("") {
            @Override
            public void run() {
            }

            @Override
            public void cancel() {
            }
        }));
    }
}