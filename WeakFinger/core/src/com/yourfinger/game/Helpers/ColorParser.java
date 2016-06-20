package com.yourfinger.game.Helpers;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;


/**
 * этот класс парсит из XML-файла цвета текста и текста кнопок для каждого мира 
 */
public class ColorParser {

	public static HashMap<String, String> XMLparseColors() {
		HashMap<String, String> colors = new HashMap<String, String>();

		try {
			Element root = new XmlReader().parse(Gdx.files
					.internal("colors.xml"));
			Array<Element> xml_colors = root.getChildrenByName("string");

			for (Element el : xml_colors) {
				colors.put(el.getAttribute("key"), el.getText());

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return colors;
	}
}
