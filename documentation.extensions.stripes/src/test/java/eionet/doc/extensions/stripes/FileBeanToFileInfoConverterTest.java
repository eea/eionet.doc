package eionet.doc.extensions.stripes;

import eionet.doc.dto.FileInfo;
import java.io.File;
import net.sourceforge.stripes.action.FileBean;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Nikolaos Nakas
 */
public class FileBeanToFileInfoConverterTest {
    
    @Test
    public void testNull() {
        FileInfo fInfo = new FileBeanToFileInfoConverter().convert(null);
        Assert.assertNull(fInfo);
    }
    
    @Test
    public void testNotNull() {
        FileBean fbean = new FileBean(new File("dummy.txt"), "text/html", "dummy.txt");
        FileInfo fInfo = new FileBeanToFileInfoConverter().convert(fbean);
        Assert.assertNotNull(fInfo);
    }
}
