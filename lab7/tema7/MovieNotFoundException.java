package lab7.tema7;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Integer id) {
        super("Nu s-a gasit niciun film cu ID-ul: " + id);
    }
}