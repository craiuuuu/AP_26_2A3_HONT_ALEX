package lab1;

public class lab1
{
    void main()
    {
        String[] args;
        System.out.println("hello world!");
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};

        int n=(int) (Math.random() * 1_000_000);
        int rezultat=n*3;

        rezultat=rezultat+0b10101;//pt binar 0b
        rezultat=rezultat+0xFF; //si pt
        rezultat=rezultat*6;

        while(rezultat>9)
        {
            int suma=0;
            while (rezultat>0)
            {
                suma=suma+(rezultat % 10);
                rezultat=rezultat / 10;
            }
            rezultat=suma;
        }
        System.out.println("Willy-nilly,this semester I will learn " + languages[rezultat]);
    }
}