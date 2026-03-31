package lab4.tema4;

import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MST
{

    private City city;
    private SimpleWeightedGraph<Intersection, DefaultWeightedEdge> graph;

    public MST(City city)
    {
        this.city = city;
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        buildGraph();
    }

    private void buildGraph()
    {
        for (Intersection node : city.getIntersections())
        {
            graph.addVertex(node);
        }

        for (Street street : city.getStreets())
        {
            DefaultWeightedEdge edge = graph.addEdge(street.getIntersection1(), street.getIntersection2());
            if (edge != null)
            {
                graph.setEdgeWeight(edge, street.getLength());
            }
        }
    }

    public void displaySolutions(int maxAlternatives)
    {
        KruskalMinimumSpanningTree<Intersection, DefaultWeightedEdge> kruskal =
                new KruskalMinimumSpanningTree<>(graph);

        var optimalMst = kruskal.getSpanningTree();

        if (optimalMst == null)
        {
            System.out.println("graful nu este conext nu se poate face arborele de acoperire");
            return;
        }

        System.out.println("Cost minim " + optimalMst.getWeight());

        List<Double> alternativeCosts = new ArrayList<>();

        for (DefaultWeightedEdge edgeToRemove : optimalMst.getEdges())
        {
            double weight = graph.getEdgeWeight(edgeToRemove);
            Intersection source = graph.getEdgeSource(edgeToRemove);
            Intersection target = graph.getEdgeTarget(edgeToRemove);

            graph.removeEdge(edgeToRemove);

            KruskalMinimumSpanningTree<Intersection, DefaultWeightedEdge> altKruskal =
                    new KruskalMinimumSpanningTree<>(graph);
            var altMst = altKruskal.getSpanningTree();

            if (altMst.getWeight() > 0 && altMst.getEdges().size() == city.getIntersections().size() - 1)
            {
                alternativeCosts.add(altMst.getWeight());
            }

            DefaultWeightedEdge restoredEdge = graph.addEdge(source, target);
            graph.setEdgeWeight(restoredEdge, weight);
        }

        Collections.sort(alternativeCosts);
        int count = 1;
        for (Double cost : alternativeCosts) {
            System.out.println("Solutia:" + count + " cu cost " + cost);
            if (count++ >= maxAlternatives)
            {
                break;
            }
        }

        if (alternativeCosts.isEmpty()) {
            System.out.println("nu s-a gasit o alta solutie");
        }
    }
}