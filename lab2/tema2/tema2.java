package lab2.tema2;

enum RoadType //enum
{
    HIGHWAY, EXPRESS, COUNTRY
}

abstract class Location
{
    protected String name;
    protected double x;
    protected double y;

    public Location(String name, double x, double y)
    {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }


    public String toString()
    {
        return " name='" + name + ", x=" + x + ", y=" + y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true; //daca sunt fix acelasi obiect din memorie
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;//obiectu cu care l compar e nul sau e altcv
        }

        Location other = (Location) obj;
        return this.name.equals(other.name) && this.x == other.x && this.y == other.y;
    }
}

class City extends Location
{
    private int population;

    public City(String name, double x, double y, int population)
    {
        super(name, x, y);//pt apelare costructor
        this.population = population;
    }

    public int getPopulation()
    {
        return population;
    }
    public void setPopulation(int population)
    {
        this.population = population;
    }

    public String toString()
    {
        return "City" + super.toString() + ", population=" + population ;
    }
}

class GasStation extends Location
{
    private double gasPrice;

    public GasStation(String name, double x, double y, double gasPrice)
    {
        super(name, x, y);
        this.gasPrice = gasPrice;
    }

    public double getGasPrice()
    {
        return gasPrice;
    }
    public void setGasPrice(double gasPrice)
    {
        this.gasPrice = gasPrice;
    }

    public String toString()
    {
        return "GasStation" + super.toString() + ", gasPrice=" + gasPrice;
    }
}

class Airport extends Location
{
    private int numberOfTerminals;

    public Airport(String name, double x, double y, int numberOfTerminals)
    {
        super(name, x, y);
        this.numberOfTerminals = numberOfTerminals;
    }

    public int getNumberOfTerminals()
    {
        return numberOfTerminals;
    }
    public void setNumberOfTerminals(int numberOfTerminals)
    {
        this.numberOfTerminals = numberOfTerminals;
    }

    public String toString()
    {
        return "Airport" + super.toString() + ", terminals=" + numberOfTerminals;
    }
}

class Road
{
    private RoadType type;
    private double length;
    private int speedLimit;
    private Location loc1;
    private Location loc2;

    public Road(RoadType type, double length, int speedLimit, Location loc1, Location loc2)
    {
        this.type = type;
        this.speedLimit = speedLimit;
        this.loc1 = loc1;
        this.loc2 = loc2;

        setLength(length);
    }

    double CalculeazaDistantaEuclediana()
    {
        return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2));
    }

    public RoadType getType()
    {
        return type;
    }

    public void setType(RoadType type)
    {
        this.type = type;
    }

    public double getLength()
    {
        return length;
    }

    public int getSpeedLimit()
    {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit)
    {
        this.speedLimit = speedLimit;
    }

    public Location getLoc1()
    {
        return loc1;
    }

    public void setLoc1(Location loc1)
    {
        this.loc1 = loc1;
    }

    public Location getLoc2()
    {
        return loc2;
    }

    public void setLoc2(Location loc2)
    {
        this.loc2 = loc2;
    }


    public void setLength(double length)
    {
        double DistantaMinima = CalculeazaDistantaEuclediana();
        if (length < DistantaMinima)
        {
            System.out.println("Lungimea nu poate fi mai mica decat distanta euclideana" + DistantaMinima);
            this.length = DistantaMinima;
        }
        else
        {
            this.length = length;
        }
    }

    public String toString()
    {
        return "type=" + type +
                ", length=" + length +
                ", speedLimit=" + speedLimit +
                "km/h, connects " + loc1.getName() + " to " + loc2.getName();
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        Road other = (Road) obj;

        boolean sameLocations =
                // drum 1 e fix in aceeasi directie cu drum 2
                (this.loc1.equals(other.loc1) && this.loc2.equals(other.loc2))
                        ||
                        // Drumurile sunt in sens invers la fel
                        (this.loc1.equals(other.loc2) && this.loc2.equals(other.loc1));

        return this.type == other.type && sameLocations;
    }
}

class Problem
{
    private Location[] locations;
    private Road[] roads;

    //cosntructor fara paramterii
    public Problem()
    {
    }

    //constructor cu parametrii
    public Problem(Location[] locations, Road[] roads)
    {
        this.locations = locations;
        this.roads = roads;
    }

    //getter/setters
    public Location[] getLocations()
    {
        return locations;
    }

    public void setLocations(Location[] locations)
    {
        this.locations = locations;
    }

    public Road[] getRoads()
    {
        return roads;
    }

    public void setRoads(Road[] roads)
    {
        this.roads = roads;
    }

    public String toString()
    {
        String result = "Locations:\n";
        if (locations != null)
        {
            for (int i = 0; i < locations.length; i++)
            {
                if (locations[i] != null)
                {
                    result = result + "- " + locations[i].toString() + "\n";
                }
            }
        }

        result += "\nRoads:\n";
        if (roads != null)
        {
            for (int i = 0; i < roads.length; i++)
            {
                if (roads[i] != null)
                {
                    result = result + "- " + roads[i].toString() + "\n";
                }
            }
        }
        return result;
    }

    public boolean isValid()
    {

        if (locations != null)
        {
            for (int i = 0; i < locations.length; i++)
            {
                for (int j = i + 1; j < locations.length; j++)
                {
                    if (locations[i] != null && locations[i].equals(locations[j]))
                    {
                        System.out.println("avem locatie duplicata!");
                        return false;
                    }
                }
            }
        }

        if (roads != null)
        {
            for (int i = 0; i < roads.length; i++)
            {
                for (int j = i + 1; j < roads.length; j++)
                {
                    if (roads[i] != null && roads[i].equals(roads[j]))
                    {
                        System.out.println("avem drumuri duplicate!");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    //alg graf

    public boolean isReachable(Location start, Location destination)
    {
        Location[] visited = new Location[locations.length];
        return dfsManual(start, destination, visited, 0);
    }

    private boolean dfsManual(Location current, Location destination, Location[] visited, int count)
    {
        if (current.equals(destination))
        {
            return true;
        }

        visited[count++] = current;

        for (int j = 0; j < roads.length; j++)
        {
            Road road = roads[j];
            if (road == null)
            {
                continue;
            }
            Location neighbor = null;

            if (road.getLoc1().equals(current))
            {
                neighbor = road.getLoc2();
            }
            else if (road.getLoc2().equals(current))
            {
                neighbor = road.getLoc1();
            }

            if (neighbor != null)
            {

                boolean alreadyVisited = false;
                for (int i = 0; i < count; i++)
                {
                    if (visited[i].equals(neighbor))
                    {
                        alreadyVisited = true;
                        break;
                    }
                }

                if (!alreadyVisited)
                {
                    if (dfsManual(neighbor, destination, visited, count))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}


public class tema2
{
    public void main(String[] args)
    {

        //locatii
        City iasi = new City("Iasi", 10.0, 20.0, 318000);
        GasStation omv = new GasStation("OMV_Pacurari", 15.0, 25.0, 7.15);
        Airport otopeni = new Airport("Otopeni", 48.0, 98.0, 2);
        City bucuresti = new City("Bucuresti", 50.0, 100.0, 1830000);

        //alt oras pt ca sa testam un drum inexistent
        City vaslui = new City("Vaslui", 20.0, 30.0, 50000);

        //pun locatiile in array
        Location[] locations = { iasi, omv, otopeni, bucuresti };

        //drumuri
        Road r1 = new Road(RoadType.HIGHWAY, 350.0, 130, iasi, bucuresti);
        Road r2 = new Road(RoadType.COUNTRY, 10.0, 60, iasi, omv);
        Road r3 = new Road(RoadType.EXPRESS, 15.0, 100, otopeni, bucuresti);

        //drumuri in array
        Road[] roads = { r1, r2, r3 };

        //instanta problemei
        Problem pb = new Problem(locations, roads);

        //afisam datele problemei
        System.out.println("Detalii:\n");
        System.out.println(pb);

        // verificam duplicate
        System.out.println("Este instanta valida?");
        if (pb.isValid() == true)
        {
            System.out.println("Instanta este valida, nu avem duplicate\n");
        }
        else
        {
            System.out.println("Eroare:Instanta nu este validam,avem date duplicate\n");
        }

        System.out.println("Verificare rute valide:");

        //test1
        System.out.print("Traseu: Iasi -> Otopeni ... ");
        boolean canReachOtopeni = pb.isReachable(iasi, otopeni);

        if (canReachOtopeni == true)
        {
            System.out.println("SUCCES (Traseu gasit)");
        }
        else
        {
            System.out.println("ESEC (Inaccesibil)");
        }

        //test2
        System.out.print("Traseu: Iasi -> Vaslui ... ");
        boolean canReachVaslui = pb.isReachable(iasi, vaslui);

        if (canReachVaslui == true)
        {
            System.out.println("Traseu gasit");
        }
        else
        {
            System.out.println("Traseu negasit");
        }
    }
}
//java lab2.tema2
//javac lab2\tema2.java