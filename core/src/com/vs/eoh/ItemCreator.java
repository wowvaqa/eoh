package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.screens.DialogScreen;

import com.vs.enums.DostepneItemki;
import com.vs.enums.CzesciCiala;
import com.vs.enums.TypItemu;

import java.util.ArrayList;

/**
 * Zwraca item
 *
 * @author v
 */
public class ItemCreator {

    private V v;

    public ItemCreator(V v) {
        this.v = v;
    }

    public Item utworzItem(DostepneItemki dostepneItemki, final V v) {
        this.v = v;
        Item item = new Item(v.getA().texFist, v);

        switch (dostepneItemki) {
// GŁOWA ======================================================================         
            case Glowa:
                item.setNazwa("Gola Glowa");
                item.setLevel(0);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.glowa);
                item.getSprite().setTexture(v.getA().texHead);
                item.setTypItemu(TypItemu.Pancerz);
                break;
            case LnianaCzapka:
                item.setNazwa("Lniana Czapka");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(1);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(1);
                item.setCzescCiala(CzesciCiala.glowa);
                item.getSprite().setTexture(v.getA().texLinenCap);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.LnianaCzapka);
                break;
            case SkorzanaCzapka:
                item.setNazwa("Skorzana czapka");
                item.setLevel(2);
                item.setAtak(0);
                item.setObrona(2);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(2);
                item.setCzescCiala(CzesciCiala.glowa);
                item.getSprite().setTexture(v.getA().texLeatherCap);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.SkorzanaCzapka);
                break;
// KORPUS ======================================================================                
            case LnianaKoszula:
                item.setNazwa("Lniana Koszula");
                item.setLevel(0);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(0);
                item.setCzescCiala(CzesciCiala.korpus);
                item.getSprite().setTexture(v.getA().texLinenShirt);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.LnianaKoszula);
                break;
            case SkorzanyNapiersnik:
                item.setNazwa("Skorzany Napiersnik");
                item.setLevel(2);
                item.setAtak(0);
                item.setObrona(1);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(2);
                item.setCzescCiala(CzesciCiala.korpus);
                item.getSprite().setTexture(v.getA().texLeatherShirt);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.SkorzanyNapiersnik);
                break;
// BROŃ RĘCE ===================================================================
            case Piesci:
                item.setNazwa("Gole Piesci");
                item.setLevel(0);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texFist);
                item.setTypItemu(TypItemu.Bron);
                break;
            case Kij:
                item.setNazwa("Kij");
                item.setLevel(1);
                item.setAtak(1);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setDmg(1);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texStick);
                item.setTypItemu(TypItemu.Bron);
                item.setItemNazwa(DostepneItemki.Kij);
                break;
            case Miecz:
                item.setNazwa("Miecz");
                item.setLevel(2);
                item.setAtak(2);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setDmg(2);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texSword);
                item.setTypItemu(TypItemu.Bron);
                item.setItemNazwa(DostepneItemki.Miecz);
                break;
            case Tarcza:
                item.setNazwa("Tarcza");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(1);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(1);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texShield);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.Tarcza);
                break;
            case Luk:
                item.setNazwa("Luk");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setDmg(1);
                item.setZasieg(1);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texBow);
                item.setTypItemu(TypItemu.Bron);
                item.setItemNazwa(DostepneItemki.Luk);
                break;
            case DlugiLuk:
                item.setNazwa("Dlugi Luk");
                item.setLevel(2);
                item.setAtak(1);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setDmg(1);
                item.setZasieg(1);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texLongBow);
                item.setTypItemu(TypItemu.Bron);
                item.setItemNazwa(DostepneItemki.DlugiLuk);
                break;
            case Laska:
                item.setNazwa("Laska");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setMoc(1);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setDmg(0);
                item.setZasieg(0);
                item.setCzescCiala(CzesciCiala.rece);
                item.getSprite().setTexture(v.getA().texStaff);
                item.setTypItemu(TypItemu.Bron);
                item.setItemNazwa(DostepneItemki.Laska);
                break;
            case MagicznyKaptur:
                item.setNazwa("Kaptur Wiedzy +1");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setMoc(0);
                item.setWiedza(1);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setDmg(0);
                item.setZasieg(0);
                item.setCzescCiala(CzesciCiala.glowa);
                item.getSprite().setTexture(v.getA().texMagicHood);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.MagicznyKaptur);
                break;
// NOGI ========================================================================
            case Nogi:
                item.setNazwa("Gole Nogi");
                item.setLevel(0);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.nogi);
                item.getSprite().setTexture(v.getA().texLegs);
                item.setTypItemu(TypItemu.Pancerz);
                break;
            case LnianeSpodnie:
                item.setNazwa("Lniane Spodnie");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(1);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(1);
                item.setCzescCiala(CzesciCiala.nogi);
                item.getSprite().setTexture(v.getA().texLinenTousers);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.LnianeSpodnie);
                break;
            case SkorzaneSpodnie:
                item.setNazwa("Skorzane Spodnie");
                item.setLevel(2);
                item.setAtak(0);
                item.setObrona(2);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setArmor(2);
                item.setCzescCiala(CzesciCiala.nogi);
                item.getSprite().setTexture(v.getA().texLeatherTousers);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.SkorzaneSpodnie);
                break;
// OBUWIE ======================================================================
            case LnianeButy:
                item.setNazwa("Lniane Obuwie");
                item.setLevel(0);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.stopy);
                item.getSprite().setTexture(v.getA().texLinenShoes);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.LnianeButy);
                break;
            case SkorzaneButy:
                item.setNazwa("Skorzane Buty");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(1);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.stopy);
                item.getSprite().setTexture(v.getA().texLeatherShoes);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.SkorzaneButy);
                break;
            case WzmocnioneSkorzaneButy:
                item.setNazwa("Wzmocnione Skorzane Buty");
                item.setLevel(2);
                item.setAtak(0);
                item.setObrona(1);
                item.setSzybkosc(1);
                item.setHp(0);
                item.setArmor(1);
                item.setCzescCiala(CzesciCiala.stopy);
                item.getSprite().setTexture(v.getA().texHardLeatherShoes);
                item.setTypItemu(TypItemu.Pancerz);
                item.setItemNazwa(DostepneItemki.WzmocnioneSkorzaneButy);
                break;
// INNE ========================================================================     
            case Gold:
                item.setNazwa("Zloto");
                item.setLevel(99);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.gold);
                item.getSprite().setTexture(v.getA().texGold);
                item.setTypItemu(TypItemu.Other);
                item.setGold(10);
                break;
// MIKSTURY ====================================================================
            case PotionZdrowie:
                item.setNazwa("Potion + 5");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.other);
                item.getSprite().setTexture(v.getA().texHelthPotion);
                item.setTypItemu(TypItemu.Mikstura);
                item.setOpis("Mikstura leczaca +5 HP.");
                item.setItemNazwa(DostepneItemki.PotionZdrowie);
                item.dzialania = new ArrayList<Effect>();
                item.dzialania.add(new Effect());
                break;
            case PotionSzybkosc:
                item.setNazwa("Szybkosc + 2");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.other);
                item.getSprite().setTexture(v.getA().texSpeedPotion);
                item.setTypItemu(TypItemu.Mikstura);
                item.setOpis("Mikstura odnawia 2 punkty akcji.");
                item.setItemNazwa(DostepneItemki.PotionSzybkosc);
                item.dzialania = new ArrayList<Effect>();
                item.dzialania.add(new Effect());
                break;
            case PotionAttack:
                item.setNazwa("Atak + 2 przez 2 tury");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.other);
                item.getSprite().setTexture(v.getA().texAttackPotion);
                item.setTypItemu(TypItemu.Mikstura);
                item.setOpis("Mikstura dodaje +2 do ataku na 2 tury.");
                item.setItemNazwa(DostepneItemki.PotionAttack);
                item.dzialania = new ArrayList<Effect>();
                item.dzialania.add(new Effect());
                item.dzialania.get(0).setIkona(new EffectActor(v.getA().texAttackPotion, 0, 0));
                item.dzialania.get(0).getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Gniew", v.getA().skin, "Zwiększenie ataku +1 do końca tury", Assets.stage01MapScreen);
                    }
                });
                break;
            case PotionDefence:
                item.setNazwa("Obrona + 2 przez 2 tury");
                item.setLevel(1);
                item.setAtak(0);
                item.setObrona(0);
                item.setSzybkosc(0);
                item.setHp(0);
                item.setCzescCiala(CzesciCiala.other);
                item.getSprite().setTexture(v.getA().texDefencePotion);
                item.setTypItemu(TypItemu.Mikstura);
                item.setOpis("Mikstura dodaje +2 do obrony na 2 tury.");
                item.setItemNazwa(DostepneItemki.PotionDefence);
                item.dzialania = new ArrayList<Effect>();
                item.dzialania.add(new Effect());
                item.dzialania.get(0).setIkona(new EffectActor(v.getA().texDefencePotion, 0, 0));
                break;
        }

        item.setSize(100, 100);
        return item;
    }
}
