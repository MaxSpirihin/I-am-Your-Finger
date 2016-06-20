package com.yourfinger.game.ClassesForMonsters;

import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * определяет путь движения некоего объекта
 */
public interface Path {
	public void SetCoordinates(float delta,Sprite object);
}
