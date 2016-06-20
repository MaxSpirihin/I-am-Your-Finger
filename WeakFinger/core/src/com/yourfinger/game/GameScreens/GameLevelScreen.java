package com.yourfinger.game.GameScreens;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.ExtraClasses.Font;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Level;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.UIScreens.ChooseLevelScreen;

/**
 * �������� ����� ����, ������ �� ��������� ������ ������
 */
public class GameLevelScreen implements Screen, InputProcessor {

	// ��������� ������������
	static final String ATLAS_DIRECTORY = "Textures\\Worlds";
	static final String MUSIC_DIRECTORY = "Music";

	// ��������� ������� ����
	final int GAME_NOT_STARTED = 0;
	final int GAME_IS = 1;
	final int GAME_OVER = 3;
	final int GAME_WIN = 4;

	// ��������� ���� ���������
	final int FINGER_DEAD = 0;
	final int FINGER_LOST = 1;

	// ��������� ������� � ������� ������ � ��������
	final float RELATIVE_SIZE_OF_BUTTON = 0.25f;
	final float RELATIVE_POSITION_OF_FIRST_BUTTON = 0.2f;
	final float RELATIVE_SIZE_IF_WIN_SPRITE = 1f;
	final float RELATIVE_POSITION_OF_Y_OF_WIN_SPRITE = 0.6f;
	final float RELATIVE_POSITION_OF_LEFT_SIDE_OF_LABEL_LOSE = 0.2f;

	// ������ ���������
	public final static int VIBRO_TIME = 150;
	final float TIME_FOR_WIN = 0;

	// ����� ��������
	TextureAtlas atlas;
	Music music;
	Sound winSound;

	// �������� �������
	Main game;
	Stage stage;
	List<Monster> monsters;
	Finger finger;
	Level level;

	// ����������
	int gameStatus;
	float time;

	// ������ ���� � ������
	int worldNumber;
	int levelNumber;

	// �������
	Sprite sprBackground;
	Sprite sprDeadFinger;
	Sprite sprWinner;

	// ������ � �����
	Button btnRestart;
	Button btnMenu;
	Button btnNextLevel;
	Label lblLevelInfo;
	Label lblTimer;
	Label lblStart;
	Label lblLose;
	Label lblWin;

	boolean immortal = false;



	// ����������� ��������� ������� � ������� ��� ���������� ��������, ��������
	// ����� ����� ������ �������������� ���
	public GameLevelScreen(Main _game, int _worldNumber, int _levelNumber) {

		game = _game;
		worldNumber = _worldNumber;
		levelNumber = _levelNumber;
		monsters = new LinkedList<Monster>();


		// ��������� �����
		atlas = game.getWorldAtlas(_worldNumber);//new TextureAtlas(ATLAS_DIRECTORY + "\\"
				//+ String.valueOf(worldNumber) + ".atlas");

		// ��������� �������
		Json json = new Json();
		level = json.fromJson(
				Level.class,
				Gdx.files.internal(
						"Levels\\" + String.valueOf(worldNumber) + "\\"
								+ String.valueOf(levelNumber) + ".txt")
						.readString());

		// ������� �����
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// ��������� ������ � ���� ������
		music =game.getMusic(_worldNumber); //Gdx.audio.newMusic(Gdx.files.internal(MUSIC_DIRECTORY + "\\"
				//+ String.valueOf(worldNumber) + ".mp3"));
		winSound = game.getWinSound(_worldNumber);//Gdx.audio.newSound(Gdx.files.internal(MUSIC_DIRECTORY + "\\"
				//+ String.valueOf(worldNumber) + "_win.mp3"));
		music.setLooping(true);

		// ������� �������, ������ � �����
		this.CreateAllSprites();
		this.CreateAllButtonsAndLabels();

		PrepareForGameNotStarted();
	}



	@Override
	public void render(float delta) {

		// ������ ����� � �������������� ���
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		PrintBackground();

		// ���������� �������� ������� �� ��������� ����
		switch (gameStatus) {

		case GAME_NOT_STARTED:
			// ���� ��� �� �������� � ������� �������
			stage.draw();
			break;

		case GAME_IS:
			// ���� ����

			// ������� ��� 
			sprBackground.setY(sprBackground.getY()
					- level.GetBackgroundSpeed() * delta);
			if (sprBackground.getY() + sprBackground.getHeight() < 0)
				sprBackground.setY(sprBackground.getY()
						+ sprBackground.getHeight());

			if (!immortal)
				// ��������� �� ���������� �� ����� � ������� �� ��������
				for (Monster monster : monsters)
					if (monster.FingerIsDead()) {
						PrepareForGameOver(FINGER_DEAD);
						return;// ���������� ������ ��� ��������
					}

			// ��������� �� ������ �� ����� ������
			if (time < TIME_FOR_WIN) {
				PrepareForWin();
				return;// ���������� ������ ��� ��������
			}

			// ��������� �� ���� �� ������� ������ �������
			if (monsters.size() != 0) {
				if (monsters.get(monsters.size() - 1).NextReady())
					CreateNewMonster();
			}

			// ��������� �� ���� �� ������� �������, ������� �������� ���������
			int indexForRemove = -1;
			for (int i = 0; i < monsters.size(); i++) {
				if (!monsters.get(i).IsCurrent())
					indexForRemove = i;
			}
			if (indexForRemove > -1)
			{
				monsters.remove(indexForRemove);
				UpdateStage();
			}

			// ������ ������, ��������� �����
			lblTimer.setText(game.GetString("hold_on") + " "
					+ String.format("%.1f", time));
			time -= delta;

			stage.draw();
			stage.act(Gdx.graphics.getDeltaTime());

			break;

		case GAME_OVER:
			// ����� ���������
			game.GetBatch().begin();
			sprDeadFinger.draw(game.GetBatch());
			game.GetBatch().end();
			stage.draw();
			break;

		case GAME_WIN:
			// ������
			game.GetBatch().begin();
			sprWinner.draw(game.GetBatch());
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
			sprBackground
					.setY(sprBackground.getY() + sprBackground.getHeight()-2);
			sprBackground.draw(game.GetBatch());

		}
		sprBackground.setY(startY);
		game.GetBatch().end();
	}



	// ������� ������ ������� � ������������ �����
	void CreateNewMonster() {

		stage.clear();
		Monster newMonster = level.GetNextMonster(finger, atlas);
		if (newMonster != null)
			monsters.add(newMonster);
		for (Monster monster : monsters)
			stage.addActor(monster);
		stage.addActor(lblTimer);
	}
	
	
	
	//��������� �����
	void UpdateStage()
	{
		stage.clear();
		for (Monster monster : monsters)
			stage.addActor(monster);
		stage.addActor(lblTimer);
	}



	// �������������� ����� � ������ � ������ ����� ����
	void PrepareForGameOver(int mode) {

		if (PrefBuilder.VibroOn())
			Gdx.input.vibrate(GameLevelScreen.VIBRO_TIME);

		gameStatus = GAME_OVER;

		// � ����������� �� ���� �� ���������, ������ ����� � ������
		if (mode == FINGER_DEAD) {
			sprDeadFinger = new Sprite(atlas.findRegion("dead"));
			lblLose.setText(game.GetString("finger_dead"));
		} else {
			sprDeadFinger = new Sprite(atlas.findRegion("lost"));
			lblLose.setText(game.GetString("finger_lost"));
		}
		sprDeadFinger.setSize(Gdx.graphics.getWidth() * 0.5f,
				Gdx.graphics.getWidth() * 0.5f);
		sprDeadFinger.setCenter(finger.x, finger.y);

		// ������������ ����� ����� �.�. �� ��� ������
		Gdx.input.setInputProcessor(stage);

		// ������� �����
		stage.clear();
		stage.addActor(btnRestart);
		stage.addActor(btnMenu);
		stage.addActor(lblLose);
	}



	// ���������� ������ ������
	void PrepareForWin() {
		// ������������� �������� ����
		music.stop();
		if (PrefBuilder.MusicOn())
			winSound.play();

		gameStatus = GAME_WIN;

		// ��������� ��������
		if ((PrefBuilder.GetComleteLevels() < levelNumber)&&(PrefBuilder.GetComleteWorlds()<worldNumber))
			PrefBuilder.SetComleteLevels(levelNumber);

		// ������������ ����� ����� �.�. �� ��� ������
		Gdx.input.setInputProcessor(stage);

		// ������� �����
		stage.clear();
		stage.addActor(btnRestart);
		stage.addActor(btnMenu);
		stage.addActor(btnNextLevel);
		stage.addActor(lblWin);
	}



	// �������������� ��������� �����
	void PrepareForGameNotStarted() {

		gameStatus = GAME_OVER;

		// �������� ������
		if ((!music.isPlaying())&&(PrefBuilder.MusicOn()))
			music.play();

		// ������ ���������
		time = level.GetLevelTime() + level.GetExtraTime();
		gameStatus = GAME_NOT_STARTED;
		level.ResetMonsterCount();

		// ������ �����������
		Gdx.input.setInputProcessor(this);

		// ������� �����
		monsters.clear();
		stage.clear();
		stage.addActor(lblLevelInfo);
		stage.addActor(lblStart);
	}



	// ������� ��� ������ �������, ����� ������� ���������, �� ��������� ���
	// ���������
	void CreateAllSprites() {
		// ������� ���
		sprBackground = new Sprite(atlas.findRegion("background",
				level.GetBackgroundIndex()));
		sprBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
				* sprBackground.getHeight() / sprBackground.getWidth());
		sprBackground.setPosition(0,0);

		// ��������
		sprWinner = new Sprite(atlas.findRegion("winner"));
		sprWinner.setSize(
				Gdx.graphics.getWidth() * RELATIVE_SIZE_IF_WIN_SPRITE,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_IF_WIN_SPRITE);
		sprWinner
				.setCenter(Gdx.graphics.getWidth() / 2,
						Gdx.graphics.getHeight()
								* RELATIVE_POSITION_OF_Y_OF_WIN_SPRITE);

	}



	// ������� ��� ������ � �����, ��������� � ������������, ������� ���
	// ���������������� ����
	void CreateAllButtonsAndLabels() {

		// ������� ����� ������ � �����
		Skin skin = new Skin();
		skin.addRegions(atlas);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		// �������� ������
		lblLevelInfo = new Label("Level "
				+ String.valueOf(worldNumber)
				+ "-"
				+ String.valueOf(levelNumber)
				+ "\n"
				+ game.GetString(String.valueOf(worldNumber) + "_"
						+ String.valueOf(levelNumber)), labelStyle);
		lblLevelInfo.setWrap(true);
		lblLevelInfo.setPosition(0, Gdx.graphics.getHeight() / 2);
		lblLevelInfo.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight() / 2);
		lblLevelInfo.setColor(level.GetTextColor());

		// ����� "��� � �� �������"
		labelStyle.font = game.GetFont().labelFont;
		lblStart = new Label(game.GetString("press"), labelStyle);
		lblStart.setWrap(true);
		lblStart.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 4);
		lblStart.setWidth(Gdx.graphics.getWidth());
		lblStart.setColor(level.GetTextColor());

		// ����� �������
		lblTimer = new Label("-", labelStyle);
		lblTimer.setPosition(0, Gdx.graphics.getHeight() - lblTimer.getHeight());
		lblTimer.setColor(level.GetTextColor());

		// ����� "������� �������"
		lblWin = new Label(game.GetString("level_complete"), labelStyle);
		lblWin.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - Font.LABEL_FONT_SIZE / 2);
		lblWin.setColor(level.GetTextColor());

		// ����� "�� ���������", ����� �������� � ���������� � ������ ���������
		lblLose = new Label("", labelStyle);
		lblLose.setPosition(Gdx.graphics.getWidth()
				* RELATIVE_POSITION_OF_LEFT_SIDE_OF_LABEL_LOSE,
				Gdx.graphics.getHeight() * 3 / 4);
		lblLose.setColor(level.GetTextColor());

		// ������ �������
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
					game.setScreen(new ChooseLevelScreen(game,worldNumber));
					dispose();
				}

			};
		});

		// ������ ���� ������
		ButtonStyle buttonStyleNext = new ButtonStyle();
		buttonStyleNext.up = skin.getDrawable("next_up");
		buttonStyleNext.down = skin.getDrawable("next_down");
		buttonStyleNext.checked = skin.getDrawable("next_up");
		btnNextLevel = new Button(buttonStyleNext);
		btnNextLevel.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BUTTON,
				Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_BUTTON);
		btnNextLevel.setCenterPosition(Gdx.graphics.getWidth()
				* (1 - RELATIVE_POSITION_OF_FIRST_BUTTON),
				btnNextLevel.getHeight() / 2);

		btnNextLevel.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnNextLevel.getWidth())
						&& (y < btnNextLevel.getHeight()) && (x > 0) && (y > 0)) {

					if (levelNumber < ChooseLevelScreen.COUNT_OF_LEVELS) {
						// ��������� ��������� �������, ���� ��� �� ���������
						// ���
						game.setScreen(new GameLevelScreen(game, worldNumber,
								levelNumber + 1));
					} else {
						// !!!!!! ��������� ������ ����� ������
						game.setScreen(new ComixScreen(game, worldNumber,2));
					}
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
		if ((pointer == 0)&&(gameStatus == GAME_NOT_STARTED)) {
			// �������� ����, ������� ����� � �������
			gameStatus = GAME_IS;
			finger = new Finger(screenX, Gdx.graphics.getHeight() - screenY);
			CreateNewMonster();
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
				PrepareForGameOver(FINGER_LOST);
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
					PrepareForGameOver(FINGER_LOST);

			}
		}
		return false;
	}



	@Override
	public void dispose() {
		stage.dispose();
	}



	// ////////////////////////////////////////////////////////////////////////
	// /������, ������� �������� �������������� ���������, �� ������� ��
	// �����./////////////
	// ///////////////////////////////////////
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}



	@Override
	public void hide() {
		// TODO Auto-generated method stub

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



	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

}
