package com.yourfinger.game;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yourfinger.game.ExtraClasses.Font;
import com.yourfinger.game.ExtraUIScreens.ArtsScreen;
import com.yourfinger.game.ExtraUIScreens.AuthorsScreen;
import com.yourfinger.game.ExtraUIScreens.BuyFullScreen;
import com.yourfinger.game.ExtraUIScreens.FactsScreen;
import com.yourfinger.game.ExtraUIScreens.MusicScreen;
import com.yourfinger.game.GameScreens.ComixScreen;
import com.yourfinger.game.GameScreens.FinalScriptScreen;
import com.yourfinger.game.GameScreens.GameAlphaScreen;
import com.yourfinger.game.GameScreens.GameArcadeScreen;
import com.yourfinger.game.GameScreens.GameBossScreen;
import com.yourfinger.game.GameScreens.GameExtraScreen;
import com.yourfinger.game.GameScreens.GameLevelScreen;
import com.yourfinger.game.Helpers.ColorParser;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.Helpers.StringParser;
import com.yourfinger.game.UIScreens.ChooseArcadeScreen;
import com.yourfinger.game.UIScreens.ChooseFinalScreen;
import com.yourfinger.game.UIScreens.ChooseLevelScreen;
import com.yourfinger.game.UIScreens.ExtraScreen;
import com.yourfinger.game.UIScreens.LoadingScreen;
import com.yourfinger.game.UIScreens.MainMenuScreen;
import com.yourfinger.game.UIScreens.OptionsScreen;

//ключевой класс
public class Main extends Game {
	
	public boolean firstDialogNeed = false;

	private final int COMPLETE_WORLDS = 1;

	private SpriteBatch batch;

	private Font font;

	private HashMap<String, String> strings = new HashMap<String, String>();
	private HashMap<String, String> colors = new HashMap<String, String>();

	// ресурсы
	private String locale;
	private Music[] music;
	private TextureAtlas[] worldAtlas;
	private Sound[] winSound;
	private TextureAtlas[] chooseLevelAtlas;
	private TextureAtlas chooseFinal;
	private Music bossMusic;
	private Music finalMusic;
	private Music mainMusic;
	private Music mainMusicForOther;
	private TextureAtlas mainAtlas;

	public AssetManager am = new AssetManager();



	@Override
	public void create() {
		
		if (PrefBuilder.GetDialogCount() == 0)
		{
			PrefBuilder.SetDialogCount(3);
			firstDialogNeed = true;
		}
		else
			PrefBuilder.SetDialogCount(PrefBuilder.GetDialogCount()-1);
		
		
		batch = new SpriteBatch();
		font = new Font();
		locale = java.util.Locale.getDefault().toString().split("_")[0];
		strings = StringParser.XMLparseLangs(locale);
		colors = ColorParser.XMLparseColors();

		setScreen(new LoadingScreen(this));
		// setScreen(new GameLevelScreen(this,3,4));
		// setScreen(new GameBossScreen(this,3));
		// setScreen(new ComixScreen(this,1,1));
		// setScreen(new GameArcadeScreen(this,1));
	}



	/**
	 * стартует загрузку ресурсов
	 */
	public void LoadResources() {
		for (int i = 0; i < COMPLETE_WORLDS; i++) {

			am.load("Textures/Worlds/" + String.valueOf(i + 1) + ".atlas",
					TextureAtlas.class);
			am.load("Textures/ChooseLevel/" + String.valueOf(i + 1) + ".atlas",
					TextureAtlas.class);
			am.load("Music" + "/" + String.valueOf(i + 1) + ".mp3", Music.class);
			am.load("Music" + "/" + String.valueOf(i + 1) + "_win.mp3",
					Sound.class);
			System.out.print(i);

		}

		am.load("Textures/MainMenu.atlas", TextureAtlas.class);
		am.load("Music/boss.mp3", Music.class);
		am.load("Music/main.mp3", Music.class);
		am.load("Textures/ChooseFinal.atlas", TextureAtlas.class);
		am.load("Music/final.mp3", Music.class);
	}



	/**
	 * забирает загруженные ресурсы из assetManagera
	 */
	public void CreateResources() {
		am.finishLoading();

		worldAtlas = new TextureAtlas[COMPLETE_WORLDS];
		music = new Music[COMPLETE_WORLDS];
		winSound = new Sound[COMPLETE_WORLDS];
		chooseLevelAtlas = new TextureAtlas[COMPLETE_WORLDS];

		bossMusic = am.get("Music/boss.mp3", Music.class);
		finalMusic = am.get("Music/final.mp3", Music.class);
		mainMusic = am.get("Music/main.mp3", Music.class);
		mainMusicForOther = am.get("Music/main.mp3", Music.class);
		mainMusic.setLooping(true);

		mainAtlas = am.get("Textures/MainMenu.atlas", TextureAtlas.class);
		
		for (int i = 0; i < COMPLETE_WORLDS; i++) {
			worldAtlas[i] = am.get("Textures/Worlds/" + String.valueOf(i + 1)
					+ ".atlas", TextureAtlas.class);
			chooseLevelAtlas[i] = am.get(
					"Textures/ChooseLevel/" + String.valueOf(i + 1) + ".atlas",
					TextureAtlas.class);
			music[i] = am.get("Music" + "/" + String.valueOf(i + 1) + ".mp3",
					Music.class);
			winSound[i] = am.get("Music" + "/" + String.valueOf(i + 1)
					+ "_win.mp3", Sound.class);
			music[i].setLooping(true);
		}
		
		//для баланса громкостей
		music[0].setVolume(0.95f);
		bossMusic.setVolume(0.7f);

	}



	/**
	 * завершилась ли загрузка ресурсов
	 */
	public boolean LoadHasDone() {
		return am.update();
	}



	/**
	 * прогресс загрузки ресурсов
	 */
	public float getProgress() {
		return am.getProgress();
	}



	public SpriteBatch GetBatch() {
		return batch;
	}



	public Font GetFont() {
		return font;
	}



	public String GetLocale() {
		return locale;
	}



	public String GetString(String key) {
		return strings.get(key);
	}



	public Color GetTextColor(int worldName) {
		return Color.valueOf((colors.get("text_" + String.valueOf(worldName))));
	}



	public Color GetButtonColor(int worldName) {
		return Color
				.valueOf((colors.get("button_" + String.valueOf(worldName))));
	}



	public TextureAtlas getWorldAtlas(int world) {
		return worldAtlas[world - 1];
	}



	public TextureAtlas getChooseAtlas(int world) {
		if (world < 6)
			return chooseLevelAtlas[world - 1];
		else
			return chooseFinal;
	}



	public Music getMusic(int world) {
		return music[world - 1];
	}



	public Sound getWinSound(int world) {
		return winSound[world - 1];
	}



	public Music GetBossMusic() {
		return bossMusic;
	}



	public Music GetMainMusic() {
		return mainMusicForOther;
	}



	public Music GetFinalMusic() {
		return finalMusic;
	}



	public TextureAtlas GetMainAtlas() {
		return mainAtlas;
	}



	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);

		if ((screen.getClass() == MainMenuScreen.class)
				|| (screen.getClass() == ChooseArcadeScreen.class)
				|| (screen.getClass() == ChooseFinalScreen.class)
				|| (screen.getClass() == ChooseLevelScreen.class)
				|| (screen.getClass() == ExtraScreen.class)
				|| (screen.getClass() == OptionsScreen.class)
				|| (screen.getClass() == ArtsScreen.class)
				|| (screen.getClass() == FactsScreen.class)
				|| (screen.getClass() == BuyFullScreen.class)
				|| (screen.getClass() == AuthorsScreen.class)) {

			mainMusic.setVolume(0.17f);
			if ((!mainMusic.isPlaying()) && (PrefBuilder.MusicOn()))
				mainMusic.play();
		}

		if ((screen.getClass() == GameLevelScreen.class)
				|| (screen.getClass() == GameExtraScreen.class)
				|| (screen.getClass() == GameBossScreen.class)
				|| (screen.getClass() == GameArcadeScreen.class)
				|| (screen.getClass() == GameAlphaScreen.class)
				|| (screen.getClass() == ComixScreen.class)
				|| (screen.getClass() == MusicScreen.class))
			
			mainMusic.pause();

		if (screen.getClass() == FinalScriptScreen.class) {
			mainMusic.stop();
			mainMusic.setVolume(1f);
			mainMusic.play();
		}
	}



	public void PlayMainMusic() {
		if (!mainMusic.isPlaying())
			mainMusic.play();
	}



	public void PauseMainMusic() {
		mainMusic.pause();
	}



	@Override
	public void render() {
		super.render(); // important!
	}



	public void dispose() {

		try {
			batch.dispose();

			for (int i = 0; i < 3; i++) {
				music[i].dispose();
				winSound[i].dispose();
				worldAtlas[i].dispose();
				chooseLevelAtlas[i].dispose();
			}

			bossMusic.dispose();
			mainAtlas.dispose();
		} catch (Exception ex) {
		}
	}
}
