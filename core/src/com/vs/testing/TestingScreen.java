package com.vs.testing;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.enums.DostepneMoby;
import com.vs.eoh.Assets;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.V;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v
 */
public class TestingScreen implements Screen {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private final OrthographicCamera c;
    private final FitViewport viewPort;

    private final Stage stage01 = new Stage();
    private final float rotationSpeed;
    Pixmap pm;
    private TextButton btnMapEditor;
    private TextButton btnExit;
    private TextButton btnSerylizacja;
    private TextButton btnServer;
    private TextButton btnClient;
    private TextButton btnGetConnections;
    private TextButton btnPathInfo;
    private TextButton btnOdczytSerylizacji;
    //private Server server;
    private Table tabela = new Table();
    private V v;

    public TestingScreen(V v) {

        pm = new Pixmap(Gdx.files.internal("mobElfTex.png"));

        pm.setColor(Color.RED);
        pm.fillRectangle(0, 0, 10, 100);
        pm.setColor(Color.WHITE);
        pm.fillRectangle(1, 1, 8, 90);

        Texture pmTex = new Texture(pm);
        pm.dispose();

        rotationSpeed = 0.5f;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        c = new OrthographicCamera(w, h);
        viewPort = new FitViewport(w, h, c);

        this.v = v;

        tabela = new Table(v.getA().skin);

        makeSprites(pmTex);

        makeButtons();

        formatujTabele();

        makeAnimation();
    }

    private void makeAnimation() {
        Texture texture = new Texture(Gdx.files.internal("animation/texExplosionFire.png"));
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 5);
        TextureRegion[] walkFrames = new TextureRegion[8 * 5];
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        Animation walkAnimation = new Animation(0.05f, walkFrames);

        AnimatedImage animatedImage = new AnimatedImage(walkAnimation);
        stage01.addActor(animatedImage);

        Mob mob = new Mob(v, 200, 200, 1, DostepneMoby.Pajak);
        stage01.addActor(mob);
    }

    private void makeSprites(Texture tex) {

        DefaultActor da = new DefaultActor(tex, 200, 200);
        stage01.addActor(da);
    }

    private void makeButtons() {
        btnMapEditor = new TextButton("MapEditor", v.getA().skin);
        btnMapEditor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getG().setScreen(v.getMapEditor());
            }
        });

        btnServer = new TextButton("Run Server", v.getA().skin);
        btnServer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Running server...");
                //server = new Server();

                //Kryo kryo = server.getKryo();
                //kryo.register(SomeRequest.class);
                //kryo.register(SomeResponse.class);

                //server.addListener(new Listener() {
//                    @Override
//                    public void received(Connection connection, Object object) {
//                        if (object instanceof SomeRequest) {
//                            SomeRequest request = (SomeRequest) object;
//                            System.out.println(request.text);
//
//                            SomeResponse response = new SomeResponse();
//                            response.text = "Thanks";
//                            connection.sendTCP(response);
//                        }
//                    }
//                });

//                server.start();

//                try {
//                    server.bind(54556, 54777);
//                } catch (IOException ex) {
//                    Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }
        );

        btnClient = new TextButton("Run Client", v.getA().skin);

        btnClient.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("Running client...");
//                        final Client client = new Client();

//                        Kryo kryo = client.getKryo();
//                        kryo.register(SomeRequest.class);
//                        kryo.register(SomeResponse.class);
//                        client.addListener(new Listener() {
//                            @Override
//                            public void received(Connection connection, Object object) {
//                                if (object instanceof SomeResponse) {
//                                    SomeResponse response = (SomeResponse) object;
//                                    System.out.println(response.text);
//                                }
//                            }
//                        });
                        //new Thread(client).start();
//                        client.start();

//                        new Thread() {
//                            public void run() {
//                                //Log.w("here", "yeah");
//                                try {
//                                    //client.connect(50000, "10.0.2.2", 54557, 54777);
//                                    client.connect(5000, "192.168.1.100", 54556, 54777);// 10.0.2.2 is addres for connecting localhost from emulator.
//                                } catch (IOException e) {
//                                  //  Log.w("expection", e);
//
//                                }
//                            }
//                        }.start();

//                        try {
//                            client.connect(5000, "192.168.1.100", 54556, 54777);
//                        } catch (IOException ex) {
//                            Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
//                        }

                    }
                }
        );

        btnGetConnections = new TextButton("Get Connections", v.getA().skin);
        btnGetConnections.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                System.out.println(server.getConnections().length);
            }
        });

        btnPathInfo = new TextButton("Path Info", v.getA().skin);
        btnPathInfo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PATH LOG", "log");

                boolean isExtAvlb = Gdx.files.isExternalStorageAvailable();
                boolean isLocAvlb = Gdx.files.isLocalStorageAvailable();

                if (isExtAvlb){
                    Gdx.app.log("Ext. avlb.:", "YES");
                    String extRoot = Gdx.files.getExternalStoragePath();
                    Gdx.app.log("Ext. Path:", extRoot);
                } else
                    Gdx.app.log("Ext. avlb.:", "NO");

                if (isLocAvlb){
                    Gdx.app.log("Loc. avlb.:", "YES");
                    String locRoot = Gdx.files.getLocalStoragePath();
                    Gdx.app.log("Loc. Path:", locRoot);
                } else
                    Gdx.app.log("Loc. avlb.:", "NO");
                }
        });

        btnExit = new TextButton("EXIT", v.getA().skin);

        btnExit.setPosition(
                1, 1);
        btnExit.setSize(
                200, 100);
        btnExit.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y
                    ) {
                        v.getG().setScreen(v.getMainMenuScreen());
                    }
                }
        );

        btnSerylizacja = new TextButton("Serylizacja", v.getA().skin);

        btnSerylizacja.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y
                    ) {
                        Mapa mapa = new Mapa();

                        try {
                            ObjectOutputStream wy = new ObjectOutputStream(new FileOutputStream("d:\\mapa.dat"));
                            wy.writeObject(mapa);
                            System.out.println("serylizacja obiektu");
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
        );

        btnOdczytSerylizacji = new TextButton("Odczyt Serylizacji", v.getA().skin);

        btnOdczytSerylizacji.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y
                    ) {
                        Mapa mapa = null;

                        try {
                            ObjectInputStream we = new ObjectInputStream(new FileInputStream("d:\\mapa.dat"));
                            mapa = (Mapa) we.readObject();
                            System.out.println("odczyt obiektu");
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(TestingScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        System.out.println(mapa.getIloscPolX() + " " + mapa.getIloscPolY());
                    }

                }
        );

    }

    private void formatujTabele() {
        tabela.setFillParent(true);
        tabela.pad(10);

        tabela.add(new Label("Testowanie funkcji", v.getA().skin)).expand().align(Align.top);
        tabela.row();
        tabela.add(btnSerylizacja);
        tabela.add(btnOdczytSerylizacji);
        tabela.row();
        tabela.add(btnMapEditor);
        tabela.row();
        tabela.add(btnServer);
        tabela.row();
        tabela.add(btnClient);
        tabela.row();
        tabela.add(btnGetConnections);
        tabela.row();
        tabela.add(btnPathInfo);
        tabela.row();
        tabela.add(btnExit).width(200).height(50).space(300);

        stage01.addActor(tabela);

    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            c.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            c.zoom -= 1.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            c.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            c.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            c.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            c.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            c.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            c.rotate(rotationSpeed, 0, 0, 1);
        }

        c.zoom = MathUtils.clamp(c.zoom, 0.1f, 100 / c.viewportWidth);

        float effectiveViewportWidth = c.viewportWidth * c.zoom;
        float effectiveViewportHeight = c.viewportHeight * c.zoom;

        c.position.x = MathUtils.clamp(c.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        c.position.y = MathUtils.clamp(c.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage01);
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage01.act();
        stage01.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage01.getViewport().update(width, height, true);
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
        stage01.dispose();
    }

    public class AnimatedImage extends Image {

        protected Animation animation = null;
        private float stateTime = 0;

        public AnimatedImage(Animation animation) {
            super(animation.getKeyFrame(0));
            this.animation = animation;
        }

        @Override
        public void act(float delta) {

            //animation.setPlayMode(Animation.PlayMode.NORMAL);
            if (!animation.isAnimationFinished(stateTime)) {
                ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
                super.act(delta);
            } else {
                this.remove();
            }
        }
    }

//    public class SomeRequest {
//
//        public String text;
//    }
//
//    public class SomeResponse {
//
//        public String text;
//    }
}
