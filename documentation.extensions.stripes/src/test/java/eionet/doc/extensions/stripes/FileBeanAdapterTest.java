package eionet.doc.extensions.stripes;

import eionet.doc.dto.FileInfo;
import java.io.File;
import net.sourceforge.stripes.action.FileBean;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Nikolaos Nakas
 */
public class FileBeanAdapterTest {
    
    @Test
    public void testPropertyWrapping() {
        FileBean fbean = new FileBean(new File("temp.html"), "text/html", "dummy.html");
        FileInfo fInfo = new FileBeanToFileInfoConverter().convert(fbean);
        
        assertEquals(fbean.getFileName(), fInfo.getFileName());
        assertEquals(fbean.getContentType(), fInfo.getContentType());
    }
}
