package eionet.doc.extensions.stripes;

import eionet.doc.dto.FileInfo;
import net.sourceforge.stripes.action.FileBean;

/**
 *
 * @author Nikolaos Nakas
 */
public final class FileBeanToFileInfoConverter {
    
    public FileInfo convert(FileBean fileBean) {
        if (fileBean == null) {
            return null;
        }
        
        return new FileBeanAdapter(fileBean);
    }
    
}
