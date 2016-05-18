package com.vs.eoh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;

public class Gracz {

    //public ArrayList<Player> bohaterowieOld = new ArrayList<Player>();
    
    private ArrayList<Bohater> bohaterowie = new ArrayList<Bohater>();

    private int gold;                                                           // złoto zgromadzone przez gracza
    
    private int numerGracza;
    
    // Ile tur gracz przebywa bez zamku.
    private int turyBezZamku = 0;
    
    // Informuje czy gracz jest posiadaczem zamku.
    private boolean statusBezZamku = false;
    
    // informuje czy gracz zakończył grę
    private boolean statusGameOver = false;
    
    // kolor gracza
    private Color color;
    
    // Kolora ikona gracza wyświetlana w na Mapie. Informująca który gracz ma turę
    private Texture teksturaIkonyGracza;

    // określa czy gracz jest pod kontrolą sztucznej inetligencji.
    private boolean ai = false;

    /**
     * Współżedne X,Y ostatnio zaznaczonego bohatra.
     */
    private float cameraPositionX;
    private float cameraPositionY;

    /**
     * 
     * @param numerGracza 
     */
    public Gracz(int numerGracza) {
        this.numerGracza = numerGracza;
        Pixmap pM = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        ustalKolor();
        pM.setColor(color);
        pM.fillRectangle(0, 0, 20, 20);
        teksturaIkonyGracza = new Texture(pM);

    }
    
    /** 
     * Ustala kolor gracza
     */
    private void ustalKolor(){
        switch (numerGracza){
                case 0:
                    color = Color.RED;
                    break;
                case 1:
                    color = Color.BLUE;
                    break;
                case 2:
                    color = Color.YELLOW;
                    break;
                case 3:
                    color = Color.GREEN;
                    break;
            }            
    }

    //SETTERS AND GETTERS

    public Texture getTeksturaIkonyGracza() {
        return teksturaIkonyGracza;
    }

    public void setTeksturaIkonyGracza(Texture teksturaIkonyGracza) {
        this.teksturaIkonyGracza = teksturaIkonyGracza;
    }

    /**
     * Zwraca numer gracza
     * @return 
     */
    public int getNumerGracza() {
        return numerGracza;
    }

    /**
     * Ustala numer Gracza
     * @param numerGracza 
     */
    public void setNumerGracza(int numerGracza) {
        this.numerGracza = numerGracza;
    }

    /**
     * Pobiera kolor gracza
     * @return 
     */
    public Color getColor() {
        return color;
    }

    /**
     * Ustala kolor dla gracza
     * @param color 
     */
    public void setColor(Color color) {
        this.color = color;
    }    
    
    /**
     * Zwraca ilość złota posiadanego przez gracza
     * @return złoto
     */
    public int getGold() {
        return gold;
    }

    /**
     * Ustala ilość złota należącego do gracza.
     * @param gold 
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Zwraca ArrayList<> z Bohaterami należącymi do gracza.
     * @return ArrayList<> z bohaterami
     */
    public ArrayList<Bohater> getBohaterowie() {
        return bohaterowie;
    }

    /**
     * Pobiera ArrayList<> z bohaterami gracza
     * @param bohaterowie 
     */
    public void setBohaterowie(ArrayList<Bohater> bohaterowie) {
        this.bohaterowie = bohaterowie;
    }

    /**
     * Zwraca ile tur bez zamku ma gracz
     * @return ilość tur bez zamku
     */
    public int getTuryBezZamku() {
        return turyBezZamku;
    }

    /**
     * Ustala ilość tur bez zamku gracza
     * @param turyBezZamku 
     */
    public void setTuryBezZamku(int turyBezZamku) {
        this.turyBezZamku = turyBezZamku;
    }

    /**
     * Zwraca True jeżeli gracz zakończył grę
     * Zwraca False jeżeli gracz nie zakończył gry.
     * @return 
     */
    public boolean isStatusGameOver() {
        return statusGameOver;
    }

    /**
     * Ustala czy gracz uczestniczy w grze(False) lub nie (TRUE)
     * @param statusGameOver 
     */
    public void setStatusGameOver(boolean statusGameOver) {
        this.statusGameOver = statusGameOver;
    }

    /**
     * Zwraca TRUE jeżeli gracz nie posiada zadnego zamku, FALSE jeżeli 
     * posiada
     * @return 
     */
    public boolean isStatusBezZamku() {
        return statusBezZamku;
    }

    /**
     * Ustala czy gracz jest posiadaczem zamku.
     * @param statusBezZamku 
     */
    public void setStatusBezZamku(boolean statusBezZamku) {
        this.statusBezZamku = statusBezZamku;
    }

    /**
     * Sprawdza czy tracz jest pod kontrolą sztucznej inteligencji
     *
     * @return TRUE jeżeli tak
     */
    public boolean isAi() {
        return ai;
    }

    /**
     * Określa czy gracz będzie pod kontrolą AI
     *
     * @param ai TRUE - tak, FALSE - nie
     */
    public void setAi(boolean ai) {
        this.ai = ai;
    }

    /**
     * Zwraca pozycję X kamery dla ostatnio zaznaczonego bohatera
     *
     * @return współżędna X
     */
    public float getCameraPositionX() {
        return cameraPositionX;
    }

    /**
     * Ustala pozycję X kamery dla ostatnio zaznaczonego bohatera
     *
     * @param cameraPositionX Współżędna Y pozycji kamery dla ostatnio zaznaczonego bohatra.
     */
    public void setCameraPositionX(float cameraPositionX) {
        this.cameraPositionX = cameraPositionX;
    }

    /**
     * Zwraca pozycję Y kamery dla ostatnio zaznaczonego bohatera
     *
     * @return współżędna Y
     */
    public float getCameraPositionY() {
        return cameraPositionY;
    }

    /**
     * Ustala pozycję X kamery dla ostatnio zaznaczonego bohatera
     *
     * @param cameraPositionY Współżędna Y pozycji kamery dla ostatnio zaznaczonego bohatra.
     */
    public void setCameraPositionY(float cameraPositionY) {
        this.cameraPositionY = cameraPositionY;
    }
}
