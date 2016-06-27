
package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.ai.PathFinder;
import com.vs.ai.PathMoves;
import com.vs.enums.DostepneItemki;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.Spells;
import com.vs.screens.DialogScreen;
import com.vs.screens.MapScreen;

import java.util.ArrayList;

/**
 * Klasa Bohater odpowiada za rysowanie i zachowanie bohatera na mapie.
 *
 * @author v
 */
public class Bohater extends Actor {

    private final String imie = null;
    // klasa bohatera
    public KlasyPostaci klasyPostaci;
    public boolean teksturaZaktualizowana = false;
    public boolean animujCiecieNetwork = false;
    public int damageNetwork = 0;   // do rysowania labelki z ilością obrażeń
    // Statystyki
    private int aiDistance = 0;
    private V v;
    private Sprite sprite;    // wygląd
    private Image image;
    private Texture bohaterTex;
    private Texture bohaterCheckTex;
    private Pixmap pixMap;
    private int pozX = 0;   // pozycja X na mapie
    private int pozY = 0;   // pozycja Y na mapie
    // Zmienne określające które stopnie awansu posiada bohater
    private boolean g0 = true;
    private boolean g1 = false;
    private boolean g2A = false;
    private boolean g2B = false;
    private boolean g3A = false;
    private boolean g3B = false;
    // ekwipunek bohatera
    private Item itemGlowa = null;
    private Item itemKorpus = null;
    private Item itemNogi = null;
    private Item itemPrawaReka = null;
    private Item itemLewaReka = null;
    private Item itemStopy = null;
    private ArrayList<Spells> listOfSpells;
    private ArrayList<SpellActor> spells;
    private ArrayList<Item> equipment;
    // informuje cz bohater jest zaznaczony
    private boolean moveInterfaceOn = false;
    private boolean zaznaczony = false;
    private boolean otwartaSkrzyniaZeSkarbem = false;
    // lokacja bohatera w obiekcie klasy Mapa
    private int pozXnaMapie;
    private int pozYnaMapie;
    // informuje czy dozwolony jest ruch dla bohatera
    // Bez sprawdzania na mapie poruszał się tylko pierwszy z utworzonych
    // bohaterów za sprawą funkcji draw która jako pierwsza została wywołana
    // u bohatera który był utworzony jako pierwszy.
    private boolean moveable = false;
    private int atak = 0;
    private int obrona = 0;
    private int hp = 0;
    private int actualHp = 0;
    private int szybkosc = 0;
    private int pozostaloRuchow = 0;
    private int moc = 0;
    private int wiedza = 0;
    private int mana = 0;
    private int actualMana = 0;
    private int manaRegeneration = 0;
    // aktualny poziom punktów doświadczenia
    private int exp = 0;
    // punkty potrzebne do uzyskania następnego poziomu
    private int expToNextLevel = 100;
    // pocziom doświadczenia
    private int levelOfExp = 1;
    private String actualHeroClass;
    private int przynaleznoscDoGracza;
    // efekty które oddziaływują na bohatera
    private ArrayList<Effect> efekty;
    // efekty czarów które oddziaływują na bohatera
    private ArrayList<SpellEffects> spellEffects;
    // efekty które działają jednorazowo
    private ArrayList<Effect> tempEffects;

    /**
     * @param textureIcon           Tekstura bohatera wyświetlająca się na mapie
     * @param textureIconZaznaczona Tekstura bohatera wyświetlana kiedy bohater
     *                              zostanie kliknięty
     * @param lokaczjaPoczatkowaX   Lokacja X początkowa bohatera na mapie
     * @param lokaczjaPoczatkowaY   Lokacja Y początkowa bohatera na mapie
     * @param pozycjaXnaMapie       definiuje pozycje X w obiekcie klasy Mapa
     * @param pozycjaYnaMapie       definiuje pozycje Y w obiekcie klasy Mapa
     * @param kp                    Klasa postaci
     */
    public Bohater(Texture textureIcon, Texture textureIconZaznaczona,
                   int lokaczjaPoczatkowaX, int lokaczjaPoczatkowaY,
                   int pozycjaXnaMapie, int pozycjaYnaMapie, KlasyPostaci kp, V v) {

        this.v = v;

        aiDistance = 0;
        this.efekty = new ArrayList<Effect>();
        this.spellEffects = new ArrayList<SpellEffects>();
        this.listOfSpells = new ArrayList<Spells>();

        this.tempEffects = new ArrayList<Effect>();
        this.tempEffects.add(new Effect());

        this.equipment = new ArrayList<Item>();
        this.spells = new ArrayList<SpellActor>();
        this.pozXnaMapie = pozycjaXnaMapie;
        this.pozYnaMapie = pozycjaYnaMapie;
        this.bohaterTex = textureIcon;
        this.bohaterCheckTex = textureIconZaznaczona;
        this.pozX = lokaczjaPoczatkowaX;
        this.pozY = lokaczjaPoczatkowaY;
        this.klasyPostaci = kp;

        sprite = new Sprite(this.bohaterTex);
        this.setSize(sprite.getWidth(), sprite.getHeight());
        this.setPosition(this.pozX, this.pozY);

        this.dodajListnera();

        ItemCreator ic = new ItemCreator(v);

        // Utworzenie pdst. zestawu itemków i przypisanie do ekwipunku bohatera
        itemLewaReka = ic.utworzItem(DostepneItemki.Piesci, v);
        itemPrawaReka = ic.utworzItem(DostepneItemki.Piesci, v);
        itemNogi = ic.utworzItem(DostepneItemki.Nogi, v);
        itemStopy = ic.utworzItem(DostepneItemki.LnianeButy, v);
        itemGlowa = ic.utworzItem(DostepneItemki.Glowa, v);
        itemKorpus = ic.utworzItem(DostepneItemki.LnianaKoszula, v);

        // Tymczasowa regeneracja many
        this.manaRegeneration = 1;
        if (this.getKlasyPostaci() == KlasyPostaci.Czarodziej) {
            manaRegeneration = 2;
        }
    }

    /**
     * Zwraca indeks bohatera w ArrayList bohaterów należących do gracza
     *
     * @param b Referencja do obiektu Gracza
     * @return indeks bohatera w tablicy.
     */
    public static int getHeroNumberInArrayList(Bohater b, Gracz g) {

        int amountOfHero = g.getBohaterowie().size();
        int heroIndex = -99;

        for (int i = 0; i < amountOfHero; i++) {
            if (g.getBohaterowie().get(i).equals(b)) {
                heroIndex = i;
            }
        }

        return heroIndex;
    }

    // 1. Dodoaje Click Listnera do obiektu klasy Bohater
    // 2. Jeżeli obiekt zostanie kliknięty wtedy włącza przyciski odpowiedzialne
    // za ruch.
    // 3. Zmienia teksture obiketu Bohatera na zaznaczoną
    // 4. Zmiena możliwość ruchu na TRUE
    // 5. Zmienia status bohatera na zaznaczony (wykorzystywane przy
    // wyświetlaniu statsów, oraz sprawdzaniu czy inny bohater nie został
    // już zaznaczony aby uniemożliwić zaznaczenie dwóch bohaterów na raz.
    // 6. Sprawdza czy kliknięty bohater należy do gracz którego trwa aktualnie
    // tura.
    private void dodajListnera() {
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean bohaterZaznaczony = false;
                // Sprawdza czy któryś z bohaterów na mapie nie jest już zaznaczony
                for (Gracz i : v.getGs().getGracze()) {
                    for (Bohater j : i.getBohaterowie()) {
                        if (j.zaznaczony) {
                            j.setZaznaczony(false);
                            j.getSprite().setTexture(j.bohaterTex);
                            Ruch.wylaczPrzyciski();
                        }
                    }
                }
                // Jeżeli TRUE wtedy uniemozliwia jego zaznaczenie
                if (bohaterZaznaczony) {
                    DialogScreen dS = new DialogScreen("Blad", v.getA().skin, "Nie moge zaznaczyc dwoch bohaterow", Assets.stage01MapScreen);
                    System.out.println("Nie mogę zaznaczyc dwóch bohaterów");
                    // Jeżeli FALSE wtedy uruchamia reszte procedur dla bohatera
                } else {
                    // Sprawdza czy bohater gracza porusza się w sowjej turze.
                    if (przynaleznoscDoGracza != v.getGs().getTuraGracza()) {
                        System.out.println("Ten Gracz teraz nie ma swojej tury");
                    } else {
                        // Sprawdza czy bohater posiada jeszcze punkty ruchu.
                        if (pozostaloRuchow < 1) {
                            System.out.println("Bohater nie posiada już ruchu!");
                            sprite.setTexture(bohaterCheckTex);
                            zaznaczony = true;
                            v.getGs().setCzyZaznaczonoBohatera(true);
                            // jeżeli posiada punkty ruchu.
                            aktualizujEfektyBohatera();
                        } else {
                            if (otwartaSkrzyniaZeSkarbem) {
                                System.out.println("Nie mogę zaznaczyć - otwarta skrzynia ze skarbem");
                            } else {
                                aktualizujEfektyBohatera();
                                moveable = true;
                                sprite.setTexture(bohaterCheckTex);
                                zaznaczony = true;
                                v.getGs().setCzyZaznaczonoBohatera(true);

                                moveInterfaceOn = true;

                                Ruch ruch = new Ruch(v.getGs().getBohaterZaznaczony(), v);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Aktualizuje ikony w stage02 na MapScreen efektów, które działają.
     */
    public void aktualizujEfektyBohatera() {
        Ruch.wylaczIkonyEfektow();

        int pozycjaX = Gdx.graphics.getWidth() - 220;

        MapScreen.mapScreen.tables.formatEffetsBarTable();

        if (!this.efekty.isEmpty()) {
            for (Effect dI : this.efekty) {
                dI.getIkona().setSize(25, 25);
                dI.getIkona().setPosition(pozycjaX, 425);
                Assets.stage02MapScreen.addActor(dI.getIkona());
                MapScreen.mapScreen.tables.tableEffectsBar.add(dI.getIkona()).pad(2);
                pozycjaX += 30;
            }
        }

        if (!this.spellEffects.isEmpty()) {
            for (SpellEffects sE : this.spellEffects) {
                sE.getIkona().setSize(25, 25);
                sE.getIkona().setPosition(pozycjaX, 425);
                Assets.stage02MapScreen.addActor(sE.getIkona());
                MapScreen.mapScreen.tables.tableEffectsBar.add(sE.getIkona()).pad(2);
                pozycjaX += 30;
            }
        }
    }

    public void aktualizujKolorTeksturyBohatera() {
        if (!bohaterTex.getTextureData().isPrepared()) {
            bohaterTex.getTextureData().prepare();
        }
        this.setPixMap(bohaterTex.getTextureData().consumePixmap());

        this.getPixMap().setColor(zwrocKolorBohatera(this.przynaleznoscDoGracza));
        this.getPixMap().fillRectangle(80, 5, 15, 15);

        this.setBohaterTex(new Texture(this.getPixMap()));
        this.sprite.setTexture(bohaterTex);

        // aktualizacja tekstury bohatera zaznaczonej
        if (!bohaterCheckTex.getTextureData().isPrepared()) {
            bohaterCheckTex.getTextureData().prepare();
        }
        this.setPixMap(bohaterCheckTex.getTextureData().consumePixmap());

        this.getPixMap().setColor(zwrocKolorBohatera(this.przynaleznoscDoGracza));
        this.getPixMap().fillRectangle(80, 5, 15, 15);

        this.setBohaterCheckTex(new Texture(this.getPixMap()));
    }

    private Color zwrocKolorBohatera(int gracz) {
        switch (gracz) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
        }
        return null;
    }

    /**
     * Aktualizuje status paska energi bohatera
     */
    public void aktualizujTeksture() {
        // aktualizacja tekxtury bohatera nie zaznaczonej
        if (!bohaterTex.getTextureData().isPrepared()) {
            bohaterTex.getTextureData().prepare();
        }
        this.setPixMap(bohaterTex.getTextureData().consumePixmap());

        this.getPixMap().setColor(Color.RED);
        //this.getPixMap().fillRectangle(0, 0, 5, 100);
        this.getPixMap().fillRectangle(0, 2, 5, 98);
        this.getPixMap().setColor(Color.WHITE);
        //this.getPixMap().fillRectangle(1, 1, 3, 100 - poziomHP());
        this.getPixMap().fillRectangle(1, 3, 3, 97 - poziomHP());

        this.setBohaterTex(new Texture(this.getPixMap()));
        this.sprite.setTexture(bohaterTex);

        // aktualizacja tekxtury bohatera zaznaczonej
        if (!bohaterCheckTex.getTextureData().isPrepared()) {
            bohaterCheckTex.getTextureData().prepare();
        }
        this.setPixMap(bohaterCheckTex.getTextureData().consumePixmap());

        this.getPixMap().setColor(Color.RED);
        //this.getPixMap().fillRectangle(0, 0, 5, 100);
        this.getPixMap().fillRectangle(0, 2, 5, 98);
        this.getPixMap().setColor(Color.WHITE);
        //this.getPixMap().fillRectangle(1, 1, 3, 100 - poziomHP());
        this.getPixMap().fillRectangle(1, 3, 3, 97 - poziomHP());

        this.setBohaterCheckTex(new Texture(this.getPixMap()));
    }

    /**
     * Zwraca pole na którym stoid bohater
     *
     * @return obiekt klasy pole.
     */
    public Pole getFiled() {
        return v.getGs().getMapa().getPola()[getPozXnaMapie()][getPozYnaMapie()];
    }

    /**
     * Zwraca proporcjonalny poziom zdrowia bohatera do jego maksymalnych
     * punktów życia.
     *
     * @return
     */
    private int poziomHP() {
        float poziom = this.actualHp * 97 / this.hp;

        return (int) Math.round(poziom);
    }

    @Override
    public void act(float delta) {
        super.act(delta); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!teksturaZaktualizowana) {
            aktualizujTeksture();
            this.aktualizujKolorTeksturyBohatera();
            teksturaZaktualizowana = true;
        }

        if (this.animujCiecieNetwork) {
            v.getA().animujCiecie((int) this.getX(), (int) this.getY());
            //a.animujLblDmgNetwork(this.getX() + 50, this.getY() + 50, damageNetwork);
            Animation.animujLblDamage(this.getX() + 50, this.getY() + 50, "Dmg: " + Integer.toString(damageNetwork), v.getA());
            this.animujCiecieNetwork = false;
        }

        batch.draw(this.sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    /**
     * Aktualizuje czas trawania efektów
     */
    public void aktualizujDzialanieEfektow() {

        // Aktualizacja efektów itemków
        int indeksEfektuDoUsuniecia = 99;

        for (int i = 0; i < this.efekty.size(); i++) {
            efekty.get(i).setDlugoscTrwaniaEfektu(efekty.get(i).getDlugoscTrwaniaEfektu() - 1);
            if (efekty.get(i).getDlugoscTrwaniaEfektu() < 1) {
                indeksEfektuDoUsuniecia = i;
            }
        }

        if (indeksEfektuDoUsuniecia != 99) {
            this.efekty.remove(indeksEfektuDoUsuniecia);
            this.aktualizujDzialanieEfektow();
        }

        // Aktualizacja efektów czarów
        int indeksSpellEfektuDoUsuniecia = 99;

        for (int i = 0; i < this.spellEffects.size(); i++) {
            spellEffects.get(i).setDlugoscTrwaniaEfektu(spellEffects.get(i).getDlugoscTrwaniaEfektu() - 1);
            if (spellEffects.get(i).getDlugoscTrwaniaEfektu() < 1) {
                indeksSpellEfektuDoUsuniecia = i;
            }
        }

        if (indeksSpellEfektuDoUsuniecia != 99) {
            this.spellEffects.remove(indeksSpellEfektuDoUsuniecia);
            this.aktualizujDzialanieEfektow();
        }
    }

    /**
     * Czyści efekty tymczasowe
     */
    public void czyscEfektyTymczasowe() {
        for (Effect efk : this.getTempEffects()) {
            efk.setEfektArmor(0);
            efk.setEfektAtak(0);
            efk.setEfektDmg(0);
            efk.setEfektObrona(0);
            efk.setEfektSzybkosc(0);
            efk.setEfektHp(0);
            efk.setEfektMana(0);
        }
    }

    public int getSzybkoscEfekt() {
        int sumaSzybkosci = 0;
        for (Effect dItema : this.efekty) {
            sumaSzybkosci += dItema.getEfektSzybkosc();
        }
        for (SpellEffects sE : this.spellEffects) {
            sumaSzybkosci += sE.getEfektSzybkosc();
        }
        return sumaSzybkosci;
    }

    /**
     * Zwraca wartość współczynnika ataku efektów które oddziaływują na
     * bohatera.
     *
     * @return
     */
    public int getAtakEfekt() {
        int sumaAtaku = 0;
        for (Effect dItema : this.efekty) {
            sumaAtaku += dItema.getEfektAtak();
        }
        for (SpellEffects sE : this.spellEffects) {
            sumaAtaku += sE.getEfektAtak();
        }
        return sumaAtaku;
    }

    /**
     * Zwraca wartość współczynnika obrony efektów które oddziaływują na
     * bohatera.
     *
     * @return
     */
    public int getObronaEfekt() {
        int sumaObrona = 0;
        for (Effect dItema : this.efekty) {
            sumaObrona += dItema.getEfektObrona();
        }
        for (SpellEffects sE : this.spellEffects) {
            sumaObrona += sE.getEfektObrona();
        }
        return sumaObrona;
    }

    /**
     * *************************************************************************
     * Setters and Getters
     * ************************************************************************
     */

    /**
     * Zwraca klasę postaci bohatera
     *
     * @return
     */
    public KlasyPostaci getKlasyPostaci() {
        return this.klasyPostaci;
    }

    /**
     * Ustala klasę postaci dla bohatera
     *
     * @param klasyPostaci
     */
    public void setKlasyPostaci(KlasyPostaci klasyPostaci) {
        this.klasyPostaci = klasyPostaci;
    }

    /**
     * @return
     */
    public Pixmap getPixMap() {
        return pixMap;
    }

    public void setPixMap(Pixmap pixMap) {
        this.pixMap = pixMap;
    }

    public Texture getBohaterTex() {
        return bohaterTex;
    }

    public void setBohaterTex(Texture bohaterTex) {
        this.bohaterTex = bohaterTex;
    }

    public Texture getBohaterCheckTex() {
        return bohaterCheckTex;
    }

    public void setBohaterCheckTex(Texture bohaterCheckTex) {
        this.bohaterCheckTex = bohaterCheckTex;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getActualHp() {
        return actualHp;
    }

    public void setActualHp(int actualHp) {
        this.actualHp = actualHp;
    }

    public ArrayList<Item> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<Item> equipment) {
        this.equipment = equipment;
    }

    /**
     * Zwraca pozycję X obiektu Bohater na w obiekcie klasy Mapa
     *
     * @return
     */
    public int getPozXnaMapie() {
        return pozXnaMapie;
    }

    /**
     * Ustala pozycję X w obiekcie klasy Mapa
     *
     * @param pozXnaMapie
     */
    public void setPozXnaMapie(int pozXnaMapie) {
        this.pozXnaMapie = pozXnaMapie;
    }

    /**
     * Zwraca do którego gracza z tablicy Graczy przynależy bohater
     *
     * @return
     */
    public int getPrzynaleznoscDoGracza() {
        return przynaleznoscDoGracza;
    }

    /**
     * Ustala do którego gracza z tablicy graczy będzie należał bohater
     *
     * @param przynaleznoscDoGracza
     */
    public void setPrzynaleznoscDoGracza(int przynaleznoscDoGracza) {
        this.przynaleznoscDoGracza = przynaleznoscDoGracza;
    }

    /**
     * Zwraca ilość pozostałych ruchów które może wykonać na mapię bohater
     *
     * @return
     */
    public int getPozostaloRuchow() {
        return pozostaloRuchow;
    }

    /**
     * Ustala ilość ruchów które pozostały bohaterowi na mapie
     *
     * @param pozostaloRuchow
     */
    public void setPozostaloRuchow(int pozostaloRuchow) {
        this.pozostaloRuchow = pozostaloRuchow;
    }

    /**
     * Zwraca pozycję Y obiektu Bohater na w obiekcie klasy Mapa
     *
     * @return
     */
    public int getPozYnaMapie() {
        return pozYnaMapie;
    }

    /**
     * Ustala pozycję Y w obiekcie klasy Mapa
     *
     * @param pozYnaMapie
     */
    public void setPozYnaMapie(int pozYnaMapie) {
        this.pozYnaMapie = pozYnaMapie;
    }

    /**
     * Zwraca wartość ataku bohatera
     *
     * @return
     */
    public int getAtak() {
        return atak;
    }

    /**
     * Ustala wartość ataku bohatera
     *
     * @param atak
     */
    public void setAtak(int atak) {
        this.atak = atak;
    }

    /**
     * Zwraca współczynika obrony
     *
     * @return
     */
    public int getObrona() {
        return obrona;
    }

    /**
     * Ustawia współczynik obrony
     *
     * @param obrona
     */
    public void setObrona(int obrona) {
        this.obrona = obrona;
    }

    /**
     * Zwraca wartość współczynnika zdrowia bohatera
     *
     * @return
     */
    public int getHp() {
        return hp;
    }

    /**
     * Ustala wartość współczynnika zdrowia bohatera
     *
     * @param hp
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Zwraca wartość współczynnika maksymalnej ilośći ruchów bohatera
     *
     * @return
     */
    public int getSzybkosc() {
        return szybkosc;
    }

    /**
     * Ustala wartość współczynnika maksymalnej ilości ruchu bohatera
     *
     * @param szybkosc
     */
    public void setSzybkosc(int szybkosc) {
        this.szybkosc = szybkosc;
    }

    /**
     * Zwraca maksymalny poziom punktów many.
     *
     * @return
     */
    public int getMana() {
        return mana;
    }

    /**
     * Ustala maksymalny poziom punktów many
     *
     * @param mana
     */
    public void setMana(int mana) {
        this.mana = mana;
    }

    /**
     * Zwraca ilość punktów many
     *
     * @return
     */
    public int getActualMana() {
        return actualMana;
    }

    /**
     * Ustala ilość punktów many
     *
     * @param actualMana
     */
    public void setActualMana(int actualMana) {
        this.actualMana = actualMana;
    }

    /**
     * Sprawdza czy bohater jest zaznaczony
     *
     * @return TRUE jeżeli jest zaznaczony
     */
    public boolean isZaznaczony() {
        return zaznaczony;
    }

    /**
     * Ustala czy bohater jest zaznaczony
     *
     * @param zaznaczony
     */
    public void setZaznaczony(boolean zaznaczony) {
        this.zaznaczony = zaznaczony;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public void setExpToNextLevel(int expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }

    public int getLevelOfExp() {
        return levelOfExp;
    }

    public void setLevelOfExp(int levelOfExp) {
        this.levelOfExp = levelOfExp;
    }

    public Item getItemGlowa() {
        return itemGlowa;
    }

    public void setItemGlowa(Item itemGlowa) {
        this.itemGlowa = itemGlowa;
    }

    public Item getItemKorpus() {
        return itemKorpus;
    }

    public void setItemKorpus(Item itemKorpus) {
        this.itemKorpus = itemKorpus;
    }

    public Item getItemNogi() {
        return itemNogi;
    }

    public void setItemNogi(Item itemNogi) {
        this.itemNogi = itemNogi;
    }

    public Item getItemPrawaReka() {
        return itemPrawaReka;
    }

    public void setItemPrawaReka(Item itemPrawaReka) {
        this.itemPrawaReka = itemPrawaReka;
    }

    public Item getItemLewaReka() {
        return itemLewaReka;
    }

    public void setItemLewaReka(Item itemLewaReka) {
        this.itemLewaReka = itemLewaReka;
    }

    public Item getItemStopy() {
        return itemStopy;
    }

    public void setItemStopy(Item itemStopy) {
        this.itemStopy = itemStopy;
    }

    public boolean isOtwartaSkrzyniaZeSkarbem() {
        return otwartaSkrzyniaZeSkarbem;
    }

    public void setOtwartaSkrzyniaZeSkarbem(boolean otwartaSkrzyniaZeSkarbem) {
        this.otwartaSkrzyniaZeSkarbem = otwartaSkrzyniaZeSkarbem;
    }

    /**
     * Zwraca referencje do obiektu klasy Effect()
     *
     * @return
     */
    public ArrayList<Effect> getEfekty() {
        return efekty;
    }

    /**
     * Ustala referencje do obiektu klasy Effect()
     *
     * @param efekty
     */
    public void setEfekty(ArrayList<Effect> efekty) {
        this.efekty = efekty;
    }

    /**
     * Zwraca arrayList czarów
     *
     * @return
     */
    public ArrayList<SpellActor> getSpells() {
        return spells;
    }

    /**
     * Ustala arrayList czarów
     *
     * @param spells
     */
    public void setSpells(ArrayList<SpellActor> spells) {
        this.spells = spells;
    }

    /**
     * Zwraca wartość mocy
     *
     * @return
     */
    public int getMoc() {
        return moc;
    }

    /**
     * Ustala wartość mocy
     *
     * @param moc
     */
    public void setMoc(int moc) {
        this.moc = moc;
    }

    /**
     * Zwraca poziom wiedzy
     *
     * @return
     */
    public int getWiedza() {
        return wiedza;
    }

    /**
     * Ustala poziom wiedzy.
     *
     * @param wiedza
     */
    public void setWiedza(int wiedza) {
        this.wiedza = wiedza;
    }

    /**
     * Zwraca regenereacje pnkt. many na ture
     *
     * @return
     */
    public int getManaRegeneration() {
        return manaRegeneration;
    }

    /**
     * Ustala regeneracje pntk. many na ture.
     *
     * @param manaRegeneration
     */
    public void setManaRegeneration(int manaRegeneration) {
        this.manaRegeneration = manaRegeneration;
    }

    /**
     * Zwraca tablicę z efektami czarów oddziaływujących na bohatera.
     *
     * @return
     */
    public ArrayList<SpellEffects> getSpellEffects() {
        return spellEffects;
    }

    /**
     * Ustala tablicę z efektami czarów oddziaływujących na bohatera.
     *
     * @param spellEffects
     */
    public void setSpellEffects(ArrayList<SpellEffects> spellEffects) {
        this.spellEffects = spellEffects;
    }

    /**
     * Zwraca listę zaklęć dostępnych dla bohatera
     *
     * @return
     */
    public ArrayList<Spells> getListOfSpells() {
        return listOfSpells;
    }

    /**
     * Ustala listę zaklęć dostępnych dla bohatera.
     *
     * @param listOfSpells
     */
    public void setListOfSpells(ArrayList<Spells> listOfSpells) {
        this.listOfSpells = listOfSpells;
    }

    /**
     * Zwraca tablicę tymczasowych efektów.
     *
     * @return
     */
    public ArrayList<Effect> getTempEffects() {
        return tempEffects;
    }

    /**
     * Ustala tablicę tymczasowych efektów
     *
     * @param tempEffects
     */
    public void setTempEffects(ArrayList<Effect> tempEffects) {
        this.tempEffects = tempEffects;
    }

    public boolean isG0() {
        return g0;
    }

    public void setG0(boolean g0) {
        this.g0 = g0;
    }

    public boolean isG1() {
        return g1;
    }

    public void setG1(boolean g1) {
        this.g1 = g1;
    }

    public boolean isG2A() {
        return g2A;
    }

    public void setG2A(boolean g2A) {
        this.g2A = g2A;
    }

    public boolean isG2B() {
        return g2B;
    }

    public void setG2B(boolean g2B) {
        this.g2B = g2B;
    }

    public boolean isG3A() {
        return g3A;
    }

    public void setG3A(boolean g3A) {
        this.g3A = g3A;
    }

    public boolean isG3B() {
        return g3B;
    }

    public void setG3B(boolean g3B) {
        this.g3B = g3B;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isMoveInterfaceOn() {
        return moveInterfaceOn;
    }

    public void setMoveInterfaceOn(boolean moveInterfaceOn) {
        this.moveInterfaceOn = moveInterfaceOn;
    }

    /**
     * Zwraca aktualną klasę postaci
     *
     * @return KlasyPostaci
     */
    public String getActualHeroClass() {
        return actualHeroClass;
    }

    /**
     * Ustala aktualną klasę postaci
     *
     * @param actualHeroClass KlasyPostaci
     */
    public void setActualHeroClass(String actualHeroClass) {
        this.actualHeroClass = actualHeroClass;
    }

    public Assets getA() {
        return v.getA();
    }

    public GameStatus getGs() {
        return v.getGs();
    }

    public Game getG() {
        return v.getG();
    }

    public V getV() {
        return v;
    }

    /**
     * Zwraca dystans AI do ustalania kolejności posunieć przez AI
     *
     * @return dystans AI
     */
    public int getAiDistance() {
        return aiDistance;
    }

    /**
     * Ustala dystans AI do ustalania kolejności posunięć
     *
     * @param aiDistance dystans AI
     */
    public void setAiDistance(int aiDistance) {
        this.aiDistance = aiDistance;
    }
}
