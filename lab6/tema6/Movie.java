package lab6.tema6;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private double score;
    private int genreId;

    public Movie(String title, Date releaseDate, int duration, double score, int genreId) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genreId = genreId;
    }
}