package com.vs.mapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.V;

/**
 * Created by v on 17.07.16.
 * Class represents visuality of map editor.
 */
public class MapEditScreen implements Screen {

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

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (mapStage != null) {
            mapStage.act();
            mapStage.draw();
        }

        interfaceStage.act();
        interfaceStage.draw();

        Gdx.input.setInputProcessor(interfaceStage);
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


        public Interface() {
            makeButtons();
        }

        /**
         * Return load map window.
         *
         * @return Window
         */
        public Window getLoadMapWindow() {
            return null;
        }

        /**
         * Return load map window
         *
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
                    mapStage = mapEdit.createStage(
                            Integer.parseInt(tfAmountOfXflilds.getText()),
                            Integer.parseInt(tfAmountOfYflilds.getText())
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
                    interfaceStage.addActor(window);
                }
            });
        }
    }
}
