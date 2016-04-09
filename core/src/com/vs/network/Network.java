package com.vs.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by v on 2016-04-09.
 */
public class Network {

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
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

}
