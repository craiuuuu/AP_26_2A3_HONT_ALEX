package lab4.tema4;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@ToString
public class City
{
    private String name;
    private List<Intersection> intersections = new ArrayList<>();
    private List<Street> streets = new ArrayList<>();

    private Map<Intersection, List<Street>> cityMap = new HashMap<>();

    public City(String name)
    {
        this.name = name;
    }

    public void addIntersection(Intersection intersection)
    {
        intersections.add(intersection);
        cityMap.putIfAbsent(intersection, new ArrayList<>());
    }


    public void addStreet(Street street)
    {
        streets.add(street);

        if (cityMap.containsKey(street.getIntersection1()))
        {
            cityMap.get(street.getIntersection1()).add(street);
        }

        if (cityMap.containsKey(street.getIntersection2()))
        {
            cityMap.get(street.getIntersection2()).add(street);
        }
    }
}