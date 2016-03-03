/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.eoh.Assets;
import com.vs.eoh.GameStatus;

/**
 * Klasa odpowiada za wyświetlanie Screenu z informacjami o itemkach
 *
 * @author v
 */
public class ItemScreen implements Screen {

    private final Assets a;
    private final GameStatus gs;
    private final Game g;

    private final Stage stage01 = new Stage();

    private final Table tabela;

    private final TextButton btnExit;

    private boolean itemScreenShow = false;

    /**
     *
     * @param a Assety
     * @param gs Status gry
     * @param g
     */
    public ItemScreen(Assets a, final GameStatus gs, final Game g) {
        this.g = g;
        this.a = a;
        this.gs = gs;
        this.tabela = new Table(a.skin);

        btnExit = new TextButton("Exit", this.a.skin);
        btnExit.addListener(
                new ClickListener() {
                    GameStatus tmpGs = gs;

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //tmpGs.setActualScreen(tmpGs.getLastScreen());                        
                        itemScreenShow = false;
                        tabela.clear();
                        g.setScreen(Assets.lastScreen);
                    }
                }
        );
    }

    private void formatujTabele() {
        Label label = new Label(gs.getItem().getNazwa(), a.skin);

        tabela.setFillParent(true);
        // ustawia odstęp od krawędzi tabeli
        tabela.pad(10);
        // włacza linie debugujące tabelę
        tabela.setDebug(true);
        tabela.add(label).colspan(2);
        tabela.row();
        tabela.add(new Label("Atak:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getAtak()), a.skin)).expandX();
        tabela.row();
        tabela.add(new Label("Obrona:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getObrona()), a.skin)).expandX();
        tabela.row();
        tabela.add(new Label("Szybkosc:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getSzybkosc()), a.skin)).expandX();
        tabela.row();
        tabela.add(new Label("HP:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getHp()), a.skin)).expandX();
        tabela.row();
        tabela.add(new Label("Obrazenia:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getDmg()), a.skin)).expandX();
        tabela.row();
        tabela.add(new Label("Pancerz:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getArmor()), a.skin)).expandX();
        tabela.row();
        

        if (gs.getItem().getZasieg() > 0) {
            tabela.add(new Label("Zasieg:", a.skin)).expandX();
            tabela.add(new Label((CharSequence) Integer.toString(gs.getItem().getZasieg()), a.skin)).expandX();
            tabela.row();
        }
        
        tabela.add(new Label("Opis:", a.skin)).expandX();
        tabela.add(new Label((CharSequence) gs.getItem().getOpis(), a.skin)).expandX();
        tabela.row();

        tabela.add(btnExit).colspan(2);
    }

    @Override
    public void show() {
        if (!itemScreenShow) {
            Gdx.input.setInputProcessor(stage01);

            formatujTabele();

            stage01.addActor(tabela);
            itemScreenShow = true;
        }
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
