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
            } else {
                ret.setDocs(this.documentationRepository.getDocObjects(true));
            }
        } else {
            DocumentationDTO doc = this.documentationRepository.getDocObject(pageId);

            if (doc == null) {
                ret.getMessages().add(new MessageDTO("Such page ID doesn't exist in database: " + pageId, MessageDTO.CAUTION));
            } else if (!StringUtils.isBlank(event) && event.equals("edit")) {
                ret.setContentType(doc.getContentType());
                ret.setTitle(doc.getTitle());

                if (doc.getContentType().startsWith("text/")) {
                    File f = this.fileService.getFile(pageId);

                    if (f != null) {
                        ret.setContent(FileUtils.readFileToString(f, "UTF-8"));
                        ret.setEditableContent(true);
                    } else {
                        ret.getMessages().add(new MessageDTO("File does not exist: " + pageId, MessageDTO.CAUTION));
                    }
                }
            } else if (doc.getContentType().startsWith("text/")) {
                File f = this.fileService.getFile(pageId);

                if (f != null) {
                    ret.setContent(FileUtils.readFileToString(f, "UTF-8"));
                } else {
                    ret.getMessages().add(new MessageDTO("File does not exist: " + pageId, MessageDTO.CAUTION));
                }

                ret.setTitle(doc.getTitle());
            } else {
                File f = this.fileService.getFile(pageId);
                ret.setContentType(doc.getContentType());
                ret.setFis(new FileInputStream(f));
            }
        }

        return ret;
    }

    @Override
    public void editContent(DocPageDTO pageObject) throws Exception {
        if (pageObject != null) {
            this.insertContent(pageObject, true);
        }
    }

    @Override
    public String addContent(DocPageDTO pageObject) throws Exception {
        if (pageObject != null) {
            return this.insertContent(pageObject, false);
        }
        return null;
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

    private String insertContent(DocPageDTO pageObject, boolean isEdit) throws Exception {
        String pid = pageObject.getPid();
        String title = pageObject.getTitle();
        String contentType = pageObject.getContentType();
        String content = pageObject.getContent();
        FileInfo file = pageObject.getFile();

        String fileName = setFileName(file, pid);

        if (StringUtils.isBlank(pid)) {
            pid = setPid(file);
        }

        if (StringUtils.isBlank(title)) {
            title = setTitle(title, pid, isEdit);
        }

        if (StringUtils.isBlank(contentType)) {
            contentType = setContentType(file);
        }

        DocumentationDTO documentation = new DocumentationDTO(pid, contentType, title);
        InputStream is = null;

        try {
            if (file != null) {
                is = file.getInputStream();
                this.fileService.addFile(fileName, is);
            } else if (content != null) {
                is = new ByteArrayInputStream(content.getBytes("UTF-8"));
                this.fileService.addFile(fileName, is);
            } else if (!isEdit) {
                is = new ByteArrayInputStream("".getBytes());
                this.fileService.addFile(fileName, is);
            }

            this.documentationRepository.insertContent(documentation);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return pid;

    }

    private String setFileName(FileInfo file, String pid) throws Exception {
        if (file != null && file.getFileName() != null && StringUtils.isBlank(pid)) {
            return file.getFileName();
        } else if (!StringUtils.isBlank(pid)) {
            return pid;
        } else {
            throw new Exception("Both file and pid not specified!");
        }
    }

    private String setPid(FileInfo file) throws Exception {
        if (file != null && file.getFileName() != null) {
            return file.getFileName();
        } else {
            throw new Exception("Both file and pid not specified!");
        }
    }

    private String setTitle(String title, String pid, boolean isEdit) {
        if (!isEdit) {
            return pid;
        } else if (title == null) {
            return "";
        } else {
            return title;
        }
    }

    private String setContentType(FileInfo file) {
        if (file != null && StringUtils.isEmpty(file.getContentType())) {
            return "application/octet-stream";
        } else if (file != null) {
            return file.getContentType();
        } else {
            return "text/html";
        }
    }

}
