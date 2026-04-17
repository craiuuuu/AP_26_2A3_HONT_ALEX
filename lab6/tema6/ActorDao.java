package lab6.tema6;

import java.sql.*;

public class ActorDao
{
    public void create(String name) throws SQLException
    {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO actors (name) VALUES (?)"))
        {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            con.commit();
        }
    }

    public Integer findByName(String name) throws SQLException
    {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement("SELECT id FROM actors WHERE name = ?"))
        {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }
}