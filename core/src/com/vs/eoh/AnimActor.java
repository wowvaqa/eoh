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
    AnimationController animationController;
    private float stateTime = 0;

    /**
     *
     * @param animation
     */
    public AnimActor(Animation animation) {

        super(animation.getKeyFrame(0));
        this.animation = animation;
 
        this.setSize(100, 100);
    }

    public AnimActor() {

    }

    @Override
    public void act(float delta) {


//        if (!animation.isAnimationFinished(stateTime)) {
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
            super.act(delta);
//        } else {
//            this.remove();
//        }
    }
}
