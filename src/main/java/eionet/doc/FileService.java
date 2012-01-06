/**
 *
 */
package eionet.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eionet.doc.config.GeneralConfig;

/**
 * @author Risto Alt
 *
 */
public class FileService {

    private static final Logger logger = Logger.getLogger(FileService.class);
    private static String dir;

    /**
     *
     */
    private FileService() throws Exception {

        dir = GeneralConfig.getRequiredProperty(GeneralConfig.FILESTORE_PATH);
        if (StringUtils.isBlank(dir)) {
            throw new IllegalArgumentException("Missing property doc.files.folder");
        } else {

        }
        File fdir = new File(dir);
        if (!fdir.exists() || !fdir.isDirectory()) {
            // creates the directory, including any necessary but nonexistent parent directories
            try {
                FileUtils.forceMkdir(fdir);
            } catch (IOException e) {
                logger.error("Failed to create folder: " + fdir.getAbsolutePath(), e);
            }
        }
    }

    /**
     *
     * @return FileService
     */
    public static FileService getInstance() throws Exception {
        return new FileService();
    }

    public void addFile(String fileName, InputStream inputStream) throws Exception {
        String dir = GeneralConfig.getRequiredProperty(GeneralConfig.FILESTORE_PATH);
        File path = new File(dir, fileName);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            IOUtils.copy(inputStream, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    public void deleteFile(String fileName) throws Exception {
        String dir = GeneralConfig.getRequiredProperty(GeneralConfig.FILESTORE_PATH);
        File file = new File(dir, fileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public File getFile(String fileName) throws Exception {
        String dir = GeneralConfig.getRequiredProperty(GeneralConfig.FILESTORE_PATH);
        File file = new File(dir, fileName);
        if (!file.exists() || !file.isFile()) {
            return null;
        } else {
            return file;
        }
    }
}
