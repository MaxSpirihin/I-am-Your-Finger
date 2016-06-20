package com.yourfinger.game.ExtraClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.yourfinger.game.ImportantClasses.Constants;

/**
 * содержит экземпляры нужных шрифтов, они инициализируются в конструкторе
 */
public class Font {

	private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗ�ИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
			+ "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

	// Все предоставляемые шрифты и их размеры
	// Размеры для стандартной высоты, они корректируются

	/**
	 * для вывода текста перед уровнем
	 */
	public BitmapFont textFont;
	public final static int TEXT_FONT_SIZE = 35;

	/**
	 * для информационных меток
	 */
	public BitmapFont labelFont;
	public final static int LABEL_FONT_SIZE = 55;

	/**
	 * шрифт в меню
	 */
	public BitmapFont menuFont;
	public final static int MENU_FONT_SIZE = 50;

	/**
	 * результат в аркадном режиме
	 */
	public BitmapFont scoreFont;
	public final static int SCORE_FONT_SIZE = 140;

	/**
	 * если нужна большая метка, напримар рекорд в аркадном режиме
	 */
	public BitmapFont bigLabelFont;
	public final static int BIG_LABEL_FONT_SIZE = 75;



	public Font() {
		// основной шрифт текста
		FreeTypeFontGenerator generatorMain = new FreeTypeFontGenerator(
				Gdx.files.internal("Fonts/russoone.ttf"));

		// шрифты меню
		FreeTypeFontGenerator generatorMenu = new FreeTypeFontGenerator(
				Gdx.files.internal("Fonts/menu.ttf"));

		FreeTypeFontParameter param = new FreeTypeFontParameter();

		// текст
		param.size = Gdx.graphics.getHeight() * TEXT_FONT_SIZE
				/ Constants.STANDART_HEIGHT;
		param.characters = FONT_CHARACTERS;
		textFont = generatorMain.generateFont(param);

		// метки
		param.size = Gdx.graphics.getHeight() * LABEL_FONT_SIZE
				/ Constants.STANDART_HEIGHT;
		param.characters = FONT_CHARACTERS;
		labelFont = generatorMain.generateFont(param);

		//крупные метки
		param.size = Gdx.graphics.getHeight() * BIG_LABEL_FONT_SIZE
				/ Constants.STANDART_HEIGHT;
		param.characters = FONT_CHARACTERS;
		bigLabelFont = generatorMain.generateFont(param);

		// меню
		param.size = Gdx.graphics.getHeight() * MENU_FONT_SIZE
				/ Constants.STANDART_HEIGHT;
		param.characters = FONT_CHARACTERS;
		menuFont = generatorMenu.generateFont(param);

		// очки
		param.size = Gdx.graphics.getHeight() * SCORE_FONT_SIZE
				/ Constants.STANDART_HEIGHT;
		param.characters = FONT_CHARACTERS;
		scoreFont = generatorMain.generateFont(param);
		
		

	}

}
