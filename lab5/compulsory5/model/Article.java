package lab5.compulsory5.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Article extends Item
{
    private String journal;

    public Article(String id, String title, String location, String journal)
    {
        super(id, title, location);
        this.journal = journal;
    }
}