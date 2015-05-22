package eionet.doc.dto;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Nikolaos Nakas
 * 
 * Intermediate protocol to inject file content information in the eionet.doc module. 
 * It was introduced to eliminate the dependency to the Stripes FileBean structure.
 */
public interface FileInfo {
    
    String getFileName();
    
    String getContentType();
    
    InputStream getInputStream() throws IOException;
    
}