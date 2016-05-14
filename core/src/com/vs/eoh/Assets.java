package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.vs.enums.AnimsTypes;
import com.vs.enums.CzesciCiala;
import com.vs.network.Network;
import com.vs.screens.MultiplayerScreen;

import java.util.ArrayList;

import javax.xml.soap.Text;

public class Assets {

    public static Stage stage01MapScreen;
    public static Stage stage02MapScreen;
    public static Stage stage03MapScreen;
    public static Screen testScreen;
    public static Screen mainMenuScreen;
    public static Screen newGameScreen;
    public static Screen mapScreen;
    public static Screen bohaterScreen;
    public static Screen itemScreen;
    public static Screen lastScreen;
    public static Screen newBohaterScreen;
    public static Screen mapEditor;
    public static Screen awansScreen;
    public static Screen multiplayerScreen;
    public static Screen gameOverScreen;
    public static Server server;
    public static Client client;
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

    public Sound swordSound;
    // predefiniowane okno ifnoramcyjne
    private Window infoWindow;

    public Assets() {

        tAtals = new TextureAtlas(Gdx.files.internal("terrain/test.atlas"));

        tAtlasWarrior = new TextureAtlas(Gdx.files.internal("moby/warrior/warrior.atlas"));
        tAtlasWizard = new TextureAtlas(Gdx.files.internal("moby/wizard/wizard.atlas"));
        tAtlasHunter = new TextureAtlas(Gdx.files.internal("moby/hunter/hunter.atlas"));
        
        aM = new AssetManager();
        //aM.load("mapa.dat", Mapa.class);
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
        //mobHumanTex = new Texture("mobHumanTex.png");
        //mobHumanTexZaznaczony = new Texture("mobHumanTexZaznaczony.png");
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

        //makeSounds();

        makeItems();
        makeSpellTextures();

        makeMobs();

        fillMap();
        utworzInfoWindow();

    }

    /**
     * Animuje label obrażeń oraz uruchamia procedurę liczenia dmg. dla BohVSBoh
     *
     * @param pozX
     * @param pozY
     * @param bohaterAtakujacy
     * @param bohaterBroniacy
     * @param spell
     */
    public void animujSpellLblDmg(float pozX, float pozY, Bohater bohaterAtakujacy, Bohater bohaterBroniacy, SpellActor spell) {
        lblDmg.setText("Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterAtakujacy, bohaterBroniacy, spell)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
    }

    /**
     * Animuje label obrażeń oraz uruchamia procedurę liczenia dmg. dla BohVSMob
     *
     * @param pozX
     * @param pozY
     * @param bohaterAtakujacy
     * @param mob
     * @param spell
     */
    public void animujSpellLblDmg(float pozX, float pozY, Bohater bohaterAtakujacy, Mob mob, SpellActor spell) {
        lblDmg.setText(Integer.toString(Fight.getSpellObrazenia(bohaterAtakujacy, mob, spell)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
    }

    /**
     * Animuje etykietę obrażeń oraz uruchamia procedurę liczenia obrażeń dla bohater vs zamek.
     * @param pozX
     * @param pozY
     * @param bohaterAtakujacy
     * @param castle
     * @param spell
     */
    public void animujSpellLblDmg(float pozX, float pozY, Bohater bohaterAtakujacy, Castle castle, SpellActor spell) {
        lblDmg.setText(Integer.toString(Fight.getSpellObrazenia(bohaterAtakujacy, castle, spell)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
    }

    /**
     * Animuje etykietę obrażeń z zadanym Stringiem obrażeń.
     *
     * @param pozX Pozycja X gdzie ma być ustawiona etykieta.
     * @param pozY Pozycja Y gdzie ma być ustawiona etykieta.
     * @param dmg  String z ilością obrażeń.
     */
    public void animujLblDamage(float pozX, float pozY, String dmg) {

        Label label = new Label("", skin);

        Assets.stage01MapScreen.addActor(label);

        label.setText("Dmg: " + dmg);
        label.setPosition(pozX - 50, pozY - 25);
        label.setFontScale(1.5f);
        label.addAction(Actions.alpha(1));
        label.addAction(Actions.fadeOut(2.0f));
        label.addAction(Actions.moveBy(0, 175, 2.0f));
        label.act(Gdx.graphics.getDeltaTime());


//        lblDmg.setText("Dmg: " + dmg);
//        lblDmg.setPosition(pozX - 50, pozY - 25);
//        lblDmg.setFontScale(1.5f);
//        lblDmg.addAction(Actions.alpha(1));
//        lblDmg.addAction(Actions.fadeOut(2.0f));
//        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
//        lblDmg.act(Gdx.graphics.getDeltaTime());
    }

    /**
     * Funkcja animuje Labelkę od wyświetlania obrażeń
     *
     * @param pozX pozycja x gdzie ma być ustawiona labelka
     * @param pozY pozycja y gdzie ma być ustawiona labelka
     * @param bohaterAtakujacy referencja do obiketu bohatera atakującego
     * @param bohaterBroniacy referencja do obiektu bohatera broniącego
     */
    public void animujLblDmg(float pozX, float pozY, Bohater bohaterAtakujacy, Bohater bohaterBroniacy) {
        lblDmg.setText("Dmg: " + Integer.toString(Fight.getObrazenia(bohaterAtakujacy, bohaterBroniacy)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
        this.animujCiecie((int) bohaterBroniacy.getX(), (int) bohaterBroniacy.getY());
    }

    public void animujLblDmgNetwork(float pozX, float pozY, int damage) {
        lblDmg.setText("Dmg: " + damage);
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
    }

    public void animujLblDmg(float pozX, float pozY, Bohater bohaterAtakujacy, Castle castle) {
        lblDmg.setText("Dmg: " + Integer.toString(Fight.getObrazenia(bohaterAtakujacy, castle)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
        this.animujCiecie((int) castle.getX(), (int) castle.getY());
    }

    public void animujLblDmg(float pozX, float pozY, Mob mob, Bohater bohaterBroniacy) {
        lblDmg.setText("Dmg: " + Integer.toString(Fight.getObrazenia(mob, bohaterBroniacy)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
        this.animujCiecie((int) bohaterBroniacy.getX(), (int) bohaterBroniacy.getY());
    }

    /**
     *
     * @param pozX
     * @param pozY
     * @param bohaterAtakujacy
     * @param mob
     */
    public void animujLblDmg(float pozX, float pozY, Bohater bohaterAtakujacy, Mob mob) {
        lblDmg.setText("Dmg: " + Integer.toString(Fight.getObrazenia(bohaterAtakujacy, mob)));
        lblDmg.setPosition(pozX - 50, pozY - 25);
        lblDmg.setFontScale(1.5f);
        lblDmg.addAction(Actions.alpha(1));
        lblDmg.addAction(Actions.fadeOut(2.0f));
        lblDmg.addAction(Actions.moveBy(0, 175, 2.0f));
        lblDmg.act(Gdx.graphics.getDeltaTime());
        this.animujCiecie((int) mob.getX(), (int) mob.getY());
    }

    /**
     * Animuje cięcie miecza w momencie ataku.
     *
     * @param locX
     * @param locY
     */
    public void animujCiecie(int locX, int locY) {
        AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.SlashAnimation));
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
     * Pokazuje okno z informacjami dla Tresure Box
     *
     * @param tresureBox Referencja do obiektu TresureBox którego itemy mają być
     * wyświetlone w oknie informacyjnym
     * @param bohater Referencja do obiketu bohatera do którego ekwipunku
     * dodawane będą itemki z tresure boxa
     * @param gs
     */
    public void pokazInfoWindow(final TresureBox tresureBox, final Bohater bohater, final GameStatus gs) {
        infoWindow.setZIndex(200);
        infoWindow.setVisible(true);

        // Tymczasowa ArrayLista przechowująca TextButtony
        final ArrayList<TextButton> tmpButtons = new ArrayList<TextButton>();

        for (int i = 0; i < tresureBox.getDostepneItemy().size(); i++) {
            infoWindow.add(tresureBox.getDostepneItemy().get(i).getNazwa());
            infoWindow.add(new Image(tresureBox.getDostepneItemy().get(i).getSprite().getTexture())).size(50, 50).pad(2);
            tmpButtons.add(new TextButton("TAKE IT", skin));
            tmpButtons.get(i).addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("przycisk TAKE IT kliknięty");
                    for (int i = 0; i < tmpButtons.size(); i++) {
                        if (tmpButtons.get(i).isPressed()) {
                            // Sprawdzenie czy itemek jest złotem
                            if (tresureBox.getDostepneItemy().get(i).getCzescCiala().equals(CzesciCiala.gold)) {
                                gs.dodajDoZlotaAktualnegoGracza(tresureBox.getDostepneItemy().get(i).getGold());
                                tresureBox.getDostepneItemy().remove(i);
                                tmpButtons.get(i).remove();
                                ukryjInfoWindow();
                                pokazInfoWindow(tresureBox, bohater, gs);
                                // Jeżeli nie jest złotem
                            } else {
                                tmpButtons.get(i).remove();
                                // dodanie itemka z tresureboxa do ekwipunku
                                bohater.getEquipment().add(tresureBox.getDostepneItemy().get(i));
                                // usuniecie wybranego itemka z trasureboxa
                                tresureBox.getDostepneItemy().remove(i);
                                // aktualizacja okna
                                ukryjInfoWindow();
                                pokazInfoWindow(tresureBox, bohater, gs);
                            }
                        }
                    }
                    return false;
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("przycisk TAKE IT kliknięty");
                    for (int i = 0; i < tmpButtons.size(); i++) {
                        if (tmpButtons.get(i).isPressed()) {
                            // Sprawdzenie czy itemek jest złotem
                            if (tresureBox.getDostepneItemy().get(i).getCzescCiala().equals(CzesciCiala.gold)) {
                                gs.dodajDoZlotaAktualnegoGracza(tresureBox.getDostepneItemy().get(i).getGold());
                                tresureBox.getDostepneItemy().remove(i);
                                tmpButtons.get(i).remove();
                                ukryjInfoWindow();
                                pokazInfoWindow(tresureBox, bohater, gs);
                                // Jeżeli nie jest złotem
                            } else {
                                tmpButtons.get(i).remove();
                                // dodanie itemka z tresureboxa do ekwipunku
                                bohater.getEquipment().add(tresureBox.getDostepneItemy().get(i));
                                // usuniecie wybranego itemka z trasureboxa
                                tresureBox.getDostepneItemy().remove(i);
                                // aktualizacja okna
                                ukryjInfoWindow();
                                pokazInfoWindow(tresureBox, bohater, gs);
                            }
                        }
                    }
                }
            });
            infoWindow.add(tmpButtons.get(i));
            infoWindow.row();
        }

        // tymczasowy przycisk Exit dodany do okna InfoWindow
        TextButton tmpExitBtn = new TextButton("EXIT", skin);
        tmpExitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                TresureBox.removeTresureBox(tresureBox, gs.getMapa());

                if (gs.getNetworkStatus() == 2) {
                    Network.RemoveTresureBox removeTresureBox = new Network.RemoveTresureBox();
                    removeTresureBox.pozX = tresureBox.getPozX();
                    removeTresureBox.pozY = tresureBox.getPozY();
                    GameStatus.client.getCnt().sendTCP(removeTresureBox);
                }

                bohater.setOtwartaSkrzyniaZeSkarbem(false);
                ukryjInfoWindow();
            }
        });

        infoWindow.add(tmpExitBtn);
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
        texHelthPotion = new Texture("items/texHealthPotion.png");
        texSpeedPotion = new Texture("items/texSpeedPotion.png");
        texAttackPotion = new Texture("items/texAttackPotion.png");
        texDefencePotion = new Texture("items/texDefencePotion.png");
    }

    private void makeSounds() {
        swordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_01.aif"));
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
