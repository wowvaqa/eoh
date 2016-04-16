package com.vs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.Assets;
import com.vs.network.NetEngine;

/**
 * Created by v on 2016-04-16.
 */
public class GameOverScreen implements Screen {

    private final OrthographicCamera c;
    private final FitViewport viewPort;
    private final NetEngine ne;
    public Stage mainStage;
    public Tables tables;
    public Interface interfce;

    public GameOverScreen(NetEngine ne) {
        this.ne = ne;

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);

        mainStage = new Stage();
        interfce = new Interface();
        tables = new Tables();
    }

    @Override
    public void show() {
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
    }

    public class Tables {
        public Table tableMain = new Table();

        /**
         * Formatuje główną tabelę
         */
        public void formatMainTable() {
            tableMain.add(interfce.btnExit).size(100, 50).pad(5);
        }
    }

    public class Interface {
        public TextButton btnExit = new TextButton("EXIT", ne.a.skin);

        public Interface() {
            Listeners listeners = new Listeners();
            listeners.addListners();
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
                addListenerBtnExit();
            }

            /**
             * Dodaje listenera do przycisku wyjścia z Screenu MS
             */
            private void addListenerBtnExit() {
                btnExit.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ne.g.setScreen(Assets.mainMenuScreen);
                    }
                });
            }
        }
    }
}
