package eionet.doc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import eionet.doc.config.GeneralConfig;

import org.junit.After;
import org.junit.Before;

/**
 * Database operations. The table is recreated between tests.
 */
public class DBFunctions {

    protected Connection dbConn;

    /** Object to hold the properties of a documentation object. */

    private void createSchema() throws Exception {
        Statement statement = dbConn.createStatement();

        String createSQL = "CREATE TABLE documentation ("
            + "page_id varchar(255) NOT NULL,"
            + "content_type varchar(100) NOT NULL default 'text/html',"
            + "title varchar(512) default '',"
            + "PRIMARY KEY (page_id))";
        statement.executeUpdate(createSQL);
        statement.close();
    }

    private void dropSchema() throws Exception {
        Statement statement = dbConn.createStatement();

        String dropSQL = "DROP TABLE IF EXISTS documentation";
        statement.executeUpdate(dropSQL);
        statement.close();
    }

    private Connection getSQLConnection() throws Exception {

        String drv = GeneralConfig.getRequiredProperty(GeneralConfig.DB_DRV);
        String url = GeneralConfig.getRequiredProperty(GeneralConfig.DB_URL);
        String usr = GeneralConfig.getRequiredProperty(GeneralConfig.DB_USR);
        String pwd = GeneralConfig.getRequiredProperty(GeneralConfig.DB_PWD);

        try {
            Class.forName(drv);
            return DriverManager.getConnection(url, usr, pwd);
        } catch (ClassNotFoundException e) {
            throw new Exception("Failed to get connection, driver class not found: " + drv, e);
        }
    }

    @Before
    public void setUp() throws Exception {
        dbConn = getSQLConnection();
        dropSchema();
        createSchema();
    }

    @After
    public void shutDown() throws Exception {
        dropSchema();
        dbConn.close();
        dbConn = null;
    }
}
