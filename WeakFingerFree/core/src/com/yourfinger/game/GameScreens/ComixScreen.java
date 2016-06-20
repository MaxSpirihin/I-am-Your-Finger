package com.yourfinger.game.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.UIScreens.ChooseFinalScreen;
import com.yourfinger.game.UIScreens.ChooseLevelScreen;

public class ComixScreen implements Screen {

	// константы расположения
	static final String ATLAS_DIRECTORY_COMIX = "Textures\\Comix\\";

	// размер кнопки
	final float RELATIVE_SIZE_OF_BUTTON = 0.3f;

	// скорость комикса
	final float SPEED = Gdx.graphics.getHeight() / 30;

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;
	Music music;

	// фон - кртинка
	Sprite sprPicture;

	int currentPicture;
	int comixNumber;
	int worldNumber;



	public ComixScreen(Main game, int worldNumber, int comixNumber) {

		// создаем основные объекты
		this.game = game;

		// выбираем атлас по локали или по умолчанию
		if (Gdx.files.internal(
				ATLAS_DIRECTORY_COMIX + String.valueOf(worldNumber) + "_"
						+ String.valueOf(comixNumber) + "_" + game.GetLocale()
						+ ".atlas").exists())
			atlas = new TextureAtlas(Gdx.files.internal(ATLAS_DIRECTORY_COMIX
					+ String.valueOf(worldNumber) + "_"
					+ String.valueOf(comixNumber) + "_" + game.GetLocale()
					+ ".atlas"));
		else
			atlas = new TextureAtlas(Gdx.files.internal(ATLAS_DIRECTORY_COMIX
					+ String.valueOf(worldNumber) + "_"
					+ String.valueOf(comixNumber) + ".atlas"));
		
		
		//Врубаем музон
		if (worldNumber < 6)
			if (comixNumber == 1)
				music = game.getMusic(worldNumber);
			else
				music = game.GetBossMusic();
		else
			music = game.GetFinalMusic();
		
		if ((!music.isPlaying())&&(PrefBuilder.MusicOn()))
				music.play();
		
		

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());
		this.comixNumber = comixNumber;
		this.worldNumber = worldNumber;
		currentPicture = 1;

		CreatePicture(atlas.findRegion(String.valueOf(currentPicture)));
		CreateButton();
		Gdx.input.setInputProcessor(stage);

	}



	@Override
	public void render(float delta) {
		// чистим экран и устанавливавем фон
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.GetBatch().begin();
		sprPicture.draw(game.GetBatch());
		game.GetBatch().end();
		MovePicture(delta);
		stage.draw();
	}



	void CreatePicture(TextureRegion region) {
		// создаем фон
		sprPicture = new Sprite(region);

		sprPicture.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
				* sprPicture.getHeight() / sprPicture.getWidth());
		if (sprPicture.getHeight() >= Gdx.graphics.getHeight())
			sprPicture.setPosition(0,
					Gdx.graphics.getHeight() - sprPicture.getHeight());
		else
			sprPicture.setPosition(0,
					(Gdx.graphics.getHeight() - sprPicture.getHeight()) / 2);
	}



	void MovePicture(float delta) {
		if (sprPicture.getY() < 0)
			sprPicture.setY(sprPicture.getY() + SPEED * delta);
	}



	void CreateButton() {
		Skin skin = new Skin();
		skin.addRegions(atlas);
		ButtonStyle style = new ButtonStyle();
		style.up = skin.getDrawable("normal");
		style.down = skin.getDrawable("pressed");
		style.checked = skin.getDrawable("normal");

		final Button btn = new Button(style);
		btn.setSize(Gdx.graphics.getWidth() * this.RELATIVE_SIZE_OF_BUTTON,
				Gdx.graphics.getWidth() * this.RELATIVE_SIZE_OF_BUTTON);

		btn.setCenterPosition(Gdx.graphics.getWidth()
				* (1 - RELATIVE_SIZE_OF_BUTTON / 2), Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_BUTTON / 2);

		btn.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btn.getWidth()) && (y < btn.getHeight()) && (x > 0)
						&& (y > 0)) {
					// след экран
					currentPicture++;
					if (atlas.findRegion(String.valueOf(currentPicture)) != null)
						CreatePicture(atlas.findRegion(String
								.valueOf(currentPicture)));
					else {
						if (comixNumber == 2) {
							if (worldNumber < 6)
								game.setScreen(new GameBossScreen(game,
										worldNumber));
							else
								game.setScreen(new GameExtraScreen(game));
						} else {
							if (worldNumber < 6)
							{
								game.setScreen(new ChooseLevelScreen(game,
										worldNumber));
								music.stop();
							}
							else
								game.setScreen(new GameAlphaScreen(game));
						}
					}

				}
			}
		});

		stage.addActor(btn);
	}



	@Override
	public void dispose() {
		atlas.dispose();
	}



	// ///////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}



	@Override
	public void show() {
		// TODO Auto-generated method stub

	}



	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}



	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}



	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
