package com.vs.eoh;

import com.vs.enums.DostepneItemki;

/**
 * Klasa opisuje działanie itema.
 *
 * @author v
 */
public class Effect {
    
    private int dlugoscTrwaniaEfektu = 0;

    private int efektAtak = 0;
    private int efektObrona = 0;
    private int efektSzybkosc = 0;
    private int efektHp = 0;
    private int efektMana = 0;
    private int efektDmg = 0;
    private int efektArmor = 0;

    private int zmianaHp;
    private String opis;

    private DefaultActor ikona;

    /**
     * Zmniejsza czas trwania efektu o zadaną ilość tur.
     *
     * @param tury Ilość tur o ile ma być skócony efekt działania itema
     */
    public void zmniejszCzasTrwaniaDzialania(int tury) {

    }

    /**
     * Wykonuje w zależności od itema odpowiedni efekt na bohaterze.
     *
     * @param dt Item z enum Dostępne Itemy
     * @param bohater Referencja do obiektu Bohatera
     */
    public void dzialanie(DostepneItemki dt, Bohater bohater) {
        switch (dt) {
            case PotionZdrowie:
                //System.out.println("Leczenie bohatera na +5");
                bohater.setActualHp(bohater.getActualHp() + 5);
                if (bohater.getActualHp() > bohater.getHp()) {
                    bohater.setActualHp(bohater.getHp());
                }
                bohater.aktualizujTeksture();
                break;
            case PotionSzybkosc:
                //System.out.println("Dodaje +2 do szybkości");
                bohater.setPozostaloRuchow(bohater.getPozostaloRuchow() + 2);
                if (bohater.getPozostaloRuchow() > bohater.getSzybkosc()) {
                    bohater.setPozostaloRuchow(bohater.getSzybkosc());
                }
                break;
            case PotionAttack:
                this.efektAtak += 2;
                this.dlugoscTrwaniaEfektu += 2;
                bohater.getEfekty().add(this);
                break;
            case PotionDefence:
                this.efektObrona += 2;
                this.dlugoscTrwaniaEfektu += 2;
                bohater.getEfekty().add(this);
                break;
        }
    }

    /**
     * *************************************************************************
     * Setters and Getters
     * ************************************************************************
     */
    /**
     * Zwraca wartośc zmianyHp
     *
     * @return
     */
    public int getZmianaHp() {
        return zmianaHp;
    }

    /**
     * Zwraca opis zmiany dla danego itema.
     *
     * @return String z opisem
     */
    public String getOpis() {
        return opis;
    }

    /**
     * Zwraca długość trwania efektu.
     *
     * @return
     */
    public int getDlugoscTrwaniaEfektu() {
        return dlugoscTrwaniaEfektu;
    }

    /**
     * Ustala długość trwania efektu (w turach).
     *
     * @param dlugoscTrwaniaEfektu Jak długo (w turach) ma trwać efekt.
     */
    public void setDlugoscTrwaniaEfektu(int dlugoscTrwaniaEfektu) {
        this.dlugoscTrwaniaEfektu = dlugoscTrwaniaEfektu;
    }

    /**
     * Zwraca tymczasowy efekt modyfikujący współczynnik ataku.
     *
     * @return
     */
    public int getEfektAtak() {
        return efektAtak;
    }

    /**
     * Ustala tymczasowy efekt modyfikujący współczynnik ataku.
     *
     * @param efektAtak
     */
    public void setEfektAtak(int efektAtak) {
        this.efektAtak = efektAtak;
    }

    /**
     * Zwraca tymczasowy efekt modyfikujący współczynnik obrony.
     *
     * @return
     */
    public int getEfektObrona() {
        return efektObrona;
    }

    /**
     * Ustala tymczasowy efekt modyfikujący współczynnik obrony.
     *
     * @param efektObrona
     */
    public void setEfektObrona(int efektObrona) {
        this.efektObrona = efektObrona;
    }

    /**
     * Zwraca tymczasowy efekt modyfikujący współczynnik szybkości.
     *
     * @return
     */
    public int getEfektSzybkosc() {
        return efektSzybkosc;
    }

    /**
     * Ustala tymczasowy efekt modyfikujący współczynnik szybkości.
     *
     * @param efektSzybkosc
     */
    public void setEfektSzybkosc(int efektSzybkosc) {
        this.efektSzybkosc = efektSzybkosc;
    }

    /**
     * Zwraca tymczasowy efekt modyfikujący współczynnik Hp
     *
     * @return
     */
    public int getEfektHp() {
        return efektHp;
    }

    /**
     * Ustala tymczasowy efekt modyfikujący współczynnik Hp
     *
     * @param efektHp
     */
    public void setEfektHp(int efektHp) {
        this.efektHp = efektHp;
    }

    /**
     * Zwraca tymczasowy efekt modyfikujący współczynnik Mana
     *
     * @return
     */
    public int getEfektMana() {
        return efektMana;
    }

    /**
     * Ustala tymczasowy efekt modyfikujący współczynnik Mana
     *
     * @param efektMana
     */
    public void setEfektMana(int efektMana) {
        this.efektMana = efektMana;
    }

    /**
     * Zwraca Ikone
     *
     * @return
     */
    public DefaultActor getIkona() {
        return ikona;
    }

    /**
     * Ustala ikone.
     *
     * @param ikona
     */
    public void setIkona(DefaultActor ikona) {
        this.ikona = ikona;
    }

    /**
     * Zwraca tymczasowy współczynnik modyfikujący obrażenia.
     * @return 
     */
    public int getEfektDmg() {
        return efektDmg;
    }

    /**
     * Ustala tymczaswoy współczynnik modyfikujący obrażenia.
     * @param efektDmg 
     */
    public void setEfektDmg(int efektDmg) {
        this.efektDmg = efektDmg;
    }

    /**
     * Zwraca współczynnik pancerza dla efektu.
     * @return 
     */
    public int getEfektArmor() {
        return efektArmor;
    }

    /**
     * Ustala współczynnik pancerza dla efektu.
     * @param efektArmor 
     */
    public void setEfektArmor(int efektArmor) {
        this.efektArmor = efektArmor;
    }
    
    
}
