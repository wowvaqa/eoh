package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.vs.enums.DostepneItemki;
import com.vs.network.RunClient;
import com.vs.network.RunServer;
import com.vs.screens.MultiplayerScreen;
import com.vs.testing.MapEditor;

import java.io.IOException;
import java.util.ArrayList;

public class GameStatus {

    public static String nazwaMapy = "brak";
    // Pola statyczne dla serwera i klienta.
    public static RunServer server;
    public static RunClient client;
    public static MultiplayerScreen mS;
    // Listy zawierają wszystkie dostępne itemy danych poziomów.
    public static ArrayList<DostepneItemki> itemyPoziom1 = new ArrayList<DostepneItemki>();
    public static ArrayList<DostepneItemki> itemyPoziom2 = new ArrayList<DostepneItemki>();
    //    public static GameStatus gs;
//    public static Assets a;
//    public static Game g;
    public static boolean gameStart = false;
    public static int CostOfNewHero = 20;
    public float xDlaInterfejsuRuchu = 0;
    public float yDlaInterfejsuRuchu = 0;
    public int iloscGraczy = 6;                                                 // ilosć graczy
    public int maxIloscBohaterow = 1;                                           // maksymalna ilość bohaterów
    //private boolean wymaganePrzerysowanieTeksturyBohatera = true;
    // Określa czy panel rzucania czarów jest aktywny.
    public boolean isSpellPanelActive = false;
    public boolean czyUtworzonoMape = false;                                    // informuje jeżeli TRUE że możliwe jest utworzenie obiektu klasy MapScreen
    public ArrayList<Gracz> gracze = new ArrayList<Gracz>();                    // przechowuje graczy
    // Przechowuje mape z obiektami graczy w celu wychwycenia kolizji
    // public Mapa mapa = new Mapa();
    public Mapa mapa;
    // Status gry 0 - standAlone, 1 - Server, 2 - Client
    private int networkStatus = 0;
    private int actualScreen = 0;                                               // zapamiętuje aktualną scenę która jest wyświetlana
    private int lastScreen = 0;                                                 // Ostatnia scena która była wyświetlana
    private int predkoscScrollowaniaKamery = 10;                                // współczynnik prędkości dla scrollingu mapy w obiekcie klasy MapScreen
    private int predkoscZoomKamery = 10;                                        // współczynnik prędkości dla zoomu mapy w obiekcie klasy MapScreen
    // informuje czy jakikolwiek z bohaterów został zaznaczony
    private boolean czyZaznaczonoBohatera = false;
    // Zwraca nr gracza z tablicy graczy który posiada swoją turę
    private int turaGracza = 0;
    // Określa ogólną turę gry.
    private int turaGry = 0;
    // Do uniemożliweinia ruchu podczas penetrowania skrzyni ze skarbem
    private Item item;

    public GameStatus(Game g) {

        //gs = this;
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

        if (!nazwaMapy.equals("mapa z serwera")) {
            mapa = MapEditor.readMap(nazwaMapy);
        }
    }

    public int getIloscGraczy() {
        return iloscGraczy;
    }

    public void setIloscGraczy(int iloscGraczy) {
        this.iloscGraczy = iloscGraczy;
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

                        this.getMapa().pola[i][j].getMob().generujTresureBoxPoSmierciMoba(i, j
                                , this.getMapa().pola[i][j].getMob().getMobLevel());

                        this.getMapa().pola[i][j].setMob(null);
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

        for (int j = 0; j < Assets.stage01MapScreen.getActors().size; j++) {
            if (Assets.stage01MapScreen.getActors().get(j).getClass() == Bohater.class) {
                Bohater tmpMob = (Bohater) Assets.stage01MapScreen.getActors().get(j);
                if (tmpMob.getActualHp() < 1) {
                    Assets.stage01MapScreen.getActors().removeIndex(j);
                }
            }
        }
    }

    // GETTER AND SETTERS

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
        itemyPoziom1.add(DostepneItemki.Laska);
        itemyPoziom1.add(DostepneItemki.MagicznyKaptur);

        //Poziom 2
        itemyPoziom2.add(DostepneItemki.SkorzanaCzapka);
        itemyPoziom2.add(DostepneItemki.DlugiLuk);
        itemyPoziom2.add(DostepneItemki.Miecz);
        itemyPoziom2.add(DostepneItemki.SkorzaneSpodnie);
        itemyPoziom2.add(DostepneItemki.SkorzanyNapiersnik);
        itemyPoziom2.add(DostepneItemki.WzmocnioneSkorzaneButy);
    }

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

    /**
     * Ustala mapę
     *
     * @param mapa Obiekt klasy Mapa
     */
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
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

    /**
     * Zwraca status gry
     * @return 0 = StandAlone, 1 = Server, 2 = Client
     */
    public int getNetworkStatus() {
        return networkStatus;
    }

    /**
     * Ustala Status gry
     * @param networkStatus 0 = StandAlone, 1 = Server, 2 = Client
     */
    public void setNetworkStatus(int networkStatus) {
        this.networkStatus = networkStatus;
    }
}
