package com.yourfinger.game.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.ImportantClasses.Constants;
import com.yourfinger.game.UIScreens.ChooseFinalScreen;
import com.yourfinger.game.UIScreens.MainMenuScreen;

public class FinalScriptScreen implements Screen {

	// основа
	Main game;
	Stage stage;
	float time;
	int state;
	TextureAtlas atlas;
	Sprite sprBackground;
	TextButton btnSkip;

	// 1 состояние - таймер
	final static float TIMER_TIME = 6f;
	final static int STATE_TIMER = 1;
	Label lblTimer;

	// 2 состояние - появление спрайта мертвеца
	final static float DEAD_TIME_APPEARENCE = 4f;
	final static float DEAD_TIME_SHOW = 2f;
	final static int STATE_DEAD = 2;
	Sprite sprDead;

	// 3 состояние - комиксы
	final static int STATE_COMIX = 3;
	final static int COMIX_COUNT = 4;
	final static float ONE_COMIX_TIME = 4f;
	int comixNumber;
	Sprite sprComix;

	// 4 состояние - исчезновение
	final static float TIME_DISAPPEARENCE = 10f;
	final static float TIME_SHOW = 2f;
	final static int STATE_DISAPPEARENCE = 4;
	Sprite sprWithout;
	Sprite sprWith;

	// 5 состояние - показ названия
	final static float TIME_TITLE = 6f;
	final static int STATE_TITLE = 5;
	Sprite sprTitle;

	// 6 состояние - титр
	final static float TITR_SPEED = Gdx.graphics.getHeight() / 9.5f;
	final static int STATE_TITR = 6;
	Label lblTitr;



	public FinalScriptScreen(Main game) {
		this.game = game;
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());
		atlas = new TextureAtlas(
				Gdx.files.internal("Textures/FinalScript.atlas"));
		
	
		CreateBackgroundSprite();
		CreateSkipButton();
		PrepareTimerState();

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

		// ОТРИСОВКА
		switch (state) {
		case STATE_TIMER:
			break;
		case STATE_DEAD:
			stage.draw();
			game.GetBatch().begin();
			sprDead.draw(game.GetBatch());
			game.GetBatch().end();
			break;
		case STATE_COMIX:
			game.GetBatch().begin();
			sprComix.draw(game.GetBatch());
			game.GetBatch().end();
			break;
		case STATE_DISAPPEARENCE:
			game.GetBatch().begin();
			sprWithout.draw(game.GetBatch());
			sprWith.draw(game.GetBatch());
			game.GetBatch().end();
			break;
		case STATE_TITLE:
			game.GetBatch().begin();
			sprTitle.draw(game.GetBatch());
			game.GetBatch().end();
			break;
		case STATE_TITR:
			break;
		}

		// АКТ
		switch (state) {
		case STATE_TIMER:
			time -= delta;
			if (time > 1)
				lblTimer.setText("00'00'0" + String.valueOf((int) time));
			else {
				lblTimer.setText(game.GetString("disabled"));
			}
			if (time < 0)
				PrepareForStateDead();
			break;
		case STATE_DEAD:
			time -= delta;
			if (time > DEAD_TIME_SHOW)
				sprDead.setAlpha(1 - (time - DEAD_TIME_SHOW)
						/ DEAD_TIME_APPEARENCE);
			else
				sprDead.setAlpha(1);

			if (time < 0)
				PrepareForComixState();
			break;
		case STATE_COMIX:
			time -= delta;
			if (time < 0) {
				if (comixNumber < 4) {
					time = ONE_COMIX_TIME;
					comixNumber++;
					try {
						sprComix.setRegion(atlas.findRegion(String
								.valueOf(comixNumber) + "_" + game.GetLocale()));
					} catch (Exception ex) {
						sprComix.setRegion(atlas.findRegion(String
								.valueOf(comixNumber)));
					}
				} else
					PrepareForStateDisappearence();
			}
			break;
		case STATE_DISAPPEARENCE:
			time -= delta;
			if (time > TIME_SHOW)
				sprWith.setAlpha((time - TIME_SHOW) / TIME_DISAPPEARENCE);
			else
				sprWith.setAlpha(0);

			if (time < 0)
				PrepareTitleState();
			break;
		case STATE_TITLE:
			time -= delta;
			if (time < 0)
				PrepareForTitrState();
			break;
		case STATE_TITR:
			lblTitr.setY(lblTitr.getY() + delta * TITR_SPEED);
			if (lblTitr.getY() > Gdx.graphics.getHeight())
			{
				game.PauseMainMusic();
				game.setScreen(new MainMenuScreen(game));
			}
			break;
		}
	}



	@Override
	public void dispose() {
		atlas.dispose();
	}



	void CreateSkipButton() {
		
		final TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = game.GetFont().textFont;
		buttonStyle.fontColor = Color.WHITE;
		
		btnSkip = new TextButton(game.GetString("skip"),buttonStyle);
		btnSkip.setPosition(Gdx.graphics.getWidth() - btnSkip.getWidth(),
				btnSkip.getHeight() / 2);

		btnSkip.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				buttonStyle.fontColor = Color.GRAY;
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnSkip.getWidth()) && (y < btnSkip.getHeight())
						&& (x > 0) && (y > 0)) {
					game.PauseMainMusic();
					game.setScreen(new ChooseFinalScreen(game));
					dispose();
				}
				else
					buttonStyle.fontColor = Color.WHITE;
			}
		});

	}



	void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("background"));

		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
		sprBackground.setPosition(0,
				Gdx.graphics.getHeight() - sprBackground.getHeight());
	}



	private void PrepareTimerState() {
		time = TIMER_TIME;
		state = STATE_TIMER;

		// создаем стили кнопок и меток
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().bigLabelFont;

		// метка имя босса
		lblTimer = new Label("00'00'0" + String.valueOf((int) time), labelStyle);
		lblTimer.setAlignment(Align.center);
		lblTimer.setWidth(Gdx.graphics.getWidth());
		lblTimer.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - lblTimer.getHeight() / 2);
		lblTimer.setColor(Color.RED);

		stage.addActor(lblTimer);
		stage.addActor(btnSkip);
	}



	private void PrepareForStateDead() {
		time = DEAD_TIME_APPEARENCE + DEAD_TIME_SHOW;
		state = STATE_DEAD;

		sprDead = new Sprite(atlas.findRegion("dead"));
		sprDead.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getWidth() * 4 / 3);
		sprDead.setPosition(0, lblTimer.getY() - sprDead.getHeight());
		sprDead.setAlpha(0);
		
		stage.clear();
		stage.addActor(btnSkip);
	}



	private void PrepareForComixState() {
		state = STATE_COMIX;
		time = ONE_COMIX_TIME;
		comixNumber = 1;
		try {
			sprComix = new Sprite(atlas.findRegion("1_" + game.GetLocale()));
		} catch (Exception ex) {
			sprComix = new Sprite(atlas.findRegion("1"));
		}

		sprComix.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		sprComix.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}



	private void PrepareForStateDisappearence() {
		time = TIME_DISAPPEARENCE + TIME_SHOW;
		state = STATE_DISAPPEARENCE;

		sprWithout = new Sprite(atlas.findRegion("fingerAfter"));
		sprWithout.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getWidth() * 4 / 3);
		sprWithout.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		sprWith = new Sprite(atlas.findRegion("fingerBefore"));
		sprWith.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getWidth() * 4 / 3);
		sprWith.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

	}



	private void PrepareTitleState() {
		time = TIME_TITLE;
		state = STATE_TITLE;

		sprTitle = new Sprite(atlas.findRegion("title"));
		sprTitle.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		sprTitle.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}



	private void PrepareForTitrState() {
		state = STATE_TITR;
		// создаем стили кнопок и меток
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().labelFont;

		lblTitr = new Label(game.GetString("titr"), labelStyle);
		lblTitr.setAlignment(Align.center);
		lblTitr.setWidth(Gdx.graphics.getWidth());
		lblTitr.setWrap(true);
		lblTitr.setCenterPosition(Gdx.graphics.getWidth() / 2,
				-lblTitr.getHeight() / 2);
		lblTitr.setColor(Color.WHITE);

		stage.clear();
		stage.addActor(btnSkip);
		stage.addActor(lblTitr);
	}



	// ///////////////////////////////////////
	// ///////////////////////////////////

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
