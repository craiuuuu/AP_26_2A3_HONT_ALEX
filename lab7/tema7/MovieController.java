package lab7.tema7;
//nu mi-a cam iesit nimic , am uploadat ca azi este deadline-ul
import com.exemplu.movies.exception.MovieNotFoundException;
import com.exemplu.movies.model.Movie;
import com.exemplu.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MovieController
{

    private MovieRepository movieRepository;

    @GetMapping
    public List<Movie> getAllMovies()
    {
        return movieRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie)
    {

        Movie savedMovie = movieRepository.save(movie); 
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movieDetails) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        movie.setTitle(movieDetails.getTitle());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setDuration(movieDetails.getDuration());
        movie.setScore(movieDetails.getScore());
        movie.setGenreId(movieDetails.getGenreId());

        Movie updatedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(updatedMovie);
    }
    @PatchMapping
    public ResponseEntity<Movie> updateScore(@PathVariable Integer id, @RequestParam Double newScore) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        movie.setScore(newScore);
        Movie updatedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(updatedMovie);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        movieRepository.delete(movie);
        return ResponseEntity.ok("Filmul cu ID " + id + " a fost sters");
    }
}