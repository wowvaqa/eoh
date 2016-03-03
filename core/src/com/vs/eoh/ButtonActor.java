package com.vs.eoh;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ButtonActor extends Actor {

    private final Sprite sprite;
    //private boolean dotkniety = false;
    //private GameStatus gs;
    private boolean klikniety;

    public ButtonActor(Texture tekstura, int x, int y) {
        sprite = new Sprite(tekstura);
        this.setSize(sprite.getWidth(), sprite.getHeight());
        this.setPosition(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
    
    // Setters and Getters

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isKlikniety() {
        return klikniety;
    }

    public void setKlikniety(boolean klikniety) {
        this.klikniety = klikniety;
    }
    
    
    
}
