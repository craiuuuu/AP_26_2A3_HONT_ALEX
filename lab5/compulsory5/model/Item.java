package lab5.compulsory5.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class Item implements Serializable
{
    private String id;
    private String title;
    private String location;

    public Item(String id, String title, String location)
    {
        this.id = id;
        this.title = title;
        this.location = location;
    }
}