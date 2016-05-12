package com.vs.eoh;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.Spells;

import java.util.ArrayList;

/**
 * Zaklecia
 *
 * @author v
 */
public class SpellActor extends DefaultActor {

    private Assets a;
    private GameStatus gs;
    private Bohater bohater;
    private Spells rodzajCzaru;
    private ArrayList<SpellEffects> spellEffects;
    private boolean spellWorksOnlyForCaster = false;
    private boolean spellWorksOnlyForPlayersHeroes = false;
    private boolean spellSummonSpell = false;

    /**
     * Określa pozycję na mapie na które ma zadziałać zaklęcie. W przypadku przywołania.
     */
    private int spellX;
    private int spellY;

    private int zasieg = 0;
    private int dmg = 0;
    private int koszt = 0;

    /**
     * Konstruktor do tworzenia panelu czarów.
     *
     * @param tekstura Tekstura Panelu
     * @param x pozycja X panelu
     * @param y pozycja Y panelu
     */
    public SpellActor(Texture tekstura, int x, int y) {
        super(tekstura, x, y);
    }

    /**
     * Konstruktor Czaru
     *
     * @param tekstura Tekstura czaru
     * @param x Pozycja X czaru
     * @param y Pozycja Y czaru
     * @param bohater Ref. do obikektu bohatera
     * @param a Referencja do obiketu Assets
     * @param gs Referencja do obiketu Game Status
     */
    public SpellActor(Texture tekstura, int x, int y, Bohater bohater, Assets a, GameStatus gs) {
        super(tekstura, x, y);
        this.bohater = bohater;
        this.dodajListnera();
        this.a = a;
        this.gs = gs;
        //this.spellEffects = new ArrayList<SpellEffects>();
    }

    /**
     *
     */
    public static void animateDamage() {

    }

    private void dodajListnera() {
        final SpellActor sA = this;
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Kliknięto w czar");
                SpellCaster spellCaster = new SpellCaster(bohater, a, gs, sA);
            }
        });
    }

    /**
     * GETTERS AND SETTERS *
     */

    /**
     * Zwraca zasięg czaru.
     *
     * @return
     */
    public int getZasieg() {
        return zasieg;
    }

    /**
     * Ustala zasięg czaru.
     *
     * @param zasieg
     */
    public void setZasieg(int zasieg) {
        this.zasieg = zasieg;
    }

    /**
     * Zwraca wartość obrażeń zaklęcia
     *
     * @return
     */
    public int getDmg() {
        return dmg;
    }

    /**
     * Ustala wartość obrażęń zaklęcia.
     *
     * @param dmg
     */
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    /**
     * Zwraca rodzaj czaru z Enum Spells
     *
     * @return
     */
    public Spells getRodzajCzaru() {
        return rodzajCzaru;
    }

    /**
     * Ustala rodzaj zaklęcia z ENUM Spells
     *
     * @param rodzajCzaru
     */
    public void setRodzajCzaru(Spells rodzajCzaru) {
        this.rodzajCzaru = rodzajCzaru;
    }

    /**
     * Zwraca tablice efektów zaklęcia
     *
     * @return
     */
    public ArrayList<SpellEffects> getSpellEffects() {
        return spellEffects;
    }

    /**
     * Ustala Tablicę efektów zaklęcia
     *
     * @param spellEffects
     */
    public void setSpellEffects(ArrayList<SpellEffects> spellEffects) {
        this.spellEffects = spellEffects;
    }

    /**
     * Zwraca koszt zaklęcia w manie.
     *
     * @return
     */
    public int getKoszt() {
        return koszt;
    }

    /**
     * Ustala koszt w manie zaklęcia.
     *
     * @param koszt
     */
    public void setKoszt(int koszt) {
        this.koszt = koszt;
    }

    /**
     * Zwraca info czy czar działa tylko na rzucającego
     *
     * @return
     */
    public boolean isSpellWorksOnlyForCaster() {
        return spellWorksOnlyForCaster;
    }

    /**
     * Ustala czy czar działa tylko na rzucającego.
     *
     * @param spellWorksOnlyForCaster
     */
    public void setSpellWorksOnlyForCaster(boolean spellWorksOnlyForCaster) {
        this.spellWorksOnlyForCaster = spellWorksOnlyForCaster;
    }

    /**
     * Zwraca czy czar działa tylko na bohaterów gracza.
     * @return 
     */
    public boolean isSpellWorksOnlyForPlayersHeroes() {
        return spellWorksOnlyForPlayersHeroes;
    }

    /**
     * Ustala czy czar działa tylko na bohaterów gracza.
     * @param spellWorksOnlyForPlayersHeroes 
     */
    public void setSpellWorksOnlyForPlayersHeroes(boolean spellWorksOnlyForPlayersHeroes) {
        this.spellWorksOnlyForPlayersHeroes = spellWorksOnlyForPlayersHeroes;
    }

    /**
     * Sprawdza czy zaklęcie jest przyzywające
     *
     * @return Zwraca TRUE jeżeli tak
     */
    public boolean isSpellSummonSpell() {
        return spellSummonSpell;
    }

    /**
     * Ustala czy zaklęcie jest czarem przyzywającym
     *
     * @param spellSummonSpell
     */
    public void setSpellSummonSpell(boolean spellSummonSpell) {
        this.spellSummonSpell = spellSummonSpell;
    }

    public int getSpellX() {
        return spellX;
    }

    public void setSpellX(int spellX) {
        this.spellX = spellX;
    }

    public int getSpellY() {
        return spellY;
    }

    public void setSpellY(int spellY) {
        this.spellY = spellY;
    }
}
