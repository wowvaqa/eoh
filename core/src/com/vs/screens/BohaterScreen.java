package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.enums.CzesciCiala;
import com.vs.enums.TypItemu;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.Effect;
import com.vs.eoh.Fight;
import com.vs.eoh.GameStatus;
import com.vs.eoh.Gracz;
import com.vs.eoh.Item;
import com.vs.eoh.SpellCreator;

import java.util.ArrayList;

import javax.xml.soap.Text;

/**
 * Wyświetla screen z statystykami oraz ekwipunkiem bohatera
 *
 * @author wow
 */
public class BohaterScreen implements Screen {

    private final OrthographicCamera c;
    private final FitViewport viewPort;
    private final Assets a;
    private final GameStatus gs;
    private final Game g;
    private final Stage stage01 = new Stage();
    private Tables tables;
    private Interface interfce;

    public BohaterScreen(Game g, Assets a, GameStatus gs) {
        this.a = a;
        this.gs = gs;
        this.g = g;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        tables = new Tables();
        interfce = new Interface();

        dodajDoStage01();

        c = new OrthographicCamera(w, h);
        viewPort = new FitViewport(w, h, c);
    }

    /**
     * Podmienia tiemka między ekwipunkiem a wyposażeniem bohatera
     *
     * @param i
     */
    private void podmianaItemkow(final int i) {
        Item tmpItem;

        if (sprawdzBohatera().getEquipment().get(i).getTypItemu().equals(TypItemu.Mikstura)) {

            for (Effect dzialanieItema : sprawdzBohatera().getEquipment().get(i).dzialania) {
                dzialanieItema.dzialanie(sprawdzBohatera().getEquipment().get(i).getItemNazwa(), gs.getBohaterZaznaczony());
            }
            sprawdzBohatera().aktualizujEfektyBohatera();
            sprawdzBohatera().getEquipment().remove(i);

        } else if (sprawdzBohatera().getEquipment().get(i).getCzescCiala().equals(CzesciCiala.glowa)) {
            tmpItem = sprawdzBohatera().getItemGlowa();
            sprawdzBohatera().setItemGlowa(sprawdzBohatera().getEquipment().get(i));
            sprawdzBohatera().getEquipment().remove(i);
            if (!"Gola Glowa".equals(tmpItem.getNazwa())) {
                sprawdzBohatera().getEquipment().add(tmpItem);
            }
        } else if (sprawdzBohatera().getEquipment().get(i).getCzescCiala().equals(CzesciCiala.korpus)) {
            tmpItem = sprawdzBohatera().getItemKorpus();
            sprawdzBohatera().setItemKorpus(sprawdzBohatera().getEquipment().get(i));
            sprawdzBohatera().getEquipment().remove(i);
            if (!"Gola Glowa".equals(tmpItem.getNazwa())) {
                sprawdzBohatera().getEquipment().add(tmpItem);
            }
        } else if (sprawdzBohatera().getEquipment().get(i).getCzescCiala().equals(CzesciCiala.nogi)) {
            tmpItem = sprawdzBohatera().getItemNogi();
            sprawdzBohatera().setItemNogi(sprawdzBohatera().getEquipment().get(i));
            sprawdzBohatera().getEquipment().remove(i);
            if (!"Gole Nogi".equals(tmpItem.getNazwa())) {
                sprawdzBohatera().getEquipment().add(tmpItem);
            }
        } else if (sprawdzBohatera().getEquipment().get(i).getCzescCiala().equals(CzesciCiala.stopy)) {
            tmpItem = sprawdzBohatera().getItemStopy();
            sprawdzBohatera().setItemStopy(sprawdzBohatera().getEquipment().get(i));
            sprawdzBohatera().getEquipment().remove(i);
            sprawdzBohatera().getEquipment().add(tmpItem);
        } else if (sprawdzBohatera().getEquipment().get(i).getCzescCiala().equals(CzesciCiala.rece)) {
            new Dialog("Ktora reka?", a.skin) {
                {
                    text("Ktora reka?");
                    button("Lewa", "lewa");
                    button("Prawa", "prawa");
                }

                @Override
                protected void result(Object object) {
                    Item tmpItem2;
                    if (object.equals("lewa")) {
                        System.out.println("Wcisnieto lewa");
                        tmpItem2 = sprawdzBohatera().getItemLewaReka();
                        sprawdzBohatera().setItemLewaReka(sprawdzBohatera().getEquipment().get(i));
                        sprawdzBohatera().getEquipment().remove(i);
                        if (!"Gole Piesci".equals(tmpItem2.getNazwa())) {
                            sprawdzBohatera().getEquipment().add(tmpItem2);
                        }
                        tables.formatMainTable();

                    } else if (object.equals("prawa")) {
                        System.out.println("Wcisnieto Prawa");
                        tmpItem2 = sprawdzBohatera().getItemPrawaReka();
                        sprawdzBohatera().setItemPrawaReka(sprawdzBohatera().getEquipment().get(i));
                        sprawdzBohatera().getEquipment().remove(i);
                        if (!"Gole Piesci".equals(tmpItem2.getNazwa())) {
                            sprawdzBohatera().getEquipment().add(tmpItem2);
                        }
                        tables.formatMainTable();
                    }
                }
            }.show(stage01);
        }
    }

    /**
     * Dodaje aktorów do stage 01
     */
    private void dodajDoStage01() {
        //stage01.addActor(tabela);
        stage01.addActor(tables.tableMain);
        this.stage01.addActor(a.getInfoWindow());
    }

    /**
     * Przeszukuje wszystkich bohaterów sprawdzając czy ktoryś nie jest zaznaczony,
     * jeżeli true zwraca referencje do zaznaczonego bohatera.
     *
     * @return referencje do obiektu bohatera
     */
    private Bohater sprawdzBohatera() {
        Bohater bohater = null;
        for (Gracz gracz : gs.getGracze()) {
            for (Bohater b : gracz.getBohaterowie()) {
                if (b.isZaznaczony()) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage01);

        interfce.updateLabels();
        tables.formatMainTable();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage01.act();
        stage01.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage01.getViewport().update(width, height, true);
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

    // Setters and Getters

    // Klasa odpowiada za obsługę tabel.
    public class Tables {

        public Table tableMain = new Table();
        public Table tableHero = new Table();
        public Table tableStats = new Table();
        public Table tableEquip = new Table();
        public Table tableBackPack = new Table();
        public Table tableSpells = new Table();

        public Tables() {
        }

        /**
         * Framtuje główną tablę
         */
        public void formatMainTable() {
            interfce.updateLabels();
            tableMain.clear();
            tableMain.setFillParent(true);
            tableMain.setDebug(true);

            formatHeroTable();
            formatStatsTable();
            formatEqupmentTable();
            formatBackPackTable();
            formatSpellsTable();

            tableMain.add(tableStats).top().padRight(20);
            tableMain.add(tableEquip);
            tableMain.row();

            tableMain.add(tableBackPack).colspan(2);
            tableMain.row();

            tableMain.add(tableSpells).colspan(2);
            tableMain.row();

            tableMain.add(interfce.btnExit).size(100, 50).pad(5).colspan(2);
        }

        /**
         * Formatuje tabelę bohatera
         */
        public void formatHeroTable() {
            tableHero.clear();
            tableHero.setDebug(true);

            tableHero.add(interfce.lblHeroClass).pad(5);
            tableHero.add(interfce.lblLevel).pad(5);
            tableHero.row();

            tableHero.add(interfce.lblActualHp).pad(5);
            tableHero.add(interfce.lblExp).pad(5);
        }

        /**
         * Formatuje tabelę statystyk
         */
        public void formatStatsTable() {
            tableStats.clear();
            tableStats.setDebug(true);

            tableStats.add(new Label("Statystyki", a.skin)).pad(5).colspan(3);
            tableStats.row();

            tableStats.add(new Image(a.texAtcIcon)).size(50, 50).pad(5);
            tableStats.add(new Image(a.texDefIcon)).size(50, 50).pad(5);
            tableStats.add(new Image(a.texHpIcon)).size(50, 50).pad(5);
            tableStats.row();

            tableStats.add(interfce.lblAttack).size(150, 30).pad(5);
            tableStats.add(interfce.lblDefence).size(150, 30).pad(5);
            tableStats.add(interfce.lblHp).size(150, 30).pad(5);
            tableStats.row();

            tableStats.add(new Image(a.texSpdIcon)).size(50, 50).pad(5);
            tableStats.add(new Image(a.texPwrIcon)).size(50, 50).pad(5);
            tableStats.add(new Image(a.texWsdIcon)).size(50, 50).pad(5);
            tableStats.row();

            tableStats.add(interfce.lblSpeed).size(150, 30).pad(5);
            tableStats.add(interfce.lblPower).size(150, 30).pad(5);
            tableStats.add(interfce.lblWisdom).size(150, 30).pad(5);
            tableStats.row();

            tableStats.add(tableHero).colspan(3).pad(5);
        }

        /**
         * Formatuje tabelę ekwipunku
         */
        public void formatEqupmentTable() {
            tableEquip.clear();
            tableEquip.setDebug(true);

            tableEquip.add(new Label("Ekwipunek", a.skin)).pad(5).colspan(3);
            tableEquip.row();

            tableEquip.add(interfce.lblHead).colspan(3);
            tableEquip.row();
            tableEquip.add(sprawdzBohatera().getItemGlowa()).size(50, 50).colspan(3).pad(5);
            tableEquip.row();
            tableEquip.add(interfce.lblLHand);
            tableEquip.add(interfce.lblBody);
            tableEquip.add(interfce.lblRHand);
            tableEquip.row();
            tableEquip.add(sprawdzBohatera().getItemLewaReka()).size(50, 50).pad(5);
            tableEquip.add(sprawdzBohatera().getItemKorpus()).size(50, 50).pad(5);
            tableEquip.add(sprawdzBohatera().getItemPrawaReka()).size(50, 50).pad(5);
            tableEquip.row();
            tableEquip.add(interfce.lblLegs).colspan(3);
            tableEquip.row();
            tableEquip.add(sprawdzBohatera().getItemNogi()).size(50, 50).colspan(3).pad(5);
            tableEquip.row();
            tableEquip.add(interfce.lblFoots).colspan(3);
            tableEquip.row();
            tableEquip.add(sprawdzBohatera().getItemStopy()).size(50, 50).colspan(3).pad(5);
        }

        /**
         * Formatuje tabelę plecaka
         */
        public void formatBackPackTable() {

            int amountInRow = 0;

            final ArrayList<TextButton> tmpButtonRemove = new ArrayList<TextButton>();
            final ArrayList<TextButton> tmpButtonSetUp = new ArrayList<TextButton>();

            tableBackPack.clear();
            tableBackPack.setDebug(true);

            tableBackPack.add(new Label("Plecak", a.skin)).pad(5).colspan(10);
            tableBackPack.row();

            for (int i = 0; i < sprawdzBohatera().getEquipment().size(); i++) {
                tableBackPack.add(sprawdzBohatera().getEquipment().get(i)).size(50).pad(2);
                tmpButtonRemove.add(new TextButton("Usun" + i, a.skin));
                tmpButtonSetUp.add(new TextButton("Zaloz" + i, a.skin));

                tmpButtonRemove.get(i).addListener(new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("przycisk Unus klikniety");
                        for (int i = 0; i < tmpButtonRemove.size(); i++) {
                            if (tmpButtonRemove.get(i).isPressed()) {

                                // usuwa przycisk z tabeli
                                tmpButtonRemove.get(i).remove();
                                tmpButtonSetUp.get(i).remove();
                                // usuwa itemka z equipmentu
                                sprawdzBohatera().getEquipment().remove(i);
                                // resetuje tabele
                                formatMainTable();
                                break;
                            }
                        }
                        return false;
                    }
                });

                tmpButtonSetUp.get(i).addListener(new ClickListener() {

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("przycisk zaloz klikniety");
                        for (int i = 0; i < tmpButtonSetUp.size(); i++) {
                            if (tmpButtonSetUp.get(i).isPressed()) {
                                int tmpI = i;

                                tmpButtonRemove.get(i).remove();
                                tmpButtonSetUp.remove(i);

                                podmianaItemkow(i);
                                System.out.println("przycisk: " + tmpI);

                                // resetuje tabele
                                tmpButtonSetUp.clear();
                                formatMainTable();
                            }
                        }
                        return false;
                    }
                });

                tableBackPack.add(tmpButtonRemove.get(i)).pad(2);
                tableBackPack.add(tmpButtonSetUp.get(i)).pad(2);
                amountInRow += 1;
                if (amountInRow > 4) {
                    tableBackPack.row();
                    amountInRow = 0;
                }
            }
        }

        /**
         * Formatuje tabelę czarów.
         */
        public void formatSpellsTable() {
            tableSpells.clear();
            tableSpells.setDebug(true);

            tableSpells.add(new Label("Czary", a.skin)).pad(5).colspan(10);
            tableSpells.row();

            for (int i = 0; i < sprawdzBohatera().getListOfSpells().size(); i++) {
                final SpellCreator sC = new SpellCreator(a, gs);
                DefaultActor dA = new DefaultActor(sC.getSpellTexture(
                        gs.getBohaterZaznaczony().getListOfSpells().get(i)), i, i);

                final int tempI = i;

                dA.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        new Dialog("Lokacja Startowa", a.skin) {
                            {
                                text(sC.getSpellDescription(gs.getBohaterZaznaczony().getListOfSpells().get(tempI)));
                                button("anuluj", "anuluj");
                            }

                            @Override
                            protected void result(Object object) {
                                if (object.equals("anuluj")) {
                                    this.remove();
                                }
                            }
                        }.show(stage01);

                    }
                });
                tableSpells.add(dA).pad(2);
            }
        }
    }

    /**
     * Klasa odpowiada za obsługę elementów interfejsu tj. przycisków, labeli itp.
     */
    public class Interface {
        public Label lblAttack;
        public Label lblDefence;
        public Label lblHp;
        public Label lblSpeed;
        public Label lblPower;
        public Label lblWisdom;

        public Label lblHeroClass;
        public Label lblLevel;
        public Label lblActualHp;
        public Label lblExp;

        public Label lblHead;
        public Label lblBody;
        public Label lblRHand;
        public Label lblLHand;
        public Label lblLegs;
        public Label lblFoots;

        public TextButton btnExit;

        public Interface() {
            // współczynniki
            lblAttack = new Label("", a.skin);
            lblDefence = new Label("", a.skin);
            lblHp = new Label("", a.skin);
            lblSpeed = new Label("", a.skin);
            lblPower = new Label("", a.skin);
            lblWisdom = new Label("", a.skin);

            lblHeroClass = new Label("", a.skin);
            lblLevel = new Label("", a.skin);
            lblActualHp = new Label("", a.skin);
            lblExp = new Label("", a.skin);

            // ekwipunek
            lblHead = new Label("", a.skin);
            lblBody = new Label("", a.skin);
            lblLHand = new Label("", a.skin);
            lblRHand = new Label("", a.skin);
            lblLegs = new Label("", a.skin);
            lblFoots = new Label("", a.skin);

            // przycisk wyjścia
            btnExit = new TextButton("EXIT", a.skin);

            addListeners();
        }

        /**
         * Dodaje listnery do obiektów.
         */
        private void addListeners() {
            btnExit.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //gs.setActualScreen(1);
                    g.setScreen(Assets.mapScreen);
                    tables.tableMain.clear();
                    ;
                    tables.tableStats.clear();
                    tables.tableBackPack.clear();
                    System.out.println("Exit klikniety");
                }
            });
        }

        /**
         * Aktualizuje etykiety.
         */
        public void updateLabels() {

            /**
             * Update statystyk
             */
            lblAttack.setText("Atak: " + sprawdzBohatera().getAtak()
                            + " (" + Fight.getAtakEkwipunkuBohaterAtakujacego(gs.getBohaterZaznaczony()) + ")"
                            + " (" + sprawdzBohatera().getAtakEfekt() + ")"
            );
            lblDefence.setText("Obrona: " + sprawdzBohatera().getObrona()
                            + " (" + Fight.getObronaEkwipunkuBohaterBroniacego(gs.getBohaterZaznaczony()) + ")"
                            + " (" + sprawdzBohatera().getObronaEfekt() + ")"
            );
            lblHp.setText("HP: " + sprawdzBohatera().getHp());
            lblSpeed.setText("Szybkosc: " + sprawdzBohatera().getSzybkosc()
                            + " (" + Fight.getSzybkoscEkwipunkuBohatera(gs.getBohaterZaznaczony()) + ")"
            );
            lblPower.setText("Moc: " + sprawdzBohatera().getMoc());
            lblWisdom.setText("Wiedza: " + sprawdzBohatera().getWiedza());

            lblActualHp.setText("Hit Points: " + sprawdzBohatera().getActualHp());
            lblExp.setText("Punkty doswiadczenia: " + sprawdzBohatera().getExp() + " / " + sprawdzBohatera().getExpToNextLevel());
            lblLevel.setText("Poziom: " + sprawdzBohatera().getLevelOfExp());
            lblHeroClass.setText(sprawdzBohatera().getKlasyPostaci().toString());

            /**
             * Update ekwipunku
             */
            lblHead.setText("Glowa");
            lblLHand.setText("Lewa Reka");
            lblRHand.setText("Prawa Reka");
            lblBody.setText("Korpus");
            lblLegs.setText("Nogi");
            lblFoots.setText("Stopy");
        }
    }
}
