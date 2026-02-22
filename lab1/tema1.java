package lab1;

public class tema1
{
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        String forma = args[1];

        long start = System.currentTimeMillis();
        int matrice[][] = new int[n][n];

        if (forma.equals("rectangle"))
        {
            deseneazaRectangle(matrice,n);
        }
        else if (forma.equals("circle"))
        {
            deseneazaCircle(matrice,n);
        }
        else
        {
            System.out.println("nu ai ales forma corecta");
        }

        long end = System.currentTimeMillis();


        if(n<=100)
        {
            deseneazafrumos(matrice,n);
        }
        else
        {
            System.out.println("Prea mare desenu.");
            System.out.println();
            System.out.println("Timpul in ms este: "+(end-start));
        }

    }

    public static void deseneazaRectangle(int matrice[][],int n)
    {
        for(int i=0; i< n; i++)
        {
            for(int j=0; j< n; j++)
            {
                matrice[i][j]=255;
            }
        }

        int margineSusJos=n/4;
        int margineStangaDreapta=n/8;
        for(int i=margineSusJos; i<n-margineSusJos; i++)
        {
            for(int j=margineStangaDreapta; j<n-margineStangaDreapta;j++)
            {
                matrice[i][j]=0;
            }
        }
    }

    public static void deseneazaCircle(int matrice[][],int n)
    {

        for(int i=0; i< n; i++)
        {
            for(int j=0; j< n; j++)
            {
                matrice[i][j]=0;
            }
        }

        int centruX=n/2;
        int centruY=n/2;
        int raza=n/4;

        for (int i=0;i<n;i++)
        {
            for (int j=0;j<n;j++)
            {
                int distantalapatrat = (i - centruX) * (i - centruX) + (j - centruY) * (j - centruY);
                if (distantalapatrat <= raza * raza)
                {
                    matrice[i][j] = 255;
                }
            }
        }
    }

    public static void deseneazafrumos(int matrice[][], int n)
    {
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<n;j++)
            {
                if (matrice[i][j]==255)
                {
                    System.out.print("1");
                }
                else
                {
                    System.out.print("0");
                }
            }
            System.out.println();
        }
    }
}//java lab1.tema1 40 rectangle/circle sau orice numar
//javac lab1\tema1.java
