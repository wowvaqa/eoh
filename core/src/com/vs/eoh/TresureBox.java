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

    // wygląd
    private Texture icon;
    private final Sprite sprite;

    // assety
    private final Assets a;
    private Game g;
    private final GameStatus gs;

    // array list przechowujący itemy.
    private final ArrayList<Item> dostepneItemy = new ArrayList<Item>();

    // obiekt do generowania itemów.
    private final ItemCreator itemCreator;

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

        itemCreator = new ItemCreator(gs);

        sprite = new Sprite(a.texTresureBox);
        this.setSize(100, 100);
        this.setPosition(pozXstage, pozYstage);

        this.losujItemy(poziomItemow, iloscItemow);
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
        //System.out.println("Wylosowano indeks: " + indeks);

        return GameStatus.itemyPoziom1.get(indeks);
    }

    /**
     * Losuje dowolny item poziomu 2.
     */
    private DostepneItemki losujDowolnyItemLevel2() {
        Random rnd = new Random();
        int indeks = rnd.nextInt(GameStatus.itemyPoziom2.size());
        //System.out.println("Wylosowano indeks: " + indeks);

        return GameStatus.itemyPoziom2.get(indeks);
    }
}
