package lab3.compulsory3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Main
{
    public static void main(String[] args)
    {
        List<Profile> network = new ArrayList<>();

        Person p1 = new Person(1, "GEorge");
        Person p2 = new Person(2, "Daniel");
        Company c1 = new Company(3, "Tech Corp");

        p2.addRelationship(c1, "Software Engineer");

        network.add(p1);
        network.add(c1);
        network.add(p2);

        network.sort(Comparator.comparing(Profile::getName));

        System.out.println("Sortat:");
        for (int i = 0; i < network.size(); i++)
        {
            System.out.println(network.get(i));
        }
    }
}
