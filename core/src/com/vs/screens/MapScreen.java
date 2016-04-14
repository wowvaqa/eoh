package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.enums.Spells;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.ButtonActor;
import com.vs.eoh.Castle;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.Ruch;
import com.vs.eoh.SpellActor;
import com.vs.eoh.SpellCreator;
import com.vs.eoh.TresureBox;

import java.util.ArrayList;
import java.util.EventListener;

public class MapScreen implements Screen {

    private final OrthographicCamera c;
    private final FitViewport viewPort;
    private final Assets a;
    private final GameStatus gs;
    private final Game g;
    private final Stage stage01 = new Stage();  // wyświetla mapę i playera
    private final Stage stage02 = new Stage();  // zarządza przyciskami interfejsu
    private final Stage stage03 = new Stage();  // zarządza czarami
    private final TextButton btnExit;
    private final TextButton btnKoniecTury;
    private final TextButton btnKupBohatera;
    //private final ArrayList<DefaultActor> teren = new ArrayList<DefaultActor>();
    private final ArrayList<Image> teren = new ArrayList<Image>();
    // labele informujące o statystykach klikniętego bohatera
    private final Label lblGold;
    private final Label lblTuraGracza;
    // ikona gracza który aktualnie posiada swoją kolej
    private final DefaultActor ikonaGracza;
    private final DefaultActor ikonaGold;
    Tables tables = new Tables();
    private InputMultiplexer inputMultiPlexer = new InputMultiplexer();
    private MyGestureListener myGL = null;
    private MyGestureDetector myGD = null;
    private DefaultActor gornaBelka;
    // *** PANEL ZAKLĘĆ
    //private boolean isSpellPanelActive = false;
    // *** KONIEC PANEL ZAKLĘĆ
    // *** PANEL BOHATERA
    private Label pbLblHp;
    private Label pbLblMove;
    private Label pbLblMana;
    private Label pbLblExp;
    private Label pbLblEfekty;
    private TextButton pbBtnBohaterScreen;
    private TextButton pbBtnAwansujBohatera;
    private TextButton pbBtnSpellBook;
    // *** KONIEC PANELU BOHATERA

    /**
     * Przechowuje efekty które oddziaływują na bohatera
     *
     * @param g
     * @param a
     * @param gs
     */
    public MapScreen(final Game g, final Assets a, final GameStatus gs) {
        this.a = a;
        this.gs = gs;
        this.g = g;

        myGL = new MyGestureListener(stage01);
        myGD = new MyGestureDetector(myGL);

        utworzPanelBohatera();
        utworzInterfejs();

        ikonaGracza = new DefaultActor(a.btnAttackTex, 0, 0);
        ikonaGracza.getSprite().setTexture(gs.gracze.get(gs.getTuraGracza()).getTeksturaIkonyGracza());
        ikonaGracza.setPosition(110, Gdx.graphics.getHeight() - 23);

        ikonaGold = new DefaultActor(a.texGold, 165, Gdx.graphics.getHeight() - 25);
        ikonaGold.setSize(25, 25);

        Assets.stage01MapScreen = this.stage01;
        Assets.stage02MapScreen = this.stage02;
        Assets.stage03MapScreen = this.stage03;

        btnKupBohatera = new TextButton("Kup bohatera", a.skin);
        btnKupBohatera.setSize(100, 50);
        btnKupBohatera.setPosition(Gdx.graphics.getWidth() - btnKupBohatera.getWidth() - 25, 175);
        btnKupBohatera.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Dodanie nowego bohatera.");
                if (gs.getGracze().get(gs.getTuraGracza()).getGold() < 10) {
                    System.out.println("Za mało złota!");
                    DialogScreen dialogScreen = new DialogScreen("ERROR", a.skin, "Za malo zlota", stage01);
                } else {
                    g.setScreen(Assets.newBohaterScreen);
                }
            }
        });

        // Dodaje przycisk wyjścia do planszy 02.
        btnExit = new TextButton("EXIT", a.skin);
        btnExit.setSize(100, 50);
        btnExit.setPosition(Gdx.graphics.getWidth() - btnExit.getWidth() - 25, 25);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitClick();
            }
        });

        // Przycisk Koniec Tury
        // 1. Zmienia turę gracza na następnego gracza
        // 2. Sprawdza czy rozmiar tablicy graczy nie został przekroczony
        // jeżeli TRUE ustawia turę gracza na pierwszego z tablicy graczy
        // 3. Wyrzuca do labela aktualną turę gracza.
        btnKoniecTury = new TextButton("Koniec Tury", a.skin);
        btnKoniecTury.setSize(100, 50);
        //btnKoniecTury.setPosition(Gdx.graphics.getWidth() - btnKoniecTury.getWidth() - 25, 125);
        btnKoniecTury.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                koniecTuryClick();
            }
        });

        // Dodanie etykiet prezentujących statsy graczy
        lblTuraGracza = new Label("Tura gracz: " + gs.getTuraGracza(), a.skin);
        lblTuraGracza.setPosition(10, Gdx.graphics.getHeight() - 25);
//        lblPozostaloRuchow = new Label("Pozostalo ruchow: ", a.skin);
//        lblPozostaloRuchow.setPosition(150, Gdx.graphics.getHeight() - 25);

        lblGold = new Label("" + gs.getGracze().get(gs.getTuraGracza()).getGold(), a.skin);
        lblGold.setPosition(200, Gdx.graphics.getHeight() - 25);

        dodajDoStage02();

        generujPlansze();
        generujGraczy();

        dodajDoStage01();

        gs.czyUtworzonoMape = true;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        c = new OrthographicCamera(w, h);
        viewPort = new FitViewport(w, h, c);
    }
//
//    /**
//     * Aktualizuje ikony efektów które działają na zaznaczonego bohatera.
//     */
//    public void aktualizujEfektyBohatera() {
//        if (gs.getBohaterZaznaczony() != null) {
//            stage02.addActor(gs.getBohaterZaznaczony().getEfekty().get(0).getIkona());
//        }
//    }

    /**
     * Przycisk Koniec Tury
     */
    private void koniecTuryClick() {

        if (!sprawdzCzyGraczPosiadaZamek()) {
            System.out.println("Sprawdzanie czy gracz posiada zamek.");
            // Jeżeli gracz nie będzie posiadał zamku wtedy zmianie ulegnie jego
            // status oraz zwiększona zostanie ilość tur bez zamku
            gs.getGracze().get(gs.getTuraGracza()).setStatusBezZamku(true);
            gs.getGracze().get(gs.getTuraGracza()).setTuryBezZamku(gs.getGracze().get(gs.getTuraGracza()).getTuryBezZamku() + 1);
            if (gs.getGracze().get(gs.getTuraGracza()).getTuryBezZamku() >= 5) {
                System.out.println("Gracz: " + gs.getTuraGracza() + " kaput");
                gs.getGracze().get(gs.getTuraGracza()).setStatusGameOver(true);
            }
            System.out.println("Gracz " + gs.getTuraGracza() + " nie posiada zamku: "
                    + gs.getGracze().get(gs.getTuraGracza()).getTuryBezZamku() + " tur.");
        }
        wylaczAktywnychBohaterow();
        sprawdzCzyKoniecTuryOgolnej();
        System.out.println("Koniec Tury");
        // Ustala turę następnego gracza
        gs.setTuraGracza(gs.getTuraGracza() + 1);
        if (gs.getTuraGracza() > gs.getGracze().size() - 1) {
            gs.setTuraGracza(0);
        }
        lblTuraGracza.setText("Tura gracz: " + Integer.toString(gs.getTuraGracza()));

        // Przywrócenie wszystkich punktów ruchu dla bohaterów oraz aktualizacja czasu działania efektów
        // Regenereacja many
        for (Bohater i : gs.getGracze().get(gs.getTuraGracza()).getBohaterowie()) {
            i.setPozostaloRuchow(i.getSzybkosc()
                    + /**
             * Fight.getSzybkoscEkwipunkuBohatera(i) + *
             */
                    i.getSzybkoscEfekt());
            i.aktualizujDzialanieEfektow();
            i.czyscEfektyTymczasowe();

            i.setActualMana(i.getActualMana() + i.getManaRegeneration());
            if (i.getActualMana() > i.getMana()) {
                i.setActualMana(i.getMana());
            }
        }

        //  Aktualizuje działanie efektów czarów moba.
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                if (gs.getMapa().getPola()[i][j].getMob() != null) {
                    gs.getMapa().getPola()[i][j].getMob().aktualizujDzialanieEfektow();
                }
            }
        }

        usunBohaterowGraczyGO();

        przesunKamereNadBohatera();

        // zmiana ikony gracza na górnej belce
        this.ikonaGracza.getSprite().setTexture(gs.gracze.get(gs.getTuraGracza()).getTeksturaIkonyGracza());

        Ruch.wylaczIkonyEfektow();
    }

    /**
     * Przesuwa kamerę nad bohatera
     */
    public void przesunKamereNadBohatera() {
        Camera cam = stage01.getCamera();
        System.out.println(cam.position);
        float xCord = gs.getGracze().get(gs.getTuraGracza()).getBohaterowie().get(0).getX();
        float yCord = gs.getGracze().get(gs.getTuraGracza()).getBohaterowie().get(0).getY();

        cam.translate(xCord - cam.position.x + 200, yCord - cam.position.y + 100, 0);
    }

    /**
     * Usuwa bohaterów graczy którzy mają status Game Over
     */
    private void usunBohaterowGraczyGO() {
        if (gs.getGracze().get(gs.getTuraGracza()).isStatusGameOver()) {
            if (gs.getGracze().get(gs.getTuraGracza()).getBohaterowie().size() > 0) {
                for (int i = 0; i < gs.getGracze().get(gs.getTuraGracza()).getBohaterowie().size(); i++) {
                    System.out.println("Usunięcie bohatera");
                    gs.getGracze().get(gs.getTuraGracza()).getBohaterowie().remove(i);

                }
            }
            for (int x = 0; x < gs.getMapa().getIloscPolX(); x++) {
                for (int y = 0; y < gs.getMapa().getIloscPolY(); y++) {
                    if (gs.getMapa().getPola()[x][y].getBohater() != null
                            && gs.getMapa().getPola()[x][y].getBohater().getPrzynaleznoscDoGracza() == gs.getTuraGracza()) {
                        System.out.println("XXX");
                        gs.getMapa().pola[x][y].getBohater().remove();
                        gs.getMapa().pola[x][y].setBohater(null);
                    }
                }
            }
            koniecTuryClick();
        }
    }

    /**
     * Sprawdza czy gracz posiada zamek. Zwraca True jeżeli posiada False jeżeli
     * nie posiada
     *
     * @return
     */
    private boolean sprawdzCzyGraczPosiadaZamek() {
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                if (gs.getMapa().getPola()[i][j].getCastle() != null) {
                    if (gs.getMapa().getPola()[i][j].getCastle().getPrzynaleznoscDoGracza() == gs.getTuraGracza()) {
                        System.out.println("Gracz posiada zamek.");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Funkcja wyłącza aktywnych bohaterów
     */
    private void wylaczAktywnychBohaterow() {
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                if (gs.getMapa().getPola()[i][j].getBohater() != null) {
                    gs.getMapa().getPola()[i][j].getBohater().setZaznaczony(false);
                    gs.getMapa().getPola()[i][j].getBohater().getSprite().setTexture(gs.getMapa().getPola()[i][j].getBohater().getBohaterTex());
                    Ruch.wylaczPrzyciski();
                }
            }
        }
        gs.setCzyZaznaczonoBohatera(false);
    }

    // Działania wywołane po naciśnięciu przycisku Exit
    private void exitClick() {
        g.setScreen(Assets.mainMenuScreen);
        gs.setActualScreen(0);
    }

    /**
     * Funkcja sprawdza czy tura gry nie została zakończona Jezeli TRUE zwiększa
     * +1
     */
    private void sprawdzCzyKoniecTuryOgolnej() {
        if (gs.getTuraGracza() == gs.iloscGraczy - 1) {
            System.out.println("Koniec tury ogólnej");
            gs.setTuraGry(gs.getTuraGry() + 1);
            odnowZdrowieZamkow();
            odnowZdrowieBohaterow();
        }
    }

    /**
     * Odnawia co turę gry HP bohaterów + 1
     */
    private void odnowZdrowieBohaterow() {
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                if (gs.getMapa().getPola()[i][j].getBohater() != null
                        && gs.getMapa().getPola()[i][j].getBohater().getActualHp()
                        < gs.getMapa().getPola()[i][j].getBohater().getHp()) {
                    System.out.println("Bohater odnawia życie");
                    gs.getMapa().getPola()[i][j].getBohater().setActualHp(
                            gs.getMapa().getPola()[i][j].getBohater().getActualHp() + 1);
                    gs.getMapa().getPola()[i][j].getBohater().aktualizujTeksture();
                }
            }
        }
    }

    /**
     * Odnawia co turę gry HP zamków +1
     */
    private void odnowZdrowieZamkow() {
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                if (gs.getMapa().getPola()[i][j].getCastle() != null
                        && gs.getMapa().getPola()[i][j].getCastle().getActualHp()
                        < gs.getMapa().getPola()[i][j].getCastle().getMaxHp()) {
                    System.out.println("Zamek odnawia życie");
                    gs.getMapa().getPola()[i][j].getCastle().setActualHp(
                            gs.getMapa().getPola()[i][j].getCastle().getActualHp() + 1);
                }
            }
        }
    }

    /**
     * Tworzy panel bohatera.
     */
    private void utworzPanelBohatera() {
        pbLblHp = new Label("HP: X/X", a.skin);
        pbLblMove = new Label("MV: X/X", a.skin);
        pbLblMana = new Label("MN: X/X", a.skin);
        pbLblExp = new Label("EX: X/X", a.skin);
        pbLblEfekty = new Label("EFEKTY:", a.skin);

        pbLblHp.setPosition(Gdx.graphics.getWidth() - 220, 500);
        pbLblMove.setPosition(Gdx.graphics.getWidth() - 110, 500);
        pbLblMana.setPosition(Gdx.graphics.getWidth() - 220, 475);
        pbLblExp.setPosition(Gdx.graphics.getWidth() - 110, 475);

        pbLblEfekty.setPosition(Gdx.graphics.getWidth() - pbLblEfekty.getWidth() - 100, 450);

        pbBtnSpellBook = new TextButton("Spell Book", a.skin);
        pbBtnSpellBook.setSize(100, 50);
        pbBtnSpellBook.setPosition(Gdx.graphics.getWidth() - 220, 360);
        pbBtnSpellBook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!gs.isSpellPanelActive) {
                    stage03.addActor(a.getSpellPanel());
                    gs.isSpellPanelActive = true;

                    TextButton btnSpellPanelExit = new TextButton("EXIT", a.skin);
                    btnSpellPanelExit.setPosition(695, 54);
                    btnSpellPanelExit.setSize(50, 50);

                    btnSpellPanelExit.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            stage03.clear();
                            gs.isSpellPanelActive = false;
                            //Gdx.input.setInputProcessor(stage01);
                            gs.getBohaterZaznaczony().getSpells().clear();
                        }
                    });
                    stage03.addActor(btnSpellPanelExit);

                    int spellXpos = 254;
                    int spellYpos = 54;

                    SpellCreator spellCreator = new SpellCreator(a, gs);

                    for (Spells spl : gs.getBohaterZaznaczony().getListOfSpells()) {
                        gs.getBohaterZaznaczony().getSpells().add(spellCreator.utworzSpell(spl, gs.getBohaterZaznaczony()));
                    }

                    for (SpellActor sA : gs.getBohaterZaznaczony().getSpells()) {
                        sA.setPosition(spellXpos, spellYpos);
                        stage03.addActor(sA);
                        spellXpos += 52;
                    }
                }
            }
        });

        // Dodaje przycisk wyjścia do planszy 02.
        pbBtnBohaterScreen = new TextButton("Bohater", a.skin);
        pbBtnBohaterScreen.setSize(100, 50);
        pbBtnBohaterScreen.setPosition(Gdx.graphics.getWidth() - 110, 360);
        pbBtnBohaterScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gs.isCzyZaznaczonoBohatera()) {
                    //gs.setActualScreen(5);
                    g.setScreen(Assets.bohaterScreen);
                } else {
                    System.out.println("Nie zaznaczono bohatera");
                }
            }
        });

        pbBtnAwansujBohatera = new TextButton("Awans", a.skin);
        pbBtnAwansujBohatera.setSize(210, 50);
        pbBtnAwansujBohatera.setPosition(Gdx.graphics.getWidth() - 220, 300);
        pbBtnAwansujBohatera.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pbBtnAwansujBohatera.setVisible(false);
                g.setScreen(Assets.awansScreen);
            }
        });
    }

    private void aktualizujPanelBohatera() {
        Bohater b = gs.getBohaterZaznaczony();
        pbLblHp.setVisible(true);
        pbLblHp.setText("HP:" + b.getActualHp() + "/" + b.getHp());
        pbLblMove.setVisible(true);
        pbLblMove.setText("MV:" + b.getPozostaloRuchow() + "/" + b.getSzybkosc());
        pbLblExp.setVisible(true);
        pbLblExp.setText("EX:" + b.getExp() + "/" + b.getExpToNextLevel());
        pbLblMana.setVisible(true);
        pbLblMana.setText("MN:" + b.getActualMana() + "/" + b.getMana());

        pbLblEfekty.setVisible(true);
        pbBtnBohaterScreen.setVisible(true);
        pbBtnSpellBook.setVisible(true);
    }

    /**
     * Tworzy wygląd interfejsu
     */
    private void utworzInterfejs() {
        Pixmap pmGornaBelka = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pmGornaBelka.setColor(Color.LIGHT_GRAY);
        pmGornaBelka.fillRectangle(0, 0, Gdx.graphics.getWidth(), 30);
        pmGornaBelka.fillRectangle(Gdx.graphics.getWidth() - 250, 0, 250, Gdx.graphics.getHeight());
        pmGornaBelka.setColor(Color.BLACK);
        // czarna ramka wokół mapy 
        pmGornaBelka.drawRectangle(0, 30, Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 30);
        pmGornaBelka.drawRectangle(1, 31, Gdx.graphics.getWidth() - 252, Gdx.graphics.getHeight() - 2 - 30);
        // imitacja miniMapy
        pmGornaBelka.fillRectangle(Gdx.graphics.getWidth() - 240, 30, 230, 200);
        pmGornaBelka.setColor(Color.WHITE);
        pmGornaBelka.drawRectangle(Gdx.graphics.getWidth() - 240, 30, 230, 200);

        Texture texGornaBelka = new Texture(pmGornaBelka);

        gornaBelka = new DefaultActor(texGornaBelka, 0, Gdx.graphics.getHeight() - texGornaBelka.getHeight());
    }

    // Dodaje do Stage 02 przycisk Exit i koniec tury oraz labele wyświetlające statystyki
    private void dodajDoStage02() {
//        stage02.addActor(gornaBelka);
//        stage02.addActor(lblTuraGracza);
//        stage02.addActor(btnExit);
//        stage02.addActor(btnKoniecTury);
//        stage02.addActor(btnKupBohatera);
//        stage02.addActor(lblGold);
//        stage02.addActor(ikonaGracza);
//        stage02.addActor(ikonaGold);

        tables.tabUpBar.add(lblTuraGracza).padRight(5);
        tables.tabUpBar.add(ikonaGracza).padRight(10);
        tables.tabUpBar.add(ikonaGold).padRight(5);
        tables.tabUpBar.add(lblGold).padRight(5);

//        //Panel Bohatera
//        stage02.addActor(pbLblHp);
//        stage02.addActor(pbLblMove);
//        stage02.addActor(pbLblMana);
//        stage02.addActor(pbLblExp);
//        stage02.addActor(pbLblEfekty);
//        stage02.addActor(pbBtnBohaterScreen);
//        stage02.addActor(pbBtnAwansujBohatera);
//        stage02.addActor(pbBtnSpellBook);

        tables.tabLeftBar.add(pbLblHp).size(100, 25).pad(5);
        tables.tabLeftBar.add(pbLblMove).size(100, 25).pad(5);
        tables.tabLeftBar.row();
        tables.tabLeftBar.add(pbLblMana).size(100, 25).pad(5);
        tables.tabLeftBar.add(pbLblExp).size(100, 25).pad(5);
        tables.tabLeftBar.row();
        tables.tabLeftBar.add(pbBtnBohaterScreen).bottom().size(100, 50).pad(5);
        tables.tabLeftBar.add(pbBtnSpellBook).bottom().size(100, 50).pad(5);
        tables.tabLeftBar.row();
        tables.tabLeftBar.add(pbBtnAwansujBohatera).size(210, 50).pad(5).colspan(2);
        tables.tabLeftBar.row();
        tables.tabLeftBar.add(btnKoniecTury).bottom().size(100, 50).pad(5).expandY();
        tables.tabLeftBar.add(btnKupBohatera).bottom().size(100, 50).pad(5).expandY();
        tables.tabLeftBar.row();
        tables.tabLeftBar.add(btnExit).align(Align.bottom).size(210, 50).pad(5).colspan(2);

        stage02.addActor(tables.getTabInterface());

    }

    // Dodaj do stage 01 predefiniowane przyciski ruchu i ataku oraz przycisk cancel
    private void dodajDoStage01() {
        // Dodaje do planszy info window z assetów do wyświetlania info o skrzynce ze skarbem
        stage01.addActor(a.getInfoWindow());

        stage01.addActor(a.lblDmg);
    }

    // Gneruje graczy w konstruktorze klasy i dodaje ich do planszy 01
    private void generujGraczy() {

        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolX(); j++) {

                if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP1()) {

                    gs.getMapa().getPola()[i][j].setCastle(new Castle(a, i * 100, j * 100, 0));
                    stage01.addActor(gs.getMapa().getPola()[i][j].getCastle());

                    gs.getGracze().get(0).getBohaterowie().get(0).setPozXnaMapie(i);
                    gs.getGracze().get(0).getBohaterowie().get(0).setPozYnaMapie(j);
                    gs.getGracze().get(0).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                    stage01.addActor(gs.getGracze().get(0).getBohaterowie().get(0));
                }
                if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP2()) {

                    gs.getMapa().getPola()[i][j].setCastle(new Castle(a, i * 100, j * 100, 1));
                    stage01.addActor(gs.getMapa().getPola()[i][j].getCastle());

                    gs.getGracze().get(1).getBohaterowie().get(0).setPozXnaMapie(i);
                    gs.getGracze().get(1).getBohaterowie().get(0).setPozYnaMapie(j);
                    gs.getGracze().get(1).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                    stage01.addActor(gs.getGracze().get(1).getBohaterowie().get(0));
                }
                if (gs.getGracze().size() == 3 || gs.getGracze().size() == 4) {

                    if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP3()) {

                        gs.getMapa().getPola()[i][j].setCastle(new Castle(a, i * 100, j * 100, 2));
                        stage01.addActor(gs.getMapa().getPola()[i][j].getCastle());

                        gs.getGracze().get(2).getBohaterowie().get(0).setPozXnaMapie(i);
                        gs.getGracze().get(2).getBohaterowie().get(0).setPozYnaMapie(j);
                        gs.getGracze().get(2).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                        stage01.addActor(gs.getGracze().get(2).getBohaterowie().get(0));
                    }
                }
                if (gs.getGracze().size() == 4) {
                    if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP4()) {

                        gs.getMapa().getPola()[i][j].setCastle(new Castle(a, i * 100, j * 100, 3));
                        stage01.addActor(gs.getMapa().getPola()[i][j].getCastle());

                        gs.getGracze().get(3).getBohaterowie().get(0).setPozXnaMapie(i);
                        gs.getGracze().get(3).getBohaterowie().get(0).setPozYnaMapie(j);
                        gs.getGracze().get(3).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                        stage01.addActor(gs.getGracze().get(3).getBohaterowie().get(0));
                    }
                }
            }
        }
    }

    /**
     * Generuje skrzynie ze skarbami.
     */
    private void tresureBoxGenerator() {
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolX(); j++) {

                if (gs.getMapa().getPola()[i][j].isTresureBox1Location()) {
                    TresureBox tb = new TresureBox(1, 2, this.a, this.gs, this.g, i * 100, j * 100);
                    gs.getMapa().getPola()[i][j].setTresureBox(tb);
                    stage01.addActor(gs.getMapa().getPola()[i][j].getTresureBox());
                } else if (gs.getMapa().getPola()[i][j].isTresureBox2Location()) {
                    TresureBox tb = new TresureBox(2, 1, this.a, this.gs, this.g, i * 100, j * 100);
                    gs.getMapa().getPola()[i][j].setTresureBox(tb);
                    stage01.addActor(gs.getMapa().getPola()[i][j].getTresureBox());
                }
            }
        }
    }

    /**
     * Generuje moby na mapie.
     */
    private void mobsGenerator() {
        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolX(); j++) {

                if (gs.getMapa().getPola()[i][j].isMob1Location()) {

                    Mob mob = new Mob(g, gs, a, i * 100, j * 100, 1, Mob.losujMoba(1));
                    gs.getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(gs.getMapa().getPola()[i][j].getMob());
                } else if (gs.getMapa().getPola()[i][j].isMob2Location()) {

                    Mob mob = new Mob(g, gs, a, i * 100, j * 100, 2, Mob.losujMoba(2));
                    gs.getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(gs.getMapa().getPola()[i][j].getMob());
                }
            }
        }
    }

    private Texture teksturaTerenu(TypyTerenu tT) {
        System.out.println(tT);
        switch (tT) {
            case Gory:
                return a.trawaGoraTex;
            case Trawa:
                return a.trawaTex;
            case Drzewo:
                return a.trawaDrzewoTex;
        }
        return a.trawaTex;
    }

    // wypełnia stage01 aktorami planszy
    private void generujPlansze() {

        for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                if (gs.getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Gory) {
                    gs.getMapa().getPola()[i][j].setMovable(false);
                }
                if (gs.getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Rzeka) {
                    Image img = new Image(a.tAtals.findRegion(Mapa.getTextureRegion(i, j, gs.getMapa(), TypyTerenu.Rzeka)));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                } else if (gs.getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Drzewo) {
                    Image img = new Image(a.tAtals.findRegion(Mapa.getTextureRegion(i, j, gs.getMapa(), TypyTerenu.Drzewo)));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                } else if (gs.getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Gory) {
                    Image img = new Image(a.tAtals.findRegion(Mapa.getTextureRegion(i, j, gs.getMapa(), TypyTerenu.Gory)));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                } else {
                    Image img = new Image(teksturaTerenu(gs.getMapa().getPola()[i][j].getTypTerenu()));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                }
            }
        }

        for (Image teren1 : teren) {
            stage01.addActor(teren1);
        }

        this.tresureBoxGenerator();
        this.mobsGenerator();
    }

    @Override
    public void render(float delta) {

        Gdx.input.setInputProcessor(inputMultiPlexer);

        if (gs.getBohaterZaznaczony() != null) {
            aktualizujPanelBohatera();
            if (gs.getBohaterZaznaczony().getExp() >= gs.getBohaterZaznaczony().getExpToNextLevel()) {
                pbBtnAwansujBohatera.setVisible(true);
            }
        } else {
            pbLblHp.setVisible(false);
            pbLblMove.setVisible(false);
            pbLblExp.setVisible(false);
            pbLblMana.setVisible(false);
            pbLblEfekty.setVisible(false);
            pbBtnAwansujBohatera.setVisible(false);
            pbBtnBohaterScreen.setVisible(false);
            pbBtnSpellBook.setVisible(false);
        }

        sortujZindex();

        //sprawdzPolozenieKursora();
        ruchKamery();
        //wyswietlStatystykiBohatera();
        this.lblGold.setText(Integer.toString(gs.getZlotoAktualnegoGracza()));

        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage01.act();
        stage01.draw();

        stage02.act();
        stage02.draw();

        stage03.act();
        stage03.draw();
    }

    // Sprawdza położenie kursora 
    // w zależności od lokalizacji ustawia sterowanie na odpowiedni stage
    private void sprawdzPolozenieKursora() {
        //Ustawia stage 02 na 1/6 ekranu z prawej strony
//        if (!gs.isSpellPanelActive) {
//            if (Gdx.input.getX() < Gdx.graphics.getWidth() / 6 * 5) {
//                Gdx.input.setInputProcessor(stage01);
//            } else {
//                Gdx.input.setInputProcessor(stage02);
//            }
//        }
    }

    // Steruje ruchem kamery
    private void ruchKamery() {

        int predkoscRuchuKamery = gs.getPredkoscScrollowaniaKamery();
        int predkoscZoom = gs.getPredkoscZoomKamery();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            stage01.getCamera().translate(-predkoscRuchuKamery, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            stage01.getCamera().translate(predkoscRuchuKamery, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            stage01.getCamera().translate(0, predkoscRuchuKamery, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            stage01.getCamera().translate(0, -predkoscRuchuKamery, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            stage01.getCamera().viewportHeight += predkoscZoom;
            stage01.getCamera().viewportWidth += predkoscZoom;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            stage01.getCamera().viewportHeight -= predkoscZoom;
            stage01.getCamera().viewportWidth -= predkoscZoom;
        }
    }

    /**
     * Metoda sortuje Zindex aktorów z ArrayList Stage01 ustawiając przyciski
     * jako te na 'górze'.
     */
    private void sortujZindex() {
        // Pętla szukająca przycisków
        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == ButtonActor.class) {
                stage01.getActors().get(i).toBack();
            }
        }

        // Pętla szukająca bohaterowie
        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == Bohater.class) {
                stage01.getActors().get(i).toBack();
            }
        }

        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == TresureBox.class) {
                stage01.getActors().get(i).toBack();
            }
        }

        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == Castle.class) {
                stage01.getActors().get(i).toBack();
            }
        }

        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == Mob.class) {
                stage01.getActors().get(i).toBack();
            }
        }

        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == DefaultActor.class) {
                stage01.getActors().get(i).toBack();
            }
        }

        for (int i = 0; i < stage01.getActors().size; i++) {
            if (stage01.getActors().get(i).getClass() == Image.class) {
                stage01.getActors().get(i).toBack();
            }
        }
    }

    // Setters and Getters
    public GameStatus getGs() {
        return gs;
    }

    @Override
    public void resize(int width, int height) {
        stage01.getViewport().update(width, height, true);
        stage02.getViewport().update(width, height, true);
        viewPort.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void show() {
        this.sortujZindex();
        inputMultiPlexer.addProcessor(myGD);
        inputMultiPlexer.addProcessor(stage03);
        inputMultiPlexer.addProcessor(stage02);
        inputMultiPlexer.addProcessor(stage01);
        Gdx.input.setInputProcessor(inputMultiPlexer);
    }

    // Setters and Getters
    public Stage getStage01() {
        return stage01;
    }

    /**
     *
     */
    public class MyGestureDetector extends GestureDetector {

        private GestureListener listener;

        public MyGestureDetector(GestureListener listner) {
            super(listner);
        }

        @Override
        public boolean isPanning() {
            return super.isPanning();
        }
    }

    /**
     * Listener przechwytujacy scroll
     */
    public class MyGestureListener implements GestureListener {

        private Stage stage;

        public MyGestureListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            stage.getCamera().translate(-deltaX, deltaY, 0);
            stage.getCamera().update();
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    }

    public class Tables {
        public Table tabInterface = new Table();
        public Table tabUpBar = new Table();
        public Table tabLeftBar = new Table();
        public Table tabSpells = new Table();

        private Texture tex = new Texture("background.jpg");
        Image img = new Image(tex);


        /**
         * Tworzy tabele interfejsu
         *
         * @return Referencje do tabeli.
         */
        public Table getTabInterface() {
            this.tempSettings();

            tabInterface.setFillParent(true);
            tabInterface.pad(0);
            tabInterface.setDebug(true);

            tabInterface.add(tabUpBar).align(Align.topLeft).expandX().top().maxHeight(25);
            tabInterface.row();
            tabInterface.add(tabLeftBar).align(Align.topRight).maxWidth(250).expandY().maxHeight(Gdx.graphics.getHeight() - 20);

            return tabInterface;
        }

        public Table getUpBarTable() {
            tabUpBar.setFillParent(true);
            tabUpBar.setDebug(true);
            tabUpBar.setSize(100, 100);
            tabUpBar.setPosition(100, 100);
            tabUpBar.setOriginX(100);
            tabUpBar.setOriginY(100);
            //tabUpBar.add(new Label("testowy labal UP BAR --------------------------------------", a.skin));
            return tabUpBar;
        }

        public void tempSettings() {
            //tabUpBar.add(new Label("testowy labal UP BAR -----------------", a.skin));

            tabLeftBar.setBackground(img.getDrawable());
            tabUpBar.setBackground(img.getDrawable());

            tabLeftBar.top();
//            tabLeftBar.add(new Label("testowy labal LEFT BAR -------------", a.skin));
//            tabLeftBar.row();
//            tabLeftBar.add(new TextButton("BUTTON 1", a.skin)).size(200, 100);
        }
    }
}
