package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.Castle;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mapa;
import com.vs.eoh.Pole;
import com.vs.eoh.TresureBox;
import com.vs.network.RunClient;
import com.vs.network.RunServer;
import com.vs.testing.MapEditor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static com.vs.eoh.Assets.*;

/**
 * Created by v on 2016-03-01.
 */
public class MultiplayerScreen implements Screen {

    private final Assets a;
    private final Game g;
    private final GameStatus gs;
    private final OrthographicCamera c;
    private final FitViewport viewPort;
    public Mapa mapa = new Mapa();
    private Stage mainStage;
    private Tables tables;
    private Interface interfce;
    // ilość graczy podłączonych do serwera.
    private int amountOfPlayers;
    // arraylist z nazwami graczy
    private ArrayList<String> palyersNames = new ArrayList<String>();
    // nazwa gracza.
    private String playerName;
    //private Server server = new Server();
    //private Client client = new Client();
    private Kryo kryoServer;
    private Kryo kryoClient;
    private NetStatus mainNetStatus = new NetStatus();

    public MultiplayerScreen(Game g, Assets a, final GameStatus gs) {
        this.a = a;
        this.g = g;
        this.gs = gs;
        Assets.netStatus = this.mainNetStatus;

        interfce = new Interface();
        mainStage = new Stage();
        tables = new Tables();

        mainStage.addActor(tables.tableMain);

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);

//        server.addListener(new Listener() {
//            @Override
//            public void received(Connection connection, Object object) {
//                if (object instanceof SomeRequest) {
//                    SomeRequest request = (SomeRequest) object;
//                    Gdx.app.log("Request text", request.text);
//
//                    SomeResponse response = new SomeResponse();
//                    response.text = "dzieki";
//                    connection.sendTCP(response);
//                } else if (object instanceof NetStatus) {
//                    NetStatus netStatus = (NetStatus) object;
//                    Gdx.app.log("wykryto NETSTATUS", "");
//                    Gdx.app.log("Pozycja bohatera 1 na kliencie", Integer.toString(netStatus.pozXboh1)
//                            + " " + Integer.toString(netStatus.pozYboh1));
//                    Gdx.app.log("Pozycja bohatera 2 na kliencie", Integer.toString(netStatus.pozXboh2)
//                            + " " + Integer.toString(netStatus.pozYboh2));
//                    mainNetStatus.pozXboh1 = netStatus.pozXboh1;
//                    mainNetStatus.pozYboh1 = netStatus.pozYboh1;
//                    mainNetStatus.pozXboh2 = netStatus.pozXboh2;
//                    mainNetStatus.pozYboh2 = netStatus.pozYboh2;
//
//                    Bohater bohater = gs.getGracze().get(0).getBohaterowie().get(0);
//
//                    int ruchX = mainNetStatus.pozXboh1 - bohater.getPozXnaMapie();
//                    int ruchY = mainNetStatus.pozYboh1 - bohater.getPozYnaMapie();
//
//                    bohater.addAction(Actions.moveBy(ruchX * 100, ruchY * 100, 0.25f));
//                    bohater.setPozXnaMapie(bohater.getPozXnaMapie() + (int) ruchX);
//                    bohater.setPozYnaMapie(bohater.getPozYnaMapie() + (int) ruchY);
//
//                    if (netStatus.cntReciveMap){
//                        Gdx.app.log("Klient otrzymal mape", "");
//                        gs.setMapa(netStatus.mapa);
//                        netStatus.srvSendMap = false;
//                        netStatus.cntReciveMap = false;
//                    }
//                }
//            }
//        });

//        client.addListener(new Listener() {
//            @Override
//            public void received(Connection connection, Object object) {
//                if (object instanceof SomeResponse) {
//                    SomeResponse response = (SomeResponse) object;
//                    Gdx.app.log("Server text", response.text);
//                } else if (object instanceof NetStatus) {
//                    Gdx.app.log("wykryto NETSTATUS", "");
//                    NetStatus netStatus = (NetStatus) object;
//                    Gdx.app.log("wykryto NETSTATUS", "");
//                    Gdx.app.log("Pozycja bohatera 1 na kliencie", Integer.toString(netStatus.pozXboh1)
//                            + " " + Integer.toString(netStatus.pozYboh1));
//                    Gdx.app.log("Pozycja bohatera 2 na kliencie", Integer.toString(netStatus.pozXboh2)
//                            + " " + Integer.toString(netStatus.pozYboh2));
//                    mainNetStatus.pozXboh1 = netStatus.pozXboh1;
//                    mainNetStatus.pozYboh1 = netStatus.pozYboh1;
//                    mainNetStatus.pozXboh2 = netStatus.pozXboh2;
//                    mainNetStatus.pozYboh2 = netStatus.pozYboh2;
//
//                    if (netStatus.srvSendMap){
//                        Gdx.app.log("SERWER WYSYLAL MAPE","");
//                        Gdx.app.log("Ilosc pol X", " " + netStatus.networkMap.amountX);
//                        Gdx.app.log("Ilosc pol Y", " " + netStatus.networkMap.amountY);
//                        netStatus.cntReciveMap = true;
//
//                        gs.setMapa(netStatus.mapa);
//                        GameStatus.nazwaMapy = "mapa z serwera";
//                        netStatus.srvSendMap = false;
//                        netStatus.cntReciveMap = false;
//
//                        client.sendTCP(netStatus);
//                    }
//                }
//            }
//        });

//        kryoServer = server.getKryo();
//        kryoClient = client.getKryo();

//        kryoServer.register(SomeRequest.class);
//        kryoClient.register(SomeRequest.class);
//
//        kryoServer.register(SomeResponse.class);
//        kryoClient.register(SomeResponse.class);

//        kryoServer.register(NetworkMap.class);
//        kryoClient.register(NetworkMap.class);
//
//        kryoServer.register(NetworkPole.class);
//        kryoClient.register(NetworkPole.class);
//
//        kryoServer.register(NetworkPole[].class);
//        kryoClient.register(NetworkPole[].class);
//
//        kryoServer.register(NetworkPole[][].class);
//        kryoClient.register(NetworkPole[][].class);
//
//        kryoServer.register(NetStatus.class);
//        kryoClient.register(NetStatus.class);
//
//        kryoServer.register(Mapa.class);
//        kryoClient.register(Mapa.class);
//
//        kryoServer.register(Pole[][].class);
//        kryoClient.register(Pole[][].class);
//
//        kryoServer.register(Pole[].class);
//        kryoClient.register(Pole[].class);
//
//        kryoServer.register(Pole.class);
//        kryoClient.register(Pole.class);
//
//        kryoServer.register(TypyTerenu.class);
//        kryoClient.register(TypyTerenu.class);
//
//        kryoServer.register(Castle.class);
//        kryoClient.register(Castle.class);
//
//        kryoServer.register(Bohater.class);
//        kryoClient.register(Bohater.class);
//
//        kryoServer.register(TresureBox.class);
//        kryoClient.register(TresureBox.class);
    }

    private void formatujTabele() {
//        formatTable01();
        formatTable02();

        tables.tableMain.clear();

        tables.tableMain.add(tables.table01).align(Align.left);
        tables.tableMain.add(tables.table02).expand();

        mainStage.addActor(tables.tableMain);
    }

//    private void formatTable01() {
//        tables.table01.clear();
//
////        tables.table01.add(getLblGameStuts()).align(Align.topLeft).space(5);
////        tables.table01.row();
////        tables.table01.add(getBtnServerStart()).size(200, 40).align(Align.topLeft).space(5);
////        tables.table01.row();
////        tables.table01.add(getBtnServerStop()).size(200, 40).align(Align.topLeft).space(5);
////        tables.table01.row();
//        //tables.table01.add(getBtnClientStart()).size(200, 40).align(Align.topLeft).space(5);
////        tables.table01.row();
////        tables.table01.add(getBtnClientStop()).size(200, 40).align(Align.topLeft).space(5);
////        tables.table01.row();
////        tables.table01.add(getBtnGetConnections()).size(200, 40).align(Align.topLeft).space(5);
////        tables.table01.row();
////        tables.table01.add(getBtnExit()).size(200, 50).align(Align.bottomLeft).expand().padTop(20);
//    }

    private void formatTable02() {
        tables.table02.clear();

        Gdx.app.debug("Network Status", "" + gs.getNetworkStatus());

        if (gs.getNetworkStatus() == 1) {
            Gdx.app.debug("Network Status", "" + gs.getNetworkStatus());
            tables.table02.add(getBtnSendMap()).size(100, 50).space(5);
            tables.table02.add(getLblGetConnections()).align(Align.topLeft).expand();
        }

        if (gs.getNetworkStatus() == 2) {
            Gdx.app.debug("Network Status", "" + gs.getNetworkStatus());
            tables.table02.add(getTxtFldPlayerName());
            tables.table02.add(getBtnClientEnd()).size(100, 50).space(5);
        }

        tables.table02.add(getBtnWybierzMape()).size(100,50).space(5);
    }

    private Label getLblGetConnections() {
        Label lblGetConnections = new Label("Aktywne polaczenia: " + gs.server.getSrv().getConnections(), a.skin);
        return lblGetConnections;
    }

    /**
     * Zwraca labelkę ze statusem gry (klient/serwer)
     *
     * @return
     */
//    private Label getLblGameStuts() {
//        if (gs.getNetworkStatus() == 1) {
//            Label lblGameStatus = new Label("SERWER", a.skin);
//            return lblGameStatus;
//        } else if (gs.getNetworkStatus() == 2) {
//            Label lblGameStatus = new Label("KLIENT", a.skin);
//            return lblGameStatus;
//        }
//        Label lblGameStatus = new Label("Stand Alone", a.skin);
//        return lblGameStatus;
//    }

    private TextField getTxtFldPlayerName() {
        TextField txtFldPlayerName = new TextField("Nazwa Gracza: ", a.skin);
        return txtFldPlayerName;
    }

    /**
     * Zwraca przycisk GetConnections
     *
     * @return Zwraca referencje do przycisku GetConnections
     */
    private TextButton getBtnGetConnections() {
        TextButton btnClientStart = new TextButton("Get Connections", a.skin);
        btnClientStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Pressed: ", "Get Connections");
                formatujTabele();
            }
        });
        return btnClientStart;
    }

    /**
     * Zwraca przycisk Client Start
     *
     * @return Zwraca referencje do przycisku Startu Klienta
     */
//    private TextButton getBtnClientStart() {
//        TextButton btnClientStart = new TextButton("Client START", a.skin);
//        btnClientStart.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Gdx.app.log("Button Pressed: ", "Client Start");
//                RunClient rc = new RunClient("wowvaqa", "192.168.2.3", 54556, 54777);
//                rc.startClient();
//                gs.setNetworkStatus(2);
//                formatujTabele();
////                client.start();
////                try {
////                    client.connect(5000, "192.168.1.100", 54556, 54777);
////                    //SomeRequest someRequest = new SomeRequest();
////                    //someRequest.text = "witaj serwerze";
////                    //client.sendTCP(someRequest);
////                    Assets.client = client;
////                    statusClient = true;
////                    gs.setGameStatus(2);
////                    formatujTabele();
////                } catch (IOException ex) {
////                    Gdx.app.log("Nie mogę uruchomić klienta", ex.toString());
////                }
//            }
//        });
//        return btnClientStart;
//    }

    /**
     * Zwraca przycisk Client Stop
     *
     * @return Zwraca referencje do przycisku Stopu Klienta
     */
//    private TextButton getBtnClientStop() {
//        TextButton btnClientStop = new TextButton("Client Stop", a.skin);
//        btnClientStop.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Gdx.app.log("Button Pressed: ", "Client STOP");
//                client.stop();
//                gs.client.stopClient();
//                gs.setNetworkStatus(0);
//                formatujTabele();
//            }
//        });
//        return btnClientStop;
//    }

    /**
     * Zwraca przycisk Server Stop
     *
     * @return Zwraca referencje do przycisku Stopu Serwera
     */
//    private TextButton getBtnServerStop() {
//        TextButton btnServerStop = new TextButton("Server Stop", a.skin);
//        btnServerStop.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Gdx.app.log("Button Pressed: ", "Server STOP");
//                gs.server.serverStop();
//                gs.setNetworkStatus(0);
//                formatujTabele();
//            }
//        });
//        return btnServerStop;
//    }

    /**
     * Zwraca przycisk Server Start
     *
     * @return Zwraca referencje do przycisku Startu Serwera
     */
//    private TextButton getBtnServerStart() {
//        TextButton btnServerStart = new TextButton("Server Start", a.skin);
//        btnServerStart.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Gdx.app.log("Button Pressed: ", "Server START");
//                RunServer rs = new RunServer(54556, 54777);
//                gs.server.startServer();
//                gs.setNetworkStatus(1);
//                formatujTabele();
////                server.start();
////                try {
////                    server.bind(54556, 54777);
////                    Assets.server = server;
////                    statusServer = true;
////                    gs.setGameStatus(1);
////                    formatujTabele();
////                } catch (IOException ex) {
////                    Gdx.app.log("Nie udało sie uruchomić servera", ex.toString());
////                }
//            }
//        });
//        return btnServerStart;
//    }

    /**
     * Zwraca przycisk Exit
     *
     * @return Zwraca referencje do przycisku wyjścia ze Screenu
     */
//    private TextButton getBtnExit() {
//        TextButton btnExit = new TextButton("EXIT", a.skin);
//        btnExit.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                g.setScreen(Assets.mainMenuScreen);
//            }
//        });
//        return btnExit;
//    }

    private TextButton getBtnSendMap() {
        TextButton btnSendMap = new TextButton("Wyslij Mape", a.skin);
        btnSendMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Assets.netStatus.srvSendMap = true;

                Mapa mapaToSend;

                try {
                    mapaToSend = MapEditor.readMap(gs.nazwaMapy);
                    Assets.netStatus.mapa = mapaToSend;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Assets.netStatus.networkMap = new NetworkMap(mapa.getIloscPolX(), mapa.getIloscPolY());

                server.sendToAllTCP(Assets.netStatus);
            }
        });
        return btnSendMap;
    }

    /**
     * Zwraca przycisk wyjscia z okna
     *
     * @return Przycisk
     */
    private TextButton getBtnWybierzMape() {
        TextButton btnWybierzMape = new TextButton("Wybierz Mape", a.skin);
        btnWybierzMape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainStage.addActor(getLoadMapWindow());
            }
        });
        return btnWybierzMape;
    }

    /**
     * Zwraca Okno wyboru mapy
     *
     * @return Window
     */
    private Window getLoadMapWindow() {
        final Window window = new Window("Wybierz Mape", a.skin);
        window.setSize(800, 600);
        window.align(Align.center);

        final List listOfMap = new List(a.skin);

        FileHandle[] files = Gdx.files.local("").list();
        for (FileHandle file : files) {
            if (file.extension().equals("dat")) {
                listOfMap.getItems().add(file);
            }
        }

        TextButton btnExitWindow = new TextButton("EXIT", a.skin);
        btnExitWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
            }
        });

        TextButton btnWybierzWindow = new TextButton("Wybieram", a.skin);
        btnWybierzWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileHandle file = (FileHandle) listOfMap.getSelected();
                Gdx.app.log("Nazwa Pliku", file.name());
                GameStatus.nazwaMapy = file.name();
                window.remove();
            }
        });


        window.add(listOfMap).size(300, 400);
        window.row();
        window.add(btnExitWindow).size(100, 50).spaceRight(5);
        window.add(btnWybierzWindow).size(100, 50);

        return window;
    }


    /**
     * Zwraca przycisk odpowiedzialny za zakończenie klienta.
     *
     * @return
     */
    private TextButton getBtnClientEnd() {
        TextButton btnClientEnd = new TextButton("START", a.skin);

        return btnClientEnd;
    }

    @Override
    public void show() {
        //formatujTabele();
        Gdx.input.setInputProcessor(mainStage);
        tables.formatMainTable();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.act();
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
        viewPort.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        server.close();
        client.close();
    }

    public Kryo getKryoClient() {
        return kryoClient;
    }

    public void setKryoClient(Kryo kryoClient) {
        this.kryoClient = kryoClient;
    }

    public Kryo getKryoServer() {
        return kryoServer;
    }

    public void setKryoServer(Kryo kryoServer) {
        this.kryoServer = kryoServer;
    }

    public static class SomeRequest {
        public String text;
    }

    public static class SomeResponse {
        public String text;
        NetStatus netStatus = new NetStatus();
    }

    public static class NetStatus {

        /*
        FLAGI odczytu i odbioru
         */
        public boolean srvSendMap = false;
        public boolean cntReciveMap = false;

        public int pozXboh1;
        public int pozYboh1;

        public int pozXboh2;
        public int pozYboh2;

        public NetworkMap networkMap;

        public Mapa mapa;
    }

    /**
     * Klasa definiuje Mape do przesyłu przez sieć.
     */
    public static class NetworkMap {
        public int amountX;
        public int amountY;
        public String nazwa = "NoName";
        public NetworkPole networkPole[][];

        public NetworkMap(int amountOfX, int amountOfY) {
            networkPole = new NetworkPole[amountOfX][amountOfY];
            this.amountX = amountOfX;
            this.amountY = amountOfY;
        }

        public NetworkMap(){
            networkPole = new NetworkPole[0][0];
        }

        public void convertMap(){

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

    /**
     * Klasa obsługuje tabele
     */
    public class Tables {
        public Table tableMain = new Table();
        public Table tableLog = new Table();
        public Table table01 = new Table();
        public Table table02 = new Table();

        public Tables() {
            table01.setDebug(true);
            table02.setDebug(true);
        }

        /**
         * Formatuje główną tabelę
         */
        public void formatMainTable() {
            tableMain.clear();
            tableMain.setFillParent(true);
            tableMain.setDebug(true);

            formatLogTable();
            formatTable01();
            formatTable02();

            tableMain.add(table01);
            tableMain.add(tableLog);
            tableMain.row();
            tableMain.add(interfce.btnExit).size(100, 50).pad(5);
        }

        /**
         * Formatuje tabelę logów.
         */
        public void formatLogTable() {
            tableLog.clear();
            tableLog.setDebug(true);

            tableLog.add(interfce.lblnetworkStatus);
            tableLog.row();
            tableLog.add(interfce.lblServerConnections).pad(5);
            tableLog.row();
            tableLog.add(interfce.lblLog01).pad(5);
            tableLog.row();
            tableLog.add(interfce.lblLog02).pad(5);
            tableLog.row();
            tableLog.add(interfce.lblLog03).pad(5);
        }

        /**
         * Formatuję tabele 01
         */
        public void formatTable01() {
            table01.clear();
            table01.setDebug(true);

            table01.add(interfce.btnServerStart).size(100, 25).pad(5);
            table01.row();
            table01.add(interfce.btnServerStop).size(100, 25).pad(5);
            table01.row();
            table01.add(interfce.btnClientStart).size(100, 25).pad(5);
            table01.row();
            table01.add(interfce.btnClientStop).size(100, 25).pad(5);
            table01.row();
            table01.add(interfce.btnGetConnections).size(100, 25).pad(5);
        }

        /**
         * Formatuje tabelę 02
         */
        public void formatTable02() {

        }
    }

    /**
     * Klasa odpowiada za obsługę elementów interfejsu tj. przycisków, etykiet itp.
     */
    public class Interface {

        // Etykieta wyświetla ilość połączeń do serwera.
        public Label lblServerConnections = new Label("Ilosc polaczen: 0", a.skin);

        // Etykieta wyświetla status gry (STAND ALONE, SERWER, KLIENT)
        public Label lblnetworkStatus = new Label("STAND ALONE", a.skin);

        public TextButton btnServerStart = new TextButton("Server START", a.skin);
        public TextButton btnServerStop = new TextButton("Server STOP", a.skin);
        public TextButton btnClientStart = new TextButton("Client START", a.skin);
        public TextButton btnClientStop = new TextButton("Client STOP", a.skin);

        public TextButton btnGetConnections = new TextButton("Polaczenia", a.skin);

        public TextButton btnExit = new TextButton("EXIT", a.skin);

        public Label lblLog01 = new Label("log 1", a.skin);
        public Label lblLog02 = new Label("log 2", a.skin);
        public Label lblLog03 = new Label("log 3", a.skin);

        public Interface() {
            addListeners();
        }

        /**
         * Uaktalnia etykietę wyświetlającą ilość połączeń do serwera.
         */
        public void updateLabelConnection() {
            lblServerConnections.setText("Ilosc polaczen: " + gs.server.getSrv().getConnections().length);
        }

        /**
         * Uaktualnia etykietę statusu sieciowego gry.
         */
        public void updateLabelNetworkStatus() {
            if (gs.getNetworkStatus() == 0) {
                lblnetworkStatus.setText("STAND ALONE");
            } else if (gs.getNetworkStatus() == 1) {
                lblnetworkStatus.setText("SERWER");
            } else if (gs.getNetworkStatus() == 2) {
                lblnetworkStatus.setText("KLIENT");
            }
        }

        /**
         * Uaktualnia etykiety logów w tabeli logów.
         *
         * @param log String z treścią loga.
         */
        public void updateLogs(String log) {
            lblLog03.setText(lblLog02.getText());
            lblLog02.setText(lblLog01.getText());
            lblLog01.setText(log);
        }

        /**
         * Dodaje odpowiednie listnery do przycisków
         */
        public void addListeners() {
            btnExit.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    g.setScreen(Assets.mainMenuScreen);
                }
            });

            btnServerStart.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    updateLogs("Uruchamianie serwera.");

                    try {
                        new RunServer(54556, 54777);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gs.server.startServer();
                    gs.setNetworkStatus(1);
                    updateLabelNetworkStatus();
                }
            });

            btnServerStop.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    updateLogs("Zatrzymanie serwera.");
                    gs.server.serverStop();
                    gs.setNetworkStatus(0);
                    updateLabelNetworkStatus();
                }
            });

            btnClientStart.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Button Pressed: ", "Client Start");
                    RunClient rc = new RunClient("wowvaqa", "192.168.2.3", 54556, 54777);
                    rc.startClient();
                    gs.setNetworkStatus(2);
                    updateLabelNetworkStatus();
                }
            });

            btnClientStop.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Button Pressed: ", "Client STOP");
                    gs.client.stopClient();
                    gs.setNetworkStatus(0);
                    updateLabelNetworkStatus();
                }
            });

            btnGetConnections.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Button Pressed: ", "Poloczenia");
                    updateLabelConnection();
                    if (gs.server.getSrv().getConnections().length > 0) {
                    }
                }
            });
        }
    }
}
