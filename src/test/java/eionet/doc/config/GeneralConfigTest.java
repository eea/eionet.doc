package eionet.doc.config;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GeneralConfigTest {

    @Test
    public void simpleInstance() throws Exception {
        String result = GeneralConfig.getRequiredProperty(GeneralConfig.FILESTORE_PATH);
        assertEquals("target/filestore", result);
    }
}
