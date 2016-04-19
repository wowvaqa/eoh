package com.vs.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mapa;
import com.vs.screens.MultiplayerScreen;
import com.vs.testing.MapEditor;

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

                    //Wysłanie mapy do nowo połączonego klienta

                    Mapa mapa = null;

                    try {
                        mapa = MapEditor.readMap("map01.dat");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    Network.NetworkMap networkMap = new Network.NetworkMap(
                            mapa.getIloscPolX(), mapa.getIloscPolY());

                    Mapa.convertToNetworkMap(mapa, networkMap);

                    srv.sendToTCP(connection.getID(), networkMap);

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

                if (object instanceof Network.Move) {
                    Gdx.app.log("NetworkMove", "Serwer odebrał ruch od klienta");
                    Network.Move move = (Network.Move) object;
                    Gdx.app.log("RuchX", "" + move.ruchX);
                    Gdx.app.log("RuchY", "" + move.ruchY);
                    Gdx.app.log("Indeks Gracza  ", "" + move.player);
                    Gdx.app.log("Indeks Bohatera", "" + move.hero);
                    srv.sendToAllExceptTCP(connection.getID(), move);
                    return;
                }

                if (object instanceof Network.DamageHero) {
                    Gdx.app.log("Network.DamageHero", "Serwer odebrał obrażenia dla bohatera");
                    Network.DamageHero damageHero = (Network.DamageHero) object;
                    Gdx.app.log("Indeks Gracza", "" + damageHero.player);
                    Gdx.app.log("Indeks Bohatera", "" + damageHero.hero);
                    Gdx.app.log("Obrazenia", "" + damageHero.damage);
                    srv.sendToAllExceptTCP(connection.getID(), damageHero);
                    return;
                }

                if (object instanceof Network.DamageMob) {
                    Gdx.app.log("Network.DamageMob", "Serwer odebrał obrażenia dla moba");
                    Network.DamageMob damageMob = (Network.DamageMob) object;
                    Gdx.app.log("Poz. X moba na mapie", "" + damageMob.pozXmoba);
                    Gdx.app.log("Poz. Y moba na mapie", "" + damageMob.pozYmoba);
                    Gdx.app.log("Obrazenia zadane Mobowi", "" + damageMob.damage);
                    srv.sendToAllExceptTCP(connection.getID(), damageMob);
                    return;
                }

                if (object instanceof Network.RemoveTresureBox) {
                    Network.RemoveTresureBox removeTresureBox = (Network.RemoveTresureBox) object;
                    srv.sendToAllExceptTCP(connection.getID(), removeTresureBox);
                    return;
                }

                if (object instanceof Network.AddItemEquip) {
                    Network.AddItemEquip addItemEquip = (Network.AddItemEquip) object;
                    srv.sendToAllExceptTCP(connection.getID(), addItemEquip);
                    return;
                }
                if (object instanceof Network.EndOfTurn) {
                    Network.EndOfTurn endOfTurn = (Network.EndOfTurn) object;
                    srv.sendToAllExceptTCP(connection.getID(), endOfTurn);
                    return;
                }
                if (object instanceof Network.StartMultiGame) {
                    Network.StartMultiGame startMultiGame = (Network.StartMultiGame) object;
                    srv.sendToAllExceptTCP(connection.getID(), startMultiGame);
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
