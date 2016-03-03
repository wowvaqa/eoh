package com.vs.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Klasa wyświetla odpowiednie dialogi
 *
 * @author v
 */
public class DialogScreen extends Dialog {

    /**
     * Dialog wyświetlający standardowy tekst błędu zakończony buttonem OK.
     *
     * @param title
     * @param skin
     * @param errorText Tekst błędu.
     * @param stage
     */
    public DialogScreen(String title, Skin skin, String errorText, Stage stage) {
        super(title, skin);

        this.setMovable(false);        
        
        text(errorText);
        button("OK", "ok");

        this.show(stage);
    }

    @Override
    protected void result(Object object) {
        this.remove();
    }
}
