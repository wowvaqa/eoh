package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.vs.enums.AnimsTypes;

public class Assets {

    public static Stage stage01MapScreen;
    public static Stage stage02MapScreen;
    public static Stage stage03MapScreen;

    public static Server server;
    public static Client client;
    // Tekstury budynków
    public Texture texTreningCamp;
    public Texture texDefenceCamp;
    public Texture texPowerCamp;
    public Texture texWisdomCamp;
    public Texture texSpeedCamp;
    public Texture texHpCamp;
    public Texture texWell;
    public Texture texTemple;
    public Texture texRandomBulding;
    // Tekstury terenu
    public Texture trawaDrzewoTex;
    public Texture trawaTex;
    public Texture trawaGoraTex;
    public Texture trawaZamekTex;
    public Texture btnGoTex, btnAttackTex;
    public Texture btnPlus, btnMinus, btnOK, btnRight, btnLeft;
    public Texture mobElfTex, mobElfTexZaznaczony;
    public Texture mobOrkTex, mobOrkTexZaznaczony;
    public Texture mobDwarfTex, mobDwarfTexZaznaczony;
    public Texture mobHumanTex, mobHumanTexZaznaczony;
    public Texture mobWizardTex, mobWizardTexZaznaczony;
    public Texture cancelTex;
    public Skin skin;
    // obiekty na mapie
    public Texture texTresureBox;
    // tekstury mobów
    public Texture texSzkieletMob;
    public Texture texWilkMob;
    public Texture texSpiderMob;
    public Texture texZombieMob;
    public Texture texWarrior0;
    public Texture texWarrior1;
    // Texture Atlas
    public TextureAtlas tAtals;
    public TextureAtlas tAtlasWarrior;
    public TextureAtlas tAtlasWizard;
    public TextureAtlas tAtlasHunter;
    public AssetManager aM;
    // tekstury itemków
    public Texture texHead;
    public Texture texLinenTousers;
    public Texture texLinenShoes;
    public Texture texLeatherShoes;
    public Texture texHardLeatherShoes;
    public Texture texLinenShirt;
    public Texture texLinenCap;
    public Texture texMagicHood;
    public Texture texLeatherCap;
    public Texture texLeatherTousers;
    public Texture texLeatherShirt;
    public Texture texLinenGloves;
    public Texture texFist;
    public Texture texStick;
    public Texture texLegs;
    public Texture texSword;
    public Texture texShield;
    public Texture texBow;
    public Texture texLongBow;
    public Texture texStaff;
    public Texture texGold;
    // Spell Textures
    public Texture texSpellFireBall;
    public Texture texSpellFreez;
    public Texture texSpellRage;
    public Texture texSpellSongOfGlory;
    public Texture texSpellHaste;
    public Texture texSpellCure;
    public Texture texSpellDiscouragement;
    public Texture texSpellFury;
    public Texture texSpellCharge;
    public Texture texSpellFinalJudgment;
    public Texture texSpellThunder;
    public Texture texSpellMeteorShower;
    public Texture texSpellBless;
    public Texture texSpellPrayer;
    public Texture texSpellPoison;
    public Texture texSpellSummonBear;
    public Texture texSpellSummonWolf;
    public Texture texSpellVampireTouch;
    public Texture texSpellLongShot;
    // Potions
    public Texture texHelthPotion;
    public Texture texSpeedPotion;
    public Texture texAttackPotion;
    public Texture texDefencePotion;
    // interfejs
    public Texture moveIcon;
    public Texture cancelIcon;
    public Texture attackIcon;
    public Texture spellIcon;
    public Texture texAtcIcon;
    public Texture texDefIcon;
    public Texture texHpIcon;
    public Texture texSpdIcon;
    public Texture texPwrIcon;
    public Texture texWsdIcon;
    public Texture texDmgIcon;
    public Texture texArmIcon;
    public AssetManager am;
    public Label lblDmg;
    public int[] mapa = new int[100];

    /**
     * Pictures
     */
    public Texture texMainPic;
    public Texture texNewGamePic;
    public Texture texNewGamePlayerTabPic;

    /**
     * SOUNDS --------------------------------------------------------------------------------------
     */
    public Sound buttonClick;
    public Sound swordSound;
    public Sound chestSqueek;
    public Sound magicWand;
    public Sound wolfSnarl;
    public Sound fireball;
    public Sound thuner;
    public Sound walk;
    public Sound levelUp;
    public Sound statisticUp;
    public Sound rageSpell;
    public Sound freezSpell;
    public Sound zombieAttack;
    public Sound skeletonAttack;
    public Sound meteorShower;

    // predefiniowane okno ifnoramcyjne
    private Window infoWindow;

    public Assets() {

        tAtals = new TextureAtlas(Gdx.files.internal("terrain/test.atlas"));

        tAtlasWarrior = new TextureAtlas(Gdx.files.internal("moby/warrior/warrior.atlas"));
        tAtlasWizard = new TextureAtlas(Gdx.files.internal("moby/wizard/wizard.atlas"));
        tAtlasHunter = new TextureAtlas(Gdx.files.internal("moby/hunter/hunter.atlas"));
        
        aM = new AssetManager();
        aM.load("terrain/test.atlas", TextureAtlas.class);
        aM.finishLoading();

        trawaDrzewoTex = new Texture("grassTree100x100.png");
        trawaTex = new Texture("grass100x100.png");
        trawaGoraTex = new Texture("grassMountain100x100.png");
        trawaZamekTex = new Texture("grassCastle100x100.png");
        btnGoTex = new Texture("goBtt.png");
        btnAttackTex = new Texture("attackBtt.png");
        btnPlus = new Texture("bttPlus.png");
        btnMinus = new Texture("bttMinus.png");
        btnOK = new Texture("btnOK.png");
        btnRight = new Texture("btnRight.png");
        btnLeft = new Texture("btnLeft.png");
        mobElfTex = new Texture("moby/hunter/0.png");
        mobElfTexZaznaczony = new Texture("moby/hunter/0z.png");
        mobOrkTex = new Texture("mobOrkTex.png");
        mobOrkTexZaznaczony = new Texture("mobOrkTexZaznaczony.png");
        mobDwarfTex = new Texture("mobDwarfTex.png");
        mobDwarfTexZaznaczony = new Texture("mobDwarfTexZaznaczony.png");
        mobHumanTex = new Texture("moby/warrior/0.png");
        mobHumanTexZaznaczony = new Texture("moby/warrior/0z.png");
        mobWizardTex = new Texture("moby/wizard/0.png");
        mobWizardTexZaznaczony = new Texture("moby/wizard/0z.png");

        cancelTex = new Texture("cancelBtt.png");

        texTresureBox = new Texture("texTresureBox2.png");

        skin = new Skin(Gdx.files.internal("styles/uiskin.json"));

        lblDmg = new Label("", skin);

        moveIcon = new Texture("interface/texMoveIcon.png");
        cancelIcon = new Texture("interface/texCancelIcon.png");
        attackIcon = new Texture("interface/texAtakIcon.png");
        spellIcon = new Texture("interface/texSpellIcon.png");
        texAtcIcon = new Texture("interface/texAtcIcon.png");
        texDefIcon = new Texture("interface/texDefIcon.png");
        texHpIcon = new Texture("interface/texHpIcon.png");
        texSpdIcon = new Texture("interface/texSpdIcon.png");
        texPwrIcon = new Texture("interface/texPwrIcon.png");
        texWsdIcon = new Texture("interface/texWsdIcon.png");
        texDmgIcon = new Texture("interface/texDmgIcon.png");
        texArmIcon = new Texture("interface/texArmIcon.png");

        texMainPic = new Texture("mainPic.jpg");
        texNewGamePic = new Texture("newGamePic.png");
        texNewGamePlayerTabPic = new Texture("texNewGamePlayerTabPic.png");

        makeSounds();

        makeItems();
        makeSpellTextures();
        makeBuldingsTextures();
        makeMobs();

        fillMap();
        utworzInfoWindow();

    }

    /**
     * Animuje cięcie miecza w momencie ataku.
     *
     * @param locX
     * @param locY
     */
    public void animujCiecie(int locX, int locY) {
        AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.SlashAnimation), false);
        animActor.setPosition(locX, locY);
        Assets.stage01MapScreen.addActor(animActor);
    }

    // utworzenie okna informacyjnego
    private void utworzInfoWindow() {
        infoWindow = new Window("TEST", skin);
        infoWindow.setPosition(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 150);
        infoWindow.setSize(400, 300);
        infoWindow.setVisible(false);
    }

    /**
     * Pokazuje okno z informacjami dotyczącymi charakterystyki itemka.
     *
     * @param nazwa Nazwa itemka
     * @param atak modyfikator ataku itemka
     * @param obrona modyfikator obrony itemka
     * @param hp modyfikator hp itemka
     * @param szybkosc modyfikator szybkości itemka
     */
    public void pokazInfoWindow(String nazwa, int atak, int obrona, int hp,
            int szybkosc) {
        System.out.println("Funkcja pokazInfoWindow jest uruchomiona");
        infoWindow.setZIndex(200);
        infoWindow.setVisible(true);
        infoWindow.add("Nazwa:");
        infoWindow.add(nazwa);
        infoWindow.row();
        infoWindow.add("Mod. Ataku: ");
        infoWindow.add(Integer.toString(atak));
        infoWindow.row();
        infoWindow.add("Mod. Obrony: ");
        infoWindow.add(Integer.toString(obrona));
        infoWindow.row();
        infoWindow.add("Mod. Hp: ");
        infoWindow.add(Integer.toString(hp));
        infoWindow.row();
        infoWindow.add("Mod. Szybkosci: ");
        infoWindow.add(Integer.toString(szybkosc));
        infoWindow.row();
        infoWindow.toFront();
    }

    /**
     * Ukrywa okno informacyjne
     */
    public void ukryjInfoWindow() {
        infoWindow.setVisible(false);
        infoWindow.reset();
    }

    private void makeItems() {
        texHead = new Texture("items/texHead.png");
        texLinenGloves = new Texture("items/texLinenGloves.png");
        texLinenCap = new Texture("items/texLinenCap.png");
        texLinenShirt = new Texture("items/texLinenShirt.png");
        texLinenShoes = new Texture("items/texLinenShoes.png");
        texLinenTousers = new Texture("items/texLinenTrousers.png");
        texLeatherCap = new Texture("items/texLeatherCap.png");
        texMagicHood = new Texture("items/texMagicHood.png");
        texLeatherTousers = new Texture("items/texLeatherTrousers.png");
        texLeatherShirt = new Texture("items/texLeatherArmor.png");
        texLeatherShoes = new Texture("items/texLeatherShoes.png");
        texHardLeatherShoes = new Texture("items/texHardLeatherShoes.png");
        texFist = new Texture("items/texFist.png");
        texStick = new Texture("items/texStick.png");
        texLegs = new Texture("items/texLegs.png");
        texSword = new Texture("items/texSword.png");
        texShield = new Texture("items/texShield.png");
        texGold = new Texture("items/texGold.png");
        texBow = new Texture("items/texBow.png");
        texLongBow = new Texture("items/texLongBow.png");
        texStaff = new Texture("items/texStaff.png");
        texHelthPotion = new Texture("items/texHealthPotion.png");
        texSpeedPotion = new Texture("items/texSpeedPotion.png");
        texAttackPotion = new Texture("items/texAttackPotion.png");
        texDefencePotion = new Texture("items/texDefencePotion.png");
    }

    private void makeBuldingsTextures() {
        texTreningCamp = new Texture("buldings/texTreningCamp.png");
        texDefenceCamp = new Texture("buldings/texDefenceTower.png");
        texPowerCamp = new Texture("buldings/texPowerCamp.png");
        texWisdomCamp = new Texture("buldings/texWisdomCamp.png");
        texSpeedCamp = new Texture("buldings/texSpeedCamp.png");
        texHpCamp = new Texture("buldings/texHpCamp.png");
        texWell = new Texture("buldings/texWell.png");
        texTemple = new Texture("buldings/texTemple.png");
        texRandomBulding = new Texture("buldings/texRandomBulding.png");
    }

    private void makeSounds() {
        buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonClick_01.wav"));
        swordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_01.wav"));
        chestSqueek = Gdx.audio.newSound(Gdx.files.internal("sounds/floor-squeak_01.wav"));
        magicWand = Gdx.audio.newSound(Gdx.files.internal("sounds/magic-wand_01.wav"));
        wolfSnarl = Gdx.audio.newSound(Gdx.files.internal("sounds/wolf-snarl_01.wav"));
        fireball = Gdx.audio.newSound(Gdx.files.internal("sounds/fireball_01.wav"));
        thuner = Gdx.audio.newSound(Gdx.files.internal("sounds/thunder_01.wav"));
        walk = Gdx.audio.newSound(Gdx.files.internal("sounds/walk_01.wav"));
        levelUp = Gdx.audio.newSound(Gdx.files.internal("sounds/levelUp_02.mp3"));
        statisticUp = Gdx.audio.newSound(Gdx.files.internal("sounds/levelUp_02.wav"));
        rageSpell = Gdx.audio.newSound(Gdx.files.internal("sounds/rage_01.wav"));
        freezSpell = Gdx.audio.newSound(Gdx.files.internal("sounds/frost_01.wav"));
        zombieAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/zombieAttack_01.wav"));
        skeletonAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/skeletonAttack_01.wav"));
        meteorShower = Gdx.audio.newSound(Gdx.files.internal("sounds/meteorShower.mp3"));
    }

    private void makeSpellTextures() {
        texSpellFireBall = new Texture("spells/texFireball.png");
        texSpellFreez = new Texture("spells/texFreez.png");
        texSpellRage = new Texture("spells/texRage.png");
        texSpellSongOfGlory = new Texture("spells/texSongOfGlory.png");
        texSpellHaste = new Texture("spells/texHaste.png");
        texSpellCure = new Texture("spells/texCure.png");
        texSpellDiscouragement = new Texture("spells/texDiscouragement.png");
        texSpellFury = new Texture("spells/texFury.png");
        texSpellCharge = new Texture("spells/texCharge.png");
        texSpellFinalJudgment = new Texture("spells/texFinalJudgment.png");
        texSpellThunder = new Texture("spells/texThunder.png");
        texSpellMeteorShower = new Texture("spells/texMeteorShower.png");
        texSpellBless = new Texture("spells/texBless.png");
        texSpellPrayer = new Texture("spells/texPrayer.png");
        texSpellPoison = new Texture("spells/texPoison.png");
        texSpellSummonBear = new Texture("spells/texSummonBear.png");
        texSpellSummonWolf = new Texture("spells/texSummonWolf.png");
        texSpellVampireTouch = new Texture("spells/texVampireTouch.png");
        texSpellLongShot = new Texture("spells/texLongShot.png");
    }

    /**
     * Zwraca Spell Actor z panelem czarów
     *
     * @return
     */
    public SpellActor getSpellPanel() {
        Pixmap pmSpellPanel = new Pixmap(Gdx.graphics.getWidth(), 100, Pixmap.Format.RGBA8888);
        pmSpellPanel.setColor(Color.LIGHT_GRAY);
        pmSpellPanel.fillRectangle(0, 0, 500, 60);
        //pmGornaBelka.fillRectangle(Gdx.graphics.getWidth() - 250, 0, 250, Gdx.graphics.getHeight());
        //pmGornaBelka.setColor(Color.BLACK);
        // czarna ramka wokół mapy 
        //pmGornaBelka.drawRectangle(0, 30, Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 30);
        //pmGornaBelka.drawRectangle(1, 31, Gdx.graphics.getWidth() - 252, Gdx.graphics.getHeight() - 2 - 30);

        Texture texSpellPanel = new Texture(pmSpellPanel);

        SpellActor spellPanel = new SpellActor(texSpellPanel, 250, 10);

        return spellPanel;
    }

    // wypełnia mapę 
    private void fillMap() {
        // wypełnienie mapy trawą
        for (int i = 0; i < 100; i++) {
            mapa[i] = 1;
        }
        // dodanie zamków
        mapa[0] = 3;
        mapa[99] = 3;
        // dodanie gór
        mapa[5] = 2;
        mapa[6] = 2;
        mapa[18] = 2;
        mapa[19] = 2;
        mapa[43] = 2;
        mapa[66] = 2;
        mapa[67] = 2;
    }

    public Window getInfoWindow() {
        return infoWindow;
    }

    public void setInfoWindow(Window infoWindow) {
        this.infoWindow = infoWindow;
    }

    private void makeMobs() {
        this.texWarrior0 = new Texture("moby/warrior/0.png");
        this.texWarrior1 = new Texture("moby/warrior/1.png");

        this.texWilkMob = new Texture("moby/mobWolfTex.png");
        this.texSzkieletMob = new Texture("moby/mobSzkieletfTex.png");
        this.texSpiderMob = new Texture("moby/mobSpiderTex.png");
        this.texZombieMob = new Texture("moby/mobZombieTex.png");
    }
}
