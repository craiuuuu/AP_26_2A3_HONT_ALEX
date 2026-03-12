package lab3.tema3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Person implements Profile, Comparable<Person>
{
    private int id;
    private String name;
    private LocalDate birthDate;
    private String nationality;//creativiate

    private Map<Profile, String> relationships = new HashMap<>();


    public Person(int id, String name,LocalDate birthDate, String nationality)
    {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
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
