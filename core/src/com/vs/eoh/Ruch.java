package com.vs.eoh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.vs.enums.KlasyPostaci;
import com.vs.network.Network;
import com.vs.screens.MapScreen;
import com.vs.screens.MultiplayerScreen;

/**
 * Klasa odpowiada za ruch, obsługe przycisków ruchu i ataku.
 *
 * @author v
 */
public class Ruch {

    private final Bohater bohater;
    private final Assets a;
    private final GameStatus gs;

    /**
     *
     * @param bohater Referencja do obiektu klasy Bohater, którego dot. ruch.
     * @param a Referencja do obiketu klasy Assets
     * @param gs Refernecja do obiektu klasy GameStatus
     */
    public Ruch(Bohater bohater, Assets a, GameStatus gs) {
        this.bohater = bohater;
        this.a = a;
        this.gs = gs;

        int pozX = this.bohater.getPozXnaMapie();
        int pozY = this.bohater.getPozYnaMapie();

        for (int i = pozX - 1; i < pozX + 1 + 1; i++) {
            for (int j = pozY - 1; j < pozY + 1 + 1; j++) {
                if (i >= 0 && j >= 0 && i < gs.getMapa().getIloscPolX() && j < gs.getMapa().getIloscPolY()) {

                    if (bohater.getPozXnaMapie() == i && bohater.getPozYnaMapie() == j) {
                        przyciskCancel przyciskCancel = new przyciskCancel(new TextureRegionDrawable(new TextureRegion(a.cancelIcon)), bohater);
                        przyciskCancel.setPosition(i * 100, j * 100);
                        Assets.stage01MapScreen.addActor(przyciskCancel);
                    } else {
                        if (gs.getMapa().getPola()[i][j].isMovable()) {
                            if (sprawdzPrzeciwnika(i, j) == false) {
                                // Warunek sprawdza czy na polu znajduje się inny bohater gracza. Jeżeli TRUE wtedy na tym polu nie zostanie utworzony przycisk ruchu.
                                if (!(gs.getMapa().getPola()[i][j].getBohater() != null && gs.getMapa().getPola()[i][j].getBohater().getPrzynaleznoscDoGracza() == gs.getTuraGracza())) {
                                    PrzyciskRuchu przyciskRuchu = new PrzyciskRuchu(new TextureRegionDrawable(new TextureRegion(a.moveIcon)), i, j, bohater, this);
                                    przyciskRuchu.setPosition(i * 100, j * 100);
                                    Assets.stage01MapScreen.addActor(przyciskRuchu);

                                }
                            }
                        }
                    }
                }
            }
        }

        int zasiegPrawy = this.bohater.getItemPrawaReka().getZasieg();
        int zasiegLewy = this.bohater.getItemLewaReka().getZasieg();
        int zasieg;
        if (zasiegLewy >= zasiegPrawy) {
            zasieg = zasiegLewy;
        } else {
            zasieg = zasiegPrawy;
        }

        for (int i = pozX - 1 - zasieg; i < pozX + 1 + 1 + zasieg; i++) {
            for (int j = pozY - 1 - zasieg; j < pozY + 1 + 1 + zasieg; j++) {
                if (i >= 0 && j >= 0 && i < gs.getMapa().getIloscPolX() && j < gs.getMapa().getIloscPolY()) {

                    if (sprawdzPrzeciwnika(i, j)) {
                        przyciskAtaku przyciskAtaku = new przyciskAtaku(new TextureRegionDrawable(new TextureRegion(a.attackIcon)), i, j, bohater, this.a);
                        przyciskAtaku.setPosition(i * 100, j * 100);
                        Assets.stage01MapScreen.addActor(przyciskAtaku);
                    }
                }
            }
        }
    }

    /**
     * Wyłącza przycisku Ruchu, Ataku, Cancel na Stage01
     */
    public static void wylaczPrzyciski() {
        int rozmiar;// = Assets.stage01MapScreen.getActors().size;
        // Infromuje czy wśród aktorów stage 01 są jeszcze przyciski
        boolean czySaPrzyciskiRuchu;
        boolean czySaPrzyciskiAtaku;
        boolean czySaPrzyciskiCancel;

        // pętla usuwająca przyciski musi być powtórzona 10x, w przeciwnym wypadku nie znikają
        // wszystkie przyciski ataku.
        for (int x = 0; x < 10; x ++) {
            do {
                rozmiar = Assets.stage01MapScreen.getActors().size;
                czySaPrzyciskiRuchu = false;
                for (int i = 0; i < rozmiar; i++) {
                    if (Assets.stage01MapScreen.getActors().get(i).getClass() == PrzyciskRuchu.class) {
                        Assets.stage01MapScreen.getActors().removeIndex(i);
                        rozmiar = Assets.stage01MapScreen.getActors().size;
                    }
                }
                for (int i = 0; i < Assets.stage01MapScreen.getActors().size; i++) {
                    //czySaPrzyciski = Assets.stage01MapScreen.getActors().get(i).getClass() == PrzyciskRuchu.class;
                    if (Assets.stage01MapScreen.getActors().get(i).getClass() == PrzyciskRuchu.class) {
                        czySaPrzyciskiRuchu = true;
                    }
                }
            } while (czySaPrzyciskiRuchu);

            do {
                rozmiar = Assets.stage01MapScreen.getActors().size;
                czySaPrzyciskiCancel = false;
                for (int i = 0; i < rozmiar; i++) {
                    if (Assets.stage01MapScreen.getActors().get(i).getClass() == przyciskCancel.class) {
                        Assets.stage01MapScreen.getActors().removeIndex(i);
                        rozmiar = Assets.stage01MapScreen.getActors().size;
                    }
                }
                for (int i = 0; i < rozmiar; i++) {
                    czySaPrzyciskiCancel = Assets.stage01MapScreen.getActors().get(i).getClass() == przyciskCancel.class;
                }
            } while (czySaPrzyciskiCancel);

            do {
                rozmiar = Assets.stage01MapScreen.getActors().size;
                czySaPrzyciskiAtaku = false;
                for (int i = 0; i < rozmiar; i++) {
                    if (Assets.stage01MapScreen.getActors().get(i).getClass() == przyciskAtaku.class) {
                        Assets.stage01MapScreen.getActors().removeIndex(i);
                        rozmiar = Assets.stage01MapScreen.getActors().size;
                    }
                }
                for (int i = 0; i < rozmiar; i++) {
                    czySaPrzyciskiAtaku = Assets.stage01MapScreen.getActors().get(i).getClass() == przyciskAtaku.class;
                }
            } while (czySaPrzyciskiAtaku);
        }
    }

    /**
     * Wyłącza ikony efektów w stage 02.
     */
    public static void wylaczIkonyEfektow() {
        int indeksEfektu = 999;

        MapScreen.mapScreen.tables.formatEffetsBarTable();

        for (int j = 0; j < Assets.stage02MapScreen.getActors().size; j++) {
            if (Assets.stage02MapScreen.getActors().get(j).getClass() == EffectActor.class) {
                indeksEfektu = j;
            }

            if (indeksEfektu != 999) {
                Assets.stage02MapScreen.getActors().removeIndex(indeksEfektu);
                Ruch.wylaczIkonyEfektow();
            }
        }
    }

    /**
     * Wykonuje ruch otrzymany z sieci
     *
     * @param b  Referencja do obiektu bohatera
     * @param gs Referencja do obiektu Game Status
     */
    public static void makeNetworkMove(Bohater b, GameStatus gs, int ruchX, int ruchY) {
        gs.getMapa().pola[b.getPozXnaMapie()][b.getPozYnaMapie()].setBohater(null);

        b.addAction(Actions.moveBy(ruchX * 100, ruchY * 100, 0.25f));
        b.setPozXnaMapie(b.getPozXnaMapie() + ruchX);
        b.setPozYnaMapie(b.getPozYnaMapie() + ruchY);

        gs.getMapa().pola[b.getPozXnaMapie()][b.getPozYnaMapie()].setBohater(b);
    }

    /**
     * Zwraca True jeżeli w zadanej lokalizacji znajduje się obiekt kalsy Mob,
     * Bohater lub Castle
     *
     * @param x Pozycja X obiektu do sprawdzenia na mapie
     * @param y Pozycja Y obiektu do sprawdzenia na mapie
     * @return
     */
    private boolean sprawdzPrzeciwnika(int x, int y) {
        if (gs.getMapa().getPola()[x][y].getBohater() != null
                && gs.getMapa().getPola()[x][y].getBohater().getPrzynaleznoscDoGracza() != gs.getTuraGracza()) {
            return true;
        }
        /**
         * Zwraca true jeżeli napotkany zamek nie należy do gracza i jego poziom
         * HP > 0
         */
        if (gs.getMapa().getPola()[x][y].getCastle() != null
                && gs.getMapa().getPola()[x][y].getCastle().getPrzynaleznoscDoGracza() != gs.getTuraGracza()
                && gs.getMapa().getPola()[x][y].getCastle().getActualHp() > 0) {
            return true;
        }
        return gs.getMapa().getPola()[x][y].getMob() != null;
    }

    /**
     * Sprawdza czy nadepnięto na skrzynię ze skarbem.
     */
    public void checkTresureBox() {
        if (gs.getMapa().getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].getTresureBox() != null) {
            System.out.println("Nadepnięto na skrzynkę ze skarbem");
            a.pokazInfoWindow(gs.getMapa().getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].getTresureBox(), bohater, this.gs);
            bohater.setOtwartaSkrzyniaZeSkarbem(true);
        }
    }

    /**
     * Sprawdza czy nadepnięto na zamek.
     */
    private void checkCastle() {
        if (gs.getMapa().getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].getCastle() != null) {
            System.out.println(gs.getTuraGracza() + " Nadepnięto na zamek :-)");
            gs.getMapa().getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].getCastle().setPrzynaleznoscDoGracza(bohater.getPrzynaleznoscDoGracza());
            gs.getMapa().getPola()[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].getCastle().aktualizujIkoneZamku();

            System.out.println("Zamek zmienił włąsciciela");
        }
    }

    /**
     * Przycisk ruchu bohatera
     */
    public class PrzyciskRuchu extends ImageButton {

        private final int locX;
        private final int locY;

        private final float ruchX;
        private final float ruchY;

        private final Bohater bohater;

        private final Ruch ruch;

        /**
         * @param imageUp Tekstura przycisku
         * @param locX Lokacja X przycisku na mapie
         * @param locY Lokacja Y przycisku na mapie
         * @param bohater Referencja do bohatera który się porusza
         * @param ruch
         */
        public PrzyciskRuchu(Drawable imageUp, int locX, int locY, Bohater bohater, Ruch ruch) {
            super(imageUp);
            this.locX = locX;
            this.locY = locY;
            this.bohater = bohater;
            this.ruch = ruch;

            ruchX = this.locX - this.bohater.getPozXnaMapie();
            ruchY = this.locY - this.bohater.getPozYnaMapie();
        }

        @Override
        public boolean addListener(EventListener listener) {
            return super.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    wykonajRuch();
                    if (!bohater.getKlasyPostaci().equals(KlasyPostaci.Summmon)) {
                        ruch.checkTresureBox();
                    }
                    ruch.checkCastle();
                }
            });
        }

        /**
         * Wykonuje ruch na mapie bohatera
         */
        private void wykonajRuch() {

            if (gs.getBohaterZaznaczony().getSpellEffects().size() > 0) {
                if (gs.getBohaterZaznaczony().getSpellEffects().get(0).isPosionEffect()) {
                    Gdx.app.log("Wykryto Poison Effect", "ot co");
                    int damage = gs.getBohaterZaznaczony().getSpellEffects().get(0).getEfektDmg();
                    gs.getBohaterZaznaczony().setActualHp(gs.getBohaterZaznaczony().getActualHp() - damage);
                    a.animujLblDamage(gs.getBohaterZaznaczony().getX() + 50, gs.getBohaterZaznaczony().getY() + 50, Integer.toString(damage));
                }
            }

            // polecenia wykonają się tylko jeżeli gra jest klientem
            if (gs.getNetworkStatus() == 2) {
                Network.Move networkMove = new Network.Move();
                networkMove.ruchX = (int) ruchX;
                networkMove.ruchY = (int) ruchY;
                networkMove.player = bohater.getPrzynaleznoscDoGracza();
                networkMove.hero = Bohater.getHeroNumberInArrayList(bohater, gs.getGracze().get(
                        bohater.getPrzynaleznoscDoGracza()
                ));
                GameStatus.client.getCnt().sendTCP(networkMove);
                Gdx.app.log("wykonajRuch", "wysyłam networkMove");
            }

            gs.getMapa().pola[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].setBohater(null);

            bohater.addAction(Actions.moveBy(ruchX * 100, ruchY * 100, 0.25f));
            bohater.setPozXnaMapie(bohater.getPozXnaMapie() + (int) ruchX);
            bohater.setPozYnaMapie(bohater.getPozYnaMapie() + (int) ruchY);

//            Assets.netStatus.pozXboh1 = gs.getGracze().get(0).getBohaterowie().get(0).getPozXnaMapie();
//            Assets.netStatus.pozYboh1 = gs.getGracze().get(0).getBohaterowie().get(0).getPozYnaMapie();
//
//            Assets.netStatus.pozXboh2 = gs.getGracze().get(1).getBohaterowie().get(0).getPozXnaMapie();
//            Assets.netStatus.pozYboh2 = gs.getGracze().get(1).getBohaterowie().get(0).getPozYnaMapie();

            //Assets.client.sendTCP(Assets.netStatus);

            gs.getMapa().pola[bohater.getPozXnaMapie()][bohater.getPozYnaMapie()].setBohater(bohater);

            bohater.getSprite().setTexture(bohater.getBohaterTex());
            bohater.setZaznaczony(false);
            bohater.setPozostaloRuchow(bohater.getPozostaloRuchow() - 1);

            gs.setCzyZaznaczonoBohatera(false);

            Ruch.wylaczIkonyEfektow();
            Ruch.wylaczPrzyciski();
        }
    }

    /**
     * Przycisk ataku bohatera
     */
    public class przyciskAtaku extends ImageButton {

        private final int locX;
        private final int locY;

        private final Bohater bohater;
        private final Assets a;

        /**
         * Tworzy przycisk ataku i umieszca go zadanej lokalizacji na stage
         *
         * @param imageUp Tekstura (drawable)
         * @param locX lokacja X przycisku na Stage
         * @param locY lokacja Y przycisku na Stage
         * @param bohater bohater którego dotyczy przycisk
         * @param a Referencja do obiektu klasy Assets
         */
        public przyciskAtaku(Drawable imageUp, int locX, int locY, Bohater bohater, Assets a) {
            super(imageUp);
            this.locX = locX;
            this.locY = locY;
            this.bohater = bohater;
            this.a = a;
        }

        /**
         * Wykonuje atak
         */
        private void wykonajAtak() {

            if (gs.getBohaterZaznaczony().getSpellEffects().size() > 0) {
                if (gs.getBohaterZaznaczony().getSpellEffects().get(0).isPosionEffect()) {
                    Gdx.app.log("Wykryto Poison Effect", "ot co");
                    int damage = gs.getBohaterZaznaczony().getSpellEffects().get(0).getEfektDmg();
                    gs.getBohaterZaznaczony().setActualHp(gs.getBohaterZaznaczony().getActualHp() - damage);
                    a.animujLblDamage(gs.getBohaterZaznaczony().getX() + 50, gs.getBohaterZaznaczony().getY() + 50, Integer.toString(damage));
                }
            }

            if (sprawdzPrzeciwnika(locX, locY).getClass() == Mob.class) {
                System.out.println("Atak na moba");
                this.a.animujLblDmg(this.locX * 100 + 50, this.locY * 100, this.bohater,
                        gs.getMapa().getPola()[locX][locY].getMob());
            } else if (sprawdzPrzeciwnika(locX, locY).getClass() == Bohater.class) {
                System.out.println("Atak na Bohatera");
                this.a.animujLblDmg(this.locX * 100 + 50, this.locY * 100, this.bohater,
                        gs.getMapa().getPola()[locX][locY].getBohater());
            } else if (sprawdzPrzeciwnika(locX, locY).getClass() == Castle.class) {
                System.out.println("Atak na Zamek");
                this.a.animujLblDmg(this.locX * 100 + 50, this.locY * 100, this.bohater,
                        gs.getMapa().getPola()[locX][locY].getCastle());
            }

            //a.swordSound.play();

            this.bohater.getSprite().setTexture(bohater.getBohaterTex());
            this.bohater.setZaznaczony(false);

            gs.setCzyZaznaczonoBohatera(false);

            gs.usunMartweMoby();
            Ruch.wylaczIkonyEfektow();
            Ruch.wylaczPrzyciski();
        }

        /**
         * Zwraca Obiekt jeżeli w zadanym Polu znajduje się obiekt klasy Mob,
         * Bohater lub Castle
         *
         * @param x Lokacja X obiektu do sprawdzenia.
         * @param y Lokacja Y obiektu do sprawdzenia.
         * @return
         */
        private Object sprawdzPrzeciwnika(int x, int y) {
            if (gs.getMapa().getPola()[x][y].getMob() != null) {
                System.out.println("Wykryto moba");
                return gs.getMapa().getPola()[x][y].getMob();
            }
            if (gs.getMapa().getPola()[x][y].getBohater() != null) {
                System.out.println("Wykryto Bohatera");
                return gs.getMapa().getPola()[x][y].getBohater();
            }
            if (gs.getMapa().getPola()[x][y].getCastle() != null) {
                System.out.println("Wykryto Bohatera");
                return gs.getMapa().getPola()[x][y].getCastle();
            }
            return null;
        }

        @Override
        public boolean addListener(EventListener listener) {
            return super.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Przycisk ataku kliknięty");
                    wykonajAtak();
                }
            });
        }
    }

    /**
     * Przycisk Anulacji ruchów bohatera
     */
    public class przyciskCancel extends ImageButton {

        private final Bohater bohater;

        /**
         *
         * @param imageUp Obrazek przycisku
         * @param bohater Referencja do obiektu klasy Bohater którego dotyczy
         * przycisk
         */
        public przyciskCancel(Drawable imageUp, Bohater bohater) {
            super(imageUp);

            this.bohater = bohater;
        }

        @Override
        public boolean addListener(EventListener listener) {
            return super.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    wykonajRuchCancel();
                }
            });
        }

        /**
         * Wykonuje czynność anulacji ruchu.
         */
        private void wykonajRuchCancel() {
            this.bohater.getSprite().setTexture(bohater.getBohaterTex());
            this.bohater.setZaznaczony(false);

            Ruch.wylaczIkonyEfektow();
            Ruch.wylaczPrzyciski();

        }
    }
}
