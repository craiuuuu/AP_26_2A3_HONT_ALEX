package lab5.tema5;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Catalog implements Serializable
{
    private String name;
    private List<Item> items = new ArrayList<>();

    public Catalog(String name)
    {
        this.name = name;
    }

    public void add(Item item)
    {
        items.add(item);
    }



}