package com.vs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.V;

public class MainScreen implements Screen {
    private final Stage stage01 = new Stage();                                  // scena najniższa
    private final FitViewport viewPort;
    // deklaracja tabeli
    private final Table tabela = new Table();
    private V v;
    // deklaracja przycisków
    private TextButton btnNowaGra;
    private TextButton btnMultiplayer;
    private TextButton btnMapa;
    private TextButton btnOptions;
    private TextButton btnTest;
    private TextButton btnExit;
    // deklaracja labeli
    private Label lblMenuGlowne;

    //public MainScreen(Game g, Assets a, GameStatus gs) {
    public MainScreen(V v) {
        this.v = v;

        OrthographicCamera c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);

        formatujTabele();
        dodajPrzyciski();
    }

    private void formatujTabele() {

        // ustawia rozmiar tebeli na cały ekran
        tabela.setFillParent(true);
        // ustawia odstęp od krawędzi tabeli
        tabela.pad(10);
        // włacza linie debugujące tabelę
        tabela.setDebug(false);

        Image img = new Image(v.getA().texMainPic);
        tabela.setBackground(img.getDrawable());

        // dodaje label do tabeli
        lblMenuGlowne = new Label("MENU GLOWNE", v.getA().skin);
        tabela.add(lblMenuGlowne).align(Align.top);
        tabela.row();

        stage01.addActor(tabela);
    }

    private void dodajPrzyciski() {
        // Dodaje przycisk nowa gra
        btnNowaGra = new TextButton("NOWA GRA", v.getA().skin);
        //btnNowaGra.setPosition(1, 500);
        btnNowaGra.setSize(200, 100);

        btnNowaGra.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gs.setActualScreen(3);

                v.getG().setScreen(v.getNewGameScreen());
            }
        });
        // Dodaje przycisk mapa
        btnMapa = new TextButton("MAPA", v.getA().skin);
        //btnMapa.setPosition(1, 380);
        btnMapa.setSize(200, 100);
        btnMapa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gs.setActualScreen(1);
                v.getG().setScreen(v.getMapScreen());
            }
        });
        // Dodaje przycisk opcje
        btnOptions = new TextButton("Map Editor", v.getA().skin);
        //btnOptions.setPosition(1, 200);
        btnOptions.setSize(200, 100);
        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getG().setScreen(v.getMapEditScreen());
            }
        });

        btnTest = new TextButton("Funkce Testowe", v.getA().skin);
        //btnTest.setPosition(1, 100);
        btnTest.setSize(200, 100);
        btnTest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getG().setScreen(v.getTestScreen());
            }
        });

        // Dodaje przycisk Exit
        btnExit = new TextButton("EXIT", v.getA().skin);
        //btnExit.setPosition(1, 1);
        btnExit.setSize(200, 100);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        btnMultiplayer = new TextButton("Multiplayer", v.getA().skin);
        btnMultiplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Press: ", "btnMultiplayer");
                v.getG().setScreen(v.getMultiplayerScreen());
            }
        });

        // Dodanie przycisków do tabeli
        tabela.add(btnNowaGra).width(200).height(50).space(5);
        tabela.row();
        tabela.add(btnMultiplayer).width(200).height(50).space(5);
        tabela.row();
        tabela.add(btnMapa).width(200).height(50).space(5);
        tabela.row();
        tabela.add(btnOptions).width(200).height(50).space(5);
        tabela.row();
        tabela.add(btnTest).width(200).height(50).space(5);
        tabela.row();
        tabela.add(btnExit).width(200).height(50).space(25);
        tabela.row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage01);
    }

    @Override
    public void render(float delta) {
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
    }
}

