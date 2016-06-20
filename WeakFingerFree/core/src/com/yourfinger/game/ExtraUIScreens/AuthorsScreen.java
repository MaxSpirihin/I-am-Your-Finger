package com.yourfinger.game.ExtraUIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.ImportantClasses.Constants;
import com.yourfinger.game.UIScreens.ExtraScreen;

public class AuthorsScreen implements Screen {

	final float RELATIVE_SIZE_OF_MANAGE_BUTTON = 0.22f;


	// ключевые объекты
	Main game;
	Stage stage;

	Button btnBack;
	Button btnRate;

	Sprite sprBackground;



	public AuthorsScreen(Main game) {
		// создаем основные объекты
		this.game = game;

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		CreateBackgroundSprite();
		CreatePanel();
		CreateManageButtons();

		Gdx.input.setInputProcessor(stage);
	}



	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		game.GetBatch().end();
		stage.draw();
		stage.act();

	}



	private void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(game.GetMainAtlas().findRegion(
				"background_ext"));
		sprBackground.setPosition(0, 0);
		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
	}



	private void CreatePanel() {

		// создвем и наполняем таблицу
		Table scrollTable = new Table();
		AddAppNameAndVersion(scrollTable);
		AddRateButton(scrollTable);
		AddPhotoAndDevInfo(scrollTable);
		AddHelpAboutFrom(scrollTable);
		AddLastAndYear(scrollTable);

		// суем ее в скролл-панель
		final ScrollPane scroller = new ScrollPane(scrollTable);
		scroller.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scroller.setPosition(0, 0);
		scroller.setScrollingDisabled(true, false);
		this.stage.addActor(scroller);

	}



	private void AddAppNameAndVersion(Table table) {
		// название игры
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().menuFont;
		labelStyle.fontColor = Color.RED;
		Label lblAppName = new Label(game.GetString("app_name"), labelStyle);
		lblAppName.setAlignment(Align.center);
		lblAppName.setWidth(Gdx.graphics.getWidth());
		lblAppName.setWrap(true);
		table.add(lblAppName);
		table.row();

		// версия
		labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;
		labelStyle.fontColor = Color.WHITE;
		Label lblVersion = new Label(game.GetString("version"), labelStyle);
		lblVersion.setAlignment(Align.center);
		lblVersion.setWidth(Gdx.graphics.getWidth());
		lblVersion.setWrap(true);
		table.add(lblVersion);
		table.row();

		// дата релиза
		labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;
		labelStyle.fontColor = Color.WHITE;
		Label lblDate = new Label(game.GetString("release_date"), labelStyle);
		lblDate.setAlignment(Align.center);
		lblDate.setWidth(Gdx.graphics.getWidth());
		lblDate.setWrap(true);
		table.add(lblDate);
		table.row();

	}



	private void AddRateButton(Table table) {
		// кнопки и их обработчики
		TextButtonStyle style = new TextButtonStyle();
		style.font = game.GetFont().labelFont;
		style.fontColor = Constants.LINK_COLOR;
		final TextButton btnRate = new TextButton(game.GetString("rate_game"),
				style);
		btnRate.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnRate.getStyle().fontColor = Constants.LINK_PRESSED_COLOR;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnRate.getWidth()) && (y < btnRate.getHeight())
						&& (x > 0) && (y > 0))
					Gdx.net.openURI(game.GetString("link_self"));
				btnRate.getStyle().fontColor = Constants.LINK_COLOR;
			}
		});

		table.add(btnRate).right().padBottom(Gdx.graphics.getHeight() * 0.06f);
		table.row();
	}



	private void AddPhotoAndDevInfo(Table table) {
		Image image = new Image(game.GetMainAtlas().findRegion("author"));
		image.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth() / 2);

		Table tableTexts = new Table();

		// разраб
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;
		Label lblAppName = new Label(game.GetString("developed_by"), labelStyle);
		lblAppName.setAlignment(Align.center);
		lblAppName.setWidth(Gdx.graphics.getWidth() / 2);
		lblAppName.setWrap(true);
		tableTexts.add(lblAppName);
		tableTexts.row();

		// вк
		TextButtonStyle style = new TextButtonStyle();
		style.font = game.GetFont().textFont;
		style.fontColor = Constants.LINK_COLOR;
		final TextButton btnVk = new TextButton(game.GetString("vk"), style);
		btnVk.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnVk.getStyle().fontColor = Constants.LINK_PRESSED_COLOR;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnVk.getWidth()) && (y < btnVk.getHeight())
						&& (x > 0) && (y > 0))
					Gdx.net.openURI("https://vk.com/maaaks777");
				btnVk.getStyle().fontColor = Constants.LINK_COLOR;
			}
		});
		btnVk.setWidth(Gdx.graphics.getWidth() / 2);

		tableTexts.add(btnVk);
		tableTexts.row();

		// мыло
		labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;
		Label lblMail = new Label(game.GetString("mail"), labelStyle);
		lblMail.setAlignment(Align.center);
		lblMail.setWidth(Gdx.graphics.getWidth() / 2);
		lblMail.setWrap(true);
		tableTexts.add(lblMail);
		tableTexts.row();

		// заполнение таблицы
		Table tableNow = new Table();
		tableNow.add(tableTexts).width(Gdx.graphics.getWidth() * 0.6f)
				.height(Gdx.graphics.getWidth() / 2);
		tableNow.add(image).width(Gdx.graphics.getWidth() * 0.4f)
				.height(Gdx.graphics.getWidth() / 2);
		table.add(tableNow).padBottom(Gdx.graphics.getHeight()*0.02f);
		table.row();
	}



	private void AddHelpAboutFrom(Table table) {

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		// об авторе
		Label lblAbout = new Label(game.GetString("about_author"), labelStyle);
		lblAbout.setAlignment(Align.left);
		lblAbout.setWidth(Gdx.graphics.getWidth());
		lblAbout.setWrap(true);
		table.add(lblAbout).width(Gdx.graphics.getWidth())
				.padBottom(Gdx.graphics.getHeight() * 0.02f);
		table.row();

		// от автора
		Label lblFrom = new Label(game.GetString("from_author"), labelStyle);
		lblFrom.setAlignment(Align.left);
		lblFrom.setWidth(Gdx.graphics.getWidth());
		lblFrom.setWrap(true);
		table.add(lblFrom).width(Gdx.graphics.getWidth())
				.padBottom(Gdx.graphics.getHeight() * 0.02f);
		table.row();

	}



	private void AddLastAndYear(Table table) {
		
		TextButtonStyle style = new TextButtonStyle();
		style.font = game.GetFont().labelFont;
		style.fontColor = Constants.LINK_COLOR;
		final TextButton btnFull = new TextButton(
				game.GetString("full_version"), style);
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
						&& (x > 0) && (y > 0))
					Gdx.net.openURI(game.GetString("link"));
				btnFull.getStyle().fontColor = Constants.LINK_COLOR;
			}
		});

		table.add(btnFull).center().padBottom(Gdx.graphics.getHeight() * 0.05f);
		table.row();
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		Label lblFrom = new Label(game.GetString("about_last_projects"), labelStyle);
		lblFrom.setAlignment(Align.left);
		lblFrom.setWidth(Gdx.graphics.getWidth());
		lblFrom.setWrap(true);
		table.add(lblFrom).width(Gdx.graphics.getWidth())
				.padBottom(Gdx.graphics.getHeight() * 0.02f);
		table.row();
		
		
		style = new TextButtonStyle();
		style.font = game.GetFont().textFont;
		style.fontColor = Constants.LINK_COLOR;
		final TextButton btnLast = new TextButton(
				game.GetString("last_projects"), style);
		btnLast.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				btnLast.getStyle().fontColor = Constants.LINK_PRESSED_COLOR;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnLast.getWidth()) && (y < btnLast.getHeight())
						&& (x > 0) && (y > 0))
					Gdx.net.openURI("https://play.google.com/store/apps/developer?id=%D0%A1%D0%BF%D0%B8%D1%80%D0%B8%D1%85%D0%B8%D0%BD+%D0%9C%D0%B0%D0%BA%D1%81%D0%B8%D0%BC");
				btnLast.getStyle().fontColor = Constants.LINK_COLOR;
			}
		});

		table.add(btnLast).left().padBottom(Gdx.graphics.getHeight() * 0.05f);
		table.row();

		// год
		labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().labelFont;
		Label lblYear = new Label(game.GetString("year"), labelStyle);
		lblYear.setAlignment(Align.center);
		lblYear.setWidth(Gdx.graphics.getWidth());
		lblYear.setWrap(true);
		table.add(lblYear);
		table.row();

	}



	private void CreateManageButtons() {
		Skin skin = new Skin();
		skin.addRegions(game.GetMainAtlas());

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
					game.setScreen(new ExtraScreen(game));
					dispose();
				}
			}
		});

		stage.addActor(btnBack);

	}



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



	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
