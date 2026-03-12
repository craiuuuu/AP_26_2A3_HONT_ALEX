package lab2.advanced2;

import java.util.*;

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

}

class Solution
{
    private Location[] route;
    private double totalDistance;

    public Solution(Location[] route, double totalDistance)
    {
        this.route = route;
        this.totalDistance = totalDistance;
    }

    public Location[] getRoute()
    {
        return route;
    }

    public double computeLength()
    {
        return totalDistance;
    }

    public String toString()
    {
        if (route == null || route.length == 0)
        {
            return "Niciun drum gasit.";
        }
        String path = route[0].getName();
        for (int i = 1; i < route.length; i++) {
            path += " -> " + route[i].getName();
        }
        return "Ruta optima: " + path + " | Distanta totala: " + totalDistance;
    }
}

abstract class Algorithm
{
    protected Problem problem;

    public Algorithm(Problem problem)
    {
        this.problem = problem;
    }

    public abstract Solution solve(Location start, Location destination);
}

class DijkstraAlgorithm extends Algorithm
{

    public DijkstraAlgorithm(Problem problem)
    {
        super(problem);
    }


    public Solution solve(Location start, Location destination)
    {

        Map<Location, Double> distances = new HashMap<>();
        Map<Location, Location> previousNodes = new HashMap<>();
        List<Location> unvisited = new ArrayList<>();


        for (Location loc : problem.getLocations())
        {
            if (loc != null)
            {
                distances.put(loc, Double.MAX_VALUE);
                unvisited.add(loc);
            }
        }
        distances.put(start, 0.0);


        while (!unvisited.isEmpty())
        {

            Location current = null;
            double minDistance = Double.MAX_VALUE;

            for (Location loc : unvisited)
            {
                if (distances.get(loc) < minDistance)
                {
                    minDistance = distances.get(loc);
                    current = loc;
                }
            }


            if (current == null || current.equals(destination))
            {
                break;
            }

            unvisited.remove(current);


            for (Road road : problem.getRoads())
            {
                if (road == null) continue;

                Location neighbor = null;

                if (road.getLoc1().equals(current))
                {
                    neighbor = road.getLoc2();
                }
                else if (road.getLoc2().equals(current))
                {
                    neighbor = road.getLoc1();
                }


                if (neighbor != null && unvisited.contains(neighbor))
                {
                    double newDist = distances.get(current) + road.getLength();


                    if (newDist < distances.get(neighbor))
                    {
                        distances.put(neighbor, newDist);
                        previousNodes.put(neighbor, current);
                    }
                }
            }
        }


        if (distances.get(destination) == Double.MAX_VALUE)
        {
            return new Solution(null, 0);
        }

        List<Location> path = new ArrayList<>();
        Location curr = destination;
        while (curr != null)
        {
            path.add(0, curr);
            curr = previousNodes.get(curr);
        }


        return new Solution(path.toArray(new Location[0]), distances.get(destination));
    }
}


public class advanced2
{

    public void main(String[] args)
    {


        City iasi = new City("Iasi", 10.0, 20.0, 318000);
        GasStation omv = new GasStation("OMV_Pacurari", 15.0, 25.0, 7.15);
        Airport otopeni = new Airport("Otopeni", 48.0, 98.0, 2);
        City bucuresti = new City("Bucuresti", 50.0, 100.0, 1830000);


        City vaslui = new City("Vaslui", 20.0, 30.0, 50000);


        Location[] locations = { iasi, omv, otopeni, bucuresti, vaslui };


        Road r1 = new Road(RoadType.HIGHWAY, 350.0, 130, iasi, bucuresti);
        Road r2 = new Road(RoadType.COUNTRY, 10.0, 60, iasi, omv);
        Road r3 = new Road(RoadType.EXPRESS, 15.0, 100, otopeni, bucuresti);


        Road[] roads = { r1, r2, r3 };


        Problem pb = new Problem(locations, roads);

        System.out.println("Detalii problema");
        System.out.println(pb);


        System.out.println("Validare");
        if (pb.isValid())
        {
            System.out.println("Nu avem duplicate\n");
        } else {
            System.out.println("Avem duplicate\n");
        }


        System.out.println("Dijkstra:");
        Algorithm alg = new DijkstraAlgorithm(pb);


        System.out.println("Test 1: Iasi -> Otopeni");
        Solution sol1 = alg.solve(iasi, otopeni);
        System.out.println(sol1);
        System.out.println();


        System.out.println("Test 2: Iasi -> Vaslui");
        Solution sol2 = alg.solve(iasi, vaslui);
        System.out.println(sol2);
    }
}
