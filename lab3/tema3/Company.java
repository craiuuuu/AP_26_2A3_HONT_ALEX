package lab3.tema3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Company implements Profile, Comparable<Company>
{

    private int id;
    private String name;
    private String industry;


    public Company(int id, String name,String industry)
    {
        this.id = id;
        this.name = name;
        this.industry = industry;
    }

    public int compareTo(Company other)
    {
        return this.name.compareTo(other.getName());
    }
}
