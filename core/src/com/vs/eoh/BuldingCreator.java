package com.vs.eoh;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.vs.enums.AnimsTypes;
import com.vs.enums.Buldings;

/**
 * Created by v on 2016-05-17.
 * Klasa odpowiada za tworzenie budynków
 */
public class BuldingCreator {

    private V v;

    private int pozX;
    private int pozY;

    public BuldingCreator(V v) {
        this.v = v;
    }

    /**
     * @param typeOfBulding Typ Budynku z ENUM Buldings
     * @param pozX          Pozycja X budynku na mapie
     * @param pozY          Pozycja Y budynku na mpaie
     * @return zwraca obiekt klasy Bulding
     */
    public Bulding createBulding(Buldings typeOfBulding, int pozX, int pozY) {

        this.pozX = pozX;
        this.pozY = pozY;

        switch (typeOfBulding) {
            case traningCamp:
                return createTraningCamp();
            case defenceCamp:
                return createDefenceTower();
            case powerCamp:
                return createPowerTower();
            case wisdomCamp:
                return createWisdomTower();
            case speedCamp:
                return createSpeedTower();
            case hpCamp:
                return createHpTower();
            case well:
                return createWell();
            case temple:
                return createTemple();
            case randomBulding:
                return createRandomBulding(typeOfBulding);
        }
        return createTraningCamp();
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Obozu treningowego.
     */
    private Bulding createTraningCamp() {

        Bulding bulding = new Bulding(v, AnimsTypes.TreningCamp);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texTreningCamp.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Oboz treningowy");
        bulding.setBuldingDescription("Zwieksza wspolczynnik ataku +1");
        bulding.setShortBuldingDescription("Atak +1");
        bulding.setModAtc(1);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createDefenceTower() {

        Bulding bulding = new Bulding(v, AnimsTypes.DefenceTower);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texDefenceTower.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Wieza Obroncow");
        bulding.setBuldingDescription("Zwieksza wspolczynnik obrony +1");
        bulding.setShortBuldingDescription("Obrona +1");
        bulding.setModDef(1);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createPowerTower() {

        Bulding bulding = new Bulding(v, AnimsTypes.TowerOfMagicPower);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texPowerCamp.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Wieza Mocy");
        bulding.setBuldingDescription("Zwieksza wspolczynnik Mocy +1");
        bulding.setShortBuldingDescription("Moc +1");
        bulding.setModPow(1);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createWisdomTower() {

        Bulding bulding = new Bulding(v, AnimsTypes.ObeliskOfWisdomAnimation);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texWisdomCamp.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Wieza Wiedzy");
        bulding.setBuldingDescription("Zwieksza wspolczynnik Wiedzy +1");
        bulding.setShortBuldingDescription("Wiedza +1");
        bulding.setModWsd(1);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createSpeedTower() {

        Bulding bulding = new Bulding(v, AnimsTypes.SpeedTower);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texSpeedCamp.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Wieza Szybkosci");
        bulding.setBuldingDescription("Zwieksza wspolczynnik Szybkosci +1");
        bulding.setShortBuldingDescription("Szybkosc +1");
        bulding.setModSpd(1);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createHpTower() {

        Bulding bulding = new Bulding(v, AnimsTypes.HpTower);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texHpCamp.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Wieza Zdrowia");
        bulding.setBuldingDescription("Zwieksza wspolczynnik Zdrowia +5");
        bulding.setShortBuldingDescription("HP +5");
        bulding.setModHp(5);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createWell() {

        Bulding bulding = new Bulding(v, AnimsTypes.ObeliskOfWisdomAnimation);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texWell.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Magiczna studnia");
        bulding.setBuldingDescription("Odnawia wszystkie punkty many");
        bulding.setShortBuldingDescription("Mana MAX");
        bulding.setMaxMana(true);
        bulding.setCancelVisited(true);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createTemple() {

        Bulding bulding = new Bulding(v, AnimsTypes.ObeliskOfWisdomAnimation);

        bulding.setPosition(pozX * 100, pozY * 100);
        bulding.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("buldings/texTemple.png"))));
        bulding.setSize(100, 100);

        bulding.setName("Magiczna swiatynia");
        bulding.setBuldingDescription("Odnawia wszystkie punkty Hp");
        bulding.setShortBuldingDescription("Hp MAX");
        bulding.setMaxHp(true);
        bulding.setCancelVisited(true);

        return bulding;
    }

    /**
     * Przypisuje do obiektu klasy Bulding charakterystykę Wieży obrońców.
     */
    private Bulding createRandomBulding(Buldings buldingType) {

        switch (buldingType) {
            case traningCamp:
                return createTraningCamp();
            case defenceCamp:
                return createDefenceTower();
            case powerCamp:
                return createPowerTower();
            case hpCamp:
                return createHpTower();
            case speedCamp:
                return createSpeedTower();
            case wisdomCamp:
                return createWisdomTower();
        }
        return createTraningCamp();
    }
}
