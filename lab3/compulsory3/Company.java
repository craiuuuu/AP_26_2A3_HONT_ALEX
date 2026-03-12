package lab3.compulsory3;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Company implements Profile, Comparable<Company>
{

    private int id;
    private String name;


    public Company(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int compareTo(Company other)
    {
        return this.name.compareTo(other.getName());
    }
}
