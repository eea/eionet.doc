package eionet.doc.mock.config;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Nikolaos Nakas
 */
public final class MockConfiguration {
    
    private final Properties properties;
    
    public MockConfiguration() {
        this.properties = new Properties();
        
        try {
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream("doc.properties"));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public String getPropertyValue(String propertyName) {
        return this.properties.getProperty(propertyName);
    }
}
