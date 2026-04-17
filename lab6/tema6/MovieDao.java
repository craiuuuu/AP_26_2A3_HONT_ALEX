package lab6.tema6;

import lab6.tema6.Database;
import java.sql.*;

public class MovieDao {
    public void create(String title, Date releaseDate, int duration, double score, int genreId) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, title);
            pstmt.setDate(2, releaseDate);
            pstmt.setInt(3, duration);
            pstmt.setDouble(4, score);
            pstmt.setInt(5, genreId);
            pstmt.executeUpdate();
            con.commit();
        }
    }
}