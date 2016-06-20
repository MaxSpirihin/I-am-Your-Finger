package com.yourfinger.game.ExtraUIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;
import com.yourfinger.game.UIScreens.ExtraScreen;

public class ArtsScreen implements Screen {

	final float RELATIVE_SIZE_OF_MANAGE_BUTTON = 0.22f;
	private final static String ATLAS_DIRECTORY = "Textures\\Arts.atlas";

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;

	// арт - кртинка
	Sprite sprPicture;
	int currentPicture;
	int countOfPictures;

	Button btnNext;
	Button btnPrevious;
	Button btnBack;

	Sprite sprBackground;



	public ArtsScreen(Main game) {
		// создаем основные объекты
		this.game = game;

		// атлас грузится сейчас
		atlas = new TextureAtlas(Gdx.files.internal(ATLAS_DIRECTORY));

		currentPicture = 0;

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		CreateManageButtons();
		CreateArt();
		FindCountOfPicture();
		CreateBackgroundSprite();

		Gdx.input.setInputProcessor(stage);

	}



	private void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(game.GetMainAtlas().findRegion("background_ext"));
		sprBackground.setPosition(0, 0);
		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
	}



	private void FindCountOfPicture() {
		countOfPictures = PrefBuilder.GetComleteWorlds()+1;
		if (PrefBuilder.GetComleteWorlds() == 5)
			if (PrefBuilder.GetComleteLevels() > 1)
				countOfPictures++;
	}



	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		sprPicture.draw(game.GetBatch());
		game.GetBatch().end();
		stage.draw();
	}



	private void CreateArt() {
		sprPicture = new Sprite(
				atlas.findRegion(String.valueOf(currentPicture)));

		float realHeiToWid = (Gdx.graphics.getHeight() - Gdx.graphics
				.getWidth() * RELATIVE_SIZE_OF_MANAGE_BUTTON)
				/ Gdx.graphics.getWidth();

		float sprHeiToWid = sprPicture.getHeight() / sprPicture.getWidth();

		if (sprHeiToWid > realHeiToWid) {
			float height = Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
					* RELATIVE_SIZE_OF_MANAGE_BUTTON;
			sprPicture.setSize(height / sprHeiToWid, height);
		} else {
			sprPicture.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
					* sprHeiToWid);
		}

		sprPicture.setCenter(Gdx.graphics.getWidth() / 2,
				(Gdx.graphics.getHeight() + Gdx.graphics.getWidth()
						* RELATIVE_SIZE_OF_MANAGE_BUTTON) / 2);
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
					// след арт
					if (currentPicture < countOfPictures - 1) {
						currentPicture++;
						CreateArt();
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
					// пред арт
					if (currentPicture > 0) {
						currentPicture--;
						CreateArt();
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
		stage.addActor(btnNext);
		stage.addActor(btnPrevious);

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
		stage.dispose();
		atlas.dispose();
	}

}
