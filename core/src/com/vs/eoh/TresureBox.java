package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vs.enums.DostepneItemki;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa przechowuje losową partię itemków, które bohater może podnieść i zabrać
 * w swoim ekwipunku.
 *
 * @author wow
 */
public class TresureBox extends Actor {

    private final Sprite sprite;
    // assety
    private final Assets a;
    private final GameStatus gs;
    // array list przechowujący itemy.
    private final ArrayList<Item> dostepneItemy = new ArrayList<Item>();
    // obiekt do generowania itemów.
    private final ItemCreator itemCreator;
    // wygląd
    private Texture icon;
    private Game g;
    // położenie na mapie
    private int pozX;
    private int pozY;

    /**
     *
     * @param poziomItemow Poziom itemów
     * @param iloscItemow Ilość itemów
     * @param a Referencja do obikektu Assets
     * @param gs Referencja do obiektu GameStatus
     * @param g Referencja do obiektu Game
     * @param pozXstage Pozycja X na stage
     * @param pozYstage Pozycja Y na stage
     */
    public TresureBox(int poziomItemow, int iloscItemow, Assets a, GameStatus gs, Game g, int pozXstage, int pozYstage) {
        this.a = a;
        this.gs = gs;
        this.g = g;

        this.pozX = pozXstage / 100;
        this.pozY = pozYstage / 100;

        itemCreator = new ItemCreator(gs);

        sprite = new Sprite(a.texTresureBox);
        this.setSize(100, 100);
        this.setPosition(pozXstage, pozYstage);

        this.losujItemy(poziomItemow, iloscItemow);
    }

    /**
     * Usuwa skrzynię ze skarbem z planszy i mapy.
     *
     * @param tresureBox referencja do obiektur klasy TresureBox
     * @param mapa       referencja do obiketu klasy Mapa
     */
    public static void removeTresureBox(TresureBox tresureBox, Mapa mapa) {
        tresureBox.remove();
        for (int i = 0; i < mapa.getIloscPolX(); i++) {
            for (int j = 0; j < mapa.getIloscPolY(); j++) {
                if (mapa.getPola()[i][j].getTresureBox() != null) {
                    if (mapa.getPola()[i][j].getTresureBox().equals(tresureBox)) {
                        mapa.getPola()[i][j].setTresureBox(null);
                    }
                }
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    // SETTERS AND GETTERS
    public ArrayList<Item> getDostepneItemy() {
        return dostepneItemy;
    }

    /**
     * Generuje zestaw itemów i dodaje je do arraylista z itemami
     */
    private void losujItemy(int poziomItemow, int iloscItemow) {

        switch (poziomItemow) {
            case 1:
                for (int i = 0; i < iloscItemow; i++) {
                    dostepneItemy.add(itemCreator.utworzItem(losujDowolnyItemLevel1(), a, g));
                }
                break;
            case 2:
                for (int i = 0; i < iloscItemow; i++) {
                    dostepneItemy.add(itemCreator.utworzItem(losujDowolnyItemLevel2(), a, g));
                }
                break;
        }

        //dostepneItemy.add(itemCreator.utworzItem(losujDowolnyItemLevel1(), a, g));
        // dostepneItemy.add(itemCreator.utworzItem(losujDowolnyItemLevel1(), a, g));
        //dostepneItemy.add(itemCreator.utworzItem(losujDowolnyItemLevel1(), a, g));
        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.Gold, a, g));

//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.SkorzanaCzapka, a, g));
//        //dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.LnianaKoszula, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.SkorzaneSpodnie, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.SkorzaneButy, a, g));
//        //dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.Kij, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.Miecz, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.Tarcza, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.Luk, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.PotionZdrowie, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.PotionSzybkosc, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.PotionAttack, a, g));
//        dostepneItemy.add(itemCreator.utworzItem(DostepneItemki.PotionDefence, a, g));
    }

    /**
     * Losuje dowolny item poziomu 1.
     */
    private DostepneItemki losujDowolnyItemLevel1() {

        Random rnd = new Random();
        int indeks = rnd.nextInt(GameStatus.itemyPoziom1.size());

        return GameStatus.itemyPoziom1.get(indeks);
    }

    /**
     * Losuje dowolny item poziomu 2.
     */
    private DostepneItemki losujDowolnyItemLevel2() {
        Random rnd = new Random();
        int indeks = rnd.nextInt(GameStatus.itemyPoziom2.size());

        return GameStatus.itemyPoziom2.get(indeks);
    }

    /**
     * Zwraca pole na którym stoi Tresure Box
     *
     * @return Obiket klasy Pole
     */
    public Pole getField() {
        return gs.getMapa().getPola()[this.pozX][this.pozY];
    }

    /**
     * Zwraca pozycję X na mapie skrzyni
     *
     * @return Pozycja X
     */
    public int getPozX() {
        return pozX;
    }

    /**
     * Ustala pozycję X na mapie
     *
     * @param pozX
     */
    public void setPozX(int pozX) {
        this.pozX = pozX;
    }

    /**
     * Zwraca pozycję Y na mapie skrzyni
     *
     * @return Pozycja Y
     */
    public int getPozY() {
        return pozY;
    }

    /**
     * Ustala pozycję Y na mapie.
     *
     * @param pozY
     */
    public void setPozY(int pozY) {
        this.pozY = pozY;
    }
}
