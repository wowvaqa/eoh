package com.vs.ai;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.vs.enums.ActionModes;
import com.vs.enums.CzesciCiala;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.Spells;
import com.vs.enums.TypItemu;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.Castle;
import com.vs.eoh.Eoh;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Gracz;
import com.vs.eoh.Item;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.NewGame;
import com.vs.eoh.Pole;
import com.vs.eoh.Ruch;
import com.vs.eoh.TresureBox;
import com.vs.network.NetEngine;
import com.vs.network.Network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

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

    /**
     * Tworzy AI dla gracza.
     *
     * @param gracz Referencja do obiektu gracza.
     */
    public AI(Gracz gracz) {
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
    private void heroPromotion() {

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
                for (int i = 0; i < GameStatus.gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < GameStatus.gs.getMapa().getIloscPolY(); j++) {
                        if (GameStatus.gs.getMapa().getPola()[i][j].isLokacjaStartowaP1()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < GameStatus.gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < GameStatus.gs.getMapa().getIloscPolY(); j++) {
                        if (GameStatus.gs.getMapa().getPola()[i][j].isLokacjaStartowaP2()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < GameStatus.gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < GameStatus.gs.getMapa().getIloscPolY(); j++) {
                        if (GameStatus.gs.getMapa().getPola()[i][j].isLokacjaStartowaP3()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < GameStatus.gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < GameStatus.gs.getMapa().getIloscPolY(); j++) {
                        if (GameStatus.gs.getMapa().getPola()[i][j].isLokacjaStartowaP4()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
        }

        tmpTex = NewGame.getTeksturaBohatera(klasaPostaciGracz01);
        tmpTexZazanaczony = NewGame.getTeksturaBohateraZaznaczonego(klasaPostaciGracz01);

        gracz.getBohaterowie().add(new Bohater(tmpTex, tmpTexZazanaczony, lokPoczatkowaX * 100, lokPoczatkowaY * 100, GameStatus.a, 0, 0, GameStatus.gs, GameStatus.g, klasaPostaciGracz01));

        int bohGracza = gracz.getNumerGracza();
        int wymTabBoh = gracz.getBohaterowie().size() - 1;

        // Ustala do którego gracza z tablicy graczy należy bohater
        gracz.getBohaterowie().get(wymTabBoh).setPrzynaleznoscDoGracza(gracz.getNumerGracza());

        switch (bohGracza) {
            case 0:
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 1:
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 2:
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 3:
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gracz.getBohaterowie().get(wymTabBoh));
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                GameStatus.gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
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
     * @param bohater Referencja do obiektu bohatera od którego ma rozpocząć się poszukiwanie ścieżki
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

    private void makeHeroAttack(Bohater bohater, int locXofField, int locYofField) {

        if (bohater.getGs().getMapa().getPola()[locXofField][locYofField].getMob() != null) {
            bohater.getA().animujLblDmg(locXofField * 100 + 50, locYofField * 100, bohater,
                    bohater.getGs().getMapa().getPola()[locXofField][locYofField].getMob());
        } else if (bohater.getGs().getMapa().getPola()[locXofField][locYofField].getBohater() != null) {
            bohater.getA().animujLblDmg(locXofField * 100 + 50, locYofField * 100, bohater,
                    bohater.getGs().getMapa().getPola()[locXofField][locYofField].getBohater());
        } else if (bohater.getGs().getMapa().getPola()[locXofField][locYofField].getCastle() != null) {
            bohater.getA().animujLblDmg(locXofField * 100 + 50, locYofField * 100, bohater,
                    bohater.getGs().getMapa().getPola()[locXofField][locYofField].getCastle());
        }

        bohater.getGs().usunMartweMoby();
    }

    /**
     * @param bohater Referencja do obiektu bohatera
     * @param x       wartość ruchu w osi X
     * @param y       wartość ruchu w osi Y
     */
    private void makeHeroMove(Bohater bohater, int x, int y) {

        GameStatus.gs.getMapa().pola[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].setBohater(null);

        bohater.addAction(Actions.moveBy(x * 100, y * 100, 0.25f));
        bohater.setPozXnaMapie(bohater.getPozXnaMapie() + x);
        bohater.setPozYnaMapie(bohater.getPozYnaMapie() + y);

        GameStatus.gs.getMapa().pola[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].setBohater(bohater);

        if (bohater.getFiled().getCastle() != null) {
            retakeCastle(bohater.getFiled());
        }

        bohater.setPozostaloRuchow(bohater.getPozostaloRuchow() - 1);
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
     * @param bohater Referencja do obiektu bohatera wg. którego mają być sprawdzane moby
     * @return Obiekt klasy MobCell
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

        Gdx.app.log("Mobs level 1", "" + listHero.size());
        for (HeroCell mc : listHero) {
            Gdx.app.log("MOB LVL1: " + mc.bohater + " DISTANCE: " + mc.distance, "");
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
                        listMobsLevel.add(new MobCell(map.getPola()[i][j].getMob(),
                                PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
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
                        listMobsLevel.add(new MobCell(map.getPola()[i][j].getMob(),
                                PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
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
                if (map.getPola()[i][j].getTresureBox() != null) {
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
