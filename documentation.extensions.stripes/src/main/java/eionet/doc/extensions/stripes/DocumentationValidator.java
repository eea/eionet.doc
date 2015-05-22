package eionet.doc.extensions.stripes;

import eionet.doc.dal.DocumentationRepository;
import eionet.doc.dto.DocPageDTO;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Nikolaos Nakas
 */
@Service(value = "documentationValidationHelper")
public final class DocumentationValidator {
    
    private final DocumentationRepository documentationRepository;
    
    @Autowired
    public DocumentationValidator(DocumentationRepository documentationRepository) {
        this.documentationRepository = documentationRepository;
    }

    public DocumentationRepository getDocumentationRepository() {
        return documentationRepository;
    }
    
    public void getStripesValidationErrors(DocPageDTO pageObject, ValidationErrors errorOutput) throws Exception {
        if (pageObject == null) {
            return;
        }
        
        // If overwrite = false, then check if page id already exists
        if (!StringUtils.isBlank(pageObject.getPid()) && !pageObject.isOverwrite()) {
            boolean exists = this.documentationRepository.idExists(pageObject.getPid());

            if (exists) {
                errorOutput.add("pageObject.pid", new SimpleError("Such Page ID already exists!"));
            }
        }

        if (pageObject.getFile() == null && StringUtils.isBlank(pageObject.getPid())) {
            errorOutput.add("pageObject.pid", new SimpleError("If no file is chosen, then Page ID is mandatory!"));
        }
    }
    
}
