package com.yourfinger.game.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.Bosses.Alpha;
import com.yourfinger.game.Bosses.DeadAlphaScript;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Boss;
import com.yourfinger.game.UIScreens.ChooseFinalScreen;

public class GameAlphaScreen implements Screen, InputProcessor {

	// ��������� ������� ����
	final int GAME_NOT_STARTED = 0;
	final int GAME_IS = 1;
	final int GAME_SCRIPT = 2;
	final int GAME_OVER = 3;
	

	// ��������� ������� � ������� ������ � ��������
	final float RELATIVE_SIZE_OF_BUTTON = 0.25f;
	final float RELATIVE_SIZE_OF_LIVE = 0.1f;
	final float RELATIVE_POSITION_OF_FIRST_BUTTON = 0.2f;
	final float RELATIVE_INDENT_OF_BOSS_SAY = 0.05f;

	final float ANIMATION_BOSS_WIN_TIME = 0.2f;

	// ����� ��������
	TextureAtlas atlas;
	Music music;
	Boss boss;

	// �������� �������
	Main game;
	Stage stage;
	Finger finger;
	DeadAlphaScript script;

	// ����������
	int gameStatus;
	float time;

	// �������
	Sprite sprBackground;
	Animation bossWinAnimation;
	Sprite sprBossWin;
	Sprite sprBossStartSkin;

	// ������ � �����
	Button btnRestart;
	Button btnMenu;
	Label lblStart;
	Label lblBossInfo;
	Label lblLose;



	public GameAlphaScreen(Main game) {
		this.game = game;
		atlas = game.getWorldAtlas(6);

		// ������� �����
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());
		music = game.GetFinalMusic();
		music.setLooping(true);

		PrepareForGameNotStarted();
	}



	@Override
	public void render(float delta) {
		time += delta;

		// ������ ����� � �������������� ���
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		PrintBackground();

		// ���������� �������� ������� �� ��������� ����
		switch (gameStatus) {

		case GAME_NOT_STARTED:
			// ���� ��� �� �������� � ������� �������
			game.GetBatch().begin();
			sprBossStartSkin.draw(game.GetBatch());
			game.GetBatch().end();
			stage.draw();
			break;

		case GAME_IS:
			// ���� ����
			stage.draw();

			// ��������� �� ������
			if (boss.FingerIsDead()) {
				PrepareForGameOver();
				return;
			}

			if (boss.IsDead()) {
				PrepareForScript();
			}

			stage.act(Gdx.graphics.getDeltaTime());

			break;
			
		case GAME_SCRIPT:
			if (script.IsDone())
				PrepareForWin();
			stage.draw();
			stage.act();
			break;

		case GAME_OVER:
			// ����� ���������
			game.GetBatch().begin();
			sprBossWin.setRegion(bossWinAnimation.getKeyFrame(time));
			sprBossWin.draw(game.GetBatch());
			game.GetBatch().end();
			stage.draw();
			break;
		}
	}



	/**
	 * ������ ���
	 */
	void PrintBackground() {
		game.GetBatch().begin();
		float startY = sprBackground.getY();
		sprBackground.draw(game.GetBatch());
		while (sprBackground.getY() < Gdx.graphics.getHeight()) {
			sprBackground.setY(sprBackground.getY() + sprBackground.getHeight()
					- 2);
			sprBackground.draw(game.GetBatch());

		}
		sprBackground.setY(startY);
		game.GetBatch().end();
	}



	/**
	 * ��������� ����� � ����� ������� ���-�� ������
	 */
	void UpdateStage() {
		stage.clear();
		stage.addActor(boss);
	}



	// ���������� ������ ������
	void PrepareForWin() {

		if (PrefBuilder.GetComleteLevels() == 0)
			PrefBuilder.SetComleteLevels(1);
		
		game.setScreen(new ComixScreen(game, 6, 2));

	}



	// �������������� ��������� �����
	void PrepareForGameNotStarted() {

		gameStatus = GAME_OVER;

		// �������� ������
		if ((!music.isPlaying()) && (PrefBuilder.MusicOn()))
			music.play();

		gameStatus = GAME_NOT_STARTED;

		// ������ �����������
		Gdx.input.setInputProcessor(this);

		// ������� �������, ������ � �����
		this.CreateLabels();
		this.CreateButtons();
		this.CreateAllSprites();

		// ������� �����
		stage.clear();
		stage.addActor(lblStart);
		stage.addActor(lblBossInfo);
	}
	
	
	void PrepareForScript()
	{
		gameStatus = GAME_SCRIPT;
		script = new DeadAlphaScript(atlas, boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight());
		stage.clear();
		stage.addActor(script);
	}



	// �������������� ����� � ������ � ������ ����� ����
	void PrepareForGameOver() {

		time = 0;

		if (PrefBuilder.VibroOn())
			Gdx.input.vibrate(GameLevelScreen.VIBRO_TIME);

		gameStatus = GAME_OVER;

		// ������
		sprBossWin = new Sprite(atlas.findRegion("alpha_win"));

		// ������� ��������
		Array<TextureRegion> txtrBossWin = new Array<TextureRegion>();
		if (atlas.findRegion("alpha_win", 1) != null) {
			// ������ ����������
			int i = 1;
			while (atlas.findRegion("alpha_win", i) != null) {
				txtrBossWin.add(atlas.findRegion("alpha_win", i));
				i++;
			}
		} else {
			// ��� ������� ���� ���������
			txtrBossWin.add(atlas.findRegion("alpha_win"));
		}
		this.bossWinAnimation = new Animation(ANIMATION_BOSS_WIN_TIME,
				txtrBossWin);
		bossWinAnimation.setPlayMode(PlayMode.LOOP);

		sprBossWin.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		sprBossWin.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 2);

		// ������������ ����� ����� �.�. �� ��� ������
		Gdx.input.setInputProcessor(stage);

		// ������� �����
		stage.clear();
		stage.addActor(btnRestart);
		stage.addActor(btnMenu);
		stage.addActor(lblLose);
	}



	// ������� ��� ������ �������, ����� ������� ���������, �� ��������� ���
	// ���������
	void CreateAllSprites() {
		// ������� ���
		sprBackground = new Sprite(atlas.findRegion("background"));

		sprBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
				* sprBackground.getHeight() / sprBackground.getWidth());
		sprBackground.setPosition(0, 0);

		// ��������� ���� �����, ���� - �������!!!!
		sprBossStartSkin = new Sprite(atlas.findRegion("alpha_skin"));
		sprBossStartSkin.setSize(
				Gdx.graphics.getHeight() / 2 - lblBossInfo.getHeight() / 2,
				Gdx.graphics.getHeight() / 2 - lblBossInfo.getHeight() / 2);
		sprBossStartSkin.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - sprBossStartSkin.getHeight() / 2);

	}



	/**
	 * ������� ��� ������ � �����, ��������� � ������������, ������� ���
	 * ���������������� ����
	 */
	void CreateLabels() {

		// ������� ����� ������ � �����
		Skin skin = new Skin();
		skin.addRegions(atlas);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		// ����� �������� �����
		lblBossInfo = new Label(game.GetString("boss_description_"
				+ String.valueOf(6)), labelStyle);
		lblBossInfo.setWrap(true);
		lblBossInfo.setAlignment(Align.center);
		lblBossInfo.setPosition(0, Gdx.graphics.getHeight() / 4);
		lblBossInfo.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight() / 2);
		lblBossInfo.setColor(game.GetTextColor(6));

		// ����� ������� �����
		labelStyle.font = game.GetFont().labelFont;

		// ����� "�� ���������", �� , ��� ������� ���������� ����
		lblLose = new Label("", labelStyle);
		lblLose.setWrap(true);
		lblLose.setColor(game.GetTextColor(6));
		lblLose.setWidth(Gdx.graphics.getWidth());
		lblLose.setPosition(0,
				Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
						- Gdx.graphics.getHeight()
						* RELATIVE_INDENT_OF_BOSS_SAY - lblLose.getHeight());
		lblLose.setAlignment(Align.center);
		lblLose.setText(game.GetString("boss_win_" + String.valueOf(6)));

		// ����� "��� � �� �������"

		lblStart = new Label(game.GetString("press"), labelStyle);
		lblStart.setWrap(true);
		lblStart.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 4);
		lblStart.setWidth(Gdx.graphics.getWidth());
		lblStart.setColor(game.GetTextColor(6));

	}



	void CreateButtons() {
		// ������ �������
		Skin skin = new Skin();
		skin.addRegions(atlas);
		ButtonStyle buttonStyleRestart = new ButtonStyle();
		buttonStyleRestart.up = skin.getDrawable("restart_up");
		buttonStyleRestart.down = skin.getDrawable("restart_down");
		buttonStyleRestart.checked = skin.getDrawable("restart_up");
		btnRestart = new Button(buttonStyleRestart);
		btnRestart.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BUTTON,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BUTTON);
		btnRestart.setCenterPosition(Gdx.graphics.getWidth() / 2,
				btnRestart.getHeight() / 2);

		btnRestart.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnRestart.getWidth()) && (y < btnRestart.getHeight())
						&& (x > 0) && (y > 0)) {
					// ���������� ������
					PrepareForGameNotStarted();
				}
			};
		});

		// ������ "� ����"
		ButtonStyle buttonStyleMenu = new ButtonStyle();
		buttonStyleMenu.up = skin.getDrawable("menu_up");
		buttonStyleMenu.down = skin.getDrawable("menu_down");
		buttonStyleMenu.checked = skin.getDrawable("menu_up");
		btnMenu = new Button(buttonStyleMenu);
		btnMenu.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BUTTON,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BUTTON);
		btnMenu.setCenterPosition(Gdx.graphics.getWidth()
				* RELATIVE_POSITION_OF_FIRST_BUTTON, btnMenu.getHeight() / 2);

		btnMenu.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnMenu.getWidth()) && (y < btnMenu.getHeight())
						&& (x > 0) && (y > 0)) {
					// !!!!!! ������������ � ����
					music.stop();
					game.setScreen(new ChooseFinalScreen(game));
					dispose();
				}

			};
		});

	}



	// ����� ������� ����� �� ����� (� ���� ������ gameStatus ==
	// GAME_NOT_STARTED ��� GAME_IS) �.�. � ������ ������ ���������� �����
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		// ������� ���������
		if ((pointer == 0) && (gameStatus == GAME_NOT_STARTED)) {

			// ��� ����� ���� ��������� ��������, � �� ����� �������
			if (boss != null)
				if (boss.FingerCanBeLose())
					return false;

			// �������� ����, ������� ����� � �������
			gameStatus = GAME_IS;
			finger = new Finger(screenX, Gdx.graphics.getHeight() - screenY);
			boss = new Alpha(finger, atlas);

			UpdateStage();

		}

		return false;

	}



	// ���� ������ �����, � ���� ������ gameStatus == GAME_IS �.�. � ������
	// ������ ���������� �����
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// ������� ���������
		if (pointer == 0) {
			if (gameStatus == GAME_IS) {
				// �� �po������
					PrepareForGameOver();
			}
		}
		return false;
	}



	// ����� ������������ �� ������, ���������� � ������
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// ������� ���������
		if (pointer == 0) {
			if (gameStatus == GAME_IS) {
				finger.x = screenX;
				finger.y = Gdx.graphics.getHeight() - screenY;

				// ��������, �� ����� �� �� �� ������� ������, �� ���������
				// ��������� - ��� ��������
				if ((finger.x < 0) || (finger.y < 0)
						|| (finger.x > Gdx.graphics.getWidth())
						|| (finger.y > Gdx.graphics.getHeight()))
					PrepareForGameOver();
			}
		}
		return false;
	}



	@Override
	public void dispose() {
		stage.dispose();
	}



	// //////////////////////////////
	// ///////////////////////////////

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
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
