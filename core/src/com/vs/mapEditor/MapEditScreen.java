package com.vs.mapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.V;

import java.io.IOException;
import java.io.StringWriter;

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

        public ImageButton imageButtonForestBrush;

        public Interface() {
            makeButtons();
        }

        /**
         * Return load map window.
         * @return Window
         */
        public Window getLoadMapWindow() {
            return null;
        }

        /**
         * Returns saving map window.
         *
         * @return Window
         */
        public Window getSaveMapWindow() {
            final Window window = new Window("Zapisz mape", v.getA().skin);
            window.setSize(600, 400);
            TextField tfNameofMap = new TextField("", v.getA().skin);
            TextButton tbOk = new TextButton("OK", v.getA().skin);
            TextButton tbCancel = new TextButton("Cancel", v.getA().skin);
            tbOk.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        mapEdit.saveMap();
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
            window.setSize(600, 400);

            final ImageButton.ImageButtonStyle imageButtonStyleTerrainBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStylePlayerBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleRubberBrush = new ImageButton.ImageButtonStyle();

            Texture terrainBrushUp = new Texture("interface/mapEditor/terrainBrushUp.png");
            Texture terrainBrushDown = new Texture("interface/mapEditor/terrainBrushDown.png");
            Texture playerBrushUp = new Texture("interface/mapEditor/PlayerBrushUp.png");
            Texture plyerBrushDown = new Texture("interface/mapEditor/PlayerBrushDown.png");
            Texture rubberBrushUp = new Texture("interface/mapEditor/rubberButtonUp.png");
            Texture rubberBrushDown = new Texture("interface/mapEditor/rubberButtonDown.png");

            imageButtonStyleTerrainBrush.imageUp = new TextureRegionDrawable(new TextureRegion(terrainBrushUp));
            imageButtonStyleTerrainBrush.imageDown = new TextureRegionDrawable(new TextureRegion(terrainBrushDown));
            imageButtonStylePlayerBrush.imageUp = new TextureRegionDrawable(new TextureRegion(playerBrushUp));
            imageButtonStylePlayerBrush.imageDown = new TextureRegionDrawable(new TextureRegion(plyerBrushDown));
            imageButtonStyleRubberBrush.imageUp = new TextureRegionDrawable(new TextureRegion(rubberBrushUp));
            imageButtonStyleRubberBrush.imageDown = new TextureRegionDrawable(new TextureRegion(rubberBrushDown));

            imageButtonTerrainBrush = new ImageButton(imageButtonStyleTerrainBrush);
            imageButtonPlayerBrush = new ImageButton(imageButtonStylePlayerBrush);
            ImageButton imageButtonRubberBrush = new ImageButton(imageButtonStyleRubberBrush);

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
        public Window getTerrainBrushWindow() {
            final Window window = new Window("Teren", v.getA().skin);
            window.setSize(600, 400);
            window.setPosition(
                    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2 - window.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 - window.getHeight() / 2
            );

            final ImageButton.ImageButtonStyle imageButtonStyleForestBrush = new ImageButton.ImageButtonStyle();
            final ImageButton.ImageButtonStyle imageButtonStyleCancel = new ImageButton.ImageButtonStyle();

            Texture forestBrushUp = new Texture("interface/mapEditor/forestBrushUp.png");
            Texture forestBrushDown = new Texture("interface/mapEditor/forestBrushDown.png");
            Texture cancelUp = new Texture("interface/mapEditor/CancelBrushUp.png");
            Texture cancelDown = new Texture("interface/mapEditor/CancelBrushDown.png");

            imageButtonStyleForestBrush.imageUp = new TextureRegionDrawable(new TextureRegion(forestBrushUp));
            imageButtonStyleForestBrush.imageDown = new TextureRegionDrawable(new TextureRegion(forestBrushDown));
            imageButtonStyleCancel.imageUp = new TextureRegionDrawable(new TextureRegion(cancelUp));
            imageButtonStyleCancel.imageDown = new TextureRegionDrawable(new TextureRegion(cancelDown));

            imageButtonForestBrush = new ImageButton(imageButtonStyleForestBrush);
            ImageButton imageButtonCancel = new ImageButton(imageButtonStyleCancel);

            imageButtonForestBrush.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    imageButtonBrush.setStyle(imageButtonStyleForestBrush);
                    mapEdit.drawingType = MapEdit.DrawingType.forestDraw;
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
        }
    }
}
