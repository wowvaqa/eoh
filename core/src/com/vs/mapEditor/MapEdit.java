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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
    public Stage createStage(int sizeRow, int sizeColumns, Viewport viewport) {
        Stage stage = new Stage(viewport);

        mapColumns = sizeColumns;
        mapRows = sizeRow;

        fields = new FieldOfEditor[sizeRow][sizeColumns];

        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeColumns; j++) {
                fields[i][j] = new FieldOfEditor();
                fields[i][j].typyTerenu = TypyTerenu.Trawa;
                fields[i][j].terrainGrass = true;
                fields[i][j].posX = i;
                fields[i][j].posY = j;
                fillField(fields[i][j]);
            }
        }

        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeColumns; j++) {
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
            field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("grass100x100.png"))));
            field.setPosition(field.posX * 100, field.posY * 100);
        } else if (field.typyTerenu.equals(TypyTerenu.Drzewo)) {
            field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("interface/mapEditor/terrains/forestC.png"))));
            field.setPosition(field.posX * 100, field.posY * 100);
        }

        if (field.player1StartLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/players/player1.png"));
        } else if (field.player2StartLocation) {
            field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/players/player2.png"));
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
        rubberDraw,
        player1Draw,
        player2Draw,
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
        public boolean terrainForest = false;
        public boolean terrainGrass = false;
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
                    } else if (drawingType.equals(DrawingType.rubberDraw)) {
                        field.terrainForest = false;
                        field.terrainGrass = true;
                        field.player1StartLocation = false;
                        field.player2StartLocation = false;
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
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }
}

