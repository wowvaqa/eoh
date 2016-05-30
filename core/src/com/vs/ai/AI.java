package com.vs.ai;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.vs.enums.ActionModes;
import com.vs.enums.CzesciCiala;
import com.vs.enums.TypItemu;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Gracz;
import com.vs.eoh.Item;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.Pole;
import com.vs.eoh.Ruch;
import com.vs.eoh.TresureBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

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

                        int index = 0;

                        for (Item item : bohater.getEquipment()) {
                            if (item.getCzescCiala().equals(CzesciCiala.glowa)) {
                                if (item.getLevel() > bohater.getItemGlowa().getLevel()) {
                                    bohater.setItemGlowa(item);
                                    for (int i = 0; i < bohater.getEquipment().size(); i++) {
                                        if (bohater.getItemGlowa().equals(item)) {
                                            index = i;
                                        }
                                    }
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.korpus)) {
                                if (item.getLevel() > bohater.getItemKorpus().getLevel()) {
                                    bohater.setItemKorpus(item);
                                    for (int i = 0; i < bohater.getEquipment().size(); i++) {
                                        if (bohater.getItemKorpus().equals(item)) {
                                            index = i;
                                        }
                                    }
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.nogi)) {
                                if (item.getLevel() > bohater.getItemNogi().getLevel()) {
                                    bohater.setItemNogi(item);
                                    for (int i = 0; i < bohater.getEquipment().size(); i++) {
                                        if (bohater.getItemNogi().equals(item)) {
                                            index = i;
                                        }
                                    }
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.stopy)) {
                                if (item.getLevel() > bohater.getItemStopy().getLevel()) {
                                    bohater.setItemStopy(item);
                                    for (int i = 0; i < bohater.getEquipment().size(); i++) {
                                        if (bohater.getItemStopy().equals(item)) {
                                            index = i;
                                        }
                                    }
                                }
                            } else if (item.getCzescCiala().equals(CzesciCiala.rece)) {
                                if (item.getLevel() > bohater.getItemLewaReka().getLevel()) {
                                    bohater.setItemLewaReka(item);
                                    for (int i = 0; i < bohater.getEquipment().size(); i++) {
                                        if (bohater.getItemLewaReka().equals(item)) {
                                            index = i;
                                        }
                                    }
                                } else if (item.getLevel() > bohater.getItemPrawaReka().getLevel()) {
                                    bohater.setItemPrawaReka(item);
                                    for (int i = 0; i < bohater.getEquipment().size(); i++) {
                                        if (bohater.getItemPrawaReka().equals(item)) {
                                            index = i;
                                        }
                                    }
                                }
                            }
                        }

                        if (index != 0) {
                            bohater.getEquipment().remove(index);
                        }

                        TresureBox.removeTresureBox(endField.getTresureBox(), bohater.getGs().getMapa());
                        break;
                }
            }
        }
    }

    /**
     * Zwraca tryb AI - MOVE, ATTACK, WAIT
     *
     * @param bohater Referencja do obiektu bohatera.
     * @return Zwraca odpowiedni tryb postępowania dla bohatera AI.
     */
    public ActionModes getAiMode(Bohater bohater) {

        double hp = bohater.getHp() / 2;

        //if (bohater.getActualHp() < hp) {
        if (bohater.getActualHp() < 2) {
            return ActionModes.wait;
        } else if (findTargetTresureBox(bohater) != null && findTargetTresureBox(bohater).size() > 0
                && findTargetTresureBox(bohater).get(0).distance < 3) {
            if (findTargetTresureBox(bohater).get(0).distance == 0) {
                return ActionModes.takeTresureBox;
            } else {
                return ActionModes.moveToTresureBox;
            }
        } else if (findTargetMobLevel1(bohater) != null && findTargetMobLevel1(bohater).size() > 0) {
            if (findTargetMobLevel1(bohater).get(0).distance == 1) {
                return ActionModes.attackMobLevel1;
            } else {
                return ActionModes.moveToMobLevel1;
            }
        }
        return ActionModes.wait;
    }

    /**
     * !!!
     *
     * @param bohater
     * @return
     */
    public Pole setTarget(Bohater bohater, ActionModes actionMode) {

        switch (actionMode) {
            case attackMobLevel1:
                return findTargetMobLevel1(bohater).get(0).mob.getField();
            case moveToMobLevel1:
                return findTargetMobLevel1(bohater).get(0).mob.getField();
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
//            Ruch ruch = new Ruch(bohater, bohater.getA(), bohater.getGs());
            bohater.getA().animujLblDmg(locXofField * 100 + 50, locYofField * 100, bohater,
                    bohater.getGs().getMapa().getPola()[locXofField][locYofField].getMob());
        }
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

        bohater.setPozostaloRuchow(bohater.getPozostaloRuchow() - 1);
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

        listMobsLevel.sort(new Comparator<MobCell>() {
            @Override
            public int compare(MobCell o1, MobCell o2) {
                if (o2 == null) return -1;
                if (o1.distance > o2.distance) return 1;
                else if (o1.distance < o2.distance) return -1;
                else
                    return 0;
            }
        });

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

        listMobsLevel.sort(new Comparator<MobCell>() {
            @Override
            public int compare(MobCell o1, MobCell o2) {
                if (o2 == null) return -1;
                if (o1.distance > o2.distance) return 1;
                else if (o1.distance < o2.distance) return -1;
                else
                    return 0;
            }
        });

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
                    listTresureBox.add(new TresureBoxCell(map.getPola()[i][j].getTresureBox(),
                            PathFinder.findPath(map, bohater.getFiled(), map.getPola()[i][j]).size()));
                }
            }
        }

        listTresureBox.sort(new Comparator<TresureBoxCell>() {
            @Override
            public int compare(TresureBoxCell o1, TresureBoxCell o2) {
                if (o2 == null) return -1;
                if (o1.distance > o2.distance) return 1;
                else if (o1.distance < o2.distance) return -1;
                else
                    return 0;
            }
        });

        Gdx.app.log("Tresure Boxes", "" + listTresureBox.size());
        for (TresureBoxCell tbc : listTresureBox) {
            Gdx.app.log("TBoxes: " + tbc.tresureBox + " DISTANCE: " + tbc.distance, "");
        }

        return listTresureBox;
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
}
