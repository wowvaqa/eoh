package com.vs.testing;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.Assets;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.Mapa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Edytor Map.
 *
 * @author v
 */
public class MapEditor implements Screen {

    private final Game g;
    private final Assets a;

    private final Stage stage01 = new Stage();
    private TextButton btnExit;

    private final Table tabela = new Table();
    private final Table tabela01 = new Table();

    // Przechowuje dostępne typy terenów
    private final Array array = new Array();
    // Przechowuje typy terenu
    private final ArrayList listaPol = new ArrayList();

    private int iloscPolX = 10;
    private int iloscPolY = 10;

    private poleEdytora[][] mapaPolEdycyjncyh;

    private Mapa mapa;

    private Label lblP1;

    private Label lblIloscPolX;
    private Label lblIloscPolY;
    private TextButton btnPlus;
    private TextButton btnMinus;
    private TextButton btnZapiszMape;

    public MapEditor(Game g, Assets a) {
        this.g = g;
        this.a = a;

        array.add(TypyTerenu.Trawa);
        array.add(TypyTerenu.Gory);
        array.add(TypyTerenu.Drzewo);

        lblP1 = new Label("P1", a.skin);

        generujNowaMape();

        this.formatujTypyTerenu();
        this.makeButtons();
        this.formatujTeabele02();
        this.formatujTabele();
    }

    private void formatujTypyTerenu() {
        for (int i = 0; i < iloscPolX; i++) {
            for (int j = 0; j < iloscPolY; j++) {
//                SelectBox sb = new SelectBox(a.skin);
                //               sb.setItems(array);
                //listaPol.add(sb);
                listaPol.add(new poleEdytora(a.trawaTex, 1, 1, this.stage01, i, j));
            }
        }
    }

    /**
     * Tworzy Buttony
     */
    private void makeButtons() {

        btnZapiszMape = new TextButton("Zapisz Mape", a.skin);
        btnZapiszMape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    zapiszMape();
                } catch (IOException ex) {
                    Logger.getLogger(MapEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnExit = new TextButton("EXIT", a.skin);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                g.setScreen(Assets.mainMenuScreen);
            }
        });

        btnPlus = new TextButton("+", a.skin);
        btnPlus.setPosition(1, 1);
        btnPlus.setSize(200, 100);
        btnPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                iloscPolX += 1;
                iloscPolY += 1;
                lblIloscPolX.setText("Ilosc Pol X: " + iloscPolX);
                lblIloscPolY.setText("Ilosc Pol Y: " + iloscPolY);
                generujNowaMape();
                aktualizacjaPol();
            }
        });

        btnMinus = new TextButton("-", a.skin);
        btnMinus.setPosition(1, 1);
        btnMinus.setSize(200, 100);
        btnMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                iloscPolX -= 1;
                iloscPolY -= 1;
                lblIloscPolX.setText("Ilosc Pol X: " + iloscPolX);
                lblIloscPolY.setText("Ilosc Pol Y: " + iloscPolY);
                generujNowaMape();
                aktualizacjaPol();
            }
        });

        lblIloscPolX = new Label("Ilosc pol X: " + iloscPolX, a.skin);
        lblIloscPolY = new Label("Ilosc pol Y: " + iloscPolY, a.skin);
    }

    private void aktualizacjaPol() {
        listaPol.clear();
        this.formatujTypyTerenu();
        tabela01.clear();
        formatujTeabele02();
    }

    private void formatujTabele() {
        tabela.setFillParent(true);
        tabela.setDebug(true);
        tabela.pad(10);

        tabela.add(new Label("Map Editor", a.skin)).expandX().colspan(100).align(Align.top);
        tabela.row();
        tabela.add(lblIloscPolX);
        tabela.add(btnMinus);
        tabela.add(lblIloscPolY);
        tabela.add(btnPlus);
        tabela.row();

        tabela.row();
        tabela.add(tabela01).colspan(100).align(Align.center);
        tabela.row();
        tabela.add(btnExit).width(200).height(50).space(5).align(Align.right);
        tabela.add(btnZapiszMape).width(200).height(50).space(5).align(Align.left);

        stage01.addActor(tabela);
    }

    private void formatujTeabele02() {
        tabela01.setDebug(true);
        tabela01.pad(5);

        //int j = 0;
        int rowIndex = 0;

        //for (int j = 9; j > -1; j--) {
        for (int j = iloscPolY - 1; j > -1; j--) {
            for (int i = 0; i < iloscPolX; i++) {
                tabela01.add(mapaPolEdycyjncyh[i][j]);

                rowIndex += 1;
                if (rowIndex > iloscPolY - 1) {
                    tabela01.row();
                    rowIndex = 0;
                }
            }
        }

        System.out.print(tabela01.getCells().size);
        poleEdytora tmpPE = (poleEdytora) tabela01.getCells().get(99).getActor();
        tmpPE.getSprite().setTexture(a.mobDwarfTex);

//        for (Object listaPol1 : listaPol) {
//            tabela01.add((poleEdytora) listaPol1);
//            j += 1;
//            if (j > iloscPolY - 1) {
//                j = 0;
//                tabela01.row();
//            }
//        }
    }

    /**
     * Generuje mapę i zapisuje do poliku.
     */
    private void zapiszMape() throws FileNotFoundException, IOException {
//        int index = 0;
//        //Mapa mapa = new Mapa(iloscPolX, iloscPolY);
//        for (int i = 0; i < iloscPolX; i++) {
//            for (int j = 0; j < iloscPolY; j++) {
//                //mapa.getPola()[i][j].setTypTerenu((TypyTerenu) ((SelectBox) listaPol.get(index)).getSelected());
//                poleEdytora tmpPE;
//                tmpPE = (poleEdytora) listaPol.get(index);
//                mapa.getPola()[i][j].setTypTerenu(tmpPE.typTerenu);
//                index += 1;
//            }
//        }

        for (int i = 0; i < iloscPolX; i++) {
            for (int j = 0; j < iloscPolY; j++) {
                mapa.getPola()[i][j].setTypTerenu(mapaPolEdycyjncyh[i][j].typTerenu);
                mapa.getPola()[i][j].setLokacjaStartowaP1(mapaPolEdycyjncyh[i][j].lokacjaStartowaP1);
                mapa.getPola()[i][j].setLokacjaStartowaP2(mapaPolEdycyjncyh[i][j].lokacjaStartowaP2);
                mapa.getPola()[i][j].setLokacjaStartowaP3(mapaPolEdycyjncyh[i][j].lokacjaStartowaP3);
                mapa.getPola()[i][j].setLokacjaStartowaP4(mapaPolEdycyjncyh[i][j].lokacjaStartowaP4);

                mapa.getPola()[i][j].setTresureBox1Location(mapaPolEdycyjncyh[i][j].tresureBox1Location);
                mapa.getPola()[i][j].setTresureBox2Location(mapaPolEdycyjncyh[i][j].tresureBox2Location);

                mapa.getPola()[i][j].setMob1Location(mapaPolEdycyjncyh[i][j].mob1Location);
            }
        }

        System.out.println("Zapisuje mape");
        //ObjectOutputStream wy = new ObjectOutputStream(new FileOutputStream("mapa.dat"));
        //wy.writeObject(this.mapa);
        zapiszMape2(mapa);
    }

    public static void zapiszMape2(Mapa map) throws IOException {
        FileHandle file = Gdx.files.local("mapa.dat");
        Mapa saveMapa = null;
        OutputStream out = null;
        try {
            file.writeBytes(serialize(map), false);
        } catch (Exception ex) {
            Gdx.app.log("Save Map Error", "Nie mogę zapisać do pliku.");
        }
    }

    public static Mapa readMap() throws IOException, ClassNotFoundException {
        Mapa map = null;
        FileHandle file = Gdx.files.local("mapa.dat");
        map = (Mapa) deserialize(file.readBytes());
        return map;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

    /**
     * Generuje nową mapę.
     */
    private void generujNowaMape() {
        this.mapa = new Mapa(iloscPolX, iloscPolY);

        this.mapaPolEdycyjncyh = new poleEdytora[iloscPolX][iloscPolY];

        for (int i = 0; i < mapa.getIloscPolX(); i++)
            for (int j = 0; j < mapa.getIloscPolY(); j++)
                this.mapaPolEdycyjncyh[i][j] = new poleEdytora(a.trawaTex, 0, 0, stage01, i, j);
    }

    /**
     * Przerysowuje tekstury
     */
    public void redrawTextures() {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage01);
        //this.generujNowaMape();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage01.act();
        stage01.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        stage01.dispose();
    }

    /**
     * Klasa definiuje pole mapy do edycji
     */
    public class poleEdytora extends DefaultActor {

        private final Stage stage;
        public int wspX;
        public int wspY;
        public TypyTerenu typTerenu;

        public boolean lokacjaStartowaP1 = false;
        public boolean lokacjaStartowaP2 = false;
        public boolean lokacjaStartowaP3 = false;
        public boolean lokacjaStartowaP4 = false;

        // Lokacje startowe Skrzyń ze skarbem
        public boolean tresureBox1Location = false;
        public boolean tresureBox2Location = false;

        // Lokacje startowe mobów
        public boolean mob1Location = false;

        /**
         * @param tekstura Tekstura pola
         * @param x        Współżędna X
         * @param y        Współżędna Y
         * @param stage    Stage na której wyświetli się okno
         * @param wspX
         * @param wspY
         */
        public poleEdytora(Texture tekstura, int x, int y, Stage stage, int wspX, int wspY) {
            super(tekstura, x, y);
            super.setSize(30, 30);
            this.stage = stage;

            this.wspX = wspX;
            this.wspY = wspY;

            this.typTerenu = TypyTerenu.Trawa;

            dodajListenera();

        }

        private void dodajListenera() {
            this.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Pole edytora klikniete");

                    new Dialog("Edycja Pola", a.skin) {
                        {
                            button("Lokacja Startowa", "ls");
                            button("Teren", "teren");
                            button("Moby", "mobs");
                            button("Skrzynia", "skrzynia");
                            button("Zakoncz", "zakoncz");
                        }

                        @Override
                        protected void result(Object object) {

                            if (object.equals("ls")) {
                                new Dialog("Lokacja Startowa", a.skin) {
                                    {
                                        button("Player 1", "p1");
                                        button("Player 2", "p2");
                                        button("Player 3", "p3");
                                        button("Player 4", "p4");
                                        button("anuluj", "anuluj");
                                    }

                                    @Override
                                    protected void result(Object object) {
                                        if (object.equals("p1")) {
                                            lokacjaStartowaP1 = true;
                                            getSprite().setTexture(a.mobWizardTex);
                                            this.remove();
                                        } else if (object.equals("p2")) {
                                            lokacjaStartowaP2 = true;
                                            getSprite().setTexture(a.mobWizardTex);
                                            this.remove();
                                        } else if (object.equals("p3")) {
                                            lokacjaStartowaP3 = true;
                                            getSprite().setTexture(a.mobWizardTex);
                                            this.remove();
                                        } else if (object.equals("p4")) {
                                            lokacjaStartowaP4 = true;
                                            getSprite().setTexture(a.mobWizardTex);
                                            this.remove();
                                        } else if (object.equals("anuluj")) {
                                            this.remove();
                                        }
                                    }
                                }.show(stage);
                                // --- SKRZYNIA ZE SKARBEM ---------------------
                            } else if (object.equals("skrzynia")) {
                                new Dialog("Lokacja Startowa skrzyni", a.skin) {
                                    {
                                        button("Lvl 1", "1");
                                        button("Lvl 2", "2");
                                        //button("Lvl 3", "3");
                                        //button("Lvl 4", "4");
                                        button("anuluj", "anuluj");
                                    }

                                    @Override
                                    protected void result(Object object) {
                                        if (object.equals("1")) {
                                            tresureBox1Location = true;
                                            getSprite().setTexture(a.texTresureBox);
                                            this.remove();
                                        } else if (object.equals("2")) {
                                            tresureBox2Location = true;
                                            getSprite().setTexture(a.texTresureBox);
                                            this.remove();
//                                        } else if (object.equals("3")) {
//                                            this.remove();
//                                        } else if (object.equals("4")) {
//                                            this.remove();
                                        } else if (object.equals("anuluj")) {
                                            tresureBox1Location = false;
                                            tresureBox2Location = false;
                                            getSprite().setTexture(a.trawaTex);
                                            this.remove();
                                        }
                                    }
                                }.show(stage);
                                // --- LOKACJA STARTOWA MOBÓW ------------------
                            } else if (object.equals("mobs")) {
                                new Dialog("Lokacja Startowa mobów", a.skin) {
                                    {
                                        button("Lvl 1", "1");
                                        //button("Lvl 2", "2");
                                        //button("Lvl 3", "3");
                                        //button("Lvl 4", "4");
                                        button("anuluj", "anuluj");
                                    }

                                    @Override
                                    protected void result(Object object) {
                                        if (object.equals("1")) {
                                            mob1Location = true;
                                            getSprite().setTexture(a.texSzkieletMob);
                                            this.remove();
//                                        } else if (object.equals("2")) {
//                                            this.remove();
//                                        } else if (object.equals("3")) {
//                                            this.remove();
//                                        } else if (object.equals("4")) {
//                                            this.remove();
                                        } else if (object.equals("anuluj")) {
                                            mob1Location = false;
                                            getSprite().setTexture(a.trawaTex);
                                            this.remove();
                                        }
                                    }
                                }.show(stage);
                                // --- TEREN -----------------------------------
                            } else if (object.equals("teren")) {
                                new Dialog("Typy Terenu", a.skin) {
                                    {
                                        button("Las", "las");
                                        button("Gory", "gory");
                                        button("Trawa", "trawa");
                                        button("Rzeka", "rzeka");
                                    }

                                    @Override
                                    protected void result(Object object) {
                                        if (object.equals("las")) {
                                            typTerenu = TypyTerenu.Drzewo;
                                            getSprite().setTexture(a.trawaDrzewoTex);
                                            //formatujTeabele02();
                                            this.remove();
                                        } else if (object.equals("gory")) {
                                            typTerenu = TypyTerenu.Gory;
                                            getSprite().setTexture(a.trawaGoraTex);
                                            this.remove();
                                        } else if (object.equals("trawa")) {
                                            typTerenu = TypyTerenu.Trawa;
                                            getSprite().setTexture(a.trawaTex);
                                            this.remove();
                                        } else if (object.equals("rzeka")) {
                                            typTerenu = TypyTerenu.Rzeka;
                                            AtlasRegion region = a.tAtals.findRegion("riverES");
                                            getSprite().setTexture(region.getTexture());
                                            this.remove();
                                        }
                                    }
                                }.show(stage);
                            } else if (object.equals("zakoncz")) {
                                this.remove();
                            }
                        }
                    }.show(stage);
                }
            });
        }
    }

    /**
     * Klasa definiująca okno edycyjne, służące do edycji pól
     */
    public class oknoEdycyjne extends Dialog {

        /**
         * @param title
         * @param skin
         */
        public oknoEdycyjne(String title, Skin skin) {
            super(title, skin);
        }
    }
}
