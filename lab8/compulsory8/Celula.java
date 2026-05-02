package lab8.compulsory8;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Celula
{

    private int rand;
    private int coloana;
    private boolean zid;
    private boolean zidNord;
    private boolean zidSud;
    private boolean zidEst;
    private boolean zidVest;
    private String culoareHex;

    public Celula(int rand, int coloana)
    {
        this.rand = rand;
        this.coloana = coloana;
        this.zid = false;
        this.zidNord = false;
        this.zidSud = false;
        this.zidEst = false;
        this.zidVest = false;
        this.culoareHex = "#FFFFFF";
    }

    public boolean esteZid()
    {
        return zid;
    }

}
