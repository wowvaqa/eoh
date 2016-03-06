package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mapa;
import com.vs.eoh.Pole;
import com.vs.testing.MapEditor;

import java.io.IOException;
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
    private Stage mainStage;
    private Tables tables;

    // czy gra jest serverem
    private boolean statusServer = false;
    // czy gra jest klientem
    private boolean statusClient = false;

    // ilość graczy podłączonych do serwera.
    private int amountOfPlayers;
    // arraylist z nazwami graczy
    private ArrayList<String> palyersNames = new ArrayList<String>();
    // nazwa gracza.
    private String playerName;

    private Server server = new Server();
    private Client client = new Client();

    private Kryo kryoServer;
    private Kryo kryoClient;

    public Mapa mapa = new Mapa();

    private NetStatus mainNetStatus = new NetStatus();

    public MultiplayerScreen(Game g, Assets a, final GameStatus gs) {
        this.a = a;
        this.g = g;
        this.gs = gs;
        Assets.netStatus = this.mainNetStatus;

        mainStage = new Stage();

        tables = new Tables();

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof SomeRequest) {
                    SomeRequest request = (SomeRequest) object;
                    Gdx.app.log("Request text", request.text);

                    SomeResponse response = new SomeResponse();
                    response.text = "dzieki";
                    connection.sendTCP(response);
                } else if (object instanceof NetStatus) {
                    NetStatus netStatus = (NetStatus) object;
                    Gdx.app.log("wykryto NETSTATUS", "");
                    Gdx.app.log("Pozycja bohatera 1 na kliencie", Integer.toString(netStatus.pozXboh1)
                            + " " + Integer.toString(netStatus.pozYboh1));
                    Gdx.app.log("Pozycja bohatera 2 na kliencie", Integer.toString(netStatus.pozXboh2)
                            + " " + Integer.toString(netStatus.pozYboh2));
                    mainNetStatus.pozXboh1 = netStatus.pozXboh1;
                    mainNetStatus.pozYboh1 = netStatus.pozYboh1;
                    mainNetStatus.pozXboh2 = netStatus.pozXboh2;
                    mainNetStatus.pozYboh2 = netStatus.pozYboh2;

                    Bohater bohater = gs.getGracze().get(0).getBohaterowie().get(0);

                    int ruchX = mainNetStatus.pozXboh1 - bohater.getPozXnaMapie();
                    int ruchY = mainNetStatus.pozYboh1 - bohater.getPozYnaMapie();

                    bohater.addAction(Actions.moveBy(ruchX * 100, ruchY * 100, 0.25f));
                    bohater.setPozXnaMapie(bohater.getPozXnaMapie() + (int) ruchX);
                    bohater.setPozYnaMapie(bohater.getPozYnaMapie() + (int) ruchY);
                }
            }
        });

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof SomeResponse) {
                    SomeResponse response = (SomeResponse) object;
                    Gdx.app.log("Server text", response.text);
                } else if (object instanceof NetStatus) {
                    Gdx.app.log("wykryto NETSTATUS", "");
                    NetStatus netStatus = (NetStatus) object;
                    Gdx.app.log("wykryto NETSTATUS", "");
                    Gdx.app.log("Pozycja bohatera 1 na kliencie", Integer.toString(netStatus.pozXboh1)
                            + " " + Integer.toString(netStatus.pozYboh1));
                    Gdx.app.log("Pozycja bohatera 2 na kliencie", Integer.toString(netStatus.pozXboh2)
                            + " " + Integer.toString(netStatus.pozYboh2));
                    mainNetStatus.pozXboh1 = netStatus.pozXboh1;
                    mainNetStatus.pozYboh1 = netStatus.pozYboh1;
                    mainNetStatus.pozXboh2 = netStatus.pozXboh2;
                    mainNetStatus.pozYboh2 = netStatus.pozYboh2;
                }
            }
        });

        kryoServer = server.getKryo();
        kryoServer.register(SomeRequest.class);
        kryoServer.register(SomeResponse.class);
        kryoServer.register(NetStatus.class);
        kryoClient = client.getKryo();
        kryoClient.register(SomeRequest.class);
        kryoClient.register(SomeResponse.class);
        kryoClient.register(NetStatus.class);
        kryoServer.register(Mapa.class);
        kryoClient.register(Mapa.class);
        kryoServer.register(Pole.class);
        kryoClient.register(Pole.class);
    }

    private void formatujTabele() {
        formatTable01();
        formatTable02();

        tables.tableMain.clear();

        tables.tableMain.add(tables.table01).align(Align.left);
        tables.tableMain.add(tables.table02).expand();

        mainStage.addActor(tables.tableMain);
    }

    private void formatTable01() {
        tables.table01.clear();

        tables.table01.add(getLblGameStuts()).align(Align.topLeft).space(5);
        tables.table01.row();
        tables.table01.add(getBtnServerStart()).size(200, 40).align(Align.topLeft).space(5);
        tables.table01.row();
        tables.table01.add(getBtnServerStop()).size(200, 40).align(Align.topLeft).space(5);
        tables.table01.row();
        tables.table01.add(getBtnClientStart()).size(200, 40).align(Align.topLeft).space(5);
        tables.table01.row();
        tables.table01.add(getBtnClientStop()).size(200, 40).align(Align.topLeft).space(5);
        tables.table01.row();
        tables.table01.add(getBtnGetConnections()).size(200, 40).align(Align.topLeft).space(5);
        tables.table01.row();
        tables.table01.add(getBtnExit()).size(200, 50).align(Align.bottomLeft).expand().padTop(20);
    }

    private void formatTable02() {
        tables.table02.clear();

        if (statusServer) {
            tables.table02.add(getBtnSendMap()).size(100, 50).space(5);
            tables.table02.add(getLblGetConnections()).align(Align.topLeft).expand();
        }

        if (statusClient){
            tables.table02.add(getTxtFldPlayerName());
            tables.table02.add(getBtnClientEnd()).size(100, 50).space(5);
        }
    }

    private Label getLblGetConnections() {
        Label lblGetConnections = new Label("Aktywne polaczenia: " + server.getConnections().length, a.skin);
        return lblGetConnections;
    }

    /**
     * Zwraca labelkę ze statusem gry (klient/serwer)
     *
     * @return
     */
    private Label getLblGameStuts() {
        if (statusServer) {
            Label lblGameStatus = new Label("SERWER", a.skin);
            return lblGameStatus;
        } else if (statusClient) {
            Label lblGameStatus = new Label("KLIENT", a.skin);
            return lblGameStatus;
        }
        Label lblGameStatus = new Label("Brak statusu", a.skin);
        return lblGameStatus;
    }

    private TextField getTxtFldPlayerName(){
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
                Gdx.app.log("Button Pressed: ", "getConnections");
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
    private TextButton getBtnClientStart() {
        TextButton btnClientStart = new TextButton("Client START", a.skin);
        btnClientStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Pressed: ", "Client Start");
                client.start();
                try {
                    client.connect(5000, "192.168.1.100", 54556, 54777);
                    SomeRequest someRequest = new SomeRequest();
                    someRequest.text = "witaj serwerze";
                    client.sendTCP(someRequest);
                    Assets.client = client;
                    statusClient = true;
                    gs.setGameStatus(2);
                    formatujTabele();
                } catch (IOException ex) {
                    Gdx.app.log("Nie mogę uruchomić klienta", ex.toString());
                }
            }
        });
        statusClient = true;
        return btnClientStart;
    }

    /**
     * Zwraca przycisk Client Stop
     *
     * @return Zwraca referencje do przycisku Stopu Klienta
     */
    private TextButton getBtnClientStop() {
        TextButton btnClientStop = new TextButton("Client Stop", a.skin);
        btnClientStop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Pressed: ", "Client STOP");
                client.stop();
                statusClient = false;
                gs.setGameStatus(0);
                formatujTabele();
            }
        });
        return btnClientStop;
    }

    /**
     * Zwraca przycisk Server Stop
     *
     * @return Zwraca referencje do przycisku Stopu Serwera
     */
    private TextButton getBtnServerStop() {
        TextButton btnServerStop = new TextButton("Server Stop", a.skin);
        btnServerStop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Pressed: ", "Server STOP");
                server.stop();
                statusServer = false;
                gs.setGameStatus(0);
                formatujTabele();
            }
        });
        return btnServerStop;
    }

    /**
     * Zwraca przycisk Server Start
     *
     * @return Zwraca referencje do przycisku Startu Serwera
     */
    private TextButton getBtnServerStart() {
        TextButton btnServerStart = new TextButton("Server Start", a.skin);
        btnServerStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Pressed: ", "Server START");
                server.start();
                try {
                    server.bind(54556, 54777);
                    Assets.server = server;
                    statusServer = true;
                    gs.setGameStatus(1);
                    formatujTabele();
                    try {
                        Assets.netStatus.mapa = MapEditor.readMap(gs.nazwaMapy);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException ex) {
                    Gdx.app.log("Nie udało sie uruchomić servera", ex.toString());
                }
            }
        });
        return btnServerStart;
    }

    /**
     * Zwraca przycisk Exit
     *
     * @return Zwraca referencje do przycisku wyjścia ze Screenu
     */
    private TextButton getBtnExit() {
        TextButton btnExit = new TextButton("EXIT", a.skin);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                g.setScreen(Assets.mainMenuScreen);
            }
        });
        return btnExit;
    }

    private TextButton getBtnSendMap() {
        TextButton btnSendMap = new TextButton("Wyslij Mape", a.skin);
        btnSendMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                server.sendToAllTCP(Assets.netStatus);
            }
        });
        return btnSendMap;
    }


    /**
     * Zwraca przycisk odpowiedzialny za zakończenie klienta.
     * @return
     */
    private TextButton getBtnClientEnd(){
        TextButton btnClientEnd = new TextButton("START", a.skin);

        return btnClientEnd;
    }

    @Override
    public void show() {
        formatujTabele();
        Gdx.input.setInputProcessor(mainStage);
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
        public int pozXboh1;
        public int pozYboh1;

        public int pozXboh2;
        public int pozYboh2;

        public Mapa mapa;

        public String playerName;
    }

    public class Tables {
        public Table tableMain;
        public Table table01;
        public Table table02;

        public Tables() {
            tableMain = new Table();
            table01 = new Table();
            table02 = new Table();

            tableMain.setFillParent(true);
            tableMain.pad(5);
            tableMain.setDebug(true);

            table01.setDebug(true);
            table02.setDebug(true);
        }
    }
}
