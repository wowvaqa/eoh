package com.vs.network;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.vs.enums.DostepneItemki;
import com.vs.eoh.Assets;
import com.vs.eoh.Item;

import java.io.Serializable;

/**
 * Created by v on 2016-04-09.
 */
public class Network {

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterName.class);
        kryo.register(String[].class);
        kryo.register(UpdateNames.class);
        kryo.register(ChatMessage.class);
        kryo.register(ChatMessagePrivate.class);
        kryo.register(Move.class);
        kryo.register(DamageHero.class);
        kryo.register(DamageMob.class);
        kryo.register(RemoveTresureBox.class);
        kryo.register(AddItemEquip.class);
        kryo.register(DostepneItemki.class);
        kryo.register(EndOfTurn.class);
        kryo.register(StartMultiGame.class);
        kryo.register(NetworkMap.class);
        kryo.register(NetworkPole.class);
        kryo.register(NetworkPole[][].class);
        kryo.register(NetworkPole[].class);
    }

    static public class RegisterName {
        public String name;
    }

    static public class UpdateNames {
        public String[] names;
    }

    static public class ChatMessage {
        public String text;
    }

    static public class ChatMessagePrivate {
        public String text;
        public String name;
    }

    /**
     * Klasa odpowiada za ruch bohatera przez sieć.
     */
    static public class Move {
        public int ruchX;
        public int ruchY;
        public int player;
        public int hero;
    }

    /**
     * Klasa odpowiada za przekazanie obrażeń przez sieć dla zadanego bohatera
     */
    static public class DamageHero {
        public int damage;
        public int player;
        public int hero;
    }

    /**
     * Klasa odpowiada za przekazanie obrażeń przez sieć dla zadanego moba.
     */
    static public class DamageMob {
        // Obrażenia zadane Mobowi.
        public int damage;
        // Pozycaj X na mapie Moba.
        public int pozXmoba;
        // Pozyca Y na mapie Moba.
        public int pozYmoba;
    }

    /**
     * Klasa sieciowa odpowiadająca za przesłąnie inforamcji dot. usunięcia tresureBoxa.
     */
    static public class RemoveTresureBox {
        // Pozycja X skrzyni ze skarbem.
        public int pozX;
        // Pozycja Y skrzyni ze skarbem.
        public int pozY;
    }

    /**
     * Klasa sieciowa odpowiadająca za przesłanie itema.
     */
    static public class AddItemEquip {
        //public String nazwaItema;
        public DostepneItemki item;
        public int player;
        public int hero;
        public int czescCiala;
    }

    /**
     * Klasa oznajmia że nastąpił koniec tury kolejnego gracza.
     */
    static public class EndOfTurn {
        /**
         * Klasa jest pusta, odebranie klasy od serwera oznacza że kolejny z graczy zakończył
         * turę.
         */
    }

    static public class StartMultiGame {
        /**
         * Klasa jest pusta, odebranie obiektu klasy przez klienta zwiększa ilość graczy
         * multi.
         */
    }

    /**
     * Klasa definiuje Mape do przesyłu przez sieć.
     */
    public static class NetworkMap implements Serializable {
        public int amountX;
        public int amountY;
        public String nazwa = "NoName";
        public NetworkPole[][] networkPole;

        public NetworkMap(int amountOfX, int amountOfY) {
            networkPole = new NetworkPole[amountOfX][amountOfY];
            for (int i = 0; i < amountOfX; i++) {
                for (int j = 0; j < amountOfY; j++) {
                    this.networkPole[i][j] = new NetworkPole();
                }
            }
            this.amountX = amountOfX;
            this.amountY = amountOfY;
        }

        public NetworkMap() {
            networkPole = new NetworkPole[0][0];
        }
    }

    /**
     * Klasa definiuje Pole do przesyłu przez sieć
     */
    public static class NetworkPole implements Serializable {
        public boolean isPlayer1Start = false;
        public boolean isPlayer2Start = false;
        public boolean isPlayer3Start = false;
        public boolean isPlayer4Start = false;

        public boolean isMobLevel1 = false;
        public boolean isMobLevel2 = false;

        public boolean isTresureBoxLevel1 = false;
        public boolean isTresureBoxLevel2 = false;

        /*
        1 - TRAWA, 2 - GÓRY, 3 - DRZEWO, 4 - RZEKA
         */
        public boolean isTerrainType1 = false;
        public boolean isTerrainType2 = false;
        public boolean isTerrainType3 = false;
        public boolean isTerrainType4 = false;
    }
}
