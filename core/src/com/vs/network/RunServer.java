package com.vs.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.vs.eoh.GameStatus;
import com.vs.screens.MultiplayerScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
    public RunServer(int portTCP, int portUDP, MultiplayerScreen mS) throws IOException {
        this.portTCP = portTCP;
        this.portUDP = portUDP;

        srv = new Server() {
            protected Connection newConnection() {
                return new ChatConnection();
            }
        };
        GameStatus.server = this;

        Network.register(srv);

        srv.addListener(new Listener() {
            public void received(Connection c, Object object) {
                ChatConnection connection = (ChatConnection) c;

                if (object instanceof Network.RegisterName) {
                    if (connection.name != null) {
                        Gdx.app.log("Serwer Listner", "connection.name != null");
                        return;
                    }
                    String name = ((Network.RegisterName) object).name;
                    if (name == null) {
                        Gdx.app.log("Serwer Listner", "name == null");
                        return;
                    }
                    name = name.trim();
                    if (name.length() == 0) {
                        Gdx.app.log("Serwer Listner", "name.length() == 0");
                        return;
                    }
                    connection.name = name;
                    Network.ChatMessage chatMessage = new Network.ChatMessage();
                    chatMessage.text = name + " polaczony.";
                    srv.sendToAllExceptTCP(connection.getID(), chatMessage);
                    updateNames();
                    Gdx.app.log("Serwer Listener: Network.RegisterName", name);
                    GameStatus.mS.interfce.lstChatPlayers.getItems().add(name);
                    return;
                }

                if (object instanceof Network.ChatMessage) {
                    if (connection.name == null) return;
                    Network.ChatMessage chatMessage = (Network.ChatMessage) object;
                    String message = chatMessage.text;
                    if (message == null) return;
                    if (message.length() == 0) return;
                    chatMessage.text = connection.name + ": " + message;
                    srv.sendToAllTCP(chatMessage);
                    return;
                }
            }

            public void disconnected(Connection c) {
                ChatConnection connection = (ChatConnection) c;
                if (connection.name != null) {
                    Network.ChatMessage chatMessage = new Network.ChatMessage();
                    chatMessage.text = connection.name + " rozlaczony.";
                    srv.sendToAllTCP(chatMessage);
                    updateNames();
                }
            }
        });
    }

    public void updateNames() {
        Connection[] connections = srv.getConnections();
        ArrayList names = new ArrayList(connections.length);

        for (int i = connections.length - 1; i >= 0; i--) {
            ChatConnection connection = (ChatConnection) connections[i];
            names.add(connection.name);

            Network.UpdateNames updateNames = new Network.UpdateNames();
            updateNames.names = (String[]) names.toArray(new String[names.size()]);
            srv.sendToAllTCP(updateNames);
        }
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
