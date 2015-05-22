package eionet.doc.dal.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Nikolaos Nakas
 */
public interface SqlConnectionProvider {
    
    Connection createConnection() throws SQLException;
}
