package com.vs.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.vs.enums.ActionModes;
import com.vs.enums.CzesciCiala;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.SpellEffects;
import com.vs.enums.Spells;
import com.vs.eoh.Animation;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.Castle;
import com.vs.eoh.Fight;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Gracz;
import com.vs.eoh.Item;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.NewGame;
import com.vs.eoh.Pole;
import com.vs.eoh.Ruch;
import com.vs.eoh.SpellActor;
import com.vs.eoh.SpellCaster;
import com.vs.eoh.SpellCreator;
import com.vs.eoh.TresureBox;
import com.vs.eoh.V;

import java.util.ArrayList;
import java.util.Random;

import static com.vs.eoh.NewGame.klasaPostaciGracz01;
import static com.vs.eoh.NewGame.pobierzAtak;
import static com.vs.eoh.NewGame.pobierzHp;
import static com.vs.eoh.NewGame.pobierzMoc;
import static com.vs.eoh.NewGame.pobierzObrone;
import static com.vs.eoh.NewGame.pobierzSzybkosc;
import static com.vs.eoh.NewGame.pobierzWiedze;

/**
 * Created by v on 2016-05-13.
 */
public class AI {

    public static AI ai0;
    public static AI ai1;
    public static AI ai2;
    public static AI ai3;

    public static ArrayList<AI> aiList;

    public static int whichMove = 0;
    public static long nextMove;
    public static boolean aiStarted = false;

    private ActionModes actionMode;

    private Gracz gracz;

    private V v;

    /**
     * Tworzy AI dla gracza.
     *
     * @param gracz Referencja do obiektu gracza.
     */
    public AI(Gracz gracz, V v) {
        this.v = v;
        //actionMode = ActionModes.moveM;
        this.gracz = gracz;
        Gdx.app.log("AI START", "" + gracz.toString());
    }

    /**
     * Ai wykonuje ruch.
     */
    public void makeAIMove() {

        if (gracz.getGold() >= GameStatus.CostOfNewHero) {
            buyNewHero(gracz);
        }

        for (Bohater bohater : gracz.getBohaterowie()) {
            if (bohater.getPozostaloRuchow() > 0) {

                Mapa map = bohater.getGs().getMapa();
                int xMov;
                int yMov;

                switch (getAiMode(bohater)) {
                    case wait:
                        break;
                    case moveToMobLevel1:
                        Pole startField = map.getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()];
                        Pole endField = setTarget(bohater, ActionModes.moveToMobLevel1);

                        ArrayList<PathMoves> moves = PathFinder.findPath(map, startField, endField);

                        if (moves != null && moves.size() > 0) {
                            xMov = moves.get(0).moveX;
                            yMov = moves.get(0).moveY;

                            makeHeroMove(bohater, xMov, yMov);
                        }

                        break;
                    case attackMobLevel1:
                        endField = setTarget(bohater, ActionModes.moveToMobLevel1);

                        for (int i = 0; i < map.getIloscPolX(); i++) {
                            for (int j = 0; j < map.getIloscPolY(); j++) {
                                if (map.getPola()[i][j].equals(endField)) {
                                    makeHeroAttack(bohater, i, j);
                                }
                            }
                        }
                        break;

                    case moveToMobLevel2:
                        startField = map.getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()];
                        endField = setTarget(bohater, ActionModes.moveToMobLevel2);

                        moves = PathFinder.findPath(map, startField, endField);

                        if (moves != null && moves.size() > 0) {
                            xMov = moves.get(0).moveX;
                            yMov = moves.get(0).moveY;

                            makeHeroMove(bohater, xMov, yMov);
                        }

                        break;

                    case attackMobLevel2:
                        endField = setTarget(bohater, ActionModes.moveToMobLevel2);

                        for (int i = 0; i < map.getIloscPolX(); i++) {
                            for (int j = 0; j < map.getIloscPolY(); j++) {
                                if (map.getPola()[i][j].equals(endField)) {
                                    makeHeroAttack(bohater, i, j);
                                }
                            }
                        }
                        break;

                    case moveToHero:
                        startField = map.getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()];
                        endField = setTarget(bohater, ActionModes.moveToHero);

                        moves = PathFinder.findPath(map, startField, endField);

                        if (moves != null && moves.size() > 0) {
                            xMov = moves.get(0).moveX;
                            yMov = moves.get(0).moveY;

                            makeHeroMove(bohater, xMov, yMov);
                        }
                        break;

                    case attackHero:
                        endField = setTarget(bohater, ActionModes.attackHero);

                        for (int i = 0; i < map.getIloscPolX(); i++) {
                            for (int j = 0; j < map.getIloscPolY(); j++) {
                                if (map.getPola()[i][j].equals(endField)) {
                                    makeHeroAttack(bohater, i, j);
                                }
                            }
                        }
                        break;

                    case moveToCastle:
                        startField = map.getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()];
                        endField = setTarget(bohater, ActionModes.moveToCastle);

                        moves = PathFinder.findPath(map, startField, endField);

                        if (moves != null && moves.size() > 0) {
                            xMov = moves.get(0).moveX;
                            yMov = moves.get(0).moveY;

                            makeHeroMove(bohater, xMov, yMov);
                        }
                        break;

                    case attackCastle:
                        endField = setTarget(bohater, ActionModes.attackCastle);

                        for (int i = 0; i < map.getIloscPolX(); i++) {
                            for (int j = 0; j < map.getIloscPolY(); j++) {
                                if (map.getPola()[i][j].equals(endField)) {
                                    makeHeroAttack(bohater, i, j);
                                }
                            }
                        }
                        break;

                    case moveToTresureBox:
                        startField = map.getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()];
                        endField = setTarget(bohater, ActionModes.moveToTresureBox);

                        moves = PathFinder.findPath(map, startField, endField);

                        if (moves != null && moves.size() > 0) {
                            xMov = moves.get(0).moveX;
                            yMov = moves.get(0).moveY;

                            makeHeroMove(bohater, xMov, yMov);
                        }
                        break;

                    case takeTresureBox:
                        endField = setTarget(bohater, ActionModes.takeTresureBox);

                        for (Item item : endField.getTresureBox().getDostepneItemy()) {
                            if (!item.getCzescCiala().equals(CzesciCiala.gold)) {
                                bohater.getEquipment().add(item);
                            } else {
                                bohater.getGs().getGracze().get(bohater.getPrzynaleznoscDoGracza()).setGold(
                                        bohater.getGs().getGracze().get(bohater.getPrzynaleznoscDoGracza()).getGold() + item.getGold()
                                );
                            }
                        }

                        for (Item item : bohater.getEquipment()) {
                            if (item.getCzescCiala().equals(CzesciCiala.glowa)) {
                                if (item.getLevel() > bohater.getItemGlowa().getLevel()) {
                                    bohater.setItemGlowa(item);
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.korpus)) {
                                if (item.getLevel() > bohater.getItemKorpus().getLevel()) {
                                    bohater.setItemKorpus(item);
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.nogi)) {
                                if (item.getLevel() > bohater.getItemNogi().getLevel()) {
                                    bohater.setItemNogi(item);
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.stopy)) {
                                if (item.getLevel() > bohater.getItemStopy().getLevel()) {
                                    bohater.setItemStopy(item);
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.rece)) {
                                if (item.getLevel() > bohater.getItemLewaReka().getLevel()) {
                                    bohater.setItemLewaReka(item);
                                } else if (item.getLevel() > bohater.getItemPrawaReka().getLevel()) {
                                    bohater.setItemPrawaReka(item);
                                }
                            }
                        }

                        TresureBox.removeTresureBox(endField.getTresureBox(), bohater.getGs().getMapa());

                        break;
                }
            }
        }
    }

    /**
     * Metoda odpowiada za awans bohatera AI
     */
    private void heroPromotion(Bohater bohater) {

        Random rnd = new Random();
        int draw;

        switch (bohater.getKlasyPostaci()) {
            case Wojownik:
                bohater.setAtak(bohater.getAtak() + 1);
                break;
            case Lowca:
                bohater.setSzybkosc(bohater.getSzybkosc() + 1);
                break;
            case Czarodziej:
                bohater.setMoc(bohater.getMoc() + 1);
                break;
        }

        switch (bohater.getKlasyPostaci()) {
            case Wojownik:
                switch (bohater.getLevelOfExp()) {
                    case 1:
                        bohater.getListOfSpells().add(Spells.SongOfGlory);
                        bohater.setBohaterTex(new Texture("moby/warrior/1.png"));
                        bohater.setBohaterCheckTex(new Texture("moby/warrior/1z.png"));
                        bohater.aktualizujTeksture();
                        break;
                    case 2:
                        draw = rnd.nextInt(2);
                        if (draw == 1) {
                            bohater.getListOfSpells().add(Spells.Charge);
                            bohater.setBohaterTex(new Texture("moby/warrior/2A.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/warrior/2Az.png"));
                            bohater.aktualizujTeksture();
                        } else {
                            bohater.getListOfSpells().add(Spells.Discouragement);
                            bohater.setBohaterTex(new Texture("moby/warrior/2B.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/warrior/2Bz.png"));
                            bohater.aktualizujTeksture();
                        }
                        break;
                    case 3:
                        draw = rnd.nextInt(2);
                        if (draw == 1) {
                            bohater.getListOfSpells().add(Spells.Fury);
                            bohater.setBohaterTex(new Texture("moby/warrior/3A.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/warrior/3Az.png"));
                        } else {
                            bohater.getListOfSpells().add(Spells.FinalJudgment);
                            bohater.setBohaterTex(new Texture("moby/warrior/3B.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/warrior/3Bz.png"));
                            bohater.aktualizujTeksture();
                        }
                        break;
                }
                break;
            case Lowca:
                switch (bohater.getLevelOfExp()) {
                    case 1:
                        bohater.getListOfSpells().add(Spells.Poison);
                        bohater.setBohaterTex(new Texture("moby/hunter/1.png"));
                        bohater.setBohaterCheckTex(new Texture("moby/hunter/1z.png"));
                        bohater.aktualizujTeksture();
                        break;
                    case 2:
                        draw = rnd.nextInt(2);
                        if (draw == 1) {
                            bohater.getListOfSpells().add(Spells.SummonWolf);
                            bohater.setBohaterTex(new Texture("moby/hunter/2A.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/hunter/2Az.png"));
                            bohater.aktualizujTeksture();
                        } else {
                            bohater.getListOfSpells().add(Spells.SummonBear);
                            bohater.setBohaterTex(new Texture("moby/hunter/2B.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/hunter/2Bz.png"));
                            bohater.aktualizujTeksture();
                        }
                        break;
                    case 3:
                        draw = rnd.nextInt(2);
                        if (draw == 1) {
                            bohater.getListOfSpells().add(Spells.LongShot);
                            bohater.setBohaterTex(new Texture("moby/hunter/3A.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/hunter/3Az.png"));
                        } else {
                            bohater.getListOfSpells().add(Spells.VampireTouch);
                            bohater.setBohaterTex(new Texture("moby/hunter/3B.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/hunter/3Bz.png"));
                            bohater.aktualizujTeksture();
                        }
                        break;
                }
                break;
            case Czarodziej:
                switch (bohater.getLevelOfExp()) {
                    case 1:
                        bohater.getListOfSpells().add(Spells.Frozen);
                        bohater.setBohaterTex(new Texture("moby/wizard/1.png"));
                        bohater.setBohaterCheckTex(new Texture("moby/wizard/1z.png"));
                        bohater.aktualizujTeksture();
                        break;
                    case 2:
                        draw = rnd.nextInt(2 + 1);
                        if (draw == 1) {
                            bohater.getListOfSpells().add(Spells.Bless);
                            bohater.setBohaterTex(new Texture("moby/wizard/2A.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/wizard/2Az.png"));
                            bohater.aktualizujTeksture();
                        } else if (draw == 2) {
                            bohater.getListOfSpells().add(Spells.Thunder);
                            bohater.setBohaterTex(new Texture("moby/wizard/2B.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/wizard/2Bz.png"));
                            bohater.aktualizujTeksture();
                        }
                        break;
                    case 3:
                        draw = rnd.nextInt(2 + 1);
                        if (draw == 1) {
                            bohater.getListOfSpells().add(Spells.Prayer);
                            bohater.setBohaterTex(new Texture("moby/wizard/3A.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/wizard/3Az.png"));
                        } else if (draw == 2) {
                            bohater.getListOfSpells().add(Spells.MeteorShower);
                            bohater.setBohaterTex(new Texture("moby/wizard/3B.png"));
                            bohater.setBohaterCheckTex(new Texture("moby/wizard/3Bz.png"));
                            bohater.aktualizujTeksture();
                        }
                        break;
                }
                break;

        }

        draw = rnd.nextInt(6 + 1);

        Gdx.app.log("DRAW: ", " " + draw);

        switch (draw) {
            case 1:
                bohater.setAtak(bohater.getAtak() + 1);
                break;
            case 2:
                bohater.setObrona(bohater.getObrona() + 1);
                break;
            case 3:
                bohater.setSzybkosc(bohater.getSzybkosc() + 1);
                break;
            case 4:
                bohater.setMoc(bohater.getMoc() + 1);
                break;
            case 5:
                bohater.setWiedza(bohater.getWiedza() + 1);
                break;
            case 6:
                bohater.setHp(bohater.getHp() + 5);
                break;
        }

        bohater.setExpToNextLevel(bohater.getExpToNextLevel() + 100);
        bohater.setLevelOfExp(bohater.getLevelOfExp() + 1);

        bohater.setMana(bohater.getWiedza());
        bohater.setActualMana(bohater.getWiedza());

    }

    /**
     * Metoda sprawdza czy bohater osiagnął następny poziom
     *
     * @param bohater
     */
    private boolean checkNextLevel(Bohater bohater) {
        if (bohater.getExp() >= bohater.getExpToNextLevel()) {
            return true;
        }
        return false;
    }

    /**
     * Metoda odpowiada za kupowanie nowego bohatera.
     */
    private void buyNewHero(Gracz gracz) {

        gracz.setGold(gracz.getGold() - GameStatus.CostOfNewHero);

        // tymczasowa tekstura przekazana do konstruktora nowego bohatera
        Texture tmpTex, tmpTexZazanaczony;
        // tymczasowa tekstura do określenia lokacji początkowej gracza
        int lokPoczatkowaX = 0, lokPoczatkowaY = 0;


        switch (gracz.getNumerGracza()) {
            case 0:
                for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                        if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP1()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                        if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP2()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                        if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP3()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                        if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP4()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
        }

        tmpTex = NewGame.getTeksturaBohatera(klasaPostaciGracz01);
        tmpTexZazanaczony = NewGame.getTeksturaBohateraZaznaczonego(klasaPostaciGracz01);

        gracz.getBohaterowie().add(new Bohater(tmpTex, tmpTexZazanaczony, lokPoczatkowaX * 100, lokPoczatkowaY * 100, 0, 0, klasaPostaciGracz01, v));

        int bohGracza = gracz.getNumerGracza();
        int wymTabBoh = gracz.getBohaterowie().size() - 1;

        // Ustala do którego gracza z tablicy graczy należy bohater
        gracz.getBohaterowie().get(wymTabBoh).setPrzynaleznoscDoGracza(gracz.getNumerGracza());

        switch (bohGracza) {
            case 0:
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 1:
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 2:
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 3:
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                v.getGs().getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
        }

        gracz.getBohaterowie().get(wymTabBoh).setAtak(pobierzAtak(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setObrona(pobierzObrone(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setHp(pobierzHp(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setActualHp(pobierzHp(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setSzybkosc(pobierzSzybkosc(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setPozostaloRuchow(pobierzSzybkosc(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setWiedza(pobierzWiedze(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setMoc(pobierzMoc(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setMana(pobierzWiedze(klasaPostaciGracz01));
        gracz.getBohaterowie().get(wymTabBoh).setActualMana(pobierzWiedze(klasaPostaciGracz01));

        if (klasaPostaciGracz01.equals(KlasyPostaci.Lowca)) {
            gracz.getBohaterowie().get(wymTabBoh).getListOfSpells().add(Spells.Haste);
        } else if (klasaPostaciGracz01.equals(KlasyPostaci.Czarodziej)) {
            gracz.getBohaterowie().get(wymTabBoh).getListOfSpells().add(Spells.FireBall);
        } else if (klasaPostaciGracz01.equals(KlasyPostaci.Wojownik)) {
            gracz.getBohaterowie().get(wymTabBoh).getListOfSpells().add(Spells.Rage);
        }


        Assets.stage01MapScreen.addActor(gracz.getBohaterowie().get(wymTabBoh));


    }

    /**
     * Zwraca tryb AI - MOVE, ATTACK, WAIT
     *
     * @param bohater Referencja do obiektu bohatera.
     * @return Zwraca odpowiedni tryb postępowania dla bohatera AI.
     */
    public ActionModes getAiMode(Bohater bohater) {

        if (findTargetHero(bohater).size() == 0) {
            if (findTargetCastle(bohater).size() > 0) {
                if (findTargetCastle(bohater).get(0).distance == 1
                        && findTargetCastle(bohater).get(0).castle.getActualHp() > 0) {
                    return ActionModes.attackCastle;
                } else {
                    return ActionModes.moveToCastle;
                }
            }
        }

        if (bohater.getActualHp() < 2) {
            return ActionModes.wait;
        } else if (findTargetTresureBox(bohater) != null && findTargetTresureBox(bohater).size() > 0
                && findTargetTresureBox(bohater).get(0).distance < 4 + bohater.getAiDistance()) {
            if (findTargetTresureBox(bohater).get(0).distance == 0) {
                return ActionModes.takeTresureBox;
            } else {
                return ActionModes.moveToTresureBox;
            }
        } else if (findTargetMobLevel1(bohater) != null && findTargetMobLevel1(bohater).size() > 0
                && findTargetMobLevel1(bohater).get(0).distance < 4 + bohater.getAiDistance()) {
            if (findTargetMobLevel1(bohater).get(0).distance == 1) {
                if (bohater.getPozostaloRuchow() <= 2) {
                    return ActionModes.wait;
                }
                return ActionModes.attackMobLevel1;
            } else {
                return ActionModes.moveToMobLevel1;
            }

        } else if (findTargetHero(bohater) != null && findTargetHero(bohater).size() > 0
                && findTargetHero(bohater).get(0).distance < 4 + bohater.getAiDistance()) {
            if (findTargetHero(bohater).get(0).distance == 1) {
                return ActionModes.attackHero;
            } else {
                return ActionModes.moveToHero;
            }
        } else if (findTargetMobLevel2(bohater) != null && findTargetMobLevel2(bohater).size() > 0
                && bohater.getLevelOfExp() >= 3) {
            if (findTargetMobLevel2(bohater).get(0).distance == 1) {
                if (bohater.getPozostaloRuchow() <= 2) {
                    return ActionModes.wait;
                }
                return ActionModes.attackMobLevel2;
            } else {
                return ActionModes.moveToMobLevel2;
            }
        } else if (findTargetCastle(bohater) != null && findTargetCastle(bohater).size() > 0
                && findTargetCastle(bohater).get(0).distance < 2 + bohater.getAiDistance()) {
            if (findTargetCastle(bohater).get(0).distance == 1 && findTargetCastle(bohater).get(0).castle.getActualHp() > 0) {
                return ActionModes.attackCastle;
            } else {
                return ActionModes.moveToCastle;
            }
        }
        bohater.setAiDistance(bohater.getAiDistance() + 1);
        return ActionModes.wait;
    }

    /**
     * Metoda zwraca pole wg zadanych parametrów
     *
     * @param bohater    Referencja do obiektu bohatera od którego ma rozpocząć się poszukiwanie ścieżki
     * @param actionMode Tryb akcji dla którego ma odbyć się poszukiwanie.
     * @return Referencja do obiektu pole
     */
    public Pole setTarget(Bohater bohater, ActionModes actionMode) {

        switch (actionMode) {
            case attackMobLevel1:
                return findTargetMobLevel1(bohater).get(0).mob.getField();
            case moveToMobLevel1:
                return findTargetMobLevel1(bohater).get(0).mob.getField();
            case attackMobLevel2:
                return findTargetMobLevel2(bohater).get(0).mob.getField();
            case moveToMobLevel2:
                return findTargetMobLevel2(bohater).get(0).mob.getField();
            case attackHero:
                return findTargetHero(bohater).get(0).bohater.getFiled();
            case moveToHero:
                return findTargetHero(bohater).get(0).bohater.getFiled();
            case moveToCastle:
                return findTargetCastle(bohater).get(0).castle.getField();
            case attackCastle:
                return findTargetCastle(bohater).get(0).castle.getField();
            case moveToTresureBox:
                return findTargetTresureBox(bohater).get(0).tresureBox.getField();
            case takeTresureBox:
                return findTargetTresureBox(bohater).get(0).tresureBox.getField();
            default:
                return null;
        }

    }

    /**
     * Wykonuje atak bohatera AI
     *
     * @param bohater     Referencja do obiektu bohatera
     * @param locXofField Lokacja X przeciwnika
     * @param locYofField Lokacja Y przeciwnika
     */
    private void makeHeroAttack(Bohater bohater, int locXofField, int locYofField) {

        castSpell(bohater, locXofField, locYofField);

        if (bohater.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
            Animation.animujLblDamage(locXofField * 100 + 50, locYofField * 100,
                    "Dmg: " + Integer.toString(Fight.getObrazenia(bohater, bohater.getGs().getMapa().getPola()[locXofField][locYofField].getMob())),
                    bohater.getA());

        } else if (bohater.getGs().getMapa().getPola()[locXofField][locYofField].getBohater() != null) {
            Animation.animujLblDamage(locXofField * 100 + 50, locYofField * 100,
                    "Dmg: " + Integer.toString(Fight.getObrazenia(bohater, bohater.getGs().getMapa().getPola()[locXofField][locYofField].getBohater())),
                    bohater.getA());
        } else if (bohater.getGs().getMapa().getPola()[locXofField][locYofField].getCastle() != null) {
            Animation.animujLblDamage(locXofField * 100 + 50, locYofField * 100,
                    "Dmg: " + Integer.toString(Fight.getObrazenia(bohater, bohater.getGs().getMapa().getPola()[locXofField][locYofField].getCastle())),
                    bohater.getA());
        }

        if (checkNextLevel(bohater)) {
            heroPromotion(bohater);
        }

        bohater.getGs().usunMartweMoby();
        Ruch.redrawMoveInterfaces(v);
    }

    /**
     * @param bohater Referencja do obiektu bohatera
     * @param x       wartość ruchu w osi X
     * @param y       wartość ruchu w osi Y
     */
    private void makeHeroMove(Bohater bohater, int x, int y) {

        v.getGs().getMapa().pola[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].setBohater(null);

        bohater.addAction(Actions.moveBy(x * 100, y * 100, 0.25f));
        bohater.setPozXnaMapie(bohater.getPozXnaMapie() + x);
        bohater.setPozYnaMapie(bohater.getPozYnaMapie() + y);

        v.getGs().getMapa().pola[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].setBohater(bohater);

        if (bohater.getFiled().getCastle() != null) {
            retakeCastle(bohater.getFiled());
        }

        bohater.setPozostaloRuchow(bohater.getPozostaloRuchow() - 1);

        Ruch.redrawMoveInterfaces(v);
    }

    /**
     * Rzuca czar przez bohatera AI
     *
     * @param bohater     Referencja do obiektu bohatera rzucającego czar
     * @param locXofField lokacja X przeciwnika.
     * @param locYofField lokacja Y przeciwnika.
     */
    private void castSpell(Bohater bohater, int locXofField, int locYofField) {
        for (Spells spl : bohater.getListOfSpells()) {

            boolean checkFinalJudgment = false;
            for (com.vs.eoh.SpellEffects se : bohater.getSpellEffects()) {
                if (se.getSpellEffect().equals(SpellEffects.FinalJudgment)) {
                    checkFinalJudgment = true;
                }
            }
            if (!checkFinalJudgment) {
                if (spl.equals(Spells.FinalJudgment)) {
                    SpellCreator sc = new SpellCreator(v);
                    SpellActor sa = sc.utworzSpell(spl, bohater);
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            boolean checkFury = false;
            for (com.vs.eoh.SpellEffects se : bohater.getSpellEffects()) {
                if (se.getSpellEffect().equals(SpellEffects.Fury)) {
                    checkFury = true;
                }
            }
            if (!checkFury) {
                if (spl.equals(Spells.Fury)) {
                    SpellCreator sc = new SpellCreator(v);
                    SpellActor sa = sc.utworzSpell(spl, bohater);
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            boolean checkDiscouragement = false;
            for (com.vs.eoh.SpellEffects se : bohater.getSpellEffects()) {
                if (se.getSpellEffect().equals(SpellEffects.Discouragement)) {
                    checkDiscouragement = true;
                }
            }
            if (!checkDiscouragement) {
                if (spl.equals(Spells.Discouragement)) {
                    SpellCreator sc = new SpellCreator(v);
                    SpellActor sa = sc.utworzSpell(spl, bohater);
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            boolean checkCharge = false;
            for (com.vs.eoh.SpellEffects se : bohater.getSpellEffects()) {
                if (se.getSpellEffect().equals(SpellEffects.Charge)) {
                    checkCharge = true;
                }
            }
            if (!checkCharge) {
                if (spl.equals(Spells.Charge)) {
                    SpellCreator sc = new SpellCreator(v);
                    SpellActor sa = sc.utworzSpell(spl, bohater);
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            boolean checkRage = false;
            for (com.vs.eoh.SpellEffects se : bohater.getSpellEffects()) {
                if (se.getSpellEffect().equals(SpellEffects.Rage)) {
                    checkRage = true;
                }
            }
            if (!checkRage) {
                if (spl.equals(Spells.Rage)) {
                    SpellCreator sc = new SpellCreator(v);
                    SpellActor sa = sc.utworzSpell(spl, bohater);
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            if (spl.equals(Spells.MeteorShower)) {
                SpellCreator sc = new SpellCreator(v);
                SpellActor sa = sc.utworzSpell(spl, bohater);
                if (v.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
                    sa.getSpellEffects().get(0).dzialanie(sa, v.getGs().getMapa().getPola()[locXofField][locYofField].getMob(), bohater, v);
                }
            }

            if (spl.equals(Spells.Prayer)) {
                SpellCreator sc = new SpellCreator(v);
                SpellActor sa = sc.utworzSpell(spl, bohater);
                if (v.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            if (spl.equals(Spells.Thunder)) {
                SpellCreator sc = new SpellCreator(v);
                SpellActor sa = sc.utworzSpell(spl, bohater);
                if (v.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
                    sa.getSpellEffects().get(0).dzialanie(sa, v.getGs().getMapa().getPola()[locXofField][locYofField].getMob(), bohater, v);
                }
            }

            if (spl.equals(Spells.Bless)) {
                SpellCreator sc = new SpellCreator(v);
                SpellActor sa = sc.utworzSpell(spl, bohater);
                if (v.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
                    sa.getSpellEffects().get(0).dzialanie(sa, bohater, bohater, v);
                }
            }

            if (spl.equals(Spells.FireBall)) {
                SpellCreator sc = new SpellCreator(v);
                SpellActor sa = sc.utworzSpell(spl, bohater);
                if (v.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
                    sa.getSpellEffects().get(0).dzialanie(sa, v.getGs().getMapa().getPola()[locXofField][locYofField].getMob(), bohater, v);
                }
            }
        }
    }

    /**
     * Zmienia właściciela zamku na gracza do którego należy bohater.
     *
     * @param field Pole na którym znajduje się bohater.
     */
    private void retakeCastle(Pole field) {
        field.getCastle().setPrzynaleznoscDoGracza(field.getBohater().getPrzynaleznoscDoGracza());
        field.getCastle().aktualizujIkoneZamku();
    }

    /**
     * Zwraca ArrayList z obiektem klasy HeroCell zawierającym wszystkich bohaterów poza bohaterem
     * wg którego następuje porównanie oraz
     * dystansem od bohatera
     *
     * @param bohater Referencja do obiektu bohatera wg. którego mają być sprawdzani inni bohaterowie
     * @return Obiekt klasy HeroCell
     */
    public ArrayList<HeroCell> findTargetHero(Bohater bohater) {
        Mapa map = bohater.getGs().getMapa();
        ArrayList<HeroCell> listHero = new ArrayList<HeroCell>();

        for (int i = 0; i < map.getIloscPolX(); i++) {
            for (int j = 0; j < map.getIloscPolY(); j++) {
                if (map.getPola()[i][j].getBohater() != null) {
                    if (map.getPola()[i][j].getBohater().getPrzynaleznoscDoGracza() != bohater.getPrzynaleznoscDoGracza()) {
                        if (PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]) != null) {
                            listHero.add(new HeroCell(map.getPola()[i][j].getBohater(),
                                    PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                        }
                    }
                }
            }
        }

        Sort.HeroCellSort(listHero);

        Gdx.app.log("Heroes", "" + listHero.size());
        for (HeroCell mc : listHero) {
            Gdx.app.log("Heroes: " + mc.bohater + " DISTANCE: " + mc.distance, "");
        }

        return listHero;
    }

    /**
     * Zwraca ArrayList z obiektem klasy MobCell zawierającym wszystkie moby poziomu 1 oraz
     * dystansem od bohatera
     *
     * @param bohater Referencja do obiektu bohatera wg. którego mają być sprawdzane moby
     * @return Obiekt klasy MobCell
     */
    public ArrayList<MobCell> findTargetMobLevel1(Bohater bohater) {
        Mapa map = bohater.getGs().getMapa();
        ArrayList<MobCell> listMobsLevel = new ArrayList<MobCell>();

        for (int i = 0; i < map.getIloscPolX(); i++) {
            for (int j = 0; j < map.getIloscPolY(); j++) {
                if (map.getPola()[i][j].getMob() != null) {
                    if (map.getPola()[i][j].getMob().getMobLevel() == 1) {
                        if (PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]) != null) {
                            listMobsLevel.add(new MobCell(map.getPola()[i][j].getMob(),
                                    PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                        }
                    }
                }
            }
        }

        Sort.mobCellSort(listMobsLevel);

        Gdx.app.log("Mobs level 1", "" + listMobsLevel.size());
        for (MobCell mc : listMobsLevel) {
            Gdx.app.log("MOB LVL1: " + mc.mob + " DISTANCE: " + mc.distance, "");
        }

        return listMobsLevel;
    }

    /**
     * Zwraca ArrayList z obiektem klasy MobCell zawierającym wszystkie moby poziomu 2 oraz
     * dystansem od bohatera
     *
     * @param bohater Referencja do obiektu bohatera wg. którego mają być sprawdzane moby
     * @return Obiekt klasy MobCell
     */
    public ArrayList<MobCell> findTargetMobLevel2(Bohater bohater) {
        Mapa map = bohater.getGs().getMapa();
        ArrayList<MobCell> listMobsLevel = new ArrayList<MobCell>();

        for (int i = 0; i < map.getIloscPolX(); i++) {
            for (int j = 0; j < map.getIloscPolY(); j++) {
                if (map.getPola()[i][j].getMob() != null) {
                    if (map.getPola()[i][j].getMob().getMobLevel() == 2) {
                        if (PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]) != null) {
                            listMobsLevel.add(new MobCell(map.getPola()[i][j].getMob(),
                                    PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                        }
                    }
                }
            }
        }

        Sort.mobCellSort(listMobsLevel);

        Gdx.app.log("Mobs level 2", "" + listMobsLevel.size());
        for (MobCell mc : listMobsLevel) {
            Gdx.app.log("MOB LVL2: " + mc.mob + " DISTANCE: " + mc.distance, "");
        }

        return listMobsLevel;
    }

    /**
     * Zwraca ArrayList z obiektem klasy TresureBoxCell zawierającym wszystkie skrzynie oraz
     * dystansem skrzyń od bohatera
     *
     * @param bohater Referencja do obiektu bohatera wg. którego mają być lokalizowane skrzynie
     * @return Zwraca ArrayList z obiektami klasy TresureBoxCell
     */
    public ArrayList<TresureBoxCell> findTargetTresureBox(Bohater bohater) {
        Mapa map = bohater.getGs().getMapa();
        ArrayList<TresureBoxCell> listTresureBox = new ArrayList<TresureBoxCell>();

        for (int i = 0; i < map.getIloscPolX(); i++) {
            for (int j = 0; j < map.getIloscPolY(); j++) {
                if (map.getPola()[i][j].getTresureBox() != null && map.getPola()[i][j].getBohater() == null) {
                    if (PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]) != null) {
                        listTresureBox.add(new TresureBoxCell(map.getPola()[i][j].getTresureBox(),
                                PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                    }
                } else if (map.getPola()[i][j].getTresureBox() != null && map.getPola()[i][j].getBohater() == bohater) {
                    if (PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]) != null) {
                        listTresureBox.add(new TresureBoxCell(map.getPola()[i][j].getTresureBox(),
                                PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                    }
                }
            }
        }

        Sort.tresureBoxCellSort(listTresureBox);

        Gdx.app.log("Tresure Boxes", "" + listTresureBox.size());
        for (TresureBoxCell tbc : listTresureBox) {
            Gdx.app.log("TBoxes: " + tbc.tresureBox + " DISTANCE: " + tbc.distance, "");
        }

        return listTresureBox;
    }

    /**
     * Zwraca ArrayList z obiektem klasy CastleCell zawierającym wszystkie zamki oraz
     * dystansem zamku od bohatera
     *
     * @param bohater Referencja do obiektu bohatera wg. którego mają być lokalizowane skrzynie
     * @return Zwraca ArrayList z obiektami klasy CastleCell
     */
    public ArrayList<CastleCell> findTargetCastle(Bohater bohater) {
        Mapa map = bohater.getGs().getMapa();
        ArrayList<CastleCell> listCastle = new ArrayList<CastleCell>();

        for (int i = 0; i < map.getIloscPolX(); i++) {
            for (int j = 0; j < map.getIloscPolY(); j++) {
                if (map.getPola()[i][j].getCastle() != null
                        && map.getPola()[i][j].getCastle().getPrzynaleznoscDoGracza() != bohater.getPrzynaleznoscDoGracza()) {
                    if (PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]) != null) {
                        listCastle.add(new CastleCell(map.getPola()[i][j].getCastle(),
                                PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                    }
                }
            }
        }

        Sort.castleCellSort(listCastle);

        Gdx.app.log("Tresure Boxes", "" + listCastle.size());
        for (CastleCell tbc : listCastle) {
            Gdx.app.log("TBoxes: " + tbc.castle + " DISTANCE: " + tbc.distance, "");
        }

        return listCastle;
    }


    /**
     * GETTERS AND SETTERS
     */
    public ActionModes getActionMode() {
        return actionMode;
    }

    public void setActionMode(ActionModes actionMode) {
        this.actionMode = actionMode;
    }

    /**
     * END OF GETTERS AND SETTERS
     */

    /**
     * Klasa zawiera TresureBox oraz dystans do obiektu.
     */
    public class TresureBoxCell {
        TresureBox tresureBox;
        int distance;

        public TresureBoxCell(TresureBox tresureBox, int distance) {
            this.tresureBox = tresureBox;
            this.distance = distance;
        }
    }

    /**
     * Klasa zawiera Moba i dystans do obiektu.
     */
    public class MobCell {
        public Mob mob;
        public int distance;

        public MobCell(Mob mob, int distance) {
            this.mob = mob;
            this.distance = distance;
        }
    }

    /**
     * Klasa zawiera Bohatera i dystans do obiektu.
     */
    public class HeroCell {
        public Bohater bohater;
        public int distance;

        public HeroCell(Bohater bohater, int distance) {
            this.bohater = bohater;
            this.distance = distance;
        }
    }

    /**
     * Klasa zawiera zamek i dystans do obiektu.
     */
    public class CastleCell {
        public Castle castle;
        public int distance;

        public CastleCell(Castle castle, int distance) {
            this.castle = castle;
            this.distance = distance;
        }
    }
}
