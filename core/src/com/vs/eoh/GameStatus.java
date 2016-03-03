package com.vs.eoh;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.vs.enums.DostepneItemki;
import com.vs.testing.MapEditor;
import com.vs.testing.TestingScreen;

public class GameStatus {

    private int actualScreen = 0;                                               // zapamiętuje aktualną scenę która jest wyświetlana    
    private int lastScreen = 0;                                                 // Ostatnia scena która była wyświetlana
    private int predkoscScrollowaniaKamery = 10;                                // współczynnik prędkości dla scrollingu mapy w obiekcie klasy MapScreen
    private int predkoscZoomKamery = 10;                                        // współczynnik prędkości dla zoomu mapy w obiekcie klasy MapScreen
    public float xDlaInterfejsuRuchu = 0;
    public float yDlaInterfejsuRuchu = 0;
    public int iloscGraczy = 6;                                                 // ilosć graczy
    public int maxIloscBohaterow = 1;                                           // maksymalna ilość bohaterów   
    // informuje czy jakikolwiek z bohaterów został zaznaczony
    private boolean czyZaznaczonoBohatera = false;
    //private boolean wymaganePrzerysowanieTeksturyBohatera = true;

    // Zwraca nr gracza z tablicy graczy który posiada swoją turę
    private int turaGracza = 0;

    // Określa czy panel rzucania czarów jest aktywny.
    public boolean isSpellPanelActive = false;
    
    // Określa ogólną turę gry.
    private int turaGry = 0;

    // Do uniemożliweinia ruchu podczas penetrowania skrzyni ze skarbem    
    private Item item;

    public boolean czyUtworzonoMape = false;                                    // informuje jeżeli TRUE że możliwe jest utworzenie obiektu klasy MapScreen 

    public ArrayList<Gracz> gracze = new ArrayList<Gracz>();                    // przechowuje graczy     

    // Przechowuje mape z obiektami graczy w celu wychwycenia kolizji
    // public Mapa mapa = new Mapa();
    public Mapa mapa;

    /**
     * Określa wspóżędne TresureBoxa po zabiciu Moba
     */
    public static int wspolzedneXtresureBox = 999;
    public static int wspolzedneYtresureBox = 999;

    // Listy zawierają wszystkie dostępne itemy danych poziomów.
    public static ArrayList<DostepneItemki> itemyPoziom1 = new ArrayList<DostepneItemki>();
    public static ArrayList<DostepneItemki> itemyPoziom2 = new ArrayList<DostepneItemki>();

    public GameStatus() {
        wczytajItemy();
//        try {
//            wczytajMape();
//        } catch (IOException ex) {
//            Logger.getLogger(GameStatus.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(GameStatus.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void wczytajMape() throws IOException, ClassNotFoundException {
        //ObjectInputStream we = new ObjectInputStream(new FileInputStream("mapa.dat"));
        //mapa = (Mapa) we.readObject();

        mapa = MapEditor.readMap();

        System.out.println("odczyt obiektu");
    }

    public int getIloscGraczy() {
        return iloscGraczy;
    }

    /**
     * Zwraca referencje do zaznaczonego bohatera na mapie
     *
     * @return zwaraca referencje do obiketu zaznaczonego bohatera
     */
    public Bohater getBohaterZaznaczony() {
        for (Gracz gracz : this.getGracze()) {
            for (Bohater bohater : gracz.getBohaterowie()) {
                if (bohater.isZaznaczony()) {
                    return bohater;
                }
            }
        }
        return null;
    }

    public void dodajDoZlotaAktualnegoGracza(int zloto) {
        this.gracze.get(this.turaGracza).setGold(zloto
                + this.getZlotoAktualnegoGracza());
    }

    /**
     * Usuwa Moby lub Bohaterów których HP < 0
     */
    public void usunMartweMoby() {
        for (int i = 0; i < this.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < this.getMapa().getIloscPolY(); j++) {
                if (this.getMapa().pola[i][j].getMob() != null) {
                    if (this.getMapa().pola[i][j].getMob().getAktualneHp() <= 0) {
                        this.getMapa().pola[i][j].setMob(null);

                        wspolzedneXtresureBox = i;
                        wspolzedneYtresureBox = j;
                    }
                }
            }
        }
        for (int j = 0; j < Assets.stage01MapScreen.getActors().size; j++) {
            if (Assets.stage01MapScreen.getActors().get(j).getClass() == Mob.class) {
                Mob tmpMob = (Mob) Assets.stage01MapScreen.getActors().get(j);
                if (tmpMob.getAktualneHp() < 1) {
                    Assets.stage01MapScreen.getActors().removeIndex(j);
                }
            }
        }

        for (int i = 0; i < this.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < this.getMapa().getIloscPolY(); j++) {
                if (this.getMapa().pola[i][j].getBohater() != null) {
                    if (this.getMapa().pola[i][j].getBohater().getActualHp() <= 0) {
                        this.getMapa().pola[i][j].setBohater(null);
                    }
                }
            }
        }
    }

    private void wczytajItemy() {
        //Poziom 1
        itemyPoziom1.add(DostepneItemki.Kij);
        itemyPoziom1.add(DostepneItemki.LnianaCzapka);
        itemyPoziom1.add(DostepneItemki.Tarcza);
        itemyPoziom1.add(DostepneItemki.Luk);
        itemyPoziom1.add(DostepneItemki.LnianeSpodnie);
        itemyPoziom1.add(DostepneItemki.SkorzaneButy);
        itemyPoziom1.add(DostepneItemki.PotionZdrowie);
        itemyPoziom1.add(DostepneItemki.PotionSzybkosc);
        itemyPoziom1.add(DostepneItemki.PotionAttack);
        itemyPoziom1.add(DostepneItemki.PotionDefence);

        //Poziom 2
        itemyPoziom2.add(DostepneItemki.SkorzanaCzapka);
        itemyPoziom2.add(DostepneItemki.Miecz);
        itemyPoziom2.add(DostepneItemki.SkorzaneSpodnie);
    }

    // GETTER AND SETTERS
    /**
     * Zwraca ilość złota gracza który aktualnie ma turę
     *
     * @return
     */
    public int getZlotoAktualnegoGracza() {
        return this.gracze.get(this.turaGracza).getGold();
    }

    public int getTuraGracza() {
        return turaGracza;
    }

    public void setTuraGracza(int turaGracza) {
        this.turaGracza = turaGracza;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setIloscGraczy(int iloscGraczy) {
        this.iloscGraczy = iloscGraczy;
    }

    public int getActualScreen() {
        return actualScreen;
    }

    public void setActualScreen(int actualScreen) {
        this.actualScreen = actualScreen;
    }

    public int getPredkoscScrollowaniaKamery() {
        return predkoscScrollowaniaKamery;
    }

    public void setPredkoscScrollowaniaKamery(int predkoscScrollowaniaKamery) {
        this.predkoscScrollowaniaKamery = predkoscScrollowaniaKamery;
    }

    public int getPredkoscZoomKamery() {
        return predkoscZoomKamery;
    }

    public void setPredkoscZoomKamery(int predkoscZoomKamery) {
        this.predkoscZoomKamery = predkoscZoomKamery;
    }

    public ArrayList<Gracz> getGracze() {
        return gracze;
    }

    public void setGracze(ArrayList<Gracz> gracze) {
        this.gracze = gracze;
    }

    public boolean isCzyZaznaczonoBohatera() {
        return czyZaznaczonoBohatera;
    }

    public void setCzyZaznaczonoBohatera(boolean czyZaznaczonoBohatera) {
        this.czyZaznaczonoBohatera = czyZaznaczonoBohatera;
    }

    public int getLastScreen() {
        return lastScreen;
    }

    public void setLastScreen(int lastScreen) {
        this.lastScreen = lastScreen;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Zwraca turę gry
     *
     * @return Tura gry
     */
    public int getTuraGry() {
        return turaGry;
    }

    /**
     * Ustala turę gry
     *
     * @param turaGry tura gry
     */
    public void setTuraGry(int turaGry) {
        this.turaGry = turaGry;
    }

}
