package com.yourfinger.game.Helpers;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * класс парсит файл со строками в хэш-карту, все слизано отсюда http://habrahabr.ru/post/224223/
 */
public class StringParser {
	
	/**
	 * получает все строки из файла, как карту
	 * @param lang
	 * @return
	 */
	public static HashMap<String, String> XMLparseLangs(String lang) {
		HashMap<String, String> langs = new HashMap<String, String>();
		try {
			Element root = new XmlReader().parse(Gdx.files.internal("strings.xml"));
			Array<Element> xml_langs = root.getChildrenByName("lang");
			
			for (Element el : xml_langs) {
				if (el.getAttribute("key").equals(lang)) {
					Array<Element> xml_strings = el.getChildrenByName("string");
					for (Element e : xml_strings) {
						langs.put(e.getAttribute("key"), e.getText());
					}
				} else if (el.getAttribute("key").equals("en")) {
					Array<Element> xml_strings = el.getChildrenByName("string");
					for (Element e : xml_strings) {
						langs.put(e.getAttribute("key"), e.getText());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return langs;
	}

}
