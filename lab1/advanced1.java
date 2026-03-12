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

        for (int j = stanga; j <= dreapta; j++)
        {
            matrice[sus][j] = 8; //latura de sus
            matrice[jos][j] = 8; //latura de jos
        }

        for (int i = sus; i <= jos; i++)
        {
            matrice[i][stanga] = 8;  //latura din stanga
            matrice[i][dreapta] = 8; //latura din dreapta
        }
        //unde se afla 8 este bounding box

        afiseazaMatrice(matrice, n);
    }

    public static void afiseazaMatrice(int[][] matrice, int n)
    {
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(matrice[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void main(String[] args)
    {
        int n = 5;
        //1alb 0negru
        int[][] testMatrice = {
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 0, 1, 1},
                {1, 1, 0, 1, 1},
                {1, 1, 1, 1, 1}
        };
        BoundBox(testMatrice, n);
    }
}//java lab1.advanced1
//javac lab1\advanced1.java