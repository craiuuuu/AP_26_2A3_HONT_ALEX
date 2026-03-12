package lab3.advanced3;

import java.util.ArrayList;
import java.util.List;

public class SocialNetwork
{

    private List<Profile> profiles = new ArrayList<>();

    public void addProfile(Profile profile)
    {
        profiles.add(profile);
    }

    public int computeImportance(Profile target)
    {
        int numarRelatii = 0;

        if (target instanceof Person)
        {
            Person persoana = (Person) target;
            numarRelatii =numarRelatii+persoana.getRelationships().size();
        }

        for (int i = 0; i < profiles.size(); i++)
        {
            Profile p = profiles.get(i);

            if (p instanceof Person && p != target)
            {
                Person altaPersoana = (Person) p;


                if (altaPersoana.getRelationships().containsKey(target))
                {
                    numarRelatii++;
                }
            }
        }

        return numarRelatii;
    }

    public void printNetwork()
    {
        profiles.sort((p1, p2) -> Integer.compare(computeImportance(p2), computeImportance(p1)));

        System.out.println("Retaua ordonata dupa importanta:");
        for (int i = 0; i < profiles.size(); i++)
        {
            Profile p = profiles.get(i);
            System.out.println(p.getName() + " are " + computeImportance(p) + " relatii");
        }
    }
}