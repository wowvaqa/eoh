package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.vs.enums.Spells;
import com.vs.enums.TypyTerenu;

/**
 * Klasa odpowiada za wyświetlanie interfejsu do rzucania czarów na odległość.
 *
 * @author v
 */
public class SpellCaster {

    Bohater bohaterCastujacy;
    SpellActor spell;
    private V v;

    public SpellCaster(Bohater bohaterCastujacy, SpellActor spell, V v) {
        this.v = v;
        this.bohaterCastujacy = bohaterCastujacy;
        this.spell = spell;

        int pozX = this.bohaterCastujacy.getPozXnaMapie();
        int pozY = this.bohaterCastujacy.getPozYnaMapie();

        // Sprawdza czy czar działa tylko na bohatera castującego
        if (spell.isSpellWorksOnlyForCaster()) {

            spell.getSpellEffects().get(0).dzialanie(spell, bohaterCastujacy, bohaterCastujacy, v);
            Gdx.input.setInputProcessor(Assets.stage01MapScreen);
            bohaterCastujacy.setMoveInterfaceOn(false);
            Ruch.wylaczPrzyciski();

            // Sprawdzenie czarów przyjacielskich
        } else if (spell.isSpellWorksOnlyForPlayersHeroes()) {
            System.out.println("Zaklęcie działa tylko na bohaterów gracza.");
            for (int i = pozX - 1 - spell.getZasieg(); i < pozX + 1 + 1 + spell.getZasieg(); i++) {
                for (int j = pozY - 1 - spell.getZasieg(); j < pozY + 1 + 1 + spell.getZasieg(); j++) {
                    if (i >= 0 && j >= 0 && i < v.getGs().getMapa().getIloscPolX() && j < v.getGs().getMapa().getIloscPolY()) {
                        if (sprawdzPrzyjaciela(i, j)) {
                            CastButton castButton = new CastButton(new TextureRegionDrawable(new TextureRegion(v.getA().spellIcon)), i, j);
                            castButton.setPosition(i * 100, j * 100);
                            Assets.stage01MapScreen.addActor(castButton);
                            bohaterCastujacy.setMoveInterfaceOn(false);
                            Ruch.wylaczPrzyciski();
                            Gdx.input.setInputProcessor(Assets.stage01MapScreen);
                        }
                    }
                }
            }
        } else if (spell.isSpellSummonSpell()) {
            Gdx.app.log("Zaklęcie przyzywające", "");
            for (int i = pozX - 1 - spell.getZasieg(); i < pozX + 1 + 1 + spell.getZasieg(); i++) {
                for (int j = pozY - 1 - spell.getZasieg(); j < pozY + 1 + 1 + spell.getZasieg(); j++) {
                    if (bohaterCastujacy.getPozXnaMapie() == i && bohaterCastujacy.getPozYnaMapie() == j) {
                        CastButtonCancel przyciskCancel = new CastButtonCancel(new TextureRegionDrawable(new TextureRegion(v.getA().cancelIcon)));
                        przyciskCancel.setPosition(i * 100, j * 100);
                        Assets.stage01MapScreen.addActor(przyciskCancel);
                        v.getGs().isSpellPanelActive = false;
                        Gdx.input.setInputProcessor(Assets.stage01MapScreen);
                        Ruch.wylaczPrzyciski();
                    } else {
                        if (i >= 0 && j >= 0 && i < v.getGs().getMapa().getIloscPolX() && j < v.getGs().getMapa().getIloscPolY()) {
                            if (!sprawdzPrzeciwnika(i, j) && !sprawdzPrzyjaciela(i, j) && v.getGs().getMapa().getPola()[i][j].getTypTerenu() != TypyTerenu.Gory) {
                                CastButton castButton = new CastButton(new TextureRegionDrawable(new TextureRegion(v.getA().spellIcon)), i, j);
                                castButton.setPosition(i * 100, j * 100);
                                Assets.stage01MapScreen.addActor(castButton);
                                v.getGs().isSpellPanelActive = false;
                                Gdx.input.setInputProcessor(Assets.stage01MapScreen);
                            }
                        }
                    }
                }
            }

        } else {
            for (int i = pozX - 1 - spell.getZasieg(); i < pozX + 1 + 1 + spell.getZasieg(); i++) {
                for (int j = pozY - 1 - spell.getZasieg(); j < pozY + 1 + 1 + spell.getZasieg(); j++) {
                    if (i >= 0 && j >= 0 && i < v.getGs().getMapa().getIloscPolX() && j < v.getGs().getMapa().getIloscPolY()) {

                        if (bohaterCastujacy.getPozXnaMapie() == i && bohaterCastujacy.getPozYnaMapie() == j) {
                            CastButtonCancel przyciskCancel = new CastButtonCancel(new TextureRegionDrawable(new TextureRegion(v.getA().cancelIcon)));
                            przyciskCancel.setPosition(i * 100, j * 100);
                            Assets.stage01MapScreen.addActor(przyciskCancel);
                            v.getGs().isSpellPanelActive = false;
                            Gdx.input.setInputProcessor(Assets.stage01MapScreen);
                            Ruch.wylaczPrzyciski();
                        } else {
                            if (sprawdzPrzeciwnika(i, j)) {

                                // Potrzebny warunek sprawdzający czy w polu znajduje się przeciwnik
                                CastButton castButton = new CastButton(new TextureRegionDrawable(new TextureRegion(v.getA().spellIcon)), i, j);
                                castButton.setPosition(i * 100, j * 100);
                                Assets.stage01MapScreen.addActor(castButton);
                                v.getGs().isSpellPanelActive = false;
                                Gdx.input.setInputProcessor(Assets.stage01MapScreen);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void wylaczPrzyciski() {
        int rozmiar = Assets.stage01MapScreen.getActors().size;
        // Infromuje czy wśród aktorów stage 01 są jeszcze przyciski
        boolean czySaPrzyciski;

        do {
            czySaPrzyciski = false;
            for (int i = 0; i < rozmiar; i++) {
                if (Assets.stage01MapScreen.getActors().get(i).getClass() == SpellCaster.CastButton.class) {
                    Assets.stage01MapScreen.getActors().removeIndex(i);
                    rozmiar = Assets.stage01MapScreen.getActors().size;
                }
            }
            for (int i = 0; i < Assets.stage01MapScreen.getActors().size; i++) {
                if (Assets.stage01MapScreen.getActors().get(i).getClass() == SpellCaster.CastButton.class) {
                    czySaPrzyciski = true;
                }
            }
        } while (czySaPrzyciski);

        do {
            czySaPrzyciski = false;
            for (int i = 0; i < rozmiar; i++) {
                if (Assets.stage01MapScreen.getActors().get(i).getClass() == SpellCaster.CastButtonCancel.class) {
                    Assets.stage01MapScreen.getActors().removeIndex(i);
                    rozmiar = Assets.stage01MapScreen.getActors().size;
                }
            }
            for (int i = 0; i < rozmiar; i++) {
                czySaPrzyciski = Assets.stage01MapScreen.getActors().get(i).getClass() == SpellCaster.CastButtonCancel.class;
            }
        } while (czySaPrzyciski);

    }

    /**
     * Zwraca TREU jeżeli w zadanym parametrami polu jest przeciwnik, tj.: mob,
     * wrogi zamek lub bohater.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean sprawdzPrzeciwnika(int x, int y) {
        if (v.getGs().getMapa().getPola()[x][y].getBohater() != null
                && v.getGs().getMapa().getPola()[x][y].getBohater().getPrzynaleznoscDoGracza() != v.getGs().getTuraGracza()) {
            return true;
        }
        /**
         * Zwraca true jeżeli napotkany zamek nie należy do gracza i jego poziom
         * HP > 0
         */
        if (v.getGs().getMapa().getPola()[x][y].getCastle() != null
                && v.getGs().getMapa().getPola()[x][y].getCastle().getPrzynaleznoscDoGracza() != v.getGs().getTuraGracza()
                && v.getGs().getMapa().getPola()[x][y].getCastle().getActualHp() > 0) {
            return true;
        }
        return v.getGs().getMapa().getPola()[x][y].getMob() != null;
    }

    /**
     * Zwraca True jeżeli w zdanaym parametrami polu mapy znajduje sie
     * przyjacielski bohater.
     *
     * @param x
     * @param y
     * @return
     */
    private boolean sprawdzPrzyjaciela(int x, int y) {
        return v.getGs().getMapa().getPola()[x][y].getBohater() != null
                && v.getGs().getMapa().getPola()[x][y].getBohater().getPrzynaleznoscDoGracza() == v.getGs().getTuraGracza();
    }

    /**
     * Klasa definiuje przycisk rzucania czaru.
     */
    public class CastButton extends ImageButton {

        private final int locX;
        private final int locY;

        /**
         * Tworzy przycisk rzucania czaru.
         *
         * @param imageUp Obiekt drawable tekstury przycisku
         * @param locX    lokacja X na mapie
         * @param locY    lokacja Y na mapie
         */
        public CastButton(Drawable imageUp, int locX, int locY) {
            super(imageUp);
            this.locX = locX;
            this.locY = locY;
        }

        @Override
        public boolean addListener(EventListener listener) {
            return super.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Przycisk cast kliknięty");

                    spell.setSpellX(locX);
                    spell.setSpellY(locY);

                    if (v.getGs().getMapa().getPola()[locX][locY].getBohater() != null) {
                        spell.getSpellEffects().get(0).dzialanie(spell, v.getGs().getMapa().getPola()[locX][locY].getBohater(), bohaterCastujacy, v);
                    } else if (v.getGs().getMapa().getPola()[locX][locY].getMob() != null) {
                        spell.getSpellEffects().get(0).dzialanie(spell, v.getGs().getMapa().getPola()[locX][locY].getMob(), bohaterCastujacy, v);
                    } else if (v.getGs().getMapa().getPola()[locX][locY].getCastle() != null) {
                        spell.getSpellEffects().get(0).dzialanie(spell, v.getGs().getMapa().getPola()[locX][locY].getCastle(), bohaterCastujacy, v);
                    } else {
                        spell.getSpellEffects().get(0).dzialanie(spell, null, bohaterCastujacy, v);
                    }

                    wylaczPrzyciski();
                    bohaterCastujacy.setMoveInterfaceOn(false);
                }
            });
        }
    }

    /**
     * Klasa definiuje przycisk anulowania rzucania czaru.
     */
    public class CastButtonCancel extends ImageButton {

        /**
         * Tworzy przycisk anulowania rzucania czaru.
         *
         * @param imageUp Obiekt drawable tekstury przycisku
         */
        public CastButtonCancel(Drawable imageUp) {
            super(imageUp);
        }

        @Override
        public boolean addListener(EventListener listener) {
            return super.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SpellCaster.wylaczPrzyciski();
                    bohaterCastujacy.setMoveInterfaceOn(false);
                }
            });
        }
    }
}
