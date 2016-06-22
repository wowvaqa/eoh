package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.CzesciCiala;
import com.vs.network.Network;

import java.util.ArrayList;

/**
 * Created by v on 2016-06-22.
 */
public class Dialog {

    /**
     * Zwraca okno z zawartością skrzyni ze skarbem
     *
     * @param text       Tekst
     * @param a          Referencja do obiektu Asseta
     * @param tresureBox Skrzynia ze skarbem której zawartość ma być wyświetlona
     * @param gs         Obiekt Game status
     * @param bohater    Bohater który wszedł na skrzynie
     * @return Obiekt klasy Window
     */
    public static Window getTresureBoxWindow(String text, final Assets a, final TresureBox tresureBox, final GameStatus gs, final Bohater bohater) {

        Assets tmpA = a;
        TresureBox tmpTB = tresureBox;
        GameStatus tmpGS = gs;
        Bohater tmpBohater = bohater;

        final Window window = new Window(text, a.skin);
        window.setPosition(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 150);
        window.setSize(400, 300);
        window.setMovable(false);

        refrestTresureBoxWindow(window, tmpA, tmpTB, tmpGS, tmpBohater);

        return window;
    }

    /**
     * Odświeża zawartość okna skrzyni ze skarbem.
     *
     * @param window
     * @param a
     * @param tresureBox
     * @param gs
     * @param bohater
     */
    private static void refrestTresureBoxWindow(final Window window, final Assets a, final TresureBox tresureBox, final GameStatus gs, final Bohater bohater) {
        window.clear();

        final Window tmpWindow = window;
        final TresureBox tmpTB = tresureBox;
        final GameStatus tmpGS = gs;
        final Bohater tmpBohater = bohater;

        // Tymczasowa ArrayLista przechowująca TextButtony
        final ArrayList<TextButton> tmpButtons = new ArrayList<TextButton>();

        for (int i = 0; i < tresureBox.getDostepneItemy().size(); i++) {
            window.add(tresureBox.getDostepneItemy().get(i).getNazwa());
            window.add(new Image(tresureBox.getDostepneItemy().get(i).getSprite().getTexture())).size(50, 50).pad(2);
            tmpButtons.add(new TextButton("TAKE IT", a.skin));
            tmpButtons.get(i).addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    for (int i = 0; i < tmpButtons.size(); i++) {
                        if (tmpButtons.get(i).isPressed()) {
                            // Sprawdzenie czy itemek jest złotem
                            if (tmpTB.getDostepneItemy().get(i).getCzescCiala().equals(CzesciCiala.gold)) {
                                tmpGS.dodajDoZlotaAktualnegoGracza(tmpTB.getDostepneItemy().get(i).getGold());
                                tmpTB.getDostepneItemy().remove(i);
                                tmpButtons.get(i).remove();
                                refrestTresureBoxWindow(window, a, tresureBox, gs, bohater);
                                // Jeżeli nie jest złotem
                            } else {
                                tmpButtons.get(i).remove();
                                // dodanie itemka z tresureboxa do ekwipunku
                                tmpBohater.getEquipment().add(tmpTB.getDostepneItemy().get(i));
                                // usuniecie wybranego itemka z trasureboxa
                                tmpTB.getDostepneItemy().remove(i);
                                refrestTresureBoxWindow(window, a, tresureBox, gs, bohater);
                            }
                        }
                    }
                    return false;
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (int i = 0; i < tmpButtons.size(); i++) {
                        if (tmpButtons.get(i).isPressed()) {
                            // Sprawdzenie czy itemek jest złotem
                            if (tmpTB.getDostepneItemy().get(i).getCzescCiala().equals(CzesciCiala.gold)) {
                                tmpGS.dodajDoZlotaAktualnegoGracza(tmpTB.getDostepneItemy().get(i).getGold());
                                tmpTB.getDostepneItemy().remove(i);
                                tmpButtons.get(i).remove();
                                refrestTresureBoxWindow(window, a, tresureBox, gs, bohater);
                                // Jeżeli nie jest złotem
                            } else {
                                tmpButtons.get(i).remove();
                                // dodanie itemka z tresureboxa do ekwipunku
                                tmpBohater.getEquipment().add(tmpTB.getDostepneItemy().get(i));
                                // usuniecie wybranego itemka z trasureboxa
                                tmpTB.getDostepneItemy().remove(i);

                                refrestTresureBoxWindow(window, a, tresureBox, gs, bohater);
                                // aktualizacja okna
                            }
                        }
                    }
                }
            });
            window.add(tmpButtons.get(i));
            window.row();
        }

        // tymczasowy przycisk Exit dodany do okna InfoWindow
        TextButton tmpExitBtn = new TextButton("EXIT", a.skin);
        tmpExitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                TresureBox.removeTresureBox(tmpTB, tmpGS.getMapa());

                if (tmpGS.getNetworkStatus() == 2) {
                    Network.RemoveTresureBox removeTresureBox = new Network.RemoveTresureBox();
                    removeTresureBox.pozX = tmpTB.getPozX();
                    removeTresureBox.pozY = tmpTB.getPozY();
                    GameStatus.client.getCnt().sendTCP(removeTresureBox);
                }

                tmpBohater.setOtwartaSkrzyniaZeSkarbem(false);
                tmpWindow.remove();
            }
        });
        tmpWindow.add(tmpExitBtn);
    }

}
