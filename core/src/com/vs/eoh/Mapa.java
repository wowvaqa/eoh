package com.vs.eoh;

// Klasa Mapa przechowuje obiekty Klasy Pole
import java.io.Serializable;
import java.util.ArrayList;

import com.vs.ai.PathFinder;
import com.vs.enums.TypyTerenu;
import com.vs.network.Network;

public final class Mapa implements Serializable {

    public Pole[][] pola;
    private int iloscPolX = 10;
    private int iloscPolY = 10;

    /**
     * Tworzy mapę domyślną 10x10 pól
     */
    public Mapa() {
        this.generujMape(iloscPolX, iloscPolY);
    }

    /**
     * Tworzy mapę z zadeklarowanych wartości ilości pól.
     *
     * @param iloscPolX Ilość pól w osi X
     * @param iloscPolY Ilość pól w osi Y
     */
    public Mapa(int iloscPolX, int iloscPolY) {
        this.iloscPolX = iloscPolX;
        this.iloscPolY = iloscPolY;
        this.generujMape(this.iloscPolX, this.iloscPolY);

        PathFinder.openLink = new ArrayList<Pole>();
        PathFinder.closedLinks = new ArrayList<Pole>();
    }

    /**
     * Zwraca w postaci Stringa odpowiednią nazwę TextureRegiona rzeki.
     *
     * @param x Współżędne X pola na mapie.
     * @param y Współżędne X pola na mapie.
     * @param mapa Referencja do obiektu mapy.
     * @param tT Referencja do TypuTerenu
     * @return
     */
    public static String getTextureRegion(int x, int y, Mapa mapa, TypyTerenu tT) {

        // Prawy górny róg mapy
        if ("PG".equals(getPartOfMap(x, y, mapa))) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverNS";
            }
        } // Lewy górny róg mapy
        else if ("LG".equals(getPartOfMap(x, y, mapa))) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverNS";
            }
        } // Prawy dolny róg mapy
        else if ("PD".equals(getPartOfMap(x, y, mapa))) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverNS";
            }
        } // Lewy dolny róg mapy
        else if ("LD".equals(getPartOfMap(x, y, mapa))) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverNS";
            }
        } // Góra MAPY
        else if ("G".equals(getPartOfMap(x, y, mapa))) {
            //SOUTH
            if (mapa.getPola()[x][y - 1].getTypTerenu() == tT) {
                if (tT == TypyTerenu.Drzewo) {
                    return "forestS";
                } else if (tT == TypyTerenu.Gory) {
                    return "mountainC";
                } else {
                    return "riverNS";
                }
            }
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverNS";
            }
            // Dół MAPY
        } else if ("D".equals(getPartOfMap(x, y, mapa))) {
            if (mapa.getPola()[x][y + 1].getTypTerenu() == tT) {
                if (tT == TypyTerenu.Drzewo) {
                    return "forestN";
                } else if (tT == TypyTerenu.Gory) {
                    return "mountainN";
                } else {
                    return "riverNS";
                }
            }
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverNS";
            }
            // Lewa krawędź MAPY
        } else if ("L".equals(getPartOfMap(x, y, mapa))) {
            if (mapa.getPola()[x + 1][y].getTypTerenu() == tT) {
                if (tT == TypyTerenu.Drzewo) {
                    return "forestE";
                } else if (tT == TypyTerenu.Gory) {
                    return "mountainE";
                } else {
                    return "riverWE";
                }
            }
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverWE";
            }
            // Prawa krawędź MAPY
        } else if ("P".equals(getPartOfMap(x, y, mapa))) {
            if (mapa.getPola()[x - 1][y].getTypTerenu() == tT) {
                if (tT == TypyTerenu.Drzewo) {
                    return "forestW";
                } else if (tT == TypyTerenu.Gory) {
                    return "mountainW";
                } else {
                    return "riverWE";
                }
            }
            if (tT == TypyTerenu.Drzewo) {
                return "forestC";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainC";
            } else {
                return "riverWE";
            }
            // Środek mapy
        } else if ("C".equals(getPartOfMap(x, y, mapa))) {
            return getRegionC(x, y, mapa, tT);
        }
        if (tT == TypyTerenu.Drzewo) {
            return "forestC";
        } else if (tT == TypyTerenu.Gory) {
            return "mountainC";
        } else {
            return "riverNS";
        }
    }

    private static String getRegionC(int x, int y, Mapa mapa, TypyTerenu tT) {
        // NORTH & SOUTH & WEST & EAST
        if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT
                && mapa.getPola()[x + 1][y].getTypTerenu() == tT
                && mapa.getPola()[x - 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNSWE";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNSWE";
            } else {
                return "riverNS";
            }
        } // NORTH & SOUTH & EAST
        else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT
                && mapa.getPola()[x + 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNSE";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNSE";
            } else {
                return "riverNS";
            }
            // NORT & SOUT & WEST
        } else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT
                && mapa.getPola()[x - 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNSW";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNSW";
            } else {
                return "riverNS";
            }
            // NORT & WEST & EAST
        } else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x + 1][y].getTypTerenu() == tT
                && mapa.getPola()[x - 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNWE";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNWE";
            } else {
                return "riverNS";
            }
            // WEST & EAST & SOUTH
        } else if (mapa.getPola()[x - 1][y].getTypTerenu() == tT
                && mapa.getPola()[x + 1][y].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestWES";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainWES";
            } else {
                return "riverNS";
            }
        } // NORTH & SOUTH
        else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNS";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNS";
            } else {
                return "riverNS";
            }
            // NORTH & EAST
        } else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x + 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNE";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNE";
            } else {
                return "riverNE";
            }
            // NORTH & WEST
        } else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT
                && mapa.getPola()[x - 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestNW";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainNW";
            } else {
                return "riverNW";
            }
            // WEST & SOUTH
        } else if (mapa.getPola()[x - 1][y].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestWS";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainWS";
            }
            return "riverWS";
            // EAST & SOUTH
        } else if (mapa.getPola()[x + 1][y].getTypTerenu() == tT
                && mapa.getPola()[x][y - 1].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestES";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainES";
            } else {
                return "riverES";
            }
            // WEST & EAST
        } else if (mapa.getPola()[x - 1][y].getTypTerenu() == tT
                && mapa.getPola()[x + 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestWE";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainWE";
            } else {
                return "riverWE";
            }
            // NORTH
        } else if (mapa.getPola()[x][y + 1].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestN";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainN";
            } else {
                return "riverNS";
            }
            // SOUTH
        } else if (mapa.getPola()[x][y - 1].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestS";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainS";
            } else {
                return "riverNS";
            }
            // WEST
        } else if (mapa.getPola()[x - 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestW";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainW";
            } else {
                return "riverNS";
            }
            // EAST
        } else if (mapa.getPola()[x + 1][y].getTypTerenu() == tT) {
            if (tT == TypyTerenu.Drzewo) {
                return "forestE";
            } else if (tT == TypyTerenu.Gory) {
                return "mountainE";
            } else {
                return "riverNS";
            }
        }
        if (tT == TypyTerenu.Drzewo) {
            return "forestC";
        } else if (tT == TypyTerenu.Gory) {
            return "mountainC";
        } else {
            return "riverNS";
        }
    }

    /**
     * Zwraca Stringa informujacego w jakiej części mapy znajduje się pole.
     *
     * @param x    Współżędna X pola na mapie.
     * @param y    Współżędna Y pola na mapie.
     * @param mapa Referencja do obiektu mapy.
     * @return D - dolna krawędź mapy. G - Górna krawędź mapy. S - Środek mapy.
     * L - Lewa krawędź mapy. P - Prawa krawędź mapy
     */
    private static String getPartOfMap(int x, int y, Mapa mapa) {
        if (x == 0 && y == 0) {
            System.out.println("Lewy Dolny róg mapy.");
            return "LD";
        } else if (x == mapa.getIloscPolX() - 1 && y == 0) {
            System.out.println("Prawy Dolny róg mapy.");
            return "PD";
        } else if (x == mapa.getIloscPolX() - 1 && y == mapa.getIloscPolY() - 1) {
            System.out.println("Prawy Górny róg mapy.");
            return "PG";
        } else if (x == 0 && y == mapa.getIloscPolY() - 1) {
            System.out.println("Lewy Górny róg mapy.");
            return "LG";
        } else if (x == 0) {
            System.out.println("Lewa krawędź mapy.");
            return "L";
        } else if (x == mapa.getIloscPolX() - 1) {
            System.out.println("Prawa krawędź mapy.");
            return "P";
        } else if (y == 0) {
            System.out.println("Dolna krawędź mapy.");
            return "D";
        } else if (y == mapa.getIloscPolY() - 1) {
            System.out.println("Górna krawędź mapy.");
            return "G";
        }
        return "C";
    }

    /**
     * Konwertuje Mapę do networkMapy
     *
     * @param mapa   Referencja do obiektu mapy
     * @param netMap Referencja do obiketu Network.Map
     */
    public static void convertToNetworkMap(Mapa mapa, Network.NetworkMap netMap) {
        for (int i = 0; i < mapa.getIloscPolX(); i++) {
            for (int j = 0; j < mapa.getIloscPolY(); j++) {

                if (mapa.getPola()[i][j].isLokacjaStartowaP1()) {
                    netMap.networkPole[i][j].isPlayer1Start = true;
                } else if (mapa.getPola()[i][j].isLokacjaStartowaP2()) {
                    netMap.networkPole[i][j].isPlayer2Start = true;
                } else if (mapa.getPola()[i][j].isLokacjaStartowaP3()) {
                    netMap.networkPole[i][j].isPlayer3Start = true;
                } else if (mapa.getPola()[i][j].isLokacjaStartowaP4()) {
                    netMap.networkPole[i][j].isPlayer4Start = true;
                }

                if (mapa.getPola()[i][j].isMob1Location()) {
                    netMap.networkPole[i][j].isMobLevel1 = true;
                } else if (mapa.getPola()[i][j].isMob2Location()) {
                    netMap.networkPole[i][j].isMobLevel2 = true;
                }

                if (mapa.getPola()[i][j].isTresureBox1Location()) {
                    netMap.networkPole[i][j].isTresureBoxLevel1 = true;
                } else if (mapa.getPola()[i][j].isTresureBox2Location()) {
                    netMap.networkPole[i][j].isTresureBoxLevel2 = true;
                }

                if (mapa.getPola()[i][j].getTypTerenu().equals(TypyTerenu.Trawa)) {
                    netMap.networkPole[i][j].isTerrainType1 = true;
                } else if (mapa.getPola()[i][j].getTypTerenu().equals(TypyTerenu.Gory)) {
                    netMap.networkPole[i][j].isTerrainType2 = true;
                } else if (mapa.getPola()[i][j].getTypTerenu().equals(TypyTerenu.Drzewo)) {
                    netMap.networkPole[i][j].isTerrainType3 = true;
                } else if (mapa.getPola()[i][j].getTypTerenu().equals(TypyTerenu.Rzeka)) {
                    netMap.networkPole[i][j].isTerrainType4 = true;
                }
            }
        }
    }

    /**
     * Konwertuje z NetworkMap do Map
     *
     * @param mapa
     * @param netMap
     */
    public static void convertTokMap(Mapa mapa, Network.NetworkMap netMap) {
        for (int i = 0; i < netMap.amountX; i++) {
            for (int j = 0; j < netMap.amountY; j++) {

                if (netMap.networkPole[i][j].isPlayer1Start) {
                    mapa.getPola()[i][j].setLokacjaStartowaP1(true);
                } else if (netMap.networkPole[i][j].isPlayer2Start) {
                    mapa.getPola()[i][j].setLokacjaStartowaP2(true);
                } else if (netMap.networkPole[i][j].isPlayer3Start) {
                    mapa.getPola()[i][j].setLokacjaStartowaP3(true);
                } else if (netMap.networkPole[i][j].isPlayer4Start) {
                    mapa.getPola()[i][j].setLokacjaStartowaP4(true);
                }

                if (netMap.networkPole[i][j].isMobLevel1) {
                    mapa.getPola()[i][j].setMob1Location(true);
                } else if (netMap.networkPole[i][j].isMobLevel2) {
                    mapa.getPola()[i][j].setMob2Location(true);
                }

                if (netMap.networkPole[i][j].isTresureBoxLevel1) {
                    mapa.getPola()[i][j].setTresureBox1Location(true);
                } else if (netMap.networkPole[i][j].isTresureBoxLevel2) {
                    mapa.getPola()[i][j].setTresureBox2Location(true);
                }

                if (netMap.networkPole[i][j].isTerrainType1) {
                    mapa.getPola()[i][j].setTypTerenu(TypyTerenu.Trawa);
                } else if (netMap.networkPole[i][j].isTerrainType2) {
                    mapa.getPola()[i][j].setTypTerenu(TypyTerenu.Gory);
                } else if (netMap.networkPole[i][j].isTerrainType3) {
                    mapa.getPola()[i][j].setTypTerenu(TypyTerenu.Drzewo);
                } else if (netMap.networkPole[i][j].isTerrainType4) {
                    mapa.getPola()[i][j].setTypTerenu(TypyTerenu.Rzeka);
                }
            }
        }
    }

    /**
     * Generuje mapę z zadanych ilości pól.
     *
     * @param iloscPolX
     * @param iloscPolY
     */
    public void generujMape(int iloscPolX, int iloscPolY) {
        this.iloscPolX = iloscPolX;
        this.iloscPolY = iloscPolY;
        pola = new Pole[iloscPolX][iloscPolY];

        for (int i = 0; i < this.iloscPolX; i++) {
            for (int j = 0; j < this.iloscPolY; j++) {
                pola[i][j] = new Pole();
                pola[i][j].locXonMap = i;
                pola[i][j].locYonMap = j;
            }
        }
    }

    //SETTERS AND GETTERS
    public int getIloscPolX() {
        return iloscPolX;
    }

    public int getIloscPolY() {
        return iloscPolY;
    }

    public Pole[][] getPola() {
        return pola;
    }

    public void setPola(Pole[][] pola) {
        this.pola = pola;
    }
}
