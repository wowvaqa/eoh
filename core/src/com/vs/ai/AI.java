package com.vs.ai;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

    Gracz gracz;

    /**
     * Tworzy AI dla gracza.
     * @param gracz Referencja do obiektu gracza.
     */
    public AI(Gracz gracz){
        this.gracz = gracz;
        Gdx.app.log("AI START", "" + gracz.toString());
    }
}
