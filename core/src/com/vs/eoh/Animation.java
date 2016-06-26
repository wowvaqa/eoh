package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by v on 2016-06-22.
 */
public class Animation {

    /**
     * Metoda wyświetla na Screnie Mapy etykietę z zadanym parametrem
     *
     * @param pozX Pozycja X etykiety
     * @param pozY Pozycja Y etykiety
     * @param text Tekst etykiety
     * @param a    Referencja do obiektu Assets
     */
    public static void animujLblDamage(float pozX, float pozY, String text, Assets a) {

        Label label = new Label("", a.skin);

        Assets.stage01MapScreen.addActor(label);

        label.setText("" + text);
        label.setPosition(pozX - 50, pozY - 25);
        label.setFontScale(1.5f);
        label.addAction(Actions.alpha(1));
        label.addAction(Actions.fadeOut(2.0f));
        label.addAction(Actions.moveBy(0, 175, 2.0f));
        label.act(Gdx.graphics.getDeltaTime());
    }


    public static void animujLblLevelUp(float pozX, float pozY) {

        Label label = new Label("", GameStatus.a.skin);

        Assets.stage01MapScreen.addActor(label);

        label.setText("Level UP");
        label.setPosition(pozX, pozY);
        label.setFontScale(1.8f);
        label.addAction(Actions.alpha(1));
        label.addAction(Actions.fadeOut(3.0f));
        label.addAction(Actions.moveBy(0, 125, 3.0f));
        label.act(Gdx.graphics.getDeltaTime());
    }
}
