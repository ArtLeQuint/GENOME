package Download.Tests;

import Download.GenbankCDS;
import Exception.HTTPException;
import Exception.OutOfMemoryException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GenbankCDSTest {
    @Test
    void genbankCDS() throws HTTPException, IOException, OutOfMemoryException {
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                GenbankCDS gr = new GenbankCDS("NC_0077" + i + j);
                gr.download();
                assertTrue(gr.getRefseqData().indexOf("ORIGIN") != -1);
                assertTrue(gr.getRefseqData().indexOf("//") != -1);
            }
        }
    }
}