package eionet.doc.io.impl;

import eionet.doc.io.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Risto Alt
 * @author Nikolaos Nakas
 *
 */
@Service
public final class LocalFileService implements FileService {

    private static final Logger logger = Logger.getLogger(LocalFileService.class);
    
    private String storagePath;

    @Autowired
    public LocalFileService(FileStorageInfoProvider storageInfoProvider) {
        this.storagePath = storageInfoProvider.getFileStoragePath();
        File storageDir = new File(this.storagePath);
        
        if (!storageDir.exists() || !storageDir.isDirectory()) {
            // creates the directory, including any necessary but nonexistent parent directories
            try {
                FileUtils.forceMkdir(storageDir);
            } 
            catch (IOException ex) {
                logger.error("Failed to create folder: " + storageDir.getAbsolutePath(), ex);
            }
        }
    }
    
    @Override
    public void addFile(String fileName, InputStream inputStream) throws FileServiceException {
        File path = new File(this.storagePath, fileName);
        FileOutputStream outputStream = null;
        
        try {
            outputStream = new FileOutputStream(path);
            IOUtils.copy(inputStream, outputStream);
        }
        catch (IOException ex) {
            throw new FileServiceException(ex);
        }
        finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    public void deleteFile(String fileName) throws FileServiceException {
        File file = new File(this.storagePath, fileName);
        
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
        catch (SecurityException ex) {
            throw new FileServiceException(ex);
        }
    }

    @Override
    public File getFile(String fileName) throws FileServiceException {
        File file = new File(this.storagePath, fileName);
        
        try {
            if (!file.exists() || !file.isFile()) {
                return null;
            } 
            else {
                return file;
            }
        }
        catch (SecurityException ex) {
            throw new FileServiceException(ex);
        }
    }
}
