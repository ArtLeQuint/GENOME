package Download;

import Exception.HTTPException;
import Utils.Logs;
import Utils.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IDownloader {

    /**
     * Connection
     */
    private HttpURLConnection m_connection;

    /**
     * Make an HTTP GET request
     *
     * @param _url The request URL to retrieve data from
     * @return The BufferedReader from which to read result
     * @throws IOException An error occurred while connecting to the server
     */
    protected BufferedReader get(URL _url) throws IOException, HTTPException {
        // Close previous connection if any
        if (m_connection != null)
            m_connection.disconnect();

        // Initialize request
        try {
            m_connection = (HttpURLConnection) _url.openConnection();
            m_connection.setConnectTimeout(Options.getConnectionTimeout());
            m_connection.setRequestMethod("GET");
        } catch (IOException e) {
            final String message = "Unable to initialize request";
            Logs.warning(message);
            Logs.exception(new IOException(message, e));
            throw e;
        }

        // Execute request
        int statusCode;
        try {
            // Get response code (executes request)
            statusCode = m_connection.getResponseCode();
        } catch (IOException e) {
            final String message = "Unable to establish connection with server";
            Logs.warning(message);
            Logs.exception(new IOException(message, e));
            throw e;
        }

        final BufferedReader in;
        if (statusCode == HttpURLConnection.HTTP_OK) {
            try {
                in = new BufferedReader(new InputStreamReader(m_connection.getInputStream()));
            } catch (IOException e) {
                final String message = "Unable to download data";
                Logs.warning(message);
                Logs.exception(new IOException(message, e));
                throw e;
            }
        } else {
            m_connection.disconnect();
            final String message = "Unable to download data : status " + String.valueOf(statusCode);
            Logs.warning(message);
            throw new HTTPException(message);
        }

        return in;
    }

    /**
     * Disconnect current connection and destroy socket
     */
    protected void disconnect() {
        if (m_connection != null) m_connection.disconnect();
    }
}
