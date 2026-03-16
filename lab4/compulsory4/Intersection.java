package lab4.compulsory4;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Intersection implements Comparable<Intersection>
{
    private String name;

    public int compareTo(Intersection other)
    {
        return this.name.compareTo(other.name);
    }
}