package eionet.doc;

import eionet.doc.dal.DocumentationRepository;
import eionet.doc.db.DatabaseDependentTest;
import eionet.doc.dto.DocPageDTO;
import eionet.doc.dto.DocumentationDTO;
import eionet.doc.dto.FileInfo;
import eionet.doc.io.FileService;
import eionet.doc.mock.dto.MockFileInfo;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class to test documentation service.
 * The table is recreated between each test.
 */
public class DocumentationServiceTest extends DatabaseDependentTest {
    
    @Autowired
    private DocumentationService documentationService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private DocumentationRepository documentationRepository;

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

        documentationService.addContent(insertDpd);

        assertTrue(documentationRepository.idExists(specifiedPageId));

        DocumentationDTO readDoc = documentationRepository.getDocObject(specifiedPageId);
        assertEquals(specifiedPageId, readDoc.getPageId());
        assertEquals(specifiedContentType, readDoc.getContentType());
        // Check that the title has been set from page id
        assertEquals(specifiedPageId, readDoc.getTitle());

        DocPageDTO readDpd;

        // Check via the view method
        readDpd = documentationService.view(specifiedPageId, null);
        assertTrue(readDpd.getMessages().isEmpty());
        assertEquals(20, readDpd.getContent().length());

        // Check via the edit event in the view method
        readDpd = documentationService.view(specifiedPageId, "edit");
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

        documentationService.addContent(insertDpd);

        String forcedPageId = "testfile.txt";
        // Look up directly in database
        assertTrue(documentationRepository.idExists(forcedPageId));

        DocumentationDTO readDoc = documentationRepository.getDocObject(forcedPageId);
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
        documentationService.addContent(insertDpd1);

        // Add an image
        DocPageDTO insertDpd2 = new DocPageDTO();
        insertDpd2.setPid("image2.gif");
        insertDpd2.setContentType("image/gif");
        insertDpd2.setFile(constructFilePayload());
        documentationService.addContent(insertDpd2);

        DocPageDTO readDpd;

        readDpd = documentationService.view("contents", null);
        assertEquals(2, readDpd.getDocs().size());

        readDpd = documentationService.view("", null);
        assertEquals(1, readDpd.getDocs().size());
    }

    /**
     * Check that delete works.
     */
    @Test
    public void deletePage() throws Exception {
        String specifiedPageId = "page1.html";
        
        // Use another method to quickly generate some files.
        listContent();

        assertNotNull(fileService.getFile(specifiedPageId));
        assertTrue(documentationRepository.idExists(specifiedPageId));

        DocPageDTO deleteDpd = new DocPageDTO();
        List<String> docIds = new ArrayList<String>();
        docIds.add(specifiedPageId);
        deleteDpd.setDocIds(docIds);

        documentationService.delete(deleteDpd);

        assertNull(fileService.getFile(specifiedPageId));
        assertFalse(documentationRepository.idExists(specifiedPageId));

    }

    private FileInfo constructFilePayload() throws Exception {
        URL u = DocumentationServiceTest.class.getClassLoader().getResource("testfile.txt");
        File file = new File(u.toURI()); // Take one from src/test/resources
        
        return new MockFileInfo(file.getName(), "text/plain", new FileInputStream(file));
    }
}
