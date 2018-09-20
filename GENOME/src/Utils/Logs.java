package Utils;

import java.io.*;

public final class Logs {

    /**
     * File's name
     */
    private static final String s_LOGS_FILE_NAME = "log.txt";
    /**
     * Buffer to write in file
     */
    private static BufferedWriter s_file = null;
    /**
     * Logs listener
     */
    private static LogsListener s_logsListener = (_message, _type) -> {
    };

    /**
     * Open the log file in order to write in it
     */
    public static void initializeLog() {
        if (s_file == null) {
            File file = new File(s_LOGS_FILE_NAME);
            try {
                s_file = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close the log file
     */
    public static void finalizeLog() {
        try {
            if (s_file != null)
                s_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the listener
     *
     * @param _logsListener the listener to set
     */
    public static void setListener(LogsListener _logsListener) {
        s_logsListener = _logsListener;
    }

    /**
     * Print a message in the log file
     *
     * @param _message the message to print
     */
    public static void info(String _message, boolean _notify) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] info : ";
        final String message = header + " " + _message;
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (_notify) {
            s_logsListener.logsEvent("Info : " + _message, Type.INFO);
        }
    }

    /**
     * Print a message in the log file
     *
     * @param _message the message to print
     */
    public static void notice(String _message, boolean _notify) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] notice : ";
        final String message = header + " " + _message;
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (_notify) {
            s_logsListener.logsEvent("Notice : " + _message, Type.NOTICE);
        }
    }

    /**
     * Print a message in the log file
     *
     * @param _message the message to print
     */
    public static void warning(String _message) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] warning : ";
        final String message = header + " " + _message;
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        s_logsListener.logsEvent("Warning : " + _message, Type.WARNING);
    }

    /**
     * Print a exception in the log file
     *
     * @param _exception the message to print
     */
    public static void exception(Throwable _exception) {
        StringWriter errors = new StringWriter();
        _exception.printStackTrace(new PrintWriter(errors));

        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] exception : ";
        final String message = header + " " + errors.toString();
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        s_logsListener.logsEvent("Exception : " + errors.toString(), Type.EXCEPTION);
    }

    /**
     * Type of the log
     */
    public enum Type {
        INFO,
        NOTICE,
        WARNING,
        EXCEPTION
    }

    /**
     * Use to get flux
     */
    public interface LogsListener {
        void logsEvent(String _message, Type _type);
    }
}
