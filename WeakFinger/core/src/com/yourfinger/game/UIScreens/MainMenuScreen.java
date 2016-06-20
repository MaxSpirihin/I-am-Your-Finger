package com.yourfinger.game.UIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.GameScreens.ComixScreen;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;

public class MainMenuScreen implements Screen {

	// константы расположения
	static final String ATLAS_DIRECTORY = "Textures\\MainMenu.atlas";

	// расстояния меж кнопками
	static final int VERTICAL_INDENT = Gdx.graphics.getHeight() / 40;

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;

	// фон
	Sprite sprBackground;

	// кнопки
	Table table;
	TextButton btnCampaign;
	TextButton btnArcade;
	TextButton btnOptions;
	TextButton btnExtra;



	public MainMenuScreen(Main game) {

		// создаем основные объекты
		this.game = game;

		atlas = game.GetMainAtlas();
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		CreateBackgroundSprite();
		CreateTable();
		// для реагирования нажатия на кнопки
		Gdx.input.setInputProcessor(stage);

	}



	@Override
	public void render(float delta) {
		// чистим экран и устанавливавем фон
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		game.GetBatch().end();
		stage.draw();
	}



	@Override
	public void dispose() {
		stage.dispose();
	}



	void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("main_picture"));

		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
		sprBackground.setPosition(0,
				Gdx.graphics.getHeight() - sprBackground.getHeight());
	}



	void CreateTable() {
		// создаем табоицу и приписываем ей параметры
		table = new Table();
		table.center();
		table.padTop(VERTICAL_INDENT);
		table.setCenterPosition(0, Gdx.graphics.getHeight() / 2);
		table.align(Align.left);

		// кнопки и их обработчики
		TextButtonStyle styleCamp = new TextButtonStyle();
		styleCamp.font = game.GetFont().menuFont;
		styleCamp.fontColor = Color.WHITE;
		btnCampaign = new TextButton(game.GetString("campaign") + "\n(" + String.valueOf(PrefBuilder.GetProgress()) + "%)", styleCamp);
		btnCampaign.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnCampaign.getStyle().fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnCampaign.getWidth())
						&& (y < btnCampaign.getHeight()) && (x > 0) && (y > 0))
					if (!PrefBuilder.IsFirstStart())
						if (PrefBuilder.GetComleteWorlds() < 5)
							game.setScreen(new ChooseLevelScreen(game,
									PrefBuilder.GetComleteWorlds() + 1));
						else
							game.setScreen(new ChooseFinalScreen(game));
					else {
						PrefBuilder.SetIsFirstStart(false);
						game.setScreen(new ComixScreen(game, 1, 1));
					}
				else
					btnCampaign.getStyle().fontColor = Color.WHITE;
			}
		});

		TextButtonStyle styleArcade = new TextButtonStyle();
		styleArcade.font = game.GetFont().menuFont;
		styleArcade.fontColor = Color.WHITE;
		btnArcade = new TextButton(game.GetString("arcade"), styleArcade);
		btnArcade.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnArcade.getStyle().fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnArcade.getWidth()) && (y < btnArcade.getHeight())
						&& (x > 0) && (y > 0)) {
					// экран аркад
					btnArcade.getStyle().fontColor = Color.WHITE;
					game.setScreen(new ChooseArcadeScreen(game));
				}

				else
					btnArcade.getStyle().fontColor = Color.WHITE;
			}
		});

		TextButtonStyle styleOptions = new TextButtonStyle();
		styleOptions.font = game.GetFont().menuFont;
		styleOptions.fontColor = Color.WHITE;
		btnOptions = new TextButton(game.GetString("options"), styleOptions);
		btnOptions.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnOptions.getStyle().fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnOptions.getWidth()) && (y < btnOptions.getHeight())
						&& (x > 0) && (y > 0)) {
					// экран настроек
					btnOptions.getStyle().fontColor = Color.WHITE;
					game.setScreen(new OptionsScreen(game));
				}

				else
					btnOptions.getStyle().fontColor = Color.WHITE;
			}
		});

		TextButtonStyle styleExtra = new TextButtonStyle();
		styleExtra.font = game.GetFont().menuFont;
		styleExtra.fontColor = Color.WHITE;
		btnExtra = new TextButton(game.GetString("extra"), styleExtra);
		btnExtra.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnExtra.getStyle().fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnExtra.getWidth()) && (y < btnExtra.getHeight())
						&& (x > 0) && (y > 0)) {
					// экран Другое
					btnExtra.getStyle().fontColor = Color.WHITE;
					game.setScreen(new ExtraScreen(game));
				} else
					btnExtra.getStyle().fontColor = Color.WHITE;
			}
		});

		table.add(btnCampaign).row().padTop(VERTICAL_INDENT);
		table.add(btnArcade).row().padTop(VERTICAL_INDENT);
		table.add(btnOptions).row().padTop(VERTICAL_INDENT);
		table.add(btnExtra);

		stage.addActor(table);

	}



	// ///////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////
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

}
