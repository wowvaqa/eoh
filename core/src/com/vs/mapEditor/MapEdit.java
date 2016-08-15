package com.vs.mapEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.V;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by v on 17.07.16.
 * Class responsible for functions of Map Editor and also for creating a stage.
 */
public class MapEdit {

    public boolean painting = false;

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

        //TextureAtlas tAtals = new TextureAtlas(Gdx.files.internal("terrain/test.atlas"));

        mapColumns = sizeColumns;
        mapRows = sizeRow;

        fields = new FieldOfEditor[sizeRow][sizeColumns];

        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeColumns; j++) {
                fields[i][j] = new FieldOfEditor();
                fields[i][j].typyTerenu = TypyTerenu.Trawa;
                fields[i][j].setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("grass100x100.png"))));
                //fields[i][j].setDrawable(new TextureRegionDrawable(new TextureRegion(tAtals.findRegion("forestC"))));
                fields[i][j].setPosition(i * 100, j * 100);
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
     * Saving map to file.
     */
    public void saveMap() throws IOException {

        StringWriter writer = new StringWriter();
        XmlWriter xml = new XmlWriter(writer);


        try {
            xml.element("meow")
                    .attribute("moo", "cow")
                    .element("child")
                    .attribute("moo", "cow")
                    .element("child")
                    .attribute("moo", "cow")
                    .text("XML is like violence. If it doesn't solve your problem, you're not using enough of it.")
                    .pop()
                    .pop()
                    .pop();

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileHandle file = Gdx.files.local("myFile.xml");

        System.out.println(writer);

    }

    /**
     * Redraw a stage.
     *
     * @param stage object.
     */
    public void redrawStage(Stage stage) {

    }

    /**
     * Getting status of map painting.
     *
     * @return True then map painting is enebled else painting is disabled.
     */
    public boolean isPainting() {
        return painting;
    }

    /**
     * Setting status of map painting.
     *
     * @param painting Boolean varible. If True then painting of map is enebled, else painting is
     *                 disabled.
     */
    public void setPainting(boolean painting) {
        this.painting = painting;
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
     * Class defines field of Map Editor
     */
    public class FieldOfEditor extends Image {

        public boolean player1StartLocation = false;
        public boolean player2StartLocation = false;
        public boolean terrainForest = false;
        public boolean terrainGrass = false;
        public TypyTerenu typyTerenu;
        FieldOfEditor field = this;

        public FieldOfEditor() {
            super(new Texture("grass100x100.png"));

            addListeners();
        }

        private void addListeners() {
            this.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (drawingType.equals(DrawingType.forestDraw)) {
                        field.terrainForest = true;
                        field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("interface/mapEditor/terrains/forestC.png"))));
                    } else if (drawingType.equals(DrawingType.rubberDraw)) {
                        field.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("grass100x100.png"))));
                        field.terrainForest = false;
                        field.terrainGrass = true;
                        field.player1StartLocation = false;
                        field.player2StartLocation = false;
                    } else if (drawingType.equals(DrawingType.player1Draw)) {

                        for (int i = 0; i < mapColumns; i++) {
                            for (int j = 0; j < mapRows; j++) {
                                if (fields[i][j].player1StartLocation) {
                                    fields[i][j].player1StartLocation = false;
                                    fields[i][j].setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("grass100x100.png"))));
                                }
                            }
                        }
                        field.player1StartLocation = true;
                        field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/players/player1.png"));
                    } else if (drawingType.equals(DrawingType.player2Draw)) {
                        for (int i = 0; i < mapColumns; i++) {
                            for (int j = 0; j < mapRows; j++) {
                                if (fields[i][j].player2StartLocation) {
                                    fields[i][j].player2StartLocation = false;
                                    fields[i][j].setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("grass100x100.png"))));
                                }
                            }
                        }
                        field.player2StartLocation = true;
                        field.setDrawable(mergeDrawable("grass100x100.png", "interface/mapEditor/players/player2.png"));
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }
}

