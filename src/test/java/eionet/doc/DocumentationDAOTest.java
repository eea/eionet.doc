package eionet.doc;

import eionet.doc.dto.DocumentationDTO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Class to test database operations.
 * The table is recreated between tests.
 */
public class DocumentationDAOTest extends DBFunctions {

    /** Object to hold the properties of a documentation object. */
    private DocumentationDAO instance;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        instance = DocumentationDAO.getInstance();
    }

    @Test
    public void happyPath() throws Exception {
        String pageId = "testfile.txt";

        instance.insertContent(pageId, "text/html", "title €");
        assertTrue(instance.idExists(pageId));
        instance.deleteContent(pageId);
        assertFalse(instance.idExists(pageId));
    }

    @Test
    public void checkTitle() throws Exception {
        String pageId = "eurotitle.txt";
        String pageTitle = "title €";

        instance.insertContent(pageId, "text/html", pageTitle);
        DocumentationDTO docObj = instance.getDocObject(pageId);
        assertEquals(pageTitle, docObj.getTitle());
        instance.deleteContent(pageId);
    }

    @Test
    public void lookForNonExistent() throws Exception {
        String pageId = "testfile.txt";
        DocumentationDTO docObj = instance.getDocObject(pageId);
        assertEquals(null, docObj);
    }
}
