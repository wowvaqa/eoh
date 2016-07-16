package com.vs.eoh;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.CzesciCiala;
import com.vs.enums.DostepneItemki;
import com.vs.enums.TypItemu;

import java.util.ArrayList;

/**
 * Klasa definiuje Item: Modyfikatory do statystyk oraz wygląd
 *
 * @author v
 */
public class Item extends Actor {
    
    public ArrayList<Effect> dzialania;
    private V v;
    private String nazwa;
    // ikona
    private Sprite sprite;
    // na której części ciała można nośić item
    private CzesciCiala czescCiala;
    // okresla jakiego rodzaju jetem
    private TypItemu typItemu;
    // statystyka itemka
    private int atak = 0;
    private int obrona = 0;
    private int moc = 0;
    private int wiedza = 0;
    private int hp = 0;
    private int szybkosc = 0;
    private int gold = 0;
    private int zasieg = 0;
    private int level = 0;
    private int czasDzialania = 0;
    private int dmg = 0;
    private int armor = 0;
    private String opis;
    private DostepneItemki itemNazwa;

    public Item(Texture teksura, final V v) {
        this.v = v;
        sprite = new Sprite(teksura);
        this.setSize(sprite.getWidth(), sprite.getHeight());  
        final Item qpa = this;

        this.addListener(new ClickListener(){
            GameStatus tmpGs;
            {
                this.tmpGs = v.getGs();
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                v.setLastScreen(v.getG().getScreen());
                tmpGs.setItem(qpa);
                v.getG().setScreen(v.getItemScreen());
            }            
        });
    }



    /***************************************************************************
     * Setters and Getters
     **************************************************************************/
    
    /**
     * zwraca itema
     * @return 
     */
    public DostepneItemki getItemNazwa() {
        return itemNazwa;
    }

    /**
     * Ustala itema
     * @param itemNazwa 
     */
    public void setItemNazwa(DostepneItemki itemNazwa) {
        this.itemNazwa = itemNazwa;
    }


    /**
     * Zwraca działania dostępne dla itemka.
     * @return 
     */
    public ArrayList<Effect> getDzialania() {
        return dzialania;
    }

    /**
     * Ustala działania dostępne dla itemka.
     * @param dzialania 
     */
    public void setDzialania(ArrayList<Effect> dzialania) {
        this.dzialania = dzialania;
    }
    
    /**
     * Zwraca opis dla itema.
     * @return 
     */
    public String getOpis() {
        return opis;
    }

    /**
     * Ustala opis dla itema
     * @param opis 
     */
    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    /**
     * Zwraca czas działania itemka w turach
     * @return 
     */
    public int getCzasDzialania() {
        return czasDzialania;
    }
    
    /**
     * Ustala czas działania dla itemka w turach.
     * @param czasDzialania 
     */
    public void setCzasDzialania(int czasDzialania) {
        this.czasDzialania = czasDzialania;
    }

    /**
     * Zwraca typ Itemu
     * @return 
     */
    public TypItemu getTypItemu() {
        return typItemu;
    }

    /**
     * Ustala typ dla Itemu
     * @param typItemu 
     */
    public void setTypItemu(TypItemu typItemu) {
        this.typItemu = typItemu;
    }
    
    /**
     * Zwracza poziom itemka
     * @return 
     */
    public int getLevel() {
        return level;
    }

    /**
     * Ustala poziom itemka
     * @param level 
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Zwraca nazwę itemka
     * @return 
     */
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getAtak() {
        return atak;
    }

    public void setAtak(int atak) {
        this.atak = atak;
    }

    public int getObrona() {
        return obrona;
    }

    public void setObrona(int obrona) {
        this.obrona = obrona;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSzybkosc() {
        return szybkosc;
    }

    public void setSzybkosc(int szybkosc) {
        this.szybkosc = szybkosc;
    }

    public CzesciCiala getCzescCiala() {
        return czescCiala;
    }

    public void setCzescCiala(CzesciCiala czescCiala) {
        this.czescCiala = czescCiala;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Assets getA() {
        return v.getA();
    }

    public int getZasieg() {
        return zasieg;
    }

    public void setZasieg(int zasieg) {
        this.zasieg = zasieg;
    }    

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getMoc() {
        return moc;
    }

    public void setMoc(int moc) {
        this.moc = moc;
    }

    public int getWiedza() {
        return wiedza;
    }

    public void setWiedza(int wiedza) {
        this.wiedza = wiedza;
    }

    @Override
    public void act(float delta) {
        super.act(delta); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    /**
     * Zwraca poziom obrażeń.
     * @return 
     */
    public int getDmg() {
        return dmg;
    }

    /**
     * Ustala poziom obrażeń.
     * @param dmg 
     */
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    /**
     * Zwraca wartość pancerza.
     * @return 
     */
    public int getArmor() {
        return armor;
    }

    /**
     * Ustala wartość pancerza.
     * @param armor 
     */
    public void setArmor(int armor) {
        this.armor = armor;
    }
    
    
}
