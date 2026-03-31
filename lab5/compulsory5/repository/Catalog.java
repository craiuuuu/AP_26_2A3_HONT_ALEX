package lab5.compulsory5.repository;

import lab5.compulsory5.exception.InvalidResourceException;
import lombok.Getter;
import lombok.Setter;
import lab5.compulsory5.model.Item;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Catalog
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

    public void openResource(Item item) throws InvalidResourceException
    {
        Desktop desktop = Desktop.getDesktop();
        String location = item.getLocation();
        if (!location.startsWith("http://") && !location.startsWith("https://"))
        {
            File file = new File(location);
            if (!file.exists())
            {
                throw new InvalidResourceException("fisierul nu exista la:" + location);
            }
        }

        try
        {
            if (location.startsWith("http://") || location.startsWith("https://"))
            {
                desktop.browse(new URI(location));
            }
            else
            {
                File file = new File(location);
                desktop.open(file);
            }
        } catch (Exception e)
        {
            throw new InvalidResourceException("Eroare la deschiderea resursei " + item.getTitle() , e);
        }
    }
}