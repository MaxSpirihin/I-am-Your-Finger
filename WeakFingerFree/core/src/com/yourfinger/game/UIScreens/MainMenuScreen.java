package com.yourfinger.game.UIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.ExtraUIScreens.BuyFullScreen;
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
	TextButton btnFull;



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
		
		if (game.firstDialogNeed)
		{
			game.firstDialogNeed = false;
			CreateDialog();
		}
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
						if (PrefBuilder.GetComleteWorlds() == 0)
							game.setScreen(new ChooseLevelScreen(game,
									PrefBuilder.GetComleteWorlds() + 1));
						else
							game.setScreen(new BuyFullScreen(game));
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
		
		
		TextButtonStyle styleFull = new TextButtonStyle();
		styleFull.font = game.GetFont().menuFont;
		styleFull.fontColor = Constants.LINK_COLOR;
		btnFull = new TextButton(game.GetString("full_version_mm"), styleFull);
		btnFull.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnFull.getStyle().fontColor = Constants.LINK_PRESSED_COLOR;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnFull.getWidth()) && (y < btnFull.getHeight())
						&& (x > 0) && (y > 0)) {
					Gdx.net.openURI(game.GetString("link"));
				}
				btnFull.getStyle().fontColor = Constants.LINK_COLOR;
			}
		});

		table.add(btnCampaign).row().padTop(VERTICAL_INDENT);
		table.add(btnArcade).row().padTop(VERTICAL_INDENT);
		table.add(btnOptions).row().padTop(VERTICAL_INDENT);
		table.add(btnExtra).row().padTop(VERTICAL_INDENT);
		table.add(btnFull);

		stage.addActor(table);

	}
	
	
	
	private void CreateDialog() {

		// надписи
		String title = game.GetString("welcome");
		String info = game.GetString("first_dialog_text");

		Skin skin = new Skin();
		skin.addRegions(game.GetMainAtlas());

		// иконки кнопок
		Drawable ok, okDown;

		ok = skin.getDrawable("ok");
		okDown = skin.getDrawable("ok_down");


		// создаем диалог
		WindowStyle w = new WindowStyle();
		w.titleFont = game.GetFont().labelFont;
		w.titleFontColor = Color.WHITE;
		w.background = skin.getDrawable("dialog");
		final Dialog dialog = new Dialog(title, w) {

			protected void result(Object object) {

					// обновляем сцену
					game.setScreen(new MainMenuScreen(game));
				
			}
		};

		// метка с инфой
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;
		labelStyle.fontColor = Color.WHITE;
		Label label = new Label(info, labelStyle);
		label.setWrap(true);
		label.setWidth(Gdx.graphics.getWidth());
		label.setAlignment(Align.center);
		dialog.getContentTable().add(label).width(Gdx.graphics.getWidth());

		// все эти литералы наугод подобраны и используются только здесь т.ч.
		// хер с ними
		dialog.padTop(Gdx.graphics.getWidth() * 0.1f).padBottom(
				Gdx.graphics.getWidth() * 0.05f);
		dialog.center();
		dialog.setMovable(false);

		// кнопка да
		ButtonStyle style = new ButtonStyle();

		// кнопка нет
		style = new ButtonStyle();
		style.up = ok;
		style.up.setMinWidth(Gdx.graphics.getWidth() * 0.3f);
		style.up.setMinHeight(Gdx.graphics.getWidth() * 0.15f);
		style.down = okDown;
		style.down.setMinWidth(Gdx.graphics.getWidth() * 0.3f);
		style.down.setMinHeight(Gdx.graphics.getWidth() * 0.15f);
		Button dbutton = new Button(style);
		dbutton.setSize(Gdx.graphics.getWidth() * 0.3f,
				Gdx.graphics.getWidth() * 0.15f);
		dialog.button(dbutton, null);
		dialog.setWidth(Gdx.graphics.getWidth());
		dialog.setHeight(label.getHeight() + dbutton.getHeight() + Gdx.graphics.getWidth()*0.5f);
		dialog.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		stage.addActor(dialog);
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
