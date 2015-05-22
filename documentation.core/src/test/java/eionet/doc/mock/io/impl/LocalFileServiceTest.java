package eionet.doc.mock.io.impl;

import eionet.doc.io.FileService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Nikolaos Nakas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:mock-spring-config.xml" })
public class LocalFileServiceTest {
    
    private static final String TEST_DATA = "atest\n";
    
    @Autowired
    private FileService fileService;
    
    private ByteArrayInputStream testInput;
    
    @Before
    public void setUp() throws Exception {
        testInput = new ByteArrayInputStream(TEST_DATA.getBytes("utf8"));
    }
    
    @After
    public void tearDown() throws IOException {
        testInput.close();
        testInput = null;
    }
    
    @Test
    public void happyPath() throws Exception {
        final String filename = "testfile.txt";
        
        fileService.addFile(filename, testInput);
        File f = fileService.getFile(filename);
        
        assertNotNull(f);
        assertEquals(filename, f.getName());
    }
    
    /**
     * It must not be possible for an attacker to place a file on the server at
     * location that is different from the file store.
     */
    @Ignore("There is no guard against abuse at this level")
    @Test
    public void tryAbuse() throws Exception {
        final String filename = "abusefile.txt";
        final String abuseFileName = "../" + filename;
        
        fileService.addFile(abuseFileName, testInput);
        File f = fileService.getFile(abuseFileName);

        assertNotNull(f);
        assertEquals(abuseFileName, f.getName());
    }
}
