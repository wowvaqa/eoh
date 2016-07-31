package com.vs.mapEditor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.vs.enums.TypyTerenu;
import com.vs.eoh.DefaultActor;
import com.vs.eoh.V;

import java.util.ArrayList;

/**
 * Created by v on 17.07.16.
 * Class responsible for functions of Map Editor and also for creating a stage.
 */
public class MapEdit {

    public boolean painting = false;

    /**
     * Create stage.
     *
     * @param sizeRow  How many rows will have a stage.
     * @param sizeCell How many cells will have a stage.
     * @return Stage
     */
    public Stage createStage(int sizeRow, int sizeCell) {
        Stage stage = new Stage();

        FieldOfEditor[][] fields = new FieldOfEditor[sizeRow][sizeCell];

        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeCell; j++) {
                fields[i][j] = new FieldOfEditor(i * 100, j * 100);
            }
        }

        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeCell; j++) {
                stage.addActor(fields[i][j]);
            }
        }

        return stage;
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
     * Class defines field of Map Editor
     */
    public class FieldOfEditor extends Image {

        public TypyTerenu typyTerenu;

        public FieldOfEditor(int x, int y) {
            super.setPosition(x, y);
        }
    }
}

