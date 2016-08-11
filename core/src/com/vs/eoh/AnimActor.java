/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vs.eoh;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Klasa odpowiada za animowanych aktorów
 *
 * @author v
 */
public class AnimActor extends Image {

    protected Animation animation = null;
    private boolean loop = true;
    private float stateTime = 0;

    /**
     * @param animation
     */
    public AnimActor(Animation animation, boolean isLooped) {

        super(animation.getKeyFrame(0));
        this.animation = animation;
        loop = isLooped;

        this.setSize(100, 100);
    }

    public AnimActor() {

    }

//    public static AnimActor createAnimateActor(Animation animation){
//        AnimActor animActor = new AnimActor(animation, true);
//        return animActor;
//    }

    @Override
    public void act(float delta) {

        if (loop) {
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
            super.act(delta);
        } else {
            if (!animation.isAnimationFinished(stateTime)) {
                ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
                super.act(delta);
            } else {
                this.remove();
            }
        }
    }

    /**
     * Returns True if the animation is looped
     *
     * @return
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Setup animation loop. True if animation is looped.
     *
     * @param loop
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
