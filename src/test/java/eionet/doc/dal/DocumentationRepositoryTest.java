package eionet.doc.dal;

import eionet.doc.db.DatabaseDependentTest;
import eionet.doc.dto.DocumentationDTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Nikolaos Nakas
 */
public class DocumentationRepositoryTest extends DatabaseDependentTest {
    
    @Autowired
    private DocumentationRepository documentationRepository;
    
    @Test
    public void happyPath() throws Exception {
        String pageId = "testfile.txt";
        DocumentationDTO doc = new DocumentationDTO(pageId, "text/html", "title €");
        
        documentationRepository.insertContent(doc);
        assertTrue(documentationRepository.idExists(pageId));
        
        documentationRepository.deleteContent(pageId);
        assertFalse(documentationRepository.idExists(pageId));
    }

    @Test
    public void checkTitle() throws Exception {
        String pageId = "eurotitle.txt";
        String pageTitle = "title €";
        DocumentationDTO doc = new DocumentationDTO(pageId, "text/html", pageTitle);
        
        documentationRepository.insertContent(doc);
        DocumentationDTO docObj = documentationRepository.getDocObject(pageId);
        assertEquals(pageTitle, docObj.getTitle());
    }

    @Test
    public void lookForNonExistent() throws Exception {
        String pageId = "testfile.txt";
        DocumentationDTO docObj = documentationRepository.getDocObject(pageId);
        assertEquals(null, docObj);
    }
}
