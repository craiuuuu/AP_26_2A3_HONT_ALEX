package lab5.tema5;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Book extends Item
{
    private String author;
    private String year;

    public Book(String id, String title, String location, String author, String year)
    {
        super(id, title, location);
        this.author = author;
        this.year = year;
    }
}