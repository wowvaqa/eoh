package com.vs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.ai.AI;
import com.vs.enums.AnimsTypes;
import com.vs.enums.Buldings;
import com.vs.enums.DostepneMoby;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.Spells;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.Bulding;
import com.vs.eoh.BuldingCreator;
import com.vs.eoh.ButtonActor;
import com.vs.eoh.Castle;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.Fight;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Mapa;
import com.vs.eoh.Mob;
import com.vs.eoh.Ruch;
import com.vs.eoh.SpellActor;
import com.vs.eoh.SpellCreator;
import com.vs.eoh.TresureBox;
import com.vs.eoh.V;
import com.vs.network.NetEngine;
import com.vs.network.Network;

import java.util.ArrayList;

public class MapScreen implements Screen {

    public static MapScreen mapScreen;
    private final OrthographicCamera c;
    private final FitViewport viewPort;
    private final Stage stage01 = new Stage();  // wyświetla mapę i playera
    private final Stage stage02 = new Stage();  // zarządza przyciskami interfejsu
    private final Stage stage03 = new Stage();  // zarządza czarami
    private final ArrayList<Image> teren = new ArrayList<Image>();
    public Tables tables = new Tables();
    public Interface interfce;
    private V v;
    private float w;
    private float h;
    private InputMultiplexer inputMultiPlexer = new InputMultiplexer();
    private MyGestureListener myGL = null;
    private MyGestureDetector myGD = null;

    private long curTime;

    /**
     * Klasa definiująca wygląd, zachowanie Mapy na której odbywa się gra
     *
     */
    public MapScreen(V v) {
        this.v = v;

        interfce = new Interface();

        myGL = new MyGestureListener(stage01);
        myGD = new MyGestureDetector(myGL);

        v.setStage01MapScreen(this.stage01);
        v.setStage02MapScreen(this.stage02);
        v.setStage03MapScreen(this.stage03);

        Assets.stage01MapScreen = this.stage01;
        Assets.stage02MapScreen = this.stage02;
        Assets.stage03MapScreen = this.stage03;

        generujPlansze();
        generujGraczy();
        dodajDoStage01();

        v.getGs().czyUtworzonoMape = true;

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        c = new OrthographicCamera(w, h);
        viewPort = new FitViewport(w, h, c);
        stage01.setViewport(viewPort);

        stage03.addActor(tables.tableStage03Main);
        stage02.addActor(tables.tableInterface);

        mapScreen = this;
    }

    /**
     * Koniec Tury
     */
    private void koniecTuryClick() {
        if (v.getGs().getNetworkStatus() == 2) {
            koniecTuryMuli();
        } else {
            koniecTurySingle();
        }
    }

    /**
     * Koniec tury w trybie Single Player
     */
    private void koniecTurySingle() {
        if (!sprawdzCzyGraczPosiadaZamek()) {
            // Jeżeli gracz nie będzie posiadał zamku wtedy zmianie ulegnie jego
            // status oraz zwiększona zostanie ilość tur bez zamku
            v.getGs().getGracze().get(v.getGs().getTuraGracza()).setStatusBezZamku(true);
            v.getGs().getGracze().get(v.getGs().getTuraGracza()).setTuryBezZamku(v.getGs().getGracze().get(v.getGs().getTuraGracza()).getTuryBezZamku() + 1);
            if (v.getGs().getGracze().get(v.getGs().getTuraGracza()).getTuryBezZamku() >= 5) {
                v.getGs().getGracze().get(v.getGs().getTuraGracza()).setStatusGameOver(true);
            }
        }
        wylaczAktywnychBohaterow();
        sprawdzCzyKoniecTuryOgolnej();
        // Ustala turę następnego gracza
        v.getGs().setTuraGracza(v.getGs().getTuraGracza() + 1);
        if (v.getGs().getTuraGracza() > v.getGs().getGracze().size() - 1) {
            v.getGs().setTuraGracza(0);
        }
        interfce.lblTuraGracza.setText("Tura gracz: " + Integer.toString(v.getGs().getTuraGracza()));

        // Przywrócenie wszystkich punktów ruchu dla bohaterów oraz aktualizacja czasu działania efektów
        // Regenereacja many
        for (Bohater i : v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie()) {

            i.setPozostaloRuchow(
                    i.getSzybkosc()
                            + Fight.getSzybkoscEkwipunkuBohatera(i)
                            + i.getSzybkoscEfekt());

            i.aktualizujDzialanieEfektow();
            i.czyscEfektyTymczasowe();

            i.setActualMana(i.getActualMana() + i.getManaRegeneration());
            if (i.getActualMana() > i.getMana() + Fight.getWiedzaEkwipunkuBohatera(i)) {
                i.setActualMana(i.getMana() + Fight.getWiedzaEkwipunkuBohatera(i));
            }

            i.setMoveInterfaceOn(false);
        }

        //  Aktualizuje działanie efektów czarów moba.
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].getMob() != null) {
                    v.getGs().getMapa().getPola()[i][j].getMob().aktualizujDzialanieEfektow();
                    v.getGs().getMapa().getPola()[i][j].getMob().setAktualnaSzybkosc(
                            v.getGs().getMapa().getPola()[i][j].getMob().getSzybkosc());
                }
            }
        }

        usunBohaterowGraczyGO();

        przesunKamereNadBohatera();

        // zmiana ikony gracza na górnej belce
        this.interfce.ikonaGracza.getSprite().setTexture(v.getGs().gracze.get(v.getGs().getTuraGracza()).getTeksturaIkonyGracza());

        Ruch.wylaczIkonyEfektow();

        if (v.getGs().getGracze().get(v.getGs().getTuraGracza()).isAi()) {
            koniecTurySingle();
        }
    }

    /**
     * Koniec tury w trybie multiplayer
     */
    public void koniecTuryMuli() {
        NetEngine.playersEndTurn += 1;

        Network.EndOfTurn endOfTurn = new Network.EndOfTurn();
        GameStatus.client.getCnt().sendTCP(endOfTurn);

        // Sprawdzenie czy wszyscy gracze zakończyli turę
        if (NetEngine.playersEndTurn >= v.getGs().getGracze().size()) {
            NetEngine.playersEndTurn = 0;

            wylaczAktywnychBohaterow();
            sprawdzCzyKoniecTuryOgolnej();
            // Ustala turę następnego gracza
            v.getGs().setTuraGracza(NetEngine.playerNumber);
            interfce.lblTuraGracza.setText("Tura gracz: " + Integer.toString(v.getGs().getTuraGracza()));

            // Przywrócenie wszystkich punktów ruchu dla bohaterów oraz aktualizacja czasu działania efektów
            // Regenereacja many
            for (Bohater i : v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie()) {
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
            for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
                for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                    if (v.getGs().getMapa().getPola()[i][j].getMob() != null) {
                        v.getGs().getMapa().getPola()[i][j].getMob().aktualizujDzialanieEfektow();
                        v.getGs().getMapa().getPola()[i][j].getMob().setAktualnaSzybkosc(
                                v.getGs().getMapa().getPola()[i][j].getMob().getSzybkosc());
                    }
                }
            }

            usunBohaterowGraczyGO();

            przesunKamereNadBohatera();

            // zmiana ikony gracza na górnej belce
            this.interfce.ikonaGracza.getSprite().setTexture(v.getGs().gracze.get(v.getGs().getTuraGracza()).getTeksturaIkonyGracza());

            Ruch.wylaczIkonyEfektow();

            interfce.btnKoniecTury.setVisible(true);
            interfce.btnKupBohatera.setVisible(true);
        } else {
            interfce.btnKoniecTury.setVisible(false);
            interfce.btnKupBohatera.setVisible(false);
        }
    }

    /**
     * Przesuwa kamerę nad bohatera
     */
    public void przesunKamereNadBohatera() {
        Camera cam = stage01.getCamera();
        float xCord = v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().get(0).getX();
        float yCord = v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().get(0).getY();

        cam.position.x = v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().get(0).getX() + 150;
        cam.position.y = v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().get(0).getY() + 100;

        //cam.translate(xCord - cam.position.x + 200, yCord - cam.position.y + 100, 0);
    }

    /**
     * Usuwa bohaterów graczy którzy mają status Game Over
     */
    private void usunBohaterowGraczyGO() {
        if (v.getGs().getGracze().get(v.getGs().getTuraGracza()).isStatusGameOver()) {
            if (v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().size() > 0) {
                for (int i = 0; i < v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().size(); i++) {
                    v.getGs().getGracze().get(v.getGs().getTuraGracza()).getBohaterowie().remove(i);

                }
            }
            for (int x = 0; x < v.getGs().getMapa().getIloscPolX(); x++) {
                for (int y = 0; y < v.getGs().getMapa().getIloscPolY(); y++) {
                    if (v.getGs().getMapa().getPola()[x][y].getBohater() != null
                            && v.getGs().getMapa().getPola()[x][y].getBohater().getPrzynaleznoscDoGracza() == v.getGs().getTuraGracza()) {
                        v.getGs().getMapa().pola[x][y].getBohater().remove();
                        v.getGs().getMapa().pola[x][y].setBohater(null);
                    }
                }
            }
            koniecTuryClick();
        }
    }

    /**
     * Sprawdza czy gracz posiada zamek. Zwraca True jeżeli posiada False jeżeli
     * nie posiada
     */
    private boolean sprawdzCzyGraczPosiadaZamek() {
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].getCastle() != null) {
                    if (v.getGs().getMapa().getPola()[i][j].getCastle().getPrzynaleznoscDoGracza() == v.getGs().getTuraGracza()) {
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
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].getBohater() != null) {
                    v.getGs().getMapa().getPola()[i][j].getBohater().setZaznaczony(false);
                    v.getGs().getMapa().getPola()[i][j].getBohater().getSprite().setTexture(v.getGs().getMapa().getPola()[i][j].getBohater().getBohaterTex());
                    Ruch.wylaczPrzyciski();
                }
            }
        }
        v.getGs().setCzyZaznaczonoBohatera(false);
    }

    /**
     * Funkcja sprawdza czy tura gry nie została zakończona Jezeli TRUE zwiększa
     * +1
     */
    private void sprawdzCzyKoniecTuryOgolnej() {
        if (v.getGs().getTuraGracza() == v.getGs().iloscGraczy - 1) {
            v.getGs().setTuraGry(v.getGs().getTuraGry() + 1);
            odnowZdrowieZamkow();
            odnowZdrowieBohaterow();
        }
    }

    /**
     * Odnawia co turę gry HP bohaterów + 1
     */
    private void odnowZdrowieBohaterow() {
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].getBohater() != null
                        && v.getGs().getMapa().getPola()[i][j].getBohater().getActualHp()
                        < v.getGs().getMapa().getPola()[i][j].getBohater().getHp()) {

                    int hpRenew = 1;

                    if (v.getGs().getMapa().getPola()[i][j].getCastle() != null) {
                        hpRenew = 3;
                    }

                    v.getGs().getMapa().getPola()[i][j].getBohater().setActualHp(
                            v.getGs().getMapa().getPola()[i][j].getBohater().getActualHp() + hpRenew);

                    if (v.getGs().getMapa().getPola()[i][j].getBohater().getActualHp() > v.getGs().getMapa().getPola()[i][j].getBohater().getHp()) {
                        v.getGs().getMapa().getPola()[i][j].getBohater().setActualHp(v.getGs().getMapa().getPola()[i][j].getBohater().getHp());
                    }

                    v.getGs().getMapa().getPola()[i][j].getBohater().aktualizujTeksture();
                }
            }
        }
    }

    /**
     * Odnawia co turę gry HP zamków +1
     */
    private void odnowZdrowieZamkow() {
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].getCastle() != null
                        && v.getGs().getMapa().getPola()[i][j].getCastle().getActualHp()
                        < v.getGs().getMapa().getPola()[i][j].getCastle().getMaxHp()) {
                    v.getGs().getMapa().getPola()[i][j].getCastle().setActualHp(
                            v.getGs().getMapa().getPola()[i][j].getCastle().getActualHp() + 1);
                }
            }
        }
    }

    private void aktualizujPanelBohatera() {
        Bohater b = v.getGs().getBohaterZaznaczony();
        interfce.lblHp.setVisible(true);
        interfce.lblHp.setText("HP:" + b.getActualHp() + "/" + b.getHp());
        interfce.lblMove.setVisible(true);
        interfce.lblMove.setText("MV:" + b.getPozostaloRuchow() + "/" + b.getSzybkosc());
        interfce.lblExp.setVisible(true);
        interfce.lblExp.setText("EX:" + b.getExp() + "/" + b.getExpToNextLevel());
        interfce.lblMana.setVisible(true);
        interfce.lblMana.setText("MN:" + b.getActualMana() + "/" + b.getMana());

        interfce.lblEfekty.setVisible(true);
        interfce.btnBohater.setVisible(true);
        interfce.btnSpellBook.setVisible(true);
    }

    // Dodaj do stage 01 predefiniowane przyciski ruchu i ataku oraz przycisk cancel
    private void dodajDoStage01() {
        // Dodaje do planszy info window z assetów do wyświetlania info o skrzynce ze skarbem
        stage01.addActor(v.getA().getInfoWindow());

        stage01.addActor(v.getA().lblDmg);
    }

    // Gneruje graczy w konstruktorze klasy i dodaje ich do planszy 01
    private void generujGraczy() {

        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolX(); j++) {

                if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP1()) {

                    v.getGs().getMapa().getPola()[i][j].setCastle(new Castle(v, i * 100, j * 100, 0));
                    v.getGs().getMapa().getPola()[i][j].getCastle().setLocXonMap(i);
                    v.getGs().getMapa().getPola()[i][j].getCastle().setLocYonMap(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getCastle());

                    v.getGs().getGracze().get(0).getBohaterowie().get(0).setPozXnaMapie(i);
                    v.getGs().getGracze().get(0).getBohaterowie().get(0).setPozYnaMapie(j);
                    v.getGs().getGracze().get(0).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                    v.getGs().getMapa().getPola()[i][j].setBohater(
                            v.getGs().getGracze().get(0).getBohaterowie().get(0));
                    stage01.addActor(v.getGs().getGracze().get(0).getBohaterowie().get(0));
                }
                if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP2()) {

                    v.getGs().getMapa().getPola()[i][j].setCastle(new Castle(v, i * 100, j * 100, 1));
                    v.getGs().getMapa().getPola()[i][j].getCastle().setLocXonMap(i);
                    v.getGs().getMapa().getPola()[i][j].getCastle().setLocYonMap(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getCastle());

                    v.getGs().getGracze().get(1).getBohaterowie().get(0).setPozXnaMapie(i);
                    v.getGs().getGracze().get(1).getBohaterowie().get(0).setPozYnaMapie(j);
                    v.getGs().getGracze().get(1).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                    v.getGs().getMapa().getPola()[i][j].setBohater(
                            v.getGs().getGracze().get(1).getBohaterowie().get(0));
                    stage01.addActor(v.getGs().getGracze().get(1).getBohaterowie().get(0));
                }
                if (v.getGs().getGracze().size() == 3 || v.getGs().getGracze().size() == 4) {

                    if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP3()) {

                        v.getGs().getMapa().getPola()[i][j].setCastle(new Castle(v, i * 100, j * 100, 2));
                        v.getGs().getMapa().getPola()[i][j].getCastle().setLocXonMap(i);
                        v.getGs().getMapa().getPola()[i][j].getCastle().setLocYonMap(j);
                        stage01.addActor(v.getGs().getMapa().getPola()[i][j].getCastle());

                        v.getGs().getGracze().get(2).getBohaterowie().get(0).setPozXnaMapie(i);
                        v.getGs().getGracze().get(2).getBohaterowie().get(0).setPozYnaMapie(j);
                        v.getGs().getGracze().get(2).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                        v.getGs().getMapa().getPola()[i][j].setBohater(
                                v.getGs().getGracze().get(2).getBohaterowie().get(0));
                        stage01.addActor(v.getGs().getGracze().get(2).getBohaterowie().get(0));
                    }
                }
                if (v.getGs().getGracze().size() == 4) {
                    if (v.getGs().getMapa().getPola()[i][j].isLokacjaStartowaP4()) {

                        v.getGs().getMapa().getPola()[i][j].setCastle(new Castle(v, i * 100, j * 100, 3));
                        v.getGs().getMapa().getPola()[i][j].getCastle().setLocXonMap(i);
                        v.getGs().getMapa().getPola()[i][j].getCastle().setLocYonMap(j);
                        stage01.addActor(v.getGs().getMapa().getPola()[i][j].getCastle());

                        v.getGs().getGracze().get(3).getBohaterowie().get(0).setPozXnaMapie(i);
                        v.getGs().getGracze().get(3).getBohaterowie().get(0).setPozYnaMapie(j);
                        v.getGs().getGracze().get(3).getBohaterowie().get(0).setPosition(i * 100, j * 100);
                        v.getGs().getMapa().getPola()[i][j].setBohater(
                                v.getGs().getGracze().get(3).getBohaterowie().get(0));
                        stage01.addActor(v.getGs().getGracze().get(3).getBohaterowie().get(0));
                    }
                }
            }
        }
    }

    /**
     * Generuje skrzynie ze skarbami.
     */
    private void tresureBoxGenerator() {
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolX(); j++) {

                if (v.getGs().getMapa().getPola()[i][j].isTresureBox1Location()) {
                    TresureBox tb = new TresureBox(1, 2, v, i * 100, j * 100);
                    v.getGs().getMapa().getPola()[i][j].setTresureBox(tb);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getTresureBox());
                } else if (v.getGs().getMapa().getPola()[i][j].isTresureBox2Location()) {
                    TresureBox tb = new TresureBox(2, 1, v, i * 100, j * 100);
                    v.getGs().getMapa().getPola()[i][j].setTresureBox(tb);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getTresureBox());
                }
            }
        }
    }

    /**
     * Generuje budynki
     */
    private void buldingsGenerator() {

        BuldingCreator buldingCreator = new BuldingCreator(v);

        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolX(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].isAttackCamp()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.traningCamp, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isDefenceCamp()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.defenceCamp, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isPowerCamp()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.powerCamp, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isWisdomCamp()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.wisdomCamp, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isSpeedCamp()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.speedCamp, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isHpCamp()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.hpCamp, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isWell()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.well, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isTemple()) {
                    Bulding bulding = buldingCreator.createBulding(Buldings.temple, i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                } else if (v.getGs().getMapa().getPola()[i][j].isRandomBulding()) {
                    Bulding bulding = buldingCreator.createBulding(Bulding.drawBulding(), i, j);
                    stage01.addActor(bulding);
                    v.getGs().getMapa().getPola()[i][j].setBulding(bulding);
                }
            }
        }
    }

    /**
     * Generuje moby na mapie.
     */
    private void mobsGenerator() {
        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolX(); j++) {

                if (v.getGs().getMapa().getPola()[i][j].isMobZombie()) {

                    Mob mob;
                    mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.ZombieAnimation, DostepneMoby.Zombie);
                    v.getGs().getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getMob());

                } else if (v.getGs().getMapa().getPola()[i][j].isMobSpider()) {

                    Mob mob;
                    mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.SpiderAnimation, DostepneMoby.Pajak);
                    v.getGs().getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getMob());

                } else if (v.getGs().getMapa().getPola()[i][j].isMobSkeletion()) {

                    Mob mob;
                    mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.SkeletonAnimation, DostepneMoby.Szkielet);
                    v.getGs().getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getMob());

                } else if (v.getGs().getMapa().getPola()[i][j].isMobWolf()) {

                    Mob mob;
                    mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.WolfAnimation, DostepneMoby.Wilk);
                    v.getGs().getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getMob());

                } else if (v.getGs().getMapa().getPola()[i][j].isMob1Location()) {

                    //Mob mob = new Mob(v, i * 100, j * 100, 1, Mob.losujMoba(1));

                    DostepneMoby dostepneMoby = Mob.losujMoba(1);

                    Mob mob;

                    if (dostepneMoby.equals(DostepneMoby.Szkielet)) {
                        mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.SkeletonAnimation, dostepneMoby);
                    } else {
                        mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.WolfAnimation, dostepneMoby);
                    }

                    v.getGs().getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getMob());
                } else if (v.getGs().getMapa().getPola()[i][j].isMob2Location()) {

                    DostepneMoby dostepneMoby = Mob.losujMoba(2);

                    Mob mob;

                    if (dostepneMoby.equals(DostepneMoby.Pajak)) {
                        mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.SpiderAnimation, dostepneMoby);
                    } else {
                        mob = new Mob(v, i * 100, j * 100, 1, AnimsTypes.ZombieAnimation, dostepneMoby);
                    }

                    //Mob mob = new Mob(v, i * 100, j * 100, 2, Mob.losujMoba(2));
                    v.getGs().getMapa().getPola()[i][j].setMob(mob);
                    mob.setPozX(i);
                    mob.setPozY(j);
                    stage01.addActor(v.getGs().getMapa().getPola()[i][j].getMob());
                }
            }
        }
    }

    private Texture teksturaTerenu(TypyTerenu tT) {
        switch (tT) {
            case Gory:
                return v.getA().trawaGoraTex;
            case Trawa:
                return v.getA().trawaTex;
            case Drzewo:
                return v.getA().trawaDrzewoTex;
        }
        return v.getA().trawaTex;
    }

    // wypełnia stage01 aktorami planszy
    private void generujPlansze() {

        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {
                if (v.getGs().getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Gory) {
                    v.getGs().getMapa().getPola()[i][j].setMovable(false);
                }
                if (v.getGs().getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Rzeka) {
                    Image img = new Image(v.getA().tAtals.findRegion(Mapa.getTextureRegion(i, j, v.getGs().getMapa(), TypyTerenu.Rzeka)));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                } else if (v.getGs().getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Drzewo) {
                    Image img = new Image(v.getA().tAtals.findRegion(Mapa.getTextureRegion(i, j, v.getGs().getMapa(), TypyTerenu.Drzewo)));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                } else if (v.getGs().getMapa().getPola()[i][j].getTypTerenu() == TypyTerenu.Gory) {
                    Image img = new Image(v.getA().tAtals.findRegion(Mapa.getTextureRegion(i, j, v.getGs().getMapa(), TypyTerenu.Gory)));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                } else {
                    Image img = new Image(teksturaTerenu(v.getGs().getMapa().getPola()[i][j].getTypTerenu()));
                    img.setPosition(i * 100, j * 100);
                    teren.add(img);
                }
            }
        }

        for (Image teren1 : teren) {
            stage01.addActor(teren1);
        }

        this.buldingsGenerator();
        this.tresureBoxGenerator();
        this.mobsGenerator();
    }

    @Override
    public void render(float delta) {

        // Następny ruch AI nie kasować
        curTime = System.currentTimeMillis() / 1000;
        if (!AI.aiStarted) {
            AI.nextMove = curTime + 2;
            AI.aiStarted = true;
        }

        if (curTime > AI.nextMove) {
            Gdx.app.log("Następuje ruch AI", "" + AI.whichMove);
            AI.whichMove += 1;
            AI.nextMove += 2;
            for (AI ai : AI.aiList) {
                ai.makeAIMove();
            }
        }

        if (NetEngine.playersEndTurn == v.getGs().getGracze().size()) {
            koniecTuryMuli();
        }

        Gdx.input.setInputProcessor(inputMultiPlexer);

        if (v.getGs().getBohaterZaznaczony() != null) {
            countMoveCost(v.getGs().getBohaterZaznaczony());
            aktualizujPanelBohatera();
            if (v.getGs().getBohaterZaznaczony().getExp() >= v.getGs().getBohaterZaznaczony().getExpToNextLevel() &&
                    v.getGs().getBohaterZaznaczony().getKlasyPostaci() != KlasyPostaci.Summmon) {
                interfce.btnAwansujBohatera.setVisible(true);
            }
        } else {
            interfce.lblHp.setVisible(false);
            interfce.lblMove.setVisible(false);
            interfce.lblExp.setVisible(false);
            interfce.lblMana.setVisible(false);
            interfce.lblEfekty.setVisible(false);
            interfce.btnAwansujBohatera.setVisible(false);
            interfce.btnBohater.setVisible(false);
            interfce.btnSpellBook.setVisible(false);
        }

        sortujZindex();

        ruchKamery();

        this.interfce.lblGold.setText(Integer.toString(v.getGs().getZlotoAktualnegoGracza()));

        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage01.act();
        stage01.draw();

        stage02.act();
        stage02.draw();

        stage03.act();
        stage03.draw();
    }


    private void countMoveCost(Bohater bohater) {
        bohater.getPozXnaMapie();
        bohater.getPozYnaMapie();

        for (int i = 0; i < v.getGs().getMapa().getIloscPolX(); i++) {
            for (int j = 0; j < v.getGs().getMapa().getIloscPolY(); j++) {

            }
        }
    }

    // Steruje ruchem kamery
    private void ruchKamery() {

        int predkoscRuchuKamery = v.getGs().getPredkoscScrollowaniaKamery();
        int predkoscZoom = v.getGs().getPredkoscZoomKamery();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (stage01.getCamera().position.x > 350) {
                stage01.getCamera().translate(-predkoscRuchuKamery, 0, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (stage01.getCamera().position.x < v.getGs().getMapa().getIloscPolX() * 100) {
                stage01.getCamera().translate(predkoscRuchuKamery, 0, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (stage01.getCamera().position.y < v.getGs().getMapa().getIloscPolY() * 100) {
                stage01.getCamera().translate(0, predkoscRuchuKamery, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (stage01.getCamera().position.y > 50) {
                stage01.getCamera().translate(0, -predkoscRuchuKamery, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (stage01.getCamera().viewportWidth < 2 * w) {
                if (stage01.getCamera().viewportHeight < 2 * h) {
                    stage01.getCamera().viewportHeight += predkoscZoom;
                    stage01.getCamera().viewportWidth += predkoscZoom;
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {

            if (stage01.getCamera().viewportWidth > w) {
                if (stage01.getCamera().viewportHeight > h) {
                    stage01.getCamera().viewportHeight -= predkoscZoom;
                    stage01.getCamera().viewportWidth -= predkoscZoom;
                }
            }
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
            if (stage01.getActors().get(i).getClass() == Bulding.class) {
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
        return v.getGs();
    }

    @Override
    public void resize(int width, int height) {
        stage01.getViewport().update(width, height, true);
        stage02.getViewport().update(width, height, true);
        stage03.getViewport().update(width, height, true);
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
        System.gc();

        przesunKamereNadBohatera();

        curTime = System.currentTimeMillis() / 1000;
        AI.nextMove = curTime + 2;

        this.sortujZindex();
        inputMultiPlexer.addProcessor(myGD);
        inputMultiPlexer.addProcessor(stage03);
        inputMultiPlexer.addProcessor(stage02);
        inputMultiPlexer.addProcessor(stage01);
        Gdx.input.setInputProcessor(inputMultiPlexer);

        tables.formatInterfaceTable();
        tables.formatStage03MainTable();
    }

    // Setters and Getters
    public Stage getStage01() {
        return stage01;
    }

    /**
     * Klasa przechwytująca gesty.
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

            if (stage01.getCamera().position.x < 350) {
                stage01.getCamera().position.x = 350;
            }

            if (stage01.getCamera().position.x > v.getGs().getMapa().getIloscPolX() * 100) {
                stage01.getCamera().position.x = v.getGs().getMapa().getIloscPolX() * 100;
            }

            if (stage01.getCamera().position.y > v.getGs().getMapa().getIloscPolY() * 100) {
                stage01.getCamera().position.y = v.getGs().getMapa().getIloscPolY() * 100;
            }

            if (stage01.getCamera().position.y < 50) {
                stage01.getCamera().position.y = 50;
            }

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

    /**
     * Klasa odpowiada za obsługę tabel.
     */
    public class Tables {

        public Table tableStage03Main = new Table();
        public Table tableSpells = new Table();

        public Table tableInterface = new Table();
        public Table tableUpBar = new Table();
        public Table tableRightBar = new Table();

        public Table tableEffectsBar = new Table();
        public Table tableStatsBar = new Table();
        public Table tableButtonPanel01 = new Table();
        public Table tableButtonPanel02 = new Table();

        private Texture tex = new Texture("background.jpg");
        Image img = new Image(tex);

        public Tables() {
            tableSpells.setVisible(false);
        }

        /**
         * Formatuje tabelę interfejsu
         */
        public void formatInterfaceTable() {
            tableInterface.clear();
            tableInterface.setFillParent(true);
            tableInterface.setDebug(true);

            formatUpBarTable();
            formatRightBarTable();
            formatEffetsBarTable();

            tableInterface.add(tableUpBar).align(Align.topLeft).expandX().top().maxHeight(25);
            tableInterface.row();
            tableInterface.add(tableRightBar).align(Align.topRight).expandY().maxWidth(250).maxHeight(Gdx.graphics.getHeight() - 20);
        }

        /**
         * Formatuje tabelę górnej belki interfejsu.
         */
        public void formatUpBarTable() {
            tableUpBar.clear();
            tableUpBar.setDebug(true);

            tableUpBar.add(interfce.lblTuraGracza).padRight(5);
            tableUpBar.add(interfce.ikonaGracza).padRight(10).size(25, 25);
            tableUpBar.add(interfce.ikonaGold).padRight(5).size(25, 25);
            tableUpBar.add(interfce.lblGold).padRight(5);

            tableUpBar.setBackground(img.getDrawable());
        }

        /**
         * Formatuje tabelę prawej belki interfejsu.
         */
        public void formatRightBarTable() {
            tableRightBar.clear();
            tableRightBar.setDebug(true);

            formatButtonPanel01Table();
            formatButtonPanel02Table();
            formatStatsTable();
            formatEffetsBarTable();

            tableRightBar.add(tableStatsBar);
            tableRightBar.row();

            tableRightBar.add(tableEffectsBar);
            tableRightBar.row();

            tableRightBar.add(tableButtonPanel01);
            tableRightBar.row();

            tableRightBar.add(tableButtonPanel02).bottom().expandY();
            tableRightBar.row();

            tableRightBar.setBackground(img.getDrawable());
            tableRightBar.top();
        }

        /**
         * Formatuje tabelę efektów.
         */
        public void formatEffetsBarTable() {
            tableEffectsBar.clear();
            tableEffectsBar.setDebug(true);

            tableEffectsBar.add(new Label("Aktywne Efekty:", v.getA().skin)).pad(2).colspan(5);
            tableEffectsBar.row();
        }

        public void formatButtonPanel01Table() {
            tableButtonPanel01.clear();
            tableButtonPanel01.setDebug(true);

            tableButtonPanel01.add(interfce.btnBohater).bottom().size(100, 50).pad(5);
            tableButtonPanel01.add(interfce.btnSpellBook).bottom().size(100, 50).pad(5);
            tableButtonPanel01.row();
            tableButtonPanel01.add(interfce.btnAwansujBohatera).size(210, 50).pad(5).colspan(2);
        }

        public void formatButtonPanel02Table() {
            tableButtonPanel02.clear();
            tableButtonPanel02.setDebug(true);

            tableButtonPanel02.add(interfce.btnKoniecTury).bottom().size(100, 50).pad(5);
            tableButtonPanel02.add(interfce.btnKupBohatera).bottom().size(100, 50).pad(5);
            tableButtonPanel02.row();
            tableButtonPanel02.add(interfce.btnExit).align(Align.bottom).size(210, 50).pad(5).colspan(2);
        }

        public void formatStatsTable() {
            tableStatsBar.clear();
            tableStatsBar.setDebug(true);

            tableStatsBar.add(interfce.lblHp).size(100, 25).pad(5);
            tableStatsBar.add(interfce.lblMove).size(100, 25).pad(5);
            tableStatsBar.row();
            tableStatsBar.add(interfce.lblMana).size(100, 25).pad(5);
            tableStatsBar.add(interfce.lblExp).size(100, 25).pad(5);
            tableStatsBar.row();
        }

        /**
         * Formatuje główną tabelę z Stage 03
         */
        public void formatStage03MainTable() {
            tableStage03Main.clear();
            tableStage03Main.setFillParent(true);
            tableStage03Main.setDebug(true);

            formatSpellsTable();

            tableStage03Main.add(tableSpells);
        }

        /**
         * Formatuje tabelę czarów.
         */
        public void formatSpellsTable() {
            tableSpells.clear();
            tableSpells.setDebug(true);

            int indeksWiersza = 0;
            //if (gs.isCzyZaznaczonoBohatera()) {
            if (v.getGs().getBohaterZaznaczony() != null) {
                v.getGs().getBohaterZaznaczony().getSpells().clear();
                SpellCreator spellCreator = new SpellCreator(v);
                for (Spells spl : v.getGs().getBohaterZaznaczony().getListOfSpells()) {
                    v.getGs().getBohaterZaznaczony().getSpells().add(spellCreator.utworzSpell(spl, v.getGs().getBohaterZaznaczony()));

                }
                for (SpellActor sA : v.getGs().getBohaterZaznaczony().getSpells()) {
                    if (indeksWiersza > 4) {
                        tableSpells.row();
                        indeksWiersza = 0;
                    }
                    tableSpells.add(sA).pad(2);
                    indeksWiersza += 1;
                }
            }
            tableSpells.row();
            tableSpells.add(interfce.btnSpellExit).size(100, 50).colspan(10);
        }
    }

    /**
     * Klasa odpowiada za obsługę elementów interfejsu.
     */
    public class Interface {

        // PRZYCISKI
        public TextButton btnExit = new TextButton("EXIT", v.getA().skin);
        public TextButton btnKoniecTury = new TextButton("Koniec Tury", v.getA().skin);
        public TextButton btnKupBohatera = new TextButton("Kup Bohatera", v.getA().skin);
        public TextButton btnSpellBook = new TextButton("Spell Book", v.getA().skin);
        public TextButton btnSpellExit = new TextButton("EXIT", v.getA().skin);
        public TextButton btnBohater = new TextButton("Bohater", v.getA().skin);
        public TextButton btnAwansujBohatera = new TextButton("AWANSUJ BOHATERA", v.getA().skin);

        // LABELKI
        public Label lblHp = new Label("", v.getA().skin);
        public Label lblMove = new Label("", v.getA().skin);
        public Label lblMana = new Label("", v.getA().skin);
        public Label lblExp = new Label("", v.getA().skin);
        public Label lblEfekty = new Label("", v.getA().skin);
        public Label lblGold = new Label("", v.getA().skin);
        public Label lblTuraGracza = new Label("Tura Gracza: 0", v.getA().skin);

        // POZOSTALE
        public DefaultActor ikonaGracza = new DefaultActor(v.getA().btnAttackTex, 0, 0);
        ;
        public DefaultActor ikonaGold = new DefaultActor(v.getA().texGold, 165, Gdx.graphics.getHeight() - 25);

        public Interface() {
            Listeners listeners = new Listeners();
            listeners.addListeners();
        }

        /**
         * Klasa odpowiada za obsługę listenerów.
         */
        public class Listeners {
            public Listeners() {
            }

            /**
             * Dodaje wszystkie listenery
             */
            public void addListeners() {
                addListnerBtnExit();
                addListnerBtnKoniecTury();
                addListnerBtnKupBohatera();
                addListnerBtnSpellExit();
                addListnerBtnSpellBook();
                addListnerBtnBohater();
                addListnerBtnAwansujBohatera();
            }

            /**
             * Dodaje Listner do przycisku Exit.
             */
            public void addListnerBtnExit() {
                btnExit.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        NetEngine.gameStarted = false;
                        NetEngine.amountOfMultiPlayers = 0;
                        NetEngine.playerNumber = 0;
                        v.getG().setScreen(v.getMainMenuScreen());
                        v.getGs().setActualScreen(0);
                    }
                });
            }

            /**
             * Dodaje Listener do przycisku koniec tury.
             */
            public void addListnerBtnKoniecTury() {
                btnKoniecTury.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        koniecTuryClick();
                    }
                });
            }

            /**
             * Dodaje Listener do przycisku Kup Bohatera.
             */
            public void addListnerBtnKupBohatera() {
                btnKupBohatera.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        if (v.getGs().getGracze().get(v.getGs().getTuraGracza()).getGold() < GameStatus.CostOfNewHero) {
                            DialogScreen dialogScreen = new DialogScreen("ERROR", v.getA().skin, "Za malo zlota", stage01);
                        } else {
                            v.getG().setScreen(v.getNewBohaterScreen());
                        }
                    }
                });
            }

            /**
             * Dodaje Listener do przycisku koniec tury.
             */
            public void addListnerBtnSpellExit() {
                btnSpellExit.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        tables.tableSpells.setVisible(false);
                        if (v.getGs().getBohaterZaznaczony() != null) {
                            v.getGs().getBohaterZaznaczony().getSpells().clear();
                        }
                    }
                });
            }

            /**
             * Dodaje Listener do przyciski Spell Book
             */
            public void addListnerBtnSpellBook() {
                btnSpellBook.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        tables.formatStage03MainTable();
                        tables.tableSpells.setVisible(true);
                    }
                });
            }

            /**
             * Dodaje Listner do przycisku bohater.
             */
            public void addListnerBtnBohater() {
                btnBohater.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        if (v.getGs().isCzyZaznaczonoBohatera()) {
                            //v.getGs().setActualScreen(5);
                            v.getG().setScreen(v.getBohaterScreen());
                        } else {
                            System.out.println("Nie zaznaczono bohatera");
                        }
                    }
                });
            }

            /**
             * Dodaje Listner do przycisku Awansuj Bohatera.
             */
            public void addListnerBtnAwansujBohatera() {
                btnAwansujBohatera.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        v.getA().buttonClick.play();
                        btnAwansujBohatera.setVisible(false);
                        v.getG().setScreen(v.getAwansScreen());
                    }
                });
            }
        }
    }
}
