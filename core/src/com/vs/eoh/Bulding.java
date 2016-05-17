/**
 * TODO Dokończyć funkcje losującą budynki.
 */

package com.vs.eoh;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.Buldings;

import java.util.ArrayList;

/**
 * Created by v on 2016-05-17.
 * Klasa odpowiada za obsługę bunynków na mapie.
 */
public class Bulding extends Image {

    private Assets a;

    private ArrayList<Bohater> visited;

    private String name;
    private String buldingDescription;
    private String shortBuldingDescription;

    private int modAtc;
    private int modDef;
    private int modSpd;
    private int modHp;
    private int modExp;
    private int modPow;
    private int modWsd;

    private Buldings typeOfBulding;

    public Bulding(Assets a) {
        super();
        this.a = a;

        visited = new ArrayList<Bohater>();

        dodajListnera();
    }

    /**
     * Modyfikuje atrybuty bohatera o atrybuty budynku.
     *
     * @param bohater Obiekt klasy bohater
     * @param bulding Obiekt klasy budynek
     */
    public static void modyfiAttributes(Bohater bohater, Bulding bulding) {
        bohater.setAtak(bohater.getAtak() + bulding.getModAtc());
        bohater.setObrona(bohater.getObrona() + bulding.getModDef());
        bohater.setSzybkosc(bohater.getSzybkosc() + bulding.getModSpd());
        bohater.setHp(bohater.getHp() + bulding.getModHp());
        bohater.setMoc(bohater.getMoc() + bulding.getModPow());
        bohater.setWiedza(bohater.getWiedza() + bulding.getModWsd());

        bulding.getVisited().add(bohater);

        bohater.getA().animujLblDamage(bulding.getX(), bulding.getY(), bulding.getShortBuldingDescription());
    }

    /**
     * Funkcja zwraca losowy typ Budynku
     *
     * @return Typ Budynku
     */
    public static Buldings drawBulding() {
        return Buldings.traningCamp;
    }

    private void dodajListnera() {
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Dialog(name, a.skin) {
                    {
                        text(buldingDescription);
                        row();

                        boolean buldingVisited = false;

                        if (GameStatus.gs.getBohaterZaznaczony() != null) {

                            for (int i = 0; i < visited.size(); i++) {
                                if (visited.get(i).equals(GameStatus.gs.getBohaterZaznaczony())) {
                                    buldingVisited = true;
                                }
                            }
                        }

                        row();

                        if (buldingVisited) {
                            text("Odwiedzony");
                        }

                        row();

                        button("Zakoncz", "zakoncz");
                    }

                    @Override
                    protected void result(Object object) {
                        if (object.equals("zakoncz")) {
                            this.remove();
                        }
                    }
                }.show(Assets.stage03MapScreen);
                //}.show(Assets.stage01MapScreen);
            }
        });
    }

    /**
     * Zwraca tablicę z bohaterami którzy odwiedzili budynek
     *
     * @return ArrayList<Bohater>
     */
    public ArrayList<Bohater> getVisited() {
        return visited;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getBuldingDescription() {
        return buldingDescription;
    }

    public void setBuldingDescription(String buldingDescription) {
        this.buldingDescription = buldingDescription;
    }

    public String getShortBuldingDescription() {
        return shortBuldingDescription;
    }

    public void setShortBuldingDescription(String shortBuldingDescription) {
        this.shortBuldingDescription = shortBuldingDescription;
    }

    public int getModAtc() {
        return modAtc;
    }

    public void setModAtc(int modAtc) {
        this.modAtc = modAtc;
    }

    public int getModDef() {
        return modDef;
    }

    public void setModDef(int modDef) {
        this.modDef = modDef;
    }

    public int getModSpd() {
        return modSpd;
    }

    public void setModSpd(int modSpd) {
        this.modSpd = modSpd;
    }

    public int getModHp() {
        return modHp;
    }

    public void setModHp(int modHp) {
        this.modHp = modHp;
    }

    public int getModExp() {
        return modExp;
    }

    public void setModExp(int modExp) {
        this.modExp = modExp;
    }

    public int getModPow() {
        return modPow;
    }

    public void setModPow(int modPow) {
        this.modPow = modPow;
    }

    public int getModWsd() {
        return modWsd;
    }

    public void setModWsd(int modWsd) {
        this.modWsd = modWsd;
    }

    public Buldings getTypeOfBulding() {
        return typeOfBulding;
    }

    public void setTypeOfBulding(Buldings typeOfBulding) {
        this.typeOfBulding = typeOfBulding;
    }
}
