/**
 *
 */
package eionet.doc.dto;

import java.io.Serializable;

/**
 * @author Risto Alt
 *
 */
public class DocumentationDTO implements Serializable {

    /**
     * serial
     */
    private static final long serialVersionUID = 1L;

    private String pageId;
    private String contentType;
    private String title;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
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

}
