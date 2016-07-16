/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.eoh.V;

/**
 * Klasa odpowiada za wyświetlanie Screenu z informacjami o itemkach
 *
 * @author v
 */
public class ItemScreen implements Screen {

    private final Stage stage01 = new Stage();
    private final Table tabela;
    private final TextButton btnExit;
    private V v;
    private boolean itemScreenShow = false;

    /**
     *
     */
    //public ItemScreen(Assets a, final GameStatus gs, final Game g) {
    public ItemScreen(final V v) {
        this.v = v;
        //this.g = g;
        //this.a = a;
        //this.gs = gs;
        this.tabela = new Table(v.getA().skin);

        btnExit = new TextButton("Exit", this.v.getA().skin);
        btnExit.addListener(
                new ClickListener() {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        itemScreenShow = false;
                        tabela.clear();
                        v.getG().setScreen(v.getLastScreen());
                    }
                }
        );
    }

    private void formatujTabele() {
        Label label = new Label(v.getGs().getItem().getNazwa(), v.getA().skin);

        tabela.setFillParent(true);
        // ustawia odstęp od krawędzi tabeli
        tabela.pad(10);
        // włacza linie debugujące tabelę
        tabela.setDebug(true);
        tabela.add(label).colspan(3);
        tabela.row();
        tabela.add(new Image(v.getA().texAtcIcon)).size(25, 25);
        tabela.add(new Label("Atak:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getAtak()), v.getA().skin)).expandX();
        tabela.row();
        tabela.add(new Image(v.getA().texDefIcon)).size(25, 25);
        tabela.add(new Label("Obrona:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getObrona()), v.getA().skin)).expandX();
        tabela.row();
        tabela.add(new Image(v.getA().texSpdIcon)).size(25, 25);
        tabela.add(new Label("Szybkosc:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getSzybkosc()), v.getA().skin)).expandX();
        tabela.row();
        tabela.add(new Image(v.getA().texHpIcon)).size(25, 25);
        tabela.add(new Label("HP:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getHp()), v.getA().skin)).expandX();
        tabela.row();
        tabela.add(new Image(v.getA().texDmgIcon)).size(25, 25);
        tabela.add(new Label("Obrazenia:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getDmg()), v.getA().skin)).expandX();
        tabela.row();
        tabela.add(new Image(v.getA().texArmIcon)).size(25, 25);
        tabela.add(new Label("Pancerz:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getArmor()), v.getA().skin)).expandX();
        tabela.row();


        if (v.getGs().getItem().getZasieg() > 0) {
            tabela.add(new Label("Zasieg:", v.getA().skin)).expandX();
            tabela.add(new Label((CharSequence) Integer.toString(v.getGs().getItem().getZasieg()), v.getA().skin)).expandX();
            tabela.row();
        }

        tabela.add(new Label("Opis:", v.getA().skin)).expandX();
        tabela.add(new Label((CharSequence) v.getGs().getItem().getOpis(), v.getA().skin)).expandX();
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
