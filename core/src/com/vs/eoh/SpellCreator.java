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

    private final Assets a;
    private final GameStatus gs;

    public SpellCreator(Assets a, GameStatus gs) {
        this.a = a;
        this.gs = gs;
    }

    /**
     * Tworzy zaklęcie
     *
     * @param spells Rodzaj zaklęcia z enum Spells
     * @param bohater
     * @return Spell Actor
     */
    public SpellActor utworzSpell(Spells spells, Bohater bohater) {

        SpellActor spell = new SpellActor(a.texFist, 0, 0, bohater, a, gs);

        switch (spells) {

            case FireBall:
                spell.getSprite().setTexture(a.texSpellFireBall);
                spell.setDmg(3);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setEfektDmg(3);
                spell.setRodzajCzaru(spells);
                break;

            case Frozen:
                spell.getSprite().setTexture(a.texSpellFreez);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellFreez, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.setKoszt(1);
                spell.getSpellEffects().get(0).setOpis("Spowolnienie spowodowane schłodzeniem");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Chlod", a.skin, "Spowolnienie spowodowane schłodzeniem", Assets.stage01MapScreen);
                    }
                });
                break;

            case Rage:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellRage);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellRage, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.setKoszt(1);
                spell.getSpellEffects().get(0).setOpis("Zwiększenie ataku +1 do końca tury");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Gniew", a.skin, "Zwiększenie ataku +1 do końca tury", Assets.stage01MapScreen);
                    }
                });
                break;

            case Haste:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellHaste);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.setRodzajCzaru(spells);
                break;

            case Cure:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellCure);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.setRodzajCzaru(spells);
                break;

            case SongOfGlory:
                spell.setSpellWorksOnlyForPlayersHeroes(true);
                spell.getSprite().setTexture(a.texSpellSongOfGlory);
                spell.setKoszt(1);
                spell.setZasieg(5);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellSongOfGlory, 0, 0));
                spell.setRodzajCzaru(spells);
                spell.setKoszt(1);
                spell.getSpellEffects().get(0).setOpis("Zwiększenie ataku +1 do końca tury");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Piesn Chwaly", a.skin, "Zwiększenie ataku +1 do końca tury", Assets.stage01MapScreen);
                    }
                });
                break;

            case Discouragement:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellDiscouragement);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellDiscouragement, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Zmniejsza obrone przeciwnika -1 za kazdy udany atak");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Zniechecenie", a.skin, "Zmniejsza obrone przeciwnika -1 za kazdy udany atak", Assets.stage01MapScreen);
                    }
                });
                spell.setRodzajCzaru(spells);
                break;

            case Charge:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellCharge);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());

                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellCharge, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Kazdy udany atak zadaje dodatkowo +1 do obrazen.");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Szarza", a.skin, "Kazdy udany atak zadaje dodatkowo +1 do obrazen.", Assets.stage01MapScreen);
                    }
                });

                spell.setRodzajCzaru(spells);
                break;

            case Fury:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellFury);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());
                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellFury, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Kazdy udany atak zwiększa atak bohatera +2 redukując obronę -1.");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Furia", a.skin, "Kazdy udany atak zwiększa atak bohatera +2 redukując obronę -1.", Assets.stage01MapScreen);
                    }
                });
                spell.setRodzajCzaru(spells);
                break;

            case FinalJudgment:
                spell.setSpellWorksOnlyForCaster(true);
                spell.getSprite().setTexture(a.texSpellFinalJudgment);
                spell.setKoszt(1);
                spell.setSpellEffects(new ArrayList<SpellEffects>());
                spell.getSpellEffects().add(new SpellEffects());

                spell.getSpellEffects().get(0).setIkona(new EffectActor(a.texSpellFinalJudgment, 0, 0));
                spell.getSpellEffects().get(0).setOpis("Kazdy udany atak zmniejsza Obronę przeciwnika -2 i zwiększa Atak +1.");
                spell.getSpellEffects().get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Final Judgment", a.skin, "Kazdy udany atak zmniejsza Obronę przeciwnika -2 i zwiększa Atak +1.", Assets.stage01MapScreen);
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
                return a.texSpellFury;
            case Charge:
                return a.texSpellCharge;
            case Discouragement:
                return a.texSpellDiscouragement;
            case Cure:
                return a.texSpellCure;
            case FinalJudgment:
                return a.texSpellFinalJudgment;
            case FireBall:
                return a.texSpellFireBall;
            case Frozen:
                return a.texSpellFreez;
            case Haste:
                return a.texSpellHaste;
            case Rage:
                return a.texSpellRage;
            case SongOfGlory:
                return a.texSpellSongOfGlory;
        }
        return a.texStick;
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
