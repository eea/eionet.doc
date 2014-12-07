/**
 *
 */
package eionet.doc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import eionet.doc.config.GeneralConfig;
import eionet.doc.dto.DocumentationDTO;

/**
 * Data Access Object for documentation page.
 *
 * @author Risto Alt
 */
public class DocumentationDAO {

    /**
     *
     * @return FileService
     */
    public static DocumentationDAO getInstance() throws Exception {
        return new DocumentationDAO();
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

    /**
     * @see eionet.doc.dao.DocumentationDAO#getDocObject(java.lang.String)
     */
    public DocumentationDTO getDocObject(String pageId) throws Exception {

        String docObjectSQL = "SELECT content_type, title FROM documentation WHERE page_id = ?";

        DocumentationDTO ret = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            stmt = conn.prepareStatement(docObjectSQL);
            stmt.setString(1, pageId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret = new DocumentationDTO();
                ret.setPageId(pageId);
                ret.setContentType(rs.getString("content_type"));
                ret.setTitle(rs.getString("title"));
            }
        } catch (Exception e) {
            throw new Exception(e.toString(), e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return ret;
    }

    /**
     * @see eionet.cr.dao.DocumentationDAO#getDocObjects()
     */
    public List<DocumentationDTO> getDocObjects(boolean htmlOnly) throws Exception {

        String docObjectSQL = "SELECT page_id, content_type, title FROM documentation";
        if (htmlOnly) {
            docObjectSQL = docObjectSQL + " WHERE content_type='text/html' OR content_type='uploaded_text/html'";
        }

        List<DocumentationDTO> ret = new ArrayList<DocumentationDTO>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(docObjectSQL);
            while (rs.next()) {
                DocumentationDTO doc = new DocumentationDTO();
                doc.setPageId(rs.getString("page_id"));
                doc.setContentType(rs.getString("content_type"));
                doc.setTitle(rs.getString("title"));
                ret.add(doc);
            }
        } catch (SQLException e) {
            throw new Exception(e.toString(), e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return ret;
    }

    /**
     * Insert a record into the documentation table.
     *
     * @param isVirtuoso - not used
     */
    @Deprecated
    public void insertContent(String pageId, String contentType, String title, boolean isVirtuoso) throws Exception {
        insertContent(pageId, contentType, title);
    }

    /**
     * Insert a record into the documentation table.
     * Deletes any record that might already exist.
     */
    public void insertContent(String pageId, String contentType, String title) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("DELETE FROM documentation WHERE page_id = ?");
            ps.setString(1, pageId);
            ps.executeUpdate();

            ps = conn.prepareStatement("INSERT INTO documentation VALUES (?, ?, ?)");
            ps.setString(1, pageId);
            ps.setString(2, contentType);
            ps.setString(3, title);
            ps.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            throw new Exception(e.toString(), e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * @see eionet.doc.dao.DocumentationDAO#idExists(java.lang.String)
     */
    public boolean idExists(String pageId) throws Exception {

        String docObjectSQL = "SELECT page_id FROM documentation WHERE page_id = ?";

        boolean ret = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            stmt = conn.prepareStatement(docObjectSQL);
            stmt.setString(1, pageId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ret = true;
            }
        } catch (SQLException e) {
            throw new Exception(e.toString(), e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return ret;
    }

    /**
     * @see eionet.doc.dao.DocumentationDAO#deleteContent(java.lang.String)
     */
    public void deleteContent(String pageId) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM documentation WHERE page_id = ?");
            ps.setString(1, pageId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.toString(), e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
