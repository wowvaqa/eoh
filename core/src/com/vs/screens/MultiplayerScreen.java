package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.Assets;
import com.vs.eoh.GameStatus;
import com.vs.network.NetEngine;
import com.vs.network.RunClient;
import com.vs.network.RunServer;

import java.io.IOException;

import static com.vs.eoh.Assets.client;
import static com.vs.eoh.Assets.server;

/**
 * Created by v on 2016-03-01.
 */
public class MultiplayerScreen implements Screen {

    private final Game g;
    private final OrthographicCamera c;
    private final FitViewport viewPort;
    public Stage mainStage;
    public Tables tables;
    public Interface interfce;
    private Assets a;
    private GameStatus gs;
    private NetEngine ne;

    public MultiplayerScreen(Game g, Assets a, final GameStatus gs, NetEngine ne) {
        this.a = a;
        this.g = g;
        this.gs = gs;
        this.ne = ne;
        GameStatus.mS = this;

        interfce = new Interface();
        mainStage = new Stage();
        tables = new Tables();

        mainStage.addActor(tables.tableMain);

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);
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

    public Assets getA() {
        return a;
    }

    public GameStatus getGs() {
        return gs;
    }

    /**
     * Klasa obsługuje tabele
     */
    public class Tables {
        public Table tableMain = new Table();
        public Table tableLog = new Table();
        public Table tableChat = new Table();
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
            formatChatTable();
            formatTable01();
            formatTable02();

            tableMain.add(table01);
            if (gs.getNetworkStatus() == 1 || gs.getNetworkStatus() == 2) {
                tableMain.add(tableLog);
                tableMain.add(tableChat);
            }
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
            tableLog.add(interfce.lstLogList).size(400, 200).pad(5);
            tableLog.row();
            tableLog.add(interfce.btnDiagnoseConnection);
        }

        /**
         * Formatuje tabelę chatu.
         */
        public void formatChatTable() {
            tableChat.clear();
            tableChat.setDebug(true);

            tableChat.add(new Label("Chat", a.skin)).pad(5).colspan(2);
            tableChat.row();
            tableChat.add(interfce.lstChatPlayers).pad(5).size(200, 200);
            tableChat.add(interfce.lstChatList).pad(5).size(400, 200);
            tableChat.row();
            tableChat.add(interfce.tfChatMessage).pad(5).colspan(2);
            tableChat.row();
            tableChat.add(interfce.btnSendMessage).pad(5).colspan(2);
        }

        /**
         * Formatuję tabele 01
         */
        public void formatTable01() {
            table01.clear();
            table01.setDebug(true);

            table01.add(interfce.tfPlayerName).pad(5);
            table01.row();
            table01.add(interfce.tfIpAdress).pad(5);
            table01.row();
            table01.add(interfce.tfTcpPort).pad(5);
            table01.row();
            table01.add(interfce.tfUdpPort).pad(5);
            table01.row();
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
        public TextButton btnSendMessage = new TextButton("Send", a.skin);

        public TextButton btnDiagnoseConnection = new TextButton("DC", a.skin);

        public TextField tfIpAdress = new TextField("192.168.2.3", a.skin);
        public TextField tfTcpPort = new TextField("54556", a.skin);
        public TextField tfUdpPort = new TextField("54777", a.skin);
        public TextField tfPlayerName = new TextField("", a.skin);
        public TextField tfChatMessage = new TextField("", a.skin);

        public List lstChatList = new List(a.skin);
        public List lstChatPlayers = new List(a.skin);
        public List lstLogList = new List(a.skin);

        public TextButton btnExit = new TextButton("EXIT", a.skin);

        public Interface() {
            Listeners listeners = new Listeners();
            listeners.addListners();
        }

        /**
         * Sprawdza czy w oknie Chatu nie ma za dużo wiadomosci i usuwa nadmiar.
         */
        public void checkChatList() {
            if (lstChatList.getItems().size > 8) {
                lstChatList.getItems().removeIndex(0);
            }
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

        }

        /**
         * Klasa odpowiada za dodawanie listnerów do elementów interfejsu.
         */
        public class Listeners {
            public Listeners() {
            }

            /**
             * Dodaje wszystki listnery
             */
            public void addListners() {
                addListnerBtnSendMessage();
                addListnerBtnGetConnections();
                addListenerBtnClientStart();
                addListenerBtnClientStop();
                addListenerBtnServerStart();
                addListenerBtnServerStop();
                addListenerBtnExit();
                addListenerBtnDiagnoseConnection();
            }

            /**
             * Dodaje Listnera do przycisku Wyśli Wiadomość
             */
            private void addListnerBtnSendMessage() {
                btnSendMessage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("Button Pressed: ", "Send Message");
                        GameStatus.client.sendChatMessage();
                        tfChatMessage.setText("");
                        checkChatList();
                    }
                });
            }

            /**
             * Dodaje Listnera do przycisku Get Connection
             */
            private void addListnerBtnGetConnections() {
                btnGetConnections.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("Button Pressed: ", "Poloczenia " + GameStatus.server.getSrv().getConnections().length);
                        updateLabelConnection();
                    }
                });
            }

            /**
             * Dodaje listenera do przyciksu klient stop.
             */
            private void addListenerBtnClientStop() {
                btnClientStop.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("Button Pressed: ", "Client STOP");
                        gs.client.stopClient();
                        gs.setNetworkStatus(0);
                        tables.formatMainTable();
                        updateLabelNetworkStatus();
                    }
                });
            }

            /**
             * Dodaje Listenera do przycisku Client Start
             */
            private void addListenerBtnClientStart() {
                btnClientStart.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.log("Button Pressed: ", "Client Start");
                        String name;
                        name = interfce.tfPlayerName.getText();
                        if (name == null) {
                            name = "wowvaqa";
                        }
                        name.trim();
                        RunClient rc = new RunClient(name, "192.168.2.3", 54556, 54777, ne);
                        rc.startClient();
                        gs.setNetworkStatus(2);
                        tables.formatMainTable();
                        updateLabelNetworkStatus();
                    }
                });
            }

            /**
             * Dodaje listenera do przycisku Server Stop
             */
            private void addListenerBtnServerStop() {
                btnServerStop.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        gs.server.serverStop();
                        gs.setNetworkStatus(0);
                        updateLabelNetworkStatus();
                        tables.formatMainTable();
                    }
                });
            }

            /**
             * Dodaje Listenera do przycisku Server Start
             */
            private void addListenerBtnServerStart() {
                btnServerStart.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        try {
                            new RunServer(Integer.parseInt(tfTcpPort.getText()),
                                    Integer.parseInt(tfUdpPort.getText()), GameStatus.mS);
                            gs.setNetworkStatus(1);
                            tables.formatMainTable();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gs.server.startServer();
                        updateLabelNetworkStatus();
                    }
                });
            }

            /**
             *
             */
            private void addListenerBtnDiagnoseConnection() {
                btnServerStart.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //if (GameStatus.server.getSrv().getConnections().length > 0)
                        //lstLogList.getItems().add(GameStatus.server.getSrv().getConnections()[0].getID());
                    }
                });
            }

            /**
             * Dodaje listenera do przycisku wyjścia z Screenu MS
             */
            private void addListenerBtnExit() {
                btnExit.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        g.setScreen(Assets.mainMenuScreen);
                    }
                });
            }
        }
    }
}
