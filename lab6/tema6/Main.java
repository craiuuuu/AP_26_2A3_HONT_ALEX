package lab6.tema6;

import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)
    {
        try {
            GenreDao genreDao = new GenreDao();
            ActorDao actorDao = new ActorDao();
            MovieDao movieDao = new MovieDao();

            genreDao.create("Action");
            actorDao.create("Chuck Norris");

            Integer actionId = genreDao.findByName("Action");
            
            if (actionId != null)
            {
                movieDao.create("Walker, Texas Ranger The Movie", Date.valueOf("1995-05-15"), 120, 8.5, actionId);
            }


            ReportGenerator reportGenerator = new ReportGenerator();
            reportGenerator.generateHtmlReport();

            System.out.println("verifica fisieru report html");

        } catch (SQLException e) {
            System.err.println("A crapat cv la bd:" + e.getMessage());
        }
    }
}