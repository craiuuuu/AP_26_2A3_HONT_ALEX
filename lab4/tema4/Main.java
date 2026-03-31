package lab4.tema4;

import com.github.javafaker.Faker;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Main
{
    public static void main(String[] args)
    {
        Faker faker = new Faker();
        City myCity = new City("Fakeville");

        var nodes = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new Intersection(faker.address().cityName()))
                .toArray(Intersection[]::new);

        for (Intersection node : nodes)
        {
            myCity.addIntersection(node);
        }

        myCity.addStreet(new Street(faker.address().streetName(), faker.number().randomDouble(2, 1, 5), nodes[0], nodes[1]));
        myCity.addStreet(new Street(faker.address().streetName(), faker.number().randomDouble(2, 1, 5), nodes[1], nodes[2]));
        myCity.addStreet(new Street(faker.address().streetName(), faker.number().randomDouble(2, 1, 5), nodes[2], nodes[3]));
        myCity.addStreet(new Street(faker.address().streetName(), faker.number().randomDouble(2, 1, 5), nodes[1], nodes[3])); // Extra stradă pentru a crea un nod cu grad 3

        myCity.getCityMap().forEach((intersection, connectedStreets) -> {
            System.out.println("Intersectia " + intersection.getName() + "' este conectata cu");
            connectedStreets.forEach(street -> System.out.println("  - " + street.getName() + "cu o luungime de " + street.getLength() ));
        });

        System.out.println("stream API");

        double minLength = 2.0;

        myCity.getStreets().stream()
                .filter(street -> street.getLength() > minLength)
                .filter(street -> {
                    Set<Street> connectedStreets = new HashSet<>();
                    connectedStreets.addAll(myCity.getCityMap().get(street.getIntersection1()));
                    connectedStreets.addAll(myCity.getCityMap().get(street.getIntersection2()));

                    int joinedStreetsCount = connectedStreets.size();

                    return joinedStreetsCount >= 3;
                })
                .forEach(street -> System.out.println(street.getName() + "Lungime " + street.getLength() ));

        System.out.println("Alg de acoperire minim:");

        MST solver = new MST(myCity);
        solver.displaySolutions(3);
    }


}