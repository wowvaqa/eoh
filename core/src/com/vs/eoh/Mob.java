package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.DostepneMoby;
import java.util.ArrayList;
import java.util.Random;

public class Mob extends Actor {

    private Sprite sprite;    // wygląd
    private Texture icon;

    // Pozycja X i Y bohatera atakującego.
    private int pozXatakujacego;
    private int pozYatakujacego;

    private long poczatekAtaku;
    private long nastepnyAtakMoba;

    private int pozX = 0;   // pozycja X na mapie
    private int pozY = 0;   // pozycja Y na mapie

    private final GameStatus gs;
    private final Assets a;
    private final Game g;

    private boolean czyZaatakowany = false;

    // Statystyki
    private String imie = null;
    private int atak = 0;
    private int obrona = 0;
    private int hp = 0;
    private int aktualneHp = 0;
    /**
     * Szybkość Moba.
     */
    private int szybkosc = 0;
    private int aktualnaSzybkosc = 0;
    private int expReward = 0;
    private int mobLevel = 0;

    // Lista efektów czarów które działają na moba.
    private ArrayList<SpellEffects> spellEffects;

    /**
     *
     * @param g Referenca do boiektu Game
     * @param gs Referencja do obiketu Game Status
     * @param a Referencja do obiektu Assets
     * @param lokaczjaPoczatkowaX Lokacja początkowa X w Stage
     * @param lokaczjaPoczatkowaY Lokacja początkowa Y w Stage
     * @param mobLevel Poziom moba.
     * @param typMoba
     */
    public Mob(/**
             * Texture textureIcon, *
             */
            Game g, GameStatus gs, Assets a,
            int lokaczjaPoczatkowaX, int lokaczjaPoczatkowaY, int mobLevel,
            DostepneMoby typMoba) {
        this.spellEffects = new ArrayList<SpellEffects>();
        this.gs = gs;
        this.a = a;
        this.g = g;
        //this.icon = textureIcon;
        this.icon = zwrocTeksture(typMoba);
        this.mobLevel = mobLevel;
        sprite = new Sprite(this.icon);
        this.setSize(sprite.getWidth(), sprite.getHeight());
        this.setPosition(lokaczjaPoczatkowaX, lokaczjaPoczatkowaY);

        wygenerujStatystykiMoba(this.mobLevel);
        this.dodajListnera();
    }

    /**
     * Zwraca teksture wg zadango typu Moba
     *
     * @param typMoba Typ moba
     * @return Tekstura moba
     */
    private Texture zwrocTeksture(DostepneMoby typMoba) {
        switch (typMoba) {
            case Szkielet:
                return a.texSzkieletMob;
            case Wilk:
                return a.texWilkMob;
        }
        return a.btnAttackTex;
    }

    // Dodaje ClickListnera do obiektu Zamku
    private void dodajListnera() {
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Mob został kliknięty");
                new Dialog("Mob", a.skin) {
                    {
                        text("Atak: " + atak);
                        row();
                        text("Obrona: " + obrona);
                        row();
                        text("HP: " + aktualneHp);
                        row();
                        text("Dzialajace efekty czarow:");
                        this.row();
                        for (SpellEffects sE : spellEffects) {
                            text("Efekt Ataku: " + sE.getEfektAtak());
                            this.row();
                            text("Efekt Obrony: " + sE.getEfektObrona());
                            this.row();
                            text("Czas: " + sE.getDlugoscTrwaniaEfektu());
                            this.row();
                            //text("Efekt Szybkosci: " + sE.getEfektSzybkosc());
                            //this.row();
                            //text("Efekt Dmg: " + sE.getEfektDmg());
                            //this.row();
                        }
                        this.row();
                        button("Zakoncz", "zakoncz");
                    }

                    @Override
                    protected void result(Object object) {
                        if (object.equals("zakoncz")) {
                            this.remove();
                        }
                    }
                }.show(Assets.stage01MapScreen);
            }
        });
    }

    /**
     * Generuje statystyki Moba.
     *
     * @param mobLevel Poziom Moba.
     */
    private void wygenerujStatystykiMoba(int mobLevel) {
        this.atak = 5 * mobLevel;
        this.obrona = 5 * mobLevel;
        this.hp = 7 * mobLevel;
        this.aktualneHp = this.hp;
        this.szybkosc = 5 * mobLevel;
        this.aktualnaSzybkosc = this.szybkosc;
        this.expReward = 100 * mobLevel;
    }

    /**
     * Informuje obiekt klasy Mob że został zaatakowany przez bohatera.
     *
     * @param pozX Pozycja X na mapie atakującego bohatera.
     * @param pozY Pozycja Y na mpaie atakującego bohatera.
     */
    public void mobZaatakowany(int pozX, int pozY) {
        czyZaatakowany = true;
        this.pozXatakujacego = pozX;
        this.pozYatakujacego = pozY;
        poczatekAtaku = System.currentTimeMillis() / 1000;
        nastepnyAtakMoba = poczatekAtaku;

        System.out.println("PA " + poczatekAtaku);
        System.out.println("NA " + nastepnyAtakMoba);
    }

    private void atakMoba() {

        if (System.currentTimeMillis() / 1000 > nastepnyAtakMoba) {
            nastepnyAtakMoba += 2;
            System.out.println("TERAZ NASTEPUJE ATAK MOBA");
            if (gs.getMapa().getPola()[this.pozXatakujacego][this.pozYatakujacego].getBohater() != null
                    && this.aktualnaSzybkosc > 0) {
                Bohater bohaterAtakujacy = gs.getMapa().getPola()[this.pozXatakujacego][this.pozYatakujacego].getBohater();
                a.animujLblDmg(bohaterAtakujacy.getX(), bohaterAtakujacy.getY(), this, bohaterAtakujacy);
                this.aktualnaSzybkosc -= 1;
            } else {
                System.out.println("Bohater dał szusa, lub Mob zmęczył się.");
                this.czyZaatakowany = false;
                this.aktualnaSzybkosc = this.szybkosc;
            }
        }
        gs.usunMartweMoby();
    }

    /**
     * Aktualizuje działanie efektów czarów.
     */
    public void aktualizujDzialanieEfektow() {
        System.out.println("Aktualizacja czasu działania efektów dla Mobó");

        // Aktualizacja efektów czarów
        int indeksSpellEfektuDoUsuniecia = 99;

        for (int i = 0; i < this.spellEffects.size(); i++) {
            System.out.println("Wykonuje petle w Spell efektach");
            spellEffects.get(i).setDlugoscTrwaniaEfektu(spellEffects.get(i).getDlugoscTrwaniaEfektu() - 1);
            System.out.println(i + ": " + spellEffects.get(i).getDlugoscTrwaniaEfektu());
            if (spellEffects.get(i).getDlugoscTrwaniaEfektu() < 1) {
                System.out.println("Warunek usuniecia Spell efektu został spełniony");
                indeksSpellEfektuDoUsuniecia = i;
            }
        }

        if (indeksSpellEfektuDoUsuniecia != 99) {
            System.out.println("Usuwam Spell efekt: ");
            this.spellEffects.remove(indeksSpellEfektuDoUsuniecia);
            this.aktualizujDzialanieEfektow();
        }
    }

    /**
     * Losuje wg. zadanego poziomu odpowiedniego moba.
     *
     * @param levelMoba Poziom Moba
     * @return Typ Enum DostepneMoby
     */
    public static DostepneMoby losujMoba(int levelMoba) {

        Random rnd = new Random();

        switch (levelMoba) {
            case 1:
                int indeks = rnd.nextInt(2);
                //System.out.println("!!!!!!!!!!!!!!!!!: " + indeks);
                if (indeks == 0) {
                    return DostepneMoby.Szkielet;
                } else {
                    return DostepneMoby.Wilk;
                }

        }
        return DostepneMoby.Szkielet;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (czyZaatakowany) {
            this.atakMoba();
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (this.getAktualneHp() < 1) {
            System.out.println("Dodaje skrzynie po zabiciu  przeciwnika");
            TresureBox tb = new TresureBox(1, 1, this.a, this.gs, this.g, this.pozX, this.pozY);
            gs.getMapa().getPola()[this.pozX / 100][this.pozY / 100].setTresureBox(tb);
            Assets.stage01MapScreen.addActor(gs.getMapa().getPola()[this.pozX / 100][this.pozY / 100].getTresureBox());
        }

    }

    /**
     * *************************************************************************
     * GETTERS AND SETTERS
     * ************************************************************************
     */
    /**
     * Zwraca TRUE jeżeli Mob jest atakowany, FALSE jeżeli nie jest atakowany.
     *
     * @return
     */
    public boolean isCzyZaatakowany() {
        return czyZaatakowany;
    }

    /**
     * Ustala status TRUE jeżeli mob jest atakowany, FALSE jeżeli nie jest
     * zaatakowany
     *
     * @param czyZaatakowany
     */
    public void setCzyZaatakowany(boolean czyZaatakowany) {
        this.czyZaatakowany = czyZaatakowany;
    }

    /**
     * Pobiera poziom moba.
     *
     * @return
     */
    public int getMobLevel() {
        return mobLevel;
    }

    /**
     * Ustala poziom moba.
     *
     * @param mobLevel
     */
    public void setMobLevel(int mobLevel) {
        this.mobLevel = mobLevel;
    }

    /**
     * Zwraca punkty doświadczenia za pokonanie moba.
     *
     * @return
     */
    public int getExpReward() {
        return expReward;
    }

    /**
     * Ustala punkty doswiadczenia za pokonanie moba.
     *
     * @param expReward
     */
    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public Texture getIcon() {
        return icon;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public int getAtak() {
        return atak;
    }

    public void setAtak(int atak) {
        this.atak = atak;
    }

    public int getObrona() {
        return obrona;
    }

    public void setObrona(int obrona) {
        this.obrona = obrona;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAktualneHp() {
        return aktualneHp;
    }

    public void setAktualneHp(int aktualneHp) {
        this.aktualneHp = aktualneHp;
    }

    public int getSzybkosc() {
        return szybkosc;
    }

    public void setSzybkosc(int szybkosc) {
        this.szybkosc = szybkosc;
    }

    public int getPozX() {
        return pozX;
    }

    public void setPozX(int pozX) {
        this.pozX = pozX;
    }

    public int getPozY() {
        return pozY;
    }

    public void setPozY(int pozY) {
        this.pozY = pozY;
    }

    public int getAktualnaSzybkosc() {
        return aktualnaSzybkosc;
    }

    public void setAktualnaSzybkosc(int aktualnaSzybkosc) {
        this.aktualnaSzybkosc = aktualnaSzybkosc;
    }

    /**
     * Zwraca listę efektów które działają na moba.
     *
     * @return
     */
    public ArrayList<SpellEffects> getSpellEffects() {
        return spellEffects;
    }

    /**
     * Ustala listę efektów które działają na moba.
     *
     * @param spellEffects
     */
    public void setSpellEffects(ArrayList<SpellEffects> spellEffects) {
        this.spellEffects = spellEffects;
    }

}
