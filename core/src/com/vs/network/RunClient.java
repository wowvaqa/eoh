package com.vs.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.vs.eoh.GameStatus;

import java.io.IOException;

/**
 * Created by v on 2016-03-09.
 */
public class RunClient {

    private Client cnt;
    private String name;
    private String adresIP;
    private final int portTCP;
    private final int portUDP;


    /**
     * @param name Unikalna nazwa
     * @param adresIP Adres IP serwera.
     * @param portTCP Port TCP serwera
     * @param portUDP Port UTP serwerea
     */
    public RunClient(String name, String adresIP, int portTCP, int portUDP) {
        this.name = name;
        this.adresIP = adresIP;
        this.portTCP = portTCP;
        this.portUDP = portUDP;

        cnt = new Client();
        GameStatus.client = this;
    }

    /**
     * Metoda uruchamia klienta.
     */
    public void startClient() {
        cnt.start();

        new Thread() {
            public void run() {
                try {
                    cnt.connect(5000, adresIP, portTCP, portUDP);
                    Gdx.app.log("Klient uruchomiony", "Sukces");
                } catch (IOException e) {
                    Gdx.app.log("Błąd podczas połączenia z Serwerem", "" + e.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stopClient(){
        cnt.stop();
    }

    /**
     * Zwraca referencje do obiektu Klienta
     *
     * @return Client
     */
    public Client getCnt() {
        return cnt;
    }

    /**
     * Ustala klienta
     *
     * @param cnt obiekt klasy Client
     */
    public void setCnt(Client cnt) {
        this.cnt = cnt;
    }
}
