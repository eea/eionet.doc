package eionet.doc.io;

/**
 *
 * @author Nikolaos Nakas
 */
public class FileServiceException extends Exception {

    public FileServiceException() {
    }

    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileServiceException(Throwable cause) {
        super(cause);
    }
    
}
