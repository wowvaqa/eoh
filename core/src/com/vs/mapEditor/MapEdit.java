package com.vs.mapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vs.enums.TypyTerenu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by v on 17.07.16.
 * Class responsible for functions of Map Editor and also for creating a stage.
 */
public class MapEdit implements Serializable {

    public FieldOfEditor[][] fields;

    public int mapColumns = 0;
    public int mapRows = 0;

    public DrawingType drawingType = DrawingType.none;

    /**
     * Create stage.
     *
     * @param sizeRow  How many rows will have a stage.
     * @param sizeColumns How many cells will have a stage.
     * @return Stage
     */
    public Stage createStage(int sizeColumns, int sizeRow, Viewport viewport) {
        Stage stage = new Stage(viewport);

        mapColumns = sizeColumns;
        mapRows = sizeRow;

        fields = new FieldOfEditor[sizeColumns][sizeRow];

        for (int i = 0; i < sizeColumns; i++) {
            for (int j = 0; j < sizeRow; j++) {
                fields[i][j] = new FieldOfEditor();
                fields[i][j].typyTerenu = TypyTerenu.Trawa;
                fields[i][j].terrainGrass = true;
                fields[i][j].posX = i;
                fields[i][j].posY = j;
                fillField(fields[i][j]);
            }
        }

        for (int i = 0; i < sizeColumns; i++) {
            for (int j = 0; j < sizeRow; j++) {
                stage.addActor(fields[i][j]);
            }
        }
        return stage;
    }

    /**
     * Fills field texture
     *
     * @param field FieldOfEditor object to be filled.
     */
    public void fillField(FieldOfEditor field) {
        if (field.typyTerenu.equals(TypyTerenu.Trawa)) {
            field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("interface/mapEditor/terrains/grass.png"))));
            field.setPosition(field.posX * 100, field.posY * 100);
        } else if (field.typyTerenu.equals(TypyTerenu.Drzewo)) {
            field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("interface/mapEditor/terrains/forestC.png"))));
            field.setPosition(field.posX * 100, field.posY * 100);
        } else if (field.typyTerenu.equals(TypyTerenu.Gory)) {
            field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("interface/mapEditor/terrains/mountainC.png"))));
            field.setPosition(field.posX * 100, field.posY * 100);
        } else if (field.typyTerenu.equals(TypyTerenu.Rzeka)) {
            field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("interface/mapEditor/terrains/river.png"))));
            field.setPosition(field.posX * 100, field.posY * 100);
        }

        if (field.player1StartLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/players/player1.png"));
        } else if (field.player2StartLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/players/player2.png"));
        }

        if (field.tresureBoxLvl1) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/boxes/texTresureBoxLvl1.png"));
        } else if (field.tresureBoxLvl2) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/boxes/texTresureBoxLvl2.png"));
        }

        if (field.towerMagic) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/buldings/towerOfMagic.png"));
        } else if (field.towerWisdom) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/buldings/towerOfWisdom.png"));
        } else if (field.towerDefence) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/buldings/towerOfDefence.png"));
        }

        if (field.mobSkeletonLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "moby/mobSzkieletfTex.png"));
        } else if (field.mobWolfLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "moby/mobWolfTex.png"));
        } else if (field.mobRandomLevel1) {
            field.setDrawable(mergeDrawable("grass100x100.png", "moby/mobRandomLevel1.png"));
        } else if (field.mobSpiderLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "moby/mobSpiderTex.png"));
        } else if (field.mobZombieLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "moby/mobZombieTex.png"));
        } else if (field.mobRandomLevel2) {
            field.setDrawable(mergeDrawable("grass100x100.png", "moby/mobRandomLevel2.png"));
        }

        field.setWidth(100);
        field.setHeight(100);
    }

    /**
     * Load map from file
     * @param listOfMap List of Map
     * @return Map object
     */
    public MapEdit loadMap(List listOfMap) {
        FileHandle file = (FileHandle) listOfMap.getSelected();
        MapEdit map = new MapEdit();
        try {
            map = (MapEdit) FileOperations.deserialize(file.readBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Saving map to file.
     */
    public void saveMap(String name) throws IOException {
        FileHandle file = Gdx.files.local(name + ".map");
        try {
            file.writeBytes(FileOperations.serialize(this), false);
        } catch (Exception ex) {
            Gdx.app.log("Save Map Error", "Nie mogę zapisać do pliku.");
        }
    }

    /**
     * Redraw a stage.
     *
     * @param stage object.
     */
    public void redrawStage(Stage stage) {

    }

    /**
     * Merging two iamges into one.
     *
     * @param pathOrginal Path to source image.
     * @param pathToMerge Path to image which be merge.
     * @return
     */
    private Drawable mergeDrawable(String pathOrginal, String pathToMerge) {
        Texture texture = new Texture(pathOrginal);
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        Texture texture2 = new Texture(pathToMerge);
        if (!texture.getTextureData().isPrepared()) {
            texture2.getTextureData().prepare();
        }
        Pixmap pixmap2 = texture2.getTextureData().consumePixmap();

        pixmap.drawPixmap(pixmap2, 0, 0);

        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    /**
     * Types of drawing
     */
    public enum DrawingType {
        forestDraw,
        mountainDraw,
        riverDraw,
        rubberDraw,
        player1Draw,
        player2Draw,
        mobSkeletonDraw,
        mobWolfDraw,
        mobRandomLvl1Draw,
        mobSpiederDraw,
        mobZombieDraw,
        mobRandomLvl2Draw,
        tresureBox1Draw,
        tresureBox2Draw,
        towerMagicDraw,
        towerWisdomDraw,
        towerDefenceDraw,

        none;
    }

    /**
     * Operations on files.
     */
    private static class FileOperations {
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
    }

    /**
     * Class defines field of Map Editor
     */
    public class FieldOfEditor extends Image implements Serializable {

        public boolean player1StartLocation = false;
        public boolean player2StartLocation = false;
        public boolean mobSkeletonLocation = false;
        public boolean mobWolfLocation = false;
        public boolean mobZombieLocation = false;
        public boolean mobSpiderLocation = false;
        public boolean mobRandomLevel1 = false;
        public boolean mobRandomLevel2 = false;
        public boolean terrainForest = false;
        public boolean terrainMountain = false;
        public boolean terrainRiver = false;
        public boolean terrainGrass = false;
        public boolean tresureBoxLvl1 = false;
        public boolean tresureBoxLvl2 = false;
        public boolean towerMagic = false;
        public boolean towerWisdom = false;
        public boolean towerDefence = false;
        public TypyTerenu typyTerenu;
        public int posX;
        public int posY;

        public FieldOfEditor() {
            super(new Texture("grass100x100.png"));

            addListeners(this);
        }

        public void addListeners(final FieldOfEditor field) {
            this.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (drawingType.equals(DrawingType.forestDraw)) {
                        field.terrainForest = true;
                        field.typyTerenu = TypyTerenu.Drzewo;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mountainDraw)) {
                        field.terrainMountain = true;
                        field.typyTerenu = TypyTerenu.Gory;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.riverDraw)) {
                        field.terrainRiver = true;
                        field.typyTerenu = TypyTerenu.Rzeka;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.rubberDraw)) {
                        field.typyTerenu = TypyTerenu.Trawa;
                        field.terrainForest = false;
                        field.terrainMountain = false;
                        field.terrainRiver = false;
                        field.terrainGrass = true;
                        field.player1StartLocation = false;
                        field.player2StartLocation = false;
                        field.mobRandomLevel1 = false;
                        field.mobSkeletonLocation = false;
                        field.mobWolfLocation = false;
                        field.mobSpiderLocation = false;
                        field.mobZombieLocation = false;
                        field.mobRandomLevel2 = false;
                        field.tresureBoxLvl1 = false;
                        field.tresureBoxLvl2 = false;
                        field.typyTerenu = TypyTerenu.Trawa;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.player1Draw)) {
                        for (int i = 0; i < mapColumns; i++) {
                            for (int j = 0; j < mapRows; j++) {
                                if (fields[i][j].player1StartLocation) {
                                    fields[i][j].player1StartLocation = false;
                                    fillField(fields[i][j]);
                                }
                            }
                        }
                        field.player1StartLocation = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.player2Draw)) {
                        for (int i = 0; i < mapColumns; i++) {
                            for (int j = 0; j < mapRows; j++) {
                                if (fields[i][j].player2StartLocation) {
                                    fields[i][j].player2StartLocation = false;
                                    fillField(fields[i][j]);
                                }
                            }
                        }
                        field.player2StartLocation = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mobSkeletonDraw)) {
                        field.mobSkeletonLocation = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mobWolfDraw)) {
                        field.mobWolfLocation = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mobRandomLvl1Draw)) {
                        field.mobRandomLevel1 = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mobSpiederDraw)) {
                        field.mobSpiderLocation = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mobZombieDraw)) {
                        field.mobZombieLocation = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.mobRandomLvl2Draw)) {
                        field.mobRandomLevel2 = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.tresureBox1Draw)) {
                        field.tresureBoxLvl1 = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.tresureBox2Draw)) {
                        field.tresureBoxLvl2 = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.towerMagicDraw)) {
                        field.towerMagic = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.towerWisdomDraw)) {
                        field.towerWisdom = true;
                        fillField(field);
                    } else if (drawingType.equals(DrawingType.towerDefenceDraw)) {
                        field.towerDefence = true;
                        fillField(field);
                    }

                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }
}

