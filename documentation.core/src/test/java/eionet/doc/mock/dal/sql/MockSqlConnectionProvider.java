package eionet.doc.mock.dal.sql;

import eionet.doc.mock.config.MockConfiguration;
import eionet.doc.dal.sql.SqlConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

/**
 *
 * @author Nikolaos Nakas
 */
@Component
public final class MockSqlConnectionProvider implements SqlConnectionProvider {

    private final MockConfiguration configuration;
    
    public MockSqlConnectionProvider() {
        this.configuration = new MockConfiguration();
    }
    
    @Override
    public Connection createConnection() throws SQLException {
        String driverName = this.configuration.getPropertyValue("db.drv");
        
        try {
            Class.forName(driverName);
        }
        catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
        
        String url = this.configuration.getPropertyValue("db.url");
        String user = this.configuration.getPropertyValue("db.usr");
        String password = this.configuration.getPropertyValue("db.pwd");
        
        return DriverManager.getConnection(url, user, password);
    }
    
}
