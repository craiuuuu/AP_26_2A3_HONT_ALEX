package lab1;

public class advanced1
{
    public static void BoundBox(int matrice[][],int n)
    {
        int sus,jos,stanga,dreapta;

        sus=n;
        jos=0;
        stanga=n;
        dreapta=0;

        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                if(matrice[i][j]==0)//0 fiind negru
                {
                    if(i<sus)
                    {
                        sus=i;
                    }

                    if(i>jos)
                    {
                        jos=i;
                    }

                    if(j<stanga)
                    {
                        stanga=j;
                    }

                    if(j>dreapta)
                    {
                        dreapta=j;
                    }
                }
            }
        }

        System.out.println("Randul cel mai de sus este:"+sus);
        System.out.println("Randul cel mai de jos este"+ jos);
        System.out.println("Coloana cea mai din stanga este"+ stanga);
        System.out.println("Coloana cea mai din dreapta este"+ dreapta);

    }
}