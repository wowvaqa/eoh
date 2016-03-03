package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

/**
 * Wyświetla screen z statystykami oraz ekwipunkiem bohatera
 *
 * @author wow
 */
public class BohaterScreen implements Screen {

    private final OrthographicCamera c;
    private final FitViewport viewPort;

    // Informuje czy tabela jest zaktualizowana
    private boolean tabelaZaktualizowana = false;

    // Referencje do obiektu assetów, statusu gry, bohatera który jest kliknięty
    private final Assets a;
    private final GameStatus gs;
    private final Game g;

    // Plansza
    private final Stage stage01 = new Stage();

    // Przyciski
    private TextButton btnExit;

    // Labele
    private Label lblBohater;
    private Label lblAtak, lblObrona, lblHp, lblSzybkosc, lblMana, lblKlasaPostaci;
    private Label lblMoc, lblWiedza;
    private Label lblExp, lblExpToNextLevel, lblLevel;
    private Label lblStopy, lblNogi, lblLewaReka, lblPrawaReka, lblKorpus, lblGlowa;

    // Tabela
    private final Table tabela = new Table();
    private final Table tabela2 = new Table();
    private final Table tabela3 = new Table();

    public BohaterScreen(Game g, Assets a, GameStatus gs) {
        this.a = a;
        this.gs = gs;
        this.g = g;

        utworzPrzyciski();
        utworzLabele();
        dodajDoStage01();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        c = new OrthographicCamera(w, h);
        viewPort = new FitViewport(w, h, c);
    }

    // Formatuje tabele dodaje do niej elementy
    private void formatujTabele() {

        tabela.setFillParent(true);
        tabela.pad(5);
        tabela.setDebug(true);

        tabela.add(lblBohater).align(Align.top).expandX().colspan(tabela.getColumns());
        tabela.row();

        tabela.add(lblAtak).align(Align.topLeft);
        tabela.add(lblObrona).align(Align.topLeft);
        tabela.add(lblHp).align(Align.topLeft);
        tabela.add(lblSzybkosc).align(Align.topLeft);
        tabela.row();
        tabela.add(lblMana).align(Align.topLeft);
        tabela.add(lblMoc).align(Align.topLeft);
        tabela.add(lblWiedza).align(Align.topLeft);
        tabela.row();

        tabela.add(lblLevel).align(Align.left);
        tabela.add(lblExp).align(Align.left);
        tabela.add(lblExpToNextLevel).align(Align.left);
        tabela.add(lblKlasaPostaci).align(Align.topLeft);
        tabela.row();

        tabela.add(lblLewaReka).align(Align.left);
        tabela.add(sprawdzBohatera().getItemLewaReka()).size(50, 50);

        tabela.add(lblGlowa).align(Align.left);
        tabela.add(sprawdzBohatera().getItemGlowa()).size(50, 50);
        tabela.row();

        tabela.add(lblPrawaReka).align(Align.left);
        tabela.add(sprawdzBohatera().getItemPrawaReka()).size(50, 50);

        tabela.add(lblKorpus).align(Align.left);
        tabela.add(sprawdzBohatera().getItemKorpus()).size(50, 50);
        tabela.row();

        tabela.add(lblNogi).align(Align.left);
        tabela.add(sprawdzBohatera().getItemNogi()).size(50, 50);

        tabela.add(lblStopy).align(Align.left);
        tabela.add(sprawdzBohatera().getItemStopy()).size(50, 50);
        tabela.row();

        tabela.add(tabela2).align(Align.topLeft).expand().colspan(tabela.getColumns());

        tabela.row();

        tabela.add(tabela3).align(Align.topLeft).expand().colspan(tabela.getColumns());

        tabela.row();

        tabela.add(btnExit).expand().align(Align.bottom).width(100).height(50).colspan(tabela.getColumns());
    }

    /**
     * Formatuje tabele ekwipunku
     */
    private void formatujTabele2() {
        int iloscPozycjiwWierszu = 0;
        tabela2.pad(2);
        tabela2.setDebug(false);

        tabela2.add(new Label("Ekwipunek:", a.skin)).align(Align.topLeft);
        tabela2.row();

        final ArrayList<TextButton> tmpButtonsUsun = new ArrayList<TextButton>();
        final ArrayList<TextButton> tmpButtonsZaloz = new ArrayList<TextButton>();

        for (int i = 0; i < sprawdzBohatera().getEquipment().size(); i++) {
            //tabela2.add(new Label(sprawdzBohatera().getEquipment().get(i).getNazwa(), a.skin)).align(Align.left).pad(2);
            tabela2.add(sprawdzBohatera().getEquipment().get(i)).size(50);
            tmpButtonsUsun.add(new TextButton("Usun" + i, a.skin));
            tmpButtonsZaloz.add(new TextButton("Zaloz" + i, a.skin));

            tmpButtonsUsun.get(i).addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("przycisk Unus klikniety");
                    for (int i = 0; i < tmpButtonsUsun.size(); i++) {
                        if (tmpButtonsUsun.get(i).isPressed()) {

                            // usuwa przycisk z tabeli
                            tmpButtonsUsun.get(i).remove();
                            tmpButtonsZaloz.get(i).remove();
                            // usuwa itemka z equipmentu
                            sprawdzBohatera().getEquipment().remove(i);
                            // resetuje tabele
                            tabela2.reset();
                            formatujTabele2();
                            break;
                        }
                    }
                    return false;
                }
            });

            tmpButtonsZaloz.get(i).addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("przycisk zaloz klikniety");
                    for (int i = 0; i < tmpButtonsZaloz.size(); i++) {
                        if (tmpButtonsZaloz.get(i).isPressed()) {
                            int tmpI = i;

                            // usuwa przycisk z tabeli
                            tmpButtonsUsun.get(i).remove();
                            //tmpButtonsZaloz.get(i).remove();
                            tmpButtonsZaloz.remove(i);
                            // usuwa itemka z equipmentu
                            tabelaZaktualizowana = false;

                            podmianaItemkow(i);
                            System.out.println("przycisk: " + tmpI);

                            // resetuje tabele
                            tabela.clear();
                            tabela2.clear();
                            tabela3.clear();
                            aktualizujTabele();
                            tmpButtonsZaloz.clear();
                        }
                    }
                    return false;
                }
            });

            tabela2.add(tmpButtonsUsun.get(i)).pad(2);
            tabela2.add(tmpButtonsZaloz.get(i)).pad(2);
            iloscPozycjiwWierszu += 1;
            if (iloscPozycjiwWierszu > 3) {
                tabela2.row();
                iloscPozycjiwWierszu = 0;
            }
        }
    }

    private void formatujTabela3() {
        tabela3.pad(2);
        tabela3.setDebug(false);

        tabela3.add(new Label("Czary:", a.skin)).align(Align.topLeft);
        tabela3.row();

        for (int i = 0; i < gs.getBohaterZaznaczony().getListOfSpells().size(); i++) {
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

            tabela3.add(dA).pad(2);
        }

//        System.out.println("Ilość czarów getSpells(): " + gs.getBohaterZaznaczony().getSpells().size());
        //tabela3.add(new ImageButton(gs.getBohaterZaznaczony().getSpells().get(0).getSprite().getTexture().getTextureData()))
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
                        tabela.clear();
                        tabela2.clear();
                        tabela3.clear();
                        tabelaZaktualizowana = false;
                        aktualizujTabele();

                    } else if (object.equals("prawa")) {
                        System.out.println("Wcisnieto Prawa");
                        tmpItem2 = sprawdzBohatera().getItemPrawaReka();
                        sprawdzBohatera().setItemPrawaReka(sprawdzBohatera().getEquipment().get(i));
                        sprawdzBohatera().getEquipment().remove(i);
                        if (!"Gole Piesci".equals(tmpItem2.getNazwa())) {
                            sprawdzBohatera().getEquipment().add(tmpItem2);
                        }
                        tabela.clear();
                        tabela2.clear();
                        tabela3.clear();
                        tabelaZaktualizowana = false;
                        aktualizujTabele();
                    }
                }
            }.show(stage01);
        }
    }

    /**
     * Tworzy labele
     */
    private void utworzLabele() {
        lblBohater = new Label("BOHATER", a.skin);
        lblAtak = new Label("Atak: ", a.skin);
        lblObrona = new Label("Obrona: ", a.skin);
        lblHp = new Label("HP: ", a.skin);
        lblSzybkosc = new Label("Szybkosc: ", a.skin);
        lblMana = new Label("Mana: ", a.skin);
        lblMoc = new Label("Moc:", a.skin);
        lblWiedza = new Label("Wiedza", a.skin);
        lblLevel = new Label("Poziom: ", a.skin);
        lblExp = new Label("Doswiadczenie: ", a.skin);
        lblExpToNextLevel = new Label("Pozostalo nast. poz.: ", a.skin);
        lblNogi = new Label("Nogi: ", a.skin);
        lblLewaReka = new Label("L. Reka: ", a.skin);
        lblPrawaReka = new Label("", a.skin);
        lblKorpus = new Label("", a.skin);
        lblGlowa = new Label("", a.skin);
        lblStopy = new Label("", a.skin);
        lblKlasaPostaci = new Label("", a.skin);

    }

    /**
     * Tworzy przyciski
     */
    private void utworzPrzyciski() {
        btnExit = new TextButton("EXIT", a.skin);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gs.setActualScreen(1);
                g.setScreen(Assets.mapScreen);
                tabela.clear();
                tabela2.clear();
                tabela3.clear();
                tabelaZaktualizowana = false;
                System.out.println("Exit klikniety");
            }
        });
    }

    /**
     * Dodaje aktorów do stage 01
     */
    private void dodajDoStage01() {
        stage01.addActor(tabela);
        this.stage01.addActor(a.getInfoWindow());
    }

    // Przeszukuje wszystkich bohaterów sprawdzając czy ktoryś nie jest zaznaczony
    // Jeżeli true zwraca referencje do zaznaczonego bohatera.
    private Bohater sprawdzBohatera() {
        Bohater bohater = null;
        for (Gracz gracz : gs.getGracze()) {
            for (Bohater b : gracz.getBohaterowie()) {
                if (b.isZaznaczony()) {
                    return b;
                }
            }
        }
        return bohater;
    }

    // Aktualizuje labele o dane klikniętego bohatera
    private void aktualizujTabele() {

        formatujTabele();
        formatujTabele2();
        formatujTabela3();

        lblAtak.setText("Atak: " + sprawdzBohatera().getAtak()
                + " (" + Fight.getAtakEkwipunkuBohaterAtakujacego(gs.getBohaterZaznaczony()) + ")"
                + " (" + sprawdzBohatera().getAtakEfekt() + ")"
        );

        lblObrona.setText("Obrona: " + sprawdzBohatera().getObrona()
                + " (" + Fight.getObronaEkwipunkuBohaterBroniacego(gs.getBohaterZaznaczony()) + ")"
                + " (" + sprawdzBohatera().getObronaEfekt() + ")");

        lblHp.setText("HP: " + sprawdzBohatera().getActualHp()
                + " (" + sprawdzBohatera().getHp() + ")"
        );
        lblSzybkosc.setText("Szybkosc: " + sprawdzBohatera().getSzybkosc()
                + " (" + Fight.getSzybkoscEkwipunkuBohatera(gs.getBohaterZaznaczony()) + ")");

        lblMana.setText("Mana: " + sprawdzBohatera().getActualMana()
                + " (" + sprawdzBohatera().getMana() + ")");

        lblMoc.setText("Moc: " + sprawdzBohatera().getMoc());

        lblWiedza.setText("Wiedza: " + sprawdzBohatera().getWiedza());

        lblKlasaPostaci.setText(gs.getBohaterZaznaczony().getKlasyPostaci().toString());

        lblLevel.setText("Poziom: " + sprawdzBohatera().getLevelOfExp());
        lblExp.setText("Punkty doswiadczenia: " + sprawdzBohatera().getExp());
        lblExpToNextLevel.setText("Punkty do nst. poz.: " + sprawdzBohatera().getExpToNextLevel());

        lblKorpus.setText("Korpus: " /*+ sprawdzBohatera().getItemKorpus().getNazwa()*/);
        lblGlowa.setText("Glowa: " /*+ sprawdzBohatera().getItemGlowa().getNazwa()*/);

        lblLewaReka.setText("L. Reka: " /*+ sprawdzBohatera().getItemLewaReka().getNazwa()*/);
        lblPrawaReka.setText("P. Reka: "/* + sprawdzBohatera().getItemPrawaReka().getNazwa()*/);
        lblNogi.setText("Nogi: " /*+ sprawdzBohatera().getItemNogi().getNazwa()*/);
        lblStopy.setText("Stopy: " /*+ sprawdzBohatera().getItemStopy().getNazwa()*/);

        tabelaZaktualizowana = true;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage01);

        if (gs.isCzyZaznaczonoBohatera()) {
            if (!tabelaZaktualizowana) {
                aktualizujTabele();
            }
        }
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
}
