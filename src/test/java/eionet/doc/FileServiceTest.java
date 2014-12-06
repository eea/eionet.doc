package eionet.doc;

import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;

public class FileServiceTest {

    @Test
    public void happyPath() throws Exception {
        String testData = "atest\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData.getBytes("utf8"));

        FileService fs = FileService.getInstance();
        fs.addFile("testfile.txt", testInput);
        File f = fs.getFile("testfile.txt");
        assertEquals("target/filestore/testfile.txt", f.toString());
    }

    @Ignore("There is no guard against abuse") @Test
    public void tryAbuse() throws Exception {
        String testData = "atest\n";
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData.getBytes("utf8"));

        FileService fs = FileService.getInstance();
        fs.addFile("../abusefile.txt", testInput);
        File f = fs.getFile("../abusefile.txt");
        assertEquals("target/filestore/abusefile.txt", f.toString());
    }
}
