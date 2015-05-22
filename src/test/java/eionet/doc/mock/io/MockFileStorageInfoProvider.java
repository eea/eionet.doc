package eionet.doc.mock.io;

import eionet.doc.mock.config.MockConfiguration;
import eionet.doc.io.FileStorageInfoProvider;

import org.springframework.stereotype.Component;

/**
 *
 * @author Nikolaos Nakas
 */
@Component
public final class MockFileStorageInfoProvider implements FileStorageInfoProvider {

    private final MockConfiguration configuration;
    
    public MockFileStorageInfoProvider() {
        this.configuration = new MockConfiguration();
    }
    
    @Override
    public String getFileStoragePath() {
        return configuration.getPropertyValue("doc.files.folder");
    }
    
}
