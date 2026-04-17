package lab6.tema6;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actor
{
    private int id;
    private String name;

    public Actor(String name)
    {
        this.name = name;
    }
}