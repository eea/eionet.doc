package eionet.doc.mock.dto;

import eionet.doc.dto.FileInfo;
import java.io.InputStream;

/**
 *
 * @author Nikolaos Nakas
 */
public final class MockFileInfo implements FileInfo {
    
    private String fileName;
    private String contentType;
    private InputStream inputStream;

    public MockFileInfo() { }
    
    public MockFileInfo(String fileName, String contentType, InputStream inputStream) {
        this.setFileName(fileName);
        this.setContentType(contentType);
        this.setInputStream(inputStream);
    }
    
    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
}
