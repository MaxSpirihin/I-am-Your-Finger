package com.yourfinger.game.Helpers;

import com.badlogic.gdx.Gdx;
import com.yourfinger.game.ImportantClasses.Constants;

public class PrefBuilder {

	private final static String PREF_NAME = "YourFinger";
	private final static String COMPLETE_WORLDS = "worlds";
	private final static String COMPLETE_LEVELS = "levels";
	private final static String ARCADE_RECORD = "record";
	private final static String MUSIC_ON = "music";
	private final static String VIBRO_ON = "vibro";
	private final static String IS_FIRST_START = "first_start";
	private final static String DIALOG = "dialog_was";



	/**
	 * возвращает кол-во пройденных уровней в первом не пройденном миреы
	 */
	public static int GetComleteLevels() {
		return Gdx.app.getPreferences(PREF_NAME).getInteger(COMPLETE_LEVELS, 0);
	}



	/**
	 * ставит кол-во пройденных уровней в первом не пройденном миреы
	 */
	public static void SetComleteLevels(int count) {
		Gdx.app.getPreferences(PREF_NAME).putInteger(COMPLETE_LEVELS, count)
				.flush();
	}



	/**
	 * возвращает кол-во пройденных миров
	 */
	public static int GetComleteWorlds() {
		return Gdx.app.getPreferences(PREF_NAME).getInteger(COMPLETE_WORLDS, 0);
	}



	/**
	 * ставит кол-во пройденных миров
	 */
	public static void SetComleteWorlds(int count) {
		Gdx.app.getPreferences(PREF_NAME).putInteger(COMPLETE_WORLDS, count)
				.flush();
	}



	/**
	 * возвращает рекорд аркадного режима в этом мире
	 */
	public static int GetArcadeRecord(int world) {
		return Gdx.app.getPreferences(PREF_NAME).getInteger(
				ARCADE_RECORD + String.valueOf(world), 0);
	}



	/**
	 * ставит рекорд аркадного режима в этом мире
	 */
	public static void SetArcadeRecord(int world, int score) {
		Gdx.app.getPreferences(PREF_NAME)
				.putInteger(ARCADE_RECORD + String.valueOf(world), score)
				.flush();
	}



	/**
	 * возвращает сумму аркадных рекордов
	 */
	public static int GetMainRecord() {
		int sum = 0;
		for (int i = 0; i < Constants.WORLD_COUNT; i++)
			sum += GetArcadeRecord(i);
		return sum;
	}



	public static boolean GetDialogWas(int world) {
		if (world == 1)
			return true;
		else
			return Gdx.app.getPreferences(PREF_NAME).getBoolean(
					DIALOG + String.valueOf(world), false);
	}



	public static void SetDialogWas(int world, boolean dialogWas) {
		Gdx.app.getPreferences(PREF_NAME)
				.putBoolean(DIALOG + String.valueOf(world), dialogWas).flush();
	}



	public static boolean MusicOn() {
		return Gdx.app.getPreferences(PREF_NAME).getBoolean(MUSIC_ON, true);
	}



	public static void SetMusicOn(boolean musicOn) {
		Gdx.app.getPreferences(PREF_NAME).putBoolean(MUSIC_ON, musicOn).flush();
	}



	public static boolean VibroOn() {
		return Gdx.app.getPreferences(PREF_NAME).getBoolean(VIBRO_ON, true);
	}



	public static void SetVibroOn(boolean vibroOn) {
		Gdx.app.getPreferences(PREF_NAME).putBoolean(VIBRO_ON, vibroOn).flush();
	}



	/**
	 * очищает все рекорды во всех мирах в аркаде
	 */
	public static void ClearRecords() {
		for (int i = 0; i < Constants.WORLD_COUNT; i++)
			SetArcadeRecord(i, 0);
	}



	/**
	 * скаидывает всеь прогресс
	 */
	public static void ClearAllProgress() {
		ClearRecords();
		SetComleteLevels(0);
		SetComleteWorlds(0);
		SetIsFirstStart(true);
		for (int i=2;i<7;i++)
			SetDialogWas(i, false);
	}



	/**
	 * показывает первый ли это вход в игру
	 */
	public static boolean IsFirstStart() {
		return Gdx.app.getPreferences(PREF_NAME).getBoolean(IS_FIRST_START,
				true);
	}



	/**
	 * учтанавливает первый ли это вход в игру
	 */
	public static void SetIsFirstStart(boolean isFirst) {
		Gdx.app.getPreferences(PREF_NAME).putBoolean(IS_FIRST_START, isFirst)
				.flush();
	}
	
	
	public static int GetProgress()
	{
		return (GetComleteWorlds()*13 + GetComleteLevels())*100/67;
	}

}
