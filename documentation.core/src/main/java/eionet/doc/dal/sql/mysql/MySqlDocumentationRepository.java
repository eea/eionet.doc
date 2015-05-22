package eionet.doc.dal.sql.mysql;

import eionet.doc.dal.DataAccessException;
import eionet.doc.dal.DocumentationRepository;
import eionet.doc.dal.sql.SqlConnectionProvider;
import eionet.doc.dto.DocumentationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Nikolaos Nakas
 */
@Repository
public final class MySqlDocumentationRepository implements DocumentationRepository {

    private static final String COLUMN_PAGE_ID = "page_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT_TYPE = "content_type";
    
    private final SqlConnectionProvider connectionProvider;

    @Autowired
    public MySqlDocumentationRepository(SqlConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    
    @Override
    public DocumentationDTO getDocObject(String pageId) throws DataAccessException {
        DocumentationDTO result = null;
        Connection connection = null;
        
        try {
            try {
                connection = this.connectionProvider.createConnection();
                String sql = "SELECT content_type, title FROM documentation WHERE page_id = ?";
                PreparedStatement stmt = null;

                try {
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, pageId);
                    ResultSet rs = null;

                    try {
                        rs = stmt.executeQuery();

                        if (rs.next()) {
                            result = new DocumentationDTO();
                            result.setPageId(pageId);
                            result.setContentType(rs.getString(COLUMN_CONTENT_TYPE));
                            result.setTitle(rs.getString(COLUMN_TITLE));
                        }
                    }
                    finally {
                        this.safeClose(rs);
                    }
                }
                finally {
                    this.safeClose(stmt);
                }
            }
            finally {
                this.safeClose(connection);
            }
        }
        catch(SQLException ex) {
            throw new DataAccessException(ex);
        }
        
        return result;
    }

    @Override
    public List<DocumentationDTO> getDocObjects(boolean htmlOnly) throws DataAccessException {
        List<DocumentationDTO> result = new ArrayList<DocumentationDTO>();
        Connection connection = null;
        
        try {
            try {
                connection = this.connectionProvider.createConnection();
                String sql = "SELECT page_id, content_type, title FROM documentation";

                if (htmlOnly) {
                    sql += " WHERE content_type = 'text/html' OR content_type = 'uploaded_text/html'";
                }

                Statement stmt = null;

                try {
                    stmt = connection.createStatement();
                    ResultSet rs = null;

                    try {
                        rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            DocumentationDTO doc = new DocumentationDTO();
                            doc.setPageId(rs.getString(COLUMN_PAGE_ID));
                            doc.setContentType(rs.getString(COLUMN_CONTENT_TYPE));
                            doc.setTitle(rs.getString(COLUMN_TITLE));
                            result.add(doc);
                        }
                    }
                    finally {
                        this.safeClose(rs);
                    }
                }
                finally {
                    this.safeClose(stmt);
                }
            }
            finally {
                this.safeClose(connection);
            }
        }
        catch(SQLException ex) {
            throw new DataAccessException(ex);
        }
        
        return result;
    }

    @Override
    public void insertContent(DocumentationDTO documentation) throws DataAccessException {
        Connection connection = null;
        
        try {
            try {
                connection = this.connectionProvider.createConnection();
                connection.setAutoCommit(false);

                try {
                    this.delete(connection, documentation.getPageId());
                    this.insert(connection, documentation);
                    connection.commit();
                }
                catch(SQLException ex) {
                    connection.rollback();
                    throw ex;
                }
                finally {
                    connection.setAutoCommit(true);
                }
            }
            finally {
                this.safeClose(connection);
            }
        }
        catch(SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public boolean idExists(String pageId) throws DataAccessException {
        Connection connection = null;
        
        try {
            try {
                connection = this.connectionProvider.createConnection();
                PreparedStatement stmt = null;

                try {
                    stmt = connection.prepareStatement("SELECT page_id FROM documentation WHERE page_id = ?");
                    stmt.setString(1, pageId);
                    ResultSet rs = null;

                    try {
                        rs = stmt.executeQuery();

                        return rs.next();
                    }
                    finally {
                        this.safeClose(rs);
                    }
                }
                finally {
                    this.safeClose(stmt);
                }
            }
            finally {
                this.safeClose(connection);
            }
        }
        catch(SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void deleteContent(String pageId) throws DataAccessException {
        Connection connection = null;
        
        try {
            try {
                connection = this.connectionProvider.createConnection();
                this.delete(connection, pageId);
            }
            finally {
                this.safeClose(connection);
            }
        }
        catch(SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
    
    private void delete(Connection connection, String pageId) throws SQLException {
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement("DELETE FROM documentation WHERE page_id = ?");
            ps.setString(1, pageId);
            ps.executeUpdate();
        }
        finally {
            this.safeClose(ps);
        }
    }
    
    private void insert(Connection connection, DocumentationDTO documentation) throws SQLException {
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement("INSERT INTO documentation (page_id, content_type, title) VALUES (?, ?, ?)");
            ps.setString(1, documentation.getPageId());
            ps.setString(2, documentation.getContentType());
            ps.setString(3, documentation.getTitle());
            ps.executeUpdate();
        }
        finally {
            this.safeClose(ps);
        }
    }
    
    private void safeClose(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    
    private void safeClose(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }
    
    private void safeClose(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
