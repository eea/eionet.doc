package eionet.doc.io;

import java.io.File;
import java.io.InputStream;

/**
 * @author Nikolaos Nakas
 *
 */
public interface FileService {
    
    /**
     * Adds a file to the file storage.
     * 
     * @param fileName 
     * @param inputStream
     * @throws FileServiceException
     */
    public void addFile(String fileName, InputStream inputStream) throws FileServiceException;

    public void deleteFile(String fileName) throws FileServiceException;

    public File getFile(String fileName) throws FileServiceException;
    
}
