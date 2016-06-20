package com.yourfinger.game.UIScreens;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;

public class LoadingScreen implements Screen {

	Main game;
	Label lblLoad;
	Stage stage;
	TextureAtlas atlas;

	// фон
	Sprite sprBackground;

	// картиники
	ArrayList<TextureRegion> pictures;
	Sprite picture;
	float timeForPicture;
	final static float SIZE_OF_PITCURE = Gdx.graphics.getWidth() * 0.4f;
	private static final float TIME_ONE_PICTURE = 2f;



	public LoadingScreen(Main g) {
		game = g;

		atlas = new TextureAtlas("Textures\\Load.atlas");
		CreateBackgroundSprite();

		game.LoadResources();

		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// загрузка
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().menuFont;
		lblLoad = new Label(game.GetString("loading"), labelStyle);
		lblLoad.setAlignment(Align.center);
		lblLoad.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.45f);
		stage.addActor(lblLoad);

		// картинки
		pictures = new ArrayList<TextureRegion>();
		for (int i = 0; atlas.findRegion(String.valueOf(i)) != null; i++)
			pictures.add(atlas.findRegion(String.valueOf(i)));
		picture = new Sprite(
				pictures.get(new Random().nextInt(pictures.size())));
		picture.setSize(SIZE_OF_PITCURE, SIZE_OF_PITCURE);
		picture.setOrigin(SIZE_OF_PITCURE/2, SIZE_OF_PITCURE/2);
		picture.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		timeForPicture = -1;

		// заголовок
		labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().bigLabelFont;
		labelStyle.fontColor = Color.RED;
		Label lblTitle = new Label(game.GetString("app_name"), labelStyle);
		lblTitle.setAlignment(Align.center);
		lblTitle.setWrap(true);
		lblTitle.setWidth(Gdx.graphics.getWidth());
		lblTitle.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.8f);
		stage.addActor(lblTitle);

		// год
		labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().labelFont;
		Label lblYear = new Label(game.GetString("year"), labelStyle);
		lblYear.setAlignment(Align.center);
		lblYear.setWidth(Gdx.graphics.getWidth());
		lblYear.setPosition(0, 0);
		stage.addActor(lblYear);

	}



	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		PaintPicture(game.GetBatch(), delta);
		game.GetBatch().end();

		lblLoad.setText(game.GetString("loading") + " "
				+ String.valueOf((int) (game.getProgress() * 100)) + "%");
		stage.draw();

		if (game.LoadHasDone()) {

			// PrefBuilder.SetComleteLevels(5);
			// PrefBuilder.SetComleteWorlds(3);
			game.CreateResources();
			game.setScreen(new MainMenuScreen(game));
		}

	}



	void PaintPicture(Batch batch, float delta) {
		timeForPicture -= delta;
		
		if (timeForPicture < 0) {
			picture.setRegion(pictures.get(new Random().nextInt(pictures.size())));
			switch (new Random().nextInt(4)) {
			case 0:
				// лево
				picture.setPosition(0,
						(Gdx.graphics.getHeight() - SIZE_OF_PITCURE)
								* (new Random().nextFloat()));
				picture.setRotation(270);
				break;
			case 1:
				// право
				picture.setPosition(Gdx.graphics.getWidth() - SIZE_OF_PITCURE,
						(Gdx.graphics.getHeight() - SIZE_OF_PITCURE)
								* (new Random().nextFloat()));
				picture.setRotation(90);
				break;
			case 2:
				// низ
				picture.setPosition((Gdx.graphics.getWidth() - SIZE_OF_PITCURE)
						* (new Random().nextFloat()), 0);
				picture.setRotation(0);
				break;
			case 3:
				// верх
				picture.setPosition((Gdx.graphics.getWidth() - SIZE_OF_PITCURE)
						* (new Random().nextFloat()), Gdx.graphics.getHeight()
						- SIZE_OF_PITCURE);
				picture.setRotation(180);
				break;
			}
			timeForPicture = TIME_ONE_PICTURE;
		}
		
		picture.draw(batch);


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
