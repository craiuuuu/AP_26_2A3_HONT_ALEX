package lab3.advanced3;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args)
    {
        SocialNetwork network = new SocialNetwork();

        // Creăm obiectele conform noului model
        Person p1 = new Person(1, "George", LocalDate.of(1995, 5, 20), "Romanian");
        Programmer p2 = new Programmer(2, "Daniel", LocalDate.of(1998, 11, 15), "Romanian", "Java");
        Designer d1 = new Designer(3, "Alice", LocalDate.of(1999, 2, 10), "British", "Figma");
        Company c1 = new Company(4, "Tech Corp", "IT Software");

        // Setăm relațiile
        p1.addRelationship(p2, "Friend");
        p1.addRelationship(c1, "Former Employee");

        p2.addRelationship(c1, "Software Engineer");
        p2.addRelationship(d1, "Colleague");

        d1.addRelationship(c1, "UX Designer");

        // Adăugăm în rețea
        network.addProfile(p1);
        network.addProfile(p2);
        network.addProfile(d1);
        network.addProfile(c1);

        // Afișăm rețeaua sortată
        network.printNetwork();
    }
}