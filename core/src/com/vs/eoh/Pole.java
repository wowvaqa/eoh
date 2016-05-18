package com.vs.eoh;

// Klasa Pole jest wirtualnym polem przechowującym referencje do obiektu bohatera
import java.io.Serializable;
import java.util.ArrayList;

import com.vs.enums.TypyTerenu;

public class Pole implements Serializable {

    // Zmienne do odnajdywania najkrótszej scieżki
    public double pathG;
    public double pathH;
    public double pathF;
    private boolean movable = true;
    private Bohater bohater;
    private TresureBox tresureBox = null;
    private Castle castle = null;
    private Mob mob = null;
    private Bulding bulding = null;
    private TypyTerenu typTerenu;
    // Zmienne określają czy dane pole jest lokacją startową któregoś z graczy
    private boolean lokacjaStartowaP1 = false;
    private boolean lokacjaStartowaP2 = false;
    private boolean lokacjaStartowaP3 = false;
    private boolean lokacjaStartowaP4 = false;
    // Zmienne określają czy pole w polu ma zostać wygenerowana skrzynka ze skarbem.
    private boolean tresureBox1Location = false;
    private boolean tresureBox2Location = false;
    // Zmienne określają czy pole w polu ma zostać wygenerowany Mob
    private boolean mob1Location = false;
    private boolean mob2Location = false;
    // Zmienne określające czy w polu jest budynek
    private boolean attackCamp = false;
    private boolean defenceCamp = false;
    private boolean PowerCamp = false;
    private boolean WisdomCamp = false;
    private boolean SpeedCamp = false;
    private boolean HpCamp = false;
    private boolean Well = false;
    private boolean Temple = false;
    private boolean randomBulding = false;

    public Pole() {

    }

    /**
     * *************************************************************************
     * Setters and Getters
     *************************************************************************
     */
    /**
     * Zwraca typ terenu pola
     *
     * @return Typ pola
     */
    public TypyTerenu getTypTerenu() {
        return typTerenu;
    }

    /**
     * Ustala typ terenu pola.
     *
     * @param typTerenu
     */
    public void setTypTerenu(TypyTerenu typTerenu) {
        this.typTerenu = typTerenu;
    }

    /**
     * Zwraca TRUE jeżeli po polu może poruszczać się bohater.
     *
     * @return
     */
    public boolean isMovable() {
        return movable;
    }

    /**
     * Ustala czy po polu może poruszać się bohater.
     *
     * @param movable
     */
    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;
    }

    public Bohater getBohater() {
        return bohater;
    }

    public void setBohater(Bohater bohater) {
        this.bohater = bohater;
    }

    public TresureBox getTresureBox() {
        return tresureBox;
    }

    public void setTresureBox(TresureBox tresureBox) {
        this.tresureBox = tresureBox;
    }

    public Castle getCastle() {
        return castle;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public boolean isLokacjaStartowaP1() {
        return lokacjaStartowaP1;
    }

    public void setLokacjaStartowaP1(boolean lokacjaStartowaP1) {
        this.lokacjaStartowaP1 = lokacjaStartowaP1;
    }

    public boolean isLokacjaStartowaP2() {
        return lokacjaStartowaP2;
    }

    public void setLokacjaStartowaP2(boolean lokacjaStartowaP2) {
        this.lokacjaStartowaP2 = lokacjaStartowaP2;
    }

    public boolean isLokacjaStartowaP3() {
        return lokacjaStartowaP3;
    }

    public void setLokacjaStartowaP3(boolean lokacjaStartowaP3) {
        this.lokacjaStartowaP3 = lokacjaStartowaP3;
    }

    public boolean isLokacjaStartowaP4() {
        return lokacjaStartowaP4;
    }

    public void setLokacjaStartowaP4(boolean lokacjaStartowaP4) {
        this.lokacjaStartowaP4 = lokacjaStartowaP4;
    }

    public boolean isTresureBox1Location() {
        return tresureBox1Location;
    }

    public void setTresureBox1Location(boolean tresureBox1Location) {
        this.tresureBox1Location = tresureBox1Location;
    }

    public boolean isTresureBox2Location() {
        return tresureBox2Location;
    }

    public void setTresureBox2Location(boolean tresureBox2Location) {
        this.tresureBox2Location = tresureBox2Location;
    }

    public boolean isMob1Location() {
        return mob1Location;
    }

    public void setMob1Location(boolean mob1Location) {
        this.mob1Location = mob1Location;
    }

    public boolean isMob2Location() {
        return mob2Location;
    }

    public void setMob2Location(boolean mob2Location) {
        this.mob2Location = mob2Location;
    }

    public boolean isAttackCamp() {
        return attackCamp;
    }

    public void setAttackCamp(boolean attackCamp) {
        this.attackCamp = attackCamp;
    }

    public boolean isDefenceCamp() {
        return defenceCamp;
    }

    public void setDefenceCamp(boolean defenceCamp) {
        this.defenceCamp = defenceCamp;
    }

    public boolean isPowerCamp() {
        return PowerCamp;
    }

    public void setPowerCamp(boolean powerCamp) {
        PowerCamp = powerCamp;
    }

    public boolean isWisdomCamp() {
        return WisdomCamp;
    }

    public void setWisdomCamp(boolean wisdomCamp) {
        WisdomCamp = wisdomCamp;
    }

    public boolean isSpeedCamp() {
        return SpeedCamp;
    }

    public void setSpeedCamp(boolean speedCamp) {
        SpeedCamp = speedCamp;
    }

    public boolean isHpCamp() {
        return HpCamp;
    }

    public void setHpCamp(boolean hpCamp) {
        HpCamp = hpCamp;
    }

    public boolean isWell() {
        return Well;
    }

    public void setWell(boolean well) {
        Well = well;
    }

    public boolean isTemple() {
        return Temple;
    }

    public void setTemple(boolean temple) {
        Temple = temple;
    }

    public boolean isRandomBulding() {
        return randomBulding;
    }

    public void setRandomBulding(boolean randomBulding) {
        this.randomBulding = randomBulding;
    }

    /**
     * Zwraca referencję do obiektu budynku
     *
     * @return Budynek
     */
    public Bulding getBulding() {
        return bulding;
    }

    /**
     * Ustala Budynek
     *
     * @param bulding
     */
    public void setBulding(Bulding bulding) {
        this.bulding = bulding;
    }
}
