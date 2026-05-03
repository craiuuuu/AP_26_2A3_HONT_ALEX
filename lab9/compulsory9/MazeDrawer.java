package lab9.compulsory9;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MazeDrawer
{
    private final double dimensiuneCelula = 30.0;

    //celulele zid sunt negre si libere albe
    public void drawMaze(Maze maze, Pane container)
    {
        container.getChildren().clear();

        for (int i = 0; i < maze.getN(); i++)
        {
            for (int j = 0; j < maze.getM(); j++)
            {
                Celula celulaData = maze.getCelula(i, j);

                Rectangle rect = new Rectangle(
                        j * dimensiuneCelula,
                        i * dimensiuneCelula,
                        dimensiuneCelula,
                        dimensiuneCelula);

                rect.setSmooth(false);
                rect.setFill(celulaData.esteZid() ? Color.BLACK : Color.WHITE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1.0);

                container.getChildren().add(rect);
            }
        }
    }

   //pt labirint dar nu cred ca l mai folosim
    public void drawMazeRemovedBorders(Maze maze, Pane container)
    {
        container.getChildren().clear();

        for (int i = 0; i < maze.getN(); i++)
        {
            for (int j = 0; j < maze.getM(); j++)
            {
                Celula celulaData = maze.getCelula(i, j);

                Rectangle rect = new Rectangle(
                        j * dimensiuneCelula,
                        i * dimensiuneCelula,
                        dimensiuneCelula + 0.5,
                        dimensiuneCelula + 0.5);

                rect.setSmooth(false);
                rect.setFill(Color.web(celulaData.getCuloareHex()));
                rect.setStroke(celulaData.esteZid() ? Color.web("white") : Color.web("black"));

                container.getChildren().add(rect);
            }
        }
    }
}
