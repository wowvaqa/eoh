/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vs.eoh;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.Spells;
import com.vs.screens.DialogScreen;

import java.util.ArrayList;

/**
 * Kalsa tworzy zaklęcia
 *
 * @author v
 */
public class SpellCreator {

    private V v;

    public SpellCreator(V v) {
        this.v = v;
    }

    /**
     * Tworzy zaklęcie
     *
     * @param spells Rodzaj zaklęcia z enum Spells
     * @param bohater
     * @return Spell Actor
     */
    public SpellActor utworzSpell(Spells spells, Bohater bohater) {

        SpellActor spell = new SpellActor(v.getA().texFist, 0, 0, bohater, v);

        switch (spells) {

            case Poison:
                spell.getSprite().setTexture(v.getA().texSpellPoison);
                spell.setDmg(1);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(1);
                spell.getSpellEffects().get(0).setPosionEffect(true);
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellPoison, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Po zadaja obrazenia po kazdym ruchu bohatera");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Trucizna", v.getA().skin, "Trucizna", Assets.stage01MapScreen);
                    }
                });
                spell.setRodzajCzaru(spells);
                break;

            case VampireTouch:
                spell.getSprite().setTexture(v.getA().texSpellVampireTouch);
                spell.setDmg(2 + bohater.getMoc() * 2);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(2 + bohater.getMoc() * 2);
                spell.getSpellEffects().get(0).setOpis("Wampirzy dotyk zabiera Hp ofiary przekazujac ja rzucajacemu czar");
                spell.setRodzajCzaru(spells);
                break;

            case FireBall:
                spell.getSprite().setTexture(v.getA().texSpellFireBall);
                spell.setDmg(3);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(3);
                spell.setRodzajCzaru(spells);
                break;

            case LongShot:
                spell.getSprite().setTexture(v.getA().texSpellLongShot);
                spell.setDmg(5 + bohater.getMoc());
                spell.setKoszt(1);
                spell.setZasieg(bohater.getMoc());
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(5 + bohater.getMoc());
                spell.getSpellEffects().get(0).setOpis("Kula ognia");
                spell.setRodzajCzaru(spells);
                break;

            case SummonBear:
                spell.setSpellSummonSpell(true);
                spell.getSprite().setTexture(v.getA().texSpellSummonBear);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setOpis("Przyzwanie niedzwiedzia");
                spell.setRodzajCzaru(spells);
                break;

            case SummonWolf:
                spell.setSpellSummonSpell(true);
                spell.getSprite().setTexture(v.getA().texSpellSummonWolf);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setOpis("Przyzwanie wilka");
                spell.setRodzajCzaru(spells);
                break;

            case Thunder:
                spell.getSprite().setTexture(v.getA().texSpellThunder);
                spell.setDmg(7);
                spell.setKoszt(2);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(7);
                spell.getSpellEffects().get(0).setOpis("Piorun");
                spell.setRodzajCzaru(spells);
                break;

            case MeteorShower:
                spell.getSprite().setTexture(v.getA().texSpellMeteorShower);
                spell.setDmg(12);
                spell.setKoszt(3);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(7);
                spell.getSpellEffects().get(0).setOpis("Deszcz meteorow");

                spell.setRodzajCzaru(spells);
                break;

            case Frozen:
                spell.getSprite().setTexture(v.getA().texSpellFreez);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellFreez, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.setKoszt(1);
                spell.getSpellEffects().get(0).setOpis("Spowolnienie spowodowane schłodzeniem");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Chlod", v.getA().skin, "Spowolnienie spowodowane schłodzeniem", Assets.stage01MapScreen);
                    }
                });
                break;


            case Rage:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellRage);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellRage, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.setKoszt(1);
                spell.getSpellEffects().get(0).setOpis("Zwiększenie ataku +1 do końca tury");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Gniew", v.getA().skin, "Zwiększenie ataku +1 do końca tury", Assets.stage01MapScreen);
                    }
                });
                break;

            case Haste:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellHaste);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setOpis("Przyspieszenie");
                spell.setRodzajCzaru(spells);
                break;

            case Cure:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellCure);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setOpis("Leczenie");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Leczenie",
                                v.getA().skin, "Przywrocenie punktow HP", Assets.stage01MapScreen);
                    }
                });
                spell.setRodzajCzaru(spells);
                break;

            case SongOfGlory:
                spell.setSpellWorksOnlyForPlayersHeroes(true);
                spell.getSprite().setTexture(v.getA().texSpellSongOfGlory);
                spell.setKoszt(1);
                spell.setZasieg(5);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellSongOfGlory, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.getSpellEffects().get(0).setOpis("Zwiększenie ataku +1 do końca tury");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Piesn Chwaly", v.getA().skin, "Zwiększenie ataku +1 do końca tury", Assets.stage01MapScreen);
                    }
                });
                break;

            case Bless:
                spell.setSpellWorksOnlyForPlayersHeroes(true);
                spell.getSprite().setTexture(v.getA().texSpellBless);
                spell.setKoszt(1);
                spell.setZasieg(5);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellBless, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.getSpellEffects().get(0).setOpis("Zwiększa atak i obronę o 5");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Blogoslawienstwo", v.getA().skin, "Zwiększa atak i obronę o 5", Assets.stage01MapScreen);
                    }
                });
                break;

            case Prayer:
                spell.setSpellWorksOnlyForPlayersHeroes(true);
                spell.getSprite().setTexture(v.getA().texSpellPrayer);
                spell.setKoszt(1);
                spell.setZasieg(5);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellPrayer, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.getSpellEffects().get(0).setOpis("Zwiększa atak i obronę o 7, przywracając szybkość i punkty HP");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Modlitwa", v.getA().skin, "Zwiększa atak i obronę o 7, przywracając szybkość i punkty HP", Assets.stage01MapScreen);
                    }
                });
                break;


            case Discouragement:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellDiscouragement);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellDiscouragement, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Zmniejsza obrone przeciwnika -1 za kazdy udany atak");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Zniechecenie", v.getA().skin, "Zmniejsza obrone przeciwnika -1 za kazdy udany atak", Assets.stage01MapScreen);
                    }
                });
                spell.setRodzajCzaru(spells);
                break;

            case Charge:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellCharge);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());

                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellCharge, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Kazdy udany atak zadaje dodatkowo +1 do obrazen.");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Szarza", v.getA().skin, "Kazdy udany atak zadaje dodatkowo +1 do obrazen.", Assets.stage01MapScreen);
                    }
                });

                spell.setRodzajCzaru(spells);
                break;

            case Fury:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellFury);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellFury, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Kazdy udany atak zwiększa atak bohatera +2 redukując obronę -1.");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Furia", v.getA().skin, "Kazdy udany atak zwiększa atak bohatera +2 redukując obronę -1.", Assets.stage01MapScreen);
                    }
                });
                spell.setRodzajCzaru(spells);
                break;

            case FinalJudgment:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(v.getA().texSpellFinalJudgment);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());

                spell.getSpellEffects().get(0).setIkona(new EffectActor(v.getA().texSpellFinalJudgment, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Kazdy udany atak zmniejsza Obronę przeciwnika -2 i zwiększa Atak bohatera o +1.");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Final Judgment", v.getA().skin, "Kazdy udany atak zmniejsza Obronę przeciwnika -2 i zwiększa Atak bohatera o +1.", Assets.stage01MapScreen);
                    }
                });

                spell.setRodzajCzaru(spells);
                break;
        }

        spell.setSize(50, 50);

        return spell;
    }

    /**
     * Zwraca teksture zadanego parametrem zaklęcia
     *
     * @param spellKind Enum Spells
     * @return Tekstura
     */
    public Texture getSpellTexture(Spells spellKind) {
        switch (spellKind) {
            case Fury:
                return v.getA().texSpellFury;
            case Charge:
                return v.getA().texSpellCharge;
            case Discouragement:
                return v.getA().texSpellDiscouragement;
            case Cure:
                return v.getA().texSpellCure;
            case FinalJudgment:
                return v.getA().texSpellFinalJudgment;
            case FireBall:
                return v.getA().texSpellFireBall;
            case Thunder:
                return v.getA().texSpellThunder;
            case MeteorShower:
                return v.getA().texSpellMeteorShower;
            case Bless:
                return v.getA().texSpellBless;
            case Prayer:
                return v.getA().texSpellPrayer;
            case Frozen:
                return v.getA().texSpellFreez;
            case Haste:
                return v.getA().texSpellHaste;
            case Rage:
                return v.getA().texSpellRage;
            case SongOfGlory:
                return v.getA().texSpellSongOfGlory;
            case Poison:
                return v.getA().texSpellPoison;
            case SummonWolf:
                return v.getA().texSpellSummonWolf;
            case SummonBear:
                return v.getA().texSpellSummonBear;
            case LongShot:
                return v.getA().texSpellLongShot;
            case VampireTouch:
                return v.getA().texSpellVampireTouch;
        }
        return v.getA().texStick;
    }

    public String getSpellDescription(Spells spellKind) {
        switch (spellKind) {
            case Fury:
                return "Kazdy udany atak zwiększa atak bohatera +2 redukując obronę -1.";
            case Charge:
                return "Kazdy udany atak zadaje dodatkowo +1 do obrazen.";
            case Discouragement:
                return "Zmniejsza obrone przeciwnika -1 za kazdy udany atak";
            case Cure:
                return "Leczenie";
            case FinalJudgment:
                return "Kazdy udany atak zmniejsza Obronę przeciwnika -2 i zwiększa Atak +1.";
            case FireBall:
                return "Fireball";
            case Thunder:
                return "Razi przeciwnika piorunem";
            case Frozen:
                return "Frozen";
            case Haste:
                return "Haste";
            case Rage:
                return "Zwiększenie ataku +1 do końca tury";
            case SongOfGlory:
                return "Song of Glory";
        }
        return "Brak opisu";
    }
}
