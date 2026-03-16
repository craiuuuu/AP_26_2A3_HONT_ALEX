package lab4.compulsory4;

import java.util.*;
import java.util.stream.IntStream;

public class Main
{
    public static void main(String[] args)
    {
        var nodes = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new Intersection("v" + i))
                .toArray(Intersection[]::new);


        List<Street> streetList = new LinkedList<>();
        streetList.add(new Street("e1", 3.5, nodes[0], nodes[1]));
        streetList.add(new Street("e2", 1.2, nodes[1], nodes[2]));
        streetList.add(new Street("e3", 2.8, nodes[2], nodes[3]));

        Collections.sort(streetList, Comparator.comparing(Street::getLength));
        System.out.println(streetList);


        Set<Intersection> nodeSet = new HashSet<>(Arrays.asList(nodes));
        System.out.println("Dimensiune hash  initial " + nodeSet.size());

        Intersection duplicateNode = new Intersection("v0");
        boolean isAdded = nodeSet.add(duplicateNode);

        System.out.println("Putem adauga v0'? " + isAdded);
        System.out.println("Dimensiune hashset dupa incercare " + nodeSet.size());
    }

}