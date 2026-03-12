package lab3.advanced3;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Designer extends Person
{
    private String designSoftware;

    public Designer(int id, String name, LocalDate birthDate, String nationality, String designSoftware)
    {
        super(id, name, birthDate, nationality);
        this.designSoftware = designSoftware;
    }
}