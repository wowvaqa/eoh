package com.vs.eoh;

import com.badlogic.gdx.Game;
import com.vs.mapEditor.MapEdit;
import com.vs.mapEditor.MapEditScreen;
import com.vs.network.NetEngine;
import com.vs.screens.AwansScreen;
import com.vs.screens.BohaterScreen;
import com.vs.screens.GameOverScreen;
import com.vs.screens.ItemScreen;
import com.vs.screens.MainScreen;
import com.vs.screens.MapScreen;
import com.vs.screens.MultiplayerScreen;
import com.vs.screens.NewBohaterScreen;
import com.vs.screens.NewGameScreen;
import com.vs.screens.OptionsScreen;
import com.vs.testing.MapEditor;
import com.vs.testing.TestingScreen;

public class Eoh extends Game {

	private GameStatus gs;
	private Assets a;
	private NetEngine ne;
	private V v;

	private MainScreen mainScreen;
	private MapScreen mapScreen;
	private NewGameScreen newGameScreen;
	private OptionsScreen optionsScreen;
	private BohaterScreen bohaterScreen;
	private ItemScreen itemScreen;
	private TestingScreen testScreen;
	private NewBohaterScreen newBohaterScreen;
	private MapEditor mapEditor;
	private MapEditScreen mapEditScreen;
	private AwansScreen awansScreen;
	private MultiplayerScreen multiplayerScreen;
	private GameOverScreen gameOverScreen;

	@Override
	public void create () {
		gs = new GameStatus(this);
		a = new Assets();
		v = new V(this, gs, a);

		ne = new NetEngine(v);
		mainScreen = new MainScreen(v);
		newGameScreen = new NewGameScreen(v);
		optionsScreen = new OptionsScreen(v);
		bohaterScreen = new BohaterScreen(v);
		itemScreen = new ItemScreen(v);
		testScreen = new TestingScreen(v);
		newBohaterScreen = new NewBohaterScreen(v);
		mapEditor = new MapEditor(v);
		mapEditScreen = new MapEditScreen(v);
		awansScreen = new AwansScreen(v);
		multiplayerScreen = new MultiplayerScreen(v, this.ne);
		gameOverScreen = new GameOverScreen(ne, v);

		v.setTestScreen(testScreen);
		v.setMainMenuScreen(mainScreen);
		v.setNewGameScreen(newGameScreen);
		v.setMapScreen(mapScreen);
		v.setBohaterScreen(bohaterScreen);
		v.setItemScreen(itemScreen);
		v.setNewBohaterScreen(newBohaterScreen);
		v.setMapEditor(mapEditor);
		v.setMapEditScreen(mapEditScreen);
		v.setAwansScreen(awansScreen);
		v.setMultiplayerScreen(multiplayerScreen);
		v.setGameOverScreen(gameOverScreen);

		this.setScreen(mainScreen);
	}

	@Override
	public void render () {
		super.render();
	}
}
