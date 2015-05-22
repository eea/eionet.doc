package eionet.doc.db;

import eionet.doc.dal.sql.SqlConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Nikolaos Nakas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:mock-spring-config.xml" })
public abstract class DatabaseDependentTest {
    
    private static final String SQL_CREATE_TABLE =
            "create table documentation (" +
                "page_id varchar(255) not null," +
                "content_type varchar(100) not null default 'text/html'," +
                "title varchar(512) default ''," +
                "primary key (page_id)" +
            ")";
    
    private static final String SQL_DROP_TABLE =
            "drop table if exists documentation";
    
    
    @Autowired
    private SqlConnectionProvider connectionProvider;
    
    private Connection schemaConnection;
    
    @Before
    public void setUp() throws Exception {
        this.schemaConnection = connectionProvider.createConnection();
        this.dropSchema();
        this.createSchema();
    }
    
    @After
    public void tearDown() throws Exception {
        try {
            this.dropSchema();
        }
        finally {
            if (this.schemaConnection != null) {
                this.schemaConnection.close();
                this.schemaConnection = null;
            }
        }
    }
    
    private void createSchema() throws Exception {
        this.executeSqlUpdateStatement(SQL_CREATE_TABLE);
    }
    
    private void dropSchema() throws Exception {
        this.executeSqlUpdateStatement(SQL_DROP_TABLE);
    }
    
    private void executeSqlUpdateStatement(String sql) throws Exception {
        PreparedStatement stmt = null;
        
        try {
            stmt = this.schemaConnection.prepareStatement(sql);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
