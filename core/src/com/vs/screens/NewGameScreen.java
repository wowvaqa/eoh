package com.vs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.enums.KlasyPostaci;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.GameStatus;
import com.vs.eoh.NewGame;
import com.vs.eoh.V;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa definiuje Screen nowej gry zajmuje się zarządzaniem interfejsem
 *
 * @author v
 */
public class NewGameScreen implements Screen {

    protected final OrthographicCamera c;
    private final FitViewport viewPort;

    private final Table tabela01 = new Table();
    private final Table tabelaIlosciGraczy = new Table();
    private final Table tabelaGracz01 = new Table();
    private final Table tabelaGracz02 = new Table();
    private final Table tabelaGracz03 = new Table();
    private final Table tabelaGracz04 = new Table();
    private final Stage stage01;
    private final Label lblIloscGraczy;
    private V v;
    private CheckBox cbAI0;
    private CheckBox cbAI1;
    private CheckBox cbAI2;
    private CheckBox cbAI3;
    private boolean tabelaUtworzona = false;

    //public NewGameScreen(Game g, Assets a, GameStatus gs) {
    public NewGameScreen(V v) {

        // referencje do obiektów assetów i gamestatusu
        this.v = v;

        stage01 = new Stage();

        lblIloscGraczy = new Label(Integer.toString(NewGame.iloscGraczy), v.getA().skin);

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);

        NewGame.pixmapRed.setColor(Color.RED);
        NewGame.pixmapRed.fillRectangle(0, 0, 50, 25);
        NewGame.pixmapBlue.setColor(Color.BLUE);
        NewGame.pixmapBlue.fillRectangle(0, 0, 50, 25);
        NewGame.pixmapYellow.setColor(Color.YELLOW);
        NewGame.pixmapYellow.fillRectangle(0, 0, 50, 25);
        NewGame.pixmapGreen.setColor(Color.GREEN);
        NewGame.pixmapGreen.fillRectangle(0, 0, 50, 25);

        cbAI0 = new CheckBox("CPU", v.getA().skin);
        cbAI1 = new CheckBox("CPU", v.getA().skin);
        cbAI2 = new CheckBox("CPU", v.getA().skin);
        cbAI3 = new CheckBox("CPU", v.getA().skin);
    }

    private void dodajDoStage01() {
        stage01.addActor(tabela01);
    }

    /**
     * Formatuje tabelę główną, w której znajdują się tabele graczy.
     */
    private void formatujTabele01() {
        // ustawia rozmiar tebeli na cały ekran
        tabela01.setFillParent(true);
        // ustawia odstęp od krawędzi tabeli
        tabela01.pad(10);
        // włacza linie debugujące tabelę
        tabela01.setDebug(false);

        Image img = new Image(v.getA().texNewGamePic);
        tabela01.setBackground(img.getDrawable());

        tabela01.add(new Label("Nowa Gra", v.getA().skin)).align(Align.center).align(Align.top).expandX().colspan(tabela01.getColumns());
        tabela01.add(getBtnWybierzMape()).size(200, 50).spaceRight(5);
        tabela01.row();

        tabela01.add(tabelaIlosciGraczy).colspan(4);
        tabela01.row();

        if (NewGame.iloscGraczy == 2) {
            tabela01.add(tabelaGracz01).align(Align.topLeft).expand().align(Align.center);
            tabela01.add(tabelaGracz02).align(Align.topLeft).expand().align(Align.center);
            tabela01.row();
        } else if (NewGame.iloscGraczy == 3) {
            tabela01.add(tabelaGracz01).align(Align.topLeft).expand().align(Align.center);
            tabela01.add(tabelaGracz02).align(Align.topLeft).expand().align(Align.center);
            tabela01.add(tabelaGracz03).align(Align.topLeft).expand().align(Align.center);
            tabela01.row();
        } else if (NewGame.iloscGraczy == 4) {
            tabela01.add(tabelaGracz01).align(Align.topLeft).expand().align(Align.center);
            tabela01.add(tabelaGracz02).align(Align.topLeft).expand().align(Align.center);
            tabela01.add(tabelaGracz03).align(Align.topLeft).expand().align(Align.center);
            tabela01.add(tabelaGracz04).align(Align.topLeft).expand().align(Align.center);
            tabela01.row();
        }

        // Przycisk Anuluj
        TextButton btnAnuluj = new TextButton("ANULUJ", v.getA().skin);
        btnAnuluj.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                v.getG().setScreen(v.getMainMenuScreen());
            }
        });
        tabela01.add(btnAnuluj).align(Align.left);


        // Przycisk Zakończ
        TextButton btnExit = new TextButton("ZAKONCZ", v.getA().skin);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();

                if (GameStatus.nazwaMapy != "brak" || !GameStatus.nazwaMapy.equals("mapa z serwera")) {
                    try {
                        NewGame.ai0 = false;
                        NewGame.ai1 = false;
                        NewGame.ai2 = false;
                        NewGame.ai3 = false;

                        if (cbAI0.isChecked()){
                            NewGame.ai0 = true;
                        }
                        if (cbAI1.isChecked()){
                            NewGame.ai1 = true;
                        }
                        if (cbAI2.isChecked()){
                            NewGame.ai2 = true;
                        }
                        if (cbAI3.isChecked()){
                            NewGame.ai3 = true;
                        }

                        NewGame.zakonczGenerowanieNowejGry(v);
                    } catch (IOException ex) {
                        Logger.getLogger(NewGameScreen.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(NewGameScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GameStatus.gameStart = true;
                    v.getG().setScreen(v.getMapScreen());
                } else {
                    new Dialog("Nie wybrano mapy", v.getA().skin) {
                        {
                            button("Zamknij", "zamknij");
                        }

                        @Override
                        protected void result(Object object) {
                            if (object.equals("zamknij")) {
                                this.remove();
                            }
                        }
                    }.show(stage01);
                }
            }
        });
        //tabela01.add(btnExit).align(Align.right).colspan(tabela01.getColumns()).pad(10);
        tabela01.add(btnExit).align(Align.right).colspan(3);

        tabelaUtworzona = true;
    }

    /**
     * Formatuje tabelę wewnetrzną służącą do wyboru ilości graczy
     */
    private void formatujTabeleIlosciGraczy() {
        //tabelaIlosciGraczy.setFillParent(true);
        tabelaIlosciGraczy.pad(10);
        tabelaIlosciGraczy.add(new Label("Ilosc graczy: ", v.getA().skin)).expandX();
        tabelaIlosciGraczy.add(lblIloscGraczy);

        // Przycisk - przy wyborze ilosci graczy
        TextButton tB01 = new TextButton("-", v.getA().skin);
        tB01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.iloscGraczy = NewGame.odejmijGracza(NewGame.iloscGraczy);
                lblIloscGraczy.setText(Integer.toString(NewGame.iloscGraczy));
                tabela01.reset();
                formatujTabele01();
                tabelaIlosciGraczy.reset();
                formatujTabeleIlosciGraczy();
            }
        });
        tabelaIlosciGraczy.add(tB01).pad(5);

        // Przycisk + przy wyborze ilośći graczy
        TextButton tB02 = new TextButton("+", v.getA().skin);
        tB02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.iloscGraczy = NewGame.dodajGracza(NewGame.iloscGraczy);
                lblIloscGraczy.setText(Integer.toString(NewGame.iloscGraczy));
                tabela01.reset();
                formatujTabele01();
                tabelaIlosciGraczy.reset();
                formatujTabeleIlosciGraczy();
            }
        });
        tabelaIlosciGraczy.add(tB02).pad(5);
    }

    /**
     * Formatuje tabelę gracza 1
     */
    private void formatujTabeleGracza01() {
        Image img = new Image(v.getA().texNewGamePlayerTabPic);
        tabelaGracz01.setBackground(img.getDrawable());

        tabelaGracz01.pad(10);
        tabelaGracz01.add(new Label("Gracz 1", v.getA().skin)).padTop(10);
        tabelaGracz01.add(cbAI0).padTop(10);
        tabelaGracz01.row();

        // Przycisk - przy wyborze ilosci graczy
        TextButton g01B01 = new TextButton("Prev", v.getA().skin);
        g01B01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz01 = NewGame.poprzedniaKlasaPostaci(NewGame.klasaPostaciGracz01);
                tabelaGracz01.clear();
                formatujTabeleGracza01();
            }
        });
        tabelaGracz01.add(g01B01).pad(10);

        // Przycisk + przy wyborze ilośći graczy
        TextButton tB02 = new TextButton("Next", v.getA().skin);
        tB02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz01 = NewGame.nastepnaKlasaPostaci(NewGame.klasaPostaciGracz01);
                tabelaGracz01.clear();
                formatujTabeleGracza01();
            }
        });
        tabelaGracz01.add(tB02).pad(10);
        tabelaGracz01.row();

        tabelaGracz01.add(new Label(NewGame.pobierzTytul(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).align(Align.center);
        tabelaGracz01.row();

        tabelaGracz01.add(NewGame.pobierzPortret(NewGame.klasaPostaciGracz01)).align(Align.center).colspan(tabelaGracz01.getColumns());

        tabelaGracz01.row();
        tabelaGracz01.add(new Image(v.getA().texAtcIcon)).size(25, 25);
        tabelaGracz01.add(new Label("Atak: " + NewGame.pobierzAtak(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).padRight(10);
        tabelaGracz01.row();
        tabelaGracz01.add(new Image(v.getA().texDefIcon)).size(25, 25);
        tabelaGracz01.add(new Label("Obrona: " + NewGame.pobierzObrone(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).padRight(10);
        tabelaGracz01.row();
        tabelaGracz01.add(new Image(v.getA().texHpIcon)).size(25, 25);
        tabelaGracz01.add(new Label("Hp: " + NewGame.pobierzHp(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).padRight(10);
        tabelaGracz01.row();
        tabelaGracz01.add(new Image(v.getA().texSpdIcon)).size(25, 25);
        tabelaGracz01.add(new Label("Szybkosc: " + NewGame.pobierzSzybkosc(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).padRight(10);
        tabelaGracz01.row();
        tabelaGracz01.add(new Image(v.getA().texPwrIcon)).size(25, 25);
        tabelaGracz01.add(new Label("Moc: " + NewGame.pobierzMoc(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).padRight(10);
        tabelaGracz01.row();
        tabelaGracz01.add(new Image(v.getA().texWsdIcon)).size(25, 25);
        tabelaGracz01.add(new Label("Wiedza: " + NewGame.pobierzWiedze(NewGame.klasaPostaciGracz01), v.getA().skin)).colspan(tabelaGracz01.getColumns()).padRight(10);
        tabelaGracz01.row();
        tabelaGracz01.add(new Label("Kolor: ", v.getA().skin));
        tabelaGracz01.add(new DefaultActor(new Texture(NewGame.pixmapRed), 0, 0)).colspan(tabelaGracz01.getColumns()).padRight(10).padBottom(10);
    }

    /**
     * Formatuje tabelę gracza 2
     */
    private void formatujTabeleGracza02() {

        Image img = new Image(v.getA().texNewGamePlayerTabPic);
        tabelaGracz02.setBackground(img.getDrawable());

        tabelaGracz02.pad(10);
        tabelaGracz02.add(new Label("Gracz 2", v.getA().skin)).padTop(10);
        tabelaGracz02.add(cbAI1).padTop(10);
        tabelaGracz02.row();

        // Przycisk - przy wyborze ilosci graczy
        TextButton g02B01 = new TextButton("Prev", v.getA().skin);
        g02B01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz02 = NewGame.poprzedniaKlasaPostaci(NewGame.klasaPostaciGracz02);
                tabelaGracz02.clear();
                formatujTabeleGracza02();
            }
        });
        tabelaGracz02.add(g02B01).pad(10);

        // Przycisk + przy wyborze ilośći graczy
        TextButton tB02 = new TextButton("Next", v.getA().skin);
        tB02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz02 = NewGame.nastepnaKlasaPostaci(NewGame.klasaPostaciGracz02);
                tabelaGracz02.clear();
                formatujTabeleGracza02();
            }
        });
        tabelaGracz02.add(tB02).pad(10);
        tabelaGracz02.row();

        tabelaGracz02.add(new Label(NewGame.pobierzTytul(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).align(Align.center);
        tabelaGracz02.row();

        tabelaGracz02.add(NewGame.pobierzPortret(NewGame.klasaPostaciGracz02)).align(Align.center).colspan(tabelaGracz02.getColumns());

        tabelaGracz02.row();
        tabelaGracz02.add(new Image(v.getA().texAtcIcon)).size(25, 25);
        tabelaGracz02.add(new Label("Atak: " + NewGame.pobierzAtak(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).padRight(10);
        tabelaGracz02.row();
        tabelaGracz02.add(new Image(v.getA().texDefIcon)).size(25, 25);
        tabelaGracz02.add(new Label("Obrona: " + NewGame.pobierzObrone(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).padRight(10);
        tabelaGracz02.row();
        tabelaGracz02.add(new Image(v.getA().texHpIcon)).size(25, 25);
        tabelaGracz02.add(new Label("Hp: " + NewGame.pobierzHp(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).padRight(10);
        tabelaGracz02.row();
        tabelaGracz02.add(new Image(v.getA().texSpdIcon)).size(25, 25);
        tabelaGracz02.add(new Label("Szybkosc: " + NewGame.pobierzSzybkosc(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).padRight(10);
        tabelaGracz02.row();
        tabelaGracz02.add(new Image(v.getA().texPwrIcon)).size(25, 25);
        tabelaGracz02.add(new Label("Moc: " + NewGame.pobierzMoc(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).padRight(10);
        tabelaGracz02.row();
        tabelaGracz02.add(new Image(v.getA().texWsdIcon)).size(25, 25);
        tabelaGracz02.add(new Label("Wiedza: " + NewGame.pobierzWiedze(NewGame.klasaPostaciGracz02), v.getA().skin)).colspan(tabelaGracz02.getColumns()).padRight(10);
        tabelaGracz02.row();
        tabelaGracz02.add(new Label("Kolor: ", v.getA().skin));
        tabelaGracz02.add(new DefaultActor(new Texture(NewGame.pixmapBlue), 0, 0)).colspan(tabelaGracz02.getColumns()).padBottom(10);
    }

    /**
     * Formatuje tabelę gracza 3
     */
    private void formatujTabeleGracza03() {
        tabelaGracz03.pad(10);
        tabelaGracz03.add(new Label("Gracz 3", v.getA().skin)).padTop(10);
        tabelaGracz03.add(cbAI2).padTop(10);
        tabelaGracz03.row();

        // Przycisk - przy wyborze ilosci graczy
        TextButton g02B01 = new TextButton("Prev", v.getA().skin);
        g02B01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz03 = NewGame.poprzedniaKlasaPostaci(NewGame.klasaPostaciGracz03);
                tabelaGracz03.clear();
                formatujTabeleGracza03();
            }
        });
        tabelaGracz03.add(g02B01).pad(10);

        // Przycisk + przy wyborze ilośći graczy
        TextButton tB02 = new TextButton("Next", v.getA().skin);
        tB02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz03 = NewGame.nastepnaKlasaPostaci(NewGame.klasaPostaciGracz03);
                tabelaGracz03.clear();
                formatujTabeleGracza03();
            }
        });
        tabelaGracz03.add(tB02).pad(10);
        tabelaGracz03.row();

        tabelaGracz03.add(new Label(NewGame.pobierzTytul(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns()).align(Align.center);
        tabelaGracz03.row();

        tabelaGracz03.add(NewGame.pobierzPortret(NewGame.klasaPostaciGracz03)).align(Align.center).colspan(tabelaGracz03.getColumns());

        tabelaGracz03.row();
        tabelaGracz03.add(new Image(v.getA().texAtcIcon)).size(25, 25);
        tabelaGracz03.add(new Label("Atak: " + NewGame.pobierzAtak(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns());
        tabelaGracz03.row();
        tabelaGracz03.add(new Image(v.getA().texDefIcon)).size(25, 25);
        tabelaGracz03.add(new Label("Obrona: " + NewGame.pobierzObrone(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns());
        tabelaGracz03.row();
        tabelaGracz03.add(new Image(v.getA().texHpIcon)).size(25, 25);
        tabelaGracz03.add(new Label("Hp: " + NewGame.pobierzHp(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns());
        tabelaGracz03.row();
        tabelaGracz03.add(new Image(v.getA().texSpdIcon)).size(25, 25);
        tabelaGracz03.add(new Label("Szybkosc: " + NewGame.pobierzSzybkosc(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns());
        tabelaGracz03.row();
        tabelaGracz03.add(new Image(v.getA().texPwrIcon)).size(25, 25);
        tabelaGracz03.add(new Label("Moc: " + NewGame.pobierzMoc(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns());
        tabelaGracz03.row();
        tabelaGracz03.add(new Image(v.getA().texWsdIcon)).size(25, 25);
        tabelaGracz03.add(new Label("Wiedza: " + NewGame.pobierzWiedze(NewGame.klasaPostaciGracz03), v.getA().skin)).colspan(tabelaGracz03.getColumns());
        tabelaGracz03.row();

        tabelaGracz03.add(new Label("Kolor: ", v.getA().skin));
        tabelaGracz03.add(new DefaultActor(new Texture(NewGame.pixmapYellow), 0, 0)).colspan(tabelaGracz03.getColumns());
    }

    /**
     * Formatuje tabelę gracza 4
     */
    private void formatujTabeleGracza04() {

        tabelaGracz04.pad(10);
        tabelaGracz04.add(new Label("Gracz 3", v.getA().skin)).padTop(10);
        tabelaGracz04.add(cbAI3).padTop(10);
        tabelaGracz04.row();

        // Przycisk - przy wyborze ilosci graczy
        TextButton g02B01 = new TextButton("Prev", v.getA().skin);
        g02B01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz04 = NewGame.poprzedniaKlasaPostaci(NewGame.klasaPostaciGracz04);
                tabelaGracz04.clear();
                formatujTabeleGracza04();
            }
        });
        tabelaGracz04.add(g02B01).pad(10);

        // Przycisk + przy wyborze ilośći graczy
        TextButton tB02 = new TextButton("Next", v.getA().skin);
        tB02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                NewGame.klasaPostaciGracz04 = NewGame.nastepnaKlasaPostaci(NewGame.klasaPostaciGracz04);
                tabelaGracz04.clear();
                formatujTabeleGracza04();
            }
        });
        tabelaGracz04.add(tB02).pad(10);
        tabelaGracz04.row();

        tabelaGracz04.add(new Label(NewGame.pobierzTytul(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns()).align(Align.center);
        tabelaGracz04.row();

        tabelaGracz04.add(NewGame.pobierzPortret(NewGame.klasaPostaciGracz04)).align(Align.center).colspan(tabelaGracz04.getColumns());

        tabelaGracz04.row();
        tabelaGracz04.add(new Image(v.getA().texAtcIcon)).size(25, 25);
        tabelaGracz04.add(new Label("Atak: " + NewGame.pobierzAtak(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns());
        tabelaGracz04.row();
        tabelaGracz04.add(new Image(v.getA().texDefIcon)).size(25, 25);
        tabelaGracz04.add(new Label("Obrona: " + NewGame.pobierzObrone(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns());
        tabelaGracz04.row();
        tabelaGracz04.add(new Image(v.getA().texHpIcon)).size(25, 25);
        tabelaGracz04.add(new Label("Hp: " + NewGame.pobierzHp(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns());
        tabelaGracz04.row();
        tabelaGracz04.add(new Image(v.getA().texSpdIcon)).size(25, 25);
        tabelaGracz04.add(new Label("Szybkosc: " + NewGame.pobierzSzybkosc(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns());
        tabelaGracz04.row();
        tabelaGracz04.add(new Image(v.getA().texPwrIcon)).size(25, 25);
        tabelaGracz04.add(new Label("Moc: " + NewGame.pobierzMoc(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns());
        tabelaGracz04.row();
        tabelaGracz04.add(new Image(v.getA().texWsdIcon)).size(25, 25);
        tabelaGracz04.add(new Label("Wiedza: " + NewGame.pobierzWiedze(NewGame.klasaPostaciGracz04), v.getA().skin)).colspan(tabelaGracz04.getColumns());
        tabelaGracz04.row();
        tabelaGracz04.add(new Label("Kolor: ", v.getA().skin));
        tabelaGracz04.add(new DefaultActor(new Texture(NewGame.pixmapGreen), 0, 0)).colspan(tabelaGracz04.getColumns());
    }

    /**
     * Zwraca Okno wyboru mapy
     *
     * @return Window
     */
    private Window getLoadMapWindow() {
        final Window window = new Window("Wybierz Mape", v.getA().skin);
        window.setSize(600, 400);
        window.align(Align.center);

        final List listOfMap = new List(v.getA().skin);

        FileHandle[] files = Gdx.files.local("").list();
        for (FileHandle file : files) {
            if (file.extension().equals("dat")) {
                listOfMap.getItems().add(file);
            } else if (file.extension().equals("map")) {
                listOfMap.getItems().add(file);
            }
        }

        TextButton btnExitWindow = new TextButton("EXIT", v.getA().skin);
        btnExitWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                window.remove();
            }
        });

        TextButton btnWybierzWindowNewMap = new TextButton("Wybieram v2", v.getA().skin);
        btnWybierzWindowNewMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();

                GameStatus.mapV2 = listOfMap;
                GameStatus.mavWersion2 = true;

                FileHandle file = (FileHandle) listOfMap.getSelected();
                Gdx.app.log("Nazwa Pliku", file.name());
                GameStatus.nazwaMapy = file.name();

                window.remove();
            }
        });

        TextButton btnWybierzWindow = new TextButton("Wybieram", v.getA().skin);
        btnWybierzWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                FileHandle file = (FileHandle) listOfMap.getSelected();
                Gdx.app.log("Nazwa Pliku", file.name());
                GameStatus.nazwaMapy = file.name();
                GameStatus.mavWersion2 = false;

                window.remove();
            }
        });


        window.add(listOfMap).size(300, 200);
        window.row();
        window.add(btnExitWindow).size(100, 50).spaceRight(5);
        window.add(btnWybierzWindow).size(100, 50);
        window.add(btnWybierzWindowNewMap).size(100, 50);

        return window;
    }

    /**
     * Zwraca przycisk wyjscia z okna
     *
     * @return Przycisk
     */
    private TextButton getBtnWybierzMape() {
        TextButton btnWybierzMape = new TextButton("Wybierz Mape", v.getA().skin);
        btnWybierzMape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                stage01.addActor(getLoadMapWindow());
            }
        });
        return btnWybierzMape;
    }

    @Override
    public void show() {
        NewGame.klasaPostaciGracz01 = KlasyPostaci.Wojownik;
        Gdx.input.setInputProcessor(stage01);
        if (!tabelaUtworzona) {
            formatujTabeleGracza01();
            formatujTabeleGracza02();
            formatujTabeleGracza03();
            formatujTabeleGracza04();
            formatujTabele01();
            formatujTabeleIlosciGraczy();
            dodajDoStage01();
        } else {
            tabelaGracz01.clear();
            formatujTabeleGracza01();
        }
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

}
