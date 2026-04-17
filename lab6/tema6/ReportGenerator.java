package lab6.tema6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportGenerator
{
    public void generateHtmlReport()
    {
        StringBuilder tableRows = new StringBuilder();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movie_report_view")) {

            while (rs.next())
            {
                tableRows.append("<tr>")
                         .append("<td>").append(rs.getString("title")).append("</td>")
                         .append("<td>").append(rs.getDate("release_date")).append("</td>")
                         .append("<td>").append(rs.getDouble("score")).append("</td>")
                         .append("<td>").append(rs.getString("genre_name")).append("</td>")
                         .append("</tr>\n");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date" + e.getMessage());
            return;
        }

        try {
            Path templatePath = Paths.get("template.html");
            String htmlTemplate = Files.readString(templatePath);
            String finalHtml = htmlTemplate.replace("{{TABLE_ROWS}}", tableRows.toString());
            
            Path outputPath = Paths.get("report.html");
            Files.writeString(outputPath, finalHtml);
            System.out.println("Raport creat" + outputPath.toAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Eroare la fisiere" + e.getMessage());
        }
    }
}