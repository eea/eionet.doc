package eionet.doc.extensions.stripes;

import eionet.doc.dal.DocumentationRepository;
import eionet.doc.dto.DocPageDTO;
import java.io.File;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.validation.ValidationErrors;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Nikolaos Nakas
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentationValidatorTest {
    
    @Mock
    private DocumentationRepository documentationRepository;
    
    @InjectMocks
    private DocumentationValidator validationHelper;

    @Test
    public void happyPath() throws Exception {
        DocPageDTO page = new DocPageDTO();
        page.setPid("pid");
        
        when(documentationRepository.idExists(any(String.class))).thenReturn(false);
        
        ValidationErrors errors = new ValidationErrors();
        validationHelper.getStripesValidationErrors(page, errors);
        
        assertEquals(0, errors.size());
    }
    
    public void nullPage() throws Exception {
        when(documentationRepository.idExists(any(String.class))).thenReturn(false);
        
        ValidationErrors errors = new ValidationErrors();
        validationHelper.getStripesValidationErrors(null, errors);
        
        assertEquals(0, errors.size());
    }
    
    @Test
    public void pageIdExists() throws Exception {
        DocPageDTO page = new DocPageDTO();
        page.setPid("pid");
        
        when(documentationRepository.idExists(page.getPid())).thenReturn(true);
        
        ValidationErrors errors = new ValidationErrors();
        validationHelper.getStripesValidationErrors(page, errors);
        
        assertEquals(1, errors.size());
    }
    
    @Test
    public void pageIdExistsAndIsOverwrite() throws Exception {
        DocPageDTO page = new DocPageDTO();
        page.setPid("pid");
        page.setOverwrite(true);
        
        when(documentationRepository.idExists(page.getPid())).thenReturn(true);
        
        ValidationErrors errors = new ValidationErrors();
        validationHelper.getStripesValidationErrors(page, errors);
        
        assertEquals(0, errors.size());
    }
    
    @Test
    public void nullPageIdWithoutAttachedFile() throws Exception {
        DocPageDTO page = new DocPageDTO();
        
        when(documentationRepository.idExists(any(String.class))).thenReturn(false);
        
        ValidationErrors errors = new ValidationErrors();
        validationHelper.getStripesValidationErrors(page, errors);
        
        assertEquals(1, errors.size());
    }
    
    @Test
    public void nullPageIdWithAttachedFile() throws Exception {
        DocPageDTO page = new DocPageDTO();
        page.setFile(new FileBeanToFileInfoConverter().convert(new FileBean(new File(""), "", "")));
        
        when(documentationRepository.idExists(any(String.class))).thenReturn(false);
        
        ValidationErrors errors = new ValidationErrors();
        validationHelper.getStripesValidationErrors(page, errors);
        
        assertEquals(0, errors.size());
    }
}
