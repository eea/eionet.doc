/**
 *
 */
package eionet.doc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import eionet.doc.dto.DocPageDTO;
import eionet.doc.dto.DocumentationDTO;
import eionet.doc.dto.MessageDTO;

/**
 * @author Risto Alt
 *
 */
public class DocumentationService {

    /**
     * @param pageId
     * @param event
     * @return FileService
     */
    public static DocumentationService getInstance() throws Exception {
        return new DocumentationService();
    }

    public DocPageDTO view(String pageId, String event) throws Exception {

        DocPageDTO ret = new DocPageDTO();
        if (StringUtils.isBlank(pageId) || (pageId != null && pageId.equals("contents"))) {
            if (pageId != null && pageId.equals("contents")) {
                ret.setDocs(DocumentationDAO.getInstance().getDocObjects(false));
            } else {
                ret.setDocs(DocumentationDAO.getInstance().getDocObjects(true));
            }
        } else {
            DocumentationDTO doc = DocumentationDAO.getInstance().getDocObject(pageId);
            if (doc == null) {
                ret.getMessages().add(new MessageDTO("Such page ID doesn't exist in database: " + pageId, MessageDTO.CAUTION));
            } else {
                if (!StringUtils.isBlank(event) && event.equals("edit")) {
                    ret.setContentType(doc.getContentType());
                    ret.setTitle(doc.getTitle());
                    if (doc.getContentType().startsWith("text/")) {
                        File f = FileService.getInstance().getFile(pageId);
                        if (f != null) {
                            ret.setContent(FileUtils.readFileToString(f, "UTF-8"));
                            ret.setEditableContent(true);
                        } else {
                            ret.getMessages().add(new MessageDTO("File does not exist: " + pageId, MessageDTO.CAUTION));
                        }
                    }
                } else {
                    if (doc.getContentType().startsWith("text/")) {
                        File f = FileService.getInstance().getFile(pageId);
                        if (f != null) {
                            ret.setContent(FileUtils.readFileToString(f, "UTF-8"));
                        } else {
                            ret.getMessages().add(new MessageDTO("File does not exist: " + pageId, MessageDTO.CAUTION));
                        }
                        ret.setTitle(doc.getTitle());
                    } else {
                        File f = FileService.getInstance().getFile(pageId);
                        ret.setContentType(doc.getContentType());
                        ret.setFis(new FileInputStream(f));
                        //return new StreamingResolution(doc.getContentType(), new FileInputStream(f));
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Edit page
     * @param pageObject
     * @param isVirtuoso - does the application uses Virtuoso DB or MySql
     * @throws Exception
     */
    public void editContent(DocPageDTO pageObject, boolean isVirtuoso) throws Exception {
        if (pageObject != null) {
            if (pageObject.getTitle() == null) {
                pageObject.setTitle("");
            }
            insertContent(pageObject, isVirtuoso);
        }
    }

    /**
     * Adds content into documentation table
     * @param pageObject
     * @param isVirtuoso - does the application uses Virtuoso DB or MySql
     * @throws Exception
     */
    public void addContent(DocPageDTO pageObject, boolean isVirtuoso) throws Exception {

        if (pageObject != null) {
            // The page title is not mandatory. If it is not filled in, then it takes the value of the page_id.
            if (StringUtils.isBlank(pageObject.getTitle())) {
                pageObject.setTitle(pageObject.getPid());
            }
            insertContent(pageObject, isVirtuoso);
        }
    }

    /**
     * Insert content into database
     * @param pageObject
     * @param isVirtuoso - does the application uses Virtuoso DB or MySql
     * @throws Exception
     */
    private void insertContent(DocPageDTO pageObject, boolean isVirtuoso) throws Exception {

        String pid = pageObject.getPid();
        String title = pageObject.getTitle();
        String contentType = pageObject.getContentType();
        String content = pageObject.getContent();
        FileBean file = pageObject.getFile();

        // Extract file name.
        String fileName = pid;
        if (file != null && file.getFileName() != null) {
            if (StringUtils.isBlank(pid)) {
                fileName = file.getFileName();
                pid = fileName;
                // If title is still empty, then set it to file name
                if (StringUtils.isBlank(title)) {
                    title = fileName;
                }
            }
        }

        // If content type is not filled in, then it takes the content-type of the file.
        // If that's not available, then it is application/octet-stream.
        // If file is null the the content-type is "text/html"
        if (StringUtils.isBlank(contentType)) {
            if (file != null) {
                contentType = file.getContentType();
                if (StringUtils.isBlank(contentType)) {
                    contentType = "application/octet-stream";
                }
            } else {
                contentType = "text/html";
            }
        }

        InputStream is = null;
        if (file != null) {
            is = file.getInputStream();
        } else if (content != null) {
            is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        } else {
            is = new ByteArrayInputStream("".getBytes());
        }
        FileService.getInstance().addFile(fileName, is);
        DocumentationDAO.getInstance().insertContent(pid, contentType, title, isVirtuoso);
    }

    /**
     * Deletes content
     *
     * @param docIds
     * @throws Exception
     */
    public void delete(DocPageDTO pageObject) throws Exception {

        if (pageObject != null && pageObject.getDocIds() != null) {
            // The page title is not mandatory. If it is not filled in, then it takes the value of the page_id.
            for (String id : pageObject.getDocIds()) {
                // Delete data from database
                DocumentationDAO.getInstance().deleteContent(id);

                // Delete file
                FileService.getInstance().deleteFile(id);
            }
        }
    }

    public ValidationErrors getStripesValidationErrors(DocPageDTO pageObject, ValidationErrors errors) throws Exception {

        if (pageObject != null && errors != null) {
            // If overwrite = false, then check if page id already exists
            if (!StringUtils.isBlank(pageObject.getPid()) && !pageObject.isOverwrite()) {
                boolean exists = DocumentationDAO.getInstance().idExists(pageObject.getPid());
                if (exists) {
                    errors.add("pageObject.pid", new SimpleError("Such Page ID already exists!"));
                }
            }
            if (pageObject.getFile() == null && StringUtils.isBlank(pageObject.getPid())) {
                errors.add("pageObject.pid", new SimpleError("If no file is chosen, then Page ID is mandatory!"));
            }
        }

        return errors;
    }

}
