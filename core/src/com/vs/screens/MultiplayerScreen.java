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

import java.io.IOException;

import sun.nio.ch.Net;

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

    private Server server = new Server();
    private Client client = new Client();

    private Kryo kryoServer;
    private Kryo kryoClient;

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
                            + " " +Integer.toString(netStatus.pozYboh1));
                    Gdx.app.log("Pozycja bohatera 2 na kliencie", Integer.toString(netStatus.pozXboh2)
                            + " " +Integer.toString(netStatus.pozYboh2));
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
                            + " " +Integer.toString(netStatus.pozYboh1));
                    Gdx.app.log("Pozycja bohatera 2 na kliencie", Integer.toString(netStatus.pozXboh2)
                            + " " +Integer.toString(netStatus.pozYboh2));
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
    }

    private void formatujTabele() {
        tables.leftTable.clear();
        tables.righTable.clear();
        tables.mainTable.clear();

        tables.leftTable.add(getBtnServerStart()).size(200, 40).align(Align.topLeft).space(5);
        tables.leftTable.row();
        tables.leftTable.add(getBtnServerStop()).size(200, 40).align(Align.topLeft).space(5);
        tables.leftTable.row();
        tables.leftTable.add(getBtnClientStart()).size(200, 40).align(Align.topLeft).space(5);
        tables.leftTable.row();
        tables.leftTable.add(getBtnClientStop()).size(200, 40).align(Align.topLeft).space(5);
        tables.leftTable.row();
        tables.leftTable.add(getBtnGetConnections()).size(200, 40).align(Align.topLeft).space(5);
        tables.leftTable.row();
        tables.leftTable.add(getBtnExit()).size(200, 50).align(Align.bottomLeft).expand().padTop(20);

        tables.righTable.add(getLabelGetConnections()).align(Align.topLeft).expand();

        tables.mainTable.add(tables.leftTable).align(Align.left);
        tables.mainTable.add(tables.righTable).expand();

        mainStage.addActor(tables.mainTable);
    }

    private Label getLabelGetConnections() {
        Label lblGetConnections = new Label("Aktywne polaczenia: " + server.getConnections().length, a.skin);
        return lblGetConnections;
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
                } catch (IOException ex) {
                    Gdx.app.log("Nie mogę uruchomić klienta", ex.toString());
                }
            }
        });
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

    public class Tables {
        public Table mainTable;
        public Table leftTable;
        public Table righTable;

        public Tables() {
            mainTable = new Table();
            leftTable = new Table();
            righTable = new Table();

            mainTable.setFillParent(true);
            mainTable.pad(5);
            mainTable.setDebug(true);

            leftTable.setDebug(true);
            righTable.setDebug(true);
        }
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
    }
}
