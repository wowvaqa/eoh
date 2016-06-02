package com.vs.ai;

import com.vs.eoh.Pole;

import java.util.ArrayList;

/**
 * Klasa do sortowania przaz AI
 * Created by v on 2016-06-01.
 */
public class Sort {

    /**
     * Metoda sortuje moby wg dystansu
     *
     * @param mobCells
     */
    static public void mobCellSort(ArrayList<AI.MobCell> mobCells) {
        AI.MobCell temp;
        int zmiana = 1;
        while (zmiana > 0) {
            zmiana = 0;
            for (int i = 0; i < mobCells.size() - 1; i++) {
                if (mobCells.get(i).distance > mobCells.get(i + 1).distance) {
                    temp = mobCells.get(i + 1);
                    mobCells.set(i + 1, mobCells.get(i));
                    mobCells.set(i, temp);
                    zmiana++;
                }
            }
        }
    }

    /**
     * Metoda sortuje skrzynie ze skarbami wg dystansu
     *
     * @param mobCells
     */
    static public void tresureBoxCellSort(ArrayList<AI.TresureBoxCell> mobCells) {
        AI.TresureBoxCell temp;
        int zmiana = 1;
        while (zmiana > 0) {
            zmiana = 0;
            for (int i = 0; i < mobCells.size() - 1; i++) {
                if (mobCells.get(i).distance > mobCells.get(i + 1).distance) {
                    temp = mobCells.get(i + 1);
                    mobCells.set(i + 1, mobCells.get(i));
                    mobCells.set(i, temp);
                    zmiana++;
                }
            }
        }
    }

    /**
     * Metoda sortuje bohaterów wg dystansu
     *
     * @param mobCells
     */
    static public void HeroCellSort(ArrayList<AI.HeroCell> mobCells) {
        AI.HeroCell temp;
        int zmiana = 1;
        while (zmiana > 0) {
            zmiana = 0;
            for (int i = 0; i < mobCells.size() - 1; i++) {
                if (mobCells.get(i).distance > mobCells.get(i + 1).distance) {
                    temp = mobCells.get(i + 1);
                    mobCells.set(i + 1, mobCells.get(i));
                    mobCells.set(i, temp);
                    zmiana++;
                }
            }
        }
    }

    /**
     * Metoda sortuje Pola wg dystansu
     *
     * @param mobCells
     */
    static public void fieldSort(ArrayList<Pole> mobCells) {
        Pole temp;
        int zmiana = 1;
        while (zmiana > 0) {
            zmiana = 0;
            for (int i = 0; i < mobCells.size() - 1; i++) {
                if (mobCells.get(i).pathF > mobCells.get(i + 1).pathF) {
                    temp = mobCells.get(i + 1);
                    mobCells.set(i + 1, mobCells.get(i));
                    mobCells.set(i, temp);
                    zmiana++;
                }
            }
        }
    }

    /**
     * Metoda sortuje Zamki wg dystansu
     *
     * @param mobCells
     */
    static public void castleCellSort(ArrayList<AI.CastleCell> mobCells) {
        AI.CastleCell temp;
        int zmiana = 1;
        while (zmiana > 0) {
            zmiana = 0;
            for (int i = 0; i < mobCells.size() - 1; i++) {
                if (mobCells.get(i).distance > mobCells.get(i + 1).distance) {
                    temp = mobCells.get(i + 1);
                    mobCells.set(i + 1, mobCells.get(i));
                    mobCells.set(i, temp);
                    zmiana++;
                }
            }
        }
    }
}
