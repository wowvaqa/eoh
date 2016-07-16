package com.vs.screens;

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
import com.vs.eoh.V;
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

    private final OrthographicCamera c;
    private final FitViewport viewPort;
    public Stage mainStage;
    public Tables tables;
    public Interface interfce;
    private V v;
    private NetEngine ne;

    public MultiplayerScreen(V v, NetEngine ne) {
        this.v = v;
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
        return v.getA();
    }

    public GameStatus getGs() {
        return v.getGs();
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
            if (v.getGs().getNetworkStatus() == 1 || v.getGs().getNetworkStatus() == 2) {
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

            tableChat.add(new Label("Chat", v.getA().skin)).pad(5).colspan(2);
            tableChat.row();
            tableChat.add(interfce.lstChatPlayers).pad(5).size(100, 200);
            tableChat.add(interfce.lstChatList).pad(5).size(200, 200);
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
        public Label lblServerConnections = new Label("Ilosc polaczen: 0", v.getA().skin);

        // Etykieta wyświetla status gry (STAND ALONE, SERWER, KLIENT)
        public Label lblnetworkStatus = new Label("STAND ALONE", v.getA().skin);

        public TextButton btnServerStart = new TextButton("Server START", v.getA().skin);
        public TextButton btnServerStop = new TextButton("Server STOP", v.getA().skin);
        public TextButton btnClientStart = new TextButton("Client START", v.getA().skin);
        public TextButton btnClientStop = new TextButton("Client STOP", v.getA().skin);
        public TextButton btnGetConnections = new TextButton("Polaczenia", v.getA().skin);
        public TextButton btnSendMessage = new TextButton("Send", v.getA().skin);

        public TextButton btnDiagnoseConnection = new TextButton("DC", v.getA().skin);

        public TextField tfIpAdress = new TextField("192.168.2.3", v.getA().skin);
        public TextField tfTcpPort = new TextField("54556", v.getA().skin);
        public TextField tfUdpPort = new TextField("54777", v.getA().skin);
        public TextField tfPlayerName = new TextField("", v.getA().skin);
        public TextField tfChatMessage = new TextField("", v.getA().skin);

        public List lstChatList = new List(v.getA().skin);
        public List lstChatPlayers = new List(v.getA().skin);
        public List lstLogList = new List(v.getA().skin);

        public TextButton btnExit = new TextButton("EXIT", v.getA().skin);

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
            lblServerConnections.setText("Ilosc polaczen: " + v.getGs().server.getSrv().getConnections().length);
        }

        /**
         * Uaktualnia etykietę statusu sieciowego gry.
         */
        public void updateLabelNetworkStatus() {
            if (v.getGs().getNetworkStatus() == 0) {
                lblnetworkStatus.setText("STAND ALONE");
            } else if (v.getGs().getNetworkStatus() == 1) {
                lblnetworkStatus.setText("SERWER");
            } else if (v.getGs().getNetworkStatus() == 2) {
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
             * Dodaje wszystkie listnery
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
                        v.getGs().client.stopClient();
                        v.getGs().setNetworkStatus(0);
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
                        //RunClient rc = new RunClient(name, "192.168.2.3", 54556, 54777, ne);
                        RunClient rc = new RunClient(name, interfce.tfIpAdress.getText(), 54556, 54777, ne);
                        rc.startClient();
                        v.getGs().setNetworkStatus(2);
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
                        v.getGs().server.serverStop();
                        v.getGs().setNetworkStatus(0);
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
                            v.getGs().setNetworkStatus(1);
                            tables.formatMainTable();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        v.getGs().server.startServer();
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
                        v.getG().setScreen(v.getMainMenuScreen());
                    }
                });
            }
        }
    }
}
