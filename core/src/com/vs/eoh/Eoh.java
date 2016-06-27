package com.vs.eoh;

import com.badlogic.gdx.Game;
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

	//private final GameStatus gs = new GameStatus();
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
		v.setAwansScreen(awansScreen);
		v.setMultiplayerScreen(multiplayerScreen);
		v.setGameOverScreen(gameOverScreen);

		//Assets.testScreen = testScreen;
		//Assets.mainMenuScreen = mainScreen;
		//Assets.newGameScreen = newGameScreen;
		//Assets.mapScreen = mapScreen;
		//Assets.bohaterScreen = bohaterScreen;
		//Assets.itemScreen = itemScreen;
		//Assets.newBohaterScreen = newBohaterScreen;
		//Assets.mapEditor = mapEditor;
		//Assets.awansScreen = awansScreen;
		//Assets.multiplayerScreen = multiplayerScreen;
		//Assets.gameOverScreen = gameOverScreen;

		//GameStatus.a = a;
		//GameStatus.gs = gs;
		//GameStatus.g = this;

		this.setScreen(mainScreen);
	}

	@Override
	public void render () {
		super.render();
	}
}
