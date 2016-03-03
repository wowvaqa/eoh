package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.eoh.Assets;
import com.vs.eoh.GameStatus;

public class MainScreen implements Screen {
    private final Game g;                                                       // referencja do instancji gry
    private final Assets a;                                                     // obiekt assetów
    private final GameStatus gs;                                                // status gry

    private final Stage stage01 = new Stage();                                  // scena najniższa

    private final OrthographicCamera c;
    private final FitViewport viewPort;

    // deklaracja przycisków
    private TextButton btnNowaGra;
    private TextButton btnMultiplayer;
    private TextButton btnMapa;
    private TextButton btnOptions;
    private TextButton btnTest;
    private TextButton btnExit;

    // deklaracja labeli
    private Label lblMenuGlowne;

    // deklaracja tabeli
    private final Table tabela = new Table();

    public MainScreen(Game g, Assets a, GameStatus gs) {
        this.g = g;
        this.a = a;
        this.gs = gs;

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        tabela.setDebug(true);

        // dodaje label do tabeli
        lblMenuGlowne = new Label("MENU GLOWNE", a.skin);
        tabela.add(lblMenuGlowne).align(Align.top);
        tabela.row();

        stage01.addActor(tabela);
    }

    private void dodajPrzyciski() {
        // Dodaje przycisk nowa gra
        btnNowaGra = new TextButton("NOWA GRA", a.skin);
        //btnNowaGra.setPosition(1, 500);
        btnNowaGra.setSize(200, 100);

        btnNowaGra.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gs.setActualScreen(3);
                g.setScreen(Assets.newGameScreen);
            }
        });
        // Dodaje przycisk mapa
        btnMapa = new TextButton("MAPA", a.skin);
        //btnMapa.setPosition(1, 380);
        btnMapa.setSize(200, 100);
        btnMapa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gs.setActualScreen(1);
                g.setScreen(Assets.mapScreen);
            }
        });
        // Dodaje przycisk opcje
        btnOptions = new TextButton("OPCJE", a.skin);
        //btnOptions.setPosition(1, 200);
        btnOptions.setSize(200, 100);
        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gs.setActualScreen(4);
            }
        });

        btnTest = new TextButton("Funkce Testowe", a.skin);
        //btnTest.setPosition(1, 100);
        btnTest.setSize(200, 100);
        btnTest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                g.setScreen(Assets.testScreen);
            }
        });

        // Dodaje przycisk Exit
        btnExit = new TextButton("EXIT", a.skin);
        //btnExit.setPosition(1, 1);
        btnExit.setSize(200, 100);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        btnMultiplayer = new TextButton("Multiplayer", a.skin);
        btnMultiplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Press: ", "btnMultiplayer");
                g.setScreen(Assets.multiplayerScreen);
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

