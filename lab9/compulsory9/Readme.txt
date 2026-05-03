BunnyThread-pt comportamentul iepurasului,ruleaza intr o bucla cat timp jocu e active apeland metoda gamestate.moveBunnt si facand o pauza intre mutari

ConcurrencyGame-clasa care initializeaza si ruleaza simularea jocului in consola.acesta construieste un labirint de test cu ziduri predefinite si initalizeaza gamestate si sharedmemmory.creeaza si porneste threadurile pt iepurasi si roboti

gamestate-gestioneaza starea tabelei de joc sincornizat.Aceasta stocheaza pozitiile iepurasului si ale robotilor

robotthread-pt fiecare robot in parte.Robotul interogheaza SharedMemory pt a afla daca se stie pozitia iepurasului,se muta prin gamestate.moveRobot.

SharedMemory-zona de memorie folosita pt comunicarea intre roboti.


Rezultatul este doar in consola.