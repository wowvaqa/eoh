package com.vs.network;

import com.badlogic.gdx.Gdx;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mob;
import com.vs.eoh.Ruch;

/**
 * Created by v on 2016-04-14.
 */
public class NetEngine {

    public GameStatus gs;
    public Assets a;

    public NetEngine(GameStatus gs, Assets a) {
        this.gs = gs;
        this.a = a;
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

        Ruch.makeNetworkMove(gs.getGracze().get(nm.player).getBohaterowie().get(nm.hero)
                , gs, nm.ruchX, nm.ruchY);
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

        tmpBohater = gs.getGracze().get(dh.player).getBohaterowie().get(dh.hero);
        tmpBohater.setActualHp(tmpBohater.getActualHp() - dh.damage);
        tmpBohater.teksturaZaktualizowana = false;
        tmpBohater.animujCiecieNetwork = true;
        tmpBohater.damageNetwork = dh.damage;
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

        tmpMob = gs.getMapa().getPola()[dm.pozXmoba][dm.pozYmoba].getMob();
        tmpMob.setAktualneHp(tmpMob.getAktualneHp() - dm.damage);
        tmpMob.animujCiecieNetwork = true;
        tmpMob.damageNetwork = dm.damage;
    }
}
