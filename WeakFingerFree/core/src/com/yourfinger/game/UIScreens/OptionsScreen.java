package com.yourfinger.game.UIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;

public class OptionsScreen implements Screen {

	// константы расположения
	static final String ATLAS_DIRECTORY = "Textures\\MainMenu.atlas";

	static final float RELATIVE_INDENT = 0.05f;
	static final float RELATIVE_POSITION_OF_BUTTON = 0.25f;
	static final float RELATIVE_SIZE_OF_CHECKBOX = 0.15f;
	static final float RELATIVE_POSITION_OF_CHECKBOX = 0.8f;
	static final float RELATIVE_SIZE_OF_BACK_BUTTON = 0.3f;

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;

	// фон
	Sprite sprBackground;

	// UI
	Label lblMusic;
	Label lblVibro;



	public OptionsScreen(Main game) {

		// основные объекты
		this.game = game;
		atlas = game.GetMainAtlas();// new
									// TextureAtlas(Gdx.files.internal(ATLAS_DIRECTORY));
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// для реагирования нажатия на кнопки
		Gdx.input.setInputProcessor(stage);

		// создаем элементы ПИ
		CreateCheckButtonsAndLabels();
		CreateBackButton();
		CreateTextButtons();
		//CreateTextButtonsBeta();
		CreateBackgroundSprite();

	}



	@Override
	public void render(float delta) {
		// чистим экран
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		game.GetBatch().end();

		stage.draw();

	}



	void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("background"));

		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
		sprBackground.setPosition(0,
				Gdx.graphics.getHeight() - sprBackground.getHeight());
	}



	/**
	 * создаем чекбуттоны для включения и отключения звука и вибро, а также
	 * заголовок
	 */
	private void CreateCheckButtonsAndLabels() {

		// заголовок
		LabelStyle labelStyleTitle = new LabelStyle();
		labelStyleTitle.font = game.GetFont().menuFont;
		Label lblTitle = new Label(game.GetString("options"), labelStyleTitle);
		lblTitle.setAlignment(Align.center);
		lblTitle.setCenterPosition(
				Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * (1 - RELATIVE_INDENT)
						- lblTitle.getHeight() / 2);
		stage.addActor(lblTitle);

		// стиль для меток музыки и вибро
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().labelFont;

		// музыка
		lblMusic = new Label(game.GetString("music"), labelStyle);
		lblMusic.setCenterPosition(Gdx.graphics.getWidth()
				* RELATIVE_POSITION_OF_BUTTON,
				lblTitle.getY() - Gdx.graphics.getHeight() * RELATIVE_INDENT
						- lblMusic.getHeight() / 2);
		if (lblMusic.getX() < 0)
			lblMusic.setX(0);
		stage.addActor(lblMusic);

		// вибро
		lblVibro = new Label(game.GetString("vibro"), labelStyle);
		lblVibro.setCenterPosition(Gdx.graphics.getWidth()
				* RELATIVE_POSITION_OF_BUTTON,
				lblMusic.getY() - Gdx.graphics.getHeight() * RELATIVE_INDENT
						- lblVibro.getHeight() / 2);
		if (lblVibro.getX() < 0)
			lblVibro.setX(0);
		stage.addActor(lblVibro);

		// скин и стиль для чеков
		Skin skin = new Skin();
		skin.addRegions(atlas);
		CheckBoxStyle style = new CheckBoxStyle();
		style.checkboxOff = skin.getDrawable("off");
		style.checkboxOn = skin.getDrawable("on");
		style.checkboxOff.setMinWidth(Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_CHECKBOX);
		style.checkboxOff.setMinHeight(Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_CHECKBOX);
		style.checkboxOn.setMinWidth(Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_CHECKBOX);
		style.checkboxOn.setMinHeight(Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_CHECKBOX);
		style.font = game.GetFont().labelFont;

		// музыка
		CheckBox cbMusic = new CheckBox("", style);
		cbMusic.setCenterPosition(Gdx.graphics.getWidth()
				* RELATIVE_POSITION_OF_CHECKBOX, lblMusic.getCenterY());
		cbMusic.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_CHECKBOX,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_CHECKBOX);
		cbMusic.setChecked(PrefBuilder.MusicOn());

		cbMusic.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PrefBuilder.SetMusicOn(((CheckBox) actor).isChecked());
				// музыка из главного меню
				if (((CheckBox) actor).isChecked())
					game.PlayMainMusic();
				else
					game.PauseMainMusic();
			}
		});

		stage.addActor(cbMusic);

		// вибро
		CheckBox cbVibro = new CheckBox("", style);
		cbVibro.setCenterPosition(Gdx.graphics.getWidth()
				* RELATIVE_POSITION_OF_CHECKBOX, lblVibro.getCenterY());
		cbVibro.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_CHECKBOX,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_CHECKBOX);

		cbVibro.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				PrefBuilder.SetVibroOn(((CheckBox) actor).isChecked());
			}
		});
		cbVibro.setChecked(PrefBuilder.VibroOn());

		stage.addActor(cbVibro);
	}



	private void CreateBackButton() {
		Skin skin = new Skin();
		skin.addRegions(atlas);

		final Button btnBack;
		ButtonStyle buttonStyleBack = new ButtonStyle();
		buttonStyleBack.up = skin.getDrawable("back_up");
		buttonStyleBack.down = skin.getDrawable("back_down");
		btnBack = new Button(buttonStyleBack);
		btnBack.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BACK_BUTTON,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BACK_BUTTON);
		btnBack.setPosition(Gdx.graphics.getWidth() - btnBack.getWidth(), 0);

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

		stage.addActor(btnBack);
	}



	/**
	 * текстовые кнопки сброса прогресса
	 */
	private void CreateTextButtons() {
		// сброс рекордов
		TextButtonStyle styleRec = new TextButtonStyle();
		styleRec.font = game.GetFont().labelFont;
		styleRec.fontColor = Color.WHITE;

		final TextButton btnClearRecords = new TextButton(
				game.GetString("clear_records"), styleRec);
		btnClearRecords.setCenterPosition(Gdx.graphics.getWidth() / 2,
				lblVibro.getY() - Gdx.graphics.getHeight() * RELATIVE_INDENT
						- btnClearRecords.getHeight() / 2);
		btnClearRecords.center();
		btnClearRecords.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnClearRecords.getStyle().fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnClearRecords.getWidth())
						&& (y < btnClearRecords.getHeight()) && (x > 0)
						&& (y > 0)) {
					// диалог подтверждения
					CreateDialog(false);
				}
				btnClearRecords.getStyle().fontColor = Color.WHITE;

			}
		});
		stage.addActor(btnClearRecords);

		// сброс всего прогресса
		TextButtonStyle styleAll = new TextButtonStyle();
		styleAll.font = game.GetFont().labelFont;
		styleAll.fontColor = Color.WHITE;

		final TextButton btnClearAll = new TextButton(
				game.GetString("clear_all"), styleAll);
		btnClearAll.setCenterPosition(Gdx.graphics.getWidth() / 2,
				btnClearRecords.getY() - Gdx.graphics.getHeight()
						* RELATIVE_INDENT - btnClearAll.getHeight() / 2);
		btnClearAll.center();
		btnClearAll.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnClearAll.getStyle().fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnClearAll.getWidth())
						&& (y < btnClearAll.getHeight()) && (x > 0) && (y > 0)) {
					// диалог подтверждения
					CreateDialog(true);
				}
				btnClearAll.getStyle().fontColor = Color.WHITE;
			}
		});
		stage.addActor(btnClearAll);
	
	}
	
	
	
	



	/**
	 * создает диалог подтверждение
	 * 
	 * @param isDeleteAll
	 *            - если false - то это для удаления рекордов
	 */
	private void CreateDialog(final boolean isDeleteAll) {

		// надписи
		String title = game.GetString("are_you_sure");
		String info;
		if (isDeleteAll)
			info = game.GetString("clear_all_info");
		else
			info = game.GetString("clear_records_info");

		Skin skin = new Skin();
		skin.addRegions(atlas);

		// иконки кнопок
		Drawable no, noDown, yes, yesDown;

		try {
			no = skin.getDrawable("no_" + game.GetLocale());
			noDown = skin.getDrawable("no_down_" + game.GetLocale());
			yes = skin.getDrawable("yes_" + game.GetLocale());
			yesDown = skin.getDrawable("yes_down_" + game.GetLocale());
		} catch (Exception ex) {
			// язык по умолчанию, для локали не нашлось
			no = skin.getDrawable("no");
			noDown = skin.getDrawable("no_down");
			yes = skin.getDrawable("yes");
			yesDown = skin.getDrawable("yes_down");
		}

		// создаем диалог
		WindowStyle w = new WindowStyle();
		w.titleFont = game.GetFont().labelFont;
		w.titleFontColor = Color.WHITE;
		w.background = skin.getDrawable("dialog");
		final Dialog dialog = new Dialog(title, w) {

			protected void result(Object object) {

				if ((Boolean) object) {
					// удаляем что надо
					if (isDeleteAll)
						PrefBuilder.ClearAllProgress();
					else
						PrefBuilder.ClearRecords();
					game.setScreen(new MainMenuScreen(game));
				} else {
					// обновляем сцену
					stage.clear();
					CreateBackButton();
					CreateTextButtons();
					CreateCheckButtonsAndLabels();
				}
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
		style.up = yes;
		style.up.setMinWidth(Gdx.graphics.getWidth() * 0.3f);
		style.up.setMinHeight(Gdx.graphics.getWidth() * 0.15f);
		style.down = yesDown;
		style.down.setMinWidth(Gdx.graphics.getWidth() * 0.3f);
		style.down.setMinHeight(Gdx.graphics.getWidth() * 0.15f);
		Button dbutton = new Button(style);
		dbutton.setSize(Gdx.graphics.getWidth() * 0.3f,
				Gdx.graphics.getWidth() * 0.15f);
		dialog.button(dbutton, true);
		dialog.getButtonTable().getCell(dbutton)
				.padRight(Gdx.graphics.getWidth() * 0.15f);

		// кнопка нет
		style = new ButtonStyle();
		style.up = no;
		style.up.setMinWidth(Gdx.graphics.getWidth() * 0.3f);
		style.up.setMinHeight(Gdx.graphics.getWidth() * 0.15f);
		style.down = noDown;
		style.down.setMinWidth(Gdx.graphics.getWidth() * 0.3f);
		style.down.setMinHeight(Gdx.graphics.getWidth() * 0.15f);
		dbutton = new Button(style);
		dbutton.setSize(Gdx.graphics.getWidth() * 0.3f,
				Gdx.graphics.getWidth() * 0.15f);
		dialog.button(dbutton, false);
		dialog.setWidth(Gdx.graphics.getWidth());
		dialog.setHeight(dialog.getWidth() / 2);
		dialog.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		stage.addActor(dialog);
	}





	// ////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
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
		stage.dispose();
	}

}
