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
import com.yourfinger.game.GameScreens.GameBossScreen;
import com.yourfinger.game.GameScreens.GameLevelScreen;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;

//экран выбора уровней и миров.
public class ChooseLevelScreen implements Screen {

	// константы расположения
	static final String ATLAS_DIRECTORY = "Textures\\ChooseLevel\\";

	// константы количества.
	public static final int COUNT_OF_LEVELS = 12;
	final int COUNT_OF_ROWS = 3;
	final int COUNT_OF_VERTICAL_INDENTS = 5;
	final int COUNT_OF_HORIZONTAL_INDENTS = 8;
	final int COUNT_OF_LEVELS_IN_ROW = COUNT_OF_LEVELS / COUNT_OF_ROWS;

	// константы относительных размеров. Манипулированием ими, я добился того,
	// что экран прилично выглядит почти на любом экране (320*240 не очень, но
	// пойдет)
	final float RELATIVE_SIZE_OF_LABEL = 0.22f;
	final float RELATIVE_SIZE_OF_MANAGE_BUTTON = 0.22f;
	final float RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON = 0.25f;
	final float RELATIVE_SIZE_OF_START_COMIX_BUTTON = 0.2f;
	final float RELATIVE_SIZE_OF_LEVEL_BUTTON = 0.19f;

	// вертикальный отступ между рядами таблицы
	float vertical_indent;
	// горизонтальный отступ между кнопками таблицы
	float horizontal_indent;

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;
	int worldNumber;

	// фон
	Sprite sprBackground;
	// название мира
	Label lblWorldName;

	// кнопки
	Button btnNext;
	Button btnPrevious;
	Button btnBack;
	Button btnBoss;
	Button btnComixBegin;
	Button btnComixBeforeBoss;
	// таблица
	Table table;



	public ChooseLevelScreen(Main game, int worldNumber) {

		// создаем основные объекты
		this.game = game;
		atlas = game.getChooseAtlas(worldNumber);
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());
		this.worldNumber = worldNumber;

		// вычисляем отступы, исходя из размеров экрана
		vertical_indent = ComputeVerticalIndent();
		horizontal_indent = ComputeHorizontalIndent();

		// создание всех эдементов
		CreateBackgroundSprite();
		CreateLabelWorldName();
		CreateManageButtons();
		CreateTable();
		CreateBossAndComixButtons();

		// добавление элементов на сцену
		stage.addActor(lblWorldName);
		stage.addActor(btnNext);
		stage.addActor(btnPrevious);
		stage.addActor(btnBack);
		stage.addActor(table);
		stage.addActor(btnBoss);
		stage.addActor(btnComixBegin);
		stage.addActor(btnComixBeforeBoss);

		// для реагирования нажатия на кнопки
		Gdx.input.setInputProcessor(stage);
		
		if (!PrefBuilder.GetDialogWas(worldNumber))
		{
			CreateDialog();
			PrefBuilder.SetDialogWas(worldNumber, true);
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



	// МЕТОДЫ ВЫЧИСЛЕНИЯ////////////////////
	float ComputeVerticalSizeOfTable() {
		return (Gdx.graphics.getHeight()
				- Gdx.graphics.getWidth()
				* (this.RELATIVE_SIZE_OF_LABEL
						+ this.RELATIVE_SIZE_OF_MANAGE_BUTTON + this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON) + vertical_indent);
	}



	float ComputeVerticalIndent() {

		return ((Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
				* (this.RELATIVE_SIZE_OF_LABEL
						+ this.RELATIVE_SIZE_OF_MANAGE_BUTTON
						+ this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON + this.RELATIVE_SIZE_OF_LEVEL_BUTTON
						* COUNT_OF_ROWS)) / COUNT_OF_VERTICAL_INDENTS);

	}



	float ComputeHorizontalIndent() {
		return (Gdx.graphics.getWidth()
				* (1 - this.RELATIVE_SIZE_OF_LEVEL_BUTTON
						* this.COUNT_OF_LEVELS_IN_ROW) / this.COUNT_OF_HORIZONTAL_INDENTS);
	}



	float ComputeVerticalCenterOfTable() {
		return (this.ComputeVerticalSizeOfTable()
				/ 2
				+ Gdx.graphics.getWidth()
				* (this.RELATIVE_SIZE_OF_MANAGE_BUTTON + this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON) + vertical_indent);
	}



	// ///////////////////////////////////
	// //МЕТОДЫ СОЗДАНИЯ ОБЪЕКТОВ///////////
	void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("background"));
		sprBackground.setPosition(0, 0);
		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
	}



	void CreateLabelWorldName() {
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().labelFont;
		lblWorldName = new Label(game.GetString("world_"
				+ String.valueOf(worldNumber)), labelStyle);
		lblWorldName.setCenterPosition(Gdx.graphics.getWidth() * 0.3f,
				Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_LABEL / 2);
		lblWorldName.setSize(Gdx.graphics.getWidth() * 0.6f,
				Gdx.graphics.getWidth() * this.RELATIVE_SIZE_OF_LABEL);
		lblWorldName.setWrap(true);
		lblWorldName.setAlignment(Align.center);
		lblWorldName.setColor(game.GetTextColor(worldNumber));
	}



	void CreateBossAndComixButtons() {
		Skin skin = new Skin();
		skin.addRegions(atlas);

		// кнопка босс
		ButtonStyle buttonStyleBoss = new ButtonStyle();
		if ((PrefBuilder.GetComleteLevels() >= COUNT_OF_LEVELS)
				|| (PrefBuilder.GetComleteWorlds() >= worldNumber)) {
			buttonStyleBoss.up = skin.getDrawable("boss_up");
			buttonStyleBoss.down = skin.getDrawable("boss_down");
			buttonStyleBoss.checked = skin.getDrawable("boss_up");
		} else {
			buttonStyleBoss.up = skin.getDrawable("block_up");
			buttonStyleBoss.down = skin.getDrawable("block_down");
			buttonStyleBoss.checked = skin.getDrawable("block_up");
		}
		btnBoss = new Button(buttonStyleBoss);
		btnBoss.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON,
				Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON);

		btnBoss.setCenterPosition(Gdx.graphics.getWidth()
				* (0.85f - this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON / 2),
				Gdx.graphics.getWidth() * this.RELATIVE_SIZE_OF_MANAGE_BUTTON
						+ vertical_indent + Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON / 2);

		btnBoss.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnBoss.getWidth()) && (y < btnBoss.getHeight())
						&& (x > 0) && (y > 0)) {
					// уровень с боссом
					if ((PrefBuilder.GetComleteLevels() >= COUNT_OF_LEVELS)
							|| (PrefBuilder.GetComleteWorlds() >= worldNumber))
						game.setScreen(new GameBossScreen(game, worldNumber));
				}
			}
		});

		// кнопка к комиксу вверху
		ButtonStyle buttonStyleComix = new ButtonStyle();
		buttonStyleComix.up = skin.getDrawable("comix_up");
		buttonStyleComix.down = skin.getDrawable("comix_down");
		buttonStyleComix.checked = skin.getDrawable("comix_up");

		btnComixBegin = new Button(buttonStyleComix);
		btnComixBegin.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_START_COMIX_BUTTON,
				Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_START_COMIX_BUTTON);

		btnComixBegin.setCenterPosition(Gdx.graphics.getWidth() * 0.75f,
				Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_LABEL / 2);

		btnComixBegin.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnComixBegin.getWidth())
						&& (y < btnComixBegin.getHeight()) && (x > 0)
						&& (y > 0)) {
					// стартовый к миру сюжет
					game.setScreen(new ComixScreen(game, worldNumber, 1));
				}
			}
		});

		// кнопка к комиксу предбоссом
		ButtonStyle buttonStyleComixBeforeBoss = new ButtonStyle();
		if ((PrefBuilder.GetComleteLevels() >= COUNT_OF_LEVELS)
				|| (PrefBuilder.GetComleteWorlds() >= worldNumber)) {
			buttonStyleComixBeforeBoss.up = skin.getDrawable("comix_up");
			buttonStyleComixBeforeBoss.down = skin.getDrawable("comix_down");
			buttonStyleComixBeforeBoss.checked = skin.getDrawable("comix_up");
		} else {
			buttonStyleComixBeforeBoss.up = skin.getDrawable("block_up");
			buttonStyleComixBeforeBoss.down = skin.getDrawable("block_down");
			buttonStyleComixBeforeBoss.checked = skin.getDrawable("block_up");
		}

		btnComixBeforeBoss = new Button(buttonStyleComixBeforeBoss);
		btnComixBeforeBoss.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON,
				Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON);

		btnComixBeforeBoss.setCenterPosition(Gdx.graphics.getWidth()
				* (0.15f + this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON / 2),
				Gdx.graphics.getWidth() * this.RELATIVE_SIZE_OF_MANAGE_BUTTON
						+ vertical_indent + Gdx.graphics.getWidth()
						* this.RELATIVE_SIZE_OF_BOSS_AND_COMIX_BUTTON / 2);

		btnComixBeforeBoss.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnComixBeforeBoss.getWidth())
						&& (y < btnComixBeforeBoss.getHeight()) && (x > 0)
						&& (y > 0)) {
					// предбоссовый сюжет
					if ((PrefBuilder.GetComleteLevels() >= COUNT_OF_LEVELS)
							|| (PrefBuilder.GetComleteWorlds() >= worldNumber))
						game.setScreen(new ComixScreen(game, worldNumber, 2));
				}
			}
		});

	}



	void CreateTable() {
		// создаем табоицу и приписываем ей параметры
		table = new Table();
		table.center();
		table.padTop(vertical_indent).padLeft(horizontal_indent)
				.padRight(horizontal_indent);
		table.setCenterPosition(Gdx.graphics.getWidth() / 2,
				this.ComputeVerticalCenterOfTable());

		// создаем стили для кнопок таблицы
		Skin skin = new Skin();
		skin.addRegions(atlas);
		TextButtonStyle style = new TextButtonStyle();
		style.font = game.GetFont().labelFont;
		style.up = skin.getDrawable("level_up");
		style.down = skin.getDrawable("level_down");
		style.checked = skin.getDrawable("level_up");
		style.fontColor = game.GetButtonColor(worldNumber);
		TextButtonStyle styleBlock = new TextButtonStyle();
		styleBlock.font = game.GetFont().labelFont;
		styleBlock.up = skin.getDrawable("block_up");
		styleBlock.down = skin.getDrawable("block_down");
		styleBlock.checked = skin.getDrawable("block_up");
		TextButton btnLevel;

		// создаем кнопки, перенаправляющие на уровень и суем в таблицу
		for (int i = 0; i < COUNT_OF_LEVELS; i++) {
			final int levelNumber = i + 1;

			// если уровень не открыт, то это кнопка с замком
			if ((levelNumber <= PrefBuilder.GetComleteLevels() + 1)
					|| (PrefBuilder.GetComleteWorlds() >= worldNumber)) {
				btnLevel = new TextButton(String.valueOf(levelNumber), style);
			} else {
				btnLevel = new TextButton("", styleBlock);
			}

			btnLevel.addListener(new ClickListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.input.vibrate(20);
					return true;
				};



				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					if ((x < RELATIVE_SIZE_OF_LEVEL_BUTTON
							* Gdx.graphics.getWidth())
							&& (y < RELATIVE_SIZE_OF_LEVEL_BUTTON
									* Gdx.graphics.getWidth())
							&& (x > 0)
							&& (y > 0)) {
						if ((levelNumber <= PrefBuilder.GetComleteLevels() + 1)
								|| (PrefBuilder.GetComleteWorlds() >= worldNumber)) {
							game.setScreen(new GameLevelScreen(game,
									worldNumber, levelNumber));
						}
					}
				}
			});

			table.add(btnLevel)
					.width(RELATIVE_SIZE_OF_LEVEL_BUTTON
							* Gdx.graphics.getWidth())
					.height(RELATIVE_SIZE_OF_LEVEL_BUTTON
							* Gdx.graphics.getWidth());

			// это для разделения столбцов
			if (levelNumber % 4 == 0)
				table.row().padTop(vertical_indent).padLeft(horizontal_indent)
						.padRight(horizontal_indent);
		}

	}



	void CreateManageButtons() {
		Skin skin = new Skin();
		skin.addRegions(atlas);
		ButtonStyle buttonStyleNext = new ButtonStyle();
		buttonStyleNext.up = skin.getDrawable("next_up");
		buttonStyleNext.down = skin.getDrawable("next_down");
		buttonStyleNext.checked = skin.getDrawable("next_up");
		btnNext = new Button(buttonStyleNext);
		btnNext.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnNext.setCenterPosition(Gdx.graphics.getWidth() * 0.4f,
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
					// след мир
					if (PrefBuilder.GetComleteWorlds() >= worldNumber)
						if (worldNumber > 0)
							game.setScreen(new BuyFullScreen(game));
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
		btnPrevious.setCenterPosition(Gdx.graphics.getWidth() * 0.15f,
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
					// пред мир
					if (worldNumber > 1)
						game.setScreen(new ChooseLevelScreen(game,
								worldNumber - 1));
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
		btnBack.setCenterPosition(Gdx.graphics.getWidth() * 0.85f,
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
					game.setScreen(new MainMenuScreen(game));
					dispose();
				}
			}
		});

	}



	private void CreateDialog() {

		// надписи
		String title = game.GetString("good_job");
		String info = game.GetString("good_job_text1") + " \"" + game.GetString("world_"
				+ String.valueOf(worldNumber - 1)) + "\"  " + game.GetString("good_job_text2");

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
					game.setScreen(new ChooseLevelScreen(game, worldNumber));
				
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
		dialog.setHeight(dialog.getWidth() / 2);
		dialog.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		stage.addActor(dialog);
	}



	// ///////////////////////////////////////////////////////
	// ///////////////////////////////////////////

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
