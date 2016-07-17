package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Klasa przetrzymuje referencje do obiketów GameStatus, Assets, Game oraz do Screenów i Plansz
 * z MapScreen.
 */
public class V {
    private Game g;
    private GameStatus gs;
    private Assets a;

    private Screen testScreen;
    private Screen mainMenuScreen;
    private Screen newGameScreen;
    private Screen mapScreen;
    private Screen bohaterScreen;
    private Screen itemScreen;
    private Screen lastScreen;
    private Screen newBohaterScreen;
    private Screen mapEditor;
    private Screen mapEditScreen;
    private Screen awansScreen;
    private Screen multiplayerScreen;
    private Screen gameOverScreen;

    private Stage stage01MapScreen;
    private Stage stage02MapScreen;
    private Stage stage03MapScreen;

    public V(Game g, GameStatus gs, Assets a) {
        this.g = g;
        this.gs = gs;
        this.a = a;
    }

    public Game getG() {
        return g;
    }

    public void setG(Game g) {
        this.g = g;
    }

    public GameStatus getGs() {
        return gs;
    }

    public void setGs(GameStatus gs) {
        this.gs = gs;
    }

    public Assets getA() {
        return a;
    }

    public void setA(Assets a) {
        this.a = a;
    }

    public Screen getTestScreen() {
        return testScreen;
    }

    public void setTestScreen(Screen testScreen) {
        this.testScreen = testScreen;
    }

    public Screen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public void setMainMenuScreen(Screen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public Screen getNewGameScreen() {
        return newGameScreen;
    }

    public void setNewGameScreen(Screen newGameScreen) {
        this.newGameScreen = newGameScreen;
    }

    public Screen getMapScreen() {
        return mapScreen;
    }

    public void setMapScreen(Screen mapScreen) {
        this.mapScreen = mapScreen;
    }

    public Screen getBohaterScreen() {
        return bohaterScreen;
    }

    public void setBohaterScreen(Screen bohaterScreen) {
        this.bohaterScreen = bohaterScreen;
    }

    public Screen getItemScreen() {
        return itemScreen;
    }

    public void setItemScreen(Screen itemScreen) {
        this.itemScreen = itemScreen;
    }

    public Screen getLastScreen() {
        return lastScreen;
    }

    public void setLastScreen(Screen lastScreen) {
        this.lastScreen = lastScreen;
    }

    public Screen getNewBohaterScreen() {
        return newBohaterScreen;
    }

    public void setNewBohaterScreen(Screen newBohaterScreen) {
        this.newBohaterScreen = newBohaterScreen;
    }

    public Screen getMapEditor() {
        return mapEditor;
    }

    public void setMapEditor(Screen mapEditor) {
        this.mapEditor = mapEditor;
    }

    public Screen getMapEditScreen() {
        return mapEditScreen;
    }

    public void setMapEditScreen(Screen mapEditScreen) {
        this.mapEditScreen = mapEditScreen;
    }

    public Screen getAwansScreen() {
        return awansScreen;
    }

    public void setAwansScreen(Screen awansScreen) {
        this.awansScreen = awansScreen;
    }

    public Screen getMultiplayerScreen() {
        return multiplayerScreen;
    }

    public void setMultiplayerScreen(Screen multiplayerScreen) {
        this.multiplayerScreen = multiplayerScreen;
    }

    public Screen getGameOverScreen() {
        return gameOverScreen;
    }

    public void setGameOverScreen(Screen gameOverScreen) {
        this.gameOverScreen = gameOverScreen;
    }

    public Stage getStage01MapScreen() {
        return stage01MapScreen;
    }

    public void setStage01MapScreen(Stage stage01MapScreen) {
        this.stage01MapScreen = stage01MapScreen;
    }

    public Stage getStage02MapScreen() {
        return stage02MapScreen;
    }

    public void setStage02MapScreen(Stage stage02MapScreen) {
        this.stage02MapScreen = stage02MapScreen;
    }

    public Stage getStage03MapScreen() {
        return stage03MapScreen;
    }

    public void setStage03MapScreen(Stage stage03MapScreen) {
        this.stage03MapScreen = stage03MapScreen;
    }
}
