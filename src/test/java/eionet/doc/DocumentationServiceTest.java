package eionet.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eionet.doc.config.GeneralConfig;
import eionet.doc.dto.DocPageDTO;
import eionet.doc.dto.DocumentationDTO;
import net.sourceforge.stripes.action.FileBean;

/**
 * Class to test documentation service.
 * The table is recreated between each test.
 */
public class DocumentationServiceTest extends DBFunctions {

    /** Object to hold the properties of a documentation object. */
    private DocumentationService instance;

    /**
     * Construct an object that is identical to one that Stripes produces
     * when there is a file upload on the webform.
     */
    private FileBean constructFilePayload() throws Exception {
        URL u = GeneralConfig.class.getClassLoader().getResource("testfile.txt");
        File file = new File(u.toURI()); // Take one from src/test/resources
        FileBean fileBean = new FileBean(file, "text/plain", "testfile.txt");
        return fileBean;
    }
 
    @Before
    public void setUp() throws Exception {
        super.setUp();
        instance = DocumentationService.getInstance();
    }

    /**
     * If the user specifies a page id, but no title, then the title is made equal to the page id.
     */
    @Test
    public void happyPath() throws Exception {
        String specifiedPageId = "page1.html";
        String specifiedContentType = "text/html";
        DocPageDTO insertDpd = new DocPageDTO();
        insertDpd.setPid(specifiedPageId);
        insertDpd.setContentType(specifiedContentType);
        insertDpd.setFile(constructFilePayload());

        instance.addContent(insertDpd, false);

        DocumentationDAO docDAO = DocumentationDAO.getInstance();
        assertTrue(docDAO.idExists(specifiedPageId));

        DocumentationDTO readDoc = docDAO.getDocObject(specifiedPageId);
        assertEquals(specifiedPageId, readDoc.getPageId());
        assertEquals(specifiedContentType, readDoc.getContentType());
        // Check that the title has been set from page id
        assertEquals(specifiedPageId, readDoc.getTitle());

        DocPageDTO readDpd;

        // Check via the view method
        readDpd = instance.view(specifiedPageId, null);
        assertTrue(readDpd.getMessages().isEmpty());
        assertEquals(20, readDpd.getContent().length());

        // Check via the edit event in the view method
        readDpd = instance.view(specifiedPageId, "edit");
        assertTrue(readDpd.getMessages().isEmpty());
        assertEquals(specifiedContentType, readDpd.getContentType());
        assertEquals(specifiedPageId, readDpd.getTitle());

    }

    /**
     * If the user only uploads a file without filling out the page id, title or content type,
     * then the page id is the same as the uploaded filename, title is the same as the uploaded filename
     * and content type is also taken from the file object.
     */
    @Test
    public void noPageId() throws Exception {
        String specifiedPageId = "";
        String specifiedContentType = "";
        DocPageDTO insertDpd = new DocPageDTO();
        insertDpd.setPid(specifiedPageId);
        insertDpd.setContentType(specifiedContentType);
        insertDpd.setFile(constructFilePayload());

        instance.addContent(insertDpd, false);

        String forcedPageId = "testfile.txt";
        // Look up directly in database
        DocumentationDAO docDAO = DocumentationDAO.getInstance();
        assertTrue(docDAO.idExists(forcedPageId));

        DocumentationDTO readDoc = docDAO.getDocObject(forcedPageId);
        assertEquals(forcedPageId, readDoc.getPageId());
        assertEquals("text/plain", readDoc.getContentType());
        // Check that the title has been set from the file name
        assertEquals("testfile.txt", readDoc.getTitle());

        //instance.delete(insertDpd);
    }

    /**
     * Insert a page and an image into the database and check if they are on the table of content.
     */
    @Test
    public void listContent() throws Exception {
        // Add a HTML page
        DocPageDTO insertDpd1 = new DocPageDTO();
        insertDpd1.setPid("page1.html");
        insertDpd1.setContentType("text/html");
        insertDpd1.setFile(constructFilePayload());
        instance.addContent(insertDpd1, false);

        // Add an image
        DocPageDTO insertDpd2 = new DocPageDTO();
        insertDpd2.setPid("image2.gif");
        insertDpd2.setContentType("image/gif");
        insertDpd2.setFile(constructFilePayload());
        instance.addContent(insertDpd2, false);

        DocPageDTO readDpd;

        readDpd = instance.view("contents", null);
        assertEquals(2, readDpd.getDocs().size());

        readDpd = instance.view("", null);
        assertEquals(1, readDpd.getDocs().size());
    }

    /**
     * Check that delete works.
     */
    @Test
    public void deletePage() throws Exception {
        String specifiedPageId = "page1.html";
        DocumentationDAO docDAO = DocumentationDAO.getInstance();
        FileService fileSvc = FileService.getInstance();

        // Use another method to quickly generate some files.
        listContent();

        assertNotNull(fileSvc.getFile(specifiedPageId));
        assertTrue(docDAO.idExists(specifiedPageId));

        DocPageDTO deleteDpd = new DocPageDTO();
        List<String> docIds = new ArrayList<String>();
        docIds.add(specifiedPageId);
        deleteDpd.setDocIds(docIds);

        instance.delete(deleteDpd);

        assertNull(fileSvc.getFile(specifiedPageId));
        assertFalse(docDAO.idExists(specifiedPageId));

    }

}
