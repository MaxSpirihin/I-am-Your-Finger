package com.yourfinger.game.GameScreens;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.ArcadeLevel;
import com.yourfinger.game.ImportantClasses.Monster;
import com.yourfinger.game.UIScreens.ChooseArcadeScreen;

public class GameArcadeScreen implements Screen, InputProcessor {

	// ��������� ������������
	static final String ATLAS_DIRECTORY = "Textures\\Worlds";
	static final String MUSIC_DIRECTORY = "Music";
	private static final float INCREASE_INDEX = 1.1f;
	final float TIME_FOR_INCREASE = 5;

	// ��������� ������� ����
	final int GAME_NOT_STARTED = 0;
	final int GAME_IS = 1;
	final int GAME_OVER = 3;

	// ��������� ���� ���������
	final int FINGER_DEAD = 0;
	final int FINGER_LOST = 1;

	// ��������� ������� � ������� ������ � ��������
	final float RELATIVE_SIZE_OF_BUTTON = 0.25f;
	final float RELATIVE_POSITION_OF_FIRST_BUTTON = 0.2f;
	final float RELATIVE_SIZE_IF_WIN_SPRITE = 1f;
	final float RELATIVE_POSITION_OF_Y_OF_WIN_SPRITE = 0.6f;
	final float RELATIVE_POSITION_OF_LEFT_SIDE_OF_LABEL_LOSE = 0.2f;

	// ����� ��������
	TextureAtlas atlas;
	Music music;

	// �������� �������
	Main game;
	Stage stage;
	List<Monster> monsters;
	Finger finger;
	ArcadeLevel level;

	// ����������
	int gameStatus;
	float time;
	float lastTime;
	int countOfIncrease;

	// ����� ����
	int worldNumber;

	// �������
	Sprite sprBackground;
	Sprite sprDeadFinger;
	Sprite sprStartSkin;

	// ������ � �����
	Button btnRestart;
	Button btnMenu;
	Label lblScore;
	Label lblStart;
	Label lblResult;
	Label lblFinalScore;
	Label lblRecord;
	Label lblInfo;



	public GameArcadeScreen(Main game, int worldNumber) {
		this.game = game;
		this.worldNumber = worldNumber;
		monsters = new LinkedList<Monster>();

		// ��������� �����
		atlas = game.getWorldAtlas(worldNumber);//new TextureAtlas(ATLAS_DIRECTORY + "\\"
				//+ String.valueOf(worldNumber) + ".atlas");

		// ������� �����
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// ��������� �������
		Json json = new Json();
		level = json.fromJson(
				ArcadeLevel.class,
				Gdx.files.internal(
						"Arcade\\" + String.valueOf(worldNumber) + ".txt")
						.readString());

		// ��������� ������
		music = game.getMusic(worldNumber);//Gdx.audio.newMusic(Gdx.files.internal(MUSIC_DIRECTORY + "\\"
				//+ String.valueOf(worldNumber) + ".mp3"));
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

		if (gameStatus == GAME_NOT_STARTED) {
			game.GetBatch().begin();
			sprStartSkin.draw(game.GetBatch());
			game.GetBatch().end();
		}

		// ���������� �������� ������� �� ��������� ����
		switch (gameStatus) {

		case GAME_NOT_STARTED:
			// ���� ��� �� �������� � ������� �������
			stage.draw();
			break;

		case GAME_IS:
			// ���� ����

			// ������ ���������� �� ���� �����
			if (time > lastTime) {
				countOfIncrease++;
				lastTime = time + TIME_FOR_INCREASE * countOfIncrease;
				level.IncreaseDifficulty(INCREASE_INDEX);
			}

			// ������� ��� 
			sprBackground.setY(sprBackground.getY()
					- level.GetBackgroundSpeed() * delta);
			if (sprBackground.getY() + sprBackground.getHeight() < 0)
				sprBackground.setY(sprBackground.getY()
						+ sprBackground.getHeight());

			// ��������� �� ���������� �� ����� � ������� �� ��������
			for (Monster monster : monsters)
				if (monster.FingerIsDead()) {
					PrepareForGameOver(FINGER_DEAD);
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
			lblScore.setText(game.GetString("score") + " "
					+ String.format("%.0f", time * 10));
			time += delta;

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
		stage.addActor(lblScore);
	}
	
	
	//��������� �����
		void UpdateStage()
		{
			stage.clear();
			for (Monster monster : monsters)
				stage.addActor(monster);
			stage.addActor(lblScore);
		}



	// �������������� ����� � ������ � ������ ����� ����
	void PrepareForGameOver(int mode) {

		if (PrefBuilder.VibroOn())
			Gdx.input.vibrate(GameLevelScreen.VIBRO_TIME);

		gameStatus = GAME_OVER;

		// � ����������� �� ���� �� ���������, ������ ������
		if (mode == FINGER_DEAD) {
			sprDeadFinger = new Sprite(atlas.findRegion("dead"));
		} else {
			sprDeadFinger = new Sprite(atlas.findRegion("lost"));
		}
		sprDeadFinger.setSize(Gdx.graphics.getWidth() * 0.5f,
				Gdx.graphics.getWidth() * 0.5f);
		sprDeadFinger.setCenter(finger.x, finger.y);

		// ��������� ����, ������� ���������
		int score = Math.round(time * 10);
		if (score > PrefBuilder.GetArcadeRecord(worldNumber))
			PrefBuilder.SetArcadeRecord(worldNumber, score);
		lblFinalScore.setText(String.valueOf(score));
		lblRecord.setText(game.GetString("record") + " "
				+ String.valueOf(PrefBuilder.GetArcadeRecord(worldNumber)));

		// ������������ ����� ����� �.�. �� ��� ������
		Gdx.input.setInputProcessor(stage);

		// ������� �����
		stage.clear();
		stage.addActor(btnRestart);
		stage.addActor(btnMenu);
		stage.addActor(lblResult);
		stage.addActor(lblRecord);
		stage.addActor(lblFinalScore);
	}



	// �������������� ��������� �����
	void PrepareForGameNotStarted() {

		gameStatus = GAME_OVER;

		// �������� ������
		if ((!music.isPlaying()) && (PrefBuilder.MusicOn()))
			music.play();

		// ������ ���������
		time = 0;
		lastTime = this.TIME_FOR_INCREASE;
		countOfIncrease = 1;
		gameStatus = GAME_NOT_STARTED;

		// ������ �����������
		Gdx.input.setInputProcessor(this);

		// ������� �����
		monsters.clear();
		stage.clear();
		stage.addActor(lblStart);
		stage.addActor(lblInfo);

		level.Start();
		this.CreateAllSprites();
	}



	// ������� ��� ������ �������, ����� ������� ���������, �� ��������� ���
	// ���������
	void CreateAllSprites() {
		// ������� ���
		sprBackground = new Sprite(atlas.findRegion("background",
				level.GetBackgroundIndex()));
		sprBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
				* sprBackground.getHeight() / sprBackground.getWidth());
		sprBackground.setPosition(0, 0);

		// ��������� ���� ����, ���� - 2 �� 1!!!!
		sprStartSkin = new Sprite(atlas.findRegion("arcade"));
		sprStartSkin.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getWidth() / 2);
		sprStartSkin.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - sprStartSkin.getHeight() / 2);

	}



	// ������� ��� ������ � �����, ��������� � ������������, ������� ���
	// ���������������� ����
	void CreateAllButtonsAndLabels() {

		// ������� ����� ������ � �����
		Skin skin = new Skin();
		skin.addRegions(atlas);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		LabelStyle labelStyleScore = new LabelStyle();
		labelStyleScore.font = game.GetFont().scoreFont;

		LabelStyle labelStyleBig = new LabelStyle();
		labelStyleBig.font = game.GetFont().bigLabelFont;

		// ����� ���� � ����
		// �������� ������
		lblInfo = new Label(game.GetString("arcade_"
				+ String.valueOf(worldNumber)), labelStyle);
		lblInfo.setWrap(true);

		lblInfo.setWidth(Gdx.graphics.getWidth());
		lblInfo.setPosition(0, sprStartSkin.getY() - lblInfo.getHeight() * 2);
		lblInfo.setColor(level.GetTextColor());

		// ����� "��� � �� �������"
		labelStyle.font = game.GetFont().labelFont;
		lblStart = new Label(game.GetString("press"), labelStyle);
		lblStart.setWrap(true);
		lblStart.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 4);
		lblStart.setWidth(Gdx.graphics.getWidth());
		lblStart.setColor(level.GetTextColor());

		// ����� �������
		lblScore = new Label("-", labelStyle);
		lblScore.setPosition(0, Gdx.graphics.getHeight() - lblScore.getHeight());
		lblScore.setColor(level.GetTextColor());

		// ����� ��� ����� ����

		lblResult = new Label(game.GetString("score"), labelStyleBig);
		lblResult.setColor(level.GetTextColor());
		lblResult.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() * 0.8f);

		// ����� � ��������� ������, ����� �������� � ���������� � ������
		// ���������
		lblFinalScore = new Label("0", labelStyleScore);
		lblFinalScore.setCenterPosition(Gdx.graphics.getWidth() / 2,
				lblResult.getCenterY() - lblFinalScore.getHeight()
						+ Gdx.graphics.getWidth() * 0.1f);
		lblFinalScore.setColor(level.GetTextColor());
		lblFinalScore.setAlignment(Align.center);

		// ����� � ��������
		lblRecord = new Label("0", labelStyleBig);
		lblRecord.setCenterPosition(Gdx.graphics.getWidth() / 2,
				lblFinalScore.getCenterY() - lblRecord.getHeight()
						- Gdx.graphics.getWidth() * 0.05f);
		lblRecord.setColor(level.GetTextColor());
		lblRecord.setAlignment(Align.center);

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
					game.setScreen(new ChooseArcadeScreen(game));
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



	// //////////////////////////////////////////////////////////////////////

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
