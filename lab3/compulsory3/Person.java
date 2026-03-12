package lab3.compulsory3;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Person implements Profile, Comparable<Person>
{
    private int id;
    private String name;

    private Map<Profile, String> relationships = new HashMap<>();


    public Person(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void addRelationship(Profile p, String value)
    {
        relationships.put(p,value);
    }


    public int compareTo(Person other)
    {
        return this.name.compareTo(other.getName());
    }
}
