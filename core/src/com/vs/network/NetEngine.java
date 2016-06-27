package com.vs.network;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.GameStatus;
import com.vs.eoh.ItemCreator;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.Ruch;
import com.vs.eoh.TresureBox;
import com.vs.eoh.V;

/**
 * Created by v on 2016-04-14.
 */
public class NetEngine {

    // Liczba graczy którzy zakończyli turę.
    public static int playersEndTurn = 0;
    public static int amountOfMultiPlayers = 0;
    // Którym graczem Multi jest gracz.
    public static int playerNumber = 0;
    public static boolean gameStarted = false;
    private V v;

    public NetEngine(V v) {
        this.v = v;
    }

    /**
     * Metoda odbiera od serwera obiekt klasy Network.Move i uruchamia odpowiednie metody.
     *
     * @param nm referencja do obiektu klasy Network.Move
     */
    public void networkMove(Network.Move nm) {
        Gdx.app.log("NetworkMove", "Klient odebrał ruch od serwera");
        Gdx.app.log("RuchX", "" + nm.ruchX);
        Gdx.app.log("RuchY", "" + nm.ruchY);
        Gdx.app.log("Indeks Gracza  ", "" + nm.player);
        Gdx.app.log("Indeks Bohatera", "" + nm.hero);

        Ruch.makeNetworkMove(v.getGs().getGracze().get(nm.player).getBohaterowie().get(nm.hero)
                , v, nm.ruchX, nm.ruchY);
    }

    /**
     * Metoda odbiera od serwera obiekt klasy Network.DamageHero i uruchamia odpowiednie metody.
     *
     * @param dh referencja do obiektu Network.DamageHero
     */
    public void damageHero(Network.DamageHero dh) {
        Gdx.app.log("Network.DamageHero", "Klient odebrał obrażenia dla bohatera");
        Gdx.app.log("Indeks Gracza", "" + dh.player);
        Gdx.app.log("Indeks Bohatera", "" + dh.hero);
        Gdx.app.log("Obrazenia", "" + dh.damage);

        Bohater tmpBohater;

        tmpBohater = v.getGs().getGracze().get(dh.player).getBohaterowie().get(dh.hero);
        tmpBohater.setActualHp(tmpBohater.getActualHp() - dh.damage);
        tmpBohater.teksturaZaktualizowana = false;
        tmpBohater.animujCiecieNetwork = true;
        tmpBohater.damageNetwork = dh.damage;

        if (tmpBohater.getActualHp() < 0) {
            tmpBohater.remove();
        }
    }

    /**
     * Metoda odbiera od serwera obiekt klasy Network.DamageMob i uruchamia odpowiednie metody.
     *
     * @param dm referencja do obiektu klasy Network.DamageMob
     */
    public void damageMob(Network.DamageMob dm) {
        Gdx.app.log("Network.DamageMob", "Klient odebrał obrażenia dla Moba");
        Gdx.app.log("Pozycja X moba", "" + dm.pozXmoba);
        Gdx.app.log("Pozycja Y moba", "" + dm.pozYmoba);
        Gdx.app.log("Obrazenia", "" + dm.damage);

        Mob tmpMob;

        tmpMob = v.getGs().getMapa().getPola()[dm.pozXmoba][dm.pozYmoba].getMob();
        tmpMob.setAktualneHp(tmpMob.getAktualneHp() - dm.damage);
        tmpMob.animujCiecieNetwork = true;
        tmpMob.damageNetwork = dm.damage;

        v.getGs().usunMartweMoby();
    }

    /**
     * Usuwa z lokalizacji podanej przez serwer TresureBoxa
     *
     * @param rtb Referencja do obiektu klasy RemoveTresure Box
     */
    public void removeTresureBox(Network.RemoveTresureBox rtb) {
        Gdx.app.log("Network.RemoveTresureBox", "Klient odebrał skrzynię ze skarbem do usunięcia");
        Gdx.app.log("Pozycja X skrzyni", "" + rtb.pozX);
        Gdx.app.log("Pozycja Y skrzyni", "" + rtb.pozY);

        TresureBox tmpTresureBox;

        tmpTresureBox = v.getGs().getMapa().getPola()[rtb.pozX][rtb.pozY].getTresureBox();
        TresureBox.removeTresureBox(tmpTresureBox, v.getGs().getMapa());
    }

    /**
     * Klasa odpowiada za dodanie itema do bohatera zadanego przez obiekt
     *
     * @param aie
     */
    public void addItemEquip(Network.AddItemEquip aie) {
        /*
        Części ciała:
        0 - Głowa, 1 - Korpus, 2 - Prawa ręka, 3 - Lewa ręka, 4 - Nogi, 5 - stopy
         */

        Gdx.app.log("Network.AddItemEquip", "Klient odebrał Item");
        Gdx.app.log("Numer bohatera", "" + aie.hero);
        Gdx.app.log("Numer Gracza", "" + aie.player);
        Gdx.app.log("Item", "" + aie.item.toString());
        Gdx.app.log("Część ciała", "" + aie.czescCiala);


        ItemCreator itemCreator = new ItemCreator(v);

        Bohater tmpHero;
        tmpHero = v.getGs().getGracze().get(aie.player).getBohaterowie().get(aie.hero);

        switch (aie.czescCiala) {
            case 0:
                tmpHero.setItemGlowa(itemCreator.utworzItem(aie.item, v));
                break;
            case 1:
                tmpHero.setItemKorpus(itemCreator.utworzItem(aie.item, v));
                break;
            case 2:
                tmpHero.setItemPrawaReka(itemCreator.utworzItem(aie.item, v));
                break;
            case 3:
                tmpHero.setItemLewaReka(itemCreator.utworzItem(aie.item, v));
                break;
            case 4:
                tmpHero.setItemNogi(itemCreator.utworzItem(aie.item, v));
                break;
            case 5:
                tmpHero.setItemStopy(itemCreator.utworzItem(aie.item, v));
                break;
        }
    }

    /**
     * @param endOfTurn
     */
    public void endOfTurn(Network.EndOfTurn endOfTurn) {
        NetEngine.playersEndTurn += 1;
    }

    /**
     * Ustala którym graczem w grze jest połączony do serwera klient.
     *
     * @param startMultiGame
     */
    public void startMultiGame(Network.StartMultiGame startMultiGame) {
        if (!NetEngine.gameStarted) {
            NetEngine.amountOfMultiPlayers += 1;
            Gdx.app.log("Odebrano kolejnego gracza do rozgrywki", "Ilość graczy: " + NetEngine.amountOfMultiPlayers);
            NetEngine.playerNumber = NetEngine.amountOfMultiPlayers;
        }
    }

    /**
     * Odbiera mapę od serwera i konwertuje na obiekt klasy Mapa
     *
     * @param networkMap
     */
    public void networkMap(Network.NetworkMap networkMap) {
        Gdx.app.log("Odebrano mape", "");
        Mapa mapa = new Mapa(networkMap.amountX, networkMap.amountY);
        Mapa.convertTokMap(mapa, networkMap);
        v.getGs().setMapa(mapa);
    }
}
