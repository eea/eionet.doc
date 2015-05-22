/**
 *
 */
package eionet.doc.dto;

import java.io.Serializable;

/**
 * @author Risto Alt
 *
 */
public class MessageDTO implements Serializable {

    /**
     * serial
     */
    private static final long serialVersionUID = 1L;

    public static final String CAUTION = "caution";
    public static final String SYSTEM = "system";

    private String message;
    private String type;

    public MessageDTO(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
