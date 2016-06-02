package com.vs.ai;

import com.badlogic.gdx.Gdx;
import com.vs.eoh.Mapa;
import com.vs.eoh.Pole;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Klasa odpowiada za wyszukiwanie ścieżki
 * Created by v on 2016-05-23.
 */
public class PathFinder {

    // Lista pól wymagających rozpatrzenia w algorytmie A*
    public static ArrayList<Pole> openLink;
    // Lista pól, które już nie będą rozpatrywane w algorytmie A*
    public static ArrayList<Pole> closedLinks;

    /**
     * Wyszukuje ścieżkę
     *
     * @param map        Referencja do obiektu mapy
     * @param startField Pole od którego rozpoczyna się szukanie scieżki
     * @param endField   Pole które jest końcem ścieżki
     */
    public static ArrayList<PathMoves> findPath(Mapa map, Pole startField, Pole endField) {

        int destiantion = 0;

//        if (endField.getMob() != null) {
//            destiantion = 1;
//        } else if (endField.getTresureBox() != null) {
//            destiantion = 2;
//        }

        clearLists();

        startField.startField = true;
        endField.endField = true;

        countF(startField, startField, endField);
        addStartedLink(startField);

        Pole q;

        while (openLink.size() > 0) {

//            Gdx.app.log("STEP 1) ", "openLinkSIZE: " + openLink.size());

            //Wyszukanie pola z najmniejszym F i przypisanie pod Q
            q = returnLessFfield(openLink);

//            Gdx.app.log("PROCESSING field", "" + q + " X: " + q.locXonMap + " Y: " + q.locYonMap);

            // Dodanie pola Q do CL oraz usuniecie go z OL
            moveToClosedList(q);

            // Sprawdzenie czy znaleziono pole końcowe
            if (q.equals(endField)) {
                Gdx.app.log("Znaleziono pole końcowe", "");
                //openLink.clear();
                startField.startField = false;
                endField.endField = false;
                ArrayList<PathMoves> listOfMoves = extractMoves(q);
                map.clearParentsOfFields();
                return listOfMoves;
            }

            // inicjacja pola z sąsiadami.
            ArrayList<Pole> neighbors = new ArrayList<Pole>();

            // Wypełnia listę sąsiadów polami sąsiadów
            fillNeighbors(map, q, neighbors, endField);

            // Sprawdzenie każdego z sąsiadów
            for (Pole pole : neighbors) {

                // Sprawdzenie czy pole znajduje się na liście zamkniętej
                if (checkClosedList(pole)) {
//                    Gdx.app.log("" + pole + "X: " + pole.locXonMap + " Y: " + pole.locYonMap, "Jest na liście CL");
                }
                // Sprawdzenie czy pole nie znajduje się na liście pól otwartych
                else if (!checkOpenList(pole)) {
                    openLink.add(pole);
                    pole.parentField = q;

                    pole.pathG = countG(q, pole);
                    pole.pathH = countH(pole, endField);
                    pole.pathF = pole.pathG + pole.pathH;

                }
                // Jeżeli pole znajduje się na liści pól otwartych
                else {
                    double tempG = countG(q, pole);
                    if (tempG < pole.pathG) {
                        pole.parentField = q;
                        pole.pathG = tempG;

                        pole.pathH = countH(pole, endField);
                        pole.pathF = tempG + pole.pathH;
                    }
                }
            }
        }
        Gdx.app.log("NIE ZNLEZIONO SCIEŻKI", "");
        startField.startField = false;
        endField.endField = false;
        map.clearParentsOfFields();
        return null;
    }

    /**
     * Zwraca listę ruchów (X,Y) w postaci tablicy ArrayList
     *
     * @param field pole Q
     * @return ArrayList PathMoves
     */
    public static ArrayList<PathMoves> extractMoves(Pole field) {
        Pole pole;
        ArrayList<PathMoves> tmpLisoOfMoves = new ArrayList<PathMoves>();
        ArrayList<PathMoves> listOfMoves = new ArrayList<PathMoves>();
        ArrayList<Pole> fieldsToClear = new ArrayList<Pole>();

        if (field.parentField != null) {
            tmpLisoOfMoves.add(new PathMoves(field.locXonMap - field.parentField.locXonMap,
                    field.locYonMap - field.parentField.locYonMap));
            fieldsToClear.add(field);
            pole = field.parentField;
            while (pole.parentField != null) {
                tmpLisoOfMoves.add(new PathMoves(pole.locXonMap - pole.parentField.locXonMap,
                        pole.locYonMap - pole.parentField.locYonMap));
                fieldsToClear.add(pole);
                pole = pole.parentField;
            }
        }

        if (tmpLisoOfMoves.size() > 0) {
            for (int i = tmpLisoOfMoves.size() - 1; i >= 0; i--) {
                listOfMoves.add(tmpLisoOfMoves.get(i));
            }
        }

        for (Pole fieldToClear : fieldsToClear) {
            fieldToClear.parentField = null;
        }

        return listOfMoves;
    }

    /**
     * Dodaje węzeł początkowy do openLinks
     *
     * @param pole Referencja do obiektu pole
     */
    public static void addStartedLink(Pole pole) {
        openLink.add(pole);

    }

    /**
     * Czyści listy openLink i closedLinks
     */
    public static void clearLists() {
        openLink.clear();
        closedLinks.clear();
    }

    /**
     * Liczy współczynnik G dla zadanego pola
     *
     * @param startField
     */
    public static double countG(Pole startField, Pole endField) {
        if (startField.startField) {
            return 0;
        } else {
            double distance = 0;
            double sideA = 0;
            double sideB = 0;

            sideA = Math.abs(endField.locYonMap - startField.locYonMap);
            sideB = Math.abs(endField.locXonMap - startField.locXonMap);

            distance = Math.sqrt(Math.pow(sideA, 2) + Math.pow(sideB, 2));

//            Gdx.app.log("Distance G", "" + (startField.pathG + distance));

            return startField.pathG + distance;
        }
    }

    /**
     * Liczy współcznnik H dla zadanego pola
     *
     * @param field
     */
    public static double countH(Pole field, Pole endField) {

        double distance = 0;
        double sideA = 0;
        double sideB = 0;

        sideA = Math.abs(endField.locYonMap - field.locYonMap);
        sideB = Math.abs(endField.locXonMap - field.locXonMap);

        distance = Math.sqrt(Math.pow(sideA, 2) + Math.pow(sideB, 2));

//        Gdx.app.log("Distance H", "" + distance);
        return distance;
    }

    /**
     * Liczy współczynnik F dla zadanego pola
     *
     * @param field
     */
    public static void countF(Pole field, Pole partntField, Pole endField) {
        field.pathF = countG(partntField, field) + countH(field, endField);
//        Gdx.app.log("F " + "X:" + field.locXonMap + " Y:" + field.locYonMap, "" + field.pathF);
    }

    /**
     * Znajduje pole z najmniejszą wartością F wśród podanej listy
     *
     * @return Pole
     */
    public static Pole returnLessFfield(ArrayList<Pole> list) {

        Sort.fieldSort(list);

//        list.sort(new Comparator<Pole>() {
//            @Override
//            public int compare(Pole o1, Pole o2) {
//                if (o2 == null) return -1;
//                if (o1.pathF > o2.pathF) return 1;
//                else if (o1.pathF < o2.pathF) return -1;
//                else
//                    return 0;
//            }
//        });

//        for (Pole pole : list) {
//            Gdx.app.log("1. " + pole, "X: " + pole.locXonMap + " Y: " + pole.locYonMap + " F: " + pole.pathF);
//        }
        return list.get(0);
    }

    /**
     * Wypełnia Arrayliste obiektami pól sąsiadujących.
     * @param mapa Referencja do obiketu mapy
     * @param field Referencja do obiektu pola wg którego będą dodawani sąsiedzi
     * @param neighborsList ArrayLista z obiektami pól sąsiadów
     * @param endField Referencja do obiektu pola końcowgo (szukanego)
     */
    private static void fillNeighbors(Mapa mapa, Pole field, ArrayList<Pole> neighborsList, Pole endField) {
        neighborsList.clear();

        for (int i = field.locXonMap - 1; i < field.locXonMap + 2; i++) {
            for (int j = field.locYonMap - 1; j < field.locYonMap + 2; j++) {
                if (i >= 0 && j >= 0 && i < mapa.getIloscPolX() && j < mapa.getIloscPolY()) {
                    Pole fieldToAdd = mapa.getPola()[i][j];

                    if (endField.getMob() != null && fieldToAdd.equals(endField)) {
                        neighborsList.add(fieldToAdd);
                    } else if (endField.getTresureBox() != null && fieldToAdd.equals(endField)) {
                        neighborsList.add(fieldToAdd);
                    } else if (endField.getBohater() != null && fieldToAdd.equals(endField)) {
                        neighborsList.add(fieldToAdd);
                    } else if (fieldToAdd.isMovable() && fieldToAdd.getMob() == null && fieldToAdd != field && fieldToAdd.getBohater() == null) {
                        neighborsList.add(fieldToAdd);
                    }
                }
            }
        }

//        Gdx.app.log("LISTA SASIADOW", "" + neighborsList.size());
//        for (Pole pole : neighborsList) {
//            Gdx.app.log("1. " + pole, "X: " + pole.locXonMap + " Y: " + pole.locYonMap);
//        }
    }

    /**
     * Przenosi zadane parametrem pole do listy pól zamkniętych.
     *
     * @param field
     */
    private static void moveToClosedList(Pole field) {
//        Gdx.app.log("Dodaje pole do CL: ", "" + field + " X: " + field.locXonMap + " Y: " + field.locYonMap);
        closedLinks.add(field);
        if (openLink.size() > 0) {
            int indeksDoUsuniecia = -1;
            for (int i = 0; i < openLink.size(); i++) {
                if (openLink.get(i) == field) {
                    indeksDoUsuniecia = i;
                }
            }
            if (indeksDoUsuniecia != -1) {
//                Gdx.app.log("Usuwam pole z OL: ", "" + field + " X: " + field.locXonMap + " Y: " + field.locYonMap);
                openLink.remove(indeksDoUsuniecia);
            }
        }
    }

    /**
     * Sprawdza czy zadane parametrem pole znajduje się na liście pól zamkniętych
     *
     * @param field
     * @return Zwraca True jeżeli tak, False jeżeli nie
     */
    private static boolean checkClosedList(Pole field) {
        for (Pole pole : closedLinks) {
            if (pole.equals(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sprawdza czy zadane parametrem pole znajduje się na liście pól otwartych
     *
     * @param field
     * @return
     */
    private static boolean checkOpenList(Pole field) {
        for (Pole pole : openLink) {
            if (pole.equals(field)) {
                return true;
            }
        }
        return false;
    }
}


