package Download.Tests;

import Download.IDownloader;
import Exception.HTTPException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IDownloaderTest {

    static private TestIDownloader m_IDownload;

    @BeforeAll
    static void setUp() {
        m_IDownload = new TestIDownloader();
    }

    @AfterAll
    static void tearDown() {
        m_IDownload.disconnect();
    }

    @Test
    void get() {
        assertThrows(MalformedURLException.class, () -> m_IDownload.get(new URL("")));
    }

    static class TestIDownloader extends IDownloader {
        @Override
        protected BufferedReader get(URL _url) throws IOException, HTTPException {
            return super.get(_url);
        }

        @Override
        protected void disconnect() {
            super.disconnect();
        }
    }
}