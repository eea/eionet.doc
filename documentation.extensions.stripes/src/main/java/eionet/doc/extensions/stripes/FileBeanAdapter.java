package eionet.doc.extensions.stripes;

import eionet.doc.dto.FileInfo;
import java.io.IOException;
import java.io.InputStream;
import net.sourceforge.stripes.action.FileBean;

/**
 *
 * @author Nikolaos Nakas
 */
final class FileBeanAdapter implements FileInfo {
    
    private final FileBean source;
    
    public FileBeanAdapter(FileBean source) {
        this.source = source;
    }
    
    @Override
    public String getFileName() {
        return this.source.getFileName();
    }

    @Override
    public String getContentType() {
        return this.source.getContentType();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.source.getInputStream();
    }
    
}
