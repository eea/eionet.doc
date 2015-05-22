package eionet.doc.impl;

import eionet.doc.dal.DocumentationRepository;
import eionet.doc.dto.DocPageDTO;
import eionet.doc.dto.DocumentationDTO;
import eionet.doc.dto.FileInfo;
import eionet.doc.dto.MessageDTO;
import eionet.doc.io.FileService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Risto Alt
 *
 */
@Service
public final class DocumentationService implements eionet.doc.DocumentationService {

    private final FileService fileService;
    
    private final DocumentationRepository documentationRepository;

    @Autowired
    public DocumentationService(DocumentationRepository documentationRepository, FileService fileService) {
        this.documentationRepository = documentationRepository;
        this.fileService = fileService;
    }
    
    @Override
    public DocPageDTO view(String pageId, String event) throws Exception {
        DocPageDTO ret = new DocPageDTO();
        
        if (StringUtils.isBlank(pageId) || (pageId != null && pageId.equals("contents"))) {
            if (pageId != null && pageId.equals("contents")) {
                ret.setDocs(this.documentationRepository.getDocObjects(false));
            } 
            else {
                ret.setDocs(this.documentationRepository.getDocObjects(true));
            }
        } 
        else {
            DocumentationDTO doc = this.documentationRepository.getDocObject(pageId);
            
            if (doc == null) {
                ret.getMessages().add(new MessageDTO("Such page ID doesn't exist in database: " + pageId, MessageDTO.CAUTION));
            } 
            else {
                if (!StringUtils.isBlank(event) && event.equals("edit")) {
                    ret.setContentType(doc.getContentType());
                    ret.setTitle(doc.getTitle());
                    
                    if (doc.getContentType().startsWith("text/")) {
                        File f = this.fileService.getFile(pageId);
                        
                        if (f != null) {
                            ret.setContent(FileUtils.readFileToString(f, "UTF-8"));
                            ret.setEditableContent(true);
                        } 
                        else {
                            ret.getMessages().add(new MessageDTO("File does not exist: " + pageId, MessageDTO.CAUTION));
                        }
                    }
                } 
                else {
                    if (doc.getContentType().startsWith("text/")) {
                        File f = this.fileService.getFile(pageId);
                        
                        if (f != null) {
                            ret.setContent(FileUtils.readFileToString(f, "UTF-8"));
                        } 
                        else {
                            ret.getMessages().add(new MessageDTO("File does not exist: " + pageId, MessageDTO.CAUTION));
                        }
                        
                        ret.setTitle(doc.getTitle());
                    } 
                    else {
                        File f = this.fileService.getFile(pageId);
                        ret.setContentType(doc.getContentType());
                        ret.setFis(new FileInputStream(f));
                    }
                }
            }
        }
        
        return ret;
    }

    @Override
    public void editContent(DocPageDTO pageObject) throws Exception {
        if (pageObject != null) {
            if (pageObject.getTitle() == null) {
                pageObject.setTitle("");
            }
            
            this.insertContent(pageObject);
        }
    }

    @Override
    public void addContent(DocPageDTO pageObject) throws Exception {
        if (pageObject != null) {
            // The page title is not mandatory. If it is not filled in, then it takes the value of the page_id.
            if (StringUtils.isBlank(pageObject.getTitle())) {
                pageObject.setTitle(pageObject.getPid());
            }
            
            this.insertContent(pageObject);
        }
    }

    @Override
    public void delete(DocPageDTO pageObject) throws Exception {
        if (pageObject != null && pageObject.getDocIds() != null) {
            for (String id : pageObject.getDocIds()) {
                // Delete data from database
                this.documentationRepository.deleteContent(id);
                // Delete file
                this.fileService.deleteFile(id);
            }
        }
    }
    
    private void insertContent(DocPageDTO pageObject) throws Exception {
        String pid = pageObject.getPid();
        String title = pageObject.getTitle();
        String contentType = pageObject.getContentType();
        String content = pageObject.getContent();
        FileInfo file = pageObject.getFile();

        // Extract file name.
        String fileName = pid;
        if (file != null && file.getFileName() != null) {
            if (StringUtils.isBlank(pid)) {
                fileName = file.getFileName();
                pid = fileName;
                // If title is still empty, then set it to file name
                if (StringUtils.isBlank(title)) {
                    title = fileName;
                }
            }
        }

        // If content type is not filled in, then it takes the content-type of the file.
        // If that's not available, then it is application/octet-stream.
        // If file is null the the content-type is "text/html"
        if (StringUtils.isBlank(contentType)) {
            if (file != null) {
                contentType = file.getContentType();
                if (StringUtils.isBlank(contentType)) {
                    contentType = "application/octet-stream";
                }
            } else {
                contentType = "text/html";
            }
        }

        DocumentationDTO documentation = new DocumentationDTO(pid, contentType, title);
        InputStream is = null;
        
        try {
            if (file != null) {
                is = file.getInputStream();
            } 
            else if (content != null) {
                is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            } 
            else {
                is = new ByteArrayInputStream("".getBytes());
            }
        
            this.fileService.addFile(fileName, is);
            this.documentationRepository.insertContent(documentation);
        }
        finally {
            IOUtils.closeQuietly(is);
        }
    }

}
