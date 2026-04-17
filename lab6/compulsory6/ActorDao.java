package lab6.compulsory6;

import java.sql.*;

public class ActorDao {

    public void create(String name) throws SQLException {
        Connection con = movies_db.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "insert into genres (name) values (?)")) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    public Integer findByName(String name) throws SQLException {
        Connection con = movies_db.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "select id from genres where name='" + name + "'")) {
            return rs.next() ? rs.getInt(1) : null;
        }
    }

    public String findById(int id) throws SQLException
    {
        Connection con = movies_db.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "select name from genres where id=" + id))
        {
            return rs.next() ? rs.getString(1) : null;
        }
    }
}