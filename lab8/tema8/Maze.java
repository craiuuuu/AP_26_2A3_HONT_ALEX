package lab8.tema8;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class Maze implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int n, m;
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
