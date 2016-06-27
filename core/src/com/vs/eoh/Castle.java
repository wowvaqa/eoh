package com.vs.eoh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Klasa odpowiedzialna za zachowanie zamku.
 *
 * @author v
 */
public class Castle extends Actor {

    private final Sprite sprite;
    private V v;

    private int maxHp = 5;
    private int actualHp = 5;
    private int Obrona = 5;
    private int przynaleznoscDoGracza = 0;
    private Pixmap ikonaZamku;

    private int locXonMap;
    private int locYonMap;

    /**
     *
     * @param x
     * @param y
     * @param wlascicielZamku
     */
    public Castle(V v, int x, int y, int wlascicielZamku) {
        this.v = v;

        this.przynaleznoscDoGracza = wlascicielZamku;
        this.sprite = new Sprite(v.getA().trawaZamekTex);
        this.setSize(sprite.getWidth(), sprite.getHeight());
        this.setPosition(x, y);

        this.dodajListnera();

        this.ikonaZamku = new Pixmap(20, 20, Pixmap.Format.RGBA8888);

        rysujIkoneZamku();
    }

    // Rysuje ikonę zamku
    private void rysujIkoneZamku() {

        if (!this.sprite.getTexture().getTextureData().isPrepared()) {
            this.sprite.getTexture().getTextureData().prepare();
        }

        this.ikonaZamku = this.sprite.getTexture().getTextureData().consumePixmap();
        // Narysowanie tyczek od chorągwii
        ikonaZamku.setColor(Color.BLACK);
        ikonaZamku.fillRectangle(32, 0, 2, 15);
        ikonaZamku.fillRectangle(68, 0, 2, 15);

        // Rysowanie poszczególnych chorągwii
        switch (this.przynaleznoscDoGracza) {
            case 0:
                ikonaZamku.setColor(Color.RED);
                break;
            case 1:
                ikonaZamku.setColor(Color.BLUE);
                break;
            case 2:
                ikonaZamku.setColor(Color.YELLOW);
                break;
            case 3:
                ikonaZamku.setColor(Color.GREEN);
                break;
        }
        ikonaZamku.fillTriangle(34, 0, 34, 10, 44, 5);
        ikonaZamku.fillTriangle(70, 0, 70, 10, 80, 5);

        this.sprite.setTexture(new Texture(ikonaZamku));
    }
    
    // Metoda aktualizuje ikonę zamku.
    public void aktualizujIkoneZamku(){
        this.rysujIkoneZamku();
    }

    // Dodaje ClickListnera do obiektu Zamku
    private void dodajListnera() {
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Zamek został kliknięty");
                new Dialog("Zamek", v.getA().skin) {
                    {
                        text("Wlasciciel: " + przynaleznoscDoGracza);
                        this.row();
                        text("Obrona: " + Obrona);
                        this.row();
                        text("HP: " + actualHp);
                        this.row();
                        button("Zakoncz", "zakoncz");
                    }

                    @Override
                    protected void result(Object object) {
                        if (object.equals("zakoncz")) {
                            this.remove();
                        }
                    }
                }.show(Assets.stage01MapScreen);
            }
        });
    }

    /**
     * Zwraca obiekt klasy Pole na którym znajduje się zamek.
     *
     * @return Obiekt klasy Pole
     */
    public Pole getField() {
        return v.getGs().getMapa().getPola()[locXonMap][locYonMap];
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    //Setters and Getters
    /**
     *
     * @return
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     *
     * @param maxHp
     */
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     *
     * @return
     */
    public int getActualHp() {
        return actualHp;
    }

    /**
     *
     * @param actualHp
     */
    public void setActualHp(int actualHp) {
        this.actualHp = actualHp;
    }

    /**
     *
     * @return
     */
    public int getObrona() {
        return Obrona;
    }

    /**
     *
     * @param Obrona
     */
    public void setObrona(int Obrona) {
        this.Obrona = Obrona;
    }

    /**
     *
     * @return
     */
    public int getPrzynaleznoscDoGracza() {
        return przynaleznoscDoGracza;
    }

    /**
     *
     * @param przynaleznoscDoGracza
     */
    public void setPrzynaleznoscDoGracza(int przynaleznoscDoGracza) {
        this.przynaleznoscDoGracza = przynaleznoscDoGracza;
    }

    /**
     * Zwraca lokacje X zamku na mapie
     *
     * @return
     */
    public int getLocXonMap() {
        return locXonMap;
    }

    /**
     * Ustala lokacje X zamku na mapie
     *
     * @param locXonMap
     */
    public void setLocXonMap(int locXonMap) {
        this.locXonMap = locXonMap;
    }

    /**
     * Zwraca lokacje Y zamku na mapie
     *
     * @return
     */
    public int getLocYonMap() {
        return locYonMap;
    }

    /**
     * Ustala lokacje Y zamku na mapie.
     *
     * @param locYonMap
     */
    public void setLocYonMap(int locYonMap) {
        this.locYonMap = locYonMap;
    }
}
