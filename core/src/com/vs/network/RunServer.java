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

import sun.nio.ch.Net;

/**
 * Created by v on 2016-03-09.
 */
public class RunServer {

    ArrayList chatPlayers = new ArrayList();
    ArrayList getChatPlayersId = new ArrayList();
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
                    GameStatus.mS.interfce.lstLogList.getItems().add(connection.getID());
                    GameStatus.mS.interfce.lstLogList.getItems().add(connection.getEndPoint().toString());
                    updateNames();
                    Gdx.app.log("Serwer Listener: Network.RegisterName", name);
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

                if (object instanceof Network.ChatMessagePrivate) {
                    Gdx.app.log("Network.ChatMessagePrivate", "Serwer odbrał prywatną wiadomość");
                    if (connection.name == null) return;
                    Network.ChatMessagePrivate chatMessagePrivate = (Network.ChatMessagePrivate) object;
                    String message = chatMessagePrivate.text;
                    String name = chatMessagePrivate.name;

                    int indeksKlienta = -99;

                    for (int i = 0; i < chatPlayers.size(); i++) {
                        if (name.equals(chatPlayers.get(i))) {
                            indeksKlienta = i;
                        }
                    }

                    if (indeksKlienta != -99) {
                        Network.ChatMessage chatMessage = new Network.ChatMessage();
                        chatMessage.text = "Priv: " + chatMessagePrivate.text;
                        srv.sendToTCP((Integer) getChatPlayersId.get(indeksKlienta), chatMessage);
                    }
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

        GameStatus.mS.interfce.lstChatPlayers.clearItems();
        getChatPlayersId.clear();

        for (int i = connections.length - 1; i >= 0; i--) {
            ChatConnection connection = (ChatConnection) connections[i];
            names.add(connection.name);

            GameStatus.mS.interfce.lstChatPlayers.getItems().add(connection.name);

            getChatPlayersId.add(connections[i].getID());

            Network.UpdateNames updateNames = new Network.UpdateNames();
            updateNames.names = (String[]) names.toArray(new String[names.size()]);
            srv.sendToAllTCP(updateNames);
        }

        chatPlayers = names;

        for (int i = 0; i < chatPlayers.size(); i++) {
            Gdx.app.log("Gracz", "" + chatPlayers.get(i));
            Gdx.app.log("ID", "" + getChatPlayersId.get(i));
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
