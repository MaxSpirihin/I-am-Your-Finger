package com.yourfinger.game.GameScreens;

import java.util.LinkedList;
import java.util.List;

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
import com.yourfinger.game.Bosses.Boss1;
import com.yourfinger.game.Bosses.Boss2;
import com.yourfinger.game.Bosses.Boss3;
import com.yourfinger.game.Bosses.Boss4;
import com.yourfinger.game.Bosses.Boss5;
import com.yourfinger.game.ExtraClasses.Finger;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Boss;
import com.yourfinger.game.UIScreens.ChooseLevelScreen;

/**
 * этот экран осуществляет сражение с боссами
 */
public class GameBossScreen implements Screen, InputProcessor {

	// константы расположения
	static final String ATLAS_DIRECTORY = "Textures\\Worlds";
	static final String MUSIC_DIRECTORY = "Music";

	// Константы статуса игры
	final int GAME_NOT_STARTED = 0;
	final int GAME_IS = 1;
	final int GAME_OVER = 3;

	// константы размера и позиций кнопок и спрайтов
	final float RELATIVE_SIZE_OF_BUTTON = 0.25f;
	final float RELATIVE_SIZE_OF_LIVE = 0.1f;
	final float RELATIVE_POSITION_OF_FIRST_BUTTON = 0.2f;
	final float RELATIVE_INDENT_OF_BOSS_SAY = 0.05f;

	final float ANIMATION_BOSS_WIN_TIME = 0.2f;

	// Файлы ресурсов
	TextureAtlas atlas;
	Music music;
	Boss boss;

	// ключевые объекты
	Main game;
	Stage stage;
	Finger finger;
	int lives;
	float time;

	// переменные
	int gameStatus;

	// номер мира
	int worldNumber;

	// Спрайты
	Sprite sprBackground;
	Animation bossWinAnimation;
	Sprite sprBossWin;
	Sprite sprBossStartSkin;
	List<Sprite> sprsLives;

	// Кнопки и метки
	Button btnRestart;
	Button btnMenu;
	Label lblStart;
	Label lblBossInfo;
	Label lblLose;
	Label lblHitHim;
	Label lblBossName;



	// Конструктор загружает ресурсы и создает все экземпляры объектов, позволяя
	// после этого просто манипулировать ими
	public GameBossScreen(Main _game, int _worldNumber) {
		game = _game;
		worldNumber = _worldNumber;
		sprsLives = new LinkedList<Sprite>();
		time = 0;

		// загружаем атлас
		atlas = game.getWorldAtlas(worldNumber);//new TextureAtlas(ATLAS_DIRECTORY + "\\"
				//+ String.valueOf(worldNumber) + ".atlas");

		// создаем сцену
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// загружаем музыку и звук победы
		music = game.GetBossMusic();//Gdx.audio.newMusic(Gdx.files.internal(MUSIC_DIRECTORY
				//+ "\\boss.mp3"));
		music.setLooping(true);

		// создаем спрайты, кнопки и метки
		this.CreateLabels();
		this.CreateButtons();
		this.CreateAllSprites();

		PrepareForGameNotStarted();

	}



	@Override
	public void render(float delta) {

		time += delta;

		// чистим экран и устанавливавем фон
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		PrintBackground();

		// дальнейшие действия зависят от состояния игры
		switch (gameStatus) {

		case GAME_NOT_STARTED:
			// игра еще не началась и ожидает нажатия
			game.GetBatch().begin();
			sprBossStartSkin.draw(game.GetBatch());
			game.GetBatch().end();
			stage.draw();
			break;

		case GAME_IS:
			// игра идет
			stage.draw();

			// рисуем жизни
			game.GetBatch().begin();
			for (Sprite spr : sprsLives)
				spr.draw(game.GetBatch());
			game.GetBatch().end();

			// проверяем можно ли атаковать босса
			if (boss.IsAttacked())
				lblHitHim.setVisible(true);
			else
				lblHitHim.setVisible(false);

			// проверяем не изменилось ли кол-во жизней босса
			if (lives != boss.GetLives())
				UpdateStage();

			// проверяем на смерть
			if (boss.FingerIsDead()) {
				PrepareForGameOver();
				return;
			}

			if (boss.IsDead()) {
				PrepareForWin();
			}

			
			stage.act(Gdx.graphics.getDeltaTime());

			break;

		case GAME_OVER:
			// ЭКРАН ПОРАЖЕНИЯ
			game.GetBatch().begin();
			sprBossWin.setRegion(bossWinAnimation.getKeyFrame(time));
			sprBossWin.draw(game.GetBatch());
			game.GetBatch().end();
			stage.draw();
			break;
		}
	}

	
	
	

	/**
	 * рисует фон
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
	
	
	


	/**
	 * обновляем сцену с целью сменить кол-во жизней
	 */
	void UpdateStage() {
		stage.clear();
		stage.addActor(boss);
		stage.addActor(lblBossName);
		stage.addActor(lblHitHim);
		lblHitHim.setVisible(false);

		// создадим список спрайтов кол-ва жизней и добавим к сцене
		lives = boss.GetLives();
		sprsLives.clear();
		for (int i = 0; i < lives; i++) {
			Sprite spr = new Sprite(atlas.findRegion("boss_live"));
			spr.setSize(Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_LIVE,
					Gdx.graphics.getWidth() * RELATIVE_SIZE_OF_LIVE);
			spr.setCenter(spr.getWidth() * (i + 0.5f), Gdx.graphics.getHeight()
					- lblBossName.getHeight() - spr.getHeight() / 2);
			sprsLives.add(spr);
		}
	}



	// подготовка экрана победы
	void PrepareForWin() {

		// сохраняем прогресс
		if (PrefBuilder.GetComleteWorlds() < worldNumber)
		{
			PrefBuilder.SetComleteWorlds(worldNumber);
			PrefBuilder.SetComleteLevels(0);
		}

		music.stop();

		// идем дальше
		game.setScreen(new ComixScreen(game, worldNumber + 1, 1));
	}



	// подготавливает начальный экран
	void PrepareForGameNotStarted() {

		gameStatus = GAME_OVER;

		// включаем музыку
		if ((!music.isPlaying()) && (PrefBuilder.MusicOn()))
			music.play();

		gameStatus = GAME_NOT_STARTED;

		// ставим обработчика
		Gdx.input.setInputProcessor(this);

		// готовим сцену
		stage.clear();
		stage.addActor(lblStart);
		stage.addActor(lblBossInfo);
	}



	// подготавливает сцену и спрайт к экрану конца игры
	void PrepareForGameOver() {
		
		time = 0;

		if (PrefBuilder.VibroOn())
			Gdx.input.vibrate(GameLevelScreen.VIBRO_TIME);

		gameStatus = GAME_OVER;

		// спрайт
		sprBossWin = new Sprite(atlas.findRegion("boss_win"));

		// добавим анимацию
		Array<TextureRegion> txtrBossWin = new Array<TextureRegion>();
		if (atlas.findRegion("boss_win", 1) != null) {
			// объект анимирован
			int i = 1;
			while (atlas.findRegion("boss_win", i) != null) {
				txtrBossWin.add(atlas.findRegion("boss_win", i));
				i++;
			}
		} else {
			// для объекта одна текстурка
			txtrBossWin.add(atlas.findRegion("boss_win"));
		}
		this.bossWinAnimation = new Animation(ANIMATION_BOSS_WIN_TIME,
				txtrBossWin);
		bossWinAnimation.setPlayMode(PlayMode.LOOP);

		sprBossWin.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		sprBossWin.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 2);

		// обработчиком будет сцена т.к. на ней кнопки
		Gdx.input.setInputProcessor(stage);

		// готовим сцену
		stage.clear();
		stage.addActor(btnRestart);
		stage.addActor(btnMenu);
		stage.addActor(lblLose);
	}



	// Создает все нужные спрайты, кроме спрайта поражения, он создается при
	// поражении
	void CreateAllSprites() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("background"));
		if (worldNumber == 4)
			sprBackground =  new Sprite(atlas.findRegion("background",6));
		
		sprBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth()
				* sprBackground.getHeight() / sprBackground.getWidth());
		sprBackground.setPosition(0, 0);

		// стартовый скин босса, СКИН - КВАДРАТ!!!!
		sprBossStartSkin = new Sprite(atlas.findRegion("boss_skin"));
		sprBossStartSkin.setSize(
				Gdx.graphics.getHeight() / 2 - lblBossInfo.getHeight() / 2,
				Gdx.graphics.getHeight() / 2 - lblBossInfo.getHeight() / 2);
		sprBossStartSkin.setCenter(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - sprBossStartSkin.getHeight() / 2);

	}



	/**
	 * Создает все кнопки и метки, вызывется в конструкторе, вынесен для
	 * структурирования кода
	 */
	void CreateLabels() {

		// создаем стили кнопок и меток
		Skin skin = new Skin();
		skin.addRegions(atlas);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().textFont;

		// метка описание босса
		lblBossInfo = new Label(game.GetString("boss_description_"
				+ String.valueOf(worldNumber)), labelStyle);
		lblBossInfo.setWrap(true);
		lblBossInfo.setPosition(0, Gdx.graphics.getHeight() / 4);
		lblBossInfo.setSize(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight() / 2);
		lblBossInfo.setColor(game.GetTextColor(worldNumber));

		// далее крупный шрифт
		labelStyle.font = game.GetFont().labelFont;

		// метка "Вы пориграли", то , что говорит победивший босс
		lblLose = new Label("", labelStyle);
		lblLose.setWrap(true);
		lblLose.setColor(game.GetTextColor(worldNumber));
		lblLose.setWidth(Gdx.graphics.getWidth());
		lblLose.setPosition(0,
				Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
						- Gdx.graphics.getHeight()
						* RELATIVE_INDENT_OF_BOSS_SAY - lblLose.getHeight());
		lblLose.setAlignment(Align.center);
		lblLose.setText(game.GetString("boss_win_"
				+ String.valueOf(worldNumber)));

		// метка "Жми и не отускай"

		lblStart = new Label(game.GetString("press"), labelStyle);
		lblStart.setWrap(true);
		lblStart.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 4);
		lblStart.setWidth(Gdx.graphics.getWidth());
		lblStart.setColor(game.GetTextColor(worldNumber));

		// метка имя босса
		lblBossName = new Label(game.GetString("boss_name_"
				+ String.valueOf(worldNumber)), labelStyle);
		lblBossName.setCenterPosition(lblBossName.getWidth() / 2,
				Gdx.graphics.getHeight() - lblBossName.getHeight() / 2);
		lblBossName.setColor(game.GetTextColor(worldNumber));

		// метка бей его
		lblHitHim = new Label(game.GetString("hit_him"), labelStyle);
		lblHitHim.setPosition(Gdx.graphics.getWidth() - lblHitHim.getWidth(),
				Gdx.graphics.getHeight() - lblHitHim.getHeight());
		lblHitHim.setColor(game.GetTextColor(worldNumber));

	}
	
	void CreateButtons() {
		// кнопка рестарт
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
					// перезапуск уровня
					PrepareForGameNotStarted();
				}
			};
		});

		// кнопка "в меню"
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
					// !!!!!! возвращаемся в меню
					music.stop();
					game.setScreen(new ChooseLevelScreen(game, worldNumber));
					dispose();
				}

			};
		});

	}



	// игрок опустил палец на экран (в этот момент gameStatus ==
	// GAME_NOT_STARTED или GAME_IS) т.к. в другом случае обработчик сцена
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		// игнорим мультитач
		if ((pointer == 0) && (gameStatus == GAME_NOT_STARTED)) {

			// это когда идет последняя анимация, а мы экран тыркаем
			if (boss != null)
				if (boss.FingerCanBeLose())
					return false;

			// начинаем игру, создаем палец и монстра
			gameStatus = GAME_IS;
			finger = new Finger(screenX, Gdx.graphics.getHeight() - screenY);
			switch (worldNumber) {
			case 1:
				boss = new Boss1(finger, atlas);
				break;
			case 2:
				boss = new Boss2(finger, atlas);
				break;
			case 3:
				boss = new Boss3(finger, atlas);
				break;
			case 4:
				boss = new Boss4(finger, atlas);
				break;
			case 5:
				boss = new Boss5(finger, atlas);
				break;
			}
			UpdateStage();

		}

		return false;

	}



	// юзер поднял палец, в этот момент gameStatus == GAME_IS т.к. в другом
	// случае обработчик сцена
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// игнорим мультитач
		if (pointer == 0) {
			if (gameStatus == GAME_IS) {
				// мы пpoиграли, возможно
				if (!boss.FingerCanBeLose())
					PrepareForGameOver();
			}
		}
		return false;
	}



	// палец перемещается по экрану, переместим и объект
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// игнорим мультитач
		if (pointer == 0) {
			if (gameStatus == GAME_IS) {
				finger.x = screenX;
				finger.y = Gdx.graphics.getHeight() - screenY;

				// проверим, не вышли ли мы за пределы экрана, на некоторых
				// телефонах - это критично
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



	// ///////////////////////////////////////////////////////
	// ///////////////////////////////////////////
	// /////////////////////////////////
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
