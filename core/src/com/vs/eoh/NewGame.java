package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.Spells;
import com.vs.network.NetEngine;
import com.vs.network.Network;
import com.vs.screens.MapScreen;

import java.io.IOException;

/**
 * Klasa zarządza zachowanie nowej gry
 *
 * @author v
 */
public class NewGame {

    private static final Assets a = new Assets();
    static public KlasyPostaci klasaPostaciGracz01 = KlasyPostaci.Wojownik;
    static public KlasyPostaci klasaPostaciGracz02 = KlasyPostaci.Wojownik;
    static public KlasyPostaci klasaPostaciGracz03 = KlasyPostaci.Wojownik;
    static public KlasyPostaci klasaPostaciGracz04 = KlasyPostaci.Wojownik;
    static public Pixmap pixmapRed = new Pixmap(50, 25, Pixmap.Format.RGBA8888);
    static public Pixmap pixmapBlue = new Pixmap(50, 25, Pixmap.Format.RGBA8888);
    static public Pixmap pixmapGreen = new Pixmap(50, 25, Pixmap.Format.RGBA8888);
    static public Pixmap pixmapYellow = new Pixmap(50, 25, Pixmap.Format.RGBA8888);
    /**
     * Określa ilość graczy
     */
    public static int iloscGraczy = 2;

    /**
     * Zwiększa ilość graczy
     *
     * @param iloscGraczy
     * @return
     */
    static public int dodajGracza(int iloscGraczy) {
        int tmpIloscGraczy = iloscGraczy;

        tmpIloscGraczy += 1;
        if (tmpIloscGraczy > 4) {
            tmpIloscGraczy = 2;
        }
        return tmpIloscGraczy;
    }

    /**
     * Zmniejsza ilosc graczy
     *
     * @param iloscGraczy
     * @return
     */
    static public int odejmijGracza(int iloscGraczy) {
        iloscGraczy -= 1;
        if (iloscGraczy < 2) {
            iloscGraczy = 4;
        }
        return iloscGraczy;
    }

    /**
     * Pobiera klase postaci gracza i zwraca następną z enum-a
     *
     * @param kP klasa postaci
     * @return
     */
    static public KlasyPostaci nastepnaKlasaPostaci(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return KlasyPostaci.Giermek;
            case Giermek:
                return KlasyPostaci.Lowca;
            case Lowca:
                return KlasyPostaci.Twierdza;
            case Twierdza:
                return KlasyPostaci.Czarodziej;
            case Czarodziej:
                return KlasyPostaci.Wojownik;
        }
        return KlasyPostaci.Wojownik;
    }

    /**
     * Pobiera klase postaci gracza i zwraca następną z enum-a
     *
     * @param kP klasa postaci
     * @return
     */
    static public KlasyPostaci poprzedniaKlasaPostaci(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return KlasyPostaci.Czarodziej;
            case Giermek:
                return KlasyPostaci.Wojownik;
            case Lowca:
                return KlasyPostaci.Giermek;
            case Twierdza:
                return KlasyPostaci.Lowca;
            case Czarodziej:
                return KlasyPostaci.Twierdza;

        }
        return KlasyPostaci.Wojownik;
    }

    /**
     * Zwraca aktora zawierającego odpowiedni portret
     *
     * @param kP Klasa postaci dla której ma być zwrócony odpowiedni portret
     * @return
     */
    static public DefaultActor pobierzPortret(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return new DefaultActor(a.mobHumanTex, 0, 0);
            case Lowca:
                return new DefaultActor(a.mobElfTex, 0, 0);
            case Giermek:
                return new DefaultActor(a.mobDwarfTex, 0, 0);
            case Twierdza:
                return new DefaultActor(a.mobOrkTex, 0, 0);
            case Czarodziej:
                return new DefaultActor(a.mobWizardTex, 0, 0);
        }
        return null;
    }

    /**
     * Zwraca String z odpowiednią nazwą klasy
     *
     * @param kP klasa postaci
     * @return
     */
    static public String pobierzTytul(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return "Wojownik";
            case Giermek:
                return "Giermek";
            case Lowca:
                return "Lowca";
            case Twierdza:
                return "Twierdza";
            case Czarodziej:
                return "Czarodziej";
        }
        return "error";
    }

    /**
     * Zwraca siłę ataku w zależności od klasy postaci
     *
     * @param kP klasa postaci
     * @return
     */
    static public int pobierzAtak(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 6;
            case Giermek:
                return 5;
            case Lowca:
                return 5;
            case Twierdza:
                return 5;
            case Czarodziej:
                return 3;
        }
        return 0;
    }

    /**
     * Zwraca wartość obrony w zależności od klasy postaci
     *
     * @param kP
     * @return
     */
    static public int pobierzObrone(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 5;
            case Giermek:
                return 6;
            case Lowca:
                return 5;
            case Twierdza:
                return 5;
            case Czarodziej:
                return 5;
        }
        return 0;
    }

    /**
     * Zwraca szybkość w zależności od klasy postaci
     *
     * @param kP Klasa postaci
     * @return
     */
    static public int pobierzSzybkosc(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 5;
            case Giermek:
                return 5;
            case Lowca:
                return 6;
            case Twierdza:
                return 5;
            case Czarodziej:
                return 5;
        }
        return 0;
    }

    /**
     * Zwraca ilość punktów życia w zależności od klasy postaci
     *
     * @param kP
     * @return
     */
    static public int pobierzHp(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 10;
            case Giermek:
                return 10;
            case Lowca:
                return 10;
            case Twierdza:
                return 15;
            case Czarodziej:
                return 10;
        }
        return 0;
    }

    /**
     *
     * @param kP
     * @return
     */
    static public int pobierzMane(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 1;
            case Giermek:
                return 1;
            case Lowca:
                return 1;
            case Twierdza:
                return 1;
            case Czarodziej:
                return 3;
        }
        return 0;
    }

    /**
     * Zwraca ilość mocy dla danej kalsy postaci
     * @param kP KlasaPostaci
     * @return int Ilość mocy
     */
    static public int pobierzMoc(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 1;
            case Giermek:
                return 1;
            case Lowca:
                return 1;
            case Twierdza:
                return 1;
            case Czarodziej:
                return 3;
        }
        return 0;
    }

    /**
     *
     * @param kP
     * @return
     */
    static public int pobierzWiedze(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return 1;
            case Giermek:
                return 1;
            case Lowca:
                return 1;
            case Twierdza:
                return 1;
            case Czarodziej:
                return 3;
        }
        return 0;
    }

    /**
     * @param g
     * @param gs
     * @param a
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    static public void zakonczGenerowanieNowejGry(Game g, GameStatus gs, Assets a) throws IOException, ClassNotFoundException {

        if (gs.getNetworkStatus() != 2) {
            gs.wczytajMape();
        }

        gs.setTuraGracza(0);

        //gs.setActualScreen(1);
        gs.iloscGraczy = iloscGraczy;
        // Po kliknięciu w OK następuje przekazanie info, że mapa
        // została utworzona (wszystkie parametry zadane przez screen
        // nowej gry zostaną użyte do tworzenia nowej mapy).
        gs.czyUtworzonoMape = true;

        // Sprawdzenie czy tablica gracz jest pusta i ewentualne wyczyszczenie jej
        if (gs.gracze.size() > 0) {
            gs.gracze.clear();
        }

        // Dodoaje nowych graczy wg. ilości zadeklarowanej
        for (int i = 0; i < gs.iloscGraczy; i++) {
            gs.gracze.add(new Gracz(i));
            gs.gracze.get(i).setNumerGracza(i);

            //System.out.println(gs.gracze.size());
        }
        // Dodanie dla każdego gracza bohatera
        for (int i = 0; i < gs.gracze.size(); i++) {
            // tymczasowa tekstura przekazana do konstruktora nowego bohatera
            Texture tmpTex = null, tmpTexZazanaczony = null;
            KlasyPostaci tmpKlasaPostaci = null;
            // tymczasowa tekstura do określenia lokacji początkowej gracza
            int lokPoczatkowaX = 0, lokPoczatkowaY = 0;

            // Ustawienie lokacji początkowej dla bohatera
            switch (i) {
                case 0:
                    lokPoczatkowaX = 0;
                    lokPoczatkowaY = 0;
                    tmpTex = getTeksturaBohatera(klasaPostaciGracz01);
                    tmpTexZazanaczony = getTeksturaBohateraZaznaczonego(klasaPostaciGracz01);
                    tmpKlasaPostaci = klasaPostaciGracz01;
                    break;
                case 1:
                    lokPoczatkowaX = 900;
                    lokPoczatkowaY = 900;
                    tmpTex = getTeksturaBohatera(klasaPostaciGracz02);
                    tmpTexZazanaczony = getTeksturaBohateraZaznaczonego(klasaPostaciGracz02);
                    tmpKlasaPostaci = klasaPostaciGracz02;
                    break;
                case 2:
                    lokPoczatkowaX = 0;
                    lokPoczatkowaY = 900;
                    tmpTex = getTeksturaBohatera(klasaPostaciGracz03);
                    tmpTexZazanaczony = getTeksturaBohateraZaznaczonego(klasaPostaciGracz03);
                    tmpKlasaPostaci = klasaPostaciGracz03;
                    break;
                case 3:
                    lokPoczatkowaX = 900;
                    lokPoczatkowaY = 0;
                    tmpTex = getTeksturaBohatera(klasaPostaciGracz04);
                    tmpTexZazanaczony = getTeksturaBohateraZaznaczonego(klasaPostaciGracz04);
                    tmpKlasaPostaci = klasaPostaciGracz04;
                    break;
            }
            gs.gracze.get(i).getBohaterowie().add(new Bohater(tmpTex, tmpTexZazanaczony, lokPoczatkowaX, lokPoczatkowaY, a, 0, 0, gs, g, tmpKlasaPostaci));
            // Ustala do którego gracza z tablicy graczy należy bohater
            gs.gracze.get(i).getBohaterowie().get(0).setPrzynaleznoscDoGracza(i);
            gs.gracze.get(i).getBohaterowie().get(0).setKlasyPostaci(tmpKlasaPostaci);
            System.out.println(tmpKlasaPostaci.toString() + "Klasa postaci: ");

            //ustawPozycjeNaMapie(gs, i);
        }

        podepnijStatystkiBohaterow(gs);

        if (gs.getNetworkStatus() == 2) {
            NetEngine.gameStarted = true;
            Network.StartMultiGame startMultiGame = new Network.StartMultiGame();
            GameStatus.client.getCnt().sendTCP(startMultiGame);
            gs.setTuraGracza(NetEngine.playerNumber);
        }

        Assets.mapScreen = new MapScreen(g, a, gs);
    }

    /**
     * W zależnosci od klasy bohatera pdopina odpowiednie czary.
     */
    private static void podepnijCzary(KlasyPostaci kP, Bohater bohater) {
        //SpellCreator spellCreator = new SpellCreator(a, gs);
        switch (kP) {
            case Wojownik:
                bohater.getListOfSpells().add(Spells.Rage);
                //bohater.getListOfSpells().add(Spells.Charge);
                //bohater.getListOfSpells().add(Spells.Discouragement);
                //bohater.getListOfSpells().add(Spells.Fury);
                //bohater.getListOfSpells().add(Spells.FinalJudgment);
                break;
            case Giermek:
                bohater.getListOfSpells().add(Spells.Frozen);
                break;
            case Lowca:
                bohater.getListOfSpells().add(Spells.Haste);
                break;
            case Twierdza:
                bohater.getListOfSpells().add(Spells.Cure);
                break;
            case Czarodziej:
                bohater.getListOfSpells().add(Spells.FireBall);
                bohater.getListOfSpells().add(Spells.Thunder);
                bohater.getListOfSpells().add(Spells.MeteorShower);
                bohater.getListOfSpells().add(Spells.Bless);
                bohater.getListOfSpells().add(Spells.Prayer);
                break;
        }
    }

    /**
     * Podpina odpowiednie statystyki pod poszczególnego bohatera każdego
     * gracza.
     *
     * @param gs GameStatus
     */
    private static void podepnijStatystkiBohaterow(GameStatus gs) {
        for (int i = 0; i < iloscGraczy; i++) {
            KlasyPostaci tmpKp = null;

            switch (i) {
                case 0:
                    tmpKp = klasaPostaciGracz01;
                    break;
                case 1:
                    tmpKp = klasaPostaciGracz02;
                    break;
                case 2:
                    tmpKp = klasaPostaciGracz03;
                    break;
                case 3:
                    tmpKp = klasaPostaciGracz04;
                    break;
            }

            gs.gracze.get(i).getBohaterowie().get(0).setAtak(pobierzAtak(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setObrona(pobierzObrone(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setHp(pobierzHp(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setActualHp(pobierzHp(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setSzybkosc(pobierzSzybkosc(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setPozostaloRuchow(pobierzSzybkosc(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setMana(pobierzMane(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setActualMana(pobierzMane(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setMoc(pobierzMoc(tmpKp));
            gs.gracze.get(i).getBohaterowie().get(0).setWiedza(pobierzWiedze(tmpKp));

            podepnijCzary(tmpKp, gs.gracze.get(i).getBohaterowie().get(0));

            switch (i) {
                case 0:
                    gs.gracze.get(i).setColor(Color.RED);
                    break;
                case 1:
                    gs.gracze.get(i).setColor(Color.BLUE);
                    break;
                case 2:
                    gs.gracze.get(i).setColor(Color.YELLOW);
                    break;
                case 3:
                    gs.gracze.get(i).setColor(Color.GREEN);
                    break;
            }
        }
    }

    /**
     * Ustala odpowiednią teksturę dla bohatera
     *
     * @param kP Klasa Postaci
     * @return
     */
    public static Texture getTeksturaBohatera(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return a.mobHumanTex;
            case Lowca:
                return a.mobElfTex;
            case Giermek:
                return a.mobDwarfTex;
            case Twierdza:
                return a.mobOrkTex;
            case Czarodziej:
                return a.mobWizardTex;
        }
        return null;
    }

    /**
     * Ustawia teksturę bohatera zaznaczonego
     *
     * @param kP Klasa Postaci
     * @return
     */
    public static Texture getTeksturaBohateraZaznaczonego(KlasyPostaci kP) {
        switch (kP) {
            case Wojownik:
                return a.mobHumanTexZaznaczony;
            case Lowca:
                return a.mobElfTexZaznaczony;
            case Giermek:
                return a.mobDwarfTexZaznaczony;
            case Twierdza:
                return a.mobOrkTexZaznaczony;
            case Czarodziej:
                return a.mobWizardTexZaznaczony;
        }
        return null;
    }
}
