package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vs.enums.AnimsTypes;
import com.vs.enums.FightEffects;
import com.vs.enums.KlasyPostaci;
import com.vs.screens.DialogScreen;

import java.util.Random;

/**
 * Klasa definiuje efekty czarów
 *
 * @author v
 */
public class SpellEffects {

    private boolean fightEffect = false;
    private FightEffects fightEffects;

    private boolean posionEffect = false;

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
     * @param spell            Referencja do obiketu czaru
     * @param bohaterCastujacy Referencja do obiketu bohatera rzucającego czar
     * @param obiketBroniacy   Referencja do obiektu na którego czar jest rzucany
     *                         (bohater/mob)
     */
    public void dzialanie(SpellActor spell, Object obiketBroniacy, Bohater bohaterCastujacy, V v) {
        switch (spell.getRodzajCzaru()) {

            case SummonBear:
                Gdx.app.log("case dziala", "");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {

                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());

                    Texture texSummon = new Texture("moby/summons/bear.png");
                    Texture texSummonZ = new Texture("moby/summons/bearZ.png");

                    Bohater summonBohater = new Bohater(texSummon, texSummonZ, spell.getSpellX() * 100, spell.getSpellY() * 100, spell.getSpellX()
                            , spell.getSpellY(), KlasyPostaci.Summmon, v);

                    bohaterCastujacy.getGs().getMapa().getPola()[spell.getSpellX()][spell.getSpellY()].setBohater(summonBohater);

                    summonBohater.setHp(8 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setActualHp(summonBohater.getHp() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setAtak(5 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setObrona(3 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setSzybkosc(2 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setPozostaloRuchow(summonBohater.getSzybkosc());
                    summonBohater.setPozXnaMapie(spell.getSpellX());
                    summonBohater.setPozYnaMapie(spell.getSpellY());
                    summonBohater.setPrzynaleznoscDoGracza(bohaterCastujacy.getPrzynaleznoscDoGracza());
                    bohaterCastujacy.getGs().getGracze().get(bohaterCastujacy.getPrzynaleznoscDoGracza()).getBohaterowie().add(summonBohater);

                    Assets.stage01MapScreen.addActor(summonBohater);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();

                break;

            case SummonWolf:
                Gdx.app.log("case dziala", "");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {

                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());

                    Texture texSummon = new Texture("moby/summons/wolf.png");
                    Texture texSummonZ = new Texture("moby/summons/wolfZ.png");

                    Bohater summonBohater = new Bohater(texSummon, texSummonZ, spell.getSpellX() * 100, spell.getSpellY() * 100, spell.getSpellX()
                            , spell.getSpellY(), KlasyPostaci.Summmon, v);

                    bohaterCastujacy.getGs().getMapa().getPola()[spell.getSpellX()][spell.getSpellY()].setBohater(summonBohater);

                    summonBohater.setHp(4 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setActualHp(summonBohater.getHp() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setAtak(3 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setObrona(3 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setSzybkosc(5 + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    summonBohater.setPozostaloRuchow(summonBohater.getSzybkosc());
                    summonBohater.setPozXnaMapie(spell.getSpellX());
                    summonBohater.setPozYnaMapie(spell.getSpellY());
                    summonBohater.setPrzynaleznoscDoGracza(bohaterCastujacy.getPrzynaleznoscDoGracza());
                    bohaterCastujacy.getGs().getGracze().get(bohaterCastujacy.getPrzynaleznoscDoGracza()).getBohaterowie().add(summonBohater);

                    Assets.stage01MapScreen.addActor(summonBohater);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();

                break;

            case FireBall:
                // Zadaje obrażenia
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    v.getA().fireball.play();
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.FireExplosionAnimation));
                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;
                        Animation.animujLblDamage(tmpBoh.getX(), tmpBoh.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpBoh, spell)), v.getA());
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());
                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        Mob tmpMob = (Mob) obiketBroniacy;
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());
                        Animation.animujLblDamage(tmpMob.getX(), tmpMob.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpMob, spell)), v.getA());
                    } else if (obiketBroniacy.getClass() == Castle.class){
                        Gdx.app.log("Typ przeciwnika:", "Zamek");
                        Castle tmpCastle = (Castle) obiketBroniacy;
                        animActor.setPosition(tmpCastle.getX(), tmpCastle.getY());
                        //a.animujSpellLblDmg(tmpCastle.getX(), tmpCastle.getY(), bohaterCastujacy, tmpCastle, spell);

                        Animation.animujLblDamage(tmpCastle.getX(), tmpCastle.getY(),
                                "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpCastle, spell)), v.getA());
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case LongShot:
                // Zadaje obrażenia
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.SlashAnimation));
                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;
                        Animation.animujLblDamage(tmpBoh.getX(), tmpBoh.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpBoh, spell)), v.getA());
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());
                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        Mob tmpMob = (Mob) obiketBroniacy;
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());
                        Animation.animujLblDamage(tmpMob.getX(), tmpMob.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpMob, spell)), v.getA());

                    }  else if (obiketBroniacy.getClass() == Castle.class){
                        Gdx.app.log("Typ przeciwnika:", "Zamek");
                        Castle tmpCastle = (Castle) obiketBroniacy;
                        animActor.setPosition(tmpCastle.getX(), tmpCastle.getY());
                        Animation.animujLblDamage(tmpCastle.getX(), tmpCastle.getY(),
                                "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpCastle, spell)), v.getA());
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case VampireTouch:
                // Zadaje obrażenia
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.BadSpellAnimation));
                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;

                        int dmg = (Fight.getSpellObrazenia(bohaterCastujacy, tmpBoh, spell));
                        Animation.animujLblDamage(tmpBoh.getX(), tmpBoh.getY(), "Dmg: " + dmg, v.getA());
                        bohaterCastujacy.setActualHp(bohaterCastujacy.getActualHp() + dmg);
                        if (bohaterCastujacy.getActualHp() > bohaterCastujacy.getHp()) {
                            bohaterCastujacy.setActualHp(bohaterCastujacy.getHp());
                        }
                        tmpBoh.setActualHp(tmpBoh.getActualHp() - dmg);
                        if (tmpBoh.getActualHp() < 1)
                            tmpBoh.setActualHp(1);

                        bohaterCastujacy.aktualizujTeksture();
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());

                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        Mob tmpMob = (Mob) obiketBroniacy;
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());

                        int dmg = (Fight.getSpellObrazenia(bohaterCastujacy, tmpMob, spell));
                        Animation.animujLblDamage(tmpMob.getX(), tmpMob.getY(), "Dmg: " + dmg, v.getA());
                        bohaterCastujacy.setActualHp(bohaterCastujacy.getActualHp() + dmg);
                        tmpMob.setAktualneHp(tmpMob.getAktualneHp() - dmg);
                        if (tmpMob.getAktualneHp() > tmpMob.getHp()) {
                            tmpMob.setAktualneHp(tmpMob.getHp());
                        }
                        if (tmpMob.getAktualneHp() < 1)
                            tmpMob.setAktualneHp(1);

                        bohaterCastujacy.aktualizujTeksture();
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Thunder:
                // Zadaje obrażenia
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.ThunderSpellAnimation));
                    v.getA().thuner.play();
                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;
                        Animation.animujLblDamage(tmpBoh.getX(), tmpBoh.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpBoh, spell)), v.getA());
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());
                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        Mob tmpMob = (Mob) obiketBroniacy;
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());
                        Animation.animujLblDamage(tmpMob.getX(), tmpMob.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpMob, spell)), v.getA());
                    }  else if (obiketBroniacy.getClass() == Castle.class){
                        Gdx.app.log("Typ przeciwnika:", "Zamek");
                        Castle tmpCastle = (Castle) obiketBroniacy;
                        animActor.setPosition(tmpCastle.getX(), tmpCastle.getY());
                        Animation.animujLblDamage(tmpCastle.getX(), tmpCastle.getY(),
                                "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpCastle, spell)), v.getA());
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case MeteorShower:
                // Zadaje obrażenia
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    v.getA().meteorShower.play();
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.ThunderSpellAnimation));
                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;
                        Animation.animujLblDamage(tmpBoh.getX(), tmpBoh.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpBoh, spell)), v.getA());
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());
                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        Mob tmpMob = (Mob) obiketBroniacy;
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());
                        Animation.animujLblDamage(tmpMob.getX(), tmpMob.getY(), "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpMob, spell)), v.getA());
                    }  else if (obiketBroniacy.getClass() == Castle.class){
                        Gdx.app.log("Typ przeciwnika:", "Zamek");
                        Castle tmpCastle = (Castle) obiketBroniacy;
                        animActor.setPosition(tmpCastle.getX(), tmpCastle.getY());
                        Animation.animujLblDamage(tmpCastle.getX(), tmpCastle.getY(),
                                "Dmg: " + Integer.toString(Fight.getSpellObrazenia(bohaterCastujacy, tmpCastle, spell)), v.getA());
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Frozen:
                // Zmniejsza szybkosć na zadaną ilość tur
                Random rnd = new Random();
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {

                    v.getA().freezSpell.play();
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.FrozenSpellAnimation));
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());

                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;
                        int modSzybkosci = 1 + rnd.nextInt(bohaterCastujacy.getMoc() + 1 + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                        this.dlugoscTrwaniaEfektu = modSzybkosci;
                        this.efektSzybkosc = -1 * modSzybkosci;
                        tmpBoh.setPozostaloRuchow(tmpBoh.getPozostaloRuchow() - modSzybkosci);
                        tmpBoh.getSpellEffects().add(this);
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());
                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        Mob tmpMob = (Mob) obiketBroniacy;
                        int modSzybkosci = 1 + rnd.nextInt(bohaterCastujacy.getMoc() + 1 + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                        tmpMob.setAktualnaSzybkosc(tmpMob.getAktualnaSzybkosc() - modSzybkosci);
                        this.dlugoscTrwaniaEfektu = modSzybkosci;
                        this.efektSzybkosc = -1 * modSzybkosci;
                        tmpMob.getSpellEffects().add(this);
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Rage:
                // Zwiększa atak o punkty mocy do końca tury.
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    v.getA().rageSpell.play();
                    this.efektAtak = 1;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                bohaterCastujacy.aktualizujEfektyBohatera();
                break;

            case Haste:
                // Zwiększa aktualną szybkość o liczbę punktów mocy
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    v.getA().magicWand.play();
                    System.out.println("Czar HASTE");
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.setPozostaloRuchow(bohaterCastujacy.getPozostaloRuchow() + bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy));
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Cure:
                // Leczy bohatera o liczbę posiadanych punków mocy * 2
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    System.out.println("Czar CURE");
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.setActualHp(bohaterCastujacy.getActualHp() + (bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy)) * 2);
                    if (bohaterCastujacy.getActualHp() > bohaterCastujacy.getHp()) {
                        bohaterCastujacy.setActualHp(bohaterCastujacy.getHp());
                    }
                    bohaterCastujacy.aktualizujTeksture();
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);

                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case SongOfGlory:
                System.out.println("Czar SONG OF GLORY");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    this.efektAtak = 1;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    Bohater tempBoh;
                    tempBoh = (Bohater) obiketBroniacy;
                    tempBoh.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(tempBoh.getX(), tempBoh.getY());
                    Assets.stage01MapScreen.addActor(animActor);

                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Bless:
                System.out.println("Czar Blogoslawienstwo");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {

                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());

                    this.efektAtak = 5;
                    this.efektObrona = 5;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    Bohater tempBoh;
                    tempBoh = (Bohater) obiketBroniacy;
                    tempBoh.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(tempBoh.getX(), tempBoh.getY());
                    Assets.stage01MapScreen.addActor(animActor);

                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Prayer:
                System.out.println("Czar Modlitwa");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {

                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());

                    this.efektAtak = 7;
                    this.efektObrona = 7;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    Bohater tempBoh;
                    tempBoh = (Bohater) obiketBroniacy;
                    tempBoh.getSpellEffects().add(this);
                    tempBoh.setActualHp(tempBoh.getHp());
                    tempBoh.setPozostaloRuchow(tempBoh.getSzybkosc() + getEfektSzybkosc());
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(tempBoh.getX(), tempBoh.getY());
                    tempBoh.aktualizujTeksture();
                    Assets.stage01MapScreen.addActor(animActor);

                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Discouragement:
                System.out.println("Czar DISCOURAGEMENT");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    this.fightEffect = true;
                    this.fightEffects = FightEffects.DiscouragementEffect;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Poison:
                System.out.println("Czar POISON");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {

                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.BadSpellAnimation));
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());

                    if (obiketBroniacy.getClass() == Bohater.class) {
                        Bohater tmpBoh = (Bohater) obiketBroniacy;
                        this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                        tmpBoh.getSpellEffects().add(this);
                        animActor.setPosition(tmpBoh.getX(), tmpBoh.getY());
                    } else if (obiketBroniacy.getClass() == Mob.class) {
                        System.out.println("przeciwnik jest mobem");
                        this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                        Mob tmpMob = (Mob) obiketBroniacy;
                        tmpMob.getSpellEffects().add(this);
                        animActor.setPosition(tmpMob.getX(), tmpMob.getY());
                    }
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Charge:
                System.out.println("Czar CHARGE");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    this.fightEffect = true;
                    this.fightEffects = FightEffects.ChargeEffect;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case Fury:
                System.out.println("Czar FURY");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    this.fightEffect = true;
                    this.fightEffects = FightEffects.FuryEffect;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;

            case FinalJudgment:
                System.out.println("Czar FinalJudgment");
                if (spell.getKoszt() <= bohaterCastujacy.getActualMana()) {
                    this.fightEffect = true;
                    this.fightEffects = FightEffects.FinalJudgeEffect;
                    this.dlugoscTrwaniaEfektu = bohaterCastujacy.getMoc() + Fight.getMocEkwipunkuBohatera(bohaterCastujacy);
                    bohaterCastujacy.setActualMana(bohaterCastujacy.getActualMana() - spell.getKoszt());
                    bohaterCastujacy.getSpellEffects().add(this);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.GoodSpellAnimation));
                    animActor.setPosition(bohaterCastujacy.getX(), bohaterCastujacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                } else {
                    System.out.println("Za mało MANY");
                }
                bohaterCastujacy.getSpells().clear();
                break;
        }
    }

    /**
     * Wykonuje działanie efektu podczas walki
     *
     * @param fightEffects    efekt czaru w czasie walki
     * @param obiektAtakujacy Referencja do obiektu atakującego
     * @param obiektBroniacy  Referencja do obiektu broniącego
     */
    public void dzialanie(FightEffects fightEffects, Object obiektAtakujacy, Object obiektBroniacy) {

        Bohater tmpBohaterAtakujacy;
        tmpBohaterAtakujacy = (Bohater) obiektAtakujacy;
        final Assets tmpAssets = new Assets();

        switch (fightEffects) {
            case DiscouragementEffect:

                SpellEffects tmpSpellEffects = new SpellEffects();
                tmpSpellEffects.efektObrona = -1;
                tmpSpellEffects.dlugoscTrwaniaEfektu = tmpBohaterAtakujacy.getMoc() + Fight.getMocEkwipunkuBohatera(tmpBohaterAtakujacy);
                EffectActor tmpEffectActor = new EffectActor(tmpAssets.texSpellDiscouragement, 0, 0);
                tmpSpellEffects.setIkona(tmpEffectActor);
                tmpSpellEffects.getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Piesn Chwaly", tmpAssets.skin, "Obrona zmniejszona o 1", Assets.stage01MapScreen);
                    }
                });
                if (obiektBroniacy.getClass() == Bohater.class) {
                    System.out.println("Dodanie efektu DISCOURAGEMENT do Bohatera.");
                    Bohater tmpBohaterBroniacy;
                    tmpBohaterBroniacy = (Bohater) obiektBroniacy;
                    tmpBohaterBroniacy.getSpellEffects().add(tmpSpellEffects);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.BadSpellAnimation));
                    animActor.setPosition(tmpBohaterBroniacy.getX(), tmpBohaterBroniacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);

                } else if (obiektBroniacy.getClass() == Mob.class) {
                    System.out.println("Dodanie efektu DISCOURAGEMENT do Moba.");
                    Mob tmpBohaterBroniacy;
                    tmpBohaterBroniacy = (Mob) obiektBroniacy;
                    tmpBohaterBroniacy.getSpellEffects().add(tmpSpellEffects);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.BadSpellAnimation));
                    animActor.setPosition(tmpBohaterBroniacy.getX(), tmpBohaterBroniacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);

                }
                break;

            case ChargeEffect:
                System.out.println("Charge Effect: Zwiększenie obrażeń o 1 za każdy udany atak");
                tmpBohaterAtakujacy.getTempEffects().get(0).setEfektDmg(tmpBohaterAtakujacy.getTempEffects().get(0).getEfektDmg() + 1);
                break;

            case FuryEffect:
                System.out.println("Fury Effect: Atak +2, Obrona -1 za każdy udany atak do końca tury ");
                tmpBohaterAtakujacy.getTempEffects().get(0).setEfektAtak(
                        tmpBohaterAtakujacy.getTempEffects().get(0).getEfektAtak() + 2);
                tmpBohaterAtakujacy.getTempEffects().get(0).setEfektObrona(
                        tmpBohaterAtakujacy.getTempEffects().get(0).getEfektObrona() - 1);
                break;

            case FinalJudgeEffect:
                System.out.println("Final Judge Effect: Obrona przeciwnika -2, Atak + 1 za każdy udany atak do końca tury ");

                SpellEffects tmpSpellEffects2 = new SpellEffects();
                tmpSpellEffects2.efektObrona = -2;
                tmpSpellEffects2.dlugoscTrwaniaEfektu = tmpBohaterAtakujacy.getMoc() + Fight.getMocEkwipunkuBohatera(tmpBohaterAtakujacy);
                ;
                EffectActor tmpEffectActor2 = new EffectActor(tmpAssets.texSpellFinalJudgment, 0, 0);
                tmpSpellEffects2.setIkona(tmpEffectActor2);
                tmpSpellEffects2.getIkona().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        DialogScreen dialogScreen = new DialogScreen("Sad Ostateczny", tmpAssets.skin, "Obrona zmniejszona o 2", Assets.stage01MapScreen);
                    }
                });
                if (obiektBroniacy.getClass() == Bohater.class) {
                    System.out.println("Dodanie efektu FINAL JUDGE do Bohatera Broniącego.");
                    Bohater tmpBohaterBroniacy;
                    tmpBohaterBroniacy = (Bohater) obiektBroniacy;
                    tmpBohaterBroniacy.getSpellEffects().add(tmpSpellEffects2);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.BadSpellAnimation));
                    animActor.setPosition(tmpBohaterBroniacy.getX(), tmpBohaterBroniacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);

                } else if (obiektBroniacy.getClass() == Mob.class) {
                    System.out.println("Dodanie efektu FINAL JUDGE do Moba.");
                    Mob tmpBohaterBroniacy;
                    tmpBohaterBroniacy = (Mob) obiektBroniacy;
                    tmpBohaterBroniacy.getSpellEffects().add(tmpSpellEffects2);
                    AnimActor animActor = new AnimActor(new AnimationCreator().makeAniamtion(AnimsTypes.BadSpellAnimation));
                    animActor.setPosition(tmpBohaterBroniacy.getX(), tmpBohaterBroniacy.getY());
                    Assets.stage01MapScreen.addActor(animActor);
                }

                System.out.println("Final Judge Effect: Atak +1 za każdy udany atak do końca tury ");
                tmpBohaterAtakujacy.getTempEffects().get(0).setEfektAtak(
                        tmpBohaterAtakujacy.getTempEffects().get(0).getEfektAtak() + 1);

                break;
        }
    }

    /**
     * @return
     */
    public int getDlugoscTrwaniaEfektu() {
        return dlugoscTrwaniaEfektu;
    }

    /**
     * @param dlugoscTrwaniaEfektu
     */
    public void setDlugoscTrwaniaEfektu(int dlugoscTrwaniaEfektu) {
        this.dlugoscTrwaniaEfektu = dlugoscTrwaniaEfektu;
    }

    /**
     * @return
     */
    public int getEfektAtak() {
        return efektAtak;
    }

    /**
     * @param efektAtak
     */
    public void setEfektAtak(int efektAtak) {
        this.efektAtak = efektAtak;
    }

    /**
     * @return
     */
    public int getEfektObrona() {
        return efektObrona;
    }

    /**
     * @param efektObrona
     */
    public void setEfektObrona(int efektObrona) {
        this.efektObrona = efektObrona;
    }

    /**
     * @return
     */
    public int getEfektSzybkosc() {
        return efektSzybkosc;
    }

    /**
     * @param efektSzybkosc
     */
    public void setEfektSzybkosc(int efektSzybkosc) {
        this.efektSzybkosc = efektSzybkosc;
    }

    /**
     * @return
     */
    public int getEfektHp() {
        return efektHp;
    }

    /**
     * @param efektHp
     */
    public void setEfektHp(int efektHp) {
        this.efektHp = efektHp;
    }

    /**
     * @return
     */
    public int getEfektMana() {
        return efektMana;
    }

    /**
     * @param efektMana
     */
    public void setEfektMana(int efektMana) {
        this.efektMana = efektMana;
    }

    /**
     * @return
     */
    public int getZmianaHp() {
        return zmianaHp;
    }

    /**
     * @param zmianaHp
     */
    public void setZmianaHp(int zmianaHp) {
        this.zmianaHp = zmianaHp;
    }

    /**
     * @return
     */
    public String getOpis() {
        return opis;
    }

    /**
     * @param opis
     */
    public void setOpis(String opis) {
        this.opis = opis;
    }

    /**
     * @return
     */
    public DefaultActor getIkona() {
        return ikona;
    }

    /**
     * @param ikona
     */
    public void setIkona(DefaultActor ikona) {
        this.ikona = ikona;
    }

    /**
     * Zwraca efekt zwiększenia obrażeń
     *
     * @return
     */
    public int getEfektDmg() {
        return efektDmg;
    }

    /**
     * Ustala efekt zwiększenia obrażeń
     *
     * @param efektDmg
     */
    public void setEfektDmg(int efektDmg) {
        this.efektDmg = efektDmg;
    }

    /**
     * Zwraca pancerz dla efektu.
     *
     * @return
     */
    public int getEfektArmor() {
        return efektArmor;
    }

    /**
     * Ustala pancerz dla efektu.
     *
     * @param efektArmor
     */
    public void setEfektArmor(int efektArmor) {
        this.efektArmor = efektArmor;
    }

    /**
     * Zwraca czy efekt dotyczy walki
     *
     * @return
     */
    public boolean isFightEffect() {
        return fightEffect;
    }

    /**
     * Ustala czy efekt dotyczy walki
     *
     * @param fightEffect
     */
    public void setFightEffect(boolean fightEffect) {
        this.fightEffect = fightEffect;
    }

    /**
     * Zwraca konkretny efekt walki
     *
     * @return
     */
    public FightEffects getFightEffects() {
        return fightEffects;
    }

    /**
     * Ustala konkretny efekt walki.
     *
     * @param fightEffects
     */
    public void setFightEffects(FightEffects fightEffects) {
        this.fightEffects = fightEffects;
    }

    /**
     * Zwraca czy efekt jest efektem trucizny.
     *
     * @return
     */
    public boolean isPosionEffect() {
        return posionEffect;
    }

    /**
     * Ustala czy efekt jest efektem trucizny.
     *
     * @param posionEffect
     */
    public void setPosionEffect(boolean posionEffect) {
        this.posionEffect = posionEffect;
    }
}
