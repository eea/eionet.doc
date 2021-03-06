package eionet.doc.dto;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DocPageDTOTest {

    @Test
    public void happyPath() {
        DocPageDTO dpd = new DocPageDTO();

        dpd.setPid("filename.txt");
        assertEquals("filename.txt", dpd.getPid());
    }

    @Test
    public void abusivePath() {
        DocPageDTO dpd = new DocPageDTO();

        dpd.setPid("../../../etc/passwd");
        assertEquals("passwd", dpd.getPid());
    }

    @Test
    public void absolutePath() {
        DocPageDTO dpd = new DocPageDTO();

        dpd.setPid("/etc/passwd");
        assertEquals("passwd", dpd.getPid());
    }

    @Test
    public void windowsPath() {
        DocPageDTO dpd = new DocPageDTO();

        dpd.setPid("C:Windows\\system.reg");
        assertEquals("system.reg", dpd.getPid());
    }
}
