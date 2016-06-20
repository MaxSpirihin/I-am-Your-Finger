package com.yourfinger.game.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.yourfinger.game.Main;

public class DesktopLauncher {

	// Меняем эти названия для обновления текстур и раскомментироваем строку в
	// коде
	@SuppressWarnings("unused")
	private static final String TEXTURES_DIRECTORY = "..\\android\\assets\\REAL\\ChooseLevel\\4";
	@SuppressWarnings("unused")
	private static final String ATLAS_DIRECTORY = "..\\android\\assets\\Textures\\ChooseLevel";
	@SuppressWarnings("unused")
	private static final String ATLAS_FILE_NAME = "4";



	public static void main(String[] arg) {

		TexturePacker.process(TEXTURES_DIRECTORY, ATLAS_DIRECTORY,
		ATLAS_FILE_NAME);

		LwjglApplicationConfiguration conf = new LwjglApplicationConfiguration();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		
		conf.height = (int) (screenSize.getHeight()*0.9f);
		conf.width = conf.height * 9/16;
		conf.resizable = false;
 
		new LwjglApplication(new Main(), conf);
	}
}
