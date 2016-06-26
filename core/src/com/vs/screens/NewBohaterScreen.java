package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.Spells;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.GameStatus;
import com.vs.eoh.NewGame;

import static com.vs.eoh.NewGame.klasaPostaciGracz01;
import static com.vs.eoh.NewGame.pobierzAtak;
import static com.vs.eoh.NewGame.pobierzHp;
import static com.vs.eoh.NewGame.pobierzMoc;
import static com.vs.eoh.NewGame.pobierzObrone;
import static com.vs.eoh.NewGame.pobierzSzybkosc;
import static com.vs.eoh.NewGame.pobierzWiedze;

/**
 * Screen odpowiedzialny za kupno nowego bohatera
 *
 * @author v
 */
public class NewBohaterScreen implements Screen {

    private final OrthographicCamera c;
    private final FitViewport viewPort;

    private final Assets a;
    private final GameStatus gs;
    private final Game g;
    private final Stage stage01;

    private final Table tabela01 = new Table();
    private final Table tabelaNowyBohater = new Table();

    private boolean tabelaUtworzona = false;

    /**
     * @param g
     * @param gs
     * @param a
     * @param stage
     */
    public NewBohaterScreen(Game g, GameStatus gs, Assets a, Stage stage) {

        this.a = a;
        this.gs = gs;
        this.g = g;

        c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), c);

        stage01 = new Stage();
    }

    private void formatujTabeleNowyBohater() {
        tabelaNowyBohater.pad(20);

        // Przycisk - przy wyborze ilosci graczy
        TextButton g01B01 = new TextButton("Prev", a.skin);
        g01B01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                a.buttonClick.play();
                NewGame.klasaPostaciGracz01 = NewGame.poprzedniaKlasaPostaci(NewGame.klasaPostaciGracz01);
                tabelaNowyBohater.clear();
                formatujTabeleNowyBohater();
            }
        });
        tabelaNowyBohater.add(g01B01).pad(20);

        // Przycisk + przy wyborze ilośći graczy
        TextButton tB02 = new TextButton("Next", a.skin);
        tB02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                a.buttonClick.play();
                NewGame.klasaPostaciGracz01 = NewGame.nastepnaKlasaPostaci(NewGame.klasaPostaciGracz01);
                tabelaNowyBohater.clear();
                formatujTabeleNowyBohater();
            }
        });
        tabelaNowyBohater.add(tB02).pad(20);
        tabelaNowyBohater.row();

        tabelaNowyBohater.add(new Label(NewGame.pobierzTytul(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns()).align(Align.center);
        tabelaNowyBohater.row();

        tabelaNowyBohater.add(NewGame.pobierzPortret(NewGame.klasaPostaciGracz01)).align(Align.center).colspan(tabelaNowyBohater.getColumns());

        tabelaNowyBohater.row();
        tabelaNowyBohater.add(new Label("Atak: " + NewGame.pobierzAtak(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns());
        tabelaNowyBohater.row();
        tabelaNowyBohater.add(new Label("Obrona: " + NewGame.pobierzObrone(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns());
        tabelaNowyBohater.row();
        tabelaNowyBohater.add(new Label("Hp: " + NewGame.pobierzHp(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns());
        tabelaNowyBohater.row();
        tabelaNowyBohater.add(new Label("Szybkosc: " + NewGame.pobierzSzybkosc(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns());
        tabelaNowyBohater.row();
        tabelaNowyBohater.add(new Label("Moc: " + NewGame.pobierzMoc(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns());
        tabelaNowyBohater.row();
        tabelaNowyBohater.add(new Label("Wiedza: " + NewGame.pobierzWiedze(NewGame.klasaPostaciGracz01), a.skin)).colspan(tabelaNowyBohater.getColumns());
        tabelaNowyBohater.row();
    }

    private void formatujTabele01() {
        // ustawia rozmiar tebeli na cały ekran
        tabela01.setFillParent(true);
        // ustawia odstęp od krawędzi tabeli
        tabela01.pad(10);
        // włacza linie debugujące tabelę
        tabela01.setDebug(true);

        tabela01.add(new Label("Nowy Bohater", a.skin)).align(Align.center).align(Align.top).expandX().colspan(tabela01.getColumns());
        tabela01.row();

        tabela01.add(tabelaNowyBohater).colspan(4);
        tabela01.row();

        // Przycisk Anuluj
        TextButton btnAnuluj = new TextButton("ANULUJ", a.skin);
        btnAnuluj.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                a.buttonClick.play();
                System.out.println("Anuluj");
                g.setScreen(Assets.mapScreen);
            }
        });
        tabela01.add(btnAnuluj).align(Align.left).colspan(2).pad(10);

        // Przycisk Zakończ
        TextButton btnExit = new TextButton("ZAKONCZ", a.skin);
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                a.buttonClick.play();
                System.out.println("EXIT");
                zakonczGenerowanieNowegoBohatera();
                gs.getGracze().get(gs.getTuraGracza()).setGold(
                        gs.getGracze().get(gs.getTuraGracza()).getGold() - GameStatus.CostOfNewHero);
                g.setScreen(Assets.mapScreen);
            }
        });
        tabela01.add(btnExit).align(Align.right).colspan(2).pad(10);

        tabelaUtworzona = true;
    }

    private void dodajDoStage01() {
        stage01.addActor(tabela01);
    }

    private void zakonczGenerowanieNowegoBohatera() {

        // tymczasowa tekstura przekazana do konstruktora nowego bohatera
        Texture tmpTex, tmpTexZazanaczony;
        // tymczasowa tekstura do określenia lokacji początkowej gracza
        int lokPoczatkowaX = 0, lokPoczatkowaY = 0;


        switch (gs.getTuraGracza()) {
            case 0:
                for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                        if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP1()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                        if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP2()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                        if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP3()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < gs.getMapa().getIloscPolX(); i++) {
                    for (int j = 0; j < gs.getMapa().getIloscPolY(); j++) {
                        if (gs.getMapa().getPola()[i][j].isLokacjaStartowaP4()) {
                            lokPoczatkowaX = i;
                            lokPoczatkowaY = j;
                        }
                    }
                }
                break;
        }

        tmpTex = NewGame.getTeksturaBohatera(klasaPostaciGracz01);
        tmpTexZazanaczony = NewGame.getTeksturaBohateraZaznaczonego(klasaPostaciGracz01);

        gs.gracze.get(gs.getTuraGracza()).getBohaterowie().add(new Bohater(tmpTex, tmpTexZazanaczony, lokPoczatkowaX * 100, lokPoczatkowaY * 100, a, 0, 0, gs, g, klasaPostaciGracz01));

        int bohGracza = gs.getTuraGracza();
        int wymTabBoh = gs.gracze.get(gs.getTuraGracza()).getBohaterowie().size() - 1;

        // Ustala do którego gracza z tablicy graczy należy bohater
        gs.gracze.get(gs.getTuraGracza()).getBohaterowie().get(wymTabBoh).setPrzynaleznoscDoGracza(gs.getTuraGracza());

        switch (bohGracza) {
            case 0:
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh));
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 1:
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh));
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 2:
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh));
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
            case 3:
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].setBohater(gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh));
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozXnaMapie(lokPoczatkowaX);
                gs.getMapa().pola[lokPoczatkowaX][lokPoczatkowaY].getBohater().setPozYnaMapie(lokPoczatkowaY);
                break;
        }

        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setAtak(pobierzAtak(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setObrona(pobierzObrone(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setHp(pobierzHp(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setActualHp(pobierzHp(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setSzybkosc(pobierzSzybkosc(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setPozostaloRuchow(pobierzSzybkosc(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setWiedza(pobierzWiedze(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setMoc(pobierzMoc(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setMana(pobierzWiedze(klasaPostaciGracz01));
        gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).setActualMana(pobierzWiedze(klasaPostaciGracz01));

        if (klasaPostaciGracz01.equals(KlasyPostaci.Lowca)) {
            gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).getListOfSpells().add(Spells.Haste);
        } else if (klasaPostaciGracz01.equals(KlasyPostaci.Czarodziej)) {
            gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).getListOfSpells().add(Spells.FireBall);
        } else if (klasaPostaciGracz01.equals(KlasyPostaci.Wojownik)) {
            gs.gracze.get(bohGracza).getBohaterowie().get(wymTabBoh).getListOfSpells().add(Spells.Rage);
        }


        Assets.stage01MapScreen.addActor(gs.getGracze().get(gs.getTuraGracza()).getBohaterowie().get(wymTabBoh));

    }

    @Override
    public void show() {
        NewGame.klasaPostaciGracz01 = KlasyPostaci.Wojownik;
        Gdx.input.setInputProcessor(stage01);
        if (!tabelaUtworzona) {
            formatujTabeleNowyBohater();
            formatujTabele01();
            dodajDoStage01();
        } else {
            tabelaNowyBohater.clear();
            formatujTabeleNowyBohater();
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
