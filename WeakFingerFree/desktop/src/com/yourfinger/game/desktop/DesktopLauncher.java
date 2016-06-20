package com.yourfinger.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.yourfinger.game.Main;

public class DesktopLauncher {

	// Меняем эти названия для обновления текстур и раскомментироваем строку в
	// коде
	@SuppressWarnings("unused")
	private static final String TEXTURES_DIRECTORY = "..\\android\\assets\\REAL\\Comix\\1_1_ru";
	@SuppressWarnings("unused")
	private static final String ATLAS_DIRECTORY = "..\\android\\assets\\Textures\\Comix";
	@SuppressWarnings("unused")
	private static final String ATLAS_FILE_NAME = "1_1_ru";



	public static void main(String[] arg) {

		TexturePacker.process(TEXTURES_DIRECTORY, ATLAS_DIRECTORY,
		ATLAS_FILE_NAME);

		LwjglApplicationConfiguration conf = new LwjglApplicationConfiguration();
		
		
		conf.width = 540;
		conf.height = 960;
		conf.resizable = false;
 
		new LwjglApplication(new Main(), conf);
	}
}
