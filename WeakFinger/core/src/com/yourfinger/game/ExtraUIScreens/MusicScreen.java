package com.yourfinger.game.ExtraUIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;
import com.yourfinger.game.UIScreens.ExtraScreen;

public class MusicScreen implements Screen {

	final float RELATIVE_SIZE_OF_MANAGE_BUTTON = 0.18f;

	// ключевые объекты
	Main game;
	Stage stage;

	Label lblMusic;
	int currentMusic;
	int countOfMusics;

	Button btnNext;
	Button btnPrevious;
	Button btnBack;

	Button btnPlayPause;
	ButtonStyle playStyle;
	ButtonStyle pauseStyle;

	Music music;

	Sprite sprBackground;



	public MusicScreen(Main game) {
		// создаем основные объекты
		this.game = game;

		currentMusic = 0;
		ComputeCountOfMusic();

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		music = GetMusic();
		music.setLooping(true);

		CreateManageButtons();
		CreateLabel();
		CreateBackgroundSprite();

		Gdx.input.setInputProcessor(stage);
	}
	
	private void ComputeCountOfMusic()
	{
		if (PrefBuilder.GetComleteWorlds() < 5)
			countOfMusics = PrefBuilder.GetComleteWorlds() + 1;
		else
			if (PrefBuilder.GetComleteLevels() < 2)
				countOfMusics = 7;
			else
				countOfMusics = 8;
	}



	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		game.GetBatch().end();
		stage.draw();

	}



	private void CreateLabel() {

		if (lblMusic == null) {
			LabelStyle labelStyle = new LabelStyle();
			labelStyle.font = game.GetFont().textFont;
			lblMusic = new Label(game.GetString(""), labelStyle);
			lblMusic.setAlignment(Align.center);
			lblMusic.setWidth(Gdx.graphics.getWidth());
			lblMusic.setWrap(true);
			stage.addActor(lblMusic);
		}
		lblMusic.setText(game.GetString("music_" + String.valueOf(currentMusic)));
		lblMusic.setCenterPosition(Gdx.graphics.getWidth() / 2,
				(Gdx.graphics.getHeight() + Gdx.graphics.getWidth()
						* RELATIVE_SIZE_OF_MANAGE_BUTTON) / 2);

	}



	private void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(game.GetMainAtlas().findRegion(
				"background_ext"));
		sprBackground.setPosition(0, 0);
		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
	}
	
	
	private Music GetMusic()
	{
		if ((currentMusic > 0)&&(currentMusic <6))
			return game.getMusic(currentMusic);
		else if (currentMusic == 0)
		{
			Music mus = game.GetMainMusic();
			mus.setVolume(1);
			mus.stop();
			return mus;
		}
		else if (currentMusic == 6)
			return game.GetBossMusic();
		else return game.GetFinalMusic();
	}



	private void CreateManageButtons() {
		Skin skin = new Skin();
		skin.addRegions(game.GetMainAtlas());
		ButtonStyle buttonStyleNext = new ButtonStyle();
		buttonStyleNext.up = skin.getDrawable("next_up");
		buttonStyleNext.down = skin.getDrawable("next_down");
		buttonStyleNext.checked = skin.getDrawable("next_up");
		btnNext = new Button(buttonStyleNext);
		btnNext.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnNext.setCenterPosition(Gdx.graphics.getWidth() * 0.5f,
				btnNext.getHeight() / 2);

		btnNext.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnNext.getWidth()) && (y < btnNext.getHeight())
						&& (x > 0) && (y > 0)) {
					// след музыка
					if (currentMusic < countOfMusics - 1) {
						boolean isPlaying = music.isPlaying();
						music.stop();
						currentMusic++;
						music = GetMusic();
						music.setLooping(true);
						if (isPlaying)
							music.play();
						CreateLabel();
					}
				}
			}
		});

		ButtonStyle buttonStylePrevious = new ButtonStyle();
		buttonStylePrevious.up = skin.getDrawable("previous_up");
		buttonStylePrevious.down = skin.getDrawable("previous_down");
		buttonStylePrevious.checked = skin.getDrawable("previous_up");
		btnPrevious = new Button(buttonStylePrevious);
		btnPrevious.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnPrevious.setCenterPosition(Gdx.graphics.getWidth() * 0.1f,
				btnPrevious.getHeight() * 0.53f);

		btnPrevious.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnPrevious.getWidth())
						&& (y < btnPrevious.getHeight()) && (x > 0) && (y > 0)) {
					// пред музыка
					if (currentMusic > 0) {
						boolean isPlaying = music.isPlaying();
						music.stop();
						currentMusic--;
						music = GetMusic();
						music.setLooping(true);
						if (isPlaying)
							music.play();
						CreateLabel();
					}
				}
			}
		});

		ButtonStyle buttonStyleBack = new ButtonStyle();
		buttonStyleBack.up = skin.getDrawable("back_up");
		buttonStyleBack.down = skin.getDrawable("back_down");
		buttonStyleBack.checked = skin.getDrawable("back_up");
		btnBack = new Button(buttonStyleBack);
		btnBack.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnBack.setCenterPosition(Gdx.graphics.getWidth() * 0.88f,
				btnBack.getHeight() / 2);

		btnBack.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnBack.getWidth()) && (y < btnBack.getHeight())
						&& (x > 0) && (y > 0)) {
					music.stop();
					game.setScreen(new ExtraScreen(game));
					dispose();
				}
			}
		});

		playStyle = new ButtonStyle();
		playStyle.up = skin.getDrawable("play_up");
		playStyle.down = skin.getDrawable("play_down");
		playStyle.checked = skin.getDrawable("play_up");

		pauseStyle = new ButtonStyle();
		pauseStyle.up = skin.getDrawable("pause_up");
		pauseStyle.down = skin.getDrawable("pause_down");
		pauseStyle.checked = skin.getDrawable("pause_up");

		btnPlayPause = new Button(playStyle);
		btnPlayPause.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnPlayPause.setCenterPosition(Gdx.graphics.getWidth() * 0.3f,
				btnPlayPause.getHeight() / 2);

		btnPlayPause.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnPlayPause.getWidth())
						&& (y < btnPlayPause.getHeight()) && (x > 0) && (y > 0)) {
					if (music.isPlaying()) {
						music.pause();
						btnPlayPause.setStyle(playStyle);
					} else {
						music.play();
						btnPlayPause.setStyle(pauseStyle);
					}
				}
			}
		});

		stage.addActor(btnBack);
		stage.addActor(btnNext);
		stage.addActor(btnPrevious);
		stage.addActor(btnPlayPause);

	}



	@Override
	public void resize(int width, int height) {
	}



	@Override
	public void show() {

	}



	@Override
	public void hide() {
	}



	@Override
	public void pause() {
	}



	@Override
	public void resume() {

	}



	@Override
	public void dispose() {

	}

}
