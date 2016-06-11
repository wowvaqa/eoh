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

	private final GameStatus gs = new GameStatus();
	private Assets a;
	private NetEngine ne;

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
		a = new Assets();
		ne = new NetEngine(this.gs, this.a, this);
		mainScreen = new MainScreen(this, a, gs);
		newGameScreen = new NewGameScreen(this, this.a, this.gs);
		optionsScreen = new OptionsScreen(this.gs, this.a);
		bohaterScreen = new BohaterScreen(this, this.a, this.gs);
		itemScreen = new ItemScreen(this.a, this.gs, this);
		testScreen = new TestingScreen(this, this.a, this.gs);
		newBohaterScreen = new NewBohaterScreen(this, this.gs, this.a, Assets.stage01MapScreen);
		mapEditor = new MapEditor(this, this.a);
		awansScreen = new AwansScreen(this, this.a, this.gs);
		multiplayerScreen = new MultiplayerScreen(this, this.a, this.gs, this.ne);
		gameOverScreen = new GameOverScreen(ne);

		Assets.testScreen = testScreen;
		Assets.mainMenuScreen = mainScreen;
		Assets.newGameScreen = newGameScreen;
		Assets.mapScreen = mapScreen;
		Assets.bohaterScreen = bohaterScreen;
		Assets.itemScreen = itemScreen;
		Assets.newBohaterScreen = newBohaterScreen;
		Assets.mapEditor = mapEditor;
		Assets.awansScreen = awansScreen;
		Assets.multiplayerScreen = multiplayerScreen;
		Assets.gameOverScreen = gameOverScreen;

		GameStatus.a = a;
		GameStatus.gs = gs;
		GameStatus.g = this;

		this.setScreen(mainScreen);
	}

	@Override
	public void render () {
		super.render();
	}
}
