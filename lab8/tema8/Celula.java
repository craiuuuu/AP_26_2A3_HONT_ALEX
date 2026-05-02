package lab8.tema8;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class Celula implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int rand;
    private int coloana;
    private boolean zid;       // true atunci celula este zid adica blocata
    private boolean zidNord;
    private boolean zidSud;
    private boolean zidEst;
    private boolean zidVest;
    private String culoareHex;

    public Celula(int rand, int coloana)
    {
        this.rand      = rand;
        this.coloana   = coloana;
        this.zid       = false;
        this.zidNord   = false;
        this.zidSud    = false;
        this.zidEst    = false;
        this.zidVest   = false;
        this.culoareHex = "#FFFFFF";
    }

    //returneaza true daca celula este zid
    public boolean esteZid()
    {
        return zid;
    }
}
