package Manager;

import Utils.Logs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Utils.Options.getMaxThread;

public final class ThreadManager {

    /**
     * Max number of thread
     */
    private final int m_NBTHREADMAX;
    /**
     * True if threads are running
     */
    private final Lock m_RUNNINGLOCK;
    /**
     * Condition to wait
     */
    private final Lock m_LOCKARRAY;
    private final Condition m_CONDARRAY;
    private final Condition m_CONDPUSH;
    /**
     * Threads array
     */
    private final ArrayList<Thread> m_THREADS;
    /**
     * Task list
     */
    private final LinkedList<ITask> m_task;
    private volatile boolean m_running;

    /**
     * Instantiate all threads
     */
    public ThreadManager(int _nbThreadMax) {

        m_NBTHREADMAX = _nbThreadMax > getMaxThread() ? getMaxThread() : _nbThreadMax;

        Logs.info("Number of threads : " + m_NBTHREADMAX, true);

        m_THREADS = new ArrayList<>(m_NBTHREADMAX);
        m_running = true;
        m_RUNNINGLOCK = new ReentrantLock();

        m_task = new LinkedList<>();

        m_LOCKARRAY = new ReentrantLock();
        m_CONDARRAY = m_LOCKARRAY.newCondition();
        m_CONDPUSH = m_LOCKARRAY.newCondition();

        for (int i = 0; i < m_NBTHREADMAX; ++i) {
            final Thread thr = new Thread(new Executor(i));
            thr.setPriority(Thread.MAX_PRIORITY);
            m_THREADS.add(thr);
            thr.start();
        }
    }

    /**
     * Blocking method which wait until all task are finished
     */
    public void finalizeThreadManager(boolean _cancel) {
        m_RUNNINGLOCK.lock();
        {
            m_running = false;
        }
        m_RUNNINGLOCK.unlock();

        m_LOCKARRAY.lock();
        {
            if (_cancel) {
                Logs.notice("Cancel requested", true);
                while (!m_task.isEmpty()) {
                    ITask task = m_task.removeFirst();
                    Logs.notice("Cancel : " + task.getName(), true);
                    task.cancel();
                }
            }
            m_CONDARRAY.signalAll();
        }
        m_LOCKARRAY.unlock();

        for (Thread thr : m_THREADS) {
            try {
                thr.join();
            } catch (InterruptedException e) {
                Logs.warning("Unable to join thread : " + thr.getName());
                Logs.exception(e);
            }
        }
        Logs.notice("Finalized", true);
    }

    /**
     * Push a new task
     *
     * @param _iTask, the task to push
     * @return the insertion success
     */
    public boolean pushTask(ITask _iTask) {
        boolean res = false;
        m_LOCKARRAY.lock();
        try {
            while (m_task.size() >= m_NBTHREADMAX + 1) {
                m_CONDPUSH.await();
            }
            res = m_task.add(_iTask);
            if (res) {
                m_CONDARRAY.signal();
                Logs.info("Add task : " + _iTask.getName(), true);
            }
        } catch (InterruptedException e) {
            Logs.warning("Unable to add task : " + _iTask.getName());
            Logs.exception(e);
        } finally {
            m_LOCKARRAY.unlock();
        }
        return res;
    }

    /**
     * Check if the threads are running
     *
     * @return the running state
     */
    private boolean isRunning() {
        final boolean res;
        m_RUNNINGLOCK.lock();
        {
            res = m_running;
        }
        m_RUNNINGLOCK.unlock();
        return res;
    }

    /**
     * Executor which get a task and execute it
     */
    final class Executor implements Runnable {

        final private int m_id;

        Executor(int _id) {
            m_id = _id;
        }

        @Override
        public void run() {

            while (true) {

                ITask todo = null;

                // Get task to do
                m_LOCKARRAY.lock();
                {
                    // Wait until they are data
                    while (m_task.isEmpty()) {
                        if (!isRunning()) {
                            m_LOCKARRAY.unlock();
                            return;
                        }
                        try {
                            m_CONDARRAY.await();
                        } catch (InterruptedException e) {
                            Logs.warning("Error while waiting for tasks : " + this.m_id);
                            Logs.exception(e);
                        }
                    }

                    // Choose a data
                    if (!m_task.isEmpty()) {
                        todo = m_task.removeFirst();
                    } else {
                        Logs.warning("Concurrency error in thread " + m_id);
                    }

                    // Log
                    if (todo != null) {
                        if (m_task.size() < m_NBTHREADMAX + 1) {
                            m_CONDPUSH.signalAll();
                        }
                        Logs.info("Task '" + todo.getName() + "' is chosen by thread " + m_id + " : remains " + m_task.size() + " task", true);
                    }

                }
                m_LOCKARRAY.unlock();

                if (todo != null) {

                    // Run task
                    try {
                        todo.run();
                    } catch (Throwable e) {
                        Logs.warning("Error catch in thread " + m_id + " : " + todo.getName());
                        Logs.exception(e);
                    }

                    StringBuilder threadsInfos = new StringBuilder("[");
                    int remain;
                    int runnable = 0;
                    m_LOCKARRAY.lock();
                    {
                        for (Thread t : m_THREADS) {
                            threadsInfos.append(t.getState()).append(", ");
                            if (t.getState() == Thread.State.RUNNABLE || t.getState() == Thread.State.WAITING) {
                                ++runnable;
                            }
                        }
                        remain = m_task.size();
                    }
                    m_LOCKARRAY.unlock();
                    threadsInfos.append("]");

                    Logs.info("Task '" + todo.getName() + " from thread " + m_id + "  is finished : " + threadsInfos, true);
                    Logs.info("(" + todo.getName() + " : " + m_id + ") : remains " + remain + " task and " + runnable + " threads to wait", true);

                } else {
                    Logs.warning("No task to do by thread " + m_id);
                }
            }

        }
    }

}
