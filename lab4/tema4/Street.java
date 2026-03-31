package lab4.tema4;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Street implements Comparable<Street>
{
    private String name;
    private double length;
    private Intersection intersection1;
    private Intersection intersection2;


    public int compareTo(Street other)
    {
        return Double.compare(this.length, other.length);
    }
}