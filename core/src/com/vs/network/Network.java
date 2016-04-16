package com.vs.network;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.vs.enums.DostepneItemki;
import com.vs.eoh.Assets;
import com.vs.eoh.Item;

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
}
