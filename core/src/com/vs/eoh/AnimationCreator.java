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

        float animationSpeed = 0.05f;

        Texture texture;
        TextureRegion[][] tmp;
        TextureRegion[] walkFrames = null;
        int index = 0;

        switch (animations) {

            case DefenceTower:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/defenceTower.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case TreningCamp:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/treningCamp.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case SpeedTower:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/speedTower.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case TowerOfMagicPower:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/towerOfMagicPower.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case ObeliskOfWisdomAnimation:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/obeliskOfWisdom.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case ZombieAnimation:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/zombie.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case SpiderAnimation:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/spider.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case SkeletonAnimation:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/skeleton.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case WolfAnimation:
                animationSpeed = 0.1f;
                texture = new Texture(Gdx.files.internal("animation/wolf.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 4);
                walkFrames = new TextureRegion[6 * 4];
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 6; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case ThunderSpellAnimation:
                texture = new Texture(Gdx.files.internal("animation/texThunderExplosion.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 3);
                walkFrames = new TextureRegion[5 * 3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 5; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case FrozenSpellAnimation:
                texture = new Texture(Gdx.files.internal("animation/Ice1-1.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 6);
                walkFrames = new TextureRegion[5 * 6];
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        walkFrames[index++] = tmp[i][j];
                    }
                }
                break;

            case FireExplosionAnimation:
                texture = new Texture(Gdx.files.internal("animation/texExplosionFire.png"));
                tmp = TextureRegion.split(texture, texture.getWidth() / 8, texture.getHeight() / 5);
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

        Animation animation = new Animation(animationSpeed, walkFrames);
        return animation;
    }
}
