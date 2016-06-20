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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.GameScreens.GameArcadeScreen;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;

public class ChooseArcadeScreen implements Screen {

	// константы расположения
	static final String ATLAS_DIRECTORY = "Textures\\MainMenu.atlas";

	static final float RELATIVE_SIZE_OF_BACK_BUTTON = 0.3f;
	static final float RELATIVE_INDENT = 0.02f;
	private static final float RELATIVE_POSITION_OF_LABEL_RECORDS = 0.2f;

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;
	Sprite sprBackground;
	Label lblTitle;



	public ChooseArcadeScreen(Main game) {
		// основные объекты
		this.game = game;
		atlas = game.GetMainAtlas();// new
									// TextureAtlas(Gdx.files.internal(ATLAS_DIRECTORY));
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// для реагирования нажатия на кнопки
		Gdx.input.setInputProcessor(stage);

		CreateBackButton();
		CreateLabels();
		CreateTextButtons();
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



	private void CreateLabels() {
		// заголовок
		LabelStyle labelStyleTitle = new LabelStyle();
		labelStyleTitle.font = game.GetFont().menuFont;
		lblTitle = new Label(game.GetString("arcade"), labelStyleTitle);
		lblTitle.setAlignment(Align.center);
		lblTitle.setCenterPosition(
				Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * (1 - RELATIVE_INDENT)
						- lblTitle.getHeight() / 2);
		stage.addActor(lblTitle);

		LabelStyle labelStyleRecord = new LabelStyle();
		labelStyleRecord.font = game.GetFont().labelFont;
		Label lblRecord = new Label(game.GetString("main_record") + " "
				+ String.valueOf(PrefBuilder.GetMainRecord()), labelStyleRecord);
		lblRecord.setAlignment(Align.center);
		lblRecord.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * RELATIVE_POSITION_OF_LABEL_RECORDS);
		stage.addActor(lblRecord);
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



	private void CreateTextButtons() {
		// создаем табоицу и приписываем ей параметры
		Table table = new Table();
		table.center();
		table.padTop(RELATIVE_INDENT);
		table.align(Align.center);

		float height = 0;

		for (int i = 1; i <= PrefBuilder.GetComleteWorlds(); i++) {
			final int world = i;
			// кнопки и их обработчики
			final TextButtonStyle style = new TextButtonStyle();
			style.font = game.GetFont().labelFont;
			style.fontColor = Color.WHITE;
			final Button btn = new TextButton(game.GetString("world_"
					+ String.valueOf(world)), style);
			btn.addListener(new ClickListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.input.vibrate(20);
					style.fontColor = Color.GRAY;
					return true;
				};



				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					if ((x < btn.getWidth()) && (y < btn.getHeight())
							&& (x > 0) && (y > 0))
						game.setScreen(new GameArcadeScreen(game, world));
					else
						style.fontColor = Color.WHITE;
				}
			});

			height += btn.getHeight() + Gdx.graphics.getHeight()
					* RELATIVE_INDENT;
			table.add(btn).row()
					.padTop(Gdx.graphics.getHeight() * RELATIVE_INDENT);
		}
		table.setCenterPosition(Gdx.graphics.getWidth() / 2, lblTitle.getY()
				- Gdx.graphics.getHeight() * RELATIVE_INDENT * 2 - height / 2);
		stage.addActor(table);

		if (PrefBuilder.GetComleteWorlds() == 0) {
			LabelStyle labelStyle = new LabelStyle();
			labelStyle.font = game.GetFont().labelFont;
			Label lblPlayToOpen = new Label(game.GetString("play_to_open"),
					labelStyle);
			lblPlayToOpen.setAlignment(Align.center);
			lblPlayToOpen.setWidth(Gdx.graphics.getWidth());
			lblPlayToOpen.setWrap(true);
			lblPlayToOpen.setCenterPosition(Gdx.graphics.getWidth() / 2,
					lblTitle.getY() - Gdx.graphics.getHeight()
							* RELATIVE_INDENT * 2 - lblPlayToOpen.getHeight()
							/ 2);
			stage.addActor(lblPlayToOpen);
		}
	}



	// //////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////

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
		stage.dispose();
	}

}
