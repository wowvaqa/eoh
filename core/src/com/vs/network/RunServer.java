package com.vs.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.vs.eoh.GameStatus;

import java.io.IOException;

/**
 * Created by v on 2016-03-09.
 */
public class RunServer {

    private Server srv;
    private int portTCP;
    private int portUDP;

    /**
     *
     * @param portTCP Numer portu TCP
     * @param portUDP Numer portu UDP
     */
    public RunServer(int portTCP, int portUDP) throws IOException {
        this.portTCP = portTCP;
        this.portUDP = portUDP;

        srv = new Server() {
            protected Connection newConnection() {
                return new ChatConnection();
            }
        };
        GameStatus.server = this;

        Network.register(srv);
    }

    /**
     * Metoda uruchamia Server
     */
    public void startServer(){

        srv.start();
        try {
            srv.bind(portTCP, portUDP);
            Gdx.app.log("Serwer uruchomiony", "Sukces");
        } catch (IOException e) {
            Gdx.app.log("Nie mogę uruchomić serwera. KOD", "" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Metoda zatrzymuje serwer
     */
    public void serverStop(){
        srv.stop();
    }

    /**
     * Zwraca referencję do obiektu serwera
     * @return Server
     */
    public Server getSrv() {
        return srv;
    }

    /**
     * Ustala serwer
     * @param srv Referencja do obiektu Server
     */
    public void setSrv(Server srv) {
        this.srv = srv;
    }

    public static class ChatConnection extends Connection {
        public String name;
    }

}
