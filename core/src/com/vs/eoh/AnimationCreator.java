/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vs.enums.AnimsTypes;

/**
 *
 * @author v
 */
public class AnimationCreator {

    public Animation makeAniamtion(AnimsTypes animations) {

        Texture texture;
        TextureRegion[][] tmp;
        TextureRegion[] walkFrames = null;
        int index = 0;

        switch (animations) {

            case FireExplosionAnimation:
                //Texture texture = new Texture(Gdx.files.internal("animation/texExplosionFire.png"));
                texture = new Texture(Gdx.files.internal("animation/texExplosionFire.png"));
                //TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 5);
                tmp = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 5);
                //TextureRegion[] walkFrames = new TextureRegion[8 * 5];
                walkFrames = new TextureRegion[8 * 5];
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 8; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case SlashAnimation:
                texture = new Texture(Gdx.files.internal("animation/texSlash.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 2);
                walkFrames = new TextureRegion[5 * 2];
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 5; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case UseMixtureAnimation:
                texture = new Texture(Gdx.files.internal("animation/texMixtureUse.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 11, texture.getHeight() / 7);
                walkFrames = new TextureRegion[11 * 7];
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 11; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case GoodSpellAnimation:
                texture = new Texture(Gdx.files.internal("animation/texGoodSpell.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 4);
                walkFrames = new TextureRegion[8 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 8; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case BadSpellAnimation:
                texture = new Texture(Gdx.files.internal("animation/texBadSpell.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 4);
                walkFrames = new TextureRegion[8 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 8; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case SelfSpellAnimation:
                break;
        }

        Animation animation = new Animation(0.05f, walkFrames);
        return animation;
    }
}
