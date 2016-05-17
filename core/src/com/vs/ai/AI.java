package com.vs.ai;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.vs.eoh.Bohater;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Gracz;

import java.util.ArrayList;

/**
 * Created by v on 2016-05-13.
 */
public class AI {

    public static AI ai0;
    public static AI ai1;
    public static AI ai2;
    public static AI ai3;

    public static ArrayList<AI> aiList;

    public static int witchMove = 0;
    public static long nextMove;
    public static boolean aiStarted = false;

    Gracz gracz;

    /**
     * Tworzy AI dla gracza.
     * @param gracz Referencja do obiektu gracza.
     */
    public AI(Gracz gracz){
        this.gracz = gracz;
        Gdx.app.log("AI START", "" + gracz.toString());
    }

    /**
     * Ai wykonuje ruch.
     */
    public void makeAIMove() {
        for (Bohater bohater : gracz.getBohaterowie()) {
            if (bohater.getPozostaloRuchow() > 0) {
                makeHeroMove(bohater, 1, 1);
            }
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
}
