package com.vs.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

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
}
