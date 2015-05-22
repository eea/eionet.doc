package eionet.doc.dto;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for documentation page. This object SHOULD NOT be filled directly
 * by Stripes. Actually it should not have anything to do with Stripes, at all.-
 *
 * @author Risto Alt
 */
public class DocPageDTO implements Serializable {

    /**
     * serial
     */
    private static final long serialVersionUID = 1L;

    private String content;
    private List<DocumentationDTO> docs;

    /**
     * Properties for documentation add page.
     */
    private String pid;
    private FileInfo file;
    private String contentType;
    private String title;

    private boolean editableContent;
    private boolean overwrite;
    private FileInputStream fis;
    private List<String> docIds = new ArrayList<String>();

    /** System messages to show the user. */
    private List<MessageDTO> messages = new ArrayList<MessageDTO>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<DocumentationDTO> getDocs() {
        return docs;
    }

    public void setDocs(List<DocumentationDTO> docs) {
        this.docs = docs;
    }

    public String getPid() {
        return pid;
    }

    /**
     * Set the page id. Cuts the page id at '/', '\' and ':' as these are path
     * separators in some systems. The DocumentationService uses the Pid to fetch
     * a file in the filesystem.
     *
     * @param pid - page id.
     */
    public void setPid(String pid) {
        if (pid == null) {
            this.pid = null;
        } else {
            int fnPart = Math.max(Math.max(pid.lastIndexOf('/'), pid.lastIndexOf('\\')),
                    pid.lastIndexOf(':')) + 1;
            this.pid = pid.substring(fnPart);
        }
    }

    public FileInfo getFile() {
        return file;
    }

    public void setFile(FileInfo file) {
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEditableContent() {
        return editableContent;
    }

    public void setEditableContent(boolean editableContent) {
        this.editableContent = editableContent;
    }

    public FileInputStream getFis() {
        return fis;
    }

    public void setFis(FileInputStream fis) {
        this.fis = fis;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public List<String> getDocIds() {
        return docIds;
    }

    public void setDocIds(List<String> docIds) {
        this.docIds = docIds;
    }

}
