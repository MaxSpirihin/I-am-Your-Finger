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
import com.yourfinger.game.UIScreens.ChooseLevelScreen;
import com.yourfinger.game.UIScreens.MainMenuScreen;


public class BuyFullScreen implements Screen {
	final float RELATIVE_SIZE_OF_MANAGE_BUTTON = 0.22f;


	// ключевые объекты
	Main game;
	Stage stage;

	Button btnBack;
	Button btnRate;

	Sprite sprBackground;



	public BuyFullScreen(Main game) {
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
		AddPhotoAndDevInfo(scrollTable);
		AddLastAndYear(scrollTable);

		// суем ее в скролл-панель
		final ScrollPane scroller = new ScrollPane(scrollTable);
		scroller.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scroller.setPosition(0, 0);
		scroller.setScrollingDisabled(true, false);
		this.stage.addActor(scroller);

	}






	private void AddPhotoAndDevInfo(Table table) {
		Image image = new Image(game.GetMainAtlas().findRegion("fingers"));
		image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()*0.4f);

		table.add(image).padBottom(Gdx.graphics.getHeight()*0.02f);
		table.row();
	}






	private void AddLastAndYear(Table table) {
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		Label lblFrom = new Label(game.GetString("completely_win"), labelStyle);
		lblFrom.setAlignment(Align.left);
		lblFrom.setWidth(Gdx.graphics.getWidth());
		lblFrom.setWrap(true);
		table.add(lblFrom).width(Gdx.graphics.getWidth())
				.padBottom(Gdx.graphics.getHeight() * 0.02f);
		table.row();
		
		
		
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
		

		


	}



	private void CreateManageButtons() {
		Skin skin = new Skin();
		skin.addRegions(game.GetMainAtlas());

		
		ButtonStyle buttonStylePrevious = new ButtonStyle();
		buttonStylePrevious.up = skin.getDrawable("previous_up");
		buttonStylePrevious.down = skin.getDrawable("previous_down");
		buttonStylePrevious.checked = skin.getDrawable("previous_up");
		final Button btnPrevious = new Button(buttonStylePrevious);
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
						game.setScreen(new ChooseLevelScreen(game,
								1));
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

		stage.addActor(btnBack);
		stage.addActor(btnPrevious);

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
