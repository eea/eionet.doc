package eionet.doc.dal;

import eionet.doc.dto.DocumentationDTO;
import java.util.List;

/**
 *
 * @author Nikolaos Nakas
 */
public interface DocumentationRepository {

    /**
     * Gets the documentation info for a given page.
     * 
     * @param pageId the id of the page to be fetched.
     * @return a documentation object if the page exists; null otherwise.
     * @throws DataAccessException in case of a data access error.
     */
    DocumentationDTO getDocObject(String pageId) throws DataAccessException;
    
    /**
     * Gets the list of documentation pages.
     * 
     * @param htmlOnly set to 'true' to fetch only HTML based pages.
     * @return a list of documentation objects.
     * @throws DataAccessException in case of a data access error.
     */
    List<DocumentationDTO> getDocObjects(boolean htmlOnly) throws DataAccessException;
    
    /**
     * Inserts a new documentation object in the database. Any existing documentation object 
     * with the same page id will be deleted.
     * 
     * @param documentation the documentation object to be inserted.
     * @throws DataAccessException in case of a data access error.
     */
    void insertContent(DocumentationDTO documentation) throws DataAccessException;
    
    /**
     * Checks if any documentation object with the given page id exists.
     * 
     * @param pageId the page id to search for.
     * @return true if the page id is found; false otherwise.
     * @throws DataAccessException in case of a data access error.
     */
    boolean idExists(String pageId) throws DataAccessException;
    
    /**
     * Deletes any documentation object with a matching page id,
     * 
     * @param pageId the page id of the documentation object to delete.
     * @throws DataAccessException in case of a data access error.
     */
    void deleteContent(String pageId) throws DataAccessException;
}
