package eionet.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eionet.doc.config.GeneralConfig;

public class FileServiceTest {

    private String fileStore;
    private String testData = "atest\n";
    private ByteArrayInputStream testInput;
    private FileService fs;

    @Before
    public void setUp() throws Exception {
        fileStore = GeneralConfig.getRequiredProperty(GeneralConfig.FILESTORE_PATH);
        testInput = new ByteArrayInputStream(testData.getBytes("utf8"));
        fs = FileService.getInstance();
    }

    @Test
    public void happyPath() throws Exception {
        fs.addFile("testfile.txt", testInput);
        File f = fs.getFile("testfile.txt");
        assertNotNull(f);
        assertEquals(fileStore, f.getParent());
        assertEquals("testfile.txt", f.getName());
    }

    /**
     * It must not be possible for an attacker to place a file on the server at
     * location that is different from the file store.
     */
    @Ignore("There is no guard against abuse at this level")
    @Test
    public void tryAbuse() throws Exception {
        fs.addFile("../abusefile.txt", testInput);
        File f = fs.getFile("../abusefile.txt");

        assertNotNull(f);
        assertEquals(fileStore, f.getParent());
        assertEquals("abusefile.txt", f.getName());
    }
}
