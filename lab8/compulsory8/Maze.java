package lab8.compulsory8;

import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Maze
{
    private int n, m;//n=linii m coloane
    private Celula[][] maze;

    public Maze(int n, int m)
    {
        this.n = n;
        this.m = m;
        this.maze = new Celula[n][m];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                maze[i][j] = new Celula(i, j);
            }
        }
    }

    public Celula getCelula(int rand, int coloana)
    {
        return maze[rand][coloana];
    }
}
