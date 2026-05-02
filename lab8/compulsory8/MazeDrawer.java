package lab8.compulsory8;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class MazeDrawer
{
    private double dimensiuneCelula =30.0;

    public void drawMazeRemovedBorders(Maze maze,Pane container)
    {
        container.getChildren().clear();

        Random r = new Random();
        int[] numere = r.ints(maze.getN()*maze.getM(), 0, Math.max(maze.getN(),maze.getM())).toArray();
        int count=0;
        for (int i = 0; i < maze.getN(); i++)
        {
            for (int j = 0; j < maze.getM(); j++)
            {
                Celula celulaData = maze.getCelula(i, j);

                Rectangle rect = new Rectangle(j*dimensiuneCelula, i*dimensiuneCelula, dimensiuneCelula+0.5, dimensiuneCelula+0.5);
                rect.setSmooth(false);
                rect.setFill(Color.web(celulaData.getCuloareHex()));

                rect.setStroke(Color.web("black"));
                count=0;
                while(count<maze.getN()*maze.getM()-1)
                {
                    if(celulaData.getRand()== numere[count] && celulaData.getColoana()==numere[count+1])
                    {
                        rect.setStroke(Color.web("white"));
                        break;
                    }
                    count=count+2;
                }


                container.getChildren().add(rect);
            }
        }
    }

    public void drawMaze(Maze maze, Pane container)
    {

        container.getChildren().clear();


        for (int i = 0; i < maze.getN(); i++)
        {
            for (int j = 0; j < maze.getM(); j++)
            {
                Celula celulaData = maze.getCelula(i, j);

                Rectangle rect = new Rectangle(j*dimensiuneCelula, i*dimensiuneCelula, dimensiuneCelula+0.5, dimensiuneCelula+0.5);
                rect.setSmooth(false);
                rect.setFill(Color.web(celulaData.getCuloareHex()));

                rect.setStroke(Color.web("black"));


                container.getChildren().add(rect);
            }
        }
    }
}