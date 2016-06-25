/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.vs.enums.DostepneMoby;
import com.vs.network.Network;

import java.util.Random;

/**
 *
 * @author v
 */
public class Fight {

    /**
     * Zwraca współczynnik ataku bohater z uwzględnieniem ekwipunku i efektów.
     *
     * @param bohater Referencja do obiektu bohatera.
     * @return
     */
    static public int getAttack(Bohater bohater) {
        int atak;
        Random rnd = new Random();
        atak = rnd.nextInt(bohater.getAtak() + getAtakEkwipunkuBohaterAtakujacego(bohater)
                + getAtakEfektyBohatera(bohater) + 1);

        return atak;
    }

    /**
     * Zwraca współczynnik oborony bohatera z uzwględnieniem ekwipunku i
     * efektów.
     *
     * @param bohater Referencja do obiektu bohatera.
     * @return
     */
    static public int getObrona(Bohater bohater) {
        int obrona;
        Random rnd = new Random();
        obrona = bohater.getObrona() + getObronaEkwipunkuBohaterBroniacego(bohater)
                + getObronaEfektyBohatera(bohater) + 1;
        if (obrona <= 0) {
            obrona = 0;
        } else {
            obrona = rnd.nextInt(obrona);
        }
        System.out.println("Siła obrony: " + obrona);
        return obrona;
    }

    /**
     * Zwraca ilość obrażeń po ataku bohatera
     *
     * @param bohaterAtakujacy referencja do obiektu bohatera atakującego
     * @param bohaterBroniacy referencja do obiketu bohatera broniącego się
     * @return Zwraca ilość obrażeń
     */
    static public int getObrazenia(Bohater bohaterAtakujacy, Bohater bohaterBroniacy) {

        System.out.println("----- BOHATER VS BOHATER -----");
        int atak = getAttack(bohaterAtakujacy);
        int obrona = getObrona(bohaterBroniacy);

        if (atak > 0) {
            System.out.println("ATAK > 0 = TRAFIENIE");
            atak += getDamageEkwipunkuBohateraAtakujacego(bohaterAtakujacy);
            atak += getDamageEfektyBohatera(bohaterAtakujacy);

            for (SpellEffects sE : bohaterAtakujacy.getSpellEffects()) {
                if (sE.isFightEffect()) {
                    sE.dzialanie(sE.getFightEffects(), bohaterAtakujacy, bohaterBroniacy);
                }
            }

        } else {
            System.out.println("ATAK < 0 = PUDŁO");
        }

        int dmg = 0;

//        if (bohaterAtakujacy.getTempEffects().get(0).getEfektDmg() > 0) {
//            System.out.println("Wykryto zwiększone obrażenia: " + bohaterAtakujacy.getTempEffects().get(0).getEfektDmg());
//            dmg += bohaterAtakujacy.getTempEffects().get(0).getEfektDmg();
//            bohaterAtakujacy.getTempEffects().get(0).setEfektDmg(0);
//        }
        dmg += atak - (obrona + getArmorEkwipunkuBohateraAtakujacego(bohaterBroniacy)
                + getArmorEfektyBohatera(bohaterBroniacy));

        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            bohaterBroniacy.setActualHp(bohaterBroniacy.getActualHp() - dmg);
        }
        if (bohaterBroniacy.getActualHp() <= 0) {
            bohaterBroniacy.remove();
            System.out.println("Smierc Bohatera broniacego się");
        }

        bohaterAtakujacy.setPozostaloRuchow(bohaterAtakujacy.getPozostaloRuchow() - 1);

        bohaterBroniacy.aktualizujTeksture();

        if (GameStatus.gs.getNetworkStatus() == 2) {
            networkHeroDamage(dmg, bohaterBroniacy);
        }
        return dmg;
    }

    /**
     * Zwraca ilość obrażeń po ataku bohatera na zamek
     *
     * @param bohaterAtakujacy Referencja do obiektu bohatera atakującego
     * @param castle Referencja do obiektu zamku
     * @return Ilosć obrażeń
     */
    static public int getObrazenia(Bohater bohaterAtakujacy, Castle castle) {
        Random rnd = new Random();
        System.out.println("Nastąpił atak Bohater VS Zamek");
        int atak = getAttack(bohaterAtakujacy);
        int obrona = rnd.nextInt(castle.getObrona() + 1);
        System.out.println("siła obrony: " + obrona);

        if (atak > 0) {
            System.out.println("ATAK > 0 = TRAFIENIE");
            atak += getDamageEkwipunkuBohateraAtakujacego(bohaterAtakujacy);
            atak += getDamageEfektyBohatera(bohaterAtakujacy);
        } else {
            System.out.println("ATAK < 0 = PUDŁO");
        }

        int dmg = 0;

//        if (bohaterAtakujacy.getTempEffects().get(0).getEfektDmg() > 0) {
//            System.out.println("Wykryto zwiększone obrażenia: " + bohaterAtakujacy.getTempEffects().get(0).getEfektDmg());
//            dmg += bohaterAtakujacy.getTempEffects().get(0).getEfektDmg();
//            bohaterAtakujacy.getTempEffects().get(0).setEfektDmg(0);
//        }
        dmg += atak - obrona;
        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            castle.setActualHp(castle.getActualHp() - dmg);
        }
        if (castle.getActualHp() <= 0) {
            System.out.println("Zamek nie posiada już obrońców - można go zająć");
            castle.setActualHp(0);
        }

        bohaterAtakujacy.setPozostaloRuchow(bohaterAtakujacy.getPozostaloRuchow() - 1);

        return dmg;
    }

    /**
     *
     * @param bohaterAtakujacy Referencja do obiektu bohatera atakującego
     * @param mob Referencja do obiektu moba
     * @return Ilość obrażeń
     */
    static public int getObrazenia(Bohater bohaterAtakujacy, Mob mob) {

        mob.mobZaatakowany(bohaterAtakujacy.getPozXnaMapie(), bohaterAtakujacy.getPozYnaMapie());

        Random rnd = new Random();
        System.out.println("----- BOHATER VS MOB ------");
        int atak = getAttack(bohaterAtakujacy);
        System.out.println("Siła ataku: " + atak);
        int obrona = mob.getObrona() + getObronaEfektyMoba(mob) + 1;
        if (obrona <= 0) {
            obrona = 0;
        } else {
            obrona = rnd.nextInt(obrona);
        }
        System.out.println("siła obrony: " + obrona);

        if (atak > 0) {
            System.out.println("ATAK > 0 = TRAFIENIE");
            atak += getDamageEkwipunkuBohateraAtakujacego(bohaterAtakujacy);
            atak += getDamageEfektyBohatera(bohaterAtakujacy);

            for (SpellEffects sE : bohaterAtakujacy.getSpellEffects()) {
                if (sE.isFightEffect()) {
                    sE.dzialanie(sE.getFightEffects(), bohaterAtakujacy, mob);
                }
            }

        } else {
            System.out.println("ATAK < 0 = PUDŁO");
        }

        int dmg = 0;

//        if (bohaterAtakujacy.getTempEffects().get(0).getEfektDmg() > 0) {
//            System.out.println("Wykryto zwiększone obrażęnia: " + bohaterAtakujacy.getTempEffects().get(0).getEfektDmg());
//            dmg += bohaterAtakujacy.getTempEffects().get(0).getEfektDmg();
//            bohaterAtakujacy.getTempEffects().get(0).setEfektDmg(0);
//        }
        dmg += atak - obrona;
        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            mob.setAktualneHp(mob.getAktualneHp() - dmg);
        }
        if (mob.getAktualneHp() <= 0) {
            System.out.println("Śmierć moba.");
            mob.setAktualneHp(0);
            bohaterAtakujacy.setExp(bohaterAtakujacy.getExp() + mob.getExpReward());
        }

        bohaterAtakujacy.setPozostaloRuchow(bohaterAtakujacy.getPozostaloRuchow() - 1);

        if (GameStatus.gs.getNetworkStatus() == 2) {
            networkMobDamage(dmg, mob.getPozX(), mob.getPozY());
        }

        return dmg;
    }

    /**
     * Zwraca ilość obrażeń po ataku Moba
     *
     * @param mob Referencja do obektu Moba
     * @param bohaterBroniacy referencja do obiketu bohatera broniącego się
     * @return Zwraca ilość obrażeń
     */
    static public int getObrazenia(Mob mob, Bohater bohaterBroniacy) {

        if (mob.getTypMoba() == DostepneMoby.Wilk) {
            GameStatus.a.wolfSnarl.play();
        }

        Random rnd = new Random();
        System.out.println("----- MOB VS BOHATER -----");
        int atak = rnd.nextInt(mob.getAtak() + 1 + getAtakEfektyMoba(mob));
        int obrona = getObrona(bohaterBroniacy);
        System.out.println("Siła ataku:  " + atak);
        int dmg = atak - (obrona + getArmorEkwipunkuBohateraAtakujacego(bohaterBroniacy)
                + getArmorEfektyBohatera(bohaterBroniacy));
        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            bohaterBroniacy.setActualHp(bohaterBroniacy.getActualHp() - dmg);
        }
        if (bohaterBroniacy.getActualHp() <= 0) {
            bohaterBroniacy.remove();
            System.out.println("Smierc Bohatera broniacego się");
        }

        bohaterBroniacy.aktualizujTeksture();

        if (GameStatus.gs.getNetworkStatus() == 2) {
            networkHeroDamage(dmg, bohaterBroniacy);
        }

        return dmg;
    }

    /**
     * Liczy obrażenia dla rzucenia czaru przez bohatera na moba.
     *
     * @param bohaterAtakujacy
     * @param mob
     * @param spell
     * @return
     */
    static public int getSpellObrazenia(Bohater bohaterAtakujacy, Mob mob, SpellActor spell) {
        mob.mobZaatakowany(bohaterAtakujacy.getPozXnaMapie(), bohaterAtakujacy.getPozYnaMapie());

        Random rnd = new Random();
        System.out.println("Nastąpił sepll na moba");
        int damage = rnd.nextInt(bohaterAtakujacy.getMoc() + 1) + spell.getDmg();
        int obrona = rnd.nextInt(mob.getObrona() + 1);
        System.out.println("Siła ataku:  " + damage);
        System.out.println("siła obrony: " + obrona);
        int dmg = damage - obrona;
        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            mob.setAktualneHp(mob.getAktualneHp() - dmg);
        }
        if (mob.getAktualneHp() <= 0) {
            System.out.println("Śmierć moba.");
            mob.setAktualneHp(0);
            bohaterAtakujacy.setExp(bohaterAtakujacy.getExp() + mob.getExpReward());
        }

        bohaterAtakujacy.setPozostaloRuchow(bohaterAtakujacy.getPozostaloRuchow() - 1);
        bohaterAtakujacy.setActualMana(bohaterAtakujacy.getActualMana() - spell.getKoszt());

        return dmg;
    }

    /**
     * Zwraca obrażenia zadane zamkowi
     * @param bohaterAtakujacy  Referencja do obiektu bohatera atakującego
     * @param castle    Referenja do obiektu zamku
     * @param spell Referencja do obiektu zaklęcia
     * @return  ilość obrażeń
     */
    static public int getSpellObrazenia(Bohater bohaterAtakujacy, Castle castle, SpellActor spell) {

        Random rnd = new Random();
        System.out.println("Nastąpił sepll na zamek");
        int damage = rnd.nextInt(bohaterAtakujacy.getMoc() + 1) + spell.getDmg();
        int obrona = rnd.nextInt(castle.getObrona() + 1);
        System.out.println("Siła ataku:  " + damage);
        System.out.println("siła obrony: " + obrona);
        int dmg = damage - obrona;
        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            castle.setActualHp(castle.getActualHp() - dmg);
        }
        if (castle.getActualHp() <=0) {
            System.out.println("Zamek stracił całe HP.");
            castle.setActualHp(0);
        }

        bohaterAtakujacy.setPozostaloRuchow(bohaterAtakujacy.getPozostaloRuchow() - 1);
        bohaterAtakujacy.setActualMana(bohaterAtakujacy.getActualMana() - spell.getKoszt());

        return dmg;
    }

    static public int getSpellObrazenia(Bohater bohaterAtakujacy, Bohater bohaterBroniacy, SpellActor spell) {
        System.out.println("Funkacja Fight.getObrazenia");

        Random rnd = new Random();
        int damage = rnd.nextInt(bohaterAtakujacy.getMoc() + 1) + spell.getDmg();
        int obrona = rnd.nextInt(bohaterBroniacy.getObrona() + getObronaEkwipunkuBohaterBroniacego(bohaterBroniacy)
                + getObronaEfektyBohatera(bohaterBroniacy) + 1);
        System.out.println("Siła ataku:  " + damage);
        System.out.println("siła obrony: " + obrona);
        int dmg = damage - (obrona + getArmorEkwipunkuBohateraAtakujacego(bohaterBroniacy)
                + getArmorEfektyBohatera(bohaterBroniacy));
        if (dmg < 0) {
            dmg = 0;
        }
        System.out.println("damage: " + dmg);
        if (dmg > 0) {
            bohaterBroniacy.setActualHp(bohaterBroniacy.getActualHp() - dmg);
        }
        if (bohaterBroniacy.getActualHp() <= 0) {
            bohaterBroniacy.remove();
            System.out.println("Smierc Bohatera broniacego się");
        }

        bohaterAtakujacy.setPozostaloRuchow(bohaterAtakujacy.getPozostaloRuchow() - 1);
        bohaterAtakujacy.setActualMana(bohaterAtakujacy.getActualMana() - spell.getKoszt());

        bohaterBroniacy.aktualizujTeksture();

        return dmg;
    }

    /**
     * Zwraca sume pancerza itemkó bohatera
     *
     * @param bohaterBroniacy
     * @return
     */
    static public int getArmorEkwipunkuBohateraAtakujacego(Bohater bohaterBroniacy) {
        int sumaObrazen = 0;
        sumaObrazen += bohaterBroniacy.getItemGlowa().getArmor();
        sumaObrazen += bohaterBroniacy.getItemKorpus().getArmor();
        sumaObrazen += bohaterBroniacy.getItemLewaReka().getArmor();
        sumaObrazen += bohaterBroniacy.getItemPrawaReka().getArmor();
        sumaObrazen += bohaterBroniacy.getItemNogi().getArmor();
        sumaObrazen += bohaterBroniacy.getItemStopy().getArmor();

        System.out.println("Sumba pancerza itemków: " + sumaObrazen);

        return sumaObrazen;
    }

    /**
     * Zwraca wartość obrażeń ekwipunku zadanego bohatera.
     *
     * @param bohaterAtakujacy
     * @return Wartość obrażeń
     */
    static public int getDamageEkwipunkuBohateraAtakujacego(Bohater bohaterAtakujacy) {
        int sumaObrazen = 0;
        sumaObrazen += bohaterAtakujacy.getItemGlowa().getDmg();
        sumaObrazen += bohaterAtakujacy.getItemKorpus().getDmg();
        sumaObrazen += bohaterAtakujacy.getItemLewaReka().getDmg();
        sumaObrazen += bohaterAtakujacy.getItemPrawaReka().getDmg();
        sumaObrazen += bohaterAtakujacy.getItemNogi().getDmg();
        sumaObrazen += bohaterAtakujacy.getItemStopy().getDmg();

        System.out.println("Sumba obrażeń itemków: " + sumaObrazen);

        return sumaObrazen;
    }

    /**
     *
     * @param bohaterBroniacy referencja do obiketu bohatera broniącego się
     * @return zwraca wartość obrony dla wszystkich itemków bohatera broniącego
     * się
     */
    static public int getObronaEkwipunkuBohaterBroniacego(Bohater bohaterBroniacy) {
        int sumaObrony = 0;
        sumaObrony += bohaterBroniacy.getItemGlowa().getObrona();
        sumaObrony += bohaterBroniacy.getItemKorpus().getObrona();
        sumaObrony += bohaterBroniacy.getItemLewaReka().getObrona();
        sumaObrony += bohaterBroniacy.getItemPrawaReka().getObrona();
        sumaObrony += bohaterBroniacy.getItemNogi().getObrona();
        sumaObrony += bohaterBroniacy.getItemStopy().getObrona();

        System.out.println("Suma obrony itemków: " + sumaObrony);

        return sumaObrony;
    }

    /**
     * Zwraca sume pancerza dla efektów czarów i efektó mikstur.
     *
     * @param bohater
     * @return
     */
    static public int getArmorEfektyBohatera(Bohater bohater) {
        int sumaEfektArmor = 0;
        for (Effect efekty : bohater.getEfekty()) {
            sumaEfektArmor += efekty.getEfektArmor();
            System.out.print("E ARM: " + efekty.getEfektArmor() + " ");
        }
        for (SpellEffects sE : bohater.getSpellEffects()) {
            sumaEfektArmor += sE.getEfektArmor();
            System.out.print("SE ARM: " + sE.getEfektArmor() + " ");
        }
        for (Effect tmpEfekts : bohater.getTempEffects()) {
            sumaEfektArmor += tmpEfekts.getEfektArmor();
            System.out.print("TE ARM: " + tmpEfekts.getEfektArmor() + " ");
        }

        System.out.println("Suma ARM dla efektów: " + sumaEfektArmor);

        return sumaEfektArmor;
    }

    /**
     * Zwraca sume obrażeń dla efektów czarów i efektó mikstur.
     *
     * @param bohater
     * @return
     */
    static public int getDamageEfektyBohatera(Bohater bohater) {
        int sumaEfektDamage = 0;
        for (Effect efekty : bohater.getEfekty()) {
            sumaEfektDamage += efekty.getEfektDmg();
            System.out.print("E DMG: " + efekty.getEfektDmg() + " ");
        }
        for (SpellEffects sE : bohater.getSpellEffects()) {
            sumaEfektDamage += sE.getEfektDmg();
            System.out.print("SE DMG: " + sE.getEfektDmg() + " ");
        }
        for (Effect tmpEfekts : bohater.getTempEffects()) {
            sumaEfektDamage += tmpEfekts.getEfektDmg();
            System.out.print("TE DMG: " + tmpEfekts.getEfektDmg() + " ");
        }

        System.out.println("Suma DMG dla efektów: " + sumaEfektDamage);

        return sumaEfektDamage;
    }

    /**
     * Zwraca wartosć ataku efektów zadanego bohatera
     *
     * @param bohater Referencja do obiektu bohatera
     * @return ilość punktów ataku
     */
    static public int getAtakEfektyBohatera(Bohater bohater) {
        int sumaEfektAtak = 0;
        for (Effect efekty : bohater.getEfekty()) {
            sumaEfektAtak += efekty.getEfektAtak();
            System.out.print("E ATK: " + efekty.getEfektAtak() + " ");
        }
        for (SpellEffects sE : bohater.getSpellEffects()) {
            sumaEfektAtak += sE.getEfektAtak();
            System.out.print("SE ATK: " + sE.getEfektAtak() + " ");
        }
        for (Effect tmpEfekts : bohater.getTempEffects()) {
            sumaEfektAtak += tmpEfekts.getEfektAtak();
            System.out.print("TE ATK: " + tmpEfekts.getEfektAtak() + " ");
        }
        System.out.println("Suma ATK dla efektów: " + sumaEfektAtak);
        return sumaEfektAtak;
    }

    /**
     * Zwraca wartosć obrony efektów zadanego bohatera
     *
     * @param bohater Referencja do obiektu bohatera
     * @return ilość punktów obrony
     */
    static public int getObronaEfektyBohatera(Bohater bohater) {
        int sumaEfektObrona = 0;
        for (Effect efekty : bohater.getEfekty()) {
            sumaEfektObrona += efekty.getEfektObrona();
            System.out.print("E OBR: " + efekty.getEfektObrona() + " ");
        }
        for (SpellEffects sE : bohater.getSpellEffects()) {
            sumaEfektObrona += sE.getEfektObrona();
            System.out.print("SE OBR: " + sE.getEfektObrona() + " ");
        }
        for (Effect tmpEfekts : bohater.getTempEffects()) {
            sumaEfektObrona += tmpEfekts.getEfektObrona();
            System.out.print("TE OBR: " + tmpEfekts.getEfektObrona() + " ");
        }
        System.out.println("Suma OBR dla efektów: " + sumaEfektObrona);
        return sumaEfektObrona;
    }

    /**
     *
     * @param bohaterAtakujacy referencja do obiektu bohatera atakującego
     * @return zwraca wartość ataku dla wszystkich itemków bohatera atakującego
     */
    static public int getAtakEkwipunkuBohaterAtakujacego(Bohater bohaterAtakujacy) {
        int sumaAtaku = 0;
        sumaAtaku += bohaterAtakujacy.getItemGlowa().getAtak();
        sumaAtaku += bohaterAtakujacy.getItemKorpus().getAtak();
        sumaAtaku += bohaterAtakujacy.getItemLewaReka().getAtak();
        sumaAtaku += bohaterAtakujacy.getItemPrawaReka().getAtak();
        sumaAtaku += bohaterAtakujacy.getItemNogi().getAtak();
        sumaAtaku += bohaterAtakujacy.getItemStopy().getAtak();

        System.out.println("Suma ataku itemków: " + sumaAtaku);

        return sumaAtaku;
    }

    /**
     * Zwraca sumę szybkości dla wszsytkich itemków w które wyposażony jest
     * bohater
     *
     * @param bohaterAtakujacy referencja do obiektu bohatera dla którego ma
     * zostać zsumowana ilość punktów szybkości
     * @return
     */
    static public int getSzybkoscEkwipunkuBohatera(Bohater bohaterAtakujacy) {
        int sumaSzybkosci = 0;
        sumaSzybkosci += bohaterAtakujacy.getItemGlowa().getSzybkosc();
        sumaSzybkosci += bohaterAtakujacy.getItemKorpus().getSzybkosc();
        sumaSzybkosci += bohaterAtakujacy.getItemLewaReka().getSzybkosc();
        sumaSzybkosci += bohaterAtakujacy.getItemPrawaReka().getSzybkosc();
        sumaSzybkosci += bohaterAtakujacy.getItemNogi().getSzybkosc();
        sumaSzybkosci += bohaterAtakujacy.getItemStopy().getSzybkosc();

        System.out.println("Suma zwiększenia szybkości: " + sumaSzybkosci);

        return sumaSzybkosci;
    }

    /**
     * Zwraca sumę efektów obronnych dla Moba
     *
     * @param mob Referencja do obiektu moba.
     * @return
     */
    static public int getObronaEfektyMoba(Mob mob) {
        int sumaEfektObrona = 0;

        for (SpellEffects sE : mob.getSpellEffects()) {
            sumaEfektObrona += sE.getEfektObrona();
            System.out.println("E OBR MOBA: " + sE.getEfektObrona());
        }
        return sumaEfektObrona;
    }

    /**
     * Zwraca sumę efektów atakujących dla Moba
     *
     * @param mob Referencja do obiektu moba.
     * @return
     */
    static public int getAtakEfektyMoba(Mob mob) {
        int sumaEfektAtak = 0;

        for (SpellEffects sE : mob.getSpellEffects()) {
            sumaEfektAtak += sE.getEfektAtak();
            System.out.println("E ATK MOBA: " + sE.getEfektAtak());
        }
        return sumaEfektAtak;
    }

    /**
     * Wykonuje przekazanie przez sieć obrażeń dla zadanego bohatera.
     *
     * @param dmg             obrażenia.
     * @param bohaterBroniacy bohater którego dot. obrażenia.
     */
    static public void networkHeroDamage(int dmg, Bohater bohaterBroniacy) {
        Network.DamageHero damageHero = new Network.DamageHero();
        damageHero.damage = dmg;
        damageHero.player = bohaterBroniacy.getPrzynaleznoscDoGracza();
        damageHero.hero = Bohater.getHeroNumberInArrayList(bohaterBroniacy, GameStatus.gs.getGracze().get(
                bohaterBroniacy.getPrzynaleznoscDoGracza()
        ));
        GameStatus.client.getCnt().sendTCP(damageHero);
    }

    /**
     * Wykonuje przekazanie przez sieć obrażeń dla zadanego moba.
     *
     * @param dmg      obrażenia
     * @param pozXmoba Pozycja X na mapie Moba.
     * @param pozYMoba Pozycja Y na mapie Moba.
     */
    static public void networkMobDamage(int dmg, int pozXmoba, int pozYMoba) {
        Network.DamageMob damageMob = new Network.DamageMob();
        damageMob.damage = dmg;
        damageMob.pozXmoba = pozXmoba;
        damageMob.pozYmoba = pozYMoba;
        GameStatus.client.getCnt().sendTCP(damageMob);
    }
}
