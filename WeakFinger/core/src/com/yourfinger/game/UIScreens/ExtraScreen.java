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
import com.yourfinger.game.ExtraUIScreens.ArtsScreen;
import com.yourfinger.game.ExtraUIScreens.AuthorsScreen;
import com.yourfinger.game.ExtraUIScreens.FactsScreen;
import com.yourfinger.game.ExtraUIScreens.MusicScreen;
import com.yourfinger.game.ImportantClasses.Constants;

public class ExtraScreen implements Screen {

	static final float RELATIVE_INDENT = 0.02f;
	static final float RELATIVE_SIZE_OF_BACK_BUTTON = 0.3f;

	// константы расположения
	static final String ATLAS_DIRECTORY = "Textures\\MainMenu.atlas";

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;

	// фон
	Sprite sprBackground;

	// метки и кнопки
	Label lblTitle;
	



	public ExtraScreen(Main game) {

		// создаем основные объекты
		this.game = game;
		atlas = game.GetMainAtlas();//new TextureAtlas(Gdx.files.internal(ATLAS_DIRECTORY));
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		CreateBackgroundSprite();
		CreateBackButton();
		CreateLabelTitle();
		CreateTable();
		// для реагирования нажатия на кнопки
		Gdx.input.setInputProcessor(stage);

	}



	void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("background"));

		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
		sprBackground.setPosition(0,
				Gdx.graphics.getHeight() - sprBackground.getHeight());
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



	private void CreateLabelTitle() {
		// заголовок
		LabelStyle labelStyleTitle = new LabelStyle();
		labelStyleTitle.font = game.GetFont().menuFont;
		lblTitle = new Label(game.GetString("extra"), labelStyleTitle);
		lblTitle.setAlignment(Align.center);
		lblTitle.setCenterPosition(
				Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * (1 - RELATIVE_INDENT)
						- lblTitle.getHeight() / 2);
		stage.addActor(lblTitle);
	}



	private void CreateTable() {
		// создаем табоицу и приписываем ей параметры
		Table table = new Table();
		table.center();
		table.padTop(RELATIVE_INDENT);
		table.align(Align.center);

		float height = 0;
		for (int i = 0; i < 4; i++) {
			// кнопки и их обработчики
			final int numberOfButton = i;
			final TextButtonStyle style = new TextButtonStyle();
			style.font = game.GetFont().labelFont;
			style.fontColor = Color.WHITE;
			
			String title = "";
			
			switch (i)
			{
			case 0:
				title = game.GetString("arts");
				break;
			case 1:
				title = game.GetString("music");
				break;
			case 2:
				title = game.GetString("facts");
				break;
			case 3:
				title = game.GetString("authors");
				break;
			}
			
			
			final Button btn = new TextButton(title, style);
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
					
					{
						switch (numberOfButton)
						{
						case 0:
							//арты
							game.setScreen(new ArtsScreen(game));
							break;
						case 1:
							//музыка
							game.setScreen(new MusicScreen(game));
							break;
						case 2:
							//факты
							game.setScreen(new FactsScreen(game));
							break;
						case 3:
							//автор
							//Gdx.net.openURI("http://maaaks777@mail.ru");
							game.setScreen(new AuthorsScreen(game));
						}
						style.fontColor = Color.WHITE;
						
					}
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

	}



	@Override
	public void dispose() {
		stage.dispose();
	}



	// //////////////////////////
	// ///////////////////////////
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
