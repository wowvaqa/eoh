package com.vs.mapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.V;

import java.io.IOException;

/**
 * Created by v on 17.07.16.
 * Class represents visuality of map editor.
 */
public class MapEditScreen implements Screen {

    private InputMultiplexer inputMultiPlexer = new InputMultiplexer();

    private V v;

    private OrthographicCamera c;
    private FitViewport viewPort;

    private float w;
    private float h;

    private Interface interfaceManager;

    private MapEdit mapEdit;

    private Stage interfaceStage;
    private Stage mapStage;

    /**
     * Constructor
     * @param v Object of V class
     */
    public MapEditScreen(V v){
        this.v = v;

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        c = new OrthographicCamera(w, h);
        viewPort = new FitViewport(w, h, c);

        interfaceStage = new Stage(viewPort);
        interfaceManager = new Interface();

        mapEdit = new MapEdit();

        interfaceStage.addActor(interfaceManager.imageButtonNew);
        interfaceStage.addActor(interfaceManager.imageButtonLoad);
        interfaceStage.addActor(interfaceManager.imageButtonSave);
        interfaceStage.addActor(interfaceManager.imageButtonExit);
        interfaceStage.addActor(interfaceManager.imageButtonBrush);
    }

    @Override
    public void show() {
        inputMultiPlexer.addProcessor(interfaceStage);
        if (mapStage != null) {
            inputMultiPlexer.addProcessor(mapStage);
        }
        Gdx.input.setInputProcessor(inputMultiPlexer);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (mapStage != null) {
            inputMultiPlexer.addProcessor(mapStage);
            mapStage.act();
            mapStage.draw();
        }

        interfaceStage.act();
        interfaceStage.draw();

        Gdx.input.setInputProcessor(inputMultiPlexer);

    }

    @Override
    public void resize(int width, int height) {
        interfaceStage.getViewport().update(width, height, true);
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

    }

    private class Interface {

        public ImageButton imageButtonNew;
        public ImageButton imageButtonLoad;
        public ImageButton imageButtonSave;
        public ImageButton imageButtonExit;
        public ImageButton imageButtonBrush;

        public ImageButton imageButtonTerrainBrush;
        public ImageButton imageButtonPlayerBrush;
        public ImageButton imageButtonMobsBrush;

        public ImageButton imageButtonForestBrush;

        public Interface() {
            makeButtons();
        }

        /**
         * Return load map window.
         * @return Window
         */
        public Window getLoadMapWindow() {
            final Window window = new Window("Wczytaj mape", v.getA().skin);
            final List listOfMap = new List(v.getA().skin);
            ScrollPane scrollPane = new ScrollPane(listOfMap, v.getA().skin);
            scrollPane.setSize(300, 200);


            window.setSize(600, 400);

            FileHandle[] files = Gdx.files.local("").list();
            for (FileHandle file : files) {
                if (file.extension().equals("map")) {
                    listOfMap.getItems().add(file);
                }
            }

            TextButton tbOk = new TextButton("OK", v.getA().skin);
            TextButton tbCancel = new TextButton("Cancel", v.getA().skin);
            tbOk.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (listOfMap.getSelected() != null) {
                        mapEdit = mapEdit.loadMap(listOfMap);
                        if (mapStage == null) {
                            mapStage = new Stage();
                        }

                        mapStage.clear();

                        for (int i = 0; i < mapEdit.mapColumns; i++) {
                            for (int j = 0; j < mapEdit.mapRows; j++) {
                                mapEdit.fillField(mapEdit.fields[i][j]);
                                mapEdit.fields[i][j].addListeners(mapEdit.fields[i][j]);
                                mapStage.addActor(mapEdit.fields[i][j]);
                            }
                        }
                        window.remove();
                    } else {
                        //window.remove();
                        final Window windowError = new Window("Wybierz mape", v.getA().skin);
                        windowError.setMovable(false);
                        windowError.setSize(300, 200);
                        windowError.add(new Label("Nie wybrano mapy", v.getA().skin));
                        windowError.row();
                        TextButton btnOK = new TextButton("OK", v.getA().skin);
                        btnOK.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                window.setVisible(true);
                                windowError.remove();
                            }
                        });
                        windowError.add(btnOK).size(100, 50).align(Align.center);
                        windowError.setPosition(
                                Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - windowError.getWidth() / 2,
                                Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - windowError.getHeight() / 2);
                        window.setVisible(false);

                        interfaceStage.addActor(windowError);
                    }
                }

            });
            tbCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    window.remove();
                }
            });

            window.add(scrollPane).size(300, 200).colspan(2);
            window.row();
            window.add(tbOk).pad(5).size(100, 50);
            window.add(tbCancel).pad(5).size(100, 50);
            window.row();

            return window;
        }

        /**
         * Returns saving map window.
         *
         * @return Window
         */
        public Window getSaveMapWindow() {
            final Window window = new Window("Zapisz mape", v.getA().skin);
            window.setSize(600, 400);
            final TextField tfNameofMap = new TextField("", v.getA().skin);
            TextButton tbOk = new TextButton("OK", v.getA().skin);
            TextButton tbCancel = new TextButton("Cancel", v.getA().skin);
            tbOk.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        mapEdit.saveMap(tfNameofMap.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    window.remove();
                }
            });
            tbCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    window.remove();
                }
            });

            window.add(new Label("Nazwa mapy:", v.getA().skin)).colspan(2);
            window.row();
            window.add(tfNameofMap).pad(5).colspan(2);
            window.row();
            window.add(tbOk).pad(5).size(100, 50);
            window.add(tbCancel).pad(5).size(100, 50);
            window.row();

            return window;
        }

        /**
         * Return Brush window
         *
         * @return Window
         */
        public Window getBrushWindow() {
            final Window window = new Window("Pedzel", v.getA().skin);
            window.setSize(1000, 600);

            final ImageButton.ImageButtonStyle imageButtonStyleTerrainBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMobsBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStylePlayerBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleBuldingBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleTresureBoxBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleRubberBrush = new ImageButton.ImageButtonStyle();

            Texture terrainBrushUp = new Texture("interface/mapEditor/terrains/terrainBrushUp.png");
            Texture terrainBrushDown = new Texture("interface/mapEditor/terrains/terrainBrushDown.png");
            Texture mobsBrushUp = new Texture("interface/mapEditor/mobsBrushUp.png");
            Texture mobsBrushDown = new Texture("interface/mapEditor/mobsBrushDown.png");
            Texture playerBrushUp = new Texture("interface/mapEditor/PlayerBrushUp.png");
            Texture plyerBrushDown = new Texture("interface/mapEditor/PlayerBrushDown.png");
            Texture buldingBrushUp = new Texture("interface/mapEditor/buldings/buldingBrushUp.png");
            Texture buldingBrushDown = new Texture("interface/mapEditor/buldings/buldingBrushDown.png");
            Texture tresureBoxBrushUp = new Texture("interface/mapEditor/boxes/tresureBoxBrushUp.png");
            Texture tresureBoxBrushDown = new Texture("interface/mapEditor/boxes/tresureBoxBrushDown.png");
            Texture rubberBrushUp = new Texture("interface/mapEditor/rubberButtonUp.png");
            Texture rubberBrushDown = new Texture("interface/mapEditor/rubberButtonDown.png");

            imageButtonStyleTerrainBrush.imageUp = new TextureRegionDrawable(new TextureRegion(terrainBrushUp));
            imageButtonStyleTerrainBrush.imageDown = new TextureRegionDrawable(new TextureRegion(terrainBrushDown));
            imageButtonStyleMobsBrush.imageUp = new TextureRegionDrawable(new TextureRegion(mobsBrushUp));
            imageButtonStyleMobsBrush.imageDown = new TextureRegionDrawable(new TextureRegion(mobsBrushDown));
            imageButtonStylePlayerBrush.imageUp = new TextureRegionDrawable(new TextureRegion(playerBrushUp));
            imageButtonStylePlayerBrush.imageDown = new TextureRegionDrawable(new TextureRegion(plyerBrushDown));
            imageButtonStyleBuldingBrush.imageUp = new TextureRegionDrawable(new TextureRegion(buldingBrushUp));
            imageButtonStyleBuldingBrush.imageDown = new TextureRegionDrawable(new TextureRegion(buldingBrushDown));
            imageButtonStyleTresureBoxBrush.imageUp = new TextureRegionDrawable(new TextureRegion(tresureBoxBrushUp));
            imageButtonStyleTresureBoxBrush.imageDown = new TextureRegionDrawable(new TextureRegion(tresureBoxBrushDown));
            imageButtonStyleRubberBrush.imageUp = new TextureRegionDrawable(new TextureRegion(rubberBrushUp));
            imageButtonStyleRubberBrush.imageDown = new TextureRegionDrawable(new TextureRegion(rubberBrushDown));

            imageButtonTerrainBrush = new ImageButton(imageButtonStyleTerrainBrush);
            imageButtonPlayerBrush = new ImageButton(imageButtonStylePlayerBrush);
            imageButtonMobsBrush = new ImageButton(imageButtonStyleMobsBrush);
            ImageButton imageButtonTresureBoxBrush = new ImageButton(imageButtonStyleTresureBoxBrush);
            ImageButton imageButtonBuldingBrush = new ImageButton(imageButtonStyleBuldingBrush);

            ImageButton imageButtonRubberBrush = new ImageButton(imageButtonStyleRubberBrush);

            imageButtonMobsBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getMobsBrushWindow());
                    window.remove();
                }
            });

            imageButtonTerrainBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getTerrainBrushWindow());
                    window.remove();
                }
            });

            imageButtonPlayerBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getPlayerBrushWindow());
                    window.remove();
                }
            });

            imageButtonBuldingBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getBuldingBrushWindow());
                    window.remove();
                }
            });

            imageButtonTresureBoxBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getTresureBoxBrushWindow());
                    window.remove();
                }
            });

            imageButtonRubberBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleRubberBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.rubberDraw;
                    window.remove();
                }
            });

            window.add(imageButtonTerrainBrush);
            window.add(imageButtonPlayerBrush);
            window.add(imageButtonMobsBrush);
            window.row();
            window.add(imageButtonTresureBoxBrush);
            window.add(imageButtonBuldingBrush);
            window.add(imageButtonRubberBrush);

            return window;
        }

        /**
         * Rerturns window of player brush
         *
         * @return Window
         */
        public Window getPlayerBrushWindow() {
            final Window window = new Window("Gracze", v.getA().skin);
            window.setSize(600, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStylePlayer01Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStylePlayer02Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture player01Up = new Texture("interface/mapEditor/player01BrushUp.png");
            Texture player01Down = new Texture("interface/mapEditor/player01BrushDown.png");
            Texture player02Up = new Texture("interface/mapEditor/player02BrushUp.png");
            Texture player02Down = new Texture("interface/mapEditor/player02BrushDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStylePlayer01Brush.imageUp = new TextureRegionDrawable(new TextureRegion(player01Up));
            imageButtonStylePlayer01Brush.imageDown = new TextureRegionDrawable(new TextureRegion(player01Down));
            imageButtonStylePlayer02Brush.imageUp = new TextureRegionDrawable(new TextureRegion(player02Up));
            imageButtonStylePlayer02Brush.imageDown = new TextureRegionDrawable(new TextureRegion(player02Down));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            ImageButton imagePlayer01 = new ImageButton(imageButtonStylePlayer01Brush);
            ImageButton imagePlayer02 = new ImageButton(imageButtonStylePlayer02Brush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imagePlayer01.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.player1Draw;
                    imageButtonBrush.setStyle(imageButtonStylePlayer01Brush);
                    window.remove();
                }
            });
            imagePlayer02.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.player2Draw;
                    imageButtonBrush.setStyle(imageButtonStylePlayer02Brush);
                    window.remove();
                }
            });
            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    window.remove();
                }
            });

            window.add(imagePlayer01);
            window.add(imagePlayer02);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return terrain brush window
         *
         * @return Window
         */
        public Window getMobsBrushWindow() {
            final Window window = new Window("Moby", v.getA().skin);
            window.setSize(600, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleMobLvl1Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMobLvl2Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture mobsLvl1Up = new Texture("interface/mapEditor/mobs/mobsBrushLvl1Up.png");
            Texture mobsLvl1Down = new Texture("interface/mapEditor/mobs/mobsBrushLvl1Down.png");
            Texture mobsLvl2Up = new Texture("interface/mapEditor/mobs/mobsBrushLvl2Up.png");
            Texture mobsLvl2Down = new Texture("interface/mapEditor/mobs/mobsBrushLvl2Down.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleMobLvl1Brush.imageUp = new TextureRegionDrawable(new TextureRegion(mobsLvl1Up));
            imageButtonStyleMobLvl1Brush.imageDown = new TextureRegionDrawable(new TextureRegion(mobsLvl1Down));
            imageButtonStyleMobLvl2Brush.imageUp = new TextureRegionDrawable(new TextureRegion(mobsLvl2Up));
            imageButtonStyleMobLvl2Brush.imageDown = new TextureRegionDrawable(new TextureRegion(mobsLvl2Down));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            ImageButton imageButtonMobsLvl1 = new ImageButton(imageButtonStyleMobLvl1Brush);
            ImageButton imageButtonMobsLvl2 = new ImageButton(imageButtonStyleMobLvl2Brush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonMobsLvl1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getMobsLvl1BrushWindow());
                    window.remove();
                }
            });

            imageButtonMobsLvl2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    interfaceStage.addActor(getMobsLvl2BrushWindow());
                    window.remove();
                }
            });

            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = null;
                    window.remove();
                }
            });

            window.add(imageButtonMobsLvl1);
            window.add(imageButtonMobsLvl2);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return terrain brush window
         *
         * @return Window
         */
        public Window getMobsLvl1BrushWindow() {
            final Window window = new Window("Mobs level 1", v.getA().skin);
            window.setSize(1000, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleMob01Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMob02Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMobRandomBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture mob01Up = new Texture("interface/mapEditor/mobs/mobsBrushSkeletonUp.png");
            Texture mob01Down = new Texture("interface/mapEditor/mobs/mobsBrushSkeletonDown.png");
            Texture mob02Up = new Texture("interface/mapEditor/mobs/mobsBrushWolfUp.png");
            Texture mob02Down = new Texture("interface/mapEditor/mobs/mobsBrushWolfDown.png");
            Texture mobRandomUp = new Texture("interface/mapEditor/mobs/mobsBrushRandomUp.png");
            Texture mobRandomDown = new Texture("interface/mapEditor/mobs/mobsBrushRandomDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleMob01Brush.imageUp = new TextureRegionDrawable(new TextureRegion(mob01Up));
            imageButtonStyleMob01Brush.imageDown = new TextureRegionDrawable(new TextureRegion(mob01Down));
            imageButtonStyleMob02Brush.imageUp = new TextureRegionDrawable(new TextureRegion(mob02Up));
            imageButtonStyleMob02Brush.imageDown = new TextureRegionDrawable(new TextureRegion(mob02Down));
            imageButtonStyleMobRandomBrush.imageUp = new TextureRegionDrawable(new TextureRegion(mobRandomUp));
            imageButtonStyleMobRandomBrush.imageDown = new TextureRegionDrawable(new TextureRegion(mobRandomDown));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            ImageButton imageButtonMob01 = new ImageButton(imageButtonStyleMob01Brush);
            ImageButton imageButtonMob02 = new ImageButton(imageButtonStyleMob02Brush);
            ImageButton imageButtonMobRandom = new ImageButton(imageButtonStyleMobRandomBrush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonMob01.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.mobSkeletonDraw;
                    imageButtonBrush.setStyle(imageButtonStyleMob01Brush);
                    window.remove();
                }
            });

            imageButtonMob02.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.mobWolfDraw;
                    imageButtonBrush.setStyle(imageButtonStyleMob02Brush);
                    window.remove();
                }
            });

            imageButtonMobRandom.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.mobRandomLvl1Draw;
                    imageButtonBrush.setStyle(imageButtonStyleMobRandomBrush);
                    window.remove();
                }
            });

            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = null;
                    window.remove();
                }
            });

            window.add(imageButtonMob01);
            window.add(imageButtonMob02);
            window.add(imageButtonMobRandom);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return terrain brush window
         *
         * @return Window
         */
        public Window getMobsLvl2BrushWindow() {
            final Window window = new Window("Mobs level 2", v.getA().skin);
            window.setSize(1000, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleMob01Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMob02Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMobRandomBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture mob01Up = new Texture("interface/mapEditor/mobs/mobsBrushSpiderUp.png");
            Texture mob01Down = new Texture("interface/mapEditor/mobs/mobsBrushSpiderDown.png");
            Texture mob02Up = new Texture("interface/mapEditor/mobs/mobsBrushZombieUp.png");
            Texture mob02Down = new Texture("interface/mapEditor/mobs/mobsBrushZombieDown.png");
            Texture mobRandomUp = new Texture("interface/mapEditor/mobs/mobsBrushRandomUp.png");
            Texture mobRandomDown = new Texture("interface/mapEditor/mobs/mobsBrushRandomDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleMob01Brush.imageUp = new TextureRegionDrawable(new TextureRegion(mob01Up));
            imageButtonStyleMob01Brush.imageDown = new TextureRegionDrawable(new TextureRegion(mob01Down));
            imageButtonStyleMob02Brush.imageUp = new TextureRegionDrawable(new TextureRegion(mob02Up));
            imageButtonStyleMob02Brush.imageDown = new TextureRegionDrawable(new TextureRegion(mob02Down));
            imageButtonStyleMobRandomBrush.imageUp = new TextureRegionDrawable(new TextureRegion(mobRandomUp));
            imageButtonStyleMobRandomBrush.imageDown = new TextureRegionDrawable(new TextureRegion(mobRandomDown));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            ImageButton imageButtonMob01 = new ImageButton(imageButtonStyleMob01Brush);
            ImageButton imageButtonMob02 = new ImageButton(imageButtonStyleMob02Brush);
            ImageButton imageButtonMobRandom = new ImageButton(imageButtonStyleMobRandomBrush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonMob01.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.mobSpiederDraw;
                    imageButtonBrush.setStyle(imageButtonStyleMob01Brush);
                    window.remove();
                }
            });

            imageButtonMob02.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.mobZombieDraw;
                    imageButtonBrush.setStyle(imageButtonStyleMob02Brush);
                    window.remove();
                }
            });

            imageButtonMobRandom.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.mobRandomLvl2Draw;
                    imageButtonBrush.setStyle(imageButtonStyleMobRandomBrush);
                    window.remove();
                }
            });

            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = null;
                    window.remove();
                }
            });

            window.add(imageButtonMob01);
            window.add(imageButtonMob02);
            window.add(imageButtonMobRandom);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return terrain brush window
         *
         * @return Window
         */
        public Window getTerrainBrushWindow() {
            final Window window = new Window("Teren", v.getA().skin);
            window.setSize(1000, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleForestBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleMountainBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleRiverBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture forestBrushUp = new Texture("interface/mapEditor/terrains/forestBrushUp.png");
            Texture forestBrushDown = new Texture("interface/mapEditor/terrains/forestBrushDown.png");
            Texture mountainBrushUp = new Texture("interface/mapEditor/terrains/mountainBrushUp.png");
            Texture mountainBrushDown = new Texture("interface/mapEditor/terrains/mountainBrushDown.png");
            Texture riverBrushUp = new Texture("interface/mapEditor/terrains/riverBrushUp.png");
            Texture riverBrushDown = new Texture("interface/mapEditor/terrains/riverBrushDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleForestBrush.imageUp = new TextureRegionDrawable(new TextureRegion(forestBrushUp));
            imageButtonStyleForestBrush.imageDown = new TextureRegionDrawable(new TextureRegion(forestBrushDown));
            imageButtonStyleMountainBrush.imageUp = new TextureRegionDrawable(new TextureRegion(mountainBrushUp));
            imageButtonStyleMountainBrush.imageDown = new TextureRegionDrawable(new TextureRegion(mountainBrushDown));
            imageButtonStyleRiverBrush.imageUp = new TextureRegionDrawable(new TextureRegion(riverBrushUp));
            imageButtonStyleRiverBrush.imageDown = new TextureRegionDrawable(new TextureRegion(riverBrushDown));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            imageButtonForestBrush = new ImageButton(imageButtonStyleForestBrush);
            ImageButton imageButtonMountainBrush = new ImageButton(imageButtonStyleMountainBrush);
            ImageButton imageButtonRiverBrush = new ImageButton(imageButtonStyleRiverBrush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonForestBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleForestBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.forestDraw;
                    window.remove();
                }
            });

            imageButtonMountainBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleMountainBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.mountainDraw;
                    window.remove();
                }
            });

            imageButtonRiverBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleRiverBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.riverDraw;
                    window.remove();
                }
            });

            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = null;
                    window.remove();
                }
            });

            window.add(imageButtonForestBrush);
            window.add(imageButtonMountainBrush);
            window.add(imageButtonRiverBrush);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return terrain brush window
         *
         * @return Window
         */
        public Window getBuldingBrushWindow() {
            final Window window = new Window("Budynki", v.getA().skin);
            window.setSize(1000, 600);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleMagicTowerBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleWisdomTowerBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleDefenceTowerBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture magicTowerBrushUp = new Texture("interface/mapEditor/buldings/magicTowerBrushUp.png");
            Texture magicTowerBrushDown = new Texture("interface/mapEditor/buldings/magicTowerBrushDown.png");
            Texture wisdomTowerBrushUp = new Texture("interface/mapEditor/buldings/wisdomTowerBrushUp.png");
            Texture wisdomTowerBrushDown = new Texture("interface/mapEditor/buldings/wisdomTowerBrushDown.png");
            Texture defenceTowerBrushUp = new Texture("interface/mapEditor/buldings/defenceTowerBrushUp.png");
            Texture defenceTowerBrushDown = new Texture("interface/mapEditor/buldings/defenceTowerBrushDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleMagicTowerBrush.imageUp = new TextureRegionDrawable(new TextureRegion(magicTowerBrushUp));
            imageButtonStyleMagicTowerBrush.imageDown = new TextureRegionDrawable(new TextureRegion(magicTowerBrushDown));
            imageButtonStyleWisdomTowerBrush.imageUp = new TextureRegionDrawable(new TextureRegion(wisdomTowerBrushUp));
            imageButtonStyleWisdomTowerBrush.imageDown = new TextureRegionDrawable(new TextureRegion(wisdomTowerBrushDown));
            imageButtonStyleDefenceTowerBrush.imageUp = new TextureRegionDrawable(new TextureRegion(defenceTowerBrushUp));
            imageButtonStyleDefenceTowerBrush.imageDown = new TextureRegionDrawable(new TextureRegion(defenceTowerBrushDown));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            ImageButton imageButtonMagicTowerBrush = new ImageButton(imageButtonStyleMagicTowerBrush);
            ImageButton imageButtonWisdomTowerBrush = new ImageButton(imageButtonStyleWisdomTowerBrush);
            ImageButton imageButtonDefenceTowerBrush = new ImageButton(imageButtonStyleDefenceTowerBrush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonMagicTowerBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleMagicTowerBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.towerMagicDraw;
                    window.remove();
                }
            });

            imageButtonWisdomTowerBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleWisdomTowerBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.towerWisdomDraw;
                    window.remove();
                }
            });

            imageButtonDefenceTowerBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleDefenceTowerBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.towerDefenceDraw;
                    window.remove();
                }
            });

            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = null;
                    window.remove();
                }
            });

            window.add(imageButtonMagicTowerBrush);
            window.add(imageButtonWisdomTowerBrush);
            window.add(imageButtonDefenceTowerBrush);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return terrain brush window
         *
         * @return Window
         */
        public Window getTresureBoxBrushWindow() {
            final Window window = new Window("Skrzynie ze skarbem", v.getA().skin);
            window.setSize(600, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleTresureBoxLvl01Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleTresureBoxLvl02Brush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture tresureBoxLvl1Up = new Texture("interface/mapEditor/boxes/tresureBoxLvl1BrushUp.png");
            Texture tresureBoxLvl1Down = new Texture("interface/mapEditor/boxes/tresureBoxLvl1BrushDown.png");
            Texture tresureBoxLvl2Up = new Texture("interface/mapEditor/boxes/tresureBoxLvl2BrushUp.png");
            Texture tresureBoxLvl2Down = new Texture("interface/mapEditor/boxes/tresureBoxLvl2BrushDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleTresureBoxLvl01Brush.imageUp = new TextureRegionDrawable(new TextureRegion(tresureBoxLvl1Up));
            imageButtonStyleTresureBoxLvl01Brush.imageDown = new TextureRegionDrawable(new TextureRegion(tresureBoxLvl1Down));
            imageButtonStyleTresureBoxLvl02Brush.imageUp = new TextureRegionDrawable(new TextureRegion(tresureBoxLvl2Up));
            imageButtonStyleTresureBoxLvl02Brush.imageDown = new TextureRegionDrawable(new TextureRegion(tresureBoxLvl2Down));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            ImageButton imageButtonMob01 = new ImageButton(imageButtonStyleTresureBoxLvl01Brush);
            ImageButton imageButtonMob02 = new ImageButton(imageButtonStyleTresureBoxLvl02Brush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonMob01.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.tresureBox1Draw;
                    imageButtonBrush.setStyle(imageButtonStyleTresureBoxLvl01Brush);
                    window.remove();
                }
            });

            imageButtonMob02.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = MapEdit.DrawingType.tresureBox2Draw;
                    imageButtonBrush.setStyle(imageButtonStyleTresureBoxLvl02Brush);
                    window.remove();
                }
            });

            imageButtonCancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    mapEdit.drawingType = null;
                    window.remove();
                }
            });

            window.add(imageButtonMob01);
            window.add(imageButtonMob02);
            window.add(imageButtonCancel);

            return window;
        }

        /**
         * Return load map window
         * @return Window
         */
        public Window getNewMapWindow() {

            final Window window = new Window("Nowa mapa", v.getA().skin);
            window.setSize(600, 400);
            Label lblAmountOfXfields = new Label("ilosc pol X: ", v.getA().skin);
            Label lblAmountOfYfields = new Label("ilosc pol Y: ", v.getA().skin);
            TextField tfNameofMap = new TextField("", v.getA().skin);
            final TextField tfAmountOfXflilds = new TextField("10", v.getA().skin);
            final TextField tfAmountOfYflilds = new TextField("10", v.getA().skin);
            TextButton tBmakeMap = new TextButton("OK", v.getA().skin);
            tBmakeMap.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (mapStage != null) {
                        mapStage.clear();
                    }
                    mapStage = mapEdit.createStage(
                            Integer.parseInt(tfAmountOfXflilds.getText()),
                            Integer.parseInt(tfAmountOfYflilds.getText()),
                            viewPort
                    );
                    window.remove();
                }
            });

            window.add(new Label("Nazwa mapy:", v.getA().skin));
            window.add(tfNameofMap).pad(5);
            window.row();
            window.add(lblAmountOfXfields).pad(5);
            window.add(tfAmountOfXflilds).pad(5);
            window.row();
            window.add(lblAmountOfYfields).pad(5);
            window.add(tfAmountOfYflilds).pad(5);
            window.row();
            window.add(tBmakeMap).pad(5).size(100, 50).colspan(2);
            window.row();

            return window;
        }

//        /**
//         * Saving map to file.
//         */
//        private void saveMap(){
//
//            StringWriter writer = new StringWriter();
//            XmlWriter xml = new XmlWriter(writer);
//
//            try {
//                xml.element("meow")
//                        .attribute("moo", "cow")
//                        .element("child")
//                        .attribute("moo", "cow")
//                        .element("child")
//                        .attribute("moo", "cow")
//                        .text("XML is like violence. If it doesn't solve your problem, you're not using enough of it.")
//                        .pop()
//                        .pop()
//                        .pop();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(writer);
//
//        }

        /**
         * Make main buttons.
         */
        private void makeButtons() {

            ImageButton.ImageButtonStyle imageButtonStyleNew = new ImageButton.ImageButtonStyle();
            ImageButton.ImageButtonStyle imageButtonStyleLoad = new ImageButton.ImageButtonStyle();
            ImageButton.ImageButtonStyle imageButtonStyleSave = new ImageButton.ImageButtonStyle();
            ImageButton.ImageButtonStyle imageButtonStyleExit = new ImageButton.ImageButtonStyle();
            ImageButton.ImageButtonStyle imageButtonStyleBrush = new ImageButton.ImageButtonStyle();

            Texture newUp = new Texture("interface/mapEditor/newButtonUp.png");
            Texture newDown = new Texture("interface/mapEditor/newButtonDown.png");
            Texture loadUp = new Texture("interface/mapEditor/loadButtonUp.png");
            Texture loadDown = new Texture("interface/mapEditor/loadButtonDown.png");
            Texture saveUp = new Texture("interface/mapEditor/saveButtonUp.png");
            Texture saveDown = new Texture("interface/mapEditor/saveButtonDown.png");
            Texture exitUp = new Texture("interface/mapEditor/exitButtonUp.png");
            Texture exitDown = new Texture("interface/mapEditor/exitButtonDown.png");
            Texture brushUp = new Texture("interface/mapEditor/brushButtonUp.png");
            Texture brushDown = new Texture("interface/mapEditor/brushButtonDown.png");

            newUp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            newDown.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            loadUp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            loadDown.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            saveUp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            saveDown.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            exitUp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            exitDown.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            brushUp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            brushDown.setFilter(TextureFilter.Linear, TextureFilter.Linear);

            imageButtonStyleNew.imageUp = new TextureRegionDrawable(new TextureRegion(newUp));
            imageButtonStyleNew.imageDown = new TextureRegionDrawable(new TextureRegion(newDown));
            imageButtonStyleLoad.imageUp = new TextureRegionDrawable(new TextureRegion(loadUp));
            imageButtonStyleLoad.imageDown = new TextureRegionDrawable(new TextureRegion(loadDown));
            imageButtonStyleSave.imageUp = new TextureRegionDrawable(new TextureRegion(saveUp));
            imageButtonStyleSave.imageDown = new TextureRegionDrawable(new TextureRegion(saveDown));
            imageButtonStyleExit.imageUp = new TextureRegionDrawable(new TextureRegion(exitUp));
            imageButtonStyleExit.imageDown = new TextureRegionDrawable(new TextureRegion(exitDown));
            imageButtonStyleBrush.imageUp = new TextureRegionDrawable(new TextureRegion(brushUp));
            imageButtonStyleBrush.imageDown = new TextureRegionDrawable(new TextureRegion(brushDown));

            imageButtonNew = new ImageButton(imageButtonStyleNew);
            imageButtonLoad = new ImageButton(imageButtonStyleLoad);
            imageButtonSave = new ImageButton(imageButtonStyleSave);
            imageButtonExit = new ImageButton(imageButtonStyleExit);
            imageButtonBrush = new ImageButton(imageButtonStyleBrush);

            imageButtonNew.setPosition(10, Gdx.graphics.getHeight() - 85);
            imageButtonNew.setSize(75, 75);
            imageButtonExit.setPosition(95, Gdx.graphics.getHeight() - 85);
            imageButtonExit.setSize(75, 75);
            imageButtonLoad.setPosition(10, Gdx.graphics.getHeight() - 170);
            imageButtonLoad.setSize(75, 75);
            imageButtonSave.setPosition(95, Gdx.graphics.getHeight() - 170);
            imageButtonSave.setSize(75, 75);
            imageButtonBrush.setPosition(10, Gdx.graphics.getHeight() - 340);
            imageButtonBrush.setSize(160, 160);

            addListeners();
        }

        /**
         * Adding listeners to interface buttons.
         */
        private void addListeners() {
            imageButtonExit.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    v.getG().setScreen(v.getMainMenuScreen());
                }
            });

            imageButtonNew.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Window window = getNewMapWindow();
                    window.setMovable(false);
                    window.setModal(true);
                    window.setPosition(
                            Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                            Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
                    );
                    interfaceStage.addActor(window);
                }
            });

            imageButtonBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Window window = getBrushWindow();
                    window.setMovable(false);
                    window.setModal(true);
                    window.setPosition(
                            Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                            Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
                    );
                    interfaceStage.addActor(window);
                }
            });

            imageButtonSave.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Window window = getSaveMapWindow();
                    window.setMovable(false);
                    window.setModal(true);
                    window.setPosition(
                            Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                            Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
                    );
                    interfaceStage.addActor(window);
                }
            });

            imageButtonLoad.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Window window = getLoadMapWindow();
                    window.setMovable(false);
                    window.setModal(true);
                    window.setPosition(
                            Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                            Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
                    );
                    interfaceStage.addActor(window);
                }
            });
        }
    }
}
