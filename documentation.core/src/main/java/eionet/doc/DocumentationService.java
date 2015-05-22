package eionet.doc;

import eionet.doc.dto.DocPageDTO;

/**
 * @author Nikolaos Nakas
 */
public interface DocumentationService {

    public DocPageDTO view(String pageId, String event) throws Exception;

    /**
     * Edit page.
     *
     * @param pageObject
     * @throws Exception
     */
    public void editContent(DocPageDTO pageObject) throws Exception;

    /**
     * Adds content into documentation table.
     *
     * @param pageObject
     * @throws Exception
     */
    public void addContent(DocPageDTO pageObject) throws Exception;

    /**
     * Deletes content.
     *
     * @param pageObject
     * @throws Exception
     */
    public void delete(DocPageDTO pageObject) throws Exception;

}
