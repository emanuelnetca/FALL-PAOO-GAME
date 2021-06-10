package com.manu;

import Helpers.GameManager;
import Scenes.Gameplay;
import Scenes.MainMenu;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMain extends Game {

	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GameManager.getInstance().initializeGameData();
		setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch() {
		return this.batch;
	}
}
