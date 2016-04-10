package com.vs.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.vs.eoh.GameStatus;

import java.io.IOException;

/**
 * Created by v on 2016-03-09.
 */
public class RunClient {

    private final int portTCP;
    private final int portUDP;
    private Client cnt;
    private String name;
    private String adresIP;


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

        Network.register(cnt);

        cnt.addListener(new Listener() {
            public void connected(Connection connection) {
                Network.RegisterName registerName = new Network.RegisterName();
                registerName.name = name;
                cnt.sendTCP(registerName);
            }

            public void received(Connection connection, Object object) {
                if (object instanceof Network.ChatMessage) {
                    Network.ChatMessage chatMessage = (Network.ChatMessage) object;
                    Gdx.app.log("Client Listener", "Chat Message: " + chatMessage.text);
                    GameStatus.mS.interfce.lstChatList.getItems().add(chatMessage.text);
                    GameStatus.mS.interfce.checkChatList();
                    return;
                }

                if (object instanceof Network.UpdateNames) {
                    Network.UpdateNames updateNames = (Network.UpdateNames) object;
                    Gdx.app.log("Client Listener", "Update Names: ");
                    GameStatus.mS.interfce.lstChatPlayers.clearItems();
                    for (String name : updateNames.names) {
                        Gdx.app.log("Ilosc nazw: ", "" + updateNames.names.length);
                        GameStatus.mS.interfce.lstChatPlayers.getItems().add(name);
                    }
                    return;
                }
            }
        });
    }

    /**
     * Wysyła wiadomość do wszystkich klientów.
     */
    public void sendChatMessage() {
        Network.ChatMessage chatMessage = new Network.ChatMessage();
        chatMessage.text = GameStatus.mS.interfce.tfChatMessage.getText();
        cnt.sendTCP(chatMessage);
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
