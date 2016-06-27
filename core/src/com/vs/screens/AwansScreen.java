package com.vs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.vs.enums.KlasyPostaci;
import com.vs.enums.Spells;
import com.vs.eoh.Assets;
import com.vs.eoh.Bohater;
import com.vs.eoh.ButtonActor;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.GameStatus;
import com.vs.eoh.V;

import java.util.ArrayList;

/**
 * Wyświetla screen Awansu bohatera.
 *
 * @author v
 */
public class AwansScreen implements Screen {

    private final Stage stage01 = new Stage();
    private final TextButton btnExit;
    /**
     * Główna tabela.
     */
    private final Table mainTable;
    private final Table leftTable;
    private final Table rightTable;
    private V v;
    private Awans awans = new Awans();
    /**
     * informuje czy Screen awansu jest pokazany
     */
    private boolean awansScreenShow = false;

    //private final TextButton btnExit;
    public AwansScreen(final V v) {
        this.v = v;

        btnExit = new TextButton("ZAKONCZ", this.v.getA().skin);
        btnExit.addListener(
                new ClickListener() {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        v.getA().buttonClick.play();

                        awans.zakonczAwans(v.getGs());

                        awansScreenShow = false;
                        mainTable.clear();
                        rightTable.clear();
                        leftTable.clear();
                        awans.czyscWyborKlasy();
                        v.getG().setScreen(v.getMapScreen());
                    }
                }
        );

        leftTable = new Table(v.getA().skin);
        rightTable = new Table(v.getA().skin);
        mainTable = new Table(v.getA().skin);
    }

    /**
     * Formatuje główną tabelę.
     */
    private void formatMainTable() {

        formatLeftTable();
        formatRightTable();

        mainTable.setFillParent(true);
        // ustawia odstęp od krawędzi tabeli
        mainTable.pad(5);
        // włacza linie debugujące tabelę
        mainTable.setDebug(true);

        Label lblTitle = new Label("AWANS", this.v.getA().skin);
        mainTable.add(lblTitle).expandX().pad(2).colspan(10);
        mainTable.row();
        Label lblPoziom = new Label("Poziom " + (v.getGs().getBohaterZaznaczony().getLevelOfExp() + 1), v.getA().skin);
        mainTable.add(lblPoziom).expandX().pad(2).colspan(10);
        mainTable.row();
        mainTable.add(leftTable);
        mainTable.add(rightTable).expand().colspan(10);
        mainTable.row();
        mainTable.add(btnExit).expandX().pad(2).colspan(10).size(100, 50);
    }

    private void formatLeftTable() {
        leftTable.pad(5);
        leftTable.setDebug(true);

        final Image imgPoziom1, imgPoziom2, imgPoziom3A, imgPoziom3B, imgPoziom4A, imgPoziom4B;

        if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej) {
            imgPoziom1 = new Image(v.getA().tAtlasWizard.findRegion(getImageTextureRegion("0")));
            imgPoziom2 = new Image(v.getA().tAtlasWizard.findRegion(getImageTextureRegion("1")));
            imgPoziom3A = new Image(v.getA().tAtlasWizard.findRegion(getImageTextureRegion("2A")));
            imgPoziom3B = new Image(v.getA().tAtlasWizard.findRegion(getImageTextureRegion("2B")));
            imgPoziom4A = new Image(v.getA().tAtlasWizard.findRegion(getImageTextureRegion("3A")));
            imgPoziom4B = new Image(v.getA().tAtlasWizard.findRegion(getImageTextureRegion("3B")));
        } else if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
            imgPoziom1 = new Image(v.getA().tAtlasHunter.findRegion(getImageTextureRegion("0")));
            imgPoziom2 = new Image(v.getA().tAtlasHunter.findRegion(getImageTextureRegion("1")));
            imgPoziom3A = new Image(v.getA().tAtlasHunter.findRegion(getImageTextureRegion("2A")));
            imgPoziom3B = new Image(v.getA().tAtlasHunter.findRegion(getImageTextureRegion("2B")));
            imgPoziom4A = new Image(v.getA().tAtlasHunter.findRegion(getImageTextureRegion("3A")));
            imgPoziom4B = new Image(v.getA().tAtlasHunter.findRegion(getImageTextureRegion("3B")));
        } else {
            imgPoziom1 = new Image(v.getA().tAtlasWarrior.findRegion(getImageTextureRegion("0")));
            imgPoziom2 = new Image(v.getA().tAtlasWarrior.findRegion(getImageTextureRegion("1")));
            imgPoziom3A = new Image(v.getA().tAtlasWarrior.findRegion(getImageTextureRegion("2A")));
            imgPoziom3B = new Image(v.getA().tAtlasWarrior.findRegion(getImageTextureRegion("2B")));
            imgPoziom4A = new Image(v.getA().tAtlasWarrior.findRegion(getImageTextureRegion("3A")));
            imgPoziom4B = new Image(v.getA().tAtlasWarrior.findRegion(getImageTextureRegion("3B")));
        }

        Label lblPoziom1 = new Label("1", v.getA().skin);
        final Label lblPoziom2 = new Label("2", v.getA().skin);
        final Label lblPoziom3A = new Label("3A", v.getA().skin);
        final Label lblPoziom3B = new Label("3B", v.getA().skin);
        final Label lblPoziom4A = new Label("4A", v.getA().skin);
        final Label lblPoziom4B = new Label("4B", v.getA().skin);

        definiujNazwy(lblPoziom1, lblPoziom2, lblPoziom3A, lblPoziom3B, lblPoziom4A, lblPoziom4B);

        TextButton btnPoziom2 = new TextButton("WYBIERZ", v.getA().skin);
        TextButton btnPoziom3A = new TextButton("WYBIERZ", v.getA().skin);
        TextButton btnPoziom3B = new TextButton("WYBIERZ", v.getA().skin);
        TextButton btnPoziom4A = new TextButton("WYBIERZ", v.getA().skin);
        TextButton btnPoziom4B = new TextButton("WYBIERZ", v.getA().skin);

        btnPoziom2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                v.getA().buttonClick.play();

                awans.czyscWyborKlasy();
                awans.wybor2 = true;
                v.getGs().getBohaterZaznaczony().setG1(true);
                v.getGs().getBohaterZaznaczony().setImage(imgPoziom2);
                if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/wizard/1.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/wizard/1z.png"));
                } else if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/hunter/1.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/hunter/1z.png"));
                } else {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/warrior/1.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/warrior/1z.png"));
                }
                awans.classOfHero = lblPoziom2.getText().toString();
                Gdx.app.log("Aktualna klasa postaci", awans.classOfHero);
                reformatujTabele();
            }
        });

        btnPoziom3A.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                v.getA().buttonClick.play();

                awans.czyscWyborKlasy();
                awans.wybor3A = true;
                v.getGs().getBohaterZaznaczony().setG2A(true);
                v.getGs().getBohaterZaznaczony().setG2B(false);
                if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/wizard/2A.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/wizard/2Az.png"));
                } else if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/hunter/2A.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/hunter/2Az.png"));
                } else {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/warrior/2A.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/warrior/2Az.png"));
                }
                awans.classOfHero = lblPoziom3A.getText().toString();
                Gdx.app.log("Aktualna klasa postaci", awans.classOfHero);
                reformatujTabele();
            }
        });

        btnPoziom3B.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                v.getA().buttonClick.play();

                awans.czyscWyborKlasy();
                awans.wybor3B = true;
                v.getGs().getBohaterZaznaczony().setG2B(true);
                v.getGs().getBohaterZaznaczony().setG2A(false);
                if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/wizard/2B.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/wizard/2Bz.png"));
                } else if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/hunter/2B.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/hunter/2Bz.png"));
                } else {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/warrior/2B.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/warrior/2Bz.png"));
                }
                awans.classOfHero = lblPoziom3B.getText().toString();
                Gdx.app.log("Aktualna klasa postaci", awans.classOfHero);
                reformatujTabele();
            }
        });

        btnPoziom4A.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                v.getA().buttonClick.play();

                awans.czyscWyborKlasy();
                awans.wybor4A = true;
                v.getGs().getBohaterZaznaczony().setG3B(false);
                v.getGs().getBohaterZaznaczony().setG3A(true);
                if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/wizard/3A.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/wizard/3Az.png"));
                } else if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/hunter/3A.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/hunter/3Az.png"));
                } else {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/warrior/3A.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/warrior/3Az.png"));
                }
                awans.classOfHero = lblPoziom4A.getText().toString();
                Gdx.app.log("Aktualna klasa postaci", awans.classOfHero);
                reformatujTabele();
            }
        });

        btnPoziom4B.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                v.getA().buttonClick.play();

                awans.czyscWyborKlasy();
                awans.wybor4B = true;
                v.getGs().getBohaterZaznaczony().setG3B(true);
                v.getGs().getBohaterZaznaczony().setG3A(false);
                if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/wizard/3B.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/wizard/3Bz.png"));
                } else if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/hunter/3B.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/hunter/3Bz.png"));
                } else {
                    v.getGs().getBohaterZaznaczony().setBohaterTex(new Texture("moby/warrior/3B.png"));
                    v.getGs().getBohaterZaznaczony().setBohaterCheckTex(new Texture("moby/warrior/3Bz.png"));
                }
                awans.classOfHero = lblPoziom4B.getText().toString();
                Gdx.app.log("Aktualna klasa postaci", awans.classOfHero);
                reformatujTabele();
            }
        });

        if (v.getGs().getBohaterZaznaczony().getLevelOfExp() < 4) {
            switch (v.getGs().getBohaterZaznaczony().getLevelOfExp()) {
                case 1:
                    btnPoziom3A.setVisible(false);
                    btnPoziom3B.setVisible(false);
                    btnPoziom4A.setVisible(false);
                    btnPoziom4B.setVisible(false);
                    break;
                case 2:
                    btnPoziom2.setVisible(false);
                    btnPoziom4A.setVisible(false);
                    btnPoziom4B.setVisible(false);
                    break;
                case 3:
                    btnPoziom2.setVisible(false);
                    btnPoziom3A.setVisible(false);
                    btnPoziom3B.setVisible(false);
                    if (v.getGs().getBohaterZaznaczony().getActualHeroClass().equals("Arcypaladyn")
                            || v.getGs().getBohaterZaznaczony().getActualHeroClass().equals("Strzelec")
                            || v.getGs().getBohaterZaznaczony().getActualHeroClass().equals("Kaplan")) {
                        btnPoziom4B.setVisible(false);
                    } else if (v.getGs().getBohaterZaznaczony().getActualHeroClass().equals("Msciciel")
                            || v.getGs().getBohaterZaznaczony().getActualHeroClass().equals("Lotr")
                            || v.getGs().getBohaterZaznaczony().getActualHeroClass().equals("Elementalista")) {
                        btnPoziom4A.setVisible(false);
                    }
                    break;
            }
        } else {
            btnPoziom2.setVisible(false);
            btnPoziom3A.setVisible(false);
            btnPoziom3B.setVisible(false);
            btnPoziom4A.setVisible(false);
            btnPoziom4B.setVisible(false);
        }

        leftTable.add(imgPoziom1).colspan(2);
        leftTable.row();
        leftTable.add(lblPoziom1).pad(2).colspan(2);
        leftTable.row();
        leftTable.add(imgPoziom2).colspan(2);
        leftTable.row();
        leftTable.add(lblPoziom2).pad(2).colspan(2);
        leftTable.row();
        leftTable.add(btnPoziom2).pad(2).colspan(2);
        leftTable.row();
        leftTable.add(imgPoziom3A);
        leftTable.add(imgPoziom3B);
        leftTable.row();
        leftTable.add(lblPoziom3A).pad(2);
        leftTable.add(lblPoziom3B).pad(2);
        leftTable.row();
        leftTable.add(btnPoziom3A).pad(2);
        leftTable.add(btnPoziom3B).pad(2);
        leftTable.row();
        leftTable.add(imgPoziom4A);
        leftTable.add(imgPoziom4B);
        leftTable.row();
        leftTable.add(lblPoziom4A).pad(2);
        leftTable.add(lblPoziom4B).pad(2);
        leftTable.row();
        leftTable.add(btnPoziom4A).pad(2);
        leftTable.add(btnPoziom4B).pad(2);
        leftTable.row();
    }

    private void formatRightTable() {
        rightTable.pad(5);
        rightTable.setDebug(true);

        Label lblAtrybutKlasowy = new Label("Atrybut klasowy: " + awans.getAtrybutKlasowy(), this.v.getA().skin);
        rightTable.add(lblAtrybutKlasowy).colspan(10).align(Align.center);
        rightTable.row();

        Label lbl01 = new Label("Wybierz atrybut: ", this.v.getA().skin);
        rightTable.add(lbl01).colspan(10).align(Align.center);
        rightTable.row();

        ButtonActor btn01 = new ButtonActor(v.getA().texAtcIcon, 0, 0);
        ButtonActor btn02 = new ButtonActor(v.getA().texDefIcon, 0, 0);
        ButtonActor btn03 = new ButtonActor(v.getA().texSpdIcon, 0, 0);
        ButtonActor btn04 = new ButtonActor(v.getA().texHpIcon, 0, 0);
        ButtonActor btn05 = new ButtonActor(v.getA().texPwrIcon, 0, 0);
        ButtonActor btn06 = new ButtonActor(v.getA().texWsdIcon, 0, 0);

        btn01.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                awans.zwiekszAtak();
                reformatujTabele();
            }
        });

        btn02.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                awans.zwiekszObrone();
                reformatujTabele();
            }
        });

        btn03.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                awans.zwiekszSzybkosc();
                reformatujTabele();
            }
        });

        btn04.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                awans.zwiekszHp();
                reformatujTabele();
            }
        });

        btn05.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                awans.zwiekszMoc();
                reformatujTabele();
            }
        });

        btn06.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.getA().buttonClick.play();
                awans.zwiekszWiedze();
                reformatujTabele();
            }
        });

        rightTable.add(btn01).size(50).align(Align.center).pad(10);
        rightTable.add(btn02).size(50).align(Align.center).pad(10);
        rightTable.add(btn03).size(50).align(Align.center).pad(10);
        rightTable.add(btn04).size(50).align(Align.center).pad(10);
        rightTable.add(btn05).size(50).align(Align.center).pad(10);
        rightTable.add(btn06).size(50).align(Align.center).pad(10);

        rightTable.row();

        Label lblOpis1 = new Label("Aktualne: ", v.getA().skin);
        Label lblatc = new Label("Atak: " + v.getGs().getBohaterZaznaczony().getAtak(), v.getA().skin);
        Label lblobr = new Label("Obrona: " + v.getGs().getBohaterZaznaczony().getObrona(), v.getA().skin);
        Label lblszb = new Label("Szybkosc: " + v.getGs().getBohaterZaznaczony().getSzybkosc(), v.getA().skin);
        Label lblhpp = new Label("Hp: " + v.getGs().getBohaterZaznaczony().getHp(), v.getA().skin);
        Label lblmoc = new Label("Moc: " + v.getGs().getBohaterZaznaczony().getMoc(), v.getA().skin);
        Label lblwie = new Label("Wiedza: " + v.getGs().getBohaterZaznaczony().getWiedza(), v.getA().skin);

        rightTable.add(lblOpis1).colspan(10).align(Align.center);
        rightTable.row();
        rightTable.add(lblatc).pad(2);
        rightTable.add(lblobr).pad(2);
        rightTable.add(lblszb).pad(2);
        rightTable.add(lblhpp).pad(2);
        rightTable.add(lblmoc).pad(2);
        rightTable.add(lblwie).pad(2);
        rightTable.row();

        Label lblOpis2 = new Label("Po awansie: ", v.getA().skin);
        Label lblatc2 = new Label("Atak: " + (v.getGs().getBohaterZaznaczony().getAtak() + awans.tmpAtak + awans.atrKlasowyAtak), v.getA().skin);
        Label lblobr2 = new Label("Obrona: " + (v.getGs().getBohaterZaznaczony().getObrona() + awans.tmpObrona + awans.atrKlasowyObrona), v.getA().skin);
        Label lblszb2 = new Label("Szybkosc: " + (v.getGs().getBohaterZaznaczony().getSzybkosc() + awans.tmpSzybkosc + awans.atrKlasowySzybkosc), v.getA().skin);
        Label lblhpp2 = new Label("Hp: " + (v.getGs().getBohaterZaznaczony().getHp() + awans.tmpHp + awans.atrKlasowyHp), v.getA().skin);
        Label lblmoc2 = new Label("Moc: " + (v.getGs().getBohaterZaznaczony().getMoc() + awans.tmpMoc + awans.atrKlasowyMoc), v.getA().skin);
        Label lblwie2 = new Label("Wiedza: " + (v.getGs().getBohaterZaznaczony().getWiedza() + awans.tmpWiedza + awans.atrKlasowyWiedza), v.getA().skin);

        rightTable.add(lblOpis2).colspan(10).align(Align.center);
        rightTable.row();
        rightTable.add(lblatc2).pad(2);
        rightTable.add(lblobr2).pad(2);
        rightTable.add(lblszb2).pad(2);
        rightTable.add(lblhpp2).pad(2);
        rightTable.add(lblmoc2).pad(2);
        rightTable.add(lblwie2).pad(2);
        rightTable.row();

        Label lblNewSpells = new Label("Nowe Czary: ", v.getA().skin);
        rightTable.add(lblNewSpells).pad(2).colspan(10);
        rightTable.row();

        for (Spells listaCzarow : awans.listaCzarow) {
            rightTable.add(ikoneNowegoCzaru()).pad(2).colspan(10);
        }
        rightTable.row();

        for (Spells listaCzarow : awans.listaCzarow) {
            rightTable.add(new Label(listaCzarow.toString(), v.getA().skin)).pad(2).colspan(10);
        }
        rightTable.row();
    }

    /**
     * Zwraca ikonę nowego czaru.
     *
     * @return Default Actor
     */
    private DefaultActor ikoneNowegoCzaru() {
        for (Spells spls : awans.listaCzarow) {
            switch (spls) {
                case SongOfGlory:
                    return new DefaultActor(v.getA().texSpellSongOfGlory, 0, 0);
                case Discouragement:
                    return new DefaultActor(v.getA().texSpellDiscouragement, 0, 0);
                case Fury:
                    return new DefaultActor(v.getA().texSpellFury, 0, 0);
                case Charge:
                    return new DefaultActor(v.getA().texSpellCharge, 0, 0);
                case FinalJudgment:
                    return new DefaultActor(v.getA().texSpellFinalJudgment, 0, 0);
                case Frozen:
                    return new DefaultActor(v.getA().texSpellFreez, 0, 0);
                case Thunder:
                    return new DefaultActor(v.getA().texSpellThunder, 0, 0);
                case MeteorShower:
                    return new DefaultActor(v.getA().texSpellMeteorShower, 0, 0);
                case Bless:
                    return new DefaultActor(v.getA().texSpellBless, 0, 0);
                case Prayer:
                    return new DefaultActor(v.getA().texSpellPrayer, 0, 0);
                case LongShot:
                    return new DefaultActor(v.getA().texSpellLongShot, 0, 0);
                case VampireTouch:
                    return new DefaultActor(v.getA().texSpellVampireTouch, 0, 0);
                case SummonBear:
                    return new DefaultActor(v.getA().texSpellSummonBear, 0, 0);
                case SummonWolf:
                    return new DefaultActor(v.getA().texSpellSummonWolf, 0, 0);
                case Poison:
                    return new DefaultActor(v.getA().texSpellPoison, 0, 0);
            }
        }
        return null;
    }

    /**
     * Formatuje wszystkie tabele.
     */
    private void reformatujTabele() {
        awans.uzupelnijListyCzarow();
        mainTable.clear();
        rightTable.clear();
        leftTable.clear();
        formatMainTable();
    }

    /**
     * Definiuje jakie klasy bohaterów znajdą się na labelach
     *
     * @param l1
     * @param l2
     * @param l3
     * @param l4
     * @param l5
     * @param l6
     */
    private void definiujNazwy(Label l1, Label l2, Label l3, Label l4, Label l5, Label l6) {
        switch (v.getGs().getBohaterZaznaczony().klasyPostaci) {
            case Wojownik:
                l1.setText("Wojownik");
                l2.setText("Paladyn");
                l3.setText("Arcypaladyn");
                l4.setText("Msciciel");
                l5.setText("Pogromca");
                l6.setText("Sedzia");
                break;
            case Czarodziej:
                l1.setText("Czarodziej");
                l2.setText("Mag");
                l3.setText("Kaplan");
                l4.setText("Elementalista");
                l5.setText("Hierofanta");
                l6.setText("Arcymag");
                break;
            case Lowca:
                l1.setText("Lowca");
                l2.setText("Lucznik");
                l3.setText("Strzelec");
                l4.setText("Lotr");
                l5.setText("Strzelec wyborowy");
                l6.setText("Zabojca");

        }
    }

    /**
     * Zwraca odposiednie string z Texture Regionem
     *
     * @param level poziom
     * @return String z Texture Regionem
     */
    private String getImageTextureRegion(String level) {
        if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Wojownik ||
                v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Czarodziej ||
                v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Lowca) {
            if (level.equals("0")) {
                return "0";
            } else if (level.equals("1")) {
                if (v.getGs().getBohaterZaznaczony().isG1()) {
                    return "1";
                } else {
                    return "1-bw";
                }
            } else if (level.equals("2A")) {
                if (v.getGs().getBohaterZaznaczony().isG2A()) {
                    return "2A";
                } else {
                    return "2A-bw";
                }
            } else if (level.equals("2B")) {
                if (v.getGs().getBohaterZaznaczony().isG2B()) {
                    return "2B";
                } else {
                    return "2B-bw";
                }
            } else if (level.equals("3A")) {
                if (v.getGs().getBohaterZaznaczony().isG3A()) {
                    return "3A";
                } else {
                    return "3A-bw";
                }
            } else if (level.equals("3B")) {
                if (v.getGs().getBohaterZaznaczony().isG3B()) {
                    return "3B";
                } else {
                    return "3B-bw";
                }
            }
        }
        return "0-bw";
    }

    /**
     * Zwraca String z scieżką do pliku
     *
     * @param level Poziom bohatera
     * @return String ze scieżka do pliku.
     */
    private String getTextureForHero(String level) {
        if (v.getGs().getBohaterZaznaczony().getKlasyPostaci() == KlasyPostaci.Wojownik) {
            if (level.equals("0")) {
                return "moby/warrior/0.png";
            } else if (level.equals("1")) {
                if (v.getGs().getBohaterZaznaczony().isG1()) {
                    return "moby/warrior/1.png";
                } else {
                    return "1-bw";
                }
            } else if (level.equals("2A")) {
                if (v.getGs().getBohaterZaznaczony().isG2A()) {
                    return "2A";
                } else {
                    return "2A-bw";
                }
            } else if (level.equals("2B")) {
                if (v.getGs().getBohaterZaznaczony().isG2B()) {
                    return "2B";
                } else {
                    return "2B-bw";
                }
            } else if (level.equals("3A")) {
                if (v.getGs().getBohaterZaznaczony().isG3A()) {
                    return "3A";
                } else {
                    return "3A-bw";
                }
            } else if (level.equals("3B")) {
                if (v.getGs().getBohaterZaznaczony().isG3B()) {
                    return "3B";
                } else {
                    return "3B-bw";
                }
            }
        }
        return "moby/warrior/0.png";
    }

    @Override
    public void show() {
        if (!awansScreenShow) {
            Gdx.input.setInputProcessor(stage01);
            formatMainTable();
            stage01.addActor(mainTable);
            awansScreenShow = true;
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

    public class Awans {

        public String classOfHero;

        public int tmpAtak = 0;
        public int tmpObrona = 0;
        public int tmpSzybkosc = 0;
        public int tmpHp = 0;
        public int tmpMoc = 0;
        public int tmpWiedza = 0;

        public int atrKlasowyAtak = 0;
        public int atrKlasowyObrona = 0;
        public int atrKlasowySzybkosc = 0;
        public int atrKlasowyHp = 0;
        public int atrKlasowyMoc = 0;
        public int atrKlasowyWiedza = 0;

        public boolean wybor2 = false;
        public boolean wybor3A = false;
        public boolean wybor3B = false;
        public boolean wybor4A = false;
        public boolean wybor4B = false;

        private ArrayList<Spells> listaCzarow;

        public Awans() {
            listaCzarow = new ArrayList<Spells>();
        }

        /**
         * Czyści wybór klasy.
         */
        public void czyscWyborKlasy() {
            listaCzarow.clear();
            wybor2 = false;
            wybor3A = false;
            wybor3B = false;
            wybor4A = false;
            wybor4B = false;
        }

        /**
         * Uzupełnia listy czarów dla klas bohaterów
         */
        public void uzupelnijListyCzarow() {

            listaCzarow.clear();

            switch (v.getGs().getBohaterZaznaczony().getKlasyPostaci()) {
                case Wojownik:
                    if (wybor2) {
                        listaCzarow.add(Spells.SongOfGlory);
                    } else if (wybor3A) {
                        listaCzarow.add(Spells.Charge);
                    } else if (wybor3B) {
                        listaCzarow.add(Spells.Discouragement);
                    } else if (wybor4A) {
                        listaCzarow.add(Spells.Fury);
                    } else if (wybor4B) {
                        listaCzarow.add(Spells.FinalJudgment);
                    }
                    break;
                case Czarodziej:
                    if (wybor2) {
                        listaCzarow.add(Spells.Frozen);
                    } else if (wybor3A) {
                        listaCzarow.add(Spells.Bless);
                    } else if (wybor3B) {
                        listaCzarow.add(Spells.Thunder);
                    } else if (wybor4A) {
                        listaCzarow.add(Spells.Prayer);
                    } else if (wybor4B) {
                        listaCzarow.add(Spells.MeteorShower);
                    }
                    break;
                case Lowca:
                    if (wybor2) {
                        listaCzarow.add(Spells.Poison);
                    } else if (wybor3A) {
                        listaCzarow.add(Spells.SummonWolf);
                    } else if (wybor3B) {
                        listaCzarow.add(Spells.SummonBear);
                    } else if (wybor4A) {
                        listaCzarow.add(Spells.LongShot);
                    } else if (wybor4B) {
                        listaCzarow.add(Spells.VampireTouch);
                    }
                    break;
            }
        }

        public void zwiekszAtak() {
            wyczyscTmpStats();
            tmpAtak += 1;
        }

        public void zwiekszObrone() {
            wyczyscTmpStats();
            tmpObrona += 1;
        }

        public void zwiekszSzybkosc() {
            wyczyscTmpStats();
            tmpSzybkosc += 1;
        }

        public void zwiekszHp() {
            wyczyscTmpStats();
            tmpHp += 2;
        }

        public void zwiekszMoc() {
            wyczyscTmpStats();
            tmpMoc += 1;
        }

        public void zwiekszWiedze() {
            wyczyscTmpStats();
            tmpWiedza += 1;
        }

        private void wyczyscTmpStats() {
            this.tmpAtak = 0;
            this.tmpObrona = 0;
            this.tmpSzybkosc = 0;
            this.tmpHp = 0;
            this.tmpMoc = 0;
            this.tmpWiedza = 0;
        }

        public String getAtrybutKlasowy() {
            this.atrKlasowyAtak = 0;
            this.atrKlasowyObrona = 0;
            this.atrKlasowySzybkosc = 0;
            this.atrKlasowyHp = 0;
            this.atrKlasowyMoc = 0;
            this.atrKlasowyWiedza = 0;

            switch (v.getGs().getBohaterZaznaczony().getKlasyPostaci()) {
                case Wojownik:
                    atrKlasowyAtak += 1;
                    return "Atak + 1";
                case Czarodziej:
                    atrKlasowyMoc += 1;
                    return "Moc + 1";
                case Lowca:
                    atrKlasowySzybkosc += 1;
                    return "Szybkosc + 1";
                case Giermek:
                    atrKlasowyObrona += 1;
                    return "Obrona + 1";
                case Twierdza:
                    atrKlasowyHp += 4;
                    return "HP + 4";
            }
            return null;
        }

        public void zakonczAwans(GameStatus gs) {
            Bohater b = v.getGs().getBohaterZaznaczony();

            b.setAtak(b.getAtak() + this.tmpAtak + this.atrKlasowyAtak);
            b.setObrona(b.getObrona() + this.tmpObrona + this.atrKlasowyObrona);
            b.setSzybkosc(b.getSzybkosc() + this.tmpSzybkosc + this.atrKlasowySzybkosc);
            b.setHp(b.getHp() + this.tmpHp + this.atrKlasowyHp);
            b.setMoc(b.getMoc() + this.tmpMoc + this.atrKlasowyMoc);
            b.setWiedza(b.getWiedza() + this.tmpWiedza + this.atrKlasowyWiedza);

            //Ustala poziom many na poziomie nowej wiedzy.
            b.setMana(b.getWiedza());
            b.setActualMana(b.getWiedza());

            b.setLevelOfExp(b.getLevelOfExp() + 1);

            b.setActualHeroClass(this.classOfHero);

            // Oryginalne ustawienie osiągnięcia następnego poziomu. NIE KASOWAĆ
            //b.setExpToNextLevel(b.getExp() + 2 * b.getExp());
            b.setExpToNextLevel(b.getExp() + 100);

            for (Spells listaCzarow1 : awans.listaCzarow) {
                v.getGs().getBohaterZaznaczony().getListOfSpells().add(listaCzarow1);
            }

            b.aktualizujTeksture();
        }

        /**
         * Zwraca listę czarów dostępnych dla klasy.
         *
         * @return
         */
        public ArrayList<Spells> getListaCzarow() {
            return listaCzarow;
        }

        /**
         * Ustala listę czarów dostępnych dla klasy.
         *
         * @param listaCzarow
         */
        public void setListaCzarow(ArrayList<Spells> listaCzarow) {
            this.listaCzarow = listaCzarow;
        }
    }
}
