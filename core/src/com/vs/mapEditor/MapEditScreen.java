package com.vs.mapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
